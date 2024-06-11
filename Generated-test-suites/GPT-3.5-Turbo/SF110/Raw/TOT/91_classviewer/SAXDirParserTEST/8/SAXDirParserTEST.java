import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SAXDirParserTest {

    private SAXDirParser saxDirParser;

    @Before
    public void setUp() {
        saxDirParser = new SAXDirParser();
    }

    @After
    public void tearDown() {
        saxDirParser = null;
    }

    @Test
    public void testGetWebData() {
        assertEquals("", saxDirParser.getWebData());
    }

    @Test
    public void testStartElement_browserLoc() throws SAXException {
        saxDirParser.startElement("", "", "BrowserLoc", null);

        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);
    }

    @Test
    public void testStartElement_editor() throws SAXException {
        saxDirParser.startElement("", "", "Editor", null);

        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);
    }

    @Test
    public void testStartElement_acceptsLineNumber() throws SAXException {
        saxDirParser.startElement("", "", "acceptsLineNumber", null);

        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);
    }

    @Test
    public void testStartElement_parameter() throws SAXException {
        saxDirParser.startElement("", "", "parameter", null);

        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);
    }

    @Test
    public void testStartElement_group() throws SAXException {
        saxDirParser.startElement("", "", "Group", null);

        assertEquals(2, saxDirParser.level);
        assertFalse(saxDirParser.check);
    }

    @Test
    public void testStartElement_web() throws SAXException {
        saxDirParser.startElement("", "", "Web", null);

        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);
    }

    @Test
    public void testStartElement_local() throws SAXException {
        saxDirParser.startElement("", "", "Local", null);

        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);
    }

    @Test
    public void testStartElement_names() throws SAXException {
        saxDirParser.startElement("", "", "Names", null);

        assertTrue(saxDirParser.check);
    }

    @Test
    public void testStartElement_pkg() throws SAXException {
        saxDirParser.startElement("", "", "pkg", null);

        assertEquals(4, saxDirParser.level);
    }

    @Test
    public void testCharacters_browserLoc() {
        char[] charArray = "http://www.example.com".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 0;

        saxDirParser.characters(charArray, 0, charArray.length);

        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("http://www.example.com", SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testCharacters_editor() {
        char[] charArray = "notepad".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 10;

        saxDirParser.characters(charArray, 0, charArray.length);

        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("notepad", SAXDirParser.getEditor());
    }

    @Test
    public void testCharacters_acceptsLineNumber_yes() {
        char[] charArray = "Yes".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 11;

        saxDirParser.characters(charArray, 0, charArray.length);

        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testCharacters_acceptsLineNumber_no() {
        char[] charArray = "No".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 11;

        saxDirParser.characters(charArray, 0, charArray.length);

        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertFalse(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testCharacters_parameter() {
        char[] charArray = "param".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 12;

        saxDirParser.characters(charArray, 0, charArray.length);

        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("param", SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testCharacters_web() {
        char[] charArray = "location".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 30;

        saxDirParser.characters(charArray, 0, charArray.length);

        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("location", saxDirParser.location);
    }

    @Test
    public void testCharacters_local_yes() {
        char[] charArray = "Yes".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 31;

        saxDirParser.characters(charArray, 0, charArray.length);

        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(saxDirParser.local);
    }

    @Test
    public void testCharacters_local_no() {
        char[] charArray = "No".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 31;

        saxDirParser.characters(charArray, 0, charArray.length);

        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertFalse(saxDirParser.local);
    }

    @Test
    public void testCharacters_pkg_local() {
        char[] charArray = "com.jstevh.viewer".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 4;

        saxDirParser.characters(charArray, 0, charArray.length);

        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("location", SAXDirParser.getDirectory().get("com.jstevh.viewer"));
        assertNotNull(SAXDirParser.getLocalPackages());
        assertEquals("com.jstevh.viewer", SAXDirParser.getLocalPackages().get(0));
    }

    @Test
    public void testEndElement_group() throws SAXException {
        saxDirParser.check = true;

        saxDirParser.endElement("", "", "Group");

        assertFalse(saxDirParser.check);
    }

    @Test
    public void testGetDirectory_empty() {
        assertTrue(SAXDirParser.getDirectory().isEmpty());
    }

    @Test
    public void testGetLocalPackages_empty() {
        assertNull(SAXDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalBrowser_empty() {
        assertEquals("", SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor_empty() {
        assertEquals("", SAXDirParser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber_empty() {
        assertFalse(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter_empty() {
        assertEquals("", SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testMain() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        XMLReader myReader = myParser.getXMLReader();
        myReader.setContentHandler(new SAXDirParser());

        // Test parsing the XML file
        SAXDirParser.main(new String[]{"packagedirectory.xml"});

        // Assert the results
        Map<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "location");
        expectedDirectory.put("javax.swing.border", "location");
        assertEquals(expectedDirectory, SAXDirParser.getDirectory());

        assertEquals("", SAXDirParser.getLocalBrowser());
    }

}