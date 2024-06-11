import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {
    
    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "/path/to/dir", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals("/path/to/dir", explorer.rootPath.getPath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerNoPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/path/to/dir");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals("/path/to/dir", explorer.fsPath.getRootDirectory().getPath());
    }
    
    @Test
    public void testCreateFSPathNoRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertNull(explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testStartExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(null, null, null) {
            public String readLine() throws IOException {
                return "exit";
            }
        };
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        assertEquals("exiting.....\n", outContent.toString());
    }
    
    @Test
    public void testStartPwdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = new FSPath(new File("/path/to/dir"));
        explorer.prompt = new Prompt(null, null, null) {
            public String readLine() throws IOException {
                return "pwd";
            }
        };
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        assertEquals("Current directory : /path/to/dir\n", outContent.toString());
    }
    
    @Test
    public void testStartCdCommandValid() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = new FSPath(new File("/path/to/dir"));
        explorer.prompt = new Prompt(null, null, null) {
            public String readLine() throws IOException {
                return "cd /new/dir";
            }
        };
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        assertEquals("changed to : /new/dir\n", outContent.toString());
        assertEquals("/new/dir", explorer.fsPath.getRootDirectory().getPath());
    }
    
    @Test
    public void testStartCdCommandInvalid() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = new FSPath(new File("/path/to/dir"));
        explorer.prompt = new Prompt(null, null, null) {
            public String readLine() throws IOException {
                return "cd /invalid/dir";
            }
        };
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        assertEquals("unable to change to /invalid/dir please make sure that the directory exitst and a is readable\n", outContent.toString());
        assertEquals("/path/to/dir", explorer.fsPath.getRootDirectory().getPath());
    }
    
    @Test
    public void testStartHelpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(null, null, null) {
            public String readLine() throws IOException {
                return "help";
            }
        };
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        // check the expected help output
    }
    
    @Test
    public void testStartFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = new FSPath(new File("/path/to/dir"));
        explorer.fsPathQuery = "//dir[@name = 'foo']";
        explorer.prompt = new Prompt(null, null, null) {
            public String readLine() throws IOException {
                return "//dir[@name = 'foo']";
            }
        };
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        assertEquals("/path/to/dir/foo", outContent.toString());
    }
    
    @Test
    public void testStartUnknownCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = new FSPath(new File("/path/to/dir"));
        explorer.prompt = new Prompt(null, null, null) {
            public String readLine() throws IOException {
                return "unknown";
            }
        };
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        // check the expected error output
    }
    
}