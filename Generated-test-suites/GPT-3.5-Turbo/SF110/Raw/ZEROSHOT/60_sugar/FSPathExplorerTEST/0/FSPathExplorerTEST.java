import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.cli.Prompt;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    
    @Before
    public void setup() {
        explorer = new FSPathExplorer();
    }
    
    @Test
    public void testConfigureExplorer_withNoArgs_shouldNotSetRootPathAndFsPathQuery() {
        String[] args = {};
        
        explorer.configureExplorer(args);
        
        Assert.assertNull(explorer.rootPath);
        Assert.assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorer_withRootPathArg_shouldSetRootPath() {
        String[] args = {"-path", "/path/to/directory"};
        
        explorer.configureExplorer(args);
        
        Assert.assertEquals(new File("/path/to/directory"), explorer.rootPath);
    }
    
    @Test
    public void testConfigureExplorer_withFsPathQueryArg_shouldSetFsPathQuery() {
        String[] args = {"-find", "//dir[@name='foo']"};
        
        explorer.configureExplorer(args);
        
        Assert.assertEquals("//dir[@name='foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPath_withRootPathSet_shouldCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/path/to/directory");
        
        explorer.createFSPath();
        
        Assert.assertEquals(FSPathFactory.newFSPath(explorer.rootPath), explorer.fsPath);
    }
    
    @Test
    public void testCreateFSPath_withNoRootPathSet_shouldCreateFSPathWithoutRootPath() {
        explorer.rootPath = null;
        
        explorer.createFSPath();
        
        Assert.assertEquals(FSPathFactory.newFSPath(), explorer.fsPath);
    }
    
    @Test
    public void testStart_withExitCommand_shouldClosePromptAndPrintExitingMessage() {
        Prompt prompt = new Prompt(System.in, System.out, System.err);
        explorer.prompt = prompt;
        
        explorer.start();
        
        Assert.assertTrue(prompt.isClosed());
        Assert.assertEquals("exiting.....\n", prompt.getOutput());
    }
    
    @Test
    public void testStart_withPwdCommand_shouldPrintCurrentDirectory() {
        Prompt prompt = new Prompt(System.in, System.out, System.err);
        explorer.prompt = prompt;
        explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/directory"));
        
        prompt.setInput("pwd\nexit\n");
        
        explorer.start();
        
        Assert.assertEquals("Current directory : /path/to/directory\n", prompt.getOutput());
    }
    
    @Test
    public void testStart_withCdCommandAndValidDirectory_shouldChangeToNewDirectory() {
        Prompt prompt = new Prompt(System.in, System.out, System.err);
        explorer.prompt = prompt;
        explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/directory"));
        
        prompt.setInput("cd /new/path\npwd\nexit\n");
        
        explorer.start();
        
        Assert.assertEquals("changed to : /new/path\nCurrent directory : /new/path\n", prompt.getOutput());
    }
    
    @Test
    public void testStart_withCdCommandAndInvalidDirectory_shouldNotChangeDirectory() {
        Prompt prompt = new Prompt(System.in, System.out, System.err);
        explorer.prompt = prompt;
        explorer.fsPath = FSPathFactory.newFSPath(new File("/path/to/directory"));
        
        prompt.setInput("cd /nonexistent/directory\npwd\nexit\n");
        
        explorer.start();
        
        Assert.assertEquals("unable to change to /nonexistent/directory please make sure that the directory exists and is readable\nCurrent directory : /path/to/directory\n", prompt.getOutput());
    }
    
    @Test
    public void testStart_withHelpCommand_shouldPrintHelp() {
        Prompt prompt = new Prompt(System.in, System.out, System.err);
        explorer.prompt = prompt;
        
        prompt.setInput("help\nexit\n");
        
        explorer.start();
        
        Assert.assertTrue(prompt.getOutput().contains("FSPathExplorer help :\n"));
    }
    
    @Test
    public void testStart_withFsPathQuery_shouldExecuteQueryAndPrintResults() {
        Prompt prompt = new Prompt(System.in, System.out, System.err);
        FSPathResultList results = new FSPathResultList();
        results.add(new FSPathResult(new File("file1.txt")));
        results.add(new FSPathResult(new File("file2.txt")));
        
        explorer.prompt = prompt;
        explorer.fsPath = new FSPath() {
            @Override
            public FSPathResultList query(String query) {
                return results;
            }
        };
        
        prompt.setInput("//dir[@name='foo']\nexit\n");
        
        explorer.start();
        
        Assert.assertEquals("file1.txt\nfile2.txt\n", prompt.getOutput());
    }
}