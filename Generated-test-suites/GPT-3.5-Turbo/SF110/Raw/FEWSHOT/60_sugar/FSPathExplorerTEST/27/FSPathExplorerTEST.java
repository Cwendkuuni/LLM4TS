import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FSPathExplorerTest {
    private FSPathExplorer explorer;
    private ByteArrayOutputStream out;
    private ByteArrayOutputStream err;
    private ByteArrayInputStream in;

    @Before
    public void setup() {
        explorer = new FSPathExplorer();
        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
    }

    @After
    public void cleanup() {
        System.setOut(null);
        System.setErr(null);
        System.setIn(null);
    }

    @Test
    public void testConfigureExplorerWithPathArg() {
        String[] args = {"-path", "/path/to/root", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals(new File("/path/to/root"), explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithPArg() {
        String[] args = {"-p", "/path/to/root", "-f", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals(new File("/path/to/root"), explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/path/to/root");
        explorer.createFSPath();
        assertEquals(FSPathFactory.newFSPath(explorer.rootPath), explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertEquals(FSPathFactory.newFSPath(), explorer.fsPath);
    }

    @Test
    public void testStartExitCommand() {
        String input = "exit";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(out));
        explorer.start();
        assertEquals("exiting.....\n", out.toString());
    }

    @Test
    public void testStartPWDCommand() {
        String input = "pwd\nexit";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(out));
        explorer.start();
        assertEquals("Current directory : /path/to/root\nexiting.....\n", out.toString());
    }

    @Test
    public void testStartCDCommandWithValidDirectory() {
        File newDir = new File("/new/dir");
        String input = "cd /new/dir\npwd\nexit";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(out));
        explorer.start();
        assertEquals("changed to : /new/dir\nCurrent directory : /new/dir\nexiting.....\n", out.toString());
        assertEquals(FSPathFactory.newFSPath(newDir), explorer.fsPath);
    }

    @Test
    public void testStartCDCommandWithInvalidDirectory() {
        String input = "cd /invalid/dir\nexit";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(out));
        explorer.start();
        assertEquals("unable to change to /invalid/dir please make sure that the directory exists and is readable\nexiting.....\n", out.toString());
    }

    @Test
    public void testStartHelpCommand() {
        String input = "help\nexit";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(out));
        explorer.start();
        assertTrue(out.toString().contains("FSPathExplorer help :"));
        assertTrue(out.toString().endsWith("\n"));
    }

    @Test
    public void testStartFSPathQuery() {
        String input = "//dir[@name = 'foo']\nexit";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(out));
        explorer.fsPathQuery = "//dir[@name = 'foo']";
        FSPathResultList results = new FSPathResultList();
        results.add(new FSPathResult(new File("/path/to/root/foo")));
        explorer.fsPath = new FSPath() {
            public FSPathResultList query(String query) {
                return results;
            }
        };
        explorer.start();
        assertEquals("/path/to/root/foo\nexiting.....\n", out.toString());
    }
}