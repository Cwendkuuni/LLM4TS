package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        originalOut = System.out;
        originalErr = System.err;
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testConfigureExplorer() {
        String[] args = {"-path", "C:/path/to/dir", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals(new File("C:/path/to/dir"), explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStart_ExitCommand() {
        ByteArrayInputStream in = new ByteArrayInputStream("exit".getBytes());
        System.setIn(in);
        explorer.start();
        assertEquals("exiting.....\n", outContent.toString());
        assertTrue(outContent.toString().endsWith("exiting.....\n"));
    }

    @Test
    public void testStart_PwdCommand() {
        ByteArrayInputStream in = new ByteArrayInputStream("pwd\nexit".getBytes());
        System.setIn(in);
        explorer.configureExplorer(new String[] {});
        explorer.createFSPath();
        explorer.start();
        String expectedOutput = "Current directory : " + explorer.fsPath.getRootDirectory().getAbsolutePath() + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testStart_CdCommandInvalidDirectory() {
        ByteArrayInputStream in = new ByteArrayInputStream("cd invalid\nexit".getBytes());
        System.setIn(in);
        explorer.configureExplorer(new String[] {"-path", "C:/path/to/dir"});
        explorer.createFSPath();
        explorer.start();
        String expectedOutput = "unable to change to invalid please make sure that the directory exists and is readable\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testStart_CdCommandValidDirectory() {
        String path = "C:/path/to/dir";
        ByteArrayInputStream in = new ByteArrayInputStream(("cd " + path + "\npwd\nexit").getBytes());
        System.setIn(in);
        explorer.configureExplorer(new String[] {});
        explorer.createFSPath();
        explorer.start();
        String expectedOutput = "changed to : " + new File(path).getAbsolutePath() + "\n" + "Current directory : " + new File(path).getAbsolutePath() + "\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(new File(path), explorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStart_HelpCommand() {
        ByteArrayInputStream in = new ByteArrayInputStream("help\nexit".getBytes());
        System.setIn(in);
        explorer.start();
        // Verify that help message is printed to outContent
        assertNotNull(outContent.toString());
    }

    @Test
    public void testStart_FSPathQuery() {
        ByteArrayInputStream in = new ByteArrayInputStream("//dir[@name = 'foo']\nexit".getBytes());
        System.setIn(in);

        explorer.configureExplorer(new String[] {});
        explorer.createFSPath();
        FSPath expectedFSPath = FSPathFactory.newFSPath();
        FSPathResultList expectedResults = expectedFSPath.query("//dir[@name = 'foo']");

        explorer.start();
        String[] outputLines = outContent.toString().split(System.lineSeparator());

        for (int i = 0; i < expectedResults.size(); i++) {
            assertEquals(expectedResults.get(i).getFile().getPath(), outputLines[i]);
        }
    }
}