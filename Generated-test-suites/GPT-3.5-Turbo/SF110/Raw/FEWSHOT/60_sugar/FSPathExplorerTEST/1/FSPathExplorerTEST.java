import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.io.File;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.cli.TestUtil;

public class FSPathExplorerTest {

    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "/path/to/directory", "-find", "//dir[@name='foo']"};
        explorer.configureExplorer(args);
        assertEquals(new File("/path/to/directory"), explorer.rootPath);
        assertEquals("//dir[@name='foo']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/path/to/directory");
        explorer.createFSPath();
        assertEquals(FSPathFactory.newFSPath(new File("/path/to/directory")), explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithNullRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertEquals(FSPathFactory.newFSPath(), explorer.fsPath);
    }

    @Test
    public void testStartExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        TestUtil.setSystemIn("exit");
        explorer.start();
        // Verify that prompt is closed and "exiting....." is printed
    }

    @Test
    public void testStartPwdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/path/to/directory");
        explorer.fsPath = FSPathFactory.newFSPath(rootPath);
        PrintStream out = System.out;
        TestUtil.setSystemOut();
        explorer.start();
        assertEquals("Current directory: " + rootPath.getAbsolutePath(), TestUtil.getSystemOutValue());
        TestUtil.resetSystemOut(out);
    }

    @Test
    public void testStartCdCommandSuccess() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/path/to/directory");
        File newDir = new File("/path/to/newDirectory");
        explorer.fsPath = FSPathFactory.newFSPath(rootPath);
        TestUtil.setSystemIn("cd /path/to/newDirectory");
        PrintStream out = System.out;
        TestUtil.setSystemOut();
        explorer.start();
        assertEquals("changed to : " + newDir.getAbsolutePath(), TestUtil.getSystemOutValue());
        TestUtil.resetSystemOut(out);
    }
    
    @Test
    public void testStartCdCommandFailure() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/path/to/directory");
        File invalidDir = new File("/invalid/path");
        explorer.fsPath = FSPathFactory.newFSPath(rootPath);
        TestUtil.setSystemIn("cd /invalid/path");
        PrintStream out = System.out;
        TestUtil.setSystemOut();
        explorer.start();
        assertEquals("unable to change to /invalid/path please make sure that the directory exists and is readable", TestUtil.getSystemOutValue());
        TestUtil.resetSystemOut(out);
    }

    @Test
    public void testStartHelpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        PrintStream out = System.out;
        TestUtil.setSystemOut();
        explorer.start();
        // Verify that the help message is printed
        TestUtil.resetSystemOut(out);
    }

    @Test
    public void testStartFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/path/to/directory");
        File file1 = new File("/path/to/file1.txt");
        File file2 = new File("/path/to/file2.txt");
        FSPathResultList results = new FSPathResultList();
        results.add(new FSPathResult(file1));
        results.add(new FSPathResult(file2));
        explorer.fsPath = FSPathFactory.newFSPath(rootPath);
        TestUtil.setSystemIn("//file[@name='*.txt']");
        PrintStream out = System.out;
        TestUtil.setSystemOut();
        explorer.start();
        assertEquals(file1.getPath() + "\n" + file2.getPath(), TestUtil.getSystemOutValue());
        TestUtil.resetSystemOut(out);
    }

}