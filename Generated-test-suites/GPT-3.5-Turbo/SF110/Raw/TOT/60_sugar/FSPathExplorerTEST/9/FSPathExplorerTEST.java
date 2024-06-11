import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

public class FSPathExplorerTest {

    @Test
    public void testConfigureExplorerWithRootPathAndQuery() {
        String[] args = {"-path", "/path/to/root", "-find", "//dir[@name='test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        
        assertNotNull(explorer.rootPath);
        assertNotNull(explorer.fsPathQuery);
        assertEquals("/path/to/root", explorer.rootPath.getPath());
        assertEquals("//dir[@name='test']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithOnlyQuery() {
        String[] args = {"-find", "//dir[@name='test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertNotNull(explorer.fsPathQuery);
        assertEquals("//dir[@name='test']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        File rootPath = new File("/path/to/root");
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertEquals(rootPath, explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertTrue(explorer.fsPath.getRootDirectory().exists());
    }
    
    @Test
    public void testStartWithCommandExit() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() {
                return "exit";
            }
        };
        explorer.start();
        
        assertTrue(explorer.prompt.isClosed());
    }
    
    @Test
    public void testStartWithCommandPWD() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() {
                return "pwd";
            }
        };
        explorer.start();
        
        assertEquals("Current directory : " + explorer.fsPath.getRootDirectory().getAbsolutePath(), explorer.outContent.toString().trim());
    }
    
    @Test
    public void testStartWithCommandCD() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() {
                return "cd /path/to/new/dir";
            }
        };
        explorer.start();
        
        assertEquals("changed to : /path/to/new/dir", explorer.outContent.toString().trim());
    }
    
    @Test
    public void testStartWithCommandHELP() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() {
                return "help";
            }
        };
        explorer.start();
        
        // Verify the help message is displayed
        assertTrue(explorer.outContent.toString().contains("FSPathExplorer help :"));
    }
    
    @Test
    public void testStartWithFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() {
                return "//dir[@name='test']";
            }
        };
        explorer.fsPath = new FSPath("/path/to/root");
        explorer.start();
        
        // Verify the fsPath query results are displayed
        assertTrue(explorer.outContent.toString().contains("result1"));
    }
}