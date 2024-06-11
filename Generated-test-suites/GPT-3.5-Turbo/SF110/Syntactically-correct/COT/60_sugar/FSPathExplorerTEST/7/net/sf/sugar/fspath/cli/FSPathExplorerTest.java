
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {
    
    @Test
    public void testRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertEquals(null, explorer.rootPath);
    }

    @Test
    public void testFsPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertEquals("", explorer.fsPathQuery);
    }

    @Test
    public void testFsPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertEquals(null, explorer.fsPath);
    }

    @Test
    public void testOut() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertEquals(System.out, explorer.out);
    }

    @Test
    public void testErr() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertEquals(System.err, explorer.err);
    }

    @Test
    public void testConfigureExplorerWithPathArgs() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = new String[]{"-path", "/path/to/root"};
        explorer.configureExplorer(args);
        assertEquals(new File("/path/to/root"), explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithFindArgs() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = new String[]{"-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals(null, explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithBothArgs() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = new String[]{"-path", "/path/to/root", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals(new File("/path/to/root"), explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/path/to/root");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals(new File("/path/to/root"), explorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStartExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        try {
            explorer.prompt.setReadLineContent("exit");
            explorer.start();
            assertTrue(explorer.prompt.isClosed());
        } catch (IOException e) {
            fail("IOException occurred while running testStartExitCommand");
        }
    }

    @Test
    public void testStartPwdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        try {
            explorer.prompt.setReadLineContent("pwd");
            explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/root"));
            explorer.start();
            assertEquals("Current directory : /path/to/root", explorer.outContent().trim());
        } catch (IOException e) {
            fail("IOException occurred while running testStartPwdCommand");
        }
    }

    @Test
    public void testStartCdCommandWithValidDirectory() {
        FSPathExplorer explorer = new FSPathExplorer();
        try {
            explorer.prompt.setReadLineContent("cd /path/to/directory");
            explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/root"));
            explorer.start();
            assertEquals("changed to : /path/to/directory", explorer.outContent().trim());
            assertEquals(new File("/path/to/directory"), explorer.fsPath.getRootDirectory());
        } catch (IOException e) {
            fail("IOException occurred while running testStartCdCommandWithValidDirectory");
        }
    }

    @Test
    public void testStartCdCommandWithInvalidDirectory() {
        FSPathExplorer explorer = new FSPathExplorer();
        try {
            explorer.prompt.setReadLineContent("cd /invalid/directory");
            explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/root"));
            explorer.start();
            assertEquals("unable to change to /invalid/directory please make sure that the directory exists and is readable", explorer.outContent().trim());
            assertEquals(new File("/path/to/root"), explorer.fsPath.getRootDirectory());
        } catch (IOException e) {
            fail("IOException occurred while running testStartCdCommandWithInvalidDirectory");
        }
    }

    @Test
    public void testStartHelpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        try {
            explorer.prompt.setReadLineContent("help");
            explorer.start();
            assertTrue(explorer.outContent().contains("FSPathExplorer help"));
        } catch (IOException e) {
            fail("IOException occurred while running testStartHelpCommand");
        }
    }

    @Test
    public void testStartFsPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        try {
            explorer.prompt.setReadLineContent("//dir[@name = 'foo']");
            FSPathResultList results = new FSPathResultList();
            results.add(new FSPathResult(new File("/path/to/foo")));
            explorer.fsPath = Mockito.mock(FSPath.class);
            Mockito.when(explorer.fsPath.query(Mockito.anyString())).thenReturn(results);
            explorer.start();
            assertEquals("/path/to/foo", explorer.outContent().trim());
            Mockito.verify(explorer.fsPath, Mockito.times(1)).query("//dir[@name = 'foo']");
        } catch (IOException e) {
            fail("IOException occurred while running testStartFsPathQuery");
        }
    }
    
    @Test
    public void testStartInvalidCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        try {
            explorer.prompt.setReadLineContent("invalid command");
            explorer.start();
            assertTrue(explorer.outContent().isEmpty());
        } catch (IOException e) {
            fail("IOException occurred while running testStartInvalidCommand");
        }
    }

}
