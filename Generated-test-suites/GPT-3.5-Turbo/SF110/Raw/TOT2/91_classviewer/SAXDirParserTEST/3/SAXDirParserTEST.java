import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import com.jstevh.viewer.SAXDirParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testStartElement() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "BrowserLoc", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
    }

    @Test
    public void testCharacters() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "BrowserLoc", "BrowserLoc", null);
        parser.characters("URL".toCharArray(), 0, 3);
        assertEquals("URL", SAXDirParser.localBrowser);
    }

    @Test
    public void testGetDirectory() {
        HashMap<String, String> directory = SAXDirParser.getDirectory();
        assertNotNull(directory);
    }

    @Test
    public void testGetLocalPackages() {
        assertNull(SAXDirParser.getLocalPackages());
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