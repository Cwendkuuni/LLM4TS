import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.util.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        
        String webData = parser.getWebData();
        
        assertEquals("", webData);
    }
    
    @Test
    public void testStartElement() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        String namespace = "";
        String local = "";
        String qname = "";
        Attributes atts = null;
        
        // Test with qname equals "BrowserLoc"
        qname = "BrowserLoc";
        parser.startElement(namespace, local, qname, atts);
        
        assertTrue(parser.check);
        assertEquals(0, parser.level);
        
        // Test with qname equals "Editor"
        qname = "Editor";
        parser.startElement(namespace, local, qname, atts);
        
        assertTrue(parser.check);
        assertEquals(10, parser.level);
        
        // Test with qname equals "acceptsLineNumber"
        qname = "acceptsLineNumber";
        parser.startElement(namespace, local, qname, atts);
        
        assertTrue(parser.check);
        assertEquals(11, parser.level);
        
        // Test with qname equals "parameter"
        qname = "parameter";
        parser.startElement(namespace, local, qname, atts);
        
        assertTrue(parser.check);
        assertEquals(12, parser.level);
        
        // Test with qname equals "Group"
        qname = "Group";
        parser.startElement(namespace, local, qname, atts);
        
        assertEquals(2, parser.level);
        assertFalse(parser.check);
        
        // Test with qname equals "Web"
        qname = "Web";
        parser.startElement(namespace, local, qname, atts);
        
        assertTrue(parser.check);
        assertEquals(30, parser.level);
        
        // Test with qname equals "Local"
        qname = "Local";
        parser.startElement(namespace, local, qname, atts);
        
        assertTrue(parser.check);
        assertEquals(31, parser.level);
        
        // Test with qname equals "Names"
        qname = "Names";
        parser.startElement(namespace, local, qname, atts);
        
        assertTrue(parser.check);
        
        // Test with qname equals "pkg"
        qname = "pkg";
        parser.startElement(namespace, local, qname, atts);
        
        assertEquals(4, parser.level);
        
    }
    
    @Test
    public void testCharacters() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        char[] charArray = null;
        
        // Test when check is true and level is greater than or equal to 0
        
        parser.check = true;
        parser.level = 0;
        charArray = new char[] {'A', 'B', 'C'};
        parser.characters(charArray, 0, charArray.length);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("ABC", parser.localBrowser);
        
        parser.check = true;
        parser.level = 10;
        charArray = new char[] {'X', 'Y', 'Z'};
        parser.characters(charArray, 0, charArray.length);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("XYZ", parser.file_editor);
        
        parser.check = true;
        parser.level = 11;
        charArray = new char[] {'Y', 'e', 's'};
        parser.characters(charArray, 0, charArray.length);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.lineNumAccepted);
        
        parser.check = true;
        parser.level = 11;
        charArray = new char[] {'n', 'o'};
        parser.characters(charArray, 0, charArray.length);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.lineNumAccepted);
        
        parser.check = true;
        parser.level = 12;
        charArray = new char[] {'p', 'a', 'r', 'a', 'm'};
        parser.characters(charArray, 0, charArray.length);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("param", parser.lineNumParam);
        
        parser.check = true;
        parser.level = 30;
        charArray = new char[] {'W', 'e', 'b'};
        parser.characters(charArray, 0, charArray.length);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Web", parser.location);
        
        // Test when check is true and level is 31
        
        parser.check = true;
        parser.level = 31;
        charArray = new char[] {'Y', 'e', 's'};
        parser.characters(charArray, 0, charArray.length);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
        
        parser.check = true;
        parser.level = 31;
        charArray = new char[] {'n', 'o'};
        parser.characters(charArray, 0, charArray.length);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
        
        // Test when level is 4 and tempStr is not empty
        
        parser.level = 4;
        charArray = new char[] {'t', 'e', 's', 't'};
        parser.characters(charArray, 0, charArray.length);
        
        assertEquals(4, parser.level);
        
    }
    
    @Test
    public void testEndElement() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        String namespace = "";
        String local = "";
        String qname = "";
        
        qname = "Group";
        parser.endElement(namespace, local, qname);
        
        assertFalse(parser.check);
        
    }
    
    @Test
    public void testGetDirectory() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        
        HashMap<String, String> directory = parser.getDirectory();
        
        assertEquals(SAXDirParser.directory, directory);
    }
    
    @Test
    public void testGetLocalPackages() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        
        StringList localPackages = parser.getLocalPackages();
        
        assertEquals(SAXDirParser.localPackages, localPackages);
    }
    
    @Test
    public void testGetLocalBrowser() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        
        String localBrowser = parser.getLocalBrowser();
        
        assertEquals(SAXDirParser.localBrowser, localBrowser);
    }
    
    @Test
    public void testGetEditor() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        
        String fileEditor = parser.getEditor();
        
        assertEquals(SAXDirParser.file_editor, fileEditor);
    }
    
    @Test
    public void testAcceptsLineNumber() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        
        boolean acceptsLineNumber = parser.acceptsLineNumber();
        
        assertEquals(SAXDirParser.lineNumAccepted, acceptsLineNumber);
    }
    
    @Test
    public void testLineNumberParameter() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        
        String lineNumberParam = parser.lineNumberParameter();
        
        assertEquals(SAXDirParser.lineNumParam, lineNumberParam);
    }
    
}