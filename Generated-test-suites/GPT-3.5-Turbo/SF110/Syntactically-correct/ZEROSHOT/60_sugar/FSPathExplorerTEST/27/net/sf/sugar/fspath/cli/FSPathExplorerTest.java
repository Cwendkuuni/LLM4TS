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
  public void testConfigureExplorer() {
    String[] args = new String[]{"-path", "test_path", "-find", "//dir[@name = 'foo']"};
    explorer.configureExplorer(args);
    assertEquals("test_path", explorer.rootPath.getPath());
    assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
  }
  
  @Test
  public void testCreateFSPath() {
    explorer.createFSPath();
    assertNotNull(explorer.fsPath);
  }
  
  @Test
  public void testStart() {
    // TODO: Write test logic for start method
  }
}