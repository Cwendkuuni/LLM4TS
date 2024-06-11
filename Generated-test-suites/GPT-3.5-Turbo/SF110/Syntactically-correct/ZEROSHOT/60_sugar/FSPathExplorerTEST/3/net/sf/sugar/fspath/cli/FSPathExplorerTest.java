import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }
    
    @Test
    public void testMain() {
        String[] args = new String[1];
        args[0] = "test";
        
        final InputStream originalIn = System.in;
        final PrintStream originalOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        String input = "exit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(outContent));
        
        FSPathExplorer.main(args);
        
        assertEquals("exiting.....\n", outContent.toString());
        
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
    
    @Test
    public void testConfigureExplorerWithArgs() {
        String[] args = new String[4];
        args[0] = "-path";
        args[1] = "root";
        args[2] = "-find";
        args[3] = "//dir[@name = 'test']";
        
        explorer.configureExplorer(args);
        assertEquals("root", explorer.rootPath.getName());
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithoutArgs() {
        String[] args = {};
        
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("root");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testStartExit() {
        final InputStream originalIn = System.in;
        final PrintStream originalOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        String input = "exit";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(outContent));
        
        explorer.start();
        
        assertEquals("Current directory : \nexiting.....\n", outContent.toString());
        
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
    
    @Test
    public void testStartPwd() {
        final InputStream originalIn = System.in;
        final PrintStream originalOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        String input = "pwd";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(outContent));
        
        explorer.start();
        
        assertEquals("Current directory : \n", outContent.toString());
        
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
    
    @Test
    public void testStartCd() {
        final InputStream originalIn = System.in;
        final PrintStream originalOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        File tempDir = new File("temp");
        tempDir.mkdir();
        
        String input = "cd temp";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(outContent));
        
        explorer.start();
        
        assertEquals("changed to : " + tempDir.getAbsolutePath() + "\n", outContent.toString());
        
        System.setIn(originalIn);
        System.setOut(originalOut);
        
        tempDir.delete();
    }
    
    @Test
    public void testStartHelp() {
        final InputStream originalIn = System.in;
        final PrintStream originalOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        String input = "help";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(outContent));
        
        explorer.start();
        
        String expectedOutput = "FSPathExplorer help :\n" +
                "Enter an FSPath query to search the filesystem\n" +
                "\n" +
                "Examples :\n" +
                "\n" +
                "1) //dir[@name = 'foo'] \n" +
                "   finds any directory under the current directory named 'foo'\n" +
                "\n" +
                "2) //dir[@name = 'src']//file[@length > 10000] \n" +
                "   finds any file which is nested anywhere below a 'src' directory\n" +
                "   with a file size greater than 10000 bytes\n" +
                "\n" +
                "3) //dir[fs:matches(@name, '^[.].*')] \n" +
                "   finds all directories in the directory sructure which begin with a '.'\n" +
                "\n" +
                "Attributes : (see javadoc for java.io.File for more info)\n" +
                "\n" +
                "@absolute\n" +
                "@canonical_path\n" +
                "@last_modified\n" +
                "@length\n" +
                "@name\n" +
                "\n" +
                "Commands :\n" +
                "\n" +
                "exit\t: Exit the application\n" +
                "pwd\t: Show the current directory\n" +
                "cd\t: Change the current directory\n" +
                "help\t: Show the help menu\n";
        
        assertEquals(expectedOutput, outContent.toString());
        
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
    
    @Test
    public void testStartQuery() {
        final InputStream originalIn = System.in;
        final PrintStream originalOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        String input = "//dir[@name = 'test']";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(outContent));
        
        explorer.start();
        
        assertEquals("\n", outContent.toString());
        
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
    
}