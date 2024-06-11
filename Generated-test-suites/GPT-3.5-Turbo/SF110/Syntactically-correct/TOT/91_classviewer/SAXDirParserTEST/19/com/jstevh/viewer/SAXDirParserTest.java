import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import static org.junit.Assert.*;

public class SAXDirParserTest {

    private SAXDirParser saxDirParser;

    @Before
    public void setUp() throws Exception {
        saxDirParser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertNull(saxDirParser.getWebData());
    }

    @Test
    public void testStartElement_BrowserLoc() throws SAXException {
        saxDirParser.startElement("", "", "BrowserLoc", null);
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);
    }

    @Test
    public void testStartElement_Editor() throws SAXException {
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
    public void testStartElement_Group() throws SAXException {
        saxDirParser.startElement("", "", "Group", null);
        assertFalse(saxDirParser.check);
        assertEquals(2, saxDirParser.level);
    }

    @Test
    public void testStartElement_Web() throws SAXException {
        saxDirParser.startElement("", "", "Web", null);
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);
    }

    @Test
    public void testStartElement_Local() throws SAXException {
        saxDirParser.startElement("", "", "Local", null);
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);
    }

    @Test
    public void testStartElement_Names() throws SAXException {
        saxDirParser.startElement("", "", "Names", null);
        assertTrue(saxDirParser.check);
    }

    @Test
    public void testStartElement_pkg() throws SAXException {
        saxDirParser.startElement("", "", "pkg", null);
        assertEquals(4, saxDirParser.level);
    }

    @Test
    public void testCharacters_Level0() {
        char[] charArray = "test".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters(charArray, 0, charArray.length);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testCharacters_Level10() {
        char[] charArray = "test".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 10;
        saxDirParser.characters(charArray, 0, charArray.length);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.getEditor());
    }

    @Test
    public void testCharacters_Level11_Yes() {
        char[] charArray = "Yes".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(charArray, 0, charArray.length);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testCharacters_Level11_No() {
        char[] charArray = "No".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(charArray, 0, charArray.length);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertFalse(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testCharacters_Level12() {
        char[] charArray = "test".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 12;
        saxDirParser.characters(charArray, 0, charArray.length);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testCharacters_Level30() {
        char[] charArray = "test".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 30;
        saxDirParser.characters(charArray, 0, charArray.length);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", saxDirParser.location);
    }

    @Test
    public void testCharacters_Level31() {
        char[] charArray = "Yes".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(charArray, 0, charArray.length);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(saxDirParser.local);
    }

    @Test
    public void testCharacters_Level31_No() {
        char[] charArray = "No".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(charArray, 0, charArray.length);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertFalse(saxDirParser.local);
    }

    @Test
    public void testCharacters_Level4() {
        char[] charArray = "test".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 4;
        saxDirParser.location = "location";
        saxDirParser.local = true;
        saxDirParser.characters(charArray, 0, charArray.length);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertFalse(SAXDirParser.getLocalPackages().isEmpty());
        assertEquals("test", SAXDirParser.getLocalPackages().get(0));
        assertTrue(SAXDirParser.getDirectory().containsKey("test"));
        assertEquals("location", SAXDirParser.getDirectory().get("test"));
    }

    @Test
    public void testEndElement_Group() throws SAXException {
        saxDirParser.endElement("", "", "Group");
        assertFalse(saxDirParser.check);
    }

    @Test
    public void testGetDirectory() {
        assertNotNull(SAXDirParser.getDirectory());
    }

    @Test
    public void testGetLocalPackages_Empty() {
        assertNull(SAXDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalBrowser() {
        assertNotNull(SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        assertNotNull(SAXDirParser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber() {
        assertNotNull(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        assertNotNull(SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testMain() throws Exception {
        SAXDirParser.main(new String[]{});
    }

    @Test
    public void testGetDirectory_ContainsValue() throws ParserConfigurationException, SAXException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        myParser.parse("packagedirectory.xml", saxDirParser);
        assertTrue(SAXDirParser.getDirectory().containsValue("location"));
    }

    @Test
    public void testGetDirectory_ContainsKey() throws ParserConfigurationException, SAXException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        myParser.parse("packagedirectory.xml", saxDirParser);
        assertTrue(SAXDirParser.getDirectory().containsKey("java.lang"));
    }

    @Test
    public void testGetDirectory_ContainsKey_Invalid() throws ParserConfigurationException, SAXException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        myParser.parse("packagedirectory.xml", saxDirParser);
        assertFalse(SAXDirParser.getDirectory().containsKey("invalid_key"));
    }

    @Test
    public void testGetLocalPackages_NotEmpty() throws ParserConfigurationException, SAXException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        myParser.parse("packagedirectory.xml", saxDirParser);
        assertNotNull(SAXDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalBrowser_NotEmpty() throws ParserConfigurationException, SAXException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        myParser.parse("packagedirectory.xml", saxDirParser);
        assertNotNull(SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor_NotEmpty() throws ParserConfigurationException, SAXException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        myParser.parse("packagedirectory.xml", saxDirParser);
        assertNotNull(SAXDirParser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber_NotNull() throws ParserConfigurationException, SAXException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        myParser.parse("packagedirectory.xml", saxDirParser);
        assertNotNull(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter_NotNull() throws ParserConfigurationException, SAXException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        myParser.parse("packagedirectory.xml", saxDirParser);
        assertNotNull(SAXDirParser.lineNumberParameter());
    }
}