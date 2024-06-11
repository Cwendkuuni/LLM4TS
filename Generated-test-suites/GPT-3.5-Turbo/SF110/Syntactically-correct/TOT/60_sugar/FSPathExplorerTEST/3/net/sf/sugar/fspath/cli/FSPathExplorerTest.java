import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {
  
  private FSPathExplorer explorer;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  
  @Before
  public void setUpStreams() {
    explorer = new FSPathExplorer();
    explorer.out = new PrintStream(outContent);
    explorer.err = new PrintStream(errContent);
  }
  
  @Test
  public void testMain() {
    String[] args = {"-p", "/path/to/root"};
    FSPathExplorer.main(args);
    assertEquals("Current directory : /path/to/root", outContent.toString().trim());
  }
  
  @Test
  public void testMainNoArgs() {
    FSPathExplorer.main(new String[0]);
    String expectedOutput = "\n+-------------------------------------------------------+\n" +
                            "|                                                       |\n" +
                            "| FSPathExplorer                                        |\n" +
                            "|                                                       |\n" +
                            "+-------------------------------------------------------+\n" +
                            "\nType 'help' for commands...\n" +
                            "\nScanning filesystem metadata...\n" +
                            "\n+-------------------------------------------------------+\n" +
                            "| PLEASE NOTE running FSPathExplorer from the top level |\n" +
                            "| of your filesystem is not advised                     |\n" +
                            "+-------------------------------------------------------+\n";
    assertEquals(expectedOutput, outContent.toString());
  }
  
  @Test
  public void testStartExitCommand() {
    String input = "exit\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    explorer.start();
    assertTrue(outContent.toString().contains("exiting....."));
  }
  
  @Test
  public void testStartPwdCommand() {
    String input = "pwd\nexit\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    explorer.start();
    assertTrue(outContent.toString().contains("Current directory"));
  }
  
  @Test
  public void testStartCdCommand() {
    String input = "cd /path/to/dir\npwd\nexit\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    explorer.start();
    assertTrue(outContent.toString().contains("changed to : /path/to/dir"));
  }
  
  @Test
  public void testStartHelpCommand() {
    String input = "help\nexit\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    explorer.start();
    assertTrue(outContent.toString().contains("FSPathExplorer help :"));
  }
  
  @Test
  public void testStartQueryCommand() {
    String input = "//dir[@name = 'foo']\nexit\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    explorer.fsPath = new FSPathMock();
    explorer.start();
    assertTrue(outContent.toString().contains("test/path/to/foo"));
  }
  
  @Test
  public void testConfigureExplorerWithPath() {
    String[] args = {"-path", "/path/to/root"};
    explorer.configureExplorer(args);
    assertEquals(new File("/path/to/root"), explorer.rootPath);
  }
  
  @Test
  public void testConfigureExplorerWithFind() {
    String[] args = {"-find", "//dir[@name = 'foo']"};
    explorer.configureExplorer(args);
    assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
  }
  
  @Test
  public void testConfigureExplorerNoArgs() {
    explorer.configureExplorer(new String[0]);
    assertNull(explorer.rootPath);
    assertEquals("", explorer.fsPathQuery);
  }
  
  @Test
  public void testCreateFSPathWithRootPath() {
    explorer.rootPath = new File("/path/to/root");
    explorer.createFSPath();
    assertEquals("/path/to/root", explorer.fsPath.getRootDirectory().getAbsolutePath());
  }
  
  @Test
  public void testCreateFSPathWithoutRootPath() {
    explorer.createFSPath();
    assertNotNull(explorer.fsPath.getRootDirectory());
  }
  
  class FSPathMock implements FSPath {
    
    @Override
    public FSPathResultList query(String query) {
      FSPathResultList results = new FSPathResultList();
      results.add(new FSPathResult(new File("test/path/to/foo")));
      return results;
    }
    
    // Implement other FSPath methods if needed for testing
  }
  
}