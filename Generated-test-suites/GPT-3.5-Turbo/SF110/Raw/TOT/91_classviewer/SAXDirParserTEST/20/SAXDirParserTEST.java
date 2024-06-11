import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SAXDirParserTest {

    private SAXDirParser saxDirParser;

    @Before
    public void setup() {
        saxDirParser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertNull(saxDirParser.getWebData());
    }

    @Test
    public void testStartElement_BrowserLoc() {
        saxDirParser.startElement("", "", "BrowserLoc", null);
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);
    }

    @Test
    public void testStartElement_Editor() {
        saxDirParser.startElement("", "", "Editor", null);
        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);
    }

    @Test
    public void testStartElement_acceptsLineNumber() {
        saxDirParser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);
    }

    @Test
    public void testStartElement_parameter() {
        saxDirParser.startElement("", "", "parameter", null);
        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);
    }

    @Test
    public void testStartElement_Group() {
        saxDirParser.startElement("", "", "Group", null);
        assertEquals(2, saxDirParser.level);
        assertFalse(saxDirParser.check);
    }

    @Test
    public void testStartElement_Web() {
        saxDirParser.startElement("", "", "Web", null);
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);
    }

    @Test
    public void testStartElement_Local() {
        saxDirParser.startElement("", "", "Local", null);
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);
    }

    @Test
    public void testStartElement_Names() {
        saxDirParser.startElement("", "", "Names", null);
        assertTrue(saxDirParser.check);
    }

    @Test
    public void testStartElement_pkg() {
        saxDirParser.startElement("", "", "pkg", null);
        assertEquals(4, saxDirParser.level);
    }

    @Test
    public void testCharacters_Level0() {
        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.localBrowser);
    }

    @Test
    public void testCharacters_Level10() {
        saxDirParser.check = true;
        saxDirParser.level = 10;
        saxDirParser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.file_editor);
    }

    @Test
    public void testCharacters_Level11_Yes() {
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(new char[]{'Y', 'e', 's'}, 0, 3);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(SAXDirParser.lineNumAccepted);
    }

    @Test
    public void testCharacters_Level11_No() {
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(new char[]{'N', 'o'}, 0, 2);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertFalse(SAXDirParser.lineNumAccepted);
    }

    @Test
    public void testCharacters_Level12() {
        saxDirParser.check = true;
        saxDirParser.level = 12;
        saxDirParser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.lineNumParam);
    }

    @Test
    public void testCharacters_Level30() {
        saxDirParser.check = true;
        saxDirParser.level = 30;
        saxDirParser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", saxDirParser.location);
    }

    @Test
    public void testCharacters_Level31_Yes() {
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(new char[]{'N', 'o'}, 0, 2);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertFalse(saxDirParser.local);
    }

    @Test
    public void testCharacters_Level31_No() {
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(saxDirParser.local);
    }

    @Test
    public void testCharacters_Level4() {
        saxDirParser.check = true;
        saxDirParser.level = 4;
        saxDirParser.location = "location";
        saxDirParser.local = true;
        SAXDirParser.directory = new HashMap<>();
        SAXDirParser.localPackages = new StringList();
        saxDirParser.characters(new char[]{'p', 'k', 'g'}, 0, 3);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("location", SAXDirParser.directory.get("pkg"));
        assertEquals("pkg", SAXDirParser.localPackages.get(0));
    }

    @Test
    public void testEndElement_Group() {
        saxDirParser.check = true;
        saxDirParser.endElement("", "", "Group");
        assertFalse(saxDirParser.check);
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser.directory = new HashMap<>();
        SAXDirParser.directory.put("java.lang", "location1");
        SAXDirParser.directory.put("javax.swing.border", "location2");
        HashMap<String, String> result = saxDirParser.getDirectory();
        assertNotSame(SAXDirParser.directory, result);
        assertEquals(SAXDirParser.directory, result);
    }

    @Test
    public void testGetLocalPackages_Empty() {
        SAXDirParser.localPackages = new StringList();
        assertNull(saxDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages = new StringList();
        SAXDirParser.localPackages.add("pkg1");
        SAXDirParser.localPackages.add("pkg2");
        StringList result = saxDirParser.getLocalPackages();
        assertNotNull(result);
        assertNotSame(SAXDirParser.localPackages, result);
        assertEquals(SAXDirParser.localPackages, result);
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "browser";
        assertEquals("browser", saxDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "editor";
        assertEquals("editor", saxDirParser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber_True() {
        SAXDirParser.lineNumAccepted = true;
        assertTrue(saxDirParser.acceptsLineNumber());
    }

    @Test
    public void testAcceptsLineNumber_False() {
        SAXDirParser.lineNumAccepted = false;
        assertFalse(saxDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "param";
        assertEquals("param", saxDirParser.lineNumberParameter());
    }

    @Test
    public void testMain() throws ParserConfigurationException, SAXException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        //assuming packagedirectory.xml is a valid test file
        myParser.parse("packagedirectory.xml", saxDirParser);
        assertEquals("location1", SAXDirParser.directory.get("java.lang"));
        assertEquals("location2", SAXDirParser.directory.get("javax.swing.border"));
        assertEquals("test", SAXDirParser.localBrowser);
    }
}