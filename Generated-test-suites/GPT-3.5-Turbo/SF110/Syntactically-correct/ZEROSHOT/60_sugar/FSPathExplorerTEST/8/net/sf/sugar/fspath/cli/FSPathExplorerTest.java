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
    public void testConfigureExplorerWithRootPathAndQuery() {
        String[] args = {"-path", "/root", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals("/root", explorer.rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithOnlyRootPath() {
        String[] args = {"-path", "/root"};
        explorer.configureExplorer(args);
        assertEquals("/root", explorer.rootPath.getAbsolutePath());
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithOnlyQuery() {
        String[] args = {"-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithNoArgs() {
        String[] args = {};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/root");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals("/root", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertNotNull(explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testStartExitCommand() {
        String[] args = {"-path", "/root"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        // Check if the prompt has been closed
        assertTrue(explorer.prompt.isClosed());
    }
    
    @Test
    public void testStartPwdCommand() {
        String[] args = {"-path", "/root"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        // Check if the output contains the current directory
        assertTrue(explorer.out.toString().contains("/root"));
    }
    
    @Test
    public void testStartCdCommandValidDirectory() {
        String[] args = {"-path", "/root"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        String[] input = {"cd", "/newdir"};
        explorer.prompt.setInput(input);
        explorer.start();
        // Check if the output contains the new directory
        assertTrue(explorer.out.toString().contains("/newdir"));
    }
    
    @Test
    public void testStartCdCommandInvalidDirectory() {
        String[] args = {"-path", "/root"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        String[] input = {"cd", "/nonexistent"};
        explorer.prompt.setInput(input);
        explorer.start();
        // Check if the output contains an error message
        assertTrue(explorer.out.toString().contains("unable to change to /nonexistent"));
    }
    
    @Test
    public void testStartHelpCommand() {
        String[] args = {"-path", "/root"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        String[] input = {"help"};
        explorer.prompt.setInput(input);
        explorer.start();
        // Check if the output contains the help text
        assertTrue(explorer.out.toString().contains("FSPathExplorer help"));
    }
    
    @Test
    public void testStartFSPathQuery() {
        String[] args = {"-path", "/root"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        String[] input = {"//dir[@name = 'foo']"};
        explorer.prompt.setInput(input);
        explorer.start();
        // Check if the output contains the results of the query
        assertTrue(explorer.out.toString().contains("/root/foo"));
    }
}