
package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void testConfigureExplorerWithRootPath() {
        String[] args = {"-path", "root", "-find", "query"};
        fsPathExplorer.configureExplorer(args);
        assertEquals(new File("root"), fsPathExplorer.rootPath);
        assertEquals("query", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithoutRootPath() {
        String[] args = {"-find", "query"};
        fsPathExplorer.configureExplorer(args);
        assertNull(fsPathExplorer.rootPath);
        assertEquals("query", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        fsPathExplorer.rootPath = new File("root");
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath);
        assertEquals("root", fsPathExplorer.fsPath.getRootDirectory().getName());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath);
        assertEquals(0, fsPathExplorer.fsPath.getRootDirectory().listFiles().length);
    }

    @Test
    public void testStartWithExitCommand() {
        InputStream in = new ByteArrayInputStream("exit\n".getBytes());
        fsPathExplorer.prompt.setInputStream(in);
        fsPathExplorer.start();
        assertEquals("exiting.....\n", outContent.toString());
    }

    @Test
    public void testStartWithPwdCommand() {
        fsPathExplorer.start();
        assertEquals("Current directory : \n", outContent.toString());
    }

    @Test
    public void testStartWithCdCommand() {
        InputStream in = new ByteArrayInputStream("cd new_dir\ncd invalid_dir\nexit\n".getBytes());
        fsPathExplorer.prompt.setInputStream(in);
        fsPathExplorer.start();
        assertEquals("changed to : new_dir\nunable to change to invalid_dir please make sure that the directory exitst and a is readable\nexiting.....\n", outContent.toString());
    }

    @Test
    public void testStartWithHelpCommand() {
        fsPathExplorer.start();
        assertTrue(outContent.toString().contains("FSPathExplorer help :"));
    }

    @Test
    public void testStartWithFSPathQuery() {
        InputStream in = new ByteArrayInputStream("dir[@name = 'foo']\nexit\n".getBytes());
        fsPathExplorer.prompt.setInputStream(in);
        fsPathExplorer.start();
        assertTrue(outContent.toString().contains("foo"));
    }

    @Test
    public void testPromptClose() {
        fsPathExplorer.prompt.close();
        assertFalse(fsPathExplorer.prompt.isOpen());
    }

    @Test
    public void testShowWelcome() {
        FSPathExplorer.showWelcome();
        assertTrue(outContent.toString().contains("FSPathExplorer"));
    }

    @Test
    public void testShowHelp() {
        FSPathExplorer.showHelp();
        assertTrue(outContent.toString().contains("FSPathExplorer help :"));
    }
}
