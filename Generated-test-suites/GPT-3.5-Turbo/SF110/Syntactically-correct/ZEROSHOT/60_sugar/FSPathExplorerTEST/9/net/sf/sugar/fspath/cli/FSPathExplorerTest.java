import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class FSPathExplorerTest {
    
    private FSPathExplorer explorer;
    private PrintStream standardOut;
    
    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        standardOut = System.out;
    }
    
    @Test
    public void testConfigureExplorer() {
        String[] args = {"-path", "/path/to/directory", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        
        assertEquals(new File("/path/to/directory"), explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithNoPath() {
        String[] args = {"-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testConfigureExplorerWithNoQuery() {
        String[] args = {"-path", "/path/to/directory"};
        explorer.configureExplorer(args);
        
        assertEquals(new File("/path/to/directory"), explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPathWithRootPath() {
        explorer.rootPath = new File("/path/to/directory");
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
        assertEquals(new File("/path/to/directory"), explorer.fsPath.getRootDirectory());
    }
    
    @Test
    public void testCreateFSPathWithoutRootPath() {
        explorer.createFSPath();
        
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testStartWithExitCommand() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("exit".getBytes());
        System.setIn(in);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
        explorer.prompt = mock(Prompt.class);
        when(explorer.prompt.readLine()).thenReturn("exit");
        
        explorer.start();
        
        verify(explorer.prompt).close();
        assertTrue(out.toString().contains("exiting....."));
        
        System.setIn(System.in);
        System.setOut(standardOut);
    }
    
    @Test
    public void testStartWithPWDCommand() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("pwd\nexit".getBytes());
        System.setIn(in);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
        explorer.prompt = mock(Prompt.class);
        when(explorer.prompt.readLine()).thenReturn("pwd", "exit");
        
        explorer.start();
        
        assertTrue(out.toString().contains("Current directory : "));
        
        System.setIn(System.in);
        System.setOut(standardOut);
    }
    
    @Test
    public void testStartWithCDCommand() throws IOException {
        File newDir = mock(File.class);
        when(newDir.getAbsolutePath()).thenReturn("/new/path");
        
        ByteArrayInputStream in = new ByteArrayInputStream("cd /new/path\nexit".getBytes());
        System.setIn(in);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
        explorer.prompt = mock(Prompt.class);
        when(explorer.prompt.readLine()).thenReturn("cd /new/path", "exit");
        
        explorer.start();
        
        verify(explorer.fsPath).newFSPath(newDir);
        assertTrue(out.toString().contains("changed to : /new/path"));
        
        System.setIn(System.in);
        System.setOut(standardOut);
    }
    
    @Test
    public void testStartWithInvalidCDCommand() throws IOException {
        File newDir = mock(File.class);
        when(newDir.exists()).thenReturn(false);
        when(newDir.isDirectory()).thenReturn(false);
        when(newDir.canRead()).thenReturn(false);
        
        ByteArrayInputStream in = new ByteArrayInputStream("cd /new/path\nexit".getBytes());
        System.setIn(in);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
        explorer.prompt = mock(Prompt.class);
        when(explorer.prompt.readLine()).thenReturn("cd /new/path", "exit");
        
        explorer.start();
        
        verify(explorer.fsPath, never()).newFSPath(newDir);
        assertTrue(out.toString().contains("unable to change to /new/path"));
        
        System.setIn(System.in);
        System.setOut(standardOut);
    }
    
    @Test
    public void testStartWithHelpCommand() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("help\nexit".getBytes());
        System.setIn(in);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
        explorer.prompt = mock(Prompt.class);
        when(explorer.prompt.readLine()).thenReturn("help", "exit");
        
        explorer.start();
        
        assertTrue(out.toString().contains("FSPathExplorer help :"));
        
        System.setIn(System.in);
        System.setOut(standardOut);
    }
    
    @Test
    public void testStartWithFSPathQuery() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("//dir[@name = 'foo']\nexit".getBytes());
        System.setIn(in);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
        explorer.prompt = mock(Prompt.class);
        when(explorer.prompt.readLine()).thenReturn("//dir[@name = 'foo']", "exit");
        
        explorer.fsPath = mock(FSPath.class);
        FSPathResultList results = mock(FSPathResultList.class);
        FSPathResult result = mock(FSPathResult.class);
        when(result.getFile()).thenReturn(mock(File.class));
        when(results.iterator()).thenReturn(mock(FSPathResultList.Iterator.class));
        when(results.iterator().hasNext()).thenReturn(true, false);
        when(results.iterator().next()).thenReturn(result);
        when(explorer.fsPath.query("//dir[@name = 'foo']")).thenReturn(results);
        
        explorer.start();
        
        verify(explorer.fsPath).query("//dir[@name = 'foo']");
        assertTrue(out.toString().contains(result.getFile().getPath()));
        
        System.setIn(System.in);
        System.setOut(standardOut);
    }
    
}