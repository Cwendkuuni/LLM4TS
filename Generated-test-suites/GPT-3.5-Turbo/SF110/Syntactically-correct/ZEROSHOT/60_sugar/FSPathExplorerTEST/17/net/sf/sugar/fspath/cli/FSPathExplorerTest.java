import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathExplorerTest {
  
  @Test
  public void testConfigureExplorerWithArgsPath() {
    FSPathExplorer explorer = new FSPathExplorer();
    String[] args = {"-path", "/root/path", "-find", "//dir[@name = 'foo']"};
    explorer.configureExplorer(args);

    assertEquals("/root/path", explorer.rootPath.getPath());
    assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
  }

  @Test
  public void testConfigureExplorerWithArgsP() {
    FSPathExplorer explorer = new FSPathExplorer();
    String[] args = {"-p", "/root/path", "-find", "//dir[@name = 'foo']"};
    explorer.configureExplorer(args);

    assertEquals("/root/path", explorer.rootPath.getPath());
    assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
  }

  @Test
  public void testConfigureExplorerWithArgsFind() {
    FSPathExplorer explorer = new FSPathExplorer();
    String[] args = {"-find", "//dir[@name = 'foo']"};
    explorer.configureExplorer(args);

    assertNull(explorer.rootPath);
    assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
  }

  @Test
  public void testConfigureExplorerWithArgsF() {
    FSPathExplorer explorer = new FSPathExplorer();
    String[] args = {"-f", "//dir[@name = 'foo']"};
    explorer.configureExplorer(args);

    assertNull(explorer.rootPath);
    assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
  }

  @Test
  public void testConfigureExplorerWithNoArgs() {
    FSPathExplorer explorer = new FSPathExplorer();
    String[] args = {};
    explorer.configureExplorer(args);

    assertNull(explorer.rootPath);
    assertEquals("", explorer.fsPathQuery);
  }

  @Test
  public void testCreateFSPath() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.rootPath = new File("/root/path");
    explorer.createFSPath();

    assertNotNull(explorer.fsPath);
    assertEquals("/root/path", explorer.fsPath.getRootDirectory().getPath());
  }

  @Test
  public void testCreateFSPathWithNullRootPath() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.createFSPath();

    assertNotNull(explorer.fsPath);
    assertNull(explorer.fsPath.getRootDirectory());
  }

  @Test
  public void testStartExitCommand() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.prompt = new Prompt(new InputStream() {
      public int read() {
        return 'e';
      }
    }, null, null);

    explorer.start();
    assertTrue(explorer.prompt.isClosed());
  }

  @Test
  public void testStartPwdCommand() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.fsPath = FSPathFactory.newFSPath(new File("/root/path"));
    explorer.out = new PrintStream(new ByteArrayOutputStream());

    explorer.prompt = new Prompt(new InputStream() {
      public int read() {
        return 'p';
      }
    }, null, null);

    explorer.start();
  }

  @Test
  public void testStartCdCommandWithValidDirectory() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.fsPath = FSPathFactory.newFSPath(new File("/root/path"));
    explorer.out = new PrintStream(new ByteArrayOutputStream());

    explorer.prompt = new Prompt(new InputStream() {
      private int count = 0;
      public int read() {
        if(count==0){
         count++; 
         return 'c';
        }else if(count<14){
         count++; 
         return '/';
        }else if(count<22){
         count++; 
         return 'n';
        }else if(count<26){
         count++; 
         return '\n';
        }
        return 'y';
      }
    }, null, null);

    explorer.start();
  }

  @Test
  public void testStartCdCommandWithInvalidDirectory() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.fsPath = FSPathFactory.newFSPath(new File("/root/path"));
    explorer.out = new PrintStream(new ByteArrayOutputStream());

    explorer.prompt = new Prompt(new InputStream() {
      private int count = 0;
      public int read() {
        if(count==0){
         count++; 
         return 'c';
        }else if(count<14){
         count++; 
         return '/';
        }else if(count<22){
         count++; 
         return 'n';
        }else if(count<26){
         count++; 
         return 'u';
        }else if(count<32){
         count++; 
         return 'n';
        }
        return '\n';
      }
    }, null, null);

    explorer.start();
  }

  @Test
  public void testStartHelpCommand() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.out = new PrintStream(new ByteArrayOutputStream());

    explorer.prompt = new Prompt(new InputStream() {
      public int read() {
        return 'h';
      }
    }, null, null);

    explorer.start();
  }

  @Test
  public void testStartFSPathQuery() {
    FSPathExplorer explorer = new FSPathExplorer();
    explorer.fsPath = FSPathFactory.newFSPath(new File("/root/path"));
    explorer.fsPathQuery = "//dir[@name = 'foo']";
    explorer.out = new PrintStream(new ByteArrayOutputStream());

    explorer.prompt = new Prompt(new InputStream() {
      private int count = 0;
      public int read() {
        if(count==0){
         count++; 
         return '\n';
        }
        return 'y';
      }
    }, null, null);

    explorer.start();
  }
  
}