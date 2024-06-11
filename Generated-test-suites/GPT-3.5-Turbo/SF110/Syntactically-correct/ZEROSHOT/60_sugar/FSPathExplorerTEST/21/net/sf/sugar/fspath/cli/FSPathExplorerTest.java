import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(errorOutput));
    }

    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-p", "path/to/root", "-f", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals("path/to/root", explorer.rootPath.getPath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("path/to/root");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals("path/to/root", explorer.fsPath.getRootDirectory().getPath());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals(System.getProperty("user.dir"), explorer.fsPath.getRootDirectory().getPath());
    }

    @Test
    public void testStartCommandExit() {
        FSPathExplorer explorer = new FSPathExplorer();
        String input = "exit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        explorer.start();
        assertEquals("exiting.....\n", output.toString());
    }

    @Test
    public void testStartCommandPwd() {
        FSPathExplorer explorer = new FSPathExplorer();
        String input = "pwd\nexit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        explorer.start();
        assertEquals("Current directory : " + System.getProperty("user.dir") + "\n", output.toString());
    }

    @Test
    public void testStartCommandCdSuccess() {
        FSPathExplorer explorer = new FSPathExplorer();
        String input = "cd path/to/newDir\npwd\nexit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        explorer.start();
        assertEquals("changed to : " + new File("path/to/newDir").getAbsolutePath() + "\n", output.toString());
        assertEquals("Current directory : " + new File("path/to/newDir").getAbsolutePath() + "\n", output.toString());
    }

    @Test
    public void testStartCommandCdFailure() {
        FSPathExplorer explorer = new FSPathExplorer();
        String input = "cd nonExistentDir\nexit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        explorer.start();
        assertTrue(output.toString().contains("unable to change to nonExistentDir"));
    }

    @Test
    public void testStartCommandHelp() {
        FSPathExplorer explorer = new FSPathExplorer();
        String input = "help\nexit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        explorer.start();
        assertTrue(output.toString().contains("FSPathExplorer help"));
    }

    @Test
    public void testStartQueryCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        String input = "//dir[@name = 'foo']\nexit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        explorer.start();
        assertTrue(output.toString().contains("file.getPath()"));
    }
}