import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {
  
    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "testPath"};
        explorer.configureExplorer(args);
        assertEquals("testPath", explorer.rootPath.getAbsolutePath());
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("testPath");
        explorer.createFSPath();
        assertEquals("testPath", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath.getRootDirectory().getAbsolutePath());
    }
    
    @Test
    public void testStartWithExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() throws IOException {
                return "exit";
            }
        };
        explorer.start();
        assertTrue(explorer.prompt.isClosed());
    }

    @Test
    public void testStartWithPwdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = FSPathFactory.newFSPath(new File("testPath"));
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() throws IOException {
                return "pwd";
            }
        };
        explorer.start();
        assertEquals("Current directory : testPath", explorer.out.toString());
    }

    @Test
    public void testStartWithCdCommandValidDirectory() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() throws IOException {
                return "cd testDirectory";
            }
        };
        explorer.start();
        assertEquals("changed to : testDirectory", explorer.out.toString());
    }
    
    @Test
    public void testStartWithCdCommandInvalidDirectory() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() throws IOException {
                return "cd invalidDirectory";
            }
        };
        explorer.start();
        assertEquals("unable to change to invalidDirectory please make sure that the directory exists and is readable", explorer.out.toString());
    }

    @Test
    public void testStartWithHelpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() throws IOException {
                return "help";
            }
        };
        explorer.start();
        assertEquals("FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n", explorer.out.toString());
    }

    @Test
    public void testStartWithFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            public String readLine() throws IOException {
                return "//dir[@name = 'foo']";
            }
        };
        explorer.fsPath = FSPathFactory.newFSPath(new File("testPath"));
        explorer.fsPath.query = new FSPathQuery();
        explorer.fsPath.queryResultList = new FSPathResultList();
        explorer.fsPath.queryResultList.add(new FSPathResult());
        explorer.start();
        assertEquals("testPath\n", explorer.out.toString());
    }
}