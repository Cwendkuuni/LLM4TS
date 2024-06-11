import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

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
    public void testConfigureExplorer_withPathOption_shouldSetRootPath() {
        String[] args = {"-path", "test-directory", "-find", "//dir"};
        explorer.configureExplorer(args);
        
        File expectedRootPath = new File("test-directory");
        
        assertEquals(expectedRootPath, explorer.rootPath);
    }
    
    @Test
    public void testConfigureExplorer_withFindOption_shouldSetFSPathQuery() {
        String[] args = {"-path", "test-directory", "-find", "//dir"};
        explorer.configureExplorer(args);
        
        String expectedFSPathQuery = "//dir";
        
        assertEquals(expectedFSPathQuery, explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorer_withMultipleOptions_shouldSetRootPathAndFSPathQuery() {
        String[] args = {"-path", "test-directory", "-find", "//dir"};
        explorer.configureExplorer(args);
        
        File expectedRootPath = new File("test-directory");
        String expectedFSPathQuery = "//dir";
        
        assertEquals(expectedRootPath, explorer.rootPath);
        assertEquals(expectedFSPathQuery, explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorer_withoutOptions_shouldNotSetRootPathAndFSPathQuery() {
        String[] args = {};
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPath_withRootPath_shouldCreateFSPathWithRootPath() {
        File rootPath = new File("test-directory");
        explorer.rootPath = rootPath;
        
        explorer.createFSPath();
        
        assertEquals(rootPath, explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testCreateFSPath_withoutRootPath_shouldCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        
        assertNull(explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testStart_withExitCommand_shouldExitAndClosePrompt() {
        String input = "exit";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        explorer.prompt = new Prompt(in, explorer.out, explorer.err);
        
        explorer.start();
        
        assertTrue(explorer.prompt.isClosed());
        assertEquals("exiting.....\n", outContent.toString());
    }
    
    @Test
    public void testStart_withPwdCommand_shouldPrintCurrentDirectory() {
        String input = "pwd";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        explorer.prompt = new Prompt(in, explorer.out, explorer.err);
        explorer.fsPath = FSPathFactory.newFSPath(new File("test-directory"));
        
        explorer.start();
        
        assertEquals("Current directory : test-directory\n", outContent.toString());
    }
    
    @Test
    public void testStart_withCdCommandAndValidDirectory_shouldChangeToNewDirectory() {
        String input = "cd new-directory";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        explorer.prompt = new Prompt(in, explorer.out, explorer.err);
        explorer.fsPath = FSPathFactory.newFSPath(new File("test-directory"));
        
        explorer.start();
        
        assertEquals("changed to : new-directory\n", outContent.toString());
        assertEquals(new File("new-directory"), explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testStart_withCdCommandAndInvalidDirectory_shouldPrintErrorMessage() {
        String input = "cd invalid-directory";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        explorer.prompt = new Prompt(in, explorer.out, explorer.err);
        explorer.fsPath = FSPathFactory.newFSPath(new File("test-directory"));
        
        explorer.start();
        
        assertEquals("unable to change to invalid-directory please make sure that the directory exists and is readable\n", outContent.toString());
    }
    
    @Test
    public void testStart_withHelpCommand_shouldShowHelp() {
        String input = "help";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        explorer.prompt = new Prompt(in, explorer.out, explorer.err);
        
        explorer.start();
        
        // Make assertions on the help text displayed on console
        // Example: assertEquals("FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n...", outContent.toString());
    }
    
    @Test
    public void testStart_withFSPathQuery_shouldExecuteQueryAndPrintResults() {
        String input = "//dir[@name = 'foo']";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        explorer.prompt = new Prompt(in, explorer.out, explorer.err);
        // Set up FSPath object with query results
        // Example: explorer.fsPath = FSPathFactory.withQueryResults(...);
        
        explorer.start();
        
        // Make assertions on the output printed on console
        // Example: assertEquals("file1\nfile2\n", outContent.toString());
    }
}