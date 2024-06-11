import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testConfigureExplorer() {
        String[] args = { "-p", "/path/to/root", "-f", "//dir[@name = 'foo']" };
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.configureExplorer(args);
        assertEquals("/path/to/root", fsPathExplorer.rootPath.getPath());
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.rootPath = new File("/path/to/root");
        fsPathExplorer.createFSPath();
        assertEquals("/path/to/root", fsPathExplorer.fsPath.getRootDirectory().getPath());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStartExitCommand() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.prompt = new Prompt(null, null, null); // Mock prompt for testing
        fsPathExplorer.prompt.setInput("exit");
        fsPathExplorer.start();
        assertTrue(fsPathExplorer.prompt.isClosed());
    }

    @Test
    public void testStartPwdCommand() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.prompt = new Prompt(null, System.out, null); // Mock prompt for testing
        fsPathExplorer.fsPath = new FSPath(new File("/path/to/root")); // Set root path manually for testing
        fsPathExplorer.prompt.setInput("pwd");
        fsPathExplorer.start();
        assertEquals("Current directory : /path/to/root", fsPathExplorer.prompt.getOutput());
    }

    @Test
    public void testStartCdCommand() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.prompt = new Prompt(null, System.out, null); // Mock prompt for testing
        fsPathExplorer.fsPath = new FSPath(new File("/path/to/root")); // Set root path manually for testing
        fsPathExplorer.prompt.setInput("cd /new/path");
        fsPathExplorer.start();
        assertEquals("changed to : /new/path", fsPathExplorer.prompt.getOutput());
        assertEquals("/new/path", fsPathExplorer.fsPath.getRootDirectory().getPath());
    }

    @Test
    public void testStartHelpCommand() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.prompt = new Prompt(null, System.out, null); // Mock prompt for testing
        fsPathExplorer.prompt.setInput("help");
        fsPathExplorer.start();
        assertNotNull(fsPathExplorer.prompt.getOutput());
    }

    @Test
    public void testStartFsPathQuery() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.prompt = new Prompt(null, System.out, null); // Mock prompt for testing
        fsPathExplorer.fsPath = new FSPath(new File("/path/to/root")); // Set root path manually for testing
        fsPathExplorer.prompt.setInput("//dir[@name = 'foo']");
        fsPathExplorer.start();
        assertEquals("/path/to/root/foo", fsPathExplorer.prompt.getOutput());
    }

}