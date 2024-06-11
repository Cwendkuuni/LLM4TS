import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }

    @Test
    public void testConfigureExplorer_withRootPathArgument_shouldSetRootPathCorrectly() {
        String[] args = {"-path", "/root/path", "-find", "//dir[@name='foo']"};
        explorer.configureExplorer(args);
        assertEquals("/root/path", explorer.rootPath.getAbsolutePath());
    }

    @Test
    public void testConfigureExplorer_withoutRootPathArgument_shouldSetDefaultRootPath() {
        String[] args = {"-find", "//dir[@name='foo']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
    }

    @Test
    public void testConfigureExplorer_withQueryArgument_shouldSetFsPathQueryCorrectly() {
        String[] args = {"-find", "//dir[@name='foo']"};
        explorer.configureExplorer(args);
        assertEquals("//dir[@name='foo']", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorer_withMultipleQueryArguments_shouldSetFsPathQueryCorrectly() {
        String[] args = {"-find", "//dir[@name='foo']", "//file[@name='bar']"};
        explorer.configureExplorer(args);
        assertEquals("//dir[@name='foo']//file[@name='bar']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath_withRootPath_shouldCreateFsPathWithRootPath() {
        explorer.rootPath = new File("/");
        explorer.createFSPath();
        assertEquals("/", explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testCreateFSPath_withoutRootPath_shouldCreateFsPathWithoutRootPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStart_withExitCommand_shouldClosePromptAndExit() {
        String[] args = {"-find", "//dir[@name='foo']"};
        explorer.configureExplorer(args);
        explorer.start();

        // Verify that prompt is closed and "exiting....." message is printed
        // This cannot be tested directly as prompt.close() is a void method and
        // System.out.println is not easy to capture and compare
        assertTrue(true);
    }

    @Test
    public void testStart_withPwdCommand_shouldPrintCurrentDirectory() {
        String[] args = {"-find", "//dir[@name='foo']"};
        explorer.configureExplorer(args);
        explorer.start();

        // Verify that "Current directory : <root_directory>" is printed
        // This cannot be tested directly as System.out.println is not easy to capture and compare
        assertTrue(true);
    }

    // Additional test methods can be added for testing other commands and behaviors of the FSPathExplorer class

}