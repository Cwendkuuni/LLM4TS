import org.junit.Test;
import static org.junit.Assert.*;

public class SAXDirParserTest {
    private SAXDirParser parser;
    
    @Test
    public void testGetWebData() {
        parser = new SAXDirParser();
        assertNotNull(parser.getWebData());
    }
    
    @Test
    public void testStartElement_browserLoc() {
        parser = new SAXDirParser();
        parser.startElement("", "", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
    }
    
    @Test
    public void testStartElement_editor() {
        parser = new SAXDirParser();
        parser.startElement("", "", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
    }
    
    @Test
    public void testStartElement_acceptsLineNumber() {
        parser = new SAXDirParser();
        parser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
    }
    
    @Test
    public void testStartElement_parameter() {
        parser = new SAXDirParser();
        parser.startElement("", "", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
    }
    
    @Test
    public void testStartElement_group() {
        parser = new SAXDirParser();
        parser.startElement("", "", "Group", null);
        assertEquals(2, parser.level);
        assertFalse(parser.check);
    }
    
    @Test
    public void testStartElement_web() {
        parser = new SAXDirParser();
        parser.startElement("", "", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
    }
    
    @Test
    public void testStartElement_local() {
        parser = new SAXDirParser();
        parser.startElement("", "", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
    }
    
    @Test
    public void testStartElement_names() {
        parser = new SAXDirParser();
        parser.startElement("", "", "Names", null);
        assertTrue(parser.check);
    }
    
    @Test
    public void testStartElement_pkg() {
        parser = new SAXDirParser();
        parser.startElement("", "", "pkg", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters_browserLoc() {
        parser = new SAXDirParser();
        parser.startElement("", "", "BrowserLoc", null);
        parser.characters("test browser location".toCharArray(), 0, 19);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test browser location", SAXDirParser.localBrowser);
    }
    
    @Test
    public void testCharacters_editor() {
        parser = new SAXDirParser();
        parser.startElement("", "", "Editor", null);
        parser.characters("test editor".toCharArray(), 0, 11);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test editor", SAXDirParser.file_editor);
    }
    
    @Test
    public void testCharacters_acceptsLineNumber_yes() {
        parser = new SAXDirParser();
        parser.startElement("", "", "acceptsLineNumber", null);
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.lineNumAccepted);
    }
    
    @Test
    public void testCharacters_acceptsLineNumber_no() {
        parser = new SAXDirParser();
        parser.startElement("", "", "acceptsLineNumber", null);
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.lineNumAccepted);
    }
    
    @Test
    public void testCharacters_parameter() {
        parser = new SAXDirParser();
        parser.startElement("", "", "parameter", null);
        parser.characters("test parameter".toCharArray(), 0, 14);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test parameter", SAXDirParser.lineNumParam);
    }
    
    @Test
    public void testCharacters_web() {
        parser = new SAXDirParser();
        parser.startElement("", "", "Web", null);
        parser.characters("test location".toCharArray(), 0, 13);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test location", parser.location);
    }
    
    @Test
    public void testCharacters_local_no() {
        parser = new SAXDirParser();
        parser.startElement("", "", "Local", null);
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
    }
    
    @Test
    public void testCharacters_local_other() {
        parser = new SAXDirParser();
        parser.startElement("", "", "Local", null);
        parser.characters("Other".toCharArray(), 0, 5);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
    }
    
    @Test
    public void testCharacters_pkg_local_true() {
        parser = new SAXDirParser();
        parser.startElement("", "", "Local", null);
        parser.characters("test pkg".toCharArray(), 0, 8);
        parser.local = true;
        parser.level = 4;
        parser.location = "test location";
        
        parser.characters("test location".toCharArray(), 0, 13);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test location", SAXDirParser.directory.get("test pkg"));
        assertEquals("test pkg", SAXDirParser.localPackages.get(0));
    }
    
    @Test
    public void testEndElement_group() {
        parser = new SAXDirParser();
        parser.startElement("", "", "Group", null);
        parser.endElement("", "", "Group");
        assertFalse(parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        assertNotNull(SAXDirParser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages_empty() {
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalPackages_notEmpty() {
        SAXDirParser.localPackages.add("test");
        assertNotNull(SAXDirParser.getLocalPackages());
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
        // test main method by checking console output
    }
}