import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {
  
  private FSPathExplorer explorer;
  
  @Before
  public void setUp() {
    explorer = new FSPathExplorer();
  }
  
  // Test case for the configureExplorer method when rootPath is specified
  @Test
  public void testConfigureExplorerWithRootPath() {
    String[] args = new String[]{"-path", "/root/path", "-find", "//dir[@name = 'test']"};
    explorer.configureExplorer(args);
    assertEquals(new File("/root/path"), explorer.rootPath);
  }
  
  // Test case for the configureExplorer method when fsPathQuery is specified
  @Test
  public void testConfigureExplorerWithFSPathQuery() {
    String[] args = new String[]{"-find", "//dir[@name = 'test']"};
    explorer.configureExplorer(args);
    assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
  }
  
  // Test case for the configureExplorer method when both rootPath and fsPathQuery are specified
  @Test
  public void testConfigureExplorerWithBoth() {
    String[] args = new String[]{"-path", "/root/path", "-find", "//dir[@name = 'test']"};
    explorer.configureExplorer(args);
    assertEquals(new File("/root/path"), explorer.rootPath);
    assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
  }
  
  // Test case for the createFSPath method when rootPath is specified
  @Test
  public void testCreateFSPathWithRootPath() {
    explorer.rootPath = new File("/root/path");
    explorer.createFSPath();
    assertNotNull(explorer.fsPath);
  }
  
  // Test case for the createFSPath method when rootPath is not specified
  @Test
  public void testCreateFSPathWithoutRootPath() {
    explorer.createFSPath();
    assertNotNull(explorer.fsPath);
  }
  
  // Test case for the start method when the user enters a command to exit
  @Test
  public void testStartExitCommand() {
    // Mocking the Prompt class to return "exit" when readLine() is called
    Prompt prompt = Mockito.mock(Prompt.class);
    explorer.prompt = prompt;
    Mockito.when(prompt.readLine()).thenReturn("exit");
    
    // Mocking the System.out to capture the output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    explorer.start();
    
    String expectedOutput = "exiting.....";
    assertEquals(expectedOutput, outputStream.toString().trim());
    Mockito.verify(prompt).close();
  }
  
  // Test case for the start method when the user enters the PWD command
  @Test
  public void testStartPWDCommand() {
    // Mocking the Prompt class to return "pwd" when readLine() is called
    Prompt prompt = Mockito.mock(Prompt.class);
    explorer.prompt = prompt;
    Mockito.when(prompt.readLine()).thenReturn("pwd");
    
    // Mocking the System.out to capture the output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    explorer.start();
    
    String expectedOutput = "Current directory : /root/path";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }
  
  // Test case for the start method when the user enters the CD command with a valid directory
  @Test
  public void testStartValidCDCommand() {
    // Mocking the Prompt class to return "cd /new/dir" when readLine() is called
    Prompt prompt = Mockito.mock(Prompt.class);
    explorer.prompt = prompt;
    Mockito.when(prompt.readLine()).thenReturn("cd /new/dir");
    
    // Mocking the FSPathFactory class to return a new FSPath with the new directory
    FSPathFactory fsPathFactory = Mockito.mock(FSPathFactory.class);
    Mockito.when(fsPathFactory.newFSPath(Mockito.any(File.class))).thenReturn(new FSPath());
    explorer.fsPathFactory = fsPathFactory;
    
    // Mocking the System.out to capture the output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    explorer.start();
    
    String expectedOutput = "changed to : /new/dir";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }
  
  // Test case for the start method when the user enters the CD command with an invalid directory
  @Test
  public void testStartInvalidCDCommand() {
    // Mocking the Prompt class to return "cd /invalid/dir" when readLine() is called
    Prompt prompt = Mockito.mock(Prompt.class);
    explorer.prompt = prompt;
    Mockito.when(prompt.readLine()).thenReturn("cd /invalid/dir");
    
    // Mocking the FSPathFactory class to throw an exception when creating new FSPath
    FSPathFactory fsPathFactory = Mockito.mock(FSPathFactory.class);
    Mockito.when(fsPathFactory.newFSPath(Mockito.any(File.class))).thenThrow(new Throwable());
    explorer.fsPathFactory = fsPathFactory;
    
    // Mocking the System.out to capture the output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    explorer.start();
    
    String expectedOutput = "unable to change to /invalid/dir please make sure that the directory exists and is readable";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }
  
  // Test case for the start method when the user enters the HELP command
  @Test
  public void testStartHELPCommand() {
    // Mocking the Prompt class to return "help" when readLine() is called
    Prompt prompt = Mockito.mock(Prompt.class);
    explorer.prompt = prompt;
    Mockito.when(prompt.readLine()).thenReturn("help");
    
    // Mocking the System.out to capture the output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    explorer.start();
    
    String expectedOutput = //Expected help output
    assertEquals(expectedOutput, outputStream.toString().trim());
  }
  
  // Test case for the start method when the user enters an FSPath query
  @Test
  public void testStartFSPathQuery() {
    // Mocking the Prompt class to return "//dir[@name = 'test']" when readLine() is called
    Prompt prompt = Mockito.mock(Prompt.class);
    explorer.prompt = prompt;
    Mockito.when(prompt.readLine()).thenReturn("//dir[@name = 'test']");
    
    // Mocking the FSPath class to return a list of FSPathResult
    FSPath fsPath = Mockito.mock(FSPath.class);
    FSPathResultList results = new FSPathResultList();
    results.add(new FSPathResult(new File("/root/path/test"), true));
    Mockito.when(fsPath.query(Mockito.anyString())).thenReturn(results);
    explorer.fsPath = fsPath;
    
    // Mocking the System.out to capture the output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    explorer.start();
    
    String expectedOutput = "/root/path/test";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }
  
  // Test case for the start method when the user enters an unknown command
  @Test
  public void testStartUnknownCommand() {
    // Mocking the Prompt class to return "invalid" when readLine() is called
    Prompt prompt = Mockito.mock(Prompt.class);
    explorer.prompt = prompt;
    Mockito.when(prompt.readLine()).thenReturn("invalid");
    
    // Mocking the System.out to capture the output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    explorer.start();
    
    String expectedOutput = "";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

}