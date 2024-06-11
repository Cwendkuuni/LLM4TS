
package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {

    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private FSPathExplorer explorer;

    @Before
    public void setUpStreams() {
        originalOut = System.out;
        originalErr = System.err;
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        explorer = new FSPathExplorer();
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testCreateFSPath_Default() {
        explorer.createFSPath();

        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testCreateFSPath_WithRootPath() {
        explorer.rootPath = new File("/path/to/root");

        explorer.createFSPath();

        assertNotNull(explorer.fsPath);
        assertEquals(explorer.rootPath, explorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStart_ExitCommand() throws IOException {
        String command = "exit\n";

        provideCommandInput(command);

        explorer.start();

        assertEquals("exiting.....\n", outContent.toString());
    }

    @Test
    public void testStart_PwdCommand() throws IOException {
        String command = "pwd\n";

        provideCommandInput(command);

        explorer.start();

        assertEquals("Current directory : \n", outContent.toString());
    }

    @Test
    public void testStart_CdCommand_ExistingAndReadableDirectory() throws IOException {
        String command = "cd /path/to/dir\n";

        provideCommandInput(command);

        explorer.start();

        assertEquals("changed to : /path/to/dir\n", outContent.toString());
    }

    @Test
    public void testStart_CdCommand_NonExistingDirectory() throws IOException {
        String command = "cd /path/to/nonexistingdir\n";

        provideCommandInput(command);

        explorer.start();

        assertEquals("unable to change to /path/to/nonexistingdir please make sure that the directory exitst and a is readable\n", outContent.toString());
    }

    @Test
    public void testStart_CdCommand_NotReadableDirectory() throws IOException {
        String command = "cd /path/to/notreadabledir\n";

        provideCommandInput(command);

        explorer.start();

        assertEquals("unable to change to /path/to/notreadabledir please make sure that the directory exitst and a is readable\n", outContent.toString());
    }

    @Test
    public void testStart_HelpCommand() throws IOException {
        String command = "help\n";

        provideCommandInput(command);

        explorer.start();

        // Verify help message output
    }

    @Test
    public void testStart_QueryCommand() throws IOException {
        String command = "//dir[@name = 'foo']\n";

        provideCommandInput(command);

        explorer.start();

        // Verify output
    }

    private void provideCommandInput(String input) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        explorer.prompt = new Prompt(in, System.out, System.err);
    }
}
