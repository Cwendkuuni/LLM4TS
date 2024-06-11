import org.junit.*;
import static org.junit.Assert.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        String webData = parser.getWebData();
        assertEquals("", webData);
    }
    
    @Test
    public void testStartElement_WithBrowserLoc() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "BrowserLoc", "", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
    }
    
    @Test
    public void testStartElement_WithEditor() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "Editor", "", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
    }

    @Test
    public void testStartElement_WithAcceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "acceptsLineNumber", "", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
    }
    
    @Test
    public void testStartElement_WithParameter() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "parameter", "", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
    }
    
    @Test
    public void testStartElement_WithGroup() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "Group", "", null);
        assertEquals(2, parser.level);
        assertFalse(parser.check);
    }
    
    @Test
    public void testStartElement_WithWeb() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "Web", "", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
    }
    
    @Test
    public void testStartElement_WithLocal() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "Local", "", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
    }
    
    @Test
    public void testStartElement_WithNames() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "Names", "", null);
        assertTrue(parser.check);
    }
    
    @Test
    public void testStartElement_WithPkg() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 4;
        parser.startElement("", "pkg", "", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters_WithLevel0() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 0;
        parser.characters("Test String".toCharArray(), 0, 11);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Test String", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testCharacters_WithLevel10() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 10;
        parser.characters("Test String".toCharArray(), 0, 11);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Test String", SAXDirParser.getEditor());
    }
    
    @Test
    public void testCharacters_WithLevel11_Yes() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 11;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testCharacters_WithLevel11_No() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 11;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testCharacters_WithLevel12() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 12;
        parser.characters("Test String".toCharArray(), 0, 11);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Test String", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testCharacters_WithLevel30() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 30;
        parser.characters("Test String".toCharArray(), 0, 11);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Test String", parser.location);
    }
    
    @Test
    public void testCharacters_WithLevel31_Yes() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 31;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
    }
    
    @Test
    public void testCharacters_WithLevel31_No() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 31;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
    }
    
    @Test
    public void testCharacters_WithLevel4() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 4;
        parser.local = true;
        parser.location = "location";
        parser.characters("Test String".toCharArray(), 0, 11);
        assertEquals(4, parser.level);
        assertFalse(parser.local);
        assertEquals("location", SAXDirParser.getDirectory().get("Test String"));
        assertNotNull(SAXDirParser.getLocalPackages());
        assertTrue(SAXDirParser.getLocalPackages().contains("Test String"));
    }
    
    @Test
    public void testEndElement_WithGroup() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.endElement("", "Group", "");
        assertFalse(parser.check);
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser parser = new SAXDirParser();
        SAXDirParser.directory.put("key", "value");
        assertEquals(parser.getDirectory().get("key"), SAXDirParser.directory.get("key"));
    }
    
    @Test
    public void testGetLocalPackages_WhenEmpty() {
        SAXDirParser.directory.clear();
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalPackages_WhenNotEmpty() {
        SAXDirParser.directory.clear();
        SAXDirParser.localPackages.add("package1");
        SAXDirParser.localPackages.add("package2");
        assertNotNull(SAXDirParser.getLocalPackages());
        assertEquals(2, SAXDirParser.getLocalPackages().size());
        assertTrue(SAXDirParser.getLocalPackages().contains("package1"));
        assertTrue(SAXDirParser.getLocalPackages().contains("package2"));
    }
    
    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "Test Browser";
        assertEquals("Test Browser", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "Test Editor";
        assertEquals("Test Editor", SAXDirParser.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumber_WhenTrue() {
        SAXDirParser.lineNumAccepted = true;
        assertTrue(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testAcceptsLineNumber_WhenFalse() {
        SAXDirParser.lineNumAccepted = false;
        assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "Test Parameter";
        assertEquals("Test Parameter", SAXDirParser.lineNumberParameter());
    }
    
    // Test main method
    @Test
    public void testMain() throws Exception {
        // Unable to test main method as it requires external file resource "packagedirectory.xml" and prints to console
    }

}