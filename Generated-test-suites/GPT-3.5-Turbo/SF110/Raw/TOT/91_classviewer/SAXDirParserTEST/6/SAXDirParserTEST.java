import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class SAXDirParserTest {
    
    private SAXDirParser parser;
    
    @Before
    public void setUp() {
        parser = new SAXDirParser();
    }
    
    @Test
    public void testGetWebData() {
        // Typical use case
        parser.webData = "webData";
        assertEquals("webData", parser.getWebData());
    }
    
    @Test
    public void testSAXDirParser() throws Exception {
        // Typical use case
        SAXDirParser saxParser = new SAXDirParser();
        assertNotNull(saxParser);
    }
    
    @Test
    public void testStartElement() throws SAXException {
        // Typical use case
        parser.startElement("namespace", "local", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
        
        parser.startElement("namespace", "local", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
        
        parser.startElement("namespace", "local", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
        
        parser.startElement("namespace", "local", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
        
        parser.startElement("namespace", "local", "Group", null);
        assertFalse(parser.check);
        assertEquals(2, parser.level);
        
        parser.startElement("namespace", "local", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
        
        parser.startElement("namespace", "local", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
        
        parser.startElement("namespace", "local", "Names", null);
        assertTrue(parser.check);
        
        parser.startElement("namespace", "local", "pkg", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters() {
        // Typical use case
        parser.check = true;
        parser.level = 0;
        parser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.getLocalBrowser());
        
        parser.level = 10;
        parser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.getEditor());
        
        parser.level = 11;
        parser.characters(new char[] {'Y', 'e', 's'}, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());
        
        parser.level = 11;
        parser.characters(new char[] {'N', 'o'}, 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.acceptsLineNumber());
        
        parser.level = 12;
        parser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.lineNumberParameter());
        
        parser.level = 30;
        parser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertNull(parser.location);
        
        parser.level = 31;
        parser.characters(new char[] {'N', 'o'}, 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
        
        parser.level = 31;
        parser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
        
        parser.level = 4;
        parser.characters(new char[] {'p', 'k', 'g'}, 0, 3);
        assertEquals(4, parser.level);
        assertEquals("pkg", SAXDirParser.getLocalPackages().get(0));
        
        parser.level = 4;
        parser.characters(new char[0], 0, 0);
        assertEquals(4, parser.level);
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testEndElement() throws SAXException {
        // Typical use case
        parser.endElement("namespace", "local", "Group");
        assertFalse(parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        // Typical use case
        SAXDirParser.directory.put("java.lang", "directory1");
        SAXDirParser.directory.put("javax.swing.border", "directory2");
        assertEquals("directory1", SAXDirParser.getDirectory().get("java.lang"));
        assertEquals("directory2", SAXDirParser.getDirectory().get("javax.swing.border"));
    }
    
    @Test
    public void testGetLocalPackages() {
        // Typical use case
        SAXDirParser.localPackages.add("pkg1");
        SAXDirParser.localPackages.add("pkg2");
        assertEquals("pkg1", SAXDirParser.getLocalPackages().get(0));
        assertEquals("pkg2", SAXDirParser.getLocalPackages().get(1));
        
        // Edge case: list is empty
        SAXDirParser.localPackages.clear();
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "LocalBrowser";
        assertEquals("LocalBrowser", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "FileEditor";
        assertEquals("FileEditor", SAXDirParser.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser.lineNumAccepted = true;
        assertTrue(SAXDirParser.acceptsLineNumber());
        
        SAXDirParser.lineNumAccepted = false;
        assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "LineNumberParam";
        assertEquals("LineNumberParam", SAXDirParser.lineNumberParameter());
    }
}