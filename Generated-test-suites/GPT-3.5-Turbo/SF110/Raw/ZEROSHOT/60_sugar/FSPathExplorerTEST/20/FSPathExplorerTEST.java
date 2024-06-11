import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class FSPathExplorerTest {
    
    // Test case for configureExplorer method when both path and query are provided as arguments
    @Test
    public void testConfigureExplorerWithPathAndQuery() {
        String[] args = {"-path", "/path/to/directory", "-find", "//dir[@name = 'foo']"};
        
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        
        assertEquals("/path/to/directory", explorer.rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    // Test case for configureExplorer method when only path is provided as argument
    @Test
    public void testConfigureExplorerWithPath() {
        String[] args = {"-path", "/path/to/directory"};
        
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        
        assertEquals("/path/to/directory", explorer.rootPath.getAbsolutePath());
        assertEquals("", explorer.fsPathQuery);
    }
    
    // Test case for configureExplorer method when only query is provided as argument
    @Test
    public void testConfigureExplorerWithQuery() {
        String[] args = {"-find", "//dir[@name = 'foo']"};
        
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    // Test case for createFSPath method when rootPath is not null
    @Test
    public void testCreateFSPathWithPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/path/to/directory");
        
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertEquals("/path/to/directory", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    // Test case for createFSPath method when rootPath is null
    @Test
    public void testCreateFSPathWithoutPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = null;
        
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertEquals(new File("").getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    // Test case for start method when command is EXIT
    @Test
    public void testStartExitCommand() {
        ByteArrayInputStream in = new ByteArrayInputStream("exit".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printOut = new PrintStream(out);
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(in, printOut, printOut);
        
        explorer.start();
        
        String output = out.toString();
        
        assertFalse(explorer.prompt.isOpen());
        assertTrue(output.contains("exiting....."));
    }
    
    // Test case for start method when command is PWD
    @Test
    public void testStartPwdCommand() {
        ByteArrayInputStream in = new ByteArrayInputStream("pwd".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printOut = new PrintStream(out);
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(in, printOut, printOut);
        
        explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/directory"));
        
        explorer.start();
        
        String output = out.toString();
        
        assertTrue(output.contains("Current directory : /path/to/directory"));
    }
    
    // Test case for start method when command is CD
    @Test
    public void testStartCdCommand() {
        ByteArrayInputStream in = new ByteArrayInputStream("cd /new/path".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printOut = new PrintStream(out);
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(in, printOut, printOut);
        explorer.rootPath = new File("/path/to/directory");
        
        explorer.start();
        
        String output = out.toString();
        
        assertEquals("/new/path", explorer.fsPath.getRootDirectory().getAbsolutePath());
        assertTrue(output.contains("changed to : /new/path"));
    }
    
    // Test case for start method when command is HELP
    @Test
    public void testStartHelpCommand() {
        ByteArrayInputStream in = new ByteArrayInputStream("help".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printOut = new PrintStream(out);
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(in, printOut, printOut);
        
        explorer.start();
        
        String output = out.toString();
        
        assertTrue(output.contains("FSPathExplorer help :"));
    }
    
    // Test case for start method when command is an FSPath query
    @Test
    public void testStartFsPathQuery() {
        ByteArrayInputStream in = new ByteArrayInputStream("//dir[@name = 'foo']".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printOut = new PrintStream(out);
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(in, printOut, printOut);
        
        FSPathResultList results = new FSPathResultList();
        FSPathResult result = new FSPathResult(new File("/path/to/file"));
        results.add(result);
        explorer.fsPath = new FSPath() {
            public FSPathResultList query(String query) {
                return results;
            }
            public File getRootDirectory() {
                return new File("");
            }
        };
        
        explorer.start();
        
        String output = out.toString();
        
        assertTrue(output.contains("/path/to/file"));
    }
    
}