import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FSPathExplorerTest {
    
    @Test
    public void testConfigureExplorerWithPathArg() {
        FSPathExplorer explorer = new FSPathExplorer();
        
        String[] args = {"-path", "/path/to/root", "-find", "//dir[@name = 'foo']"};
        
        explorer.configureExplorer(args);
        
        assertEquals("/path/to/root", explorer.rootPath.getPath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithPathShortArg() {
        FSPathExplorer explorer = new FSPathExplorer();
        
        String[] args = {"-p", "/path/to/root", "-f", "//dir[@name = 'foo']"};
        
        explorer.configureExplorer(args);
        
        assertEquals("/path/to/root", explorer.rootPath.getPath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithoutPathArg() {
        FSPathExplorer explorer = new FSPathExplorer();
        
        String[] args = {"-find", "//dir[@name = 'foo']"};
        
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/path/to/root");
        
        explorer.createFSPath();
        
        assertEquals("/path/to/root", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        
        explorer.createFSPath();
        
        assertTrue(explorer.fsPath.getRootDirectory().exists());
        assertTrue(explorer.fsPath.getRootDirectory().isDirectory());
        assertTrue(explorer.fsPath.getRootDirectory().canRead());
    }
    
    @Test
    public void testStartExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(null, null, null);
        
        explorer.start();
        
        assertTrue(explorer.prompt.isClosed());
    }
    
    @Test
    public void testStartPwdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/root"));
        explorer.out = new PrintStream(new ByteArrayOutputStream());
        
        explorer.start();
        
        assertEquals("Current directory : /path/to/root", explorer.out.toString().trim());
    }
    
    @Test
    public void testStartCdCommandValidDir() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/root"));
        explorer.out = new PrintStream(new ByteArrayOutputStream());
        
        explorer.start();
        
        assertEquals("changed to : /path/to/newdir", explorer.out.toString().trim());
        assertEquals("/path/to/newdir", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testStartCdCommandInvalidDir() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/root"));
        explorer.out = new PrintStream(new ByteArrayOutputStream());
        
        File originalDir = explorer.fsPath.getRootDirectory();
        
        explorer.start();
        
        assertEquals("unable to change to /path/to/invalidDir please make sure that the directory exists and is readable",
                explorer.out.toString().trim());
        assertEquals(originalDir.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testStartHelpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.out = new PrintStream(new ByteArrayOutputStream());
        
        explorer.start();
        
        // Check if help text is printed
    }
    
    @Test
    public void testStartFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPathQuery = "//dir[@name = 'foo']";
        explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/root"));
        explorer.out = new PrintStream(new ByteArrayOutputStream());
        
        explorer.start();
        
        // Check if FSPath query results are printed
    }
}