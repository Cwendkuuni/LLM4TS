package net.sf.sugar.fspath.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.io.IOException;

public class FSPathExplorerTest {

    @Test
    public void testConfigureExplorer() {
        // Test configuring FSPathExplorer with root path and query
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-path", "C:\\", "-find", "//dir"};
        explorer.configureExplorer(args);
        assertEquals(new File("C:\\"), explorer.rootPath);
        assertEquals("//dir", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        // Test creating FSPath without root path
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);

        // Test creating FSPath with root path
        File rootPath = new File("C:\\");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStart() {
        // Test starting FSPathExplorer and checking the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        FSPathExplorer explorer = new FSPathExplorer();
        
        String[] args = {"-path", "C:\\", "-find", "//dir"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        
        assertTrue(outContent.toString().contains("Current directory : C:\\"));
        
        System.setOut(System.out);
    }
}