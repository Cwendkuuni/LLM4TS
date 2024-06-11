
import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

public class SAXDirParserTest {
    
    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        String webData = parser.getWebData();
        assertEquals("", webData);
    }
    
    @Test
    public void testStartElement() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
        
        parser.startElement("", "", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
        
        parser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
        
        parser.startElement("", "", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
        
        parser.startElement("", "", "Group", null);
        assertEquals(2, parser.level);
        assertFalse(parser.check);
        
        parser.startElement("", "", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
        
        parser.startElement("", "", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
        
        parser.startElement("", "", "Names", null);
        assertTrue(parser.check);
        
        parser.startElement("", "", "pkg", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        
        parser.level = 0;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.localBrowser);
        
        parser.level = 10;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.file_editor);
        
        parser.level = 11;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.lineNumAccepted);
        
        parser.level = 11;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.lineNumAccepted);
        
        parser.level = 12;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.lineNumParam);
        
        parser.level = 30;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", parser.location);
        
        parser.level = 31;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
        
        parser.level = 31;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
        
        parser.level = 4;
        parser.characters("pkg".toCharArray(), 0, 3);
        assertEquals(4, parser.level);
        assertTrue(SAXDirParser.directory.containsKey("pkg"));
        assertEquals(parser.location, SAXDirParser.directory.get("pkg"));
    }
    
    @Test
    public void testEndElement() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.endElement("", "", "Group");
        assertFalse(parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        SAXDirParser.directory.put("pkg1", "location1");
        SAXDirParser.directory.put("pkg2", "location2");
        
        HashMap<String, String> directory = SAXDirParser.getDirectory();
        assertEquals("location1", directory.get("pkg1"));
        assertEquals("location2", directory.get("pkg2"));
    }
    
    @Test
    public void testGetLocalPackages() {
        assertNull(SAXDirParser.getLocalPackages());
        
        SAXDirParser.localPackages.add("pkg1");
        SAXDirParser.localPackages.add("pkg2");
        
        StringList localPackages = SAXDirParser.getLocalPackages();
        assertEquals("pkg1", localPackages.get(0));
        assertEquals("pkg2", localPackages.get(1));
    }
    
    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "browser";
        assertEquals("browser", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "editor";
        assertEquals("editor", SAXDirParser.getEditor());
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
        SAXDirParser.lineNumParam = "param";
        assertEquals("param", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testMain() throws Exception {
        // Unable to test main method, as it requires file input
    }
    
}
