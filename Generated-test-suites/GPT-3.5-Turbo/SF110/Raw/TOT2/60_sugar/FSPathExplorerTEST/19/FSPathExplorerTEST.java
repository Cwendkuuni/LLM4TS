import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FSPathExplorerTest {

    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "/some/root/path", "-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        
        assertEquals("/some/root/path", explorer.rootPath.getPath());
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/some/root/path");
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStart_exitCommand() {
        FSPathExplorer explorer = mock(FSPathExplorer.class);
        when(explorer.prompt.readLine()).thenReturn("exit");
        
        explorer.start();
        
        verify(explorer, times(1)).prompt.close();
    }

    @Test
    public void testStart_pwdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/some/root/path");
        
        String expectedOutput = "Current directory : /some/root/path\n";
        
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testStart_cdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        File directory = new File("/some/new/directory");
        
        String[] args = {"cd", "/some/new/directory"};
        
        explorer.configureExplorer(args);
        explorer.start();
        
        assertEquals(directory.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testStart_helpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        
        explorer.start();
        
        assertTrue(outContent.toString().contains("FSPathExplorer help"));
    }

    @Test
    public void testStart_fsPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPathQuery = "//dir[@name = 'test']";
        
        String expectedOutput = "/some/root/path/test";
        
        assertEquals(expectedOutput, outContent.toString());
    }
}