import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(explorer);
    }
    
    @Test
    public void testConfigureExplorerWithRootPath() {
        String[] args = {"-path", "testDirectory", "-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        assertEquals(new File("testDirectory"), explorer.rootPath);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithoutRootPath() {
        String[] args = {"-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("testDirectory");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testStartWithExitCommand() {
        ByteArrayInputStream in = new ByteArrayInputStream("exit".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        explorer.prompt = new Prompt(in, new PrintStream(out), System.err);
        explorer.start();
        assertTrue(out.toString().contains("exiting"));
    }
    
    @Test
    public void testStartWithPWDCommand() {
        ByteArrayInputStream in = new ByteArrayInputStream("pwd".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        explorer.prompt = new Prompt(in, new PrintStream(out), System.err);
        explorer.start();
        assertTrue(out.toString().contains("Current directory"));
    }
    
    @Test
    public void testStartWithCDCommand() {
        ByteArrayInputStream in = new ByteArrayInputStream("cd test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        explorer.prompt = new Prompt(in, new PrintStream(out), System.err);
        explorer.start();
        assertTrue(out.toString().contains("changed to"));
    }
    
    @Test
    public void testStartWithHelpCommand() {
        ByteArrayInputStream in = new ByteArrayInputStream("help".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        explorer.prompt = new Prompt(in, new PrintStream(out), System.err);
        explorer.start();
        assertTrue(out.toString().contains("FSPathExplorer help"));
    }
    
    @Test
    public void testStartWithFSPathQuery() {
        ByteArrayInputStream in = new ByteArrayInputStream("//dir[@name = 'test']".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        explorer.prompt = new Prompt(in, new PrintStream(out), System.err);
        explorer.start();
        assertTrue(out.toString().contains("testDirectory"));
    }
    
}