
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {

    private FSPathExplorer fsPathExplorer;
    private File testRoot;

    @Before
    public void setUp() throws IOException {
        fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.out = new PrintStream(System.out);
        fsPathExplorer.err = new PrintStream(System.err);
        fsPathExplorer.prompt = new Prompt(System.in, fsPathExplorer.out, fsPathExplorer.err);
        fsPathExplorer.prompt.setPromptText("fspath>");

        testRoot = File.createTempFile("testRoot", "");
        testRoot.delete();
        testRoot.mkdir();
    }

    @Test
    public void testConfigureExplorer_Path() {
        String[] args = new String[]{"-path", testRoot.getAbsolutePath(), "-find", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        assertEquals(testRoot, fsPathExplorer.rootPath);
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorer_P() {
        String[] args = new String[]{"-p", testRoot.getAbsolutePath(), "-f", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        assertEquals(testRoot, fsPathExplorer.rootPath);
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorer_Find() {
        String[] args = new String[]{"-find", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        assertNull(fsPathExplorer.rootPath);
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath_WithRootPath() {
        fsPathExplorer.rootPath = testRoot;
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath);
    }

    @Test
    public void testCreateFSPath_NoRootPath() {
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath);
    }

    // Add more unit tests for the remaining methods in FSPathExplorer class

    @Test
    public void testStart_ExitCommand() throws Exception {
        fsPathExplorer.prompt = new Prompt(System.in, fsPathExplorer.out, fsPathExplorer.err) {
            @Override
            public String readLine() throws IOException {
                return "exit";
            }
        };
        fsPathExplorer.start();
    }

    @Test
    public void testStart_PwdCommand() throws Exception {
        fsPathExplorer.prompt = new Prompt(System.in, fsPathExplorer.out, fsPathExplorer.err) {
            private boolean pwdCommandExecuted = false;

            @Override
            public String readLine() throws IOException {
                if (!pwdCommandExecuted) {
                    pwdCommandExecuted = true;
                    return "pwd";
                } else {
                    return "exit";
                }
            }
        };
        fsPathExplorer.start();
    }

    @Test
    public void testStart_CdCommand_ValidDirectory() throws Exception {
        fsPathExplorer.prompt = new Prompt(System.in, fsPathExplorer.out, fsPathExplorer.err) {
            private boolean cdCommandExecuted = false;

            @Override
            public String readLine() throws IOException {
                if (!cdCommandExecuted) {
                    cdCommandExecuted = true;
                    return "cd " + testRoot.getAbsolutePath();
                } else {
                    return "exit";
                }
            }
        };
        fsPathExplorer.start();
    }

    @Test
    public void testStart_CdCommand_InvalidDirectory() throws Exception {
        fsPathExplorer.prompt = new Prompt(System.in, fsPathExplorer.out, fsPathExplorer.err) {
            @Override
            public String readLine() throws IOException {
                return "cd " + "invalidDirectory";
            }
        };
        fsPathExplorer.start();
    }

    @Test
    public void testStart_HelpCommand() throws Exception {
        fsPathExplorer.prompt = new Prompt(System.in, fsPathExplorer.out, fsPathExplorer.err) {
            private boolean helpCommandExecuted = false;

            @Override
            public String readLine() throws IOException {
                if (!helpCommandExecuted) {
                    helpCommandExecuted = true;
                    return "help";
                } else {
                    return "exit";
                }
            }
        };
        fsPathExplorer.start();
    }

    @Test
    public void testStart_InvalidCommand() throws Exception {
        fsPathExplorer.prompt = new Prompt(System.in, fsPathExplorer.out, fsPathExplorer.err) {
            private boolean invalidCommandExecuted = false;

            @Override
            public String readLine() throws IOException {
                if (!invalidCommandExecuted) {
                    invalidCommandExecuted = true;
                    return "invalidCommand";
                } else {
                    return "exit";
                }
            }
        };
        fsPathExplorer.start();
    }

    @Test
    public void testStart_ValidFSPathQuery() throws Exception {
        fsPathExplorer.prompt = new Prompt(System.in, fsPathExplorer.out, fsPathExplorer.err) {
            @Override
            public String readLine() throws IOException {
                return "//dir[@name = 'test']";
            }
        };
        fsPathExplorer.rootPath = testRoot;
        fsPathExplorer.createFSPath();
        fsPathExplorer.start();
    }

    @Test
    public void testStart_InvalidFSPathQuery() throws Exception {
        fsPathExplorer.prompt = new Prompt(System.in, fsPathExplorer.out, fsPathExplorer.err) {
            @Override
            public String readLine() throws IOException {
                return "//invalidQuery";
            }
        };
        fsPathExplorer.rootPath = testRoot;
        fsPathExplorer.createFSPath();
        fsPathExplorer.start();
    }
}
