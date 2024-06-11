import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;

public class FSPathExplorerTest {

    // Test case for main method
    @Test
    public void testMain() {
        String[] args = {"-path", "test/path", "-find", "//dir"};
        FSPathExplorer.main(args);
    }

    // Test case for configureExplorer method when -path option is provided
    @Test
    public void testConfigureExplorerWithPathOption() {
        String[] args = {"-path", "test/path", "-find", "//dir"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        Assert.assertEquals(new File("test/path"), explorer.rootPath);
    }

    // Test case for configureExplorer method when -find option is provided
    @Test
    public void testConfigureExplorerWithFindOption() {
        String[] args = {"-path", "test/path", "-find", "//dir"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        Assert.assertEquals("//dir", explorer.fsPathQuery);
    }

    // Test case for configureExplorer method when both -path and -find options are provided
    @Test
    public void testConfigureExplorerWithBothOptions() {
        String[] args = {"-path", "test/path", "-find", "//dir"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        Assert.assertEquals(new File("test/path"), explorer.rootPath);
        Assert.assertEquals("//dir", explorer.fsPathQuery);
    }

    // Test case for createFSPath method when rootPath is provided
    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("test/path");
        explorer.createFSPath();
        Assert.assertNotNull(explorer.fsPath);
    }

    // Test case for createFSPath method when rootPath is null
    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        Assert.assertNotNull(explorer.fsPath);
    }

    // Test case for start method
    @Test
    public void testStart() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        // No assertion as start method is an interactive method
    }

    // Test case for showWelcome method
    @Test
    public void testShowWelcome() {
        FSPathExplorer.showWelcome();
        // No assertion as showWelcome method prints the welcome message
    }

    // Test case for showHelp method
    @Test
    public void testShowHelp() {
        FSPathExplorer.showHelp();
        // No assertion as showHelp method prints the help message
    }

    // Test case for FSPath query
    @Test
    public void testFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("test/path");
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query("//dir[@name = 'test']");
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        FSPathResult result = results.get(0);
        Assert.assertNotNull(result);
        Assert.assertEquals(new File("test/path/test"), result.getFile());
        Assert.assertEquals(FSPathAttributes.dir, result.getAttributes());
    }

}