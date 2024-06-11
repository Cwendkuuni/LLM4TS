package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {
    private FSPathExplorer explorer;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }
    
    @Test
    public void testConfigureExplorerWithPathAndQuery() {
        String[] args = {"-path", "rootPath", "-find", "query"};
        explorer.configureExplorer(args);
        
        assertEquals("rootPath", explorer.rootPath.getName());
        assertEquals("query", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithQueryOnly() {
        String[] args = {"-find", "query"};
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertEquals("query", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithRootPathOnly() {
        String[] args = {"-path", "rootPath"};
        explorer.configureExplorer(args);
        
        assertEquals("rootPath", explorer.rootPath.getName());
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("rootPath");
        
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertEquals("rootPath", explorer.fsPath.getRootDirectory().getName());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertNotNull(explorer.fsPath.getRootDirectory());
        assertEquals(System.getProperty("user.dir"), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testStartWithExitCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayInputStream inContent = new ByteArrayInputStream("exit\n".getBytes());
        System.setIn(inContent);
        
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        
        explorer.start();
        
        assertEquals("exiting.....", outContent.toString().trim());
    }
    
    @Test
    public void testStartWithPWDCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayInputStream inContent = new ByteArrayInputStream("pwd\nexit\n".getBytes());
        System.setIn(inContent);
        
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        explorer.fsPath = FSPathFactory.newFSPath(new File("rootPath"));
        
        explorer.start();
        
        assertEquals("Current directory : rootPath", outContent.toString().trim());
    }
    
    @Test
    public void testStartWithCDCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayInputStream inContent = new ByteArrayInputStream("cd newDir\npwd\nexit\n".getBytes());
        System.setIn(inContent);
        
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        explorer.fsPath = FSPathFactory.newFSPath(new File("rootPath"));
        
        explorer.start();
        
        assertEquals("changed to : newDir\nCurrent directory : newDir", outContent.toString().trim());
    }
    
    @Test
    public void testStartWithInvalidCDCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayInputStream inContent = new ByteArrayInputStream("cd invalidDir\nexit\n".getBytes());
        System.setIn(inContent);
        
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        explorer.fsPath = FSPathFactory.newFSPath(new File("rootPath"));
        
        explorer.start();
        
        assertEquals("unable to change to invalidDir please make sure that the directory exitst and a is readable", outContent.toString().trim());
    }
    
    @Test
    public void testStartWithHelpCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayInputStream inContent = new ByteArrayInputStream("help\nexit\n".getBytes());
        System.setIn(inContent);
        
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        
        explorer.start();
        
        assertEquals("FSPathExplorer help : ...", outContent.toString().trim());
    }
    
    @Test
    public void testStartWithFSPathQuery() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayInputStream inContent = new ByteArrayInputStream("//dir[@name = 'foo']\nexit\n".getBytes());
        System.setIn(inContent);
        
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        explorer.fsPath = FSPathFactory.newFSPath(new File("rootPath"));
        
        explorer.start();
        
        assertEquals("rootPath/foo", outContent.toString().trim());
    }
    
    @Test
    public void testStartWithIncompleteCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayInputStream inContent = new ByteArrayInputStream("incomplete\nexit\n".getBytes());
        System.setIn(inContent);
        
        explorer.prompt = new Prompt(System.in, System.out, System.err);
        
        explorer.start();
        
        assertEquals("", outContent.toString().trim());
    }
}