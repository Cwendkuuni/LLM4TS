package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        
        explorer.out = new PrintStream(outContent);
        explorer.err = new PrintStream(errContent);
    }
    
    @After
    public void tearDown() {
        System.setOut(System.out);
        System.setErr(System.err);
    }
    
    @Test
    public void testConfigureExplorerWithRootPath() {
        String[] args = {"-path", "C:\\Users\\john\\Documents", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        
        File expectedRootPath = new File("C:\\Users\\john\\Documents");
        String expectedFsPathQuery = "//dir[@name = 'foo']";
        
        assertEquals(expectedRootPath.getAbsolutePath(), explorer.rootPath.getAbsolutePath());
        assertEquals(expectedFsPathQuery, explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithoutRootPath() {
        String[] args = {"-find", "//file[contains(name, '.html')]"};
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        
        String expectedFsPathQuery = "//file[contains(name, '.html')]";
        
        assertEquals(expectedFsPathQuery, explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        File rootPath = new File("C:\\Users\\john\\Documents");
        explorer.rootPath = rootPath;
        
        explorer.createFSPath();
        
        assertEquals(rootPath.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testStartWithExitCommand() {
        explorer.prompt = new PromptMock(new String[] {"exit"});
        
        explorer.start();
        
        assertEquals("exiting.....\n", outContent.toString());
    }
    
    @Test
    public void testStartWithPwdCommand() {
        explorer.prompt = new PromptMock(new String[] {"pwd", "exit"});
        
        explorer.start();
        
        assertTrue(outContent.toString().contains("Current directory : "));
    }
    
    @Test
    public void testStartWithCdCommand() {
        explorer.prompt = new PromptMock(new String[] {"cd /path/to/folder", "exit"});
        
        explorer.start();
        
        assertTrue(outContent.toString().contains("changed to : "));
    }
    
    @Test
    public void testStartWithHelpCommand() {
        explorer.prompt = new PromptMock(new String[] {"help", "exit"});
        
        explorer.start();
        
        assertTrue(outContent.toString().contains("FSPathExplorer help :"));
    }
    
    @Test
    public void testStartWithFsPathQuery() {
        explorer.prompt = new PromptMock(new String[] {"//file[contains(name, '.txt')]", "exit"});
        
        explorer.fsPath = new FSPathMock();
        
        explorer.start();
        
        assertTrue(outContent.toString().contains(".txt"));
    }
    
    class PromptMock extends Prompt {
        
        private String[] commands;
        private int currentIndex;
        
        public PromptMock(String[] commands) {
            super(null, null, null);
            this.commands = commands;
            this.currentIndex = 0;
        }
        
        @Override
        public String readLine() {
            String command = commands[currentIndex];
            currentIndex++;
            return command;
        }
    }
    
    class FSPathMock extends FSPath {
        
        @Override
        public FSPathResultList query(String fsPathQuery) {
            FSPathResultList results = new FSPathResultList();
            
            File file1 = new File("C:\\Users\\john\\Documents\\file1.txt");
            FSPathResult result1 = new FSPathResult(file1);
            results.add(result1);
            
            File file2 = new File("C:\\Users\\john\\Documents\\file2.txt");
            FSPathResult result2 = new FSPathResult(file2);
            results.add(result2);
            
            return results;
        }
    }
    
}