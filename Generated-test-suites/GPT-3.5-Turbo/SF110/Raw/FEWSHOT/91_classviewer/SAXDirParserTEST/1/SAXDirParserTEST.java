import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SAXDirParserTest {
    
    private static SAXDirParser parser;
    
    @BeforeClass
    public static void setup() {
        parser = new SAXDirParser();
        parser.webData = "test data";
    }
    
    @AfterClass
    public static void teardown() {
        parser = null;
    }
    
    @Test
    public void testGetWebData() {
        assertEquals("test data", parser.getWebData());
    }
    
    @Test
    public void testConstructor() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        assertNull(parser.getWebData());
    }
    
    @Test
    public void testStartElement() throws SAXException {
        Attributes atts = null;
        
        parser.startElement("namespace", "local", "BrowserLoc", atts);
        assertEquals(true, parser.check);
        assertEquals(0, parser.level);
        
        parser.startElement("namespace", "local", "Editor", atts);
        assertEquals(true, parser.check);
        assertEquals(10, parser.level);
        
        parser.startElement("namespace", "local", "acceptsLineNumber", atts);
        assertEquals(true, parser.check);
        assertEquals(11, parser.level);
        
        parser.startElement("namespace", "local", "parameter", atts);
        assertEquals(true, parser.check);
        assertEquals(12, parser.level);
        
        parser.startElement("namespace", "local", "Group", atts);
        assertEquals(false, parser.check);
        assertEquals(2, parser.level);
        
        parser.startElement("namespace", "local", "Web", atts);
        assertEquals(true, parser.check);
        assertEquals(30, parser.level);
        
        parser.startElement("namespace", "local", "Local", atts);
        assertEquals(true, parser.check);
        assertEquals(31, parser.level);
        
        parser.startElement("namespace", "local", "Names", atts);
        assertEquals(true, parser.check);
        
        parser.startElement("namespace", "local", "pkg", atts);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters() {
        char[] charArray = {'t', 'e', 's', 't'};
        
        parser.check = true;
        parser.level = 0;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("test", SAXDirParser.localBrowser);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 10;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("test", SAXDirParser.file_editor);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 11;
        parser.characters(charArray, 0, charArray.length);
        assertEquals(false, SAXDirParser.lineNumAccepted);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 12;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("test", SAXDirParser.lineNumParam);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 30;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("test", parser.location);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 31;
        parser.characters(charArray, 0, charArray.length);
        assertEquals(true, parser.local);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 4;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("", SAXDirParser.directory.get("test"));
        assertEquals(parser.location, SAXDirParser.directory.get("test"));
        assertEquals(true, SAXDirParser.localPackages.contains("test"));
    }
    
    @Test
    public void testEndElement() throws SAXException {
        parser.check = true;
        parser.endElement("namespace", "local", "Group");
        assertEquals(false, parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        assertNull(SAXDirParser.getDirectory());
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
        assertEquals(false, SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        assertEquals("", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testMain() throws Exception {
        // Test main method here
    }
}