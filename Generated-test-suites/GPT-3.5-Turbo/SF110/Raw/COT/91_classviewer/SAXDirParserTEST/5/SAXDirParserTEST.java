import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.*;

public class SAXDirParserTest {
    private SAXDirParser parser;
    
    @Before
    public void setUp() throws Exception {
        parser = new SAXDirParser();
    }
    
    @Test
    public void testGetWebData() {
        String webData = parser.getWebData();
        assertNotNull(webData);
    }
    
    @Test
    public void testGetDirectory() {
        parser.startElement("", "Folder", "BrowserLoc", null);
        parser.characters("test".toCharArray(), 0, 4);
        parser.endElement("", "Folder", "BrowserLoc");
        
        HashMap<String, String> directory = parser.getDirectory();
        assertNotNull(directory);
        assertEquals("test", directory.get("Folder"));
    }
    
    @Test
    public void testGetLocalPackages() {
        StringList localPackages = parser.getLocalPackages();
        assertNull(localPackages);
    }
    
    @Test
    public void testGetLocalBrowser() {
        String localBrowser = parser.getLocalBrowser();
        assertNotNull(localBrowser);
    }
    
    @Test
    public void testGetEditor() {
        String fileEditor = parser.getEditor();
        assertNotNull(fileEditor);
    }

    @Test
    public void testAcceptsLineNumber() {
        boolean acceptsLineNumber = parser.acceptsLineNumber();
        assertFalse(acceptsLineNumber);
    }

    @Test
    public void testLineNumberParameter() {
        String lineNumberParam = parser.lineNumberParameter();
        assertNotNull(lineNumberParam);
    }
    
    @Test
    public void testStartElement() throws SAXException {
        parser.startElement("", "BrowserLoc", "", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
        
        parser.startElement("", "Editor", "", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
        
        parser.startElement("", "acceptsLineNumber", "", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
        
        parser.startElement("", "parameter", "", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
        
        parser.startElement("", "Group", "", null);
        assertFalse(parser.check);
        assertEquals(2, parser.level);
        
        parser.startElement("", "Web", "", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
        
        parser.startElement("", "Local", "", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
        
        parser.startElement("", "Names", "", null);
        assertTrue(parser.check);
        
        parser.startElement("", "pkg", "", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters() {
        parser.check = true;
        parser.level = 0;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.localBrowser);
        
        parser.check = true;
        parser.level = 10;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.file_editor);
        
        parser.check = true;
        parser.level = 11;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.lineNumAccepted);
        
        parser.check = true;
        parser.level = 12;
        parser.characters("param".toCharArray(), 0, 5);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("param", SAXDirParser.lineNumParam);
        
        parser.check = true;
        parser.level = 30;
        parser.characters("location".toCharArray(), 0, 8);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("location", parser.location);
        
        parser.check = true;
        parser.level = 31;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
        
        parser.check = true;
        parser.level = 4;
        parser.characters("pkg".toCharArray(), 0, 3);
        assertEquals(4, parser.level);
        assertEquals("pkg", SAXDirParser.directory.get("pkg"));
        assertNotNull(SAXDirParser.localPackages);
        assertEquals("pkg", SAXDirParser.localPackages.get(0));
    }
    
    @Test
    public void testEndElement() throws SAXException {
        parser.endElement("", "Group", "");
        assertFalse(parser.check);
    }
    
    @Test
    public void testMain() throws Exception {
        SAXDirParser.main(new String[0]);
    }
}
