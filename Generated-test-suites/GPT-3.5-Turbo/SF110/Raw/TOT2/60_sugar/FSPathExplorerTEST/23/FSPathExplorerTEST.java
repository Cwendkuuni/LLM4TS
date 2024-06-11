import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FSPathExplorerTest {
    
    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testChangeDirectory() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("testFolder");
        explorer.createFSPath();
        assertEquals("Current directory : " + new File("testFolder").getAbsolutePath(), explorer.prompt.readLine());
    }
    
    @Test
    public void testQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("testFolder");
        explorer.createFSPath();
        explorer.fsPathQuery = "//dir[@name = 'test']";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        assertTrue(outContent.toString().contains("testFolder/test"));
    }
    
}