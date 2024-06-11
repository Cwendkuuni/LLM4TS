import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private ByteArrayInputStream inContent;
  private PrintStream originalOut;
  private PrintStream originalErr;

  @Before
  public void setUpStreams() {
    originalOut = System.out;
    originalErr = System.err;
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void testStart() {
    FSPathExplorer explorer = new FSPathExplorer();
    inContent = new ByteArrayInputStream("exit".getBytes());
    System.setIn(inContent);
    explorer.start();
    assertEquals("exiting.....\n", outContent.toString());
  }

  @Test
  public void testConfigureExplorer() {
    String[] args = new String[] {"-path", "directory/path", "-f", "query"};
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.configureExplorer(args);
    assertEquals(new File("directory/path"), explorer.rootPath);
    assertEquals("query", explorer.fsPathQuery);
  }

  @Test
  public void testCreateFSPath() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.createFSPath();
    assertNull(explorer.rootPath);
    assertNotNull(explorer.fsPath);
  }

  @Test
  public void testConfigureAndCreateFSPath() {
    String[] args = new String[] {"-path", "directory/path"};
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.configureExplorer(args);
    explorer.createFSPath();
    assertEquals(new File("directory/path"), explorer.rootPath);
    assertNotNull(explorer.fsPath);
  }
}