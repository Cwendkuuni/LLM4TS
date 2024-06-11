import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.Attributes;

public class SAXDirParserTest {
    
    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }
    
    @Test
    public void testStartElement_BrowserLoc() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
    }
    
    @Test
    public void testStartElement_Editor() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
    }
    
    @Test
    public void testStartElement_acceptsLineNumber() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
    }
    
    @Test
    public void testStartElement_parameter() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
    }
    
    @Test
    public void testStartElement_Group() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Group", null);
        assertEquals(2, parser.level);
        assertFalse(parser.check);
    }
    
    @Test
    public void testStartElement_Web() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
    }
    
    @Test
    public void testStartElement_Local() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
    }
    
    @Test
    public void testStartElement_Names() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Names", null);
        assertTrue(parser.check);
    }
    
    @Test
    public void testStartElement_pkg() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "pkg", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters_BrowserLoc() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 0;
        char[] charArray = {'t', 'e', 's', 't'};
        parser.characters(charArray, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.localBrowser);
    }
    
    @Test
    public void testCharacters_Editor() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 10;
        char[] charArray = {'t', 'e', 's', 't'};
        parser.characters(charArray, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.file_editor);
    }
    
    @Test
    public void testCharacters_acceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 11;
        char[] charArray = {'Y', 'e', 's'};
        parser.characters(charArray, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.lineNumAccepted);
    }
    
    @Test
    public void testCharacters_parameter() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 12;
        char[] charArray = {'t', 'e', 's', 't'};
        parser.characters(charArray, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.lineNumParam);
    }
    
    @Test
    public void testCharacters_Web() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 30;
        char[] charArray = {'t', 'e', 's', 't'};
        parser.characters(charArray, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", parser.location);
    }
    
    @Test
    public void testCharacters_Local() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 31;
        char[] charArray = {'Y', 'e', 's'};
        parser.characters(charArray, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
    }
    
    @Test
    public void testCharacters_pkg() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 4;
        char[] charArray = {'t', 'e', 's', 't'};
        parser.characters(charArray, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals(parser.location, SAXDirParser.directory.get("test"));
        assertNull(SAXDirParser.localPackages);
    }
    
    @Test
    public void testEndElement_Group() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.endElement("", "", "Group");
        assertFalse(parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        SAXDirParser.directory = new HashMap<String, String>();
        SAXDirParser.directory.put("testKey", "testValue");
        assertEquals(SAXDirParser.directory, SAXDirParser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages = new StringList();
        SAXDirParser.localPackages.add("test");
        assertEquals(SAXDirParser.localPackages, SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalPackages_null() {
        SAXDirParser.localPackages = new StringList();
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "test";
        assertEquals("test", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "test";
        assertEquals("test", SAXDirParser.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser.lineNumAccepted = true;
        assertTrue(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "test";
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testMain() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        XMLReader myReader = myParser.getXMLReader();
        myReader.setContentHandler(new SAXDirParser());
        myReader.parse("packagedirectory.xml");
        assertEquals("testValue", SAXDirParser.directory.get("testKey"));
        assertNull(SAXDirParser.directory.get("java.lang"));
        assertNull(SAXDirParser.directory.get("javax.swing.border"));
        assertEquals("", SAXDirParser.localBrowser);
    }
}