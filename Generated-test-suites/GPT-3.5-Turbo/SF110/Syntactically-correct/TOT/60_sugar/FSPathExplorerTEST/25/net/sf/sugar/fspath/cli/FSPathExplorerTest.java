import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }

    @Test
    public void testConfigureExplorerWithRootPath() {
        String[] args = {"-path", "/root/path", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        
        assertEquals("/root/path", explorer.rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithoutRootPath() {
        String[] args = {"-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/root/path");
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertEquals("/root/path", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        // The default root path will be the current directory
        assertEquals(System.getProperty("user.dir"), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testStartWithExitCommand() {
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() throws IOException {
                return "exit";
            }
        };
        
        explorer.start();
        assertTrue(explorer.prompt.isClosed());
    }
    
    @Test
    public void testStartWithPWDCommand() {
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() throws IOException {
                return "pwd";
            }
        };
        
        explorer.start();
        // The output should contain the current directory path
        assertTrue(explorer.outContent.toString().contains("Current directory : "));
    }
    
    @Test
    public void testStartWithCDCommandValidDirectory() {
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() throws IOException {
                return "cd /valid/directory";
            }
        };
        
        explorer.start();
        // The output should contain the new directory path
        assertTrue(explorer.outContent.toString().contains("changed to : /valid/directory"));
    }
    
    @Test
    public void testStartWithCDCommandInvalidDirectory() {
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() throws IOException {
                return "cd /invalid/directory";
            }
        };
        
        explorer.start();
        // The output should contain the error message
        assertTrue(explorer.outContent.toString().contains("unable to change to /invalid/directory"));
    }
    
    @Test
    public void testStartWithHelpCommand() {
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() throws IOException {
                return "help";
            }
        };
        
        explorer.start();
        // The output should contain the help information
        assertTrue(explorer.outContent.toString().contains("FSPathExplorer help :"));
    }
    
    @Test
    public void testStartWithFSPathQuery() {
        explorer.fsPathQuery = "//dir[@name = 'foo']";
        explorer.fsPath = new FSPath();
        explorer.fsPath.setRootDirectory(new File("/root/path"));
        
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() throws IOException {
                return "";
            }
        };
        
        explorer.start();
        // The output should contain the results of the FSPath query
        assertTrue(explorer.outContent.toString().contains("/root/path/foo"));
    }
}