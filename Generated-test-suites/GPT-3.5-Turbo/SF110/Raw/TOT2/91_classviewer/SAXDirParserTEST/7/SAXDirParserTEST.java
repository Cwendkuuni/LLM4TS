import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import com.jstevh.tools.StringList;

public class SAXDirParserTest {
    
    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }
    
    @Test
    public void testGetDirectory() {
        HashMap<String, String> directory = SAXDirParser.getDirectory();
        assertNotNull(directory);
    }
    
    @Test
    public void testGetLocalPackages() {
        StringList localPackages = SAXDirParser.getLocalPackages();
        assertNull(localPackages);
    }
    
    @Test
    public void testGetLocalBrowser() {
        assertEquals("", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        assertEquals("", SAXDirParser.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumber() {
        assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        assertEquals("", SAXDirParser.lineNumberParameter());
    }
}