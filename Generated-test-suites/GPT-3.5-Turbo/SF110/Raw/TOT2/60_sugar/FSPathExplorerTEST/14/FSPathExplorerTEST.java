import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {
    
    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "C:\\test", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        
        assertNotNull(explorer.rootPath);
        assertEquals("C:\\test", explorer.rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        File rootPath = new File("C:\\test");
        FSPath fsPath = FSPathFactory.newFSPath(rootPath);
        
        assertNotNull(fsPath);
        assertEquals(rootPath, fsPath.getRootDirectory());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPath fsPath = FSPathFactory.newFSPath();
        
        assertNotNull(fsPath);
        assertNull(fsPath.getRootDirectory());
    }
    
    @Test
    public void testStartExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        
        // The start method should exit when the exit command is given
        assertFalse(explorer.prompt.isOpen());
    }
    
    @Test
    public void testStartPwdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("C:\\test");
        explorer.start();
        
        // The start method should display the current directory after the pwd command
        assertEquals("Current directory : C:\\test", explorer.out.toString().trim());
    }
}