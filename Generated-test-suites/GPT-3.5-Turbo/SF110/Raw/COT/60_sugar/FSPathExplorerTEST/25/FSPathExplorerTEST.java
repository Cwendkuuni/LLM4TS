
import org.junit.Test;

public class FSPathExplorerTest {
  
  @Test
  public void testConstructor() {
    FSPathExplorer explorer = new FSPathExplorer();
    assertNotNull(explorer);
  }
  
  @Test
  public void testConfigureExplorer() {
    FSPathExplorer explorer = new FSPathExplorer();
    String[] args = {"-path", "/path/to/root", "-find", "//dir[@name='test']"};
    explorer.configureExplorer(args);
    assertEquals("/path/to/root", explorer.rootPath.getPath());
    assertEquals("//dir[@name='test']", explorer.fsPathQuery);
  }
  
  @Test
  public void testCreateFSPath() {
    FSPathExplorer explorer = new FSPathExplorer();
    String[] args = {"-path", "/path/to/root", "-find", "//dir[@name='test']"};
    explorer.configureExplorer(args);
    explorer.createFSPath();
    assertNotNull(explorer.fsPath);
  }
  
  @Test
  public void testShowWelcome() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    FSPathExplorer.showWelcome();
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
  public void testShowHelp() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    FSPathExplorer.showHelp();
    String expectedOutput = "FSPathExplorer help :\n" +
            "\n" +
            "Enter an FSPath query to search the filesystem\n" +
            "\n" +
            "Examples :\n" +
            "\n" +
            "1) //dir[@name = 'foo'] \n" +
            "   finds any directory under the current directory named 'foo'\n" +
            "\n" +
            "2) //dir[@name = 'src']//file[@length > 10000] \n" +
            "   finds any file which is nested anywhere below a 'src' directory\n" +
            "   with a file size greater than 10000 bytes\n" +
            "\n" +
            "3) //dir[fs:matches(@name, '^[.].*')] \n" +
            "   finds all directories in the directory sructure which begin with a '.'\n" +
            "\n" +
            "Attributes : (see javadoc for java.io.File for more info)\n" +
            "\n" +
            "@name\n" +
            "@length\n" +
            "@lastModified\n" +
            "@directory\n" +
            "@hidden\n" +
            "@readable\n" +
            "@writable\n" +
            "\n" +
            "Commands :\n" +
            "\n" +
            "help\t: Show help information\n" +
            "exit\t: Exit the program\n" +
            "pwd\t: Show the current directory\n" +
            "cd <directory>\t: Change the current directory\n";
    assertEquals(expectedOutput, outContent.toString());
  }
}