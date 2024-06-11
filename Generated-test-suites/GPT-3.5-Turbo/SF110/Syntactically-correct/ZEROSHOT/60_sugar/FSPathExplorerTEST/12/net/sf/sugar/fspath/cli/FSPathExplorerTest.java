import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

  private FSPathExplorer explorer;

  @Before
  public void setUp() {
    explorer = new FSPathExplorer();
  }

  @Test
  public void testConfigureExplorerWithEmptyArgs() {
    String[] args = new String[]{};
    explorer.configureExplorer(args);
    assertNull(explorer.rootPath);
    assertEquals("", explorer.fsPathQuery);
  }

  @Test
  public void testConfigureExplorerWithValidArgs() {
    String[] args = new String[]{"-p", "testRootPath", "-f", "//dir[@name = 'test']"};
    explorer.configureExplorer(args);
    assertEquals("testRootPath", explorer.rootPath.getPath());
    assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
  }

  @Test
  public void testConfigureExplorerWithInvalidArgs() {
    String[] args = new String[]{"-p", "testRootPath"};
    explorer.configureExplorer(args);
    assertEquals("testRootPath", explorer.rootPath.getPath());
    assertEquals("", explorer.fsPathQuery);
  }

  @Test
  public void testCreateFSPathWithRootPath() {
    explorer.rootPath = new File("testRootPath");
    explorer.createFSPath();
    assertNotNull(explorer.fsPath);
    assertEquals("testRootPath", explorer.fsPath.getRootDirectory().getPath());
  }

  @Test
  public void testCreateFSPathWithoutRootPath() {
    explorer.createFSPath();
    assertNotNull(explorer.fsPath);
    assertEquals(System.getProperty("user.dir"), explorer.fsPath.getRootDirectory().getPath());
  }
}