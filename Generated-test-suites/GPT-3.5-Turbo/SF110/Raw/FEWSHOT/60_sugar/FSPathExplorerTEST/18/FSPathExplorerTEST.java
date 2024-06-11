import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;

public class FSPathExplorerTest {
    private FSPathExplorer fspathExplorer;
    private ByteArrayInputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;
    
    @Before
    public void setUp() {
        fspathExplorer = new FSPathExplorer();
        inputStream = new ByteArrayInputStream("".getBytes());
        outputStream = new ByteArrayOutputStream();
        errorStream = new ByteArrayOutputStream();
        fspathExplorer.out = new PrintStream(outputStream);
        fspathExplorer.err = new PrintStream(errorStream);
    }
    
    @Test
    public void testConfigureExplorerWithRootPath() {
        String[] args = {"-path", "/path/to/directory", "-find", "//dir[@name='foo']"};
        fspathExplorer.configureExplorer(args);
        assertNotNull(fspathExplorer.rootPath);
        assertEquals("/path/to/directory", fspathExplorer.rootPath.getPath());
        assertEquals("//dir[@name='foo']", fspathExplorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithoutRootPath() {
        String[] args = {"-find", "//dir[@name='foo']"};
        fspathExplorer.configureExplorer(args);
        assertNull(fspathExplorer.rootPath);
        assertEquals("//dir[@name='foo']", fspathExplorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        fspathExplorer.rootPath = new File("/path/to/directory");
        fspathExplorer.createFSPath();
        assertNotNull(fspathExplorer.fsPath);
        assertEquals("/path/to/directory", fspathExplorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        fspathExplorer.createFSPath();
        assertNotNull(fspathExplorer.fsPath);
    }
    
    @Test
    public void testStartExitCommand() {
        String input = "exit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        fspathExplorer.start();
        assertEquals("exiting.....\n", outputStream.toString());
    }
    
    @Test
    public void testStartPwdCommand() {
        String input = "pwd\nexit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        fspathExplorer.start();
        assertEquals("Current directory : \n", outputStream.toString());
    }
    
    @Test
    public void testStartCdCommand() {
        String input = "cd /path/to/directory\nexit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        fspathExplorer.start();
        assertEquals("changed to : /path/to/directory\n", outputStream.toString());
    }
    
    @Test
    public void testStartInvalidCdCommand() {
        String input = "cd /invalid/path\nexit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        fspathExplorer.start();
        assertEquals("unable to change to /invalid/path please make sure that the directory exists and is readable\n", outputStream.toString());
    }
    
    @Test
    public void testStartHelpCommand() {
        String input = "help\nexit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        fspathExplorer.start();
        assertEquals("FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\nAttributes : (see javadoc for java.io.File for more info)\n\n@length\n@modificationTime\n@creationTime\n@lastAccessTime\n\nCommands :\n\nexit\t: Exit the application\npwd\t: Print the current directory\nhelp\t: Show help\n\n", outputStream.toString());
    }
    
    @Test
    public void testStartInvalidCommand() {
        String input = "invalid\nexit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        fspathExplorer.start();
        assertEquals("", outputStream.toString());
        assertEquals("invalid\n", errorStream.toString());
    }
    
    @Test
    public void testStartFSPathQuery() {
        String input = "//dir[@name='foo']\nexit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        fspathExplorer.start();
        assertEquals("", errorStream.toString());
    }
    
    @Test
    public void testStartEmptyInput() {
        String input = "\nexit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        fspathExplorer.start();
        assertEquals("", errorStream.toString());
    }
}