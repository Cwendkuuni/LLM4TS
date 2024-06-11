import static org.junit.Assert.*;
import org.junit.*;

public class FSPathExplorerTest {

    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
    }

    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-p", "C:/Users", "-f", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        assertEquals("C:/Users", explorer.rootPath.getPath());
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("C:/Users");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals("C:/Users", explorer.fsPath.getRootDirectory().getPath());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }
}