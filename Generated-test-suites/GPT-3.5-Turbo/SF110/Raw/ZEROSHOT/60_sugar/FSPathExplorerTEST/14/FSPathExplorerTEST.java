import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;

    private ByteArrayOutputStream consoleOut;
    private PrintStream originalOut;

    private ByteArrayOutputStream consoleErr;
    private PrintStream originalErr;

    private ByteArrayInputStream consoleIn;
    private InputStream originalIn;

    @Before
    public void setUp() {
        this.explorer = new FSPathExplorer();

        this.consoleOut = new ByteArrayOutputStream();
        this.originalOut = System.out;
        System.setOut(new PrintStream(this.consoleOut));

        this.consoleErr = new ByteArrayOutputStream();
        this.originalErr = System.err;
        System.setErr(new PrintStream(this.consoleErr));

        this.originalIn = System.in;
    }

    @After
    public void tearDown() {
        System.setOut(this.originalOut);
        System.setErr(this.originalErr);
        System.setIn(this.originalIn);
    }

    private void setInput(String input) {
        this.consoleIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(this.consoleIn);
        this.explorer.prompt = new Prompt(this.consoleIn, System.out, System.err);
    }

    @Test
    public void testConfigureExplorerWithRootPath() {
        String[] args = {"-path", "/path/to/directory", "-find", "//dir[@name = 'foo']"};
        this.explorer.configureExplorer(args);

        assertEquals(new File("/path/to/directory"), this.explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", this.explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithoutRootPath() {
        String[] args = {"-find", "//dir[@name = 'foo']"};
        this.explorer.configureExplorer(args);

        assertNull(this.explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", this.explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        this.explorer.rootPath = new File("/path/to/directory");
        this.explorer.createFSPath();

        assertEquals(FSPathFactory.newFSPath(new File("/path/to/directory")), this.explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        this.explorer.createFSPath();

        assertEquals(FSPathFactory.newFSPath(), this.explorer.fsPath);
    }

    @Test
    public void testStartWithExitCommand() throws IOException {
        this.setInput("exit");

        this.explorer.start();

        assertEquals("exiting.....", this.consoleOut.toString().trim());
        assertTrue(this.explorer.prompt.isClosed());
    }

    @Test
    public void testStartWithPwdCommand() throws IOException {
        this.setInput("pwd");

        this.explorer.start();

        assertEquals("Current directory : /root/directory", this.consoleOut.toString().trim());
    }

    @Test
    public void testStartWithCdCommand() throws IOException {
        this.setInput("cd /new/directory");

        this.explorer.start();

        assertEquals("changed to : /new/directory", this.consoleOut.toString().trim());
    }

    @Test
    public void testStartWithCdCommandInvalidDirectory() throws IOException {
        this.setInput("cd /invalid/directory");

        this.explorer.start();

        assertEquals("unable to change to /invalid/directory please make sure that the directory exists and is readable", this.consoleOut.toString().trim());
    }

    @Test
    public void testStartWithHelpCommand() throws IOException {
        this.setInput("help");

        this.explorer.start();

        assertTrue(this.consoleOut.toString().contains("FSPathExplorer help :"));
    }

    @Test
    public void testStartWithInvalidCommand() throws IOException {
        this.setInput("invalid");

        this.explorer.start();

        assertTrue(this.consoleOut.toString().contains("invalid is not a valid command"));
    }

    @Test
    public void testStartWithFSPathQuery() throws IOException {
        this.explorer.fsPathQuery = "//dir[@name = 'foo']";
        FSPathResultList results = new FSPathResultList();
        results.add(new FSPathResult(new File("/path/to/directory/foo")));
        results.add(new FSPathResult(new File("/path/to/directory/bar")));
        this.explorer.fsPath = new FSPath() {
            public FSPathResultList query(String query) {
                return results;
            }
        };

        this.setInput("//dir[@name = 'foo']");

        this.explorer.start();

        assertEquals("/path/to/directory/foo\n/path/to/directory/bar", this.consoleOut.toString().trim());
    }
}