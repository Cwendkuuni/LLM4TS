import org.junit.*;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

	@Test
	public void testConfigureExplorer() {
		FSPathExplorer fsPathExplorer = new FSPathExplorer();
		String[] args = new String[]{"-path", "/root/path", "-f", "//dir[@name = 'foo']"};
		fsPathExplorer.configureExplorer(args);
		assertEquals(fsPathExplorer.rootPath.getAbsolutePath(), "/root/path");
		assertEquals(fsPathExplorer.fsPathQuery, "//dir[@name = 'foo']");
		
		args = new String[]{"-p", "/root/path", "-find", "//file[@length > 10000]"};
		fsPathExplorer.configureExplorer(args);
		assertEquals(fsPathExplorer.rootPath.getAbsolutePath(), "/root/path");
		assertEquals(fsPathExplorer.fsPathQuery, "//file[@length > 10000]");
	}

	@Test
	public void testCreateFSPath() {
		FSPathExplorer fsPathExplorer = new FSPathExplorer();
		fsPathExplorer.createFSPath();
		assertNotNull(fsPathExplorer.fsPath);
		
		fsPathExplorer.rootPath = new File("/root/path");
		fsPathExplorer.createFSPath();
		assertEquals(fsPathExplorer.fsPath.getRootDirectory().getAbsolutePath(), "/root/path");
	}

	@Test
	public void testStart() {
		FSPathExplorer fsPathExplorer = new FSPathExplorer();
		fsPathExplorer.start();
		// Test start() method by mocking the input and output streams
	}
	
	@Test
	public void testShowHelp() {
		FSPathExplorer.showHelp();
		// Test showHelp() method by verifying the console output
	}
	
	@Test
	public void testShowWelcome() {
		FSPathExplorer.showWelcome();
		// Test showWelcome() method by verifying the console output
	}

}