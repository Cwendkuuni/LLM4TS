package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    
    private FSPathExplorer explorer;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        explorer = new FSPathExplorer();
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testConfigureExplorer() {
        String[] args1 = {"-path", "C:/test", "-find", "//dir[@name = 'foo']"};
        String[] args2 = {"-f", "//dir[@name = 'foo']"};
        String[] args3 = {"-p", "C:/test"};
        String[] args4 = {"-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args1);
        assertEquals("C:/test", explorer.rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
        explorer.configureExplorer(args2);
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
        explorer.configureExplorer(args3);
        assertEquals("C:/test", explorer.rootPath.getAbsolutePath());
        assertEquals("", explorer.fsPathQuery);
        explorer.configureExplorer(args4);
        assertNull(explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertNull(explorer.rootPath);
        explorer.rootPath = new File("C:/test");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals(explorer.rootPath, explorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStart() {
        explorer.rootPath = new File("C:/test");
        explorer.fsPathQuery = "//dir[@name = 'foo']";
        explorer.start();
        assertEquals("Current directory : C:/test", outContent.toString().trim());
        outContent.reset();
        explorer.start();
        assertEquals("", outContent.toString().trim());
    }

    @Test
    public void testMain() {
        String[] args1 = {"-help"};
        FSPathExplorer.main(args1);
        assertTrue(outContent.toString().contains("FSPathExplorer help :"));
        outContent.reset();
    }
}