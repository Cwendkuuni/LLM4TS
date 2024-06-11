package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private ByteArrayOutputStream out;
    private ByteArrayOutputStream err;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
        explorer.out = new PrintStream(out);
        explorer.err = new PrintStream(err);
    }

    @After
    public void cleanUp() {
        explorer.prompt.close();
    }

    @Test
    public void testConfigureExplorerWithPathArgument() {
        String[] args = {"-path", "test/path", "-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        assertNotNull(explorer.rootPath);
        assertEquals("test/path", explorer.rootPath.getPath());
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithFindArgument() {
        String[] args = {"-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("test/path");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals("test/path", explorer.fsPath.getRootDirectory().getPath());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStartWithExitCommand() {
        explorer.prompt = new PromptMock(new ByteArrayInputStream("exit".getBytes()), out);
        explorer.start();
        assertEquals("exiting.....\n", out.toString());
    }

    @Test
    public void testStartWithPWDCommand() {
        explorer.prompt = new PromptMock(new ByteArrayInputStream("pwd".getBytes()), out);
        explorer.fsPath = new FSPathMock();
        explorer.start();
        assertEquals("Current directory : /test/path\n", out.toString());
    }

    @Test
    public void testStartWithCDCommandSuccess() {
        explorer.prompt = new PromptMock(new ByteArrayInputStream("cd test".getBytes()), out);
        explorer.fsPath = new FSPathMock();
        explorer.start();
        assertEquals("changed to : /test\n", out.toString());
    }

    @Test
    public void testStartWithCDCommandFailure() {
        explorer.prompt = new PromptMock(new ByteArrayInputStream("cd nonexistent".getBytes()), out);
        explorer.fsPath = new FSPathMock();
        explorer.start();
        assertEquals("unable to change to nonexistent please make sure that the directory exists and is readable\n", out.toString());
    }

    @Test
    public void testStartWithHelpCommand() {
        explorer.prompt = new PromptMock(new ByteArrayInputStream("help".getBytes()), out);
        explorer.start();
        // Check if the help message includes the expected commands
        String helpMessage = out.toString();
        String expectedCommands = "exit\t: Exit the program\npwd\t: Print the current directory\n";
        assertTrue(helpMessage.contains(expectedCommands));
    }

    @Test
    public void testStartWithFSPathQuery() {
        explorer.prompt = new PromptMock(new ByteArrayInputStream("//dir[@name = 'test']".getBytes()), out);
        explorer.fsPath = new FSPathMock();
        explorer.start();
        assertEquals("/test", out.toString());
    }

    private class PromptMock extends Prompt {
        private ByteArrayInputStream in;

        public PromptMock(ByteArrayInputStream in, ByteArrayOutputStream out) {
            super(in, new PrintStream(out), new PrintStream(err));
            this.in = in;
        }

        @Override
        public String readLine() throws IOException {
            byte[] input = new byte[in.available()];
            in.read(input);
            return new String(input);
        }
    }

    private class FSPathMock extends FSPath {
        @Override
        public FSPathResultList query(String expression) {
            FSPathResultList results = new FSPathResultList();
            results.add(new FSPathResult(new File("/test")));
            return results;
        }
    }
}