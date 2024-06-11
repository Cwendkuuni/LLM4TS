package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import java.io.File;
import java.io.PrintStream;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }

    @Test
    public void testConfigureExplorerWithValidPath() {
        String[] args = {"-path", "/Users/testuser/Documents", "-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        assertEquals(new File("/Users/testuser/Documents"), explorer.rootPath);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithNoPath() {
        String[] args = {"-find", "//file[@length > 10000]"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("//file[@length > 10000]", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/Users/testuser/Documents");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

}
