import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    private PrintStream realOut;
    private PrintStream realErr;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    
    @Before
    public void setup() {
        explorer = new FSPathExplorer();
        realOut = System.out;
        realErr = System.err;
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    
    @After
    public void restoreStreams() {
        System.setOut(realOut);
        System.setErr(realErr);
    }
    
    @Test
    public void testConfigureExplorerWithRootPath() {
        String[] args = {"-p", "path/to/root", "-f", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals(new File("path/to/root"), explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithoutRootPath() {
        String[] args = {"-f", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("path/to/root");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals(new File("path/to/root"), explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals(new File("").getAbsoluteFile(), explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testStartExitCommand() throws IOException {
        String input = "exit\n";
        System.setIn(new TestInputStream(input));
        explorer.start();
        assertEquals("exiting.....\n", outContent.toString());
    }
    
    @Test
    public void testStartPwdCommand() throws IOException {
        String input = "pwd\nexit\n";
        System.setIn(new TestInputStream(input));
        explorer.start();
        assertEquals("Current directory : \n", outContent.toString());
    }
    
    @Test
    public void testStartCdCommandWithExistingDirectory() throws IOException {
        String input = "cd path/to/dir\npwd\nexit\n";
        System.setIn(new TestInputStream(input));
        explorer.start();
        assertEquals("changed to : path/to/dir\nCurrent directory : " + new File("path/to/dir").getAbsolutePath() + "\n", outContent.toString());
    }
    
    @Test
    public void testStartCdCommandWithNonExistingDirectory() throws IOException {
        String input = "cd non/existing/dir\nexit\n";
        System.setIn(new TestInputStream(input));
        explorer.start();
        assertEquals("unable to change to non/existing/dir please make sure that the directory exitst and a is readable\n", outContent.toString());
    }
    
    @Test
    public void testStartHelpCommand() throws IOException {
        String input = "help\nexit\n";
        System.setIn(new TestInputStream(input));
        explorer.start();
        assertEquals("FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\nAttributes : " + 
                "(see javadoc for java.io.File for more info)\n\nrosemary\nluke\nron\nlemon\ntom\nsalad\ncucumber\n\nCommands :\n\nexit\t: exit the program\n\npwd\t: display the current directory\n\ncd <directory>\t: change to the specified directory\n\nhelp\t: display this help message\n\n", outContent.toString());
    }
    
    @Test
    public void testStartWithQuery() throws IOException {
        String input = "//dir[@name = 'foo']\nexit\n";
        System.setIn(new TestInputStream(input));
        explorer.createFSPath();
        explorer.start();
        assertEquals("", outContent.toString());
    }
    
    private class TestInputStream extends java.io.InputStream {
        private String input;
        private int currentIndex;
        
        public TestInputStream(String input) {
            this.input = input;
            currentIndex = 0;
        }
        
        @Override
        public int read() throws IOException {
            if (currentIndex >= input.length()) {
                return -1;
            }
            return input.charAt(currentIndex++);
        }
    }
}