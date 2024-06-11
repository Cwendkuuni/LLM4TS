package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private PrintStream originalErr;
    
    @Before
    public void setUpStreams() {
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    @Test
    public void testConfigureExplorer() {
        String[] args1 = {"-path", "directoryPath", "-find", "file"};
        FSPathExplorer explorer1 = new FSPathExplorer();
        explorer1.configureExplorer(args1);
        assertEquals(new File("directoryPath"), explorer1.rootPath);
        assertEquals("file", explorer1.fsPathQuery);
        
        String[] args2 = {"-p", "directoryPath2", "-f", "file2"};
        FSPathExplorer explorer2 = new FSPathExplorer();
        explorer2.configureExplorer(args2);
        assertEquals(new File("directoryPath2"), explorer2.rootPath);
        assertEquals("file2", explorer2.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testStart() throws Exception {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = FSPathFactory.newFSPath();
        
        // Test EXIT command
        String input1 = "exit";
        explorer.prompt = new Prompt(input1);
        explorer.start();
        assertTrue(outContent.toString().contains("exiting....."));
        
        // Test PWD command
        String input2 = "pwd";
        explorer.prompt = new Prompt(input2);
        explorer.start();
        assertTrue(outContent.toString().contains("Current directory : "));
        
        // Test CD command with valid directory
        String input3 = "cd directoryPath";
        explorer.prompt = new Prompt(input3);
        explorer.start();
        assertTrue(outContent.toString().contains("changed to : "));
        
        // Test CD command with invalid directory
        String input4 = "cd invalidDirectoryPath";
        explorer.prompt = new Prompt(input4);
        explorer.start();
        assertTrue(outContent.toString().contains("unable to change to "));
        
        // Test HELP command
        String input5 = "help";
        explorer.prompt = new Prompt(input5);
        explorer.start();
        assertTrue(outContent.toString().contains("FSPathExplorer help"));
        
        // Test FSPath query
        String input6 = "//dir[@name = 'foo']";
        explorer.prompt = new Prompt(input6);
        explorer.start();
        assertTrue(outContent.toString().contains("Path result for query: /directoryPath/foo"));
    }

    @Test
    public void testFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = FSPathFactory.newFSPath();
        String query = "//dir[@name = 'foo']";
        FSPathResultList results = explorer.fsPath.query(query);
        for (FSPathResult result : results) {
            assertTrue(result.getFile().getPath().contains("foo"));
        }
    }
}