import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }
    
    @Test
    public void testConfigureExplorerWithRootPath() {
        String[] args = {"-path", "/root", "-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        
        assertEquals(new File("/root"), explorer.rootPath);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithoutRootPath() {
        String[] args = {"-find", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithInvalidArguments() {
        String[] args = {"-invalid", "//dir[@name = 'test']"};
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/root");
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertEquals(new File("/root"), explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertEquals(System.getProperty("user.dir"), explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testStartWithExitCommand() {
        // Mocking the prompt.readLine() method to simulate entering "exit"
        explorer.prompt = Mockito.mock(Prompt.class);
        Mockito.when(explorer.prompt.readLine()).thenReturn("exit");
        
        explorer.start();
        
        // Verify that the prompt.close() method is called
        Mockito.verify(explorer.prompt).close();
        
        // Verify that the out.println() method is called with the correct message
        Mockito.verify(explorer.out).println("exiting.....");
    }
    
    @Test
    public void testStartWithPWDCommand() {
        // Mocking the prompt.readLine() method to simulate entering "pwd"
        explorer.prompt = Mockito.mock(Prompt.class);
        Mockito.when(explorer.prompt.readLine()).thenReturn("pwd");
        
        // Mocking the System.getProperty("user.dir") method to return "/root"
        System.setProperty("user.dir", "/root");
        
        explorer.start();
        
        // Verify that the out.println() method is called with the correct message
        Mockito.verify(explorer.out).println("Current directory : /root");
    }
    
    @Test
    public void testStartWithCDCommandValidDirectory() {
        // Mocking the prompt.readLine() method to simulate entering "cd /test"
        explorer.prompt = Mockito.mock(Prompt.class);
        Mockito.when(explorer.prompt.readLine()).thenReturn("cd /test");
        
        // Mocking the File.exists(), File.isDirectory(), and File.canRead() methods to return true
        File newDir = new File("/test");
        File mockedDir = Mockito.spy(newDir);
        Mockito.when(mockedDir.exists()).thenReturn(true);
        Mockito.when(mockedDir.isDirectory()).thenReturn(true);
        Mockito.when(mockedDir.canRead()).thenReturn(true);
        
        // Mocking the FSPathFactory.newFSPath() method to return a mocked FSPath object
        FSPath mockedFSPath = Mockito.mock(FSPath.class);
        Mockito.when(FSPathFactory.newFSPath(Mockito.any(File.class))).thenReturn(mockedFSPath);
        
        explorer.start();
        
        // Verify that the FSPathFactory.newFSPath() method is called with the correct argument
        Mockito.verify(FSPathFactory).newFSPath(mockedDir);
        
        // Verify that the out.println() method is called with the correct message
        Mockito.verify(explorer.out).println("changed to : /test");
    }
    
    @Test
    public void testStartWithCDCommandInvalidDirectory() {
        // Mocking the prompt.readLine() method to simulate entering "cd /invalid"
        explorer.prompt = Mockito.mock(Prompt.class);
        Mockito.when(explorer.prompt.readLine()).thenReturn("cd /invalid");
        
        // Mocking the File.exists(), File.isDirectory(), and File.canRead() methods to return false
        File newDir = new File("/invalid");
        File mockedDir = Mockito.spy(newDir);
        Mockito.when(mockedDir.exists()).thenReturn(false);
        Mockito.when(mockedDir.isDirectory()).thenReturn(false);
        Mockito.when(mockedDir.canRead()).thenReturn(false);
        
        explorer.start();
        
        // Verify that the FSPathFactory.newFSPath() method is not called
        Mockito.verify(FSPathFactory, Mockito.never()).newFSPath(Mockito.any(File.class));
        
        // Verify that the out.println() method is called with the correct message
        Mockito.verify(explorer.out).println("unable to change to /invalid please make sure that the directory exists and is readable");
    }
    
    @Test
    public void testStartWithHelpCommand() {
        // Mocking the prompt.readLine() method to simulate entering "help"
        explorer.prompt = Mockito.mock(Prompt.class);
        Mockito.when(explorer.prompt.readLine()).thenReturn("help");
        
        explorer.start();
        
        // Verify that the showHelp() method is called
        Mockito.verify(explorer).showHelp();
    }
    
    @Test
    public void testStartWithFSPathQuery() {
        // Mocking the prompt.readLine() method to simulate entering "//dir[@name = 'test']"
        explorer.prompt = Mockito.mock(Prompt.class);
        Mockito.when(explorer.prompt.readLine()).thenReturn("//dir[@name = 'test']");
        
        // Mocking the FSPath.query() method to return a mocked FSPathResultList object
        FSPathResultList mockedResults = Mockito.mock(FSPathResultList.class);
        Mockito.when(explorer.fsPath.query("//dir[@name = 'test']")).thenReturn(mockedResults);
        
        File file1 = Mockito.mock(File.class);
        Mockito.when(file1.getPath()).thenReturn("/root/dir1");
        
        File file2 = Mockito.mock(File.class);
        Mockito.when(file2.getPath()).thenReturn("/root/dir2");
        
        Mockito.when(mockedResults.iterator()).thenReturn(Arrays.asList(file1, file2).iterator());
        
        explorer.start();
        
        // Verify that the FSPath.query() method is called with the correct argument
        Mockito.verify(explorer.fsPath).query("//dir[@name = 'test']");
        
        // Verify that the out.println() method is called with the correct messages
        Mockito.verify(explorer.out).println("/root/dir1");
        Mockito.verify(explorer.out).println("/root/dir2");
    }
}