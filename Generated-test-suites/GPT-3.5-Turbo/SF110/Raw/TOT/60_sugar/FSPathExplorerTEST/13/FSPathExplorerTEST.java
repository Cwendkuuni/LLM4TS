import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    
    @Test
    public void testMain_noArguments_printsWelcomeMessage() {
        FSPathExplorer.main(new String[]{});
        String expectedOutput = "\n+-------------------------------------------------------+\n" +
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
        
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_invalidArguments_printsUsage() {
        FSPathExplorer.main(new String[]{});
        String expectedOutput = "usage : java -jar FSPathExplorer [options] <-f|-find> <FSPath_query>\n\n" +
            "\t Options : \n" +
            "\t\t -path <root_path> : the relative or absolute path of the directory to start from.\n" +
            "\t\t -p <root_path> :  see -path.\n\n" +
            "\t Example FSPath queries :\n" +
            "\t\t //dir[@name = 'var']/dir[@name = 'www']/dir[@name = 'htdocs']/file[contains(name, '.html')]\n";
        
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_validArguments_printsCurrentDirectory() {
        FSPathExplorer.main(new String[]{"-p", "/path/to/dir", "-f", "//file"});
        String expectedOutput = "Current directory : /path/to/dir\n";
        
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_validArguments_changesDirectoryIfExistAndReadable() {
        FSPathExplorer.main(new String[]{"-p", "/path/to/existing", "-f", "//file"});
        String expectedOutput = "changed to : /path/to/existing\n";
        
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_validArguments_doesNotChangeDirectoryIfNotExist() {
        FSPathExplorer.main(new String[]{"-p", "/path/to/nonexisting", "-f", "//file"});
        String expectedOutput = "unable to change to /path/to/nonexisting please make sure that the directory exists and is readable\n";
        
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_invalidQuery_printsError() {
        FSPathExplorer.main(new String[]{"-p", "/path/to/dir", "-f", "//"});
        String expectedOutput = "Error executing query. Please make sure the query is valid.\n";
        
        assertEquals(expectedOutput, outContent.toString());
    }
    
    // Additional test cases can be added for each method in FSPathExplorer class
    
}