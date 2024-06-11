import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathExplorerTest {
  
  @Test
  public void testConfigureExplorer() {
    // Basic test to check if configuration works correctly
    FSPathExplorer explorer = new FSPathExplorer();
    String[] args = {"-path", "C:\\testFolder", "-find", "//dir[@name = 'test']"};
    explorer.configureExplorer(args);
    assertEquals("C:\\testFolder", explorer.rootPath.getAbsolutePath());
    assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
  }

  @Test
  public void testCreateFSPath() {
    // Create FSPath object and check if it's not null
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.createFSPath();
    assertNotNull(explorer.fsPath);
  }

  @Test
  public void testStartPWDCommand() {
    // Test the PWD command output
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.rootPath = new File("C:\\");
    explorer.prompt = new Prompt(System.in, System.out, System.err);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    explorer.start();
    String expected = "Current directory : C:\\\n";
    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void testStartCDCommand() {
    // Test the CD command to change directory
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.rootPath = new File("C:\\");
    explorer.prompt = new Prompt(System.in, System.out, System.err);
    String[] args = {"-p", "C:\\testFolder"};
    explorer.configureExplorer(args);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    explorer.start();
    String expected = "changed to : C:\\testFolder\n";
    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void testStartHelpCommand() {
    // Test the HELP command output
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.prompt = new Prompt(System.in, System.out, System.err);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    explorer.start();
    String expected = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\n\nAttributes : (see javadoc for java.io.File for more info)\n\n\nCommands :\n\nexit\t: Exit the program\npwd\t: Show current directory\nhelp\t: Display help\n";
    assertEquals(expected, outputStream.toString());
  }

}