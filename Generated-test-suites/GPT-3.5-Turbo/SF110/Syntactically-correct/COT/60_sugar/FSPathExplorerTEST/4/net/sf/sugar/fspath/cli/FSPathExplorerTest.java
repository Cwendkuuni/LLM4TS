import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

    @Test
    public void testRootPathSetter() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/path/to/root");
        explorer.setRootPath(rootPath);

        assertEquals(rootPath, explorer.getRootPath());
    }

    @Test
    public void testFSPathQuerySetter() {
        FSPathExplorer explorer = new FSPathExplorer();
        String fsPathQuery = "//dir[@name = 'foo']";
        explorer.setFSPathQuery(fsPathQuery);

        assertEquals(fsPathQuery, explorer.getFSPathQuery());
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/path/to/root");
        explorer.setRootPath(rootPath);
        
        explorer.createFSPath();

        assertNotNull(explorer.getFSPath());
        assertEquals(rootPath, explorer.getFSPath().getRootDirectory());
    }

    @Test
    public void testCreateFSPathWithoutRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();

        assertNotNull(explorer.getFSPath());
        assertEquals(System.getProperty("user.dir"), explorer.getFSPath().getRootDirectory().getPath());
    }

    @Test
    public void testStartWithCDCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/path/to/root"));
        explorer.createFSPath();

        ByteArrayInputStream in = new ByteArrayInputStream("cd /new/path".getBytes());
        System.setIn(in);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
        explorer.start();

        assertEquals("/new/path", explorer.getFSPath().getRootDirectory().getPath());
    }

    @Test
    public void testStartWithPWDCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/path/to/root"));
        explorer.createFSPath();

        ByteArrayInputStream in = new ByteArrayInputStream("pwd".getBytes());
        System.setIn(in);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
        explorer.start();

        assertEquals("/path/to/root", out.toString().trim());
    }

    @Test
    public void testStartWithFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/path/to/root"));
        explorer.createFSPath();

        ByteArrayInputStream in = new ByteArrayInputStream("//dir[@name = 'foo']".getBytes());
        System.setIn(in);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
        explorer.start();

        assertEquals("/path/to/root/foo", out.toString().trim());
    }

    @Test
    public void testStartWithExitCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/path/to/root"));
        explorer.createFSPath();

        ByteArrayInputStream in = new ByteArrayInputStream("exit".getBytes());
        System.setIn(in);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        explorer.start();

        assertEquals("exiting.....", out.toString().trim());
    }
}