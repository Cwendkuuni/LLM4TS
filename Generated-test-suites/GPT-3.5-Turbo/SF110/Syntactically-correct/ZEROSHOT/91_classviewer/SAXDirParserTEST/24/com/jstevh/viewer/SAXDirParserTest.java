import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class SAXDirParserTest {
    
    private SAXDirParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertNull(parser.getWebData());
    }

    @Test
    public void testStartElement() throws SAXException {
  
        parser.startElement(null, "BrowserLoc", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);

        parser.startElement(null, "Editor", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);

        parser.startElement(null, "acceptsLineNumber", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);

        parser.startElement(null, "parameter", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);

        parser.startElement(null, "Group", "Group", null);
        assertFalse(parser.check);
        assertEquals(2, parser.level);

        parser.startElement(null, "Web", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);

        parser.startElement(null, "Local", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);

        parser.startElement(null, "Names", "Names", null);
        assertTrue(parser.check);

        parser.startElement(null, "pkg", "pkg", null);
        assertEquals(4, parser.level);
    }

    @Test
    public void testCharacters() {

        parser.level = 0;
        parser.check = true;
        char[] charArray1 = {'B', 'r', 'o', 'w', 's', 'e', 'r'};
        parser.characters(charArray1, 0, charArray1.length);
        assertFalse(parser.check);
        assertEquals("", parser.webData);
        assertEquals("Browser", SAXDirParser.getLocalBrowser());

        parser.level = 10;
        parser.check = true;
        char[] charArray2 = {'E', 'd', 'i', 't', 'o', 'r'};
        parser.characters(charArray2, 0, charArray2.length);
        assertFalse(parser.check);
        assertEquals("", parser.webData);
        assertEquals("Editor", SAXDirParser.getEditor());

        parser.level = 11;
        parser.check = true;
        char[] charArray3 = {'Y', 'e', 's'};
        parser.characters(charArray3, 0, charArray3.length);
        assertFalse(parser.check);
        assertEquals("", parser.webData);
        assertTrue(SAXDirParser.acceptsLineNumber());

        parser.level = 12;
        parser.check = true;
        char[] charArray4 = {'P', 'a', 'r', 'a', 'm'};
        parser.characters(charArray4, 0, charArray4.length);
        assertFalse(parser.check);
        assertEquals("", parser.webData);
        assertEquals("Param", SAXDirParser.lineNumberParameter());

        parser.level = 30;
        parser.check = true;
        char[] charArray5 = {'L', 'o', 'c', 'a', 't', 'i', 'o', 'n'};
        parser.characters(charArray5, 0, charArray5.length);
        assertFalse(parser.check);
        assertEquals("", parser.webData);
        assertEquals("Location", parser.location);

        parser.level = 31;
        parser.check = true;
        char[] charArray6 = {'Y', 'e', 's'};
        parser.characters(charArray6, 0, charArray6.length);
        assertFalse(parser.check);
        assertEquals("", parser.webData);
        assertTrue(parser.local);

        parser.level = 4;
        char[] charArray7 = {'P', 'a', 'c', 'k'};
        parser.characters(charArray7, 0, charArray7.length);
        assertEquals(4, parser.level);
        assertEquals(1, SAXDirParser.getDirectory().size());
        assertEquals("Location", SAXDirParser.getDirectory().get("Pack"));

    }

    @Test
    public void testEndElement() throws SAXException {
      
        parser.endElement(null, null, "Group");
        assertFalse(parser.check);

        parser.endElement(null, null, "Other");
        assertFalse(parser.check);

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

    @Test
    public void testMain() throws Exception {
        // Cannot test as main method is void and contains console output
    }
}