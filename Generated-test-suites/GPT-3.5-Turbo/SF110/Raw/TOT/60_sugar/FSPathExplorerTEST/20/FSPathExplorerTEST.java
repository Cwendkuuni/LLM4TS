import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FSPathExplorerTest {

  // Test case for the configureExplorer method with root path argument
  @Test
  public void testConfigureExplorerWithPathArgument() {
    String[] args = {"-path", "path/to/root", "-find", "//dir[@name = 'foo']"};
    FSPathExplorer explorer = new FSPathExplorer();

    explorer.configureExplorer(args);

    assertEquals(new File("path/to/root"), explorer.rootPath);
    assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
  }

  // Test case for the configureExplorer method without root path argument
  @Test
  public void testConfigureExplorerWithoutPathArgument() {
    String[] args = {"-find", "//dir[@name = 'foo']"};
    FSPathExplorer explorer = new FSPathExplorer();

    explorer.configureExplorer(args);

    assertNull(explorer.rootPath);
    assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
  }

  // Test case for the createFSPath method with root path
  @Test
  public void testCreateFSPathWithRootPath() {
    File rootPath = new File("path/to/root");
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.rootPath = rootPath;

    explorer.createFSPath();

    assertEquals(FSPathFactory.newFSPath(rootPath), explorer.fsPath);
  }

  // Test case for the createFSPath method without root path
  @Test
  public void testCreateFSPathWithoutRootPath() {
    FSPathExplorer explorer = new FSPathExplorer();

    explorer.createFSPath();

    assertEquals(FSPathFactory.newFSPath(), explorer.fsPath);
  }

  // Test case for the start method with valid command
  @Test
  public void testStartWithValidCommand() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.prompt = mock(Prompt.class);
    when(explorer.prompt.readLine()).thenReturn("help");

    explorer.start();

    verify(explorer.prompt, times(1)).readLine();
  }

  // Test case for the start method with exit command
  @Test
  public void testStartWithExitCommand() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.prompt = mock(Prompt.class);
    when(explorer.prompt.readLine()).thenReturn("exit");

    explorer.start();

    verify(explorer.prompt, times(1)).readLine();
    assertEquals("exiting.....", explorer.out.toString());
  }

  // Test case for the start method with pwd command
  @Test
  public void testStartWithPWDCommand() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.prompt = mock(Prompt.class);
    when(explorer.prompt.readLine()).thenReturn("pwd");

    explorer.start();

    verify(explorer.prompt, times(1)).readLine();
    assertEquals("Current directory : " + explorer.fsPath.getRootDirectory().getAbsolutePath(), explorer.out.toString());
  }

  // Test case for the start method with cd command and valid directory
  @Test
  public void testStartWithCDCommandAndValidDirectory() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.prompt = mock(Prompt.class);
    when(explorer.prompt.readLine()).thenReturn("cd path/to/newDir");

    explorer.start();

    verify(explorer.prompt, times(1)).readLine();
    assertEquals("changed to : path/to/newDir", explorer.out.toString());
    assertEquals(FSPathFactory.newFSPath(new File("path/to/newDir")), explorer.fsPath);
  }

  // Test case for the start method with cd command and invalid directory
  @Test
  public void testStartWithCDCommandAndInvalidDirectory() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.prompt = mock(Prompt.class);
    when(explorer.prompt.readLine()).thenReturn("cd invalid/dir");

    explorer.start();

    verify(explorer.prompt, times(1)).readLine();
    assertEquals("unable to change to invalid/dir please make sure that the directory exitst and a is readable", explorer.out.toString());
  }

  // Test case for the start method with help command
  @Test
  public void testStartWithHelpCommand() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.prompt = mock(Prompt.class);
    when(explorer.prompt.readLine()).thenReturn("help");

    explorer.start();

    verify(explorer.prompt, times(1)).readLine();
    // Add assert for checking if help text is printed to the output stream
  }

  // Test case for the start method with FSPath query
  @Test
  public void testStartWithFSPathQuery() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.prompt = mock(Prompt.class);
    when(explorer.prompt.readLine()).thenReturn("//dir[@name = 'foo']");

    // Mock the FSPath and FSPathResultList objects
    FSPath fsPath = mock(FSPath.class);
    FSPathResultList results = mock(FSPathResultList.class);
    when(results.toString()).thenReturn("path/to/file1\npath/to/file2");
    when(fsPath.query("//dir[@name = 'foo']")).thenReturn(results);
    explorer.fsPath = fsPath;

    explorer.start();

    verify(explorer.prompt, times(1)).readLine();
    // Add assert for checking if the results of the FSPath query are printed to the output stream
  }

}