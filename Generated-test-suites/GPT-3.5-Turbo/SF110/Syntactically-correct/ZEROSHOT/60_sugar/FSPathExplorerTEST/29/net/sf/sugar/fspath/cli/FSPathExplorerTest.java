import static org.junit.Assert.*;
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

public class FSPathExplorerTest {

    @Test
    public void testConfigureExplorer() {
        // Test case 1
        String[] args1 = {"-path", "/home/user/", "-find", "//dir[@name = 'foo']"};
        FSPathExplorer explorer1 = new FSPathExplorer();
        explorer1.configureExplorer(args1);
        assertEquals(explorer1.rootPath, new File("/home/user/"));
        assertEquals(explorer1.fsPathQuery, "//dir[@name = 'foo']");

        // Test case 2
        String[] args2 = {"-find", "//dir[@name = 'bar']//-file[@length > 10000]"};
        FSPathExplorer explorer2 = new FSPathExplorer();
        explorer2.configureExplorer(args2);
        assertEquals(explorer2.rootPath, null);
        assertEquals(explorer2.fsPathQuery, "//dir[@name = 'bar']//-file[@length > 10000]");

        // Test case 3
        String[] args3 = {"-path", "/home/user/", "-find"};
        FSPathExplorer explorer3 = new FSPathExplorer();
        explorer3.configureExplorer(args3);
        assertEquals(explorer3.rootPath, new File("/home/user/"));
        assertEquals(explorer3.fsPathQuery, "");
    }

    @Test
    public void testCreateFSPath() {
        // Test case 1
        FSPathExplorer explorer1 = new FSPathExplorer();
        explorer1.createFSPath();
        assertNotNull(explorer1.fsPath);

        // Test case 2
        FSPathExplorer explorer2 = new FSPathExplorer();
        explorer2.rootPath = new File("/home/user/");
        explorer2.createFSPath();
        assertNotNull(explorer2.fsPath);
        assertEquals(explorer2.fsPath.getRootDirectory().getAbsolutePath(), "/home/user/");
    }

    @Test
    public void testStart() {
        // Test case 1
        FSPathExplorer explorer1 = new FSPathExplorer();
        explorer1.out = new PrintStream(System.out);
        explorer1.err = new PrintStream(System.err);
        explorer1.start();

        // Test case 2
        FSPathExplorer explorer2 = new FSPathExplorer();
        explorer2.out = new PrintStream(System.out);
        explorer2.err = new PrintStream(System.err);
        explorer2.rootPath = new File("/home/user/");
        explorer2.start();
    }

    @Test
    public void testMain() {
        // Test case 1
        FSPathExplorer.main(new String[] {});

        // Test case 2
        FSPathExplorer.main(new String[] {"-path", "/home/user/", "-find", "//dir[@name = 'foo']"});
    }

}