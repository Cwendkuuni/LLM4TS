import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SAXDirParserTest {

    private SAXDirParser saxDirParser;

    @Before
    public void setUp() throws Exception {
        saxDirParser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertNull(saxDirParser.getWebData());
    }

    @Test
    public void testStartElement_BrowserLoc_UpdatesCheckAndLevel() throws SAXException {
        saxDirParser.startElement(null, null, "BrowserLoc", null);
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);
    }

    @Test
    public void testStartElement_Editor_UpdatesCheckAndLevel() throws SAXException {
        saxDirParser.startElement(null, null, "Editor", null);
        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);
    }

    @Test
    public void testStartElement_AcceptsLineNumber_UpdatesCheckAndLevel() throws SAXException {
        saxDirParser.startElement(null, null, "acceptsLineNumber", null);
        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);
    }

    @Test
    public void testStartElement_Parameter_UpdatesCheckAndLevel() throws SAXException {
        saxDirParser.startElement(null, null, "parameter", null);
        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);
    }

    @Test
    public void testStartElement_Group_UpdatesLevel() throws SAXException {
        saxDirParser.startElement(null, null, "Group", null);
        assertEquals(2, saxDirParser.level);
    }

    @Test
    public void testStartElement_Web_UpdatesCheckAndLevel() throws SAXException {
        saxDirParser.startElement(null, null, "Web", null);
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);
    }

    @Test
    public void testStartElement_Local_UpdatesCheckAndLevel() throws SAXException {
        saxDirParser.startElement(null, null, "Local", null);
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);
    }

    @Test
    public void testStartElement_Names_UpdatesCheck() throws SAXException {
        saxDirParser.startElement(null, null, "Names", null);
        assertTrue(saxDirParser.check);
    }

    @Test
    public void testStartElement_Pkg_UpdatesLevel() throws SAXException {
        saxDirParser.startElement(null, null, "pkg", null);
        assertEquals(4, saxDirParser.level);
    }

    @Test
    public void testCharacters_Level0_UpdatesLocalBrowserAndResetsCheckAndLevel() {
        saxDirParser.check = true;
        saxDirParser.level = 0;

        saxDirParser.characters("TestString".toCharArray(), 0, 10);

        assertEquals("TestString", SAXDirParser.getLocalBrowser());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }

    @Test
    public void testCharacters_Level10_UpdatesFileEditorAndResetsCheckAndLevel() {
        saxDirParser.check = true;
        saxDirParser.level = 10;

        saxDirParser.characters("TestString".toCharArray(), 0, 10);

        assertEquals("TestString", SAXDirParser.getEditor());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }

    @Test
    public void testCharacters_Level11_UpdatesLineNumAcceptedBasedOnYesAndResetsCheckAndLevel() {
        saxDirParser.check = true;
        saxDirParser.level = 11;

        saxDirParser.characters("Yes".toCharArray(), 0, 3);

        assertTrue(SAXDirParser.acceptsLineNumber());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }

    @Test
    public void testCharacters_Level11_UpdatesLineNumAcceptedBasedOnNoAndResetsCheckAndLevel() {
        saxDirParser.check = true;
        saxDirParser.level = 11;

        saxDirParser.characters("No".toCharArray(), 0, 2);

        assertFalse(SAXDirParser.acceptsLineNumber());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }

    @Test
    public void testCharacters_Level12_UpdatesLineNumParamAndResetsCheckAndLevel() {
        saxDirParser.check = true;
        saxDirParser.level = 12;

        saxDirParser.characters("TestString".toCharArray(), 0, 10);

        assertEquals("TestString", SAXDirParser.lineNumberParameter());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }

    @Test
    public void testCharacters_Level30_UpdatesLocationAndResetsCheckAndLevel() {
        saxDirParser.check = true;
        saxDirParser.level = 30;

        saxDirParser.characters("TestString".toCharArray(), 0, 10);

        assertEquals("TestString", saxDirParser.location);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }

    @Test
    public void testCharacters_Level31_UpdatesLocalBasedOnNoAndResetsCheckAndLevel() {
        saxDirParser.check = true;
        saxDirParser.level = 31;

        saxDirParser.characters("No".toCharArray(), 0, 2);

        assertFalse(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }

    @Test
    public void testCharacters_Level31_UpdatesLocalBasedOnOtherValuesAndResetsCheckAndLevel() {
        saxDirParser.check = true;
        saxDirParser.level = 31;

        saxDirParser.characters("TestString".toCharArray(), 0, 10);

        assertTrue(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }

    @Test
    public void testCharacters_Level4_AddsToDirectoryAndLocalPackages() {
        saxDirParser.location = "TestLocation";
        saxDirParser.level = 4;
        saxDirParser.local = true;

        saxDirParser.characters("java.lang".toCharArray(), 0, 9);

        assertEquals("TestLocation", SAXDirParser.getDirectory().get("java.lang"));
        assertEquals("java.lang", SAXDirParser.getLocalPackages().get(0));
    }

    @Test
    public void testEndElement_Group_UpdatesCheck() throws SAXException {
        saxDirParser.check = true;

        saxDirParser.endElement(null, null, "Group");

        assertFalse(saxDirParser.check);
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser.directory.put("java.lang", "TestLocation1");
        SAXDirParser.directory.put("javax.swing.border", "TestLocation2");

        HashMap<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "TestLocation1");
        expectedDirectory.put("javax.swing.border", "TestLocation2");

        assertEquals(expectedDirectory, SAXDirParser.getDirectory());
    }

    @Test
    public void testGetLocalPackages_ReturnsNullWhenEmpty() {
        assertNull(SAXDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalPackages_ReturnsLocalPackagesWhenNotEmpty() {
        SAXDirParser.localPackages.add("java.lang");
        SAXDirParser.localPackages.add("javax.swing.border");

        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("java.lang");
        expectedLocalPackages.add("javax.swing.border");

        assertEquals(expectedLocalPackages, SAXDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "TestBrowser";

        assertEquals("TestBrowser", SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "TestEditor";

        assertEquals("TestEditor", SAXDirParser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser.lineNumAccepted = true;

        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "TestParameter";

        assertEquals("TestParameter", SAXDirParser.lineNumberParameter());
    }
}