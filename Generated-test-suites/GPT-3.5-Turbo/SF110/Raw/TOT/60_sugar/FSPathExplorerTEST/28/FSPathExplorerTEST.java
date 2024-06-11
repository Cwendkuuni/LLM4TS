import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class FSPathExplorerTest {
    
    @Test
    public void testConfigureExplorer_withPathArgument() {
        String[] args = {"-path", "/path/to/root", "-find", "//dir[@name = 'foo']"};
        
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        
        assertEquals(new File("/path/to/root"), explorer.rootPath);
    }
    
    @Test
    public void testConfigureExplorer_withQueryArgument() {
        String[] args = {"-find", "//dir[@name = 'foo']"};
        
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorer_withNoArguments() {
        String[] args = {};
        
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPath_withRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/path/to/root");
        
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertEquals(new File("/path/to/root"), explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testCreateFSPath_withoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertNotNull(explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testStart_withExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(new ByteArrayInputStream("exit".getBytes()), System.out, System.err);
        
        explorer.start();
        
        assertTrue(explorer.prompt.isClosed());
    }
    
    @Test
    public void testStart_withHelpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(new ByteArrayInputStream("help".getBytes()), System.out, System.err);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        explorer.start();
        
        assertTrue(outputStream.toString().contains("FSPathExplorer help :"));
    }
    
    @Test
    public void testStart_withPWDCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(new ByteArrayInputStream("pwd".getBytes()), System.out, System.err);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        explorer.start();
        
        assertTrue(outputStream.toString().contains("Current directory :"));
    }
    
    @Test
    public void testStart_withCDCommand_validDirectory() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(new ByteArrayInputStream("cd /path/to/dir".getBytes()), System.out, System.err);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        explorer.start();
        
        assertTrue(outputStream.toString().contains("changed to :"));
    }
    
    @Test
    public void testStart_withCDCommand_invalidDirectory() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(new ByteArrayInputStream("cd /path/to/nonexistent/dir".getBytes()), System.out, System.err);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        explorer.start();
        
        assertTrue(outputStream.toString().contains("unable to change to"));
    }
    
    @Test
    public void testStart_withFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(new ByteArrayInputStream("//dir[@name = 'foo']".getBytes()), System.out, System.err);
        explorer.fsPath = new FSPath(new File("/path/to/root"));
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        explorer.start();
        
        assertTrue(outputStream.toString().contains("/path/to/result"));
    }
}