import static org.junit.Assert.*;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "C:/Users/User/Documents", "-find", "//dir"};
        explorer.configureExplorer(args);
        assertNotNull(explorer.rootPath);
        assertEquals("C:/Users/User/Documents", explorer.rootPath.getPath());
        assertEquals("//dir", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStart() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        // Need to mock the input and output streams to fully test this method
    }

    @Test
    public void testValidCommandExit() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"exit"};
        explorer.configureExplorer(args);
        explorer.start();
        // Test if the program exits correctly
    }

    @Test
    public void testValidCommandPWD() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"pwd"};
        explorer.configureExplorer(args);
        explorer.start();
        // Test if the current directory is printed correctly
    }

    @Test
    public void testValidCommandCD() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"cd", "C:/Users/User/Documents"};
        explorer.configureExplorer(args);
        explorer.start();
        // Test if the directory is changed correctly
    }

    @Test
    public void testValidCommandHELP() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"help"};
        explorer.configureExplorer(args);
        explorer.start();
        // Test if the help information is displayed correctly
    }

    @Test
    public void testInvalidCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"invalid"};
        explorer.configureExplorer(args);
        explorer.start();
        // Test if an invalid command is handled correctly
    }

    @Test
    public void testQueryExecution() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "C:/Users/User/Documents", "-find", "//dir"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        // Test if the FSPath query is executed correctly
    }
}