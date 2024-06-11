import org.junit.*;
import static org.junit.Assert.*;
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

public class FSPathExplorerTest {
    private FSPathExplorer fsPathExplorer;

    @Before
    public void setUp() {
        fsPathExplorer = new FSPathExplorer();
    }

    @After
    public void tearDown() {
        fsPathExplorer = null;
    }

    @Test
    public void testConfigureExplorer() {
        String[] args = new String[] {"-path", "/path/to/directory", "-find", "//dir[@name = 'test']"};
        fsPathExplorer.configureExplorer(args);
        assertEquals(new File("/path/to/directory"), fsPathExplorer.rootPath);
        assertEquals("//dir[@name = 'test']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath);
    }

    @Test
    public void testStartWhenExpressionIsExit() {
        fsPathExplorer.prompt = new Prompt(null, null, null) {
            @Override
            public String readLine() throws IOException {
                return "exit";
            }
        };
        fsPathExplorer.start();
        assertNull(fsPathExplorer.prompt);
    }

    @Test
    public void testStartWhenExpressionIsPwd() {
        fsPathExplorer.prompt = new Prompt(null, new PrintStream(new ByteArrayOutputStream()), null);
        fsPathExplorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/directory"));
        fsPathExplorer.start();
        assertEquals("Current directory : /path/to/directory", fsPathExplorer.out.toString().trim());
    }

    @Test
    public void testStartWhenExpressionIsCd() {
        fsPathExplorer.prompt = new Prompt(null, new PrintStream(new ByteArrayOutputStream()), null);
        fsPathExplorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/directory"));
        fsPathExplorer.start();
        assertEquals("changed to : /path/to/directory", fsPathExplorer.out.toString().trim());
    }

    @Test
    public void testStartWhenExpressionIsHelp() {
        fsPathExplorer.prompt = new Prompt(null, new PrintStream(new ByteArrayOutputStream()), null);
        fsPathExplorer.start();
        assertNotEquals("FSPathExplorer help :", fsPathExplorer.out.toString().trim());
    }

    @Test
    public void testStartWhenExpressionIsFSPathQuery() {
        fsPathExplorer.prompt = new Prompt(null, new PrintStream(new ByteArrayOutputStream()), null) {
            @Override
            public String readLine() throws IOException {
                return "//dir[@name = 'test']";
            }
        };
        fsPathExplorer.fsPath = new FSPath() {
            @Override
            public FSPathResultList query(String expression) {
                FSPathResultList results = new FSPathResultList();
                File file = new File("/path/to/directory/test");
                results.add(new FSPathResult(file));
                return results;
            }
        };
        fsPathExplorer.start();
        assertEquals("/path/to/directory/test", fsPathExplorer.out.toString().trim());
    }
}