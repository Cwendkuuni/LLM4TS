import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    private FSPathExplorer explorer;
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        
        explorer = new FSPathExplorer();
    }
    
    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }
    
    @Test
    public void testConfigureExplorerWithNoArgs() {
        String[] args = new String[0];
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithPathArg() {
        String[] args = new String[2];
        args[0] = "-p";
        args[1] = "/path/to/root";
        
        explorer.configureExplorer(args);
        
        assertEquals(new File("/path/to/root"), explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithFindArg() {
        String[] args = new String[2];
        args[0] = "-f";
        args[1] = "//dir[@name = 'foo']";
        
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithPathAndFindArgs() {
        String[] args = new String[4];
        args[0] = "-p";
        args[1] = "/path/to/root";
        args[2] = "-f";
        args[3] = "//dir[@name = 'foo']";
        
        explorer.configureExplorer(args);
        
        assertEquals(new File("/path/to/root"), explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/path/to/root");
        explorer.createFSPath();
        
        assertEquals("/path/to/root", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.rootPath = null;
        explorer.createFSPath();
        
        assertEquals(new File("").getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testStartWithExitCommand() {
        String input = "exit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        explorer.start();
        
        assertEquals("exiting.....\n", outContent.toString());
    }
    
    @Test
    public void testStartWithPwdCommand() {
        String input = "pwd\nexit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        explorer.start();
        
        assertEquals("Current directory : " + new File("").getAbsolutePath() + "\nexiting.....\n", outContent.toString());
    }
    
    @Test
    public void testStartWithCdCommand() {
        String input = "cd /path/to/dir\npwd\nexit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        explorer.start();
        
        assertEquals("changed to : /path/to/dir\nCurrent directory : /path/to/dir\nexiting.....\n", outContent.toString());
    }
    
    @Test
    public void testStartWithHelpCommand() {
        String input = "help\nexit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        explorer.start();
        
        // Verify that help message is printed to console
    }
    
    @Test
    public void testStartWithFSPathQuery() {
        String input = "//dir[@name = 'foo']\nexit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        explorer.start();
        
        // Verify that file paths returned from FSPath query are printed to console
    }
}