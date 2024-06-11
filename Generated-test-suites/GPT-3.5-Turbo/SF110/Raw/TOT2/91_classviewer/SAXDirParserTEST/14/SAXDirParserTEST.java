import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testStartElement() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "BrowserLoc", "BrowserLoc", null); 
        assertTrue(parser.level == 0);
        
        parser.startElement("", "Editor", "Editor", null);
        assertTrue(parser.level == 10);

        parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
        assertTrue(parser.level == 11);

        parser.startElement("", "parameter", "parameter", null);
        assertTrue(parser.level == 12);

        parser.startElement("", "Group", "Group", null);
        assertTrue(parser.level == 2);

        parser.startElement("", "Web", "Web", null);
        assertTrue(parser.level == 30);

        parser.startElement("", "Local", "Local", null);
        assertTrue(parser.level == 31);

        parser.startElement("", "Names", "Names", null);
        assertTrue(parser.level == 0);

        parser.startElement("", "pkg", "pkg", null);
        assertTrue(parser.level == 4);

    }

    @Test
    public void testEndElement() {
        SAXDirParser parser = new SAXDirParser();
        parser.endElement("", "", "Group");
        assertFalse(parser.check);
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