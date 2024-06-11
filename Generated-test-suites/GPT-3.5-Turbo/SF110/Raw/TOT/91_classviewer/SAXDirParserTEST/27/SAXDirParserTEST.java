import static org.junit.Assert.*;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

public class SAXDirParserTest {
    
    private SAXDirParser parser;
    
    @Before
    public void setUp() throws Exception {
        parser = new SAXDirParser();
    }
    
    @Test
    public void testGetWebData() {
        String expected = "";
        String actual = parser.getWebData();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStartElement_BrowserLoc() {
        String namespace = "namespace";
        String local = "local";
        String qname = "BrowserLoc";
        Attributes atts = null;
        
        try {
            parser.startElement(namespace, local, qname, atts);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        String expected = "";
        String actual = SAXDirParser.getLocalBrowser();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStartElement_Editor() {
        String namespace = "namespace";
        String local = "local";
        String qname = "Editor";
        Attributes atts = null;
        
        try {
            parser.startElement(namespace, local, qname, atts);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        String expected = "";
        String actual = SAXDirParser.getEditor();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStartElement_AcceptsLineNumber() {
        String namespace = "namespace";
        String local = "local";
        String qname = "acceptsLineNumber";
        Attributes atts = null;
        
        try {
            parser.startElement(namespace, local, qname, atts);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        boolean expected = false;
        boolean actual = SAXDirParser.acceptsLineNumber();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStartElement_Parameter() {
        String namespace = "namespace";
        String local = "local";
        String qname = "parameter";
        Attributes atts = null;
        
        try {
            parser.startElement(namespace, local, qname, atts);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        String expected = "";
        String actual = SAXDirParser.lineNumberParameter();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStartElement_Group() {
        String namespace = "namespace";
        String local = "local";
        String qname = "Group";
        Attributes atts = null;
        
        try {
            parser.startElement(namespace, local, qname, atts);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        // No assertion to be made, only side effects
    }
    
    @Test
    public void testStartElement_Web() {
        String namespace = "namespace";
        String local = "local";
        String qname = "Web";
        Attributes atts = null;
        
        try {
            parser.startElement(namespace, local, qname, atts);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        String expected = "";
        String actual = parser.location;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStartElement_Local() {
        String namespace = "namespace";
        String local = "local";
        String qname = "Local";
        Attributes atts = null;
        
        try {
            parser.startElement(namespace, local, qname, atts);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        boolean expected = false;
        boolean actual = parser.local;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStartElement_Names() {
        String namespace = "namespace";
        String local = "local";
        String qname = "Names";
        Attributes atts = null;
        
        try {
            parser.startElement(namespace, local, qname, atts);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        // No assertion to be made, only side effects
    }
    
    @Test
    public void testStartElement_pkg() {
        String namespace = "namespace";
        String local = "local";
        String qname = "pkg";
        Attributes atts = null;
        
        try {
            parser.startElement(namespace, local, qname, atts);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        // No assertion to be made, only side effects
    }
    
    @Test
    public void testCharacters_Level0() {
        char[] charArray = {'t', 'e', 's', 't'};
        int start = 0;
        int length = 4;
        
        parser.level = 0;
        parser.check = true;
        
        parser.characters(charArray, start, length);
        
        String expected = "test";
        String actual = SAXDirParser.getLocalBrowser();
        assertEquals(expected, actual);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }
    
    @Test
    public void testCharacters_Level10() {
        char[] charArray = {'t', 'e', 's', 't'};
        int start = 0;
        int length = 4;
        
        parser.level = 10;
        parser.check = true;
        
        parser.characters(charArray, start, length);
        
        String expected = "test";
        String actual = SAXDirParser.getEditor();
        assertEquals(expected, actual);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }
    
    @Test
    public void testCharacters_Level11_Yes() {
        char[] charArray = {'Y', 'e', 's'};
        int start = 0;
        int length = 3;
        
        parser.level = 11;
        parser.check = true;
        
        parser.characters(charArray, start, length);
        
        boolean expected = true;
        boolean actual = SAXDirParser.acceptsLineNumber();
        assertEquals(expected, actual);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }
    
    @Test
    public void testCharacters_Level11_No() {
        char[] charArray = {'N', 'o'};
        int start = 0;
        int length = 2;
        
        parser.level = 11;
        parser.check = true;
        
        parser.characters(charArray, start, length);
        
        boolean expected = false;
        boolean actual = SAXDirParser.acceptsLineNumber();
        assertEquals(expected, actual);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }
    
    @Test
    public void testCharacters_Level12() {
        char[] charArray = {'t', 'e', 's', 't'};
        int start = 0;
        int length = 4;
        
        parser.level = 12;
        parser.check = true;
        
        parser.characters(charArray, start, length);
        
        String expected = "test";
        String actual = SAXDirParser.lineNumberParameter();
        assertEquals(expected, actual);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }
    
    @Test
    public void testCharacters_Level30() {
        char[] charArray = {'t', 'e', 's', 't'};
        int start = 0;
        int length = 4;
        
        parser.level = 30;
        parser.check = true;
        
        parser.characters(charArray, start, length);
        
        String expected = "test";
        String actual = parser.location;
        assertEquals(expected, actual);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }
    
    @Test
    public void testCharacters_Level31_No() {
        char[] charArray = {'N', 'o'};
        int start = 0;
        int length = 2;
        
        parser.level = 31;
        parser.check = true;
        
        parser.characters(charArray, start, length);
        
        boolean expected = false;
        boolean actual = parser.local;
        assertEquals(expected, actual);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }
    
    @Test
    public void testCharacters_Level31_NotNo() {
        char[] charArray = {'t', 'e', 's', 't'};
        int start = 0;
        int length = 4;
        
        parser.level = 31;
        parser.check = true;
        
        parser.characters(charArray, start, length);
        
        boolean expected = true;
        boolean actual = parser.local;
        assertEquals(expected, actual);
        
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }
    
    @Test
    public void testCharacters_Level4_NonEmpty() {
        char[] charArray = {'a', 'b', 'c'};
        int start = 0;
        int length = 3;
        
        parser.level = 4;
        parser.check = true;
        parser.location = "location";
        parser.local = true;
        
        parser.characters(charArray, start, length);
        
        HashMap<String, String> expectedDirectory = new HashMap<String, String>();
        expectedDirectory.put("abc", "location");
        
        HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
        assertEquals(expectedDirectory, actualDirectory);
        
        StringList expectedPackages = new StringList();
        expectedPackages.add("abc");
        
        StringList actualPackages = SAXDirParser.getLocalPackages();
        assertEquals(expectedPackages, actualPackages);
    }
    
    @Test
    public void testEndElement_Group() {
        String namespace = "namespace";
        String local = "local";
        String qname = "Group";
        
        parser.check = true;
        
        try {
            parser.endElement(namespace, local, qname);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        assertFalse(parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        assertEquals(new HashMap<String, String>(), SAXDirParser.getDirectory());  
    }
    
    @Test
    public void testGetLocalPackages_Empty() {
        assertEquals(null, SAXDirParser.getLocalPackages());  
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
}