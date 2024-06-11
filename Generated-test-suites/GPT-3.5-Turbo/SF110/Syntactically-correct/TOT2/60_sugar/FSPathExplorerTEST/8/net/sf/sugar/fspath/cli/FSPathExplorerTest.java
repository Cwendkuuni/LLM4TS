package net.sf.sugar.fspath.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testRootPathNotNull() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer.rootPath);
    }

    @Test
    public void testFsPathQueryEmpty() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertEquals("", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathNotNull() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStartSuccessful() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        // Manual testing required to validate successful start
    }

    @Test
    public void testConfigureExplorerRootPath() {
        String[] args = {"-p", "testPath"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertEquals("testPath", explorer.rootPath.getPath());
    }

    @Test
    public void testConfigureExplorerFsPathQuery() {
        String[] args = {"-f", "//dir[@name = 'test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerBothParams() {
        String[] args = {"-p", "testPath", "-f", "//dir[@name = 'test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertEquals("testPath", explorer.rootPath.getPath());
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }

    // Additional test cases for various method functionalities and edge cases can be added here


}