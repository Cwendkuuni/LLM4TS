import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private File testDir;
    
    @Before
    public void setUp() throws IOException {
        explorer = new FSPathExplorer();
        testDir = new File("testDir");
        testDir.mkdir();
        explorer.setRootPath(testDir);
        
    }
    
    @After
    public void tearDown() {
        testDir.delete();
    }
    
    @Test
    public void testCreateFSPath_CurrentDirectorySet() {
        explorer.createFSPath();
        assertEquals(testDir, explorer.getFSPath().getRootDirectory());
    }
    
    @Test
    public void testStart_ExitCommand_ClosesPrompt() {
        explorer.start();
        assertTrue(explorer.getPrompt().isClosed());
    }
    
    @Test
    public void testStart_ChangeDirectoryCommand_NewDirectorySet() {
        explorer.start();
        assertEquals(newDir, explorer.getFSPath().getRootDirectory());
    }
    
    @Test
    public void testStart_HelpCommand_ShowsHelp() {
        PrintStream tempOut = explorer.getOut();
        explorer.setOut(new PrintStream("output.txt"));
        
        explorer.start();
        
        String expectedOutput = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\n";
        String actualOutput = getFileContent("output.txt");
        
        assertEquals(expectedOutput, actualOutput);
        
        explorer.setOut(tempOut);
    }
    
    @Test
    public void testStart_FSPathQuery_ResultsDisplayed() {
        PrintStream tempOut = explorer.getOut();
        explorer.setOut(new PrintStream("output.txt"));
        
        explorer.start();
        
        String expectedOutput = testDir.getAbsolutePath();
        String actualOutput = getFileContent("output.txt");
        
        assertEquals(expectedOutput, actualOutput);
        
        explorer.setOut(tempOut);
    }
    
    private String getFileContent(String filePath) {
        // code to read the contents of the file and return as a string
    }
}