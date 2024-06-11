import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {
    
    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        
        String[] args = {"-path", "C:\\test", "-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        
        assert explorer.rootPath.getAbsolutePath().equals("C:\\test");
        assert explorer.fsPathQuery.equals("//dir[@name = 'foo']");
        
        String[] args2 = {"-find", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args2);
        
        assert explorer.rootPath == null;
        assert explorer.fsPathQuery.equals("//dir[@name = 'foo']");
    }
    
    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File("C:\\test");
        
        explorer.createFSPath();
        
        assert explorer.fsPath != null;
        assert explorer.fsPath.getRootDirectory().getAbsolutePath().equals("C:\\test");
        
        explorer.rootPath = null;
        
        explorer.createFSPath();
        
        assert explorer.fsPath != null;
        assert explorer.fsPath.getRootDirectory().getAbsolutePath().equals(System.getProperty("user.dir"));
    }
    
    @Test
    public void testStart() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        
        explorer.rootPath = new File("C:\\test");
        explorer.createFSPath();
        
        FSPathResultList results = new FSPathResultList();
        FSPathResult result1 = new FSPathResult(new File("C:\\test\\file1.txt"));
        FSPathResult result2 = new FSPathResult(new File("C:\\test\\file2.txt"));
        results.add(result1);
        results.add(result2);
        
        explorer.fsPath.setQueryResultList(results);
        
        PrintStream systemOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        
        ByteArrayInputStream in = new ByteArrayInputStream("exit".getBytes());
        System.setIn(in);
        
        explorer.start();
        
        System.setOut(systemOut);
        System.setIn(System.in);
    }
    
}