
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FSPathExplorerTest {

    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
    }

    @Test
    public void testConfigureExplorerWithValidArgs() {
        String[] args = {"-p", "/path/to/dir", "-f", "//dir[@name = 'test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertNotNull(explorer.rootPath);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithMissingPath() {
        String[] args = {"-f", "//dir[@name = 'test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        File testDir = new File("/path/to/dir");
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = testDir;
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStartWithExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        explorer.out = new PrintStream(outputStream);
        String input = "exit";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayInputStream errorStream = new ByteArrayInputStream("".getBytes());
        Prompt prompt = new Prompt(inputStream, explorer.out, new PrintStream(errorStream));
        explorer.prompt = prompt;
        explorer.start();
        assertTrue(outputStream.toString().contains("exiting"));
    }

    @Test
    public void testStartWithPWDCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        explorer.out = new PrintStream(outputStream);
        String input = "pwd";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayInputStream errorStream = new ByteArrayInputStream("".getBytes());
        Prompt prompt = new Prompt(inputStream, explorer.out, new PrintStream(errorStream));
        explorer.prompt = prompt;
        explorer.start();
        assertTrue(outputStream.toString().contains("Current directory"));
    }

    @Test
    public void testStartWithInvalidCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        explorer.out = new PrintStream(outputStream);
        String input = "invalid";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayInputStream errorStream = new ByteArrayInputStream("".getBytes());
        Prompt prompt = new Prompt(inputStream, explorer.out, new PrintStream(errorStream));
        explorer.prompt = prompt;
        explorer.start();
        assertTrue(outputStream.toString().contains("FSPathExplorer help"));
    }
}

