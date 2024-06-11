import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

@RunWith(MockitoJUnitRunner.class)
public class FSPathExplorerTest {
    
    @Mock
    private PrintStream mockOut;
    
    @Mock
    private PrintStream mockErr;
    
    @Mock
    private Prompt mockPrompt;
    
    @InjectMocks
    private FSPathExplorer explorer;
    
    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assert(explorer.out == System.out);
        assert(explorer.err == System.err);
        assert(explorer.prompt != null);
    }
    
    @Test
    public void testConfigureExplorer_RootPathArgument() {
        String[] args = new String[]{"-path", "/root", "-find", "//file[@name = 'test.txt']"};
        explorer.configureExplorer(args);
        assert(explorer.rootPath != null);
        assert(explorer.rootPath.getAbsolutePath().equals("/root"));
    }
    
    @Test
    public void testConfigureExplorer_QueryArgument() {
        String[] args = new String[]{"-find", "//file[@name = 'test.txt']"};
        explorer.configureExplorer(args);
        assert(explorer.fsPathQuery.equals("//file[@name = 'test.txt']"));
    }
    
    @Test
    public void testConfigureExplorer_NoArguments() {
        String[] args = new String[]{};
        explorer.configureExplorer(args);
        assert(explorer.rootPath == null);
        assert(explorer.fsPathQuery.equals(""));
    }
    
    @Test
    public void testCreateFSPath_WithRootPath() {
        File rootPath = new File("/root");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        assert(explorer.fsPath != null);
        assert(explorer.fsPath.getRootDirectory().equals(rootPath));
    }
    
    @Test
    public void testCreateFSPath_NoRootPath() {
        explorer.rootPath = null;
        explorer.createFSPath();
        assert(explorer.fsPath != null);
    }
    
    @Test
    public void testStart_ExitCommand() throws IOException {
        Mockito.when(mockPrompt.readLine()).thenReturn("exit");
        
        explorer.prompt = mockPrompt;
        
        explorer.start();
        
        Mockito.verify(mockPrompt).close();
        Mockito.verify(mockOut).println("exiting.....");
    }
    
    @Test
    public void testStart_PwdCommand() throws IOException {
        Mockito.when(mockPrompt.readLine()).thenReturn("pwd");
        Mockito.when(explorer.fsPath.getRootDirectory()).thenReturn(new File("/root"));
        
        explorer.prompt = mockPrompt;
        explorer.out = mockOut;
        
        explorer.start();
        
        Mockito.verify(mockOut).println("Current directory : /root");
    }
    
    @Test
    public void testStart_CdCommand_ExistingDirectory() throws IOException {
        Mockito.when(mockPrompt.readLine()).thenReturn("cd /newdir");
        Mockito.when(explorer.fsPath.getRootDirectory()).thenReturn(new File("/root"));
        Mockito.when(mockPrompt.split(" ")).thenReturn(new String[]{"cd", "/newdir"});
        Mockito.when(explorer.mockRootPath.isDirectory()).thenReturn(true);
        Mockito.when(explorer.mockRootPath.canRead()).thenReturn(true);
        FSPath mockFSPath = Mockito.mock(FSPath.class);
        Mockito.when(FSPathFactory.newFSPath(explorer.mockRootPath)).thenReturn(mockFSPath);
        
        explorer.prompt = mockPrompt;
        explorer.out = mockOut;
        
        explorer.start();
        
        Mockito.verify(mockOut).println("changed to /newdir");
    }
    
    @Test
    public void testStart_CdCommand_NonExistingDirectory() throws IOException {
        Mockito.when(mockPrompt.readLine()).thenReturn("cd /nonexistingdir");
        Mockito.when(explorer.fsPath.getRootDirectory()).thenReturn(new File("/root"));
        Mockito.when(mockPrompt.split(" ")).thenReturn(new String[]{"cd", "/nonexistingdir"});
        Mockito.when(explorer.mockRootPath.exists()).thenReturn(false);
        
        explorer.prompt = mockPrompt;
        explorer.out = mockOut;
        
        explorer.start();
        
        Mockito.verify(mockOut).println("unable to change to /nonexistingdir please make sure that the directory exitst and a is readable");
    }
    
    @Test
    public void testStart_HelpCommand() throws IOException {
        Mockito.when(mockPrompt.readLine()).thenReturn("help");
        
        explorer.prompt = mockPrompt;
        explorer.out = mockOut;
        
        explorer.start();
        
        Mockito.verify(mockOut).println(Mockito.contains("FSPathExplorer help"));
    }
    
    @Test
    public void testStart_FsPathQuery() throws IOException {
        Mockito.when(mockPrompt.readLine()).thenReturn("//file[@name = 'test.txt']");
        FSPath mockFSPath = Mockito.mock(FSPath.class);
        FSPathResultList mockResults = Mockito.mock(FSPathResultList.class);
        FSPathResult mockResult = Mockito.mock(FSPathResult.class);
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockResult.getFile()).thenReturn(mockFile);
        Mockito.when(mockResults.iterator()).thenReturn(Arrays.asList(mockResult).iterator());
        Mockito.when(mockFSPath.query("//file[@name = 'test.txt']")).thenReturn(mockResults);
        Mockito.when(FSPathFactory.newFSPath()).thenReturn(mockFSPath);
        
        explorer.prompt = mockPrompt;
        explorer.out = mockOut;
        
        explorer.start();
        
        Mockito.verify(mockOut).println(Mockito.anyString());
    }
}