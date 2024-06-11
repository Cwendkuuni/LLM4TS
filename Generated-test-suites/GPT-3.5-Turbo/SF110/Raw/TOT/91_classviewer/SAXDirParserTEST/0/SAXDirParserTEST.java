import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class SAXDirParserTest {
    
    private SAXDirParser parser;
    
    @Before
    public void setUp() {
        try {
            parser = new SAXDirParser();
        } catch (Exception e) {
            fail("Failed to initialize SAXDirParser");
        }
    }
    
    @Test
    public void testGetWebData() {
        parser.webData = "Test Web Data";
        assertEquals("Test Web Data", parser.getWebData());
    }
    
    @Test
    public void testStartElement_BrowserLoc() {
        try {
            parser.startElement("", "", "BrowserLoc", null);
            assertTrue(parser.check);
            assertEquals(0, parser.level);
        } catch (SAXException e) {
            fail("Failed to start element BrowserLoc");
        }
    }
    
    @Test
    public void testStartElement_Editor() {
        try {
            parser.startElement("", "", "Editor", null);
            assertTrue(parser.check);
            assertEquals(10, parser.level);
        } catch (SAXException e) {
            fail("Failed to start element Editor");
        }
    }
    
    @Test
    public void testStartElement_acceptsLineNumber() {
        try {
            parser.startElement("", "", "acceptsLineNumber", null);
            assertTrue(parser.check);
            assertEquals(11, parser.level);
        } catch (SAXException e) {
            fail("Failed to start element acceptsLineNumber");
        }
    }
    
    @Test
    public void testStartElement_parameter() {
        try {
            parser.startElement("", "", "parameter", null);
            assertTrue(parser.check);
            assertEquals(12, parser.level);
        } catch (SAXException e) {
            fail("Failed to start element parameter");
        }
    }
    
    @Test
    public void testStartElement_Group() {
        try {
            parser.startElement("", "", "Group", null);
            assertEquals(2, parser.level);
            assertFalse(parser.check);
        } catch (SAXException e) {
            fail("Failed to start element Group");
        }
    }
    
    @Test
    public void testStartElement_Web() {
        try {
            parser.startElement("", "", "Web", null);
            assertTrue(parser.check);
            assertEquals(30, parser.level);
        } catch (SAXException e) {
            fail("Failed to start element Web");
        }
    }
    
    @Test
    public void testStartElement_Local() {
        try {
            parser.startElement("", "", "Local", null);
            assertTrue(parser.check);
            assertEquals(31, parser.level);
        } catch (SAXException e) {
            fail("Failed to start element Local");
        }
    }
    
    @Test
    public void testStartElement_Names() {
        try {
            parser.startElement("", "", "Names", null);
            assertTrue(parser.check);
        } catch (SAXException e) {
            fail("Failed to start element Names");
        }
    }
    
    @Test
    public void testStartElement_pkg() {
        try {
            parser.startElement("", "", "pkg", null);
            assertEquals(4, parser.level);
        } catch (SAXException e) {
            fail("Failed to start element pkg");
        }
    }
    
    @Test
    public void testCharacters_BrowserLoc() {
        parser.check = true;
        parser.level = 0;
        parser.characters("Test BrowserLoc".toCharArray(), 0, 15);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Test BrowserLoc", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testCharacters_Editor() {
        parser.check = true;
        parser.level = 10;
        parser.characters("Test Editor".toCharArray(), 0, 11);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Test Editor", SAXDirParser.getEditor());
    }
    
    @Test
    public void testCharacters_acceptsLineNumber_True() {
        parser.check = true;
        parser.level = 11;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testCharacters_acceptsLineNumber_False() {
        parser.check = true;
        parser.level = 11;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testCharacters_parameter() {
        parser.check = true;
        parser.level = 12;
        parser.characters("Test Parameter".toCharArray(), 0, 14);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Test Parameter", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testCharacters_Web() {
        parser.check = true;
        parser.level = 30;
        parser.characters("Test Web".toCharArray(), 0, 8);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Test Web", parser.location);
    }
    
    @Test
    public void testCharacters_Local_True() {
        parser.check = true;
        parser.level = 31;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
    }
    
    @Test
    public void testCharacters_Local_False() {
        parser.check = true;
        parser.level = 31;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
    }
    
    @Test
    public void testCharacters_pkg() {
        parser.level = 4;
        parser.location = "Test Location";
        parser.local = true;
        parser.characters("Test Package".toCharArray(), 0, 12);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
        assertEquals("Test Location", SAXDirParser.getDirectory().get("Test Package"));
        assertNotNull(SAXDirParser.getLocalPackages());
        assertEquals("Test Package", SAXDirParser.getLocalPackages().get(0));
    }
    
    @Test
    public void testEndElement_Group() {
        parser.check = true;
        parser.endElement("", "", "Group");
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
        SAXDirParser.localBrowser = "Test Local Browser";
        assertEquals("Test Local Browser", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "Test Editor";
        assertEquals("Test Editor", SAXDirParser.getEditor());
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
        SAXDirParser.lineNumParam = "Test Parameter";
        assertEquals("Test Parameter", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testMain() {
        try {
            SAXDirParser.main(new String[0]);
        } catch (Exception e) {
            fail("Failed to run main method");
        }
    }
}