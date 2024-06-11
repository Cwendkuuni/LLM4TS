import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }

    @Test
    public void testConfigureExplorer() {
        String[] args = {"-p", "/path/to/root", "-f", "//dir"};
        explorer.configureExplorer(args);
        assertEquals(new File("/path/to/root"), explorer.rootPath);
        assertEquals("//dir", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/path/to/root");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals("/path/to/root", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.rootPath = null;
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertNull(explorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStartWithExitCommand() {
        Commands command = Commands.EXIT;
        explorer.prompt = new Prompt(new ByteArrayInputStream((command.getCommandText() + "\n").getBytes()), System.out, System.err);
        explorer.start();
        assertTrue(explorer.prompt.isClosed());
    }

    @Test
    public void testStartWithPwdCommand() {
        Commands command = Commands.PWD;
        explorer.prompt = new Prompt(new ByteArrayInputStream((command.getCommandText() + "\n").getBytes()), System.out, System.err);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        explorer.out = new PrintStream(outputStream);
        explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/root"));
        explorer.start();
        assertEquals("Current directory : /path/to/root", outputStream.toString().trim());
    }

    @Test
    public void testStartWithCdCommand() {
        Commands command = Commands.CD;
        String argument = "/path/to/new/directory";
        String input = command.getCommandText() + " " + argument;
        explorer.prompt = new Prompt(new ByteArrayInputStream((input + "\n").getBytes()), System.out, System.err);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        explorer.out = new PrintStream(outputStream);
        File newDir = new File(argument);
        explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/root"));
        newDir.mkdirs();
        explorer.start();
        assertEquals("changed to : " + newDir.getAbsolutePath(), outputStream.toString().trim());
        newDir.delete();
    }

    @Test
    public void testStartWithInvalidCdCommand() {
        Commands command = Commands.CD;
        String argument = "/path/to/nonexistent/directory";
        String input = command.getCommandText() + " " + argument;
        explorer.prompt = new Prompt(new ByteArrayInputStream((input + "\n").getBytes()), System.out, System.err);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        explorer.out = new PrintStream(outputStream);
        explorer.start();
        assertEquals("unable to change to " + argument + " please make sure that the directory exists and is readable", outputStream.toString().trim());
    }

    @Test
    public void testStartWithHelpCommand() {
        Commands command = Commands.HELP;
        explorer.prompt = new Prompt(new ByteArrayInputStream((command.getCommandText() + "\n").getBytes()), System.out, System.err);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        explorer.out = new PrintStream(outputStream);
        explorer.start();
        // assert help information is displayed correctly
    }

    @Test
    public void testStartWithFSPathQuery() {
        String expression = "//dir[@name = 'foo']";
        explorer.prompt = new Prompt(new ByteArrayInputStream((expression + "\n").getBytes()), System.out, System.err);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        explorer.out = new PrintStream(outputStream);
        File file = new File("/path/to/root/foo");
        file.mkdirs();
        explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/root"));
        explorer.start();
        assertEquals(file.getPath(), outputStream.toString().trim());
        file.delete();
    }
}