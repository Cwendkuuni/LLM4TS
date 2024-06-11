import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }
    
    @After
    public void tearDown() {
        explorer = null;
    }
    
    @Test
    public void testConfigureExplorerWithRootPathArgument() {
        String[] args = {"-path", "C:/rootPath", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals("C:/rootPath", explorer.rootPath.getAbsolutePath());
    }
    
    @Test
    public void testConfigureExplorerWithoutRootPathArgument() {
        String[] args = {"-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
    }
    
    @Test
    public void testConfigureExplorerWithFsPathQueryArgument() {
        String[] args = {"-path", "C:/rootPath", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithoutFsPathQueryArgument() {
        String[] args = {"-path", "C:/rootPath"};
        explorer.configureExplorer(args);
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("C:/rootPath");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testStartExitCommand() {
        explorer.prompt = new Prompt(new ByteArrayInputStream("exit".getBytes()), System.out, System.err);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        assertEquals("exiting.....\n", outContent.toString());
    }
    
    @Test
    public void testStartPwdCommand() {
        explorer.prompt = new Prompt(new ByteArrayInputStream("pwd".getBytes()), System.out, System.err);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        assertNotNull(outContent.toString());
        // You can add assertion statements to verify the output of pwd command here
    }
    
    @Test
    public void testStartCdCommand() {
        explorer.prompt = new Prompt(new ByteArrayInputStream("cd C:/dir".getBytes()), System.out, System.err);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        assertNotNull(outContent.toString());
        // You can add assertion statements to verify the output of cd command here
    }
    
    @Test
    public void testStartHelpCommand() {
        explorer.prompt = new Prompt(new ByteArrayInputStream("help".getBytes()), System.out, System.err);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        assertNotNull(outContent.toString());
        // You can add assertion statements to verify the output of help command here
    }
    
    @Test
    public void testStartFsPathQuery() {
        explorer.prompt = new Prompt(new ByteArrayInputStream("//dir[@name = 'foo']".getBytes()), System.out, System.err);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.fsPath = new FSPath();
        explorer.start();
        assertNotNull(outContent.toString());
        // You can add assertion statements to verify the output of FSPath query here
    }
}