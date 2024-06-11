import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

    private FSPathExplorer fsPathExplorer;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @Before
    public void setUp() {
        fsPathExplorer = new FSPathExplorer();
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testConfigureExplorer() {
        String[] args1 = new String[]{"-path", "/root/path", "-find", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args1);
        assertEquals("/root/path", fsPathExplorer.rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);

        String[] args2 = new String[]{"-p", "/root/path", "-f", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args2);
        assertEquals("/root/path", fsPathExplorer.rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);

        String[] args3 = new String[]{"-find", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args3);
        assertNull(fsPathExplorer.rootPath);
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);

        String[] args4 = new String[]{};
        fsPathExplorer.configureExplorer(args4);
        assertNull(fsPathExplorer.rootPath);
        assertEquals("", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        assertNull(fsPathExplorer.rootPath);
        assertNull(fsPathExplorer.fsPath);
        fsPathExplorer.createFSPath();
        assertNull(fsPathExplorer.rootPath);
        assertNotNull(fsPathExplorer.fsPath);

        fsPathExplorer.rootPath = new File("/root/path");
        assertNull(fsPathExplorer.fsPath);
        fsPathExplorer.createFSPath();
        assertEquals("/root/path", fsPathExplorer.rootPath.getAbsolutePath());
        assertNotNull(fsPathExplorer.fsPath);
    }

    @Test
    public void testStart() {
        // Set up input stream
        ByteArrayInputStream inStream = new ByteArrayInputStream("exit\n".getBytes());
        fsPathExplorer.prompt = new Prompt(inStream, new PrintStream(outContent), new PrintStream(errContent));

        fsPathExplorer.start();
        assertEquals("exiting.....\n", outContent.toString());

        // Reset streams
        outContent.reset();

        // Set up input stream
        inStream = new ByteArrayInputStream("pwd\nexit\n".getBytes());
        fsPathExplorer.prompt = new Prompt(inStream, new PrintStream(outContent), new PrintStream(errContent));

        fsPathExplorer.start();
        assertEquals("Current directory : \nexiting.....\n", outContent.toString());

        // Reset streams
        outContent.reset();

        // Set up input stream
        inStream = new ByteArrayInputStream("cd /new/dir\nexit\n".getBytes());
        fsPathExplorer.prompt = new Prompt(inStream, new PrintStream(outContent), new PrintStream(errContent));

        fsPathExplorer.start();
        assertEquals("unable to change to /new/dir please make sure that the directory exists and is readable\nexiting.....\n", outContent.toString());

        // Reset streams
        outContent.reset();

        // Set up input stream
        inStream = new ByteArrayInputStream("help\nexit\n".getBytes());
        fsPathExplorer.prompt = new Prompt(inStream, new PrintStream(outContent), new PrintStream(errContent));

        fsPathExplorer.start();
        assertEquals("FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\n\nAttributes : (see javadoc for java.io.File for more info)\n\n@dir\n@file\n@length\n\nCommands :\n\nexit\t: Exit the program\npwd\t: Get the current directory\nhelp\t: Show help\n\nexiting.....\n", outContent.toString());

        // Reset streams
        outContent.reset();

        // Set up input stream
        inStream = new ByteArrayInputStream("//dir[@name = 'foo']\nexit\n".getBytes());
        fsPathExplorer.prompt = new Prompt(inStream, new PrintStream(outContent), new PrintStream(errContent));

        fsPathExplorer.fsPath = new FSPath(new File("."));
        fsPathExplorer.start();
        assertEquals(".\n", outContent.toString());
    }
}