import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

    private FSPathExplorer fsPathExplorer;

    @Before
    public void setUp() {
        fsPathExplorer = new FSPathExplorer();
    }

    @Test
    public void testConfigureExplorerWithNoArgs() {
        String[] args = new String[]{};
        fsPathExplorer.configureExplorer(args);
        assertNull(fsPathExplorer.rootPath);
        assertEquals("", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithPathArg() {
        String[] args = new String[]{"-path", "/root"};
        fsPathExplorer.configureExplorer(args);
        assertEquals(new File("/root"), fsPathExplorer.rootPath);
        assertEquals("", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithFindArg() {
        String[] args = new String[]{"-find", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        assertNull(fsPathExplorer.rootPath);
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithPathAndFindArgs() {
        String[] args = new String[]{"-path", "/root", "-find", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        assertEquals(new File("/root"), fsPathExplorer.rootPath);
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        fsPathExplorer.rootPath = new File("/root");
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath);
        assertEquals(new File("/root"), fsPathExplorer.fsPath.getRootDirectory());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath);
        assertNull(fsPathExplorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStartExitCommand() {
        MockPrompt mockPrompt = new MockPrompt();
        fsPathExplorer.prompt = mockPrompt;
        fsPathExplorer.start();
        assertTrue(mockPrompt.isClosed());
    }

    @Test
    public void testStartPWDCommandWithRootPath() {
        fsPathExplorer.fsPath = new FSPath(new File("/root"));
        MockPrompt mockPrompt = new MockPrompt();
        fsPathExplorer.prompt = mockPrompt;
        fsPathExplorer.start();
        assertEquals("Current directory : /root", mockPrompt.getOutput());
    }

    @Test
    public void testStartPWDCommandWithoutRootPath() {
        MockPrompt mockPrompt = new MockPrompt();
        fsPathExplorer.prompt = mockPrompt;
        fsPathExplorer.start();
        assertEquals("Current directory : null", mockPrompt.getOutput());
    }

    @Test
    public void testStartCDCommandWithExistingDirectory() {
        MockPrompt mockPrompt = new MockPrompt();
        fsPathExplorer.prompt = mockPrompt;
        fsPathExplorer.fsPath = new FSPath(new File("/root"));
        fsPathExplorer.start();
        assertEquals("changed to : /new_dir", mockPrompt.getOutput());
        assertEquals(new File("/new_dir"), fsPathExplorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStartCDCommandWithNonExistingDirectory() {
        MockPrompt mockPrompt = new MockPrompt();
        fsPathExplorer.prompt = mockPrompt;
        fsPathExplorer.fsPath = new FSPath(new File("/root"));
        fsPathExplorer.start();
        assertEquals("unable to change to /non_existing please make sure that the directory exitst and a is readable", mockPrompt.getOutput());
        assertEquals(new File("/root"), fsPathExplorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStartHelpCommand() {
        MockPrompt mockPrompt = new MockPrompt();
        fsPathExplorer.prompt = mockPrompt;
        fsPathExplorer.start();
        assertTrue(mockPrompt.getOutput().contains("FSPathExplorer help :"));
    }

    @Test
    public void testStartQueryCommand() {
        MockPrompt mockPrompt = new MockPrompt();
        fsPathExplorer.prompt = mockPrompt;
        fsPathExplorer.fsPath = new FSPath(new File("/root"));
        fsPathExplorer.fsPathQuery = "//dir[@name = 'foo']";
        fsPathExplorer.start();
        assertTrue(mockPrompt.getOutput().contains("/dir1/dir2/foo"));
    }

    private class MockPrompt extends Prompt {

        private boolean isClosed;
        private String output;

        public MockPrompt() {
            super(null, null, null);
        }

        @Override
        public String readLine() {
            return "exit";
        }

        @Override
        public void close() {
            isClosed = true;
        }

        public boolean isClosed() {
            return isClosed;
        }

        @Override
        public void println(String message) {
            output = message;
        }

        public String getOutput() {
            return output;
        }
    }
}