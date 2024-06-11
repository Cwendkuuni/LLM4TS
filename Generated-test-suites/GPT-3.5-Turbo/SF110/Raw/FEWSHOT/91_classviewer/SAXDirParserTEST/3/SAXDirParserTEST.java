import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;

public class SAXDirParserTest {
    private SAXDirParser saxDirParser;
    
    @Before
    public void setUp() {
        saxDirParser = new SAXDirParser();
    }

    @Test
    public void testStartElement_BrowserLoc() throws SAXException {
        Attributes attributes = null;
        saxDirParser.startElement(null, null, "BrowserLoc", attributes);
        assertEquals(true, saxDirParser.check);
        assertEquals(0, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_Editor() throws SAXException {
        Attributes attributes = null;
        saxDirParser.startElement(null, null, "Editor", attributes);
        assertEquals(true, saxDirParser.check);
        assertEquals(10, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_acceptsLineNumber() throws SAXException {
        Attributes attributes = null;
        saxDirParser.startElement(null, null, "acceptsLineNumber", attributes);
        assertEquals(true, saxDirParser.check);
        assertEquals(11, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_parameter() throws SAXException {
        Attributes attributes = null;
        saxDirParser.startElement(null, null, "parameter", attributes);
        assertEquals(true, saxDirParser.check);
        assertEquals(12, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_Group() throws SAXException {
        Attributes attributes = null;
        saxDirParser.startElement(null, null, "Group", attributes);
        assertEquals(2, saxDirParser.level);
        assertEquals(false, saxDirParser.check);
    }
    
    @Test
    public void testStartElement_Web() throws SAXException {
        Attributes attributes = null;
        saxDirParser.startElement(null, null, "Web", attributes);
        assertEquals(true, saxDirParser.check);
        assertEquals(30, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_Local() throws SAXException {
        Attributes attributes = null;
        saxDirParser.startElement(null, null, "Local", attributes);
        assertEquals(true, saxDirParser.check);
        assertEquals(31, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_Names() throws SAXException {
        Attributes attributes = null;
        saxDirParser.startElement(null, null, "Names", attributes);
        assertEquals(true, saxDirParser.check);
    }
    
    @Test
    public void testStartElement_pkg() throws SAXException {
        Attributes attributes = null;
        saxDirParser.startElement(null, null, "pkg", attributes);
        assertEquals(4, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_BrowserLoc() {
        char[] charArray = "test".toCharArray();
        int start = 0;
        int length = 4;
        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters(charArray, start, length);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testCharacters_Editor() {
        char[] charArray = "test".toCharArray();
        int start = 0;
        int length = 4;
        saxDirParser.check = true;
        saxDirParser.level = 10;
        saxDirParser.characters(charArray, start, length);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.getEditor());
    }
    
    @Test
    public void testCharacters_acceptsLineNumber_Yes() {
        char[] charArray = "Yes".toCharArray();
        int start = 0;
        int length = 3;
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(charArray, start, length);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals(true, SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testCharacters_acceptsLineNumber_No() {
        char[] charArray = "No".toCharArray();
        int start = 0;
        int length = 2;
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(charArray, start, length);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals(false, SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testCharacters_parameter() {
        char[] charArray = "test".toCharArray();
        int start = 0;
        int length = 4;
        saxDirParser.check = true;
        saxDirParser.level = 12;
        saxDirParser.characters(charArray, start, length);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testCharacters_Web() {
        char[] charArray = "test".toCharArray();
        int start = 0;
        int length = 4;
        saxDirParser.check = true;
        saxDirParser.level = 30;
        saxDirParser.characters(charArray, start, length);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", saxDirParser.location);
    }
    
    @Test
    public void testCharacters_Local_Yes() {
        char[] charArray = "Yes".toCharArray();
        int start = 0;
        int length = 3;
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(charArray, start, length);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals(true, saxDirParser.local);
    }
    
    @Test
    public void testCharacters_Local_No() {
        char[] charArray = "No".toCharArray();
        int start = 0;
        int length = 2;
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(charArray, start, length);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals(false, saxDirParser.local);
    }
    
    @Test
    public void testCharacters_pkg() {
        char[] charArray = "test".toCharArray();
        int start = 0;
        int length = 4;
        saxDirParser.check = true;
        saxDirParser.level = 4;
        saxDirParser.local = true;
        saxDirParser.location = "location";
        SAXDirParser.directory.clear();
        SAXDirParser.localPackages.clear();
        
        saxDirParser.characters(charArray, start, length);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("location", SAXDirParser.getDirectory().get("test"));
        assertEquals("test", SAXDirParser.getLocalPackages().get(0));
    }
    
    @Test
    public void testEndElement_Group() throws SAXException {
        saxDirParser.check = true;
        saxDirParser.endElement(null, null, "Group");
        assertEquals(false, saxDirParser.check);
    }
    
    @Test
    public void testGetDirectory() {
        HashMap<String, String> directory = new HashMap<String, String>();
        directory.put("java.lang", "test");
        directory.put("javax.swing.border", "test");
        SAXDirParser.directory = directory;
        
        assertEquals(directory, SAXDirParser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages_Empty() {
        SAXDirParser.localPackages.clear();
        
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalPackages_NotEmpty() {
        StringList localPackages = new StringList();
        localPackages.add("test");
        SAXDirParser.localPackages = localPackages;
        
        assertEquals(localPackages, SAXDirParser.getLocalPackages());
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
        
        assertEquals(true, SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "test";
        
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }
}