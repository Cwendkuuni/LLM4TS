package net.sf.sugar.fspath.cli;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class FSPathExplorerTest {

    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-p", "rootPath", "-f", "fsPathQuery"};

        explorer.configureExplorer(args);

        assertEquals("rootPath", explorer.rootPath.getName());
        assertEquals("fsPathQuery", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("rootPath");

        explorer.createFSPath();

        assertNotNull(explorer.fsPath);
        assertEquals("rootPath", explorer.fsPath.getRootDirectory().getName());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();

        explorer.createFSPath();

        assertNotNull(explorer.fsPath);
        assertEquals(System.getProperty("user.dir"), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testStartExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(new ByteArrayInputStream("exit".getBytes()), new PrintStream(new ByteArrayOutputStream()), new PrintStream(new ByteArrayOutputStream()));

        explorer.start();
    }

    @Test
    public void testStartPWDDirectoryCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        explorer.prompt = new Prompt(new ByteArrayInputStream("pwd".getBytes()), new PrintStream(outContent), new PrintStream(new ByteArrayOutputStream()));

        explorer.start();

        assertEquals("Current directory : " + System.getProperty("user.dir"), outContent.toString().trim());
    }

    @Test
    public void testStartCDCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayInputStream inContent = new ByteArrayInputStream("cd newDir".getBytes());
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        explorer.prompt = new Prompt(inContent, new PrintStream(outContent), new PrintStream(new ByteArrayOutputStream()));
        explorer.fsPath = FSPathFactory.newFSPath();

        explorer.start();

        assertEquals("changed to : " + new File("newDir").getAbsolutePath(), outContent.toString().trim());
    }

    @Test
    public void testStartHelpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        explorer.prompt = new Prompt(new ByteArrayInputStream("help".getBytes()), new PrintStream(outContent), new PrintStream(new ByteArrayOutputStream()));

        explorer.start();

        assertTrue(outContent.toString().contains("FSPathExplorer help"));
    }

    @Test
    public void testStartFSPathQuery() {
        File tempDir = Files.createTempDir();
        File file1 = new File(tempDir, "file1");
        file1.createNewFile();
        File file2 = new File(tempDir, "file2");
        file2.createNewFile();

        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayInputStream inContent = new ByteArrayInputStream("//file".getBytes());
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        explorer.prompt = new Prompt(inContent, new PrintStream(outContent), new PrintStream(new ByteArrayOutputStream()));
        explorer.fsPath = FSPathFactory.newFSPath(tempDir);

        explorer.start();

        assertEquals(file1.getAbsolutePath() + "\n" + file2.getAbsolutePath(), outContent.toString().trim());
    }
}