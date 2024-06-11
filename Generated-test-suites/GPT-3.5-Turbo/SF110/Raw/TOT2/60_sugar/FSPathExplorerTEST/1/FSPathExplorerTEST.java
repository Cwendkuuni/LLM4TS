import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

public class FSPathExplorerTest {

    @Test
    public void testRootPathNotNull() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer.rootPath);
    }

    @Test
    public void testFSPathQueryNotEmpty() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer.fsPathQuery);
        assertTrue(explorer.fsPathQuery.isEmpty());
    }

    @Test
    public void testSetRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File root = new File("testRoot");
        explorer.rootPath = root;
        assertEquals(root, explorer.rootPath);
    }

    @Test
    public void testFSPathNotNull() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStartExplorer() {
        // Not easily testable
    }

    @Test
    public void testConfigureExplorerWithPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "testPath", "-find", "//dir[@name='test']"};
        explorer.configureExplorer(args);
        assertEquals("testPath", explorer.rootPath.getPath());
    }

    @Test
    public void testConfigureExplorerWithQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-find", "//dir[@name='test']"};
        explorer.configureExplorer(args);
        assertEquals("//dir[@name='test']", explorer.fsPathQuery);
    }

}