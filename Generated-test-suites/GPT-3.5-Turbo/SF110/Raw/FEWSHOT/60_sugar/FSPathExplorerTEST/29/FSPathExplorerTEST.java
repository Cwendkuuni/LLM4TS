package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {
    
    private File rootPath;
    private FSPathExplorer explorer;
    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;

    @Before
    public void setUp() {
        rootPath = new File("/root");
        explorer = new FSPathExplorer();
        explorer.rootPath = rootPath;
        
        originalOut = System.out;
        originalErr = System.err;
        outputStream = new ByteArrayOutputStream();
        errorStream = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorStream));
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(explorer.prompt);
        assertNotNull(explorer.out);
        assertNotNull(explorer.err);
        assertNotNull(explorer.fsPath);
    }
    
    @Test
    public void testConfigureExplorer() {
        String[] argsPath = {"-path", "/home/user", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(argsPath);
        assertEquals(new File("/home/user"), explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
        
        String[] argsP = {"-p", "/home/user", "-f", "//dir[@name = 'foo']"};
        explorer.configureExplorer(argsP);
        assertEquals(new File("/home/user"), explorer.rootPath);
        assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
    }
    
    @Test
    public void testCreateFSPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertEquals(rootPath, explorer.fsPath.getRootDirectory());
        
        explorer.rootPath = null;
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
        assertTrue(explorer.fsPath.getRootDirectory().exists());
    }
    
    @Test
    public void testStart() throws ParseException {
        String[] args = {"-path", "/home/user", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        
        explorer.start();
        
        String expectedOutput = "/home/user/foo\n";
        assertEquals(expectedOutput, outputStream.toString());
        
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}