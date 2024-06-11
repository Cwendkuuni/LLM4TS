package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        originalOut = System.out;
        originalErr = System.err;
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    
    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testConfigureExplorer() {
        String[] args1 = {"-p", "path", "-f", "FAKE_QUERY"};
        String[] args2 = {"-find", "FAKE_QUERY"};
        String[] args3 = {};
        
        explorer.configureExplorer(args1);
        assertEquals(new File("path"), explorer.rootPath);
        assertEquals("FAKE_QUERY", explorer.fsPathQuery);
        
        explorer.configureExplorer(args2);
        assertNull(explorer.rootPath);
        assertEquals("FAKE_QUERY", explorer.fsPathQuery);
        
        explorer.configureExplorer(args3);
        assertNull(explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        explorer.createFSPath();
        assertEquals(FSPathFactory.newFSPath(), explorer.fsPath);
        
        explorer.rootPath = new File("path");
        explorer.createFSPath();
        assertEquals(FSPathFactory.newFSPath(new File("path")), explorer.fsPath);
    }

    @Test
    public void testStart() throws IOException {
        String promptText = "fspath>";
        String exitCommand = Commands.EXIT.getCommandText();
        String pwdCommand = Commands.PWD.getCommandText();
        String cdCommand = Commands.CD.getCommandText();
        String helpCommand = Commands.HELP.getCommandText();
        
        String query = "//dir[@name = 'foo']";
        FSPathResultList resultList = mock(FSPathResultList.class);
        FSPathResult result = mock(FSPathResult.class);
        File file = new File("path");
        Set<FSPathAttributes> attributes = new HashSet<>(Arrays.asList(FSPathAttributes.LENGTH, FSPathAttributes.SIZE));
        
        when(result.getFile()).thenReturn(file);
        when(result.getAttributes()).thenReturn(attributes);
        when(resultList.iterator()).thenReturn(Arrays.asList(result).iterator());
        when(explorer.fsPath.query(query)).thenReturn(resultList);
        
        // Test exit command
        String input1 = exitCommand;
        System.setIn(new FakeInputStream(input1));
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        explorer.start();
        assertEquals("exiting.....", outContent.toString().trim());
        
        // Test pwd command
        String input2 = pwdCommand + "\n" + exitCommand;
        System.setIn(new FakeInputStream(input2));
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        explorer.start();
        assertEquals("Current directory : " + FSPathFactory.newFSPath().getRootDirectory().getAbsolutePath(), outContent.toString().trim());
        
        // Test cd command
        String input3 = cdCommand + " path\n" + exitCommand;
        System.setIn(new FakeInputStream(input3));
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        explorer.start();
        assertEquals("changed to : " + new File("path").getAbsolutePath(), outContent.toString().trim());
        
        // Test help command
        String input4 = helpCommand + "\n" + exitCommand;
        System.setIn(new FakeInputStream(input4));
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        explorer.start();
        String expectedHelpOutput = "FSPathExplorer help :";
        expectedHelpOutput += "\n\nEnter an FSPath query to search the filesystem\n";
        expectedHelpOutput += "\nExamples :\n";
        expectedHelpOutput += "\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n";
        expectedHelpOutput += "\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n";
        expectedHelpOutput += "   with a file size greater than 10000 bytes\n";
        expectedHelpOutput += "\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n";
        expectedHelpOutput += "\n\nAttributes : (see javadoc for java.io.File for more info)";
        expectedHelpOutput += "\n";
        expectedHelpOutput += "\n@LENGTH";
        expectedHelpOutput += "\n@SIZE";
        expectedHelpOutput += "\n\nCommands :\n";
        expectedHelpOutput += "\n" + exitCommand + "\t: " + Commands.EXIT.getDescription();
        expectedHelpOutput += "\n" + pwdCommand + "\t: " + Commands.PWD.getDescription();
        expectedHelpOutput += "\n" + cdCommand + "\t: " + Commands.CD.getDescription();
        expectedHelpOutput += "\n" + helpCommand + "\t: " + Commands.HELP.getDescription();
        assertEquals(expectedHelpOutput, outContent.toString().trim());
        
        // Test FSPath query
        String input5 = query + "\n" + exitCommand;
        System.setIn(new FakeInputStream(input5));
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        explorer.start();
        assertEquals(file.getPath(), outContent.toString().trim());
        
        // Test invalid input
        String input6 = "invalid" + "\n" + exitCommand;
        System.setIn(new FakeInputStream(input6));
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        explorer.start();
        assertEquals("", outContent.toString().trim());
    }

}