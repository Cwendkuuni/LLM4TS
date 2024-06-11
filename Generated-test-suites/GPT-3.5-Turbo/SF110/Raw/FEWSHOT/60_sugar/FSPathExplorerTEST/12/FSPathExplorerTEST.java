import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import static org.junit.Assert.*;

public class FSPathExplorerTest {
    private FSPathExplorer explorer;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testConfigureExplorer() {
        String[] args1 = {"-path", "/some/dir", "-find", "//dir[@name='foo']"};
        explorer.configureExplorer(args1);
        assertEquals(new File("/some/dir"), explorer.rootPath);
        assertEquals("//dir[@name='foo']", explorer.fsPathQuery);
        
        String[] args2 = {"-p", "/another/dir", "-f", "//dir[@name='bar']"};
        explorer.configureExplorer(args2);
        assertEquals(new File("/another/dir"), explorer.rootPath);
        assertEquals("//dir[@name='bar']", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerNoPath() {
        String[] args = {"-find", "//dir[@name='foo']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name='foo']", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerNoQuery() {
        String[] args = {"-path", "/some/dir"};
        explorer.configureExplorer(args);
        assertEquals(new File("/some/dir"), explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        assertNull(explorer.fsPath);
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStartExitCommand() {
        String input = "exit";
        explorer.prompt = new Prompt(new ByteArrayInputStream(input.getBytes()), System.out, System.err);
        explorer.start();
        assertEquals("exiting.....", outContent.toString().trim());
    }

    @Test
    public void testStartPwdCommand() {
        String[] input = {"pwd","exit"};
        explorer.prompt = new Prompt(new ByteArrayInputStream(String.join("\n", input).getBytes()), System.out, System.err);
        explorer.start();
        assertEquals("Current directory : /start/dir", outContent.toString().trim());
    }

    @Test
    public void testStartCdCommandSuccess() {
        String[] input = {"cd /new/dir", "pwd","exit"};
        explorer.prompt = new Prompt(new ByteArrayInputStream(String.join("\n", input).getBytes()), System.out, System.err);
        explorer.start();
        assertEquals("changed to : /new/dir\nCurrent directory : /new/dir", outContent.toString().trim());
    }

    @Test
    public void testStartCdCommandFailure() {
        String[] input = {"cd /nonexistent/dir", "exit"};
        explorer.prompt = new Prompt(new ByteArrayInputStream(String.join("\n", input).getBytes()), System.out, System.err);
        explorer.start();
        assertEquals("unable to change to /nonexistent/dir please make sure that the directory exitst and a is readable", outContent.toString().trim());
    }

    @Test
    public void testStartHelpCommand() {
        String[] input = {"help", "exit"};
        explorer.prompt = new Prompt(new ByteArrayInputStream(String.join("\n", input).getBytes()), System.out, System.err);
        explorer.start();
        assertTrue(outContent.toString().contains("FSPathExplorer help :"));
    }

    @Test
    public void testStartFSPathQuery() {
        String[] input = {"//dir[@name='foo']", "exit"};
        explorer.prompt = new Prompt(new ByteArrayInputStream(String.join("\n", input).getBytes()), System.out, System.err);
        // Mock query method of fsPath and return some results
        explorer.fsPath = new FSPath() {
            public FSPathResultList query(String query) {
                FSPathResultList results = new FSPathResultList();
                results.add(new FSPathResult(new File("/start/dir/foo")));
                results.add(new FSPathResult(new File("/start/dir/bar")));
                return results;
            }
        };
        explorer.start();
        assertTrue(outContent.toString().contains("/start/dir/foo"));
        assertTrue(outContent.toString().contains("/start/dir/bar"));
    }

    @Test
    public void testStartInvalidCommand() {
        String[] input = {"invalid", "exit"};
        explorer.prompt = new Prompt(new ByteArrayInputStream(String.join("\n", input).getBytes()), System.out, System.err);
        explorer.start();
        assertTrue(outContent.toString().contains("invalid command"));
    }
}