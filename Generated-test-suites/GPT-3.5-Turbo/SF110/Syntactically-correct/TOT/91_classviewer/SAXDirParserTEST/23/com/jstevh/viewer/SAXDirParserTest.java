import org.junit.*;
import static org.junit.Assert.*;
import java.util.HashMap;

public class SAXDirParserTest {
    private SAXDirParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertNull(parser.getWebData());
    }

    @Test
    public void testStartElement_BrowserLoc() throws Exception {
        parser.startElement("", "", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
    }

    @Test
    public void testStartElement_Editor() throws Exception {
        parser.startElement("", "", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
    }

    @Test
    public void testStartElement_acceptsLineNumber() throws Exception {
        parser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
    }

    @Test
    public void testStartElement_parameter() throws Exception {
        parser.startElement("", "", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
    }

    @Test
    public void testStartElement_Group() throws Exception {
        parser.startElement("", "", "Group", null);
        assertFalse(parser.check);
        assertEquals(2, parser.level);
    }

    @Test
    public void testStartElement_Web() throws Exception {
        parser.startElement("", "", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
    }

    @Test
    public void testStartElement_Local() throws Exception {
        parser.startElement("", "", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
    }

    @Test
    public void testStartElement_Names() throws Exception {
        parser.startElement("", "", "Names", null);
        assertTrue(parser.check);
    }

    @Test
    public void testStartElement_pkg() throws Exception {
        parser.startElement("", "", "pkg", null);
        assertEquals(4, parser.level);
    }

    @Test
    public void testCharacters_level0() {
        parser.level = 0;
        parser.check = true;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.localBrowser);
    }

    @Test
    public void testCharacters_level10() {
        parser.level = 10;
        parser.check = true;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.file_editor);
    }

    @Test
    public void testCharacters_level11_Yes() {
        parser.level = 11;
        parser.check = true;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.lineNumAccepted);
    }

    @Test
    public void testCharacters_level11_No() {
        parser.level = 11;
        parser.check = true;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.lineNumAccepted);
    }

    @Test
    public void testCharacters_level12() {
        parser.level = 12;
        parser.check = true;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.lineNumParam);
    }

    @Test
    public void testCharacters_level30() {
        parser.level = 30;
        parser.check = true;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", parser.location);
    }

    @Test
    public void testCharacters_level31_Yes() {
        parser.level = 31;
        parser.check = true;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
    }

    @Test
    public void testCharacters_level31_No() {
        parser.level = 31;
        parser.check = true;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
    }

    @Test
    public void testCharacters_level4() {
        parser.level = 4;
        parser.check = true;
        parser.location = "location";
        parser.local = true;
        parser.characters("pkg".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("location", SAXDirParser.directory.get("pkg"));
        assertEquals("pkg", SAXDirParser.localPackages.get(0));
    }

    @Test
    public void testEndElement_Group() throws Exception {
        parser.endElement("", "", "Group");
        assertFalse(parser.check);;
    }

    @Test
    public void testGetDirectory() {
        HashMap<String, String> directory = new HashMap<>();
        directory.put("pkg", "location");
        SAXDirParser.directory = directory;
        assertEquals(directory, parser.getDirectory());
    }

    @Test
    public void testGetLocalPackages_Null() {
        SAXDirParser.localPackages.clear();
        assertNull(parser.getLocalPackages());
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages.add("pkg");
        assertEquals(SAXDirParser.localPackages, parser.getLocalPackages());
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "browser";
        assertEquals("browser", parser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "editor";
        assertEquals("editor", parser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser.lineNumAccepted = true;
        assertTrue(parser.acceptsLineNumber());
        SAXDirParser.lineNumAccepted = false;
        assertFalse(parser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "param";
        assertEquals("param", parser.lineNumberParameter());
    }

    @Test
    public void testMain() throws Exception {
        SAXDirParser.main(null);
    }
}