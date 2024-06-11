import static org.junit.Assert.*;
import org.junit.Test;
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

    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
        assertNotNull(explorer.out);
        assertNotNull(explorer.err);
        assertNotNull(explorer.prompt);
    }

    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();

        String[] args = new String[]{"-path", "test/path", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals("test/path", explorer.rootPath.getPath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);

        String[] args2 = new String[]{"-p", "/test/path", "-f", "//dir[@name = 'bar']"};
        explorer.configureExplorer(args2);
        assertEquals("/test/path", explorer.rootPath.getPath());
        assertEquals("//dir[@name = 'bar']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();

        String[] args = new String[]{"-path", "test/path", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);

        String[] args2 = new String[]{"-find", "//dir[@name = 'bar']"};
        explorer.configureExplorer(args2);
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStartWithCommands() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() throws IOException {
                return "exit";
            }
            public void close() {}
        };

        explorer.start();
        assertEquals("exiting.....", explorer.out.toString().trim());
    }

    @Test
    public void testStartWithFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() throws IOException {
                return "//dir[@name = 'foo']";
            }
            public void close() {}
        };

        FSPath fsPath = new FSPath() {
            public FSPathResultList query(String expression) {
                assertEquals("//dir[@name = 'foo']", expression);
                FSPathResultList results = new FSPathResultList();
                results.add(new FSPathResult(new File("test/file")));
                return results;
            }
        };
        explorer.fsPath = fsPath;

        explorer.start();
        assertEquals("test/file", explorer.out.toString().trim());
    }
    
}