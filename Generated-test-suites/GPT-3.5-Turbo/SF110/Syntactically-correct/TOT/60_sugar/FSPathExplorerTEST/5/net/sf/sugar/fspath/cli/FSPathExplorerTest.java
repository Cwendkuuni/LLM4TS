import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testMain() {
        String[] args = new String[0];
        
        FSPathExplorer.main(args);
        
        String expected = "exiting.....\n";
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testConfigureExplorer() {
        String[] args = {"-path", "testDirectory"};
        
        explorer.configureExplorer(args);
        
        String expected = "testDirectory";
        String actual = explorer.rootPath.getPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("testDirectory");
        
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.rootPath = null;
        
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStartExitCommand() throws IOException {
        String command = "exit";
        inputStream(command);
        
        explorer.start();
        
        String expected = "exiting.....\n";
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testStartPwdCommand() throws IOException {
        String command = "pwd";
        inputStream(command);
        
        explorer.start();
        
        String expected = "Current directory : " + explorer.fsPath.getRootDirectory().getAbsolutePath() + "\n";
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testStartCdCommand() throws IOException {
        String command = "cd testDirectory";
        inputStream(command);
        
        explorer.start();
        
        String expected = "changed to : " + new File("testDirectory").getAbsolutePath() + "\n";
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testStartHelpCommand() throws IOException {
        String command = "help";
        inputStream(command);
        
        explorer.start();
        
        String expected = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\n";
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testStartInvalidCommand() throws IOException {
        String command = "invalid";
        inputStream(command);
        
        explorer.start();
        
        String expected = "";
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testStartInvalidQuery() throws IOException {
        String command = "invalidQuery";
        inputStream(command);
        
        explorer.start();
        
        String expected = "";
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    private void inputStream(String command) {
        ByteArrayInputStream in = new ByteArrayInputStream(command.getBytes());
        System.setIn(in);
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}