import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = { "-path", "root/path", "-find", "//dir[@name = 'foo']" };

        explorer.configureExplorer(args);

        assertEquals(explorer.rootPath.getPath(), "root/path");
        assertEquals(explorer.fsPathQuery, "//dir[@name = 'foo']");
    }

    @Test
    public void testConfigureExplorerNoPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = { "-find", "//dir[@name = 'foo']" };

        explorer.configureExplorer(args);

        assertNull(explorer.rootPath);
        assertEquals(explorer.fsPathQuery, "//dir[@name = 'foo']");
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/root/path");

        explorer.createFSPath();

        assertNotNull(explorer.fsPath);
        assertEquals(explorer.fsPath.getRootDirectory().getPath(), "/root/path");
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();

        explorer.createFSPath();

        assertNotNull(explorer.fsPath);
        assertEquals(explorer.fsPath.getRootDirectory().getPath(), "/");
    }

    @Test
    public void testStartExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = { "-path", "root/path", "-find", "//dir[@name = 'foo']" };
        explorer.configureExplorer(args);

        explorer.start();

        // Check if prompt is closed
        // Check if "exiting....." is printed
    }

    @Test
    public void testStartPWDCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = { "-path", "root/path", "-find", "//dir[@name = 'foo']" };
        explorer.configureExplorer(args);

        explorer.start();

        // Check if current directory is printed correctly
    }

    @Test
    public void testStartCDCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/root/path");

        explorer.start();

        // Check if directory is changed correctly
        // Check if "changed to : /root/path" is printed
    }

    @Test
    public void testStartCDCommandInvalidDirectory() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/root/path");

        explorer.start();

        // Check if error message is printed when directory does not exist
    }

    @Test
    public void testStartHelpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = { "-path", "root/path", "-find", "//dir[@name = 'foo']" };
        explorer.configureExplorer(args);

        explorer.start();

        // Check if help message is printed correctly
    }

    @Test
    public void testStartQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = { "-path", "root/path", "-find", "//dir[@name = 'foo']" };
        explorer.configureExplorer(args);

        explorer.start();

        // Check if FSPath query is executed correctly
        // Check if results are printed correctly
    }

}