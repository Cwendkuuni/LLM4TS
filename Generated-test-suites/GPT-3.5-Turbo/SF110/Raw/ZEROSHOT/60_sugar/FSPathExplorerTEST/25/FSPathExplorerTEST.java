import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
import org.mockito.Mockito;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class FSPathExplorerTest{

    private final InputStream input = new ByteArrayInputStream("input string".getBytes());
    private final PrintStream output = Mockito.mock(PrintStream.class);
    private final PrintStream error = Mockito.mock(PrintStream.class);

    @Before
    public void setup() {
        System.setIn(input); 
        System.setOut(output);
        System.setErr(error);
    }

    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "/path/to/root", "-find", "//dir[@name='foo']//file[contains(name, '.txt')]"};
        explorer.configureExplorer(args);
        assertEquals(new File("/path/to/root"), explorer.rootPath);
        assertEquals("//dir[@name='foo']//file[contains(name, '.txt')]", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/path/to/root");
        explorer.createFSPath();
        assertEquals(FSPathFactory.newFSPath(new File("/path/to/root")), explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertEquals(FSPathFactory.newFSPath(), explorer.fsPath);
    }

    @Test
    public void testStartWithExitCommand() throws IOException {
        FSPathExplorer explorer = Mockito.spy(new FSPathExplorer());
        Mockito.doNothing().when(explorer).createFSPath();
        Mockito.doNothing().when(explorer).showWelcome();
        Mockito.doNothing().when(explorer).configureExplorer(Mockito.any());
        Mockito.doNothing().when(explorer).printFileSystemMetadata();
        explorer.prompt = Mockito.spy(new Prompt(input, output, error));
        Mockito.when(explorer.prompt.readLine()).thenReturn("exit");
        explorer.start();
        Mockito.verify(explorer).showWelcome();
        Mockito.verify(explorer).configureExplorer(Mockito.any());
        Mockito.verify(explorer).createFSPath();
        Mockito.verify(explorer).printFileSystemMetadata();
        Mockito.verify(explorer.prompt).readLine();
        Mockito.verify(explorer.prompt).close();
        Mockito.verify(output).println("exiting.....");
    }
    
    @Test
    public void testStartWithPwdCommand() throws IOException {
        FSPathExplorer explorer = Mockito.spy(new FSPathExplorer());
        Mockito.doNothing().when(explorer).createFSPath();
        Mockito.doNothing().when(explorer).showWelcome();
        Mockito.doNothing().when(explorer).configureExplorer(Mockito.any());
        Mockito.doNothing().when(explorer).printFileSystemMetadata();
        explorer.prompt = Mockito.spy(new Prompt(input, output, error));
        Mockito.when(explorer.prompt.readLine()).thenReturn("pwd", "exit");
        explorer.start();
        Mockito.verify(output).println("Current directory : " + explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testStartWithCdCommand() throws IOException {
        FSPathExplorer explorer = Mockito.spy(new FSPathExplorer());
        Mockito.doNothing().when(explorer).createFSPath();
        Mockito.doNothing().when(explorer).showWelcome();
        Mockito.doNothing().when(explorer).configureExplorer(Mockito.any());
        Mockito.doNothing().when(explorer).printFileSystemMetadata();
        explorer.prompt = Mockito.spy(new Prompt(input, output, error));
        Mockito.when(explorer.prompt.readLine()).thenReturn("cd", "new_dir_name", "exit");
        explorer.start();
        Mockito.verify(output).println("changed to : " + "new_dir_name");
    }
    
    @Test
    public void testStartWithHelpCommand() throws IOException {
        FSPathExplorer explorer = Mockito.spy(new FSPathExplorer());
        Mockito.doNothing().when(explorer).createFSPath();
        Mockito.doNothing().when(explorer).showWelcome();
        Mockito.doNothing().when(explorer).configureExplorer(Mockito.any());
        Mockito.doNothing().when(explorer).printFileSystemMetadata();
        explorer.prompt = Mockito.spy(new Prompt(input, output, error));
        Mockito.when(explorer.prompt.readLine()).thenReturn("help", "exit");
        explorer.start();
        Mockito.verify(output).println("FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\nAttributes : (see javadoc for java.io.File for more info)\n\n@STRING\n@FILE\n@HIDDEN\n@FILE_SIZE\n@DIR_SIZE\n@LAST_MODIFIED\n\nCommands :\n\nexit\t: Exit FSPathExplorer\npwd\t: Print current directory\ncd\t: Change directory\nhelp\t: Display help\n");
    }
    
    @Test
    public void testStartWithFSPathQuery() throws IOException {
        FSPathExplorer explorer = Mockito.spy(new FSPathExplorer());
        Mockito.when(explorer.fsPath.query(Mockito.any())).thenReturn(new FSPathResultList());
        Mockito.doNothing().when(explorer).createFSPath();
        Mockito.doNothing().when(explorer).showWelcome();
        Mockito.doNothing().when(explorer).configureExplorer(Mockito.any());
        Mockito.doNothing().when(explorer).printFileSystemMetadata();
        explorer.prompt = Mockito.spy(new Prompt(input, output, error));
        Mockito.when(explorer.prompt.readLine()).thenReturn("//dir[@name='foo']//file[contains(name, '.txt')]", "exit");
        explorer.start();
        Mockito.verify(explorer.fsPath).query("//dir[@name='foo']//file[contains(name, '.txt')]");
        Mockito.verify(output).println(Mockito.any());
    }
    
}