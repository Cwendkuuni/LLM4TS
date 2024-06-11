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
    public void testCreateFSPathWithRootPath() {
        String rootPath = "/path/to/root";
        explorer.configureExplorer(new String[]{"-path", rootPath});
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals(rootPath, explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals(System.getProperty("user.dir"), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testStartWithExitCommand() {
        explorer.prompt = new Prompt(new ByteArrayInputStream("exit".getBytes()), System.out, System.err);
        explorer.start();
        // Check if the prompt is closed after executing the "exit" command
        assertTrue(explorer.prompt.isClosed());
    }

    @Test
    public void testStartWithPwdCommand() {
        explorer.prompt = new Prompt(new ByteArrayInputStream("pwd".getBytes()), System.out, System.err);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        // Check if the current directory is printed correctly
        assertEquals("Current directory : " + System.getProperty("user.dir") + "\n", outContent.toString());
    }

    @Test
    public void testStartWithCdCommand() {
        String targetDir = "/path/to/target";
        explorer.prompt = new Prompt(new ByteArrayInputStream(("cd " + targetDir).getBytes()), System.out, System.err);
        ByteArrayInputStream inContent = new ByteArrayInputStream("exit".getBytes());
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setIn(inContent);
        System.setOut(new PrintStream(outContent));
        explorer.start();
        // Check if the FSPath is changed to the target directory
        assertEquals("changed to : " + targetDir + "\n", outContent.toString());
    }
    
    @Test
    public void testStartWithHelpCommand() {
        explorer.prompt = new Prompt(new ByteArrayInputStream("help".getBytes()), System.out, System.err);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        // Check if the help message is printed correctly
        assertTrue(outContent.toString().contains("FSPathExplorer help"));
    }

    @Test
    public void testStartWithQuery() {
        String query = "//dir[@name = 'foo']";
        explorer.prompt = new Prompt(new ByteArrayInputStream(query.getBytes()), System.out, System.err);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.fsPath = Mockito.mock(FSPath.class);
        FSPathResultList results = Mockito.mock(FSPathResultList.class);
        File file1 = new File("/path/to/foo");
        File file2 = new File("/path/to/other/foo");
        Mockito.when(results.getFile(0)).thenReturn(file1);
        Mockito.when(results.getFile(1)).thenReturn(file2);
        Mockito.when(explorer.fsPath.query(query)).thenReturn(results);
        explorer.start();
        // Check if the files returned from the query are printed correctly
        assertTrue(outContent.toString().contains(file1.getPath()));
        assertTrue(outContent.toString().contains(file2.getPath()));
    }

}