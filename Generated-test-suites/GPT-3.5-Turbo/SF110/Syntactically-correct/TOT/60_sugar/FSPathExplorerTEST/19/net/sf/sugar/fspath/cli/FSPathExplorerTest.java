import org.junit.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

  private static FSPathExplorer explorer;
  private static File testDirectory;

  @BeforeClass
  public static void setUp() {
    explorer = new FSPathExplorer();
    testDirectory = new File("testDirectory");
    testDirectory.mkdir();
  }

  @AfterClass
  public static void tearDown() {
    testDirectory.delete();
  }

  @Test
  public void testConfigureExplorer() {
    String[] args = {"-p", "testDirectory", "-f", "//dir[@name = 'foo']"};
    explorer.configureExplorer(args);
    Assert.assertEquals(testDirectory, explorer.rootPath);
    Assert.assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
  }

  @Test
  public void testCreateFSPathWithRootPath() {
    explorer.rootPath = testDirectory;
    explorer.createFSPath();
    Assert.assertEquals(testDirectory, explorer.fsPath.getRootDirectory());
  }

  @Test
  public void testCreateFSPathWithoutRootPath() {
    explorer.rootPath = null;
    explorer.createFSPath();
    Assert.assertNotNull(explorer.fsPath.getRootDirectory());
  }

  @Test
  public void testStartWithExitCommand() {
    explorer.prompt = new Prompt(null, null, null) {
      @Override
      public String readLine() {
        return "exit";
      }
    };

    explorer.start();
    Assert.assertEquals("exiting.....\n", explorer.out.toString());
  }

  @Test
  public void testStartWithPwdCommand() {
    explorer.prompt = new Prompt(null, explorer.out, null) {
      @Override
      public String readLine() {
        return "pwd";
      }
    };

    explorer.start();
    Assert.assertEquals("Current directory : " + explorer.fsPath.getRootDirectory().getAbsolutePath() + "\n", explorer.out.toString());
  }

  @Test
  public void testStartWithCdCommand() {
    explorer.prompt = new Prompt(null, explorer.out, null) {
      @Override
      public String readLine() {
        return "cd testDirectory";
      }
    };

    explorer.start();
    Assert.assertEquals("changed to : " + testDirectory.getAbsolutePath() + "\n", explorer.out.toString());
    Assert.assertEquals(testDirectory, explorer.fsPath.getRootDirectory());
  }

  @Test
  public void testStartWithHelpCommand() {
    explorer.prompt = new Prompt(null, explorer.out, null) {
      @Override
      public String readLine() {
        return "help";
      }
    };

    explorer.start();
    Assert.assertTrue(explorer.out.toString().contains("FSPathExplorer help"));
  }

  @Test
  public void testStartWithFSPathQuery() {
    explorer.prompt = new Prompt(null, explorer.out, null) {
      @Override
      public String readLine() {
        return "//dir[@name = 'foo']";
      }
    };

    explorer.fsPath = new FSPath() {
      @Override
      public FSPathResultList query(String expression) {
        FSPathResultList results = new FSPathResultList();
        results.add(new FSPathResult(new File("testDirectory/foo")));
        return results;
      }
    };

    explorer.start();
    Assert.assertEquals("testDirectory/foo\n", explorer.out.toString());
  }

  @Test
  public void testStartWithInvalidExpression() {
    explorer.prompt = new Prompt(null, explorer.out, null) {
      @Override
      public String readLine() {
        return "//@name";
      }
    };

    explorer.start();
    Assert.assertEquals("", explorer.out.toString());
  }

}