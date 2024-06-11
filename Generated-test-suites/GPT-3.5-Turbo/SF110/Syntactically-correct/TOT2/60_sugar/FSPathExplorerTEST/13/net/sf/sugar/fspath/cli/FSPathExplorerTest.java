package net.sf.sugar.fspath.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;

public class FSPathExplorerTest {

    FSPathExplorer explorer;

    @Before
    public void setup() {
        explorer = new FSPathExplorer();
    }

    @Test
    public void testRootPathNotNull() {
        assertNotNull(explorer.rootPath);
    }

    @Test
    public void testFsPathQueryEmpty() {
        assertEquals("", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithRootPath() {
        String[] args = {"-path", "/path/to/root"};
        explorer.configureExplorer(args);
        assertNotNull(explorer.rootPath);
        assertEquals(new File("/path/to/root"), explorer.rootPath);
    }

    @Test
    public void testConfigureExplorerWithFsPathQuery() {
        String[] args = {"-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStart() {
        // This method interacts with the command line, can't be fully tested
        // Will need manual testing
    }

    @Test
    public void testConfigureExplorerIncorrectRootPath() {
        String[] args = {"-path", "invalid_path"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
    }

    @Test
    public void testConfigureExplorerWithEmptyArgs() {
        String[] args = {};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }
}