import static org.junit.Assert.*;
import org.junit.Test;

public class SAXDirParserTest {

   @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testStartElement() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("testNamespace", "testLocal", "BrowserLoc", null);
        assertEquals(true, parser.check);
        assertEquals(0, parser.level);
    }

    @Test
    public void testCharacters() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 0;
        char[] testCharArray = {'T', 'e', 's', 't'};
        parser.characters(testCharArray, 0, 4);
        assertEquals("Test", SAXDirParser.localBrowser);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
    }

    @Test
    public void testEndElement() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.endElement("testNamespace", "testLocal", "Group");
        assertEquals(false, parser.check);
    }

    @Test
    public void testGetDirectory() {
        assertNotNull(SAXDirParser.getDirectory());
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