import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    private FSPathExplorer fsPathExplorer;
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        
        fsPathExplorer = new FSPathExplorer();
    }
    
    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }
    
    @Test
    public void testSetRootPath() {
        File rootPath = new File("testDirectory");
        fsPathExplorer.setRootPath(rootPath);
        
        assertEquals(rootPath, fsPathExplorer.getRootPath());
    }
    
    @Test
    public void testSetFSPathQuery() {
        String fsPathQuery = "//dir[@name = 'test']";
        fsPathExplorer.setFSPathQuery(fsPathQuery);
        
        assertEquals(fsPathQuery, fsPathExplorer.getFSPathQuery());
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        File rootPath = new File("testDirectory");
        fsPathExplorer.setRootPath(rootPath);
        fsPathExplorer.createFSPath();
        
        assertTrue(fsPathExplorer.getFSPath() != null);
        assertEquals(rootPath, fsPathExplorer.getFSPath().getRootDirectory());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        fsPathExplorer.createFSPath();
        
        assertTrue(fsPathExplorer.getFSPath() != null);
        assertEquals(new File("."), fsPathExplorer.getFSPath().getRootDirectory());
    }
    
    @Test
    public void testStart() {
        String input = "help\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        fsPathExplorer.start();
        
        String expectedOutput = "FSPathExplorer help :";
        assertTrue(outContent.toString().startsWith(expectedOutput));
    }
    
    @Test
    public void testStartWithExitCommand() {
        String input = "exit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        fsPathExplorer.start();
        
        assertEquals("exiting.....\n", outContent.toString());
    }
    
    @Test
    public void testStartWithPWDCommand() {
        String input = "pwd\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        fsPathExplorer.start();
        
        String currentDirectory = System.getProperty("user.dir");
        assertEquals("Current directory : " + currentDirectory + "\n", outContent.toString());
    }
    
    @Test
    public void testStartWithCDCommand() {
        String input = "cd testDirectory\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        fsPathExplorer.start();
        
        String expectedOutput = "changed to : " + new File("testDirectory").getAbsolutePath() + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testStartWithInvalidCDCommand() {
        String input = "cd nonExistingDirectory\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        fsPathExplorer.start();
        
        String expectedOutput = "unable to change to nonExistingDirectory please make sure that the directory exists and is readable\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testStartWithInvalidCommand() {
        String input = "invalidCommand\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        fsPathExplorer.start();
        
        assertEquals("", outContent.toString());
    }
    
    @Test
    public void testStartWithFSPathQuery() {
        String input = "//dir[@name = 'test']\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        fsPathExplorer.start();
        
        String expectedOutput = System.getProperty("user.dir") + File.separator + "testDirectory\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}