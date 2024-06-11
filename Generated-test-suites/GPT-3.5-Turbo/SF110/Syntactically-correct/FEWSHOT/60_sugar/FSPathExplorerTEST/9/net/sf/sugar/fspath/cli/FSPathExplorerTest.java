import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testConfigureExplorer_noArguments() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorer_withPathArgument() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "/path/to/directory"};
        explorer.configureExplorer(args);
        assertEquals("/path/to/directory", explorer.rootPath.getAbsolutePath());
        assertEquals("", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorer_withFindArgument() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorer_withPathAndFindArguments() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "/path/to/directory", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertEquals("/path/to/directory", explorer.rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath_withRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/path/to/directory");
        explorer.createFSPath();
        assertEquals("/path/to/directory", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testCreateFSPath_withoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertTrue(explorer.fsPath.getRootDirectory().exists());
        assertTrue(explorer.fsPath.getRootDirectory().isDirectory());
    }

    @Test
    public void testStart_exitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() {
                return "exit";
            }
        };
        explorer.start();
        assertTrue(explorer.prompt.isClosed());
    }

    @Test
    public void testStart_pwdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = new FSPath(new File("/path/to/directory"));
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() {
                return "pwd";
            }
        };
        explorer.start();
        assertEquals("Current directory : /path/to/directory", outContent.toString().trim());
    }

    @Test
    public void testStart_cdCommand_success() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/path/to/directory");
        explorer.createFSPath();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() {
                return "cd /new/path";
            }
        };
        explorer.start();
        assertEquals("changed to : /new/path", outContent.toString().trim());
    }

    @Test
    public void testStart_cdCommand_failure() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("/path/to/directory");
        explorer.createFSPath();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() {
                return "cd /nonexistent/path";
            }
        };
        explorer.start();
        assertEquals("unable to change to /nonexistent/path please make sure that the directory exists and is readable", outContent.toString().trim());
    }

    @Test
    public void testStart_helpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() {
                return "help";
            }
        };
        explorer.start();
        // Add assertions for the expected output of the help command
    }

    @Test
    public void testStart_fsPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPath = new FSPath(new File("/path/to/directory"));
        explorer.fsPathQuery = "//dir[@name = 'foo']";
        explorer.prompt = new Prompt(System.in, System.out, System.err) {
            @Override
            public String readLine() {
                return explorer.fsPathQuery;
            }
        };
        explorer.start();
        // Add assertions for the expected output of the fsPathQuery
    }
}