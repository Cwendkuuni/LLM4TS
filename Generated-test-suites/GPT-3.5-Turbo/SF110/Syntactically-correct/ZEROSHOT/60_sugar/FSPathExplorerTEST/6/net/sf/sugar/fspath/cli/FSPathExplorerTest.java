import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        
        originalOut = System.out;
        originalErr = System.err;
        
        outputStream = new ByteArrayOutputStream();
        errorStream = new ByteArrayOutputStream();
        
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorStream));
    }
    
    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    @Test
    public void testShowWelcome() {
        FSPathExplorer.showWelcome();
        
        assertEquals("\n+-------------------------------------------------------+\n" +
                     "|                                                       |\n" +
                     "| FSPathExplorer                                        |\n" +
                     "|                                                       |\n" +
                     "+-------------------------------------------------------+\n" +
                     "\nType 'help' for commands...\n" +
                     "\nScanning filesystem metadata...\n" +
                     "\n+-------------------------------------------------------+\n" +
                     "| PLEASE NOTE running FSPathExplorer from the top level |\n" +
                     "| of your filesystem is not advised                     |\n" +
                     "+-------------------------------------------------------+\n",
                     outputStream.toString());
    }
    
    @Test
    public void testShowHelp() {
        FSPathExplorer.showHelp();
        
        String expectedOutput = "FSPathExplorer help :\n" +
                        "\n" +
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
                        "Attributes : (see javadoc for java.io.File for more info)\n";
        
        for (FSPathAttributes a : FSPathAttributes.values()) {
            if (!(a.name().equals("dir") || a.name().equals("file"))) {
                expectedOutput += ("\n@" + a);
            }
        }
        
        expectedOutput += "\n\nCommands :\n";
        
        for (Commands c : Commands.values()) {
            expectedOutput += ("\n" + c.getCommandText() + "\t: " + c.getDescription());
        }
        
        assertEquals(expectedOutput, outputStream.toString());
    }
    
    @Test
    public void testConfigureExplorer() {
        String[] args = {"-path", "/path/to/directory", "-f", "//dir[@name = 'foo']"};
        
        explorer.configureExplorer(args);
        
        assertEquals("/path/to/directory", explorer.rootPath.getPath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPath() {
        File rootPath = new File("/path/to/directory");
        explorer.rootPath = rootPath;
        
        explorer.createFSPath();
        
        assertEquals(rootPath, explorer.fsPath.getRootDirectory());
        
        explorer.rootPath = null;
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath.getRootDirectory());
        assertEquals(System.getProperty("user.dir"), explorer.fsPath.getRootDirectory().getPath());
    }
    
    @Test
    public void testStartExitCommand() throws IOException {
        String[] args = {};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        
        Prompt mockPrompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() {
                return "exit";
            }
        };
        explorer.prompt = mockPrompt;
        
        explorer.start();
        
        assertTrue(explorer.prompt.isClosed());
        assertEquals("exiting.....\n", outputStream.toString());
    }
    
    @Test
    public void testStartPwdCommand() throws IOException {
        String[] args = {};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        
        Prompt mockPrompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() {
                return "pwd";
            }
        };
        explorer.prompt = mockPrompt;
        
        explorer.start();
        
        assertFalse(explorer.prompt.isClosed());
        assertEquals("Current directory : " + explorer.fsPath.getRootDirectory().getAbsolutePath() + "\n", outputStream.toString());
    }
    
    @Test
    public void testStartCdCommand() throws IOException {
        String[] args = {};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        
        Prompt mockPrompt = new Prompt(System.in, System.out, System.err) {
            private int count = 0;
            
            public String readLine() {
                if (count == 0) {
                    count++;
                    return "cd /new/dir";
                }
                return "exit";
            }
        };
        explorer.prompt = mockPrompt;
        
        explorer.start();
        
        assertTrue(explorer.prompt.isClosed());
        assertEquals("changed to : /new/dir\nexiting.....\n", outputStream.toString());
    }
    
    @Test
    public void testStartHelpCommand() throws IOException {
        String[] args = {};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        
        Prompt mockPrompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() {
                return "help";
            }
        };
        explorer.prompt = mockPrompt;
        
        explorer.start();
        
        assertFalse(explorer.prompt.isClosed());
        
        String expectedOutput = "FSPathExplorer help :\n" +
                        "\n" +
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
                        "Attributes : (see javadoc for java.io.File for more info)\n";
        
        for (FSPathAttributes a : FSPathAttributes.values()) {
            if (!(a.name().equals("dir") || a.name().equals("file"))) {
                expectedOutput += ("\n@" + a);
            }
        }
        
        expectedOutput += "\n\nCommands :\n";
        
        for (Commands c : Commands.values()) {
            expectedOutput += ("\n" + c.getCommandText() + "\t: " + c.getDescription());
        }
        
        assertEquals(expectedOutput + "\n", outputStream.toString());
    }
    
    @Test
    public void testStartFSPathQuery() throws IOException {
        String[] args = {};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        
        Prompt mockPrompt = new Prompt(System.in, System.out, System.err) {
            private int count = 0;
            
            public String readLine() {
                if (count == 0) {
                    count++;
                    return "//dir[@name = 'foo']";
                }
                return "exit";
            }
        };
        explorer.prompt = mockPrompt;
        
        FSPathResultList mockResults = new FSPathResultList();
        mockResults.add(new FSPathResult(new File("/path/to/file")));
        explorer.fsPath = new FSPath() {
            public FSPathResultList query(String query) {
                return mockResults;
            }
        };
        
        explorer.start();
        
        assertTrue(explorer.prompt.isClosed());
        
        assertEquals("/path/to/file\nexiting.....\n", outputStream.toString());
    }
    
    @Test
    public void testStartFSPathQueryNoResults() throws IOException {
        String[] args = {};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        
        Prompt mockPrompt = new Prompt(System.in, System.out, System.err) {
            private int count = 0;
            
            public String readLine() {
                if (count == 0) {
                    count++;
                    return "//dir[@name = 'foo']";
                }
                return "exit";
            }
        };
        explorer.prompt = mockPrompt;
        
        FSPathResultList mockResults = new FSPathResultList();
        explorer.fsPath = new FSPath() {
            public FSPathResultList query(String query) {
                return mockResults;
            }
        };
        
        explorer.start();
        
        assertTrue(explorer.prompt.isClosed());
        
        assertEquals("exiting.....\n", outputStream.toString());
    }
}