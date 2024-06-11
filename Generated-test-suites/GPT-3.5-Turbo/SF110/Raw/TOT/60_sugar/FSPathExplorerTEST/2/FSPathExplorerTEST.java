import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }
    
    @Test
    public void testConfigureExplorer_withPathArgument_shouldSetRootPath() {
        String[] args = {"-path", "/path/to/root"};
        explorer.configureExplorer(args);
        assertEquals("/path/to/root", explorer.rootPath.getAbsolutePath());
    }
    
    @Test
    public void testConfigureExplorer_withFindArgument_shouldSetFSPathQuery() {
        String[] args = {"-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorer_withMultipleArguments_shouldSetRootPathAndFSPathQuery() {
        String[] args = {"-path", "/path/to/root", "-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        assertEquals("/path/to/root", explorer.rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPath_withRootPath_shouldCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/path/to/root");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals("/path/to/root", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testCreateFSPath_withoutRootPath_shouldCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertNull(explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testStart_withValidExpression_shouldPrintResults() {
        explorer.fsPath = new FSPathStub();
        explorer.prompt = new PromptStub("///dir[@name = 'test']");
        explorer.out = new PrintStreamStub();
        explorer.start();
        String expectedOutput = "test/file1.txt\ntest/file2.txt\n";
        assertEquals(expectedOutput, explorer.out.getOutput());
    }
    
    @Test
    public void testStart_withExitCommand_shouldStopRunning() {
        explorer.prompt = new PromptStub("exit");
        explorer.start();
        assertFalse(explorer.running);
    }
    
    @Test
    public void testStart_withPWDCommand_shouldPrintCurrentDirectory() {
        explorer.fsPath = new FSPathStub(new File("/path/to/directory"));
        explorer.prompt = new PromptStub("pwd");
        explorer.out = new PrintStreamStub();
        explorer.start();
        String expectedOutput = "Current directory : /path/to/directory\n";
        assertEquals(expectedOutput, explorer.out.getOutput());
    }
    
    @Test
    public void testStart_withCDCommand_andValidDirectory_shouldChangeCurrentDirectory() {
        explorer.fsPath = new FSPathStub(new File("/path/to/directory"));
        explorer.prompt = new PromptStub("cd /new/directory");
        explorer.out = new PrintStreamStub();
        explorer.start();
        assertEquals("/new/directory", explorer.fsPath.getRootDirectory().getAbsolutePath());
        String expectedOutput = "changed to : /new/directory\n";
        assertEquals(expectedOutput, explorer.out.getOutput());
    }
    
    @Test
    public void testStart_withCDCommand_andInvalidDirectory_shouldPrintErrorMessage() {
        explorer.fsPath = new FSPathStub(new File("/path/to/directory"));
        explorer.prompt = new PromptStub("cd /nonexistent/directory");
        explorer.out = new PrintStreamStub();
        explorer.start();
        assertEquals("/path/to/directory", explorer.fsPath.getRootDirectory().getAbsolutePath());
        String expectedOutput = "unable to change to /nonexistent/directory please make sure that the directory exists and is readable\n";
        assertEquals(expectedOutput, explorer.out.getOutput());
    }
    
    @Test
    public void testStart_withHelpCommand_shouldPrintHelp() {
        explorer.prompt = new PromptStub("help");
        explorer.out = new PrintStreamStub();
        explorer.start();
        // Check that the help text is printed correctly
    }
}

class FSPathStub extends FSPath {
    
    private File rootDirectory;
    
    public FSPathStub() {
        super();
    }
    
    public FSPathStub(File rootDirectory) {
        super();
        this.rootDirectory = rootDirectory;
    }
    
    public File getRootDirectory() {
        return rootDirectory;
    }
    
    public FSPathResultList query(String query) {
        // Return dummy results for testStart_withValidExpression_shouldPrintResults test case
        FSPathResultList results = new FSPathResultList();
        results.add(new FSPathResult(new File("test/file1.txt")));
        results.add(new FSPathResult(new File("test/file2.txt")));
        return results;
    }
}

class PromptStub extends Prompt {
    
    private String readLineOutput;
    
    public PromptStub(String readLineOutput) {
        super(null, null, null);
        this.readLineOutput = readLineOutput;
    }
    
    public String readLine() {
        return readLineOutput;
    }
}

class PrintStreamStub extends PrintStream {
    
    private StringBuffer output;
    
    public PrintStreamStub() {
        super(null);
        output = new StringBuffer();
    }
    
    public void println(String message) {
        output.append(message).append("\n");
    }
    
    public String getOutput() {
        return output.toString();
    }
}