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
    public void testConfigureExplorer_withRootPath() {
        String[] args = {"-p", "/path/to/directory", "-f", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals(args[1], explorer.rootPath.getAbsolutePath());
    }
    
    @Test
    public void testConfigureExplorer_withoutRootPath() {
        String[] args = {"-f", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
    }
    
    @Test
    public void testCreateFSPath_withRootPath() {
        explorer.rootPath = new File("/path/to/directory");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals(explorer.rootPath, explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testCreateFSPath_withoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals(System.getProperty("user.dir"), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testStart_exitCommand() {
        explorer.prompt = new Prompt(new ByteArrayInputStream("exit".getBytes()), System.out, System.err);
        explorer.start();
        assertTrue(explorer.prompt.isClosed());
    }
    
    @Test
    public void testStart_pwdCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        explorer.out = new PrintStream(outContent);
        explorer.start();
        String expectedOutput = "Current directory : " + System.getProperty("user.dir") + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testStart_cdCommand_directoryExists() {
        ByteArrayInputStream inContent = new ByteArrayInputStream("cd /path/to/directory".getBytes());
        explorer.prompt = new Prompt(inContent, System.out, System.err);
        explorer.start();
        String expectedOutput = "changed to : /path/to/directory\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals("/path/to/directory", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testStart_cdCommand_directoryDoesNotExist() {
        ByteArrayInputStream inContent = new ByteArrayInputStream("cd /path/to/unknown".getBytes());
        explorer.prompt = new Prompt(inContent, System.out, System.err);
        explorer.start();
        String expectedOutput = "unable to change to /path/to/unknown please make sure that the directory exists and is readable\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(System.getProperty("user.dir"), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testStart_helpCommand() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        explorer.out = new PrintStream(outContent);
        explorer.start();
        // Assert the expected help content in outContent
    }
    
    @Test
    public void testStart_fsPathQuery() {
        ByteArrayInputStream inContent = new ByteArrayInputStream("//dir[@name = 'foo']".getBytes());
        explorer.prompt = new Prompt(inContent, System.out, System.err);
        explorer.fsPath = mock(FSPath.class);
        explorer.start();
        // Assert the expected FSPath query and output in outContent
    }
}