package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {
    private FSPathExplorer explorer;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }
    
    @Test
    public void testConfigureExplorerWithPath() {
        String[] args = {"-path", "/path/to/root", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals("/path/to/root", explorer.rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithFind() {
        String[] args = {"-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/path/to/root");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals("/path/to/root", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals(new File("").getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testStartWithExitCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        ByteArrayInputStream inContent = new ByteArrayInputStream("exit\n".getBytes());
        System.setIn(inContent);
        
        explorer.start();
        
        assertTrue(outContent.toString().contains("exiting....."));
    }
    
    @Test
    public void testStartWithPwdCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        ByteArrayInputStream inContent = new ByteArrayInputStream("pwd\nexit\n".getBytes());
        System.setIn(inContent);
        
        explorer.start();
        
        assertTrue(outContent.toString().contains("Current directory : /path/to/root"));
    }
    
    @Test
    public void testStartWithCdCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        ByteArrayInputStream inContent = new ByteArrayInputStream("cd /path/to/newdir\npwd\nexit\n".getBytes());
        System.setIn(inContent);
        
        explorer.start();
        
        assertTrue(outContent.toString().contains("changed to : /path/to/newdir"));
        assertTrue(outContent.toString().contains("Current directory : /path/to/newdir"));
    }
    
    @Test
    public void testStartWithHelpCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        ByteArrayInputStream inContent = new ByteArrayInputStream("help\nexit\n".getBytes());
        System.setIn(inContent);
        
        explorer.start();
        
        assertTrue(outContent.toString().contains("Examples :"));
    }
    
    @Test
    public void testStartWithFSPathQuery() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        ByteArrayInputStream inContent = new ByteArrayInputStream("//dir[@name = 'foo']\nexit\n".getBytes());
        System.setIn(inContent);
        
        explorer.start();
        
        assertTrue(outContent.toString().contains("Path to foo directory"));
    }
}