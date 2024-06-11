import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FSPathExplorerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final InputStream originalIn = System.in;

    private FSPathExplorer fsPathExplorer;
    private PrintStream out;
    private PrintStream err;
    private InputStream in;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        fsPathExplorer = new FSPathExplorer();
        out = new PrintStream(outContent);
        err = new PrintStream(errContent);
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    @Test
    public void testConfigureExplorerWithPathArg() {
        String[] args = {"-path", "/path/to/root"};
        fsPathExplorer.configureExplorer(args);
        assertEquals("/path/to/root", fsPathExplorer.rootPath.getAbsolutePath());
    }

    @Test
    public void testConfigureExplorerWithFindArg() {
        String[] args = {"-find", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        String[] args = {"-path", "/path/to/root"};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.createFSPath();
        assertEquals("/path/to/root", fsPathExplorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        String[] args = {"-find", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath);
    }

    @Test
    public void testStartWithExitCommand() throws IOException {
        String[] args = {};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.createFSPath();
        String input = "exit";
        InputStream in = new ByteArrayInputStream(input.getBytes("UTF-8"));
        System.setIn(in);
        fsPathExplorer.start();
        assertEquals("exiting.....", outContent.toString().trim());
    }

    @Test
    public void testStartWithPWDCommand() throws IOException {
        String[] args = {};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.createFSPath();
        String input = "pwd" + System.lineSeparator() + "exit";
        InputStream in = new ByteArrayInputStream(input.getBytes("UTF-8"));
        System.setIn(in);
        fsPathExplorer.start();
        assertEquals("Current directory : /current/directory", outContent.toString().trim());
    }

    @Test
    public void testStartWithCDCommand() throws IOException {
        String[] args = {};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.createFSPath();
        String input = "cd /new/directory" + System.lineSeparator() + "pwd" + System.lineSeparator() + "exit";
        InputStream in = new ByteArrayInputStream(input.getBytes("UTF-8"));
        System.setIn(in);
        fsPathExplorer.start();
        assertEquals("changed to : /new/directory" + System.lineSeparator() + "Current directory : /new/directory", outContent.toString().trim());
    }

    @Test
    public void testStartWithHelpCommand() throws IOException {
        String[] args = {};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.createFSPath();
        String input = "help" + System.lineSeparator() + "exit";
        InputStream in = new ByteArrayInputStream(input.getBytes("UTF-8"));
        System.setIn(in);
        fsPathExplorer.start();
        assertTrue(outContent.toString().contains("FSPathExplorer help"));
    }
    
    @Test
    public void testStartWithFSPathQuery() throws IOException {
        String[] args = {};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.createFSPath();
        String input = "//dir[@name = 'foo']" + System.lineSeparator() + "exit";
        InputStream in = new ByteArrayInputStream(input.getBytes("UTF-8"));
        System.setIn(in);
        fsPathExplorer.start();
        
        assertEquals("path/to/dir", outContent.toString().trim());
    }
}