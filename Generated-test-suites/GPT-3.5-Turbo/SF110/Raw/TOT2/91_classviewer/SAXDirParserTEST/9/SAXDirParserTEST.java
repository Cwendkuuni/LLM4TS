import org.junit.*;
import static org.junit.Assert.*;

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
        assertEquals(true, parser.check);
        assertEquals(0, parser.level);
    }

    @Test
    public void testCharacters() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "BrowserLoc", "BrowserLoc", null);
        char[] charArray = {'T', 'e', 's', 't'};
        parser.characters(charArray, 0, 4);
        assertEquals("Test", SAXDirParser.localBrowser);
    }

    @Test
    public void testEndElement() {
        SAXDirParser parser = new SAXDirParser();
        parser.endElement("", "Group", "Group");
        assertEquals(false, parser.check);
    }

    @Test
    public void testGetDirectory() {
        HashMap<String, String> expected = new HashMap<>();
        assertEquals(expected, SAXDirParser.getDirectory());
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