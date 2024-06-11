import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FSPathExplorerTest {
    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-path", "/root", "-find", "//dir[@name = 'foo']"});
        assertEquals(new File("/root"), explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
        
        explorer.configureExplorer(new String[]{"-p", "/root", "-f", "//dir[@name = 'bar']"});
        assertEquals(new File("/root"), explorer.rootPath);
        assertEquals("//dir[@name = 'bar']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/root");
        explorer.createFSPath();
        assertEquals("/root", explorer.fsPath.getRootDirectory().getAbsolutePath());
        
        FSPathExplorer explorer2 = new FSPathExplorer();
        explorer2.createFSPath();
        assertNull(explorer2.fsPath.getRootDirectory());
    }
    
    @Test
    public void testStart() {
        // TODO: Write test cases for the start method
    }
}