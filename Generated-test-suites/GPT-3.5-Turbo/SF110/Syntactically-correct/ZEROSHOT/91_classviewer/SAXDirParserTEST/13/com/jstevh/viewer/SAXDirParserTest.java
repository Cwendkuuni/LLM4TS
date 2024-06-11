import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

public class SAXDirParserTest {
    private SAXDirParser saxDirParser;

    @Before
    public void setUp() throws Exception {
        saxDirParser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertEquals("", saxDirParser.getWebData());
    }

    @Test
    public void testStartElement() throws SAXException {
        AttributesMock attributes = new AttributesMock();

        // Testing with qname "BrowserLoc"
        saxDirParser.startElement("", "", "BrowserLoc", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);

        // Testing with qname "Editor"
        saxDirParser.startElement("", "", "Editor", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);

        // Testing with qname "acceptsLineNumber"
        saxDirParser.startElement("", "", "acceptsLineNumber", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);
        
        // Testing with qname "parameter"
        saxDirParser.startElement("", "", "parameter", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);
        
        // Testing with qname "Group"
        saxDirParser.startElement("", "", "Group", attributes);
        assertFalse(saxDirParser.check);
        assertEquals(2, saxDirParser.level);

        // Testing with qname "Web"
        saxDirParser.startElement("", "", "Web", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);

        // Testing with qname "Local"
        saxDirParser.startElement("", "", "Local", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);

        // Testing with qname "Names"
        saxDirParser.startElement("", "", "Names", attributes);
        assertTrue(saxDirParser.check);

        // Testing with qname "pkg"
        saxDirParser.startElement("", "", "pkg", attributes);
        assertEquals(4, saxDirParser.level);
    }

    @Test
    public void testCharacters() {
        char[] charArray = new char[4];
        charArray[0] = 't';
        charArray[1] = 'e';
        charArray[2] = 's';
        charArray[3] = 't';

        // Testing level 0
        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.getLocalBrowser());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Testing level 10
        saxDirParser.check = true;
        saxDirParser.level = 10;
        saxDirParser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.getEditor());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Testing level 11
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(charArray, 0, 4);
        assertFalse(SAXDirParser.acceptsLineNumber());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Testing level 12
        saxDirParser.check = true;
        saxDirParser.level = 12;
        saxDirParser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.lineNumberParameter());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Testing level 30
        saxDirParser.check = true;
        saxDirParser.level = 30;
        saxDirParser.characters(charArray, 0, 4);
        assertEquals("test", saxDirParser.location);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Testing level 31 with "Yes"
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(charArray, 0, 4);
        assertTrue(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Testing level 31 with "No"
        charArray[0] = 'N';
        charArray[1] = 'o';
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(charArray, 0, 2);
        assertFalse(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Testing level 4
        saxDirParser.level = 4;
        saxDirParser.local = true;
        saxDirParser.location = "location";
        charArray[0] = 'p';
        charArray[1] = 'k';
        charArray[2] = 'g';
        saxDirParser.characters(charArray, 0, 3);
        assertEquals("location", SAXDirParser.getDirectory().get("pkg"));
        assertEquals("pkg", SAXDirParser.getLocalPackages().get(0));
    }

    @Test
    public void testEndElement() throws SAXException {
        // Testing with qname "Group"
        saxDirParser.endElement("", "", "Group");
        assertFalse(saxDirParser.check);
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
        SAXDirParser.main(null);
        assertEquals("test", SAXDirParser.getDirectory().get("java.lang"));
        assertEquals("test", SAXDirParser.getDirectory().get("javax.swing.border"));
        assertEquals("test", SAXDirParser.getLocalBrowser());
    }

    private class AttributesMock implements Attributes {
        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public String getURI(int index) {
            return "";
        }

        @Override
        public String getLocalName(int index) {
            return "";
        }

        @Override
        public String getQName(int index) {
            return "";
        }

        @Override
        public String getType(int index) {
            return "";
        }

        @Override
        public String getValue(int index) {
            return "";
        }

        @Override
        public int getIndex(String uri, String localName) {
            return 0;
        }

        @Override
        public int getIndex(String qName) {
            return 0;
        }

        @Override
        public String getType(String uri, String localName) {
            return "";
        }

        @Override
        public String getType(String qName) {
            return "";
        }

        @Override
        public String getValue(String uri, String localName) {
            return "";
        }

        @Override
        public String getValue(String qName) {
            return "";
        }
    }
}