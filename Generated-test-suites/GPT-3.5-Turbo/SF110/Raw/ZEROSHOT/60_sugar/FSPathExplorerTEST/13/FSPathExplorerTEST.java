import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {

    private FSPathExplorer fsPathExplorer;

    @Before
    public void setUp() {
        fsPathExplorer = new FSPathExplorer();
    }

    @Test
    public void configureExplorer_withPathArgument_setsRootPath() {
        String[] args = {"-path", "/root/path", "-find", "//dir[@name = 'foo']"};

        fsPathExplorer.configureExplorer(args);

        assertEquals(new File("/root/path"), fsPathExplorer.rootPath);
    }

    @Test
    public void configureExplorer_withFindArgument_setsFSPathQuery() {
        String[] args = {"-path", "/root/path", "-find", "//dir[@name = 'foo']"};

        fsPathExplorer.configureExplorer(args);

        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void configureExplorer_withNoArguments_doesNotSetRootPathOrFSPathQuery() {
        String[] args = {};

        fsPathExplorer.configureExplorer(args);

        assertNull(fsPathExplorer.rootPath);
        assertNull(fsPathExplorer.fsPathQuery);
    }

    @Test
    public void createFSPath_withRootPathConfigured_createsFSPathWithRootPath() {
        fsPathExplorer.rootPath = new File("/root/path");

        fsPathExplorer.createFSPath();

        assertEquals("/root/path", fsPathExplorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void createFSPath_withNoRootPathConfigured_createsFSPathWithNullRootPath() {
        fsPathExplorer.rootPath = null;

        fsPathExplorer.createFSPath();

        assertNull(fsPathExplorer.fsPath.getRootDirectory());
    }
}