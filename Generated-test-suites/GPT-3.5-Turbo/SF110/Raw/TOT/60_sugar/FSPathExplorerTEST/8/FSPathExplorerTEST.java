import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {
  
  @Test
  public void testConfigureExplorer_ValidArgs_CorrectConfiguration() {
    String[] args = {"-path", "/root", "-find", "//dir[@name='test']"};
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.configureExplorer(args);
    assertEquals("/root", explorer.rootPath.toString());
    assertEquals("//dir[@name='test']", explorer.fsPathQuery);
  }
  
  @Test
  public void testConfigureExplorer_InvalidArgs_EmptyConfiguration() {
    String[] args = {"-path"};
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.configureExplorer(args);
    assertNull(explorer.rootPath);
    assertEquals("", explorer.fsPathQuery);
  }
  
  @Test
  public void testCreateFSPath_RootPathProvided_ValidFSPathCreated() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.rootPath = new File("/root");
    explorer.createFSPath();
    assertNotNull(explorer.fsPath);
  }
  
  @Test
  public void testCreateFSPath_RootPathNotProvided_ValidFSPathCreated() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.createFSPath();
    assertNotNull(explorer.fsPath);
  }
  
  @Test
  public void testStart_CommandExit_PromptClosed() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.prompt = new Prompt();
    explorer.start();
    assertTrue(explorer.prompt.isClosed());
  }
  
  @Test
  public void testStart_CommandPwd_CurrentDirectoryDisplayed() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.fsPath = new FSPath();
    explorer.fsPath.setRootDirectory(new File("/root"));
    explorer.out = new PrintStream(new ByteArrayOutputStream());
    explorer.start();
    //assert output contains "Current directory : /root"
  }
  
  @Test
  public void testStart_CommandCd_ChangeToExistingDirectory_Successful() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.fsPath = new FSPath();
    File validDir = new File("/validDir");
    validDir.mkdirs();
    explorer.out = new PrintStream(new ByteArrayOutputStream());
    explorer.start();
    //assert fsPath is now in validDir
    //assert output contains "changed to : /validDir"
  }
  
  @Test
  public void testStart_CommandCd_ChangeToNonExistingDirectory_Unsuccessful() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.fsPath = new FSPath();
    File invalidDir = new File("/invalidDir");
    explorer.out = new PrintStream(new ByteArrayOutputStream());
    explorer.start();
    //assert fsPath is still in the previous directory
    //assert output contains "unable to change to /invalidDir"
  }
  
  @Test
  public void testStart_CommandHelp_HelpMessageDisplayed() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.out = new PrintStream(new ByteArrayOutputStream());
    explorer.start();
    //assert output contains the help message
  }
  
  @Test
  public void testStart_FsPathQuery_ValidQuery_ResultsDisplayed() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.fsPath = new FSPath();
    explorer.fsPathQuery = "//dir[@name='test']";
    FSPathResultList results = new FSPathResultList();
    results.add(new FSPathResult(new File("/root/test")));
    explorer.fsPath.setResults(results);
    explorer.out = new PrintStream(new ByteArrayOutputStream());
    explorer.start();
    //assert output contains "/root/test"
  }
  
  @Test
  public void testStart_FsPathQuery_InvalidQuery_NoResultsDisplayed() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.fsPath = new FSPath();
    explorer.fsPathQuery = "//invalidQuery";
    FSPathResultList results = new FSPathResultList();
    explorer.fsPath.setResults(results);
    explorer.out = new PrintStream(new ByteArrayOutputStream());
    explorer.start();
    //assert output is empty
  }
  
}