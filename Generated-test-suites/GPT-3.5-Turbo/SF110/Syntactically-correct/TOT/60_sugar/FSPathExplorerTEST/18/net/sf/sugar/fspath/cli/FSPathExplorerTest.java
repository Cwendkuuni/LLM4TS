import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

public class FSPathExplorerTest {

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @Before
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testMain_ValidArgs_OutputWelcomeMessage() {
        String[] args = { "-f", "//dir[@name = 'test']" };
        FSPathExplorer.main(args);
        String expected = "\n+-------------------------------------------------------+\n" +
                          "|                                                       |\n" +
                          "| FSPathExplorer                                        |\n" +
                          "|                                                       |\n" +
                          "+-------------------------------------------------------+\n" +
                          "\nType 'help' for commands...\n" +
                          "\nScanning filesystem metadata...\n" +
                          "\n+-------------------------------------------------------+\n" +
                          "| PLEASE NOTE running FSPathExplorer from the top level |\n" +
                          "| of your filesystem is not advised                     |\n" +
                          "+-------------------------------------------------------+\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testMain_NoArgs_OutputUsageMessage() {
        FSPathExplorer.main(new String[] {});
        String expected = "usage : java -jar FSPathExplorer [options] <-f|-find> <FSPath_query>\n\n\t Options : \n\t\t -path <root_path> : the relative or absolute path of the directory to start from.\n\t\t -p <root_path> :  see -path.\n\n\t Example FSPath queries :\n\t\t //dir[@name = 'var']/dir[@name = 'www']/dir[@name = 'htdocs']/file[contains(name, '.html')]\n";
        assertEquals(expected, errContent.toString());
    }

    @Test
    public void testShowWelcome_NoInput_OutputWelcomeMessage() {
        FSPathExplorer.showWelcome();
        String expected = "\n+-------------------------------------------------------+\n" +
                          "|                                                       |\n" +
                          "| FSPathExplorer                                        |\n" +
                          "|                                                       |\n" +
                          "+-------------------------------------------------------+\n" +
                          "\nType 'help' for commands...\n" +
                          "\nScanning filesystem metadata...\n" +
                          "\n+-------------------------------------------------------+\n" +
                          "| PLEASE NOTE running FSPathExplorer from the top level |\n" +
                          "| of your filesystem is not advised                     |\n" +
                          "+-------------------------------------------------------+\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testShowHelp_NoInput_OutputHelpMessage() {
        FSPathExplorer.showHelp();
        String expected = "FSPathExplorer help :\n" +
                          "\nEnter an FSPath query to search the filesystem\n" +
                          "\nExamples :\n" +
                          "\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n" +
                          "\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n" +
                          "   with a file size greater than 10000 bytes\n" +
                          "\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n" +
                          "\n\nAttributes : (see javadoc for java.io.File for more info)\n" +
                          "\n@length\n" +
                          "\n@lastModified\n" +
                          "\n@name\n" +
                          "\n@size\n" +
                          "\n\nCommands :\n" +
                          "\nexit\t: Exits the program\n" +
                          "\npwd\t: Prints the current directory\n" +
                          "\ncd\t: Changes the current directory\n" +
                          "\nhelp\t: Prints the help message\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testConfigureExplorer_ValidArgs_SetRootPathAndFSPathQuery() {
        String[] args = { "-p", "/path/to/directory", "-f", "//dir[@name = 'test']" };
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertEquals(new File("/path/to/directory"), explorer.rootPath);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath_ValidRootPath_SetFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/path/to/directory");
        explorer.createFSPath();
        assertEquals(explorer.rootPath, explorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStart_InputCommandExit_CallSystemExit() {
        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayInputStream in = new ByteArrayInputStream("exit".getBytes());
        System.setIn(in);
        explorer.start();
        assertEquals("exiting.....", outContent.toString());
    }

    @Test
    public void testStart_InputCommandPWD_CallPrintlnWithCurrentDirectory() {
        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayInputStream in = new ByteArrayInputStream("pwd".getBytes());
        System.setIn(in);
        explorer.start();
        File currentDirectory = new File("").getAbsoluteFile();
        assertEquals("Current directory : " + currentDirectory.getAbsolutePath(), outContent.toString());
    }

    @Test
    public void testStart_InputCommandCDWithValidDirectory_ChangeCurrentDirectory() {
        FSPathExplorer explorer = new FSPathExplorer();
        File newDirectory = new File("/new/directory");
        newDirectory.mkdir();
        ByteArrayInputStream in = new ByteArrayInputStream(("cd " + newDirectory.getAbsolutePath()).getBytes());
        System.setIn(in);
        explorer.start();
        assertEquals("changed to : " + newDirectory.getAbsolutePath(), outContent.toString());
        assertEquals(newDirectory, explorer.fsPath.getRootDirectory());
        newDirectory.delete();
    }

    @Test
    public void testStart_InputCommandCDWithInvalidDirectory_PrintErrorMessage() {
        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayInputStream in = new ByteArrayInputStream("cd /invalid/directory".getBytes());
        System.setIn(in);
        explorer.start();
        assertEquals("unable to change to /invalid/directory please make sure that the directory exitst and a is readable", outContent.toString());
    }

    @Test
    public void testStart_InputCommandHelp_CallShowHelp() {
        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayInputStream in = new ByteArrayInputStream("help".getBytes());
        System.setIn(in);
        explorer.start();
        String expected = "FSPathExplorer help :\n" +
                          "\nEnter an FSPath query to search the filesystem\n" +
                          "\nExamples :\n" +
                          "\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n" +
                          "\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n" +
                          "   with a file size greater than 10000 bytes\n" +
                          "\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n" +
                          "\n\nAttributes : (see javadoc for java.io.File for more info)\n" +
                          "\n@length\n" +
                          "\n@lastModified\n" +
                          "\n@name\n" +
                          "\n@size\n" +
                          "\n\nCommands :\n" +
                          "\nexit\t: Exits the program\n" +
                          "\npwd\t: Prints the current directory\n" +
                          "\ncd\t: Changes the current directory\n" +
                          "\nhelp\t: Prints the help message\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testStart_InputFSPathQuery_CallFSPathQueryAndPrintResults() {
        FSPathExplorer explorer = new FSPathExplorer();
        ByteArrayInputStream in = new ByteArrayInputStream("//dir[@name = 'test']".getBytes());
        System.setIn(in);
        explorer.start();
        // TODO: Test query method and results
    }

}