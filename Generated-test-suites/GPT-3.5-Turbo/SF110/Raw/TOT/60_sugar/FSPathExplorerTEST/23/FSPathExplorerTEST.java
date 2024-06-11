import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        explorer.out = new PrintStream(outContent);
        explorer.err = new PrintStream(errContent);
    }
    
    @Test
    public void testConfigureExplorer_PathArgument() {
        String[] args = {"-path", "/path/to/dir"};
        explorer.configureExplorer(args);
        File expectedRootPath = new File("/path/to/dir");
        assertEquals(expectedRootPath, explorer.rootPath);
    }
    
    @Test
    public void testConfigureExplorer_FindArgument() {
        String[] args = {"-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        String expectedFsPathQuery = "//dir[@name = 'test']";
        assertEquals(expectedFsPathQuery, explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorer_PathArgumentAndFindArgument() {
        String[] args = {"-path", "/path/to/dir", "-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        File expectedRootPath = new File("/path/to/dir");
        assertEquals(expectedRootPath, explorer.rootPath);
        String expectedFsPathQuery = "//dir[@name = 'test']";
        assertEquals(expectedFsPathQuery, explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPath_RootPathNotNull() {
        File rootPath = new File("/path/to/dir");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        assertEquals(rootPath, explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testCreateFSPath_RootPathNull() {
        explorer.createFSPath();
        assertTrue(!explorer.fsPath.getRootDirectory().exists());
    }
    
    @Test
    public void testStart_ExecuteCommandExit() {
        String input = "exit";
        String expectedOutput = "exiting.....";
        simulateUserInput(input);
        explorer.start();
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test
    public void testStart_ExecuteCommandPwd() {
        String input = "pwd";
        String expectedOutput = "Current directory : ";
        simulateUserInput(input);
        explorer.start();
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test
    public void testStart_ExecuteCommandCdWithValidDirectory() {
        String input = "cd /path/to/dir";
        String expectedOutput = "changed to : ";
        simulateUserInput(input);
        explorer.start();
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test
    public void testStart_ExecuteCommandCdWithInvalidDirectory() {
        String input = "cd /invalid/path";
        String expectedOutput = "unable to change to";
        simulateUserInput(input);
        explorer.start();
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test
    public void testStart_ExecuteCommandHelp() {
        String input = "help";
        String expectedOutput = "FSPathExplorer help";
        simulateUserInput(input);
        explorer.start();
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test
    public void testStart_ExecuteFsPathQuery() {
        String input = "//dir[@name = 'test']";
        List<String> expectedOutput = Arrays.asList("file1", "file2", "file3");
        simulateUserInput(input);
        explorer.start();
        for (String file : expectedOutput) {
            assertTrue(outContent.toString().contains(file));
        }
    }
    
    // Helper method to simulate user input
    private void simulateUserInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
    
}