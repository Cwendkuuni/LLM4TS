package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {
    private FSPathExplorer explorer;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        explorer = new FSPathExplorer();
    }

    @After
    public void tearDown() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void testConfigureExplorer() {
        String[] args = {"-path", "/test/path", "-find", "//file"};

        explorer.configureExplorer(args);

        assertEquals("/test/path", explorer.rootPath.getPath());
        assertEquals("//file", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/test/path");

        explorer.createFSPath();

        assertNotNull(explorer.fsPath);
        assertEquals("/test/path", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();

        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStartExitCommand() {
        String[] args = {"-path", "/test/path", "-find", "//file"};
        explorer.configureExplorer(args);

        explorer.start();

        assertEquals("exiting.....", outContent.toString().trim());
    }

    @Test
    public void testStartPwdCommand() {
        String[] args = {"-path", "/test/path", "-find", "//file"};
        explorer.configureExplorer(args);

        explorer.start();

        assertEquals("Current directory : /test/path", outContent.toString().trim());
    }

    @Test
    public void testStartCdCommand() {
        String[] args = {"-path", "/test/path", "-find", "//file"};
        explorer.configureExplorer(args);

        explorer.start();

        assertEquals("changed to : /new/directory", outContent.toString().trim());
    }

    @Test
    public void testStartHelpCommand() {
        String[] args = {"-path", "/test/path", "-find", "//file"};
        explorer.configureExplorer(args);

        explorer.start();

        // Check the help message output
    }

    @Test
    public void testStartQuery() {
        String[] args = {"-path", "/test/path", "-find", "//file"};
        explorer.configureExplorer(args);

        explorer.start();

        assertEquals("/test/path/file1.txt\n/test/path/file2.txt", outContent.toString().trim());
    }
}