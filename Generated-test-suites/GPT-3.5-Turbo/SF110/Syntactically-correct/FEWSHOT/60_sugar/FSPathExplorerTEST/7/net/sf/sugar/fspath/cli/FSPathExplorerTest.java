package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {
    private FSPathExplorer explorer;
    private PrintStream sysOut;
    private PrintStream sysErr;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    
    @Before
    public void setup() {
        explorer = new FSPathExplorer();
        
        sysOut = System.out;
        sysErr = System.err;
        
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    
    @Test
    public void testConfigureExplorer() {
        String[] args = {"-p", "root", "-f", "query"};
        
        explorer.configureExplorer(args);
        
        File rootPath = explorer.rootPath;
        String fsPathQuery = explorer.fsPathQuery;
        
        assertEquals("root", rootPath.getPath());
        assertEquals("query", fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorer_NullRootPath() {
        String[] args = {"-f", "query"};
        
        explorer.configureExplorer(args);
        
        File rootPath = explorer.rootPath;
        String fsPathQuery = explorer.fsPathQuery;
        
        assertNull(rootPath);
        assertEquals("query", fsPathQuery);
    }
    
    @Test
    public void testCreateFSPath() {
        File rootPath = new File("root");
        
        explorer.rootPath = rootPath;
        
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testCreateFSPath_NullRootPath() {
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testStart_ExitCommand() {
        String input = "exit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        explorer.prompt = new Prompt(in, sysOut, sysErr);
        
        explorer.start();
        
        assertEquals("exiting.....\n", outContent.toString());
    }
    
    @Test
    public void testStart_PwdCommand() {
        String input = "pwd\nexit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        explorer.prompt = new Prompt(in, sysOut, sysErr);
        
        explorer.start();
        
        assertEquals("Current directory : \n", outContent.toString());
    }
    
    @Test
    public void testStart_CdCommand_ValidDirectory() {
        String input = "cd directory\npwd\nexit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        explorer.prompt = new Prompt(in, sysOut, sysErr);
        
        explorer.start();
        
        assertEquals("changed to : directory\nCurrent directory : directory\n", outContent.toString());
    }
    
    @Test
    public void testStart_CdCommand_InvalidDirectory() {
        String input = "cd invalid\ncd invalid2\npwd\nexit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        explorer.prompt = new Prompt(in, sysOut, sysErr);
        
        explorer.start();
        
        assertEquals("unable to change to invalid2 please make sure that the directory exitst and is readable\nCurrent directory : \n", outContent.toString());
    }
    
    @Test
    public void testStart_HelpCommand() {
        String input = "help\nexit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        explorer.prompt = new Prompt(in, sysOut, sysErr);
        
        explorer.start();
        
        String expectedOutput = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\n\nAttributes : (see javadoc for java.io.File for more info)\n\n";
        
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testStart_FSPathQuery() {
        String input = "//dir[@name = 'foo']\nexit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        explorer.prompt = new Prompt(in, sysOut, sysErr);
        explorer.fsPath = new FSPath();
        explorer.fsPath.query = (fsPathQuery) -> {
            FSPathResultList results = new FSPathResultList();
            FSPathResult result = new FSPathResult();
            result.setFile(new File("file/path"));
            results.add(result);
            return results;
        };
        
        explorer.start();
        
        assertEquals("file/path\n", outContent.toString());
    }
}