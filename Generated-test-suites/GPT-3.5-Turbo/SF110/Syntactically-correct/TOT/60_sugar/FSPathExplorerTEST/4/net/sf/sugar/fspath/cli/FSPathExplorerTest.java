import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

public class FSPathExplorerTest {

    private FSPathExplorer fsPathExplorer;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        fsPathExplorer = new FSPathExplorer();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testConfigureExplorerWithRootPathOption() {
        String[] args = {"-path", "testPath"};
        fsPathExplorer.configureExplorer(args);
        Assert.assertEquals(new File("testPath"), fsPathExplorer.rootPath);
    }

    @Test
    public void testConfigureExplorerWithFindOption() {
        String[] args = {"-find", "//dir[@name = 'testDir']"};
        fsPathExplorer.configureExplorer(args);
        Assert.assertEquals("//dir[@name = 'testDir']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPathNotNull() {
        fsPathExplorer.rootPath = new File("testPath");
        fsPathExplorer.createFSPath();
        Assert.assertNotNull(fsPathExplorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithRootPathNull() {
        fsPathExplorer.createFSPath();
        Assert.assertNotNull(fsPathExplorer.fsPath);
    }

    @Test
    public void testStartWithExitCommand() {
        String[] args = {Commands.EXIT.getCommandText()};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.start();
        Assert.assertEquals("exiting.....", outContent.toString().trim());
    }

    @Test
    public void testStartWithPwdCommand() {
        String[] args = {Commands.PWD.getCommandText()};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.fsPath = new FSPath(new File("testDir"));
        fsPathExplorer.start();
        Assert.assertEquals("Current directory : testDir", outContent.toString().trim());
    }

    @Test
    public void testStartWithCdCommand() {
        String[] args = {Commands.CD.getCommandText(), "newDir"};
        fsPathExplorer.configureExplorer(args);
        File newDir = new File("newDir");
        fsPathExplorer.fsPath = new FSPath(newDir);
        fsPathExplorer.start();
        Assert.assertEquals("changed to : newDir", outContent.toString().trim());
    }

    @Test
    public void testStartWithHelpCommand() {
        String[] args = {Commands.HELP.getCommandText()};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.start();
        // Assert that help text is printed
    }

    @Test
    public void testStartWithFSPathQuery() {
        String[] args = {"//dir[@name = 'testDir']"};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.fsPath = new FSPath(new File("testDir"));
        fsPathExplorer.start();
        Assert.assertEquals("testDir/testFile", outContent.toString().trim());
    }

    @Test
    public void testStartWithInvalidCommand() {
        String[] args = {"invalidCommand"};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.start();
        // Assert that error message is printed
    }

    @Test
    public void testStartIOException() {
        String[] args = {}; 
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.start();
        // Assert that IOException stack trace is printed
    }

    @Test
    public void testMain() {
        String[] args = {};
        FSPathExplorer.main(args);
        // Assert that the welcome message is printed
    }

}