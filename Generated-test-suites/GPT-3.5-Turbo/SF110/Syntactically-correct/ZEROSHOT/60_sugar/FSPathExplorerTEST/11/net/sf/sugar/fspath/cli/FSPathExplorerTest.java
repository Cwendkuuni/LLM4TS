import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import java.io.ByteArrayOutputStream;

public class FSPathExplorerTest {

    private FSPathExplorer fsPathExplorer;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @Before
    public void setUp() {
        fsPathExplorer = new FSPathExplorer();
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        fsPathExplorer.out = new PrintStream(outContent);
        fsPathExplorer.err = new PrintStream(errContent);
    }

    @After
    public void tearDown() {
        fsPathExplorer = null;
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void testConfigureExplorer_rootPathFlag() {
        String[] args = {"-path", "testDirectory", "-find", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        assertEquals(new File("testDirectory"), fsPathExplorer.rootPath);
    }

    @Test
    public void testConfigureExplorer_defaultRootPath() {
        String[] args = {"-find", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        assertNull(fsPathExplorer.rootPath);
    }

    @Test
    public void testConfigureExplorer_fsPathQuery() {
        String[] args = {"-path", "testDirectory", "-find", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorer_multipleQueries() {
        String[] args = {"-path", "testDirectory", "-find", "//dir[@name = 'foo']", "//dir[@name = 'bar']"};
        fsPathExplorer.configureExplorer(args);
        assertEquals("//dir[@name = 'foo']//dir[@name = 'bar']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath_withRootPath() {
        fsPathExplorer.rootPath = new File("testDirectory");
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath);
        assertEquals(fsPathExplorer.rootPath, fsPathExplorer.fsPath.getRootDirectory());
    }

    @Test
    public void testCreateFSPath_withoutRootPath() {
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath);
    }

    @Test
    public void testStart_exitCommand() {
        fsPathExplorer.prompt = new Prompt(new ByteArrayInputStream("exit".getBytes()), fsPathExplorer.out, fsPathExplorer.err);
        fsPathExplorer.start();
        assertTrue(outContent.toString().contains("exiting....."));
    }

    @Test
    public void testStart_pwdCommand() {
        fsPathExplorer.fsPath = FSPathFactory.newFSPath(new File("testDirectory"));
        fsPathExplorer.prompt = new Prompt(new ByteArrayInputStream("pwd".getBytes()), fsPathExplorer.out, fsPathExplorer.err);
        fsPathExplorer.start();
        assertTrue(outContent.toString().contains("Current directory : " + fsPathExplorer.fsPath.getRootDirectory().getAbsolutePath()));
    }

    @Test
    public void testStart_cdCommand_existingDirectory() throws IOException {
        File testDir = new File("testDirectory");
        testDir.mkdir();
        fsPathExplorer.rootPath = testDir;
        fsPathExplorer.prompt = new Prompt(new ByteArrayInputStream("cd subDir".getBytes()), fsPathExplorer.out, fsPathExplorer.err);
        fsPathExplorer.start();
        assertTrue(outContent.toString().contains("changed to : " + new File(fsPathExplorer.rootPath, "subDir").getAbsolutePath()));
        testDir.delete();
        new File(fsPathExplorer.rootPath, "subDir").delete();
    }

    @Test
    public void testStart_cdCommand_nonExistingDirectory() throws IOException {
        File testDir = new File("testDirectory");
        testDir.mkdir();
        fsPathExplorer.rootPath = testDir;
        fsPathExplorer.prompt = new Prompt(new ByteArrayInputStream("cd nonExistentDir".getBytes()), fsPathExplorer.out, fsPathExplorer.err);
        fsPathExplorer.start();
        assertTrue(errContent.toString().contains("unable to change to nonExistentDir"));
        testDir.delete();
    }
    
    @Test
    public void testStart_helpCommand() {
        fsPathExplorer.prompt = new Prompt(new ByteArrayInputStream("help".getBytes()), fsPathExplorer.out, fsPathExplorer.err);
        fsPathExplorer.start();
        assertTrue(outContent.toString().contains("FSPathExplorer help :"));
    }
    
    @Test
    public void testStart_fsPathQuery() {
        fsPathExplorer.fsPath = FSPathFactory.newFSPath(new File("testDirectory"));
        fsPathExplorer.prompt = new Prompt(new ByteArrayInputStream("//dir[@name = 'test']".getBytes()), fsPathExplorer.out, fsPathExplorer.err);
        fsPathExplorer.start();
        assertFalse(outContent.toString().isEmpty());
    }

}