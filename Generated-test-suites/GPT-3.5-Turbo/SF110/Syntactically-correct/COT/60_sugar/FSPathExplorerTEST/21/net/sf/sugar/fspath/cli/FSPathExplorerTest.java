
import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;
import java.io.File;
import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {

    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        Assert.assertNotNull(explorer);
    }

    @Test
    public void testConfigureExplorer() {
        String[] args = {"-path", "/path/to/directory", "-find", "//dir[@name = 'test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        Assert.assertEquals("/path/to/directory", explorer.rootPath.getPath());
        Assert.assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        File rootPath = mock(File.class);
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        verifyStatic(FSPathFactory.class);
        FSPathFactory.newFSPath(rootPath);
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        verifyStatic(FSPathFactory.class);
        FSPathFactory.newFSPath();
    }

    @Test
    public void testStartWithExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = mock(Prompt.class);
        when(explorer.prompt.readLine()).thenReturn("exit");
        explorer.start();
        verify(explorer.prompt).close();
        verify(explorer.out, times(1)).println("exiting.....");
    }

    @Test
    public void testStartWithPwdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = mock(Prompt.class);
        explorer.fsPath = mock(FSPath.class);
        when(explorer.prompt.readLine()).thenReturn("pwd");
        when(explorer.fsPath.getRootDirectory()).thenReturn(mock(File.class));
        explorer.start();
        verify(explorer.fsPath, times(1)).getRootDirectory();
        verify(explorer.out, times(1)).println("Current directory : " + explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testStartWithCdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = mock(Prompt.class);
        explorer.fsPath = mock(FSPath.class);
        when(explorer.prompt.readLine()).thenReturn("cd /path/to/directory");
        when(explorer.fsPath.getRootDirectory()).thenReturn(mock(File.class));
        explorer.start();
        verify(explorer.fsPath, times(1)).newFSPath(new File("/path/to/directory"));
        verify(explorer.out, times(1)).println("changed to : " + new File("/path/to/directory").getAbsolutePath());
    }

    @Test
    public void testStartWithHelpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = mock(Prompt.class);
        when(explorer.prompt.readLine()).thenReturn("help");
        explorer.start();
        verify(explorer.out, times(1)).println(anyString());
    }

    @Test
    public void testStartWithFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = mock(Prompt.class);
        explorer.fsPath = mock(FSPath.class);
        FSPathResultList results = mock(FSPathResultList.class);
        FSPathResult result1 = mock(FSPathResult.class);
        FSPathResult result2 = mock(FSPathResult.class);
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        when(explorer.prompt.readLine()).thenReturn("//dir[@name = 'test']");
        when(explorer.fsPath.query("//dir[@name = 'test']")).thenReturn(results);
        when(results.iterator()).thenReturn(Arrays.asList(result1, result2).iterator());
        when(result1.getFile()).thenReturn(file1);
        when(result2.getFile()).thenReturn(file2);
        when(file1.getPath()).thenReturn("/path/to/file1");
        when(file2.getPath()).thenReturn("/path/to/file2");
        explorer.start();
        verify(explorer.fsPath, times(1)).query("//dir[@name = 'test']");
        verify(explorer.out, times(1)).println("/path/to/file1");
        verify(explorer.out, times(1)).println("/path/to/file2");
    }
}
