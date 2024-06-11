import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testRootPathInitialization() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNull(explorer.rootPath);
    }
    
    @Test
    public void testFSPathQueryInitialization() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testFSPathInitialization() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNull(explorer.fsPath);
    }
    
    @Test
    public void testPromptInitialization() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer.prompt);
    }
    
    @Test
    public void testDefaultOutStream() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer.out);
    }
    
    @Test
    public void testDefaultErrStream() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer.err);
    }
    
    @Test
    public void testConfigureExplorerWithArgs() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "somePath", "-find", "someQuery"};
        explorer.configureExplorer(args);
        
        assertNotNull(explorer.rootPath);
        assertEquals("someQuery", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("somePath");
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
    }
}