import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import net.sf.sugar.fspath.cli.FSPathExplorer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    File rootPath;
    String fsPathQuery;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }
    
    @After
    public void tearDown() {
        explorer = null;
        rootPath = null;
        fsPathQuery = null;
    }
    
    @Test
    public void testConfigureExplorerWithRootPathArg() {
        String[] args = {"-path", "example/path", "-find", "example/query"};
        explorer.configureExplorer(args);
        assertEquals("example/path", explorer.rootPath.getPath());
    }
    
    @Test
    public void testConfigureExplorerWithoutRootPathArg() {
        String[] args = {"-find", "example/query"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
    }
    
    @Test
    public void testConfigureExplorerWithQueryArg() {
        String[] args = {"-find", "example/query"};
        explorer.configureExplorer(args);
        assertEquals("example/query", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithoutQueryArg() {
        String[] args = {"-path", "example/path"};
        explorer.configureExplorer(args);
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("example/path");
        explorer.createFSPath();
        assertEquals("example/path", explorer.fsPath.getRootDirectory().getPath());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testStartWithExitCommand() {
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() throws IOException {
                return "exit";
            }
        };
        explorer.start();
        assertEquals("exiting.....", explorer.outContent.toString());
    }
    
    @Test
    public void testStartWithPwdCommand() {
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() throws IOException {
                return "pwd";
            }
        };
        explorer.fsPath = new FSPath(new File("example/path"));
        explorer.start();
        assertEquals("Current directory : example/path", explorer.outContent.toString());
    }
    
    @Test
    public void testStartWithCdCommand() {
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() throws IOException {
                return "cd example/path";
            }
        };
        explorer.fsPath = new FSPath(new File("example/startDir"));
        explorer.start();
        assertEquals("changed to : example/path", explorer.outContent.toString());
        assertEquals("example/path", explorer.fsPath.getRootDirectory().getPath());
    }
    
    @Test
    public void testStartWithHelpCommand() {
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() throws IOException {
                return "help";
            }
        };
        explorer.start();
        assertTrue(explorer.outContent.toString().contains("FSPathExplorer help :"));
    }
    
    @Test
    public void testStartWithFSPathQuery() {
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() throws IOException {
                return "//dir[@name = 'example']";
            }
        };
        explorer.fsPath = new FSPath(new File("example/path"));
        explorer.start();
        assertTrue(explorer.outContent.toString().contains("example/path"));
    }
    
}