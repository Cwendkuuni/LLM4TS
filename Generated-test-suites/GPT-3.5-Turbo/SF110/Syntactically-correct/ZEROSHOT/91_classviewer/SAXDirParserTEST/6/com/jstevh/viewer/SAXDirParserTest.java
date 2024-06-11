import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.*;
import java.util.*;

public class SAXDirParserTest {

    private SAXDirParser parser;
    
    @Before
    public void setUp() throws Exception {
        parser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testStartElement() throws SAXException {
        Attributes atts = Mockito.mock(Attributes.class);
        
        parser.startElement("", "", "BrowserLoc", atts);
        assertTrue(parser.check);
        assertEquals(0, parser.level);

        parser.startElement("", "", "Editor", atts);
        assertTrue(parser.check);
        assertEquals(10, parser.level);

        parser.startElement("", "", "acceptsLineNumber", atts);
        assertTrue(parser.check);
        assertEquals(11, parser.level);

        parser.startElement("", "", "parameter", atts);
        assertTrue(parser.check);
        assertEquals(12, parser.level);

        parser.startElement("", "", "Group", atts);
        assertEquals(2, parser.level);
        assertFalse(parser.check);

        parser.startElement("", "", "Web", atts);
        assertTrue(parser.check);
        assertEquals(30, parser.level);

        parser.startElement("", "", "Local", atts);
        assertTrue(parser.check);
        assertEquals(31, parser.level);

        parser.startElement("", "", "Names", atts);
        assertTrue(parser.check);

        parser.startElement("", "", "pkg", atts);
        assertEquals(4, parser.level);
    }

    @Test
    public void testCharacters() {
        parser.check = true;
        parser.level = 0;
        parser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.localBrowser);

        parser.check = true;
        parser.level = 10;
        parser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.file_editor);

        parser.check = true;
        parser.level = 11;
        parser.characters(new char[]{'Y', 'e', 's'}, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.lineNumAccepted);

        parser.check = true;
        parser.level = 11;
        parser.characters(new char[]{'N', 'o'}, 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.lineNumAccepted);

        parser.check = true;
        parser.level = 12;
        parser.characters(new char[]{'1', '2', '3'}, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("123", SAXDirParser.lineNumParam);

        parser.check = true;
        parser.level = 30;
        parser.characters(new char[]{'l', 'o', 'c', 'a', 't', 'i', 'o', 'n'}, 0, 8);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("location", parser.location);

        parser.check = true;
        parser.level = 31;
        parser.characters(new char[]{'Y', 'e', 's'}, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);

        parser.check = true;
        parser.level = 31;
        parser.characters(new char[]{'N', 'o'}, 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);

        parser.check = true;
        parser.level = 4;
        parser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertEquals(4, parser.level);
        assertEquals("test", SAXDirParser.directory.get("test"));
        assertEquals("location", SAXDirParser.directory.get("java.lang"));
        assertEquals("location", SAXDirParser.localPackages.get(0));
    }

    @Test
    public void testEndElement() throws SAXException {
        parser.check = true;

        parser.endElement("", "", "Group");
        assertFalse(parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        HashMap<String, String> expected = new HashMap<>();
        assertEquals(expected, SAXDirParser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages() {
        assertNull(SAXDirParser.getLocalPackages());
        
        SAXDirParser.localPackages.add("test");
        assertEquals(SAXDirParser.localPackages, SAXDirParser.getLocalPackages());
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
}