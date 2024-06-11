import static org.junit.Assert.*;
import org.junit.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testStartElement() throws Exception {
        SAXDirParser parser = new SAXDirParser();

        // Test when qname is "BrowserLoc"
        parser.startElement("", "", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);

        // Test when qname is "Editor"
        parser.startElement("", "", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);

        // Test when qname is "acceptsLineNumber"
        parser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);

        // Test when qname is "parameter"
        parser.startElement("", "", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);

        // Test when qname is "Group"
        parser.startElement("", "", "Group", null);
        assertEquals(2, parser.level);
        assertFalse(parser.check);

        // Test when qname is "Web"
        parser.startElement("", "", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);

        // Test when qname is "Local"
        parser.startElement("", "", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);

        // Test when qname is "Names"
        parser.startElement("", "", "Names", null);
        assertTrue(parser.check);

        // Test when qname is "pkg"
        parser.startElement("", "", "pkg", null);
        assertEquals(4, parser.level);
    }

    @Test
    public void testCharacters() {
        SAXDirParser parser = new SAXDirParser();

        // Test when level is 0
        parser.level = 0;
        parser.check = true;
        parser.characters("Test Data".toCharArray(), 0, 9);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Test Data", SAXDirParser.getLocalBrowser());

        // Test when level is 10
        parser.level = 10;
        parser.check = true;
        parser.characters("Test Data".toCharArray(), 0, 9);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Test Data", SAXDirParser.getEditor());

        // Test when level is 11 and tempStr is "Yes"
        parser.level = 11;
        parser.check = true;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());

        // Test when level is 11 and tempStr is not "Yes"
        parser.level = 11;
        parser.check = true;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.acceptsLineNumber());

        // Test when level is 12
        parser.level = 12;
        parser.check = true;
        parser.characters("Test Data".toCharArray(), 0, 9);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("Test Data", SAXDirParser.lineNumberParameter());

        // Test when level is 30
        parser.level = 30;
        parser.check = true;
        parser.characters("Test Data".toCharArray(), 0, 9);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        String location = parser.location;
        assertEquals("Test Data", location);

        // Test when level is 31 and tempStr is not "No"
        parser.level = 31;
        parser.check = true;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);

        // Test when level is 31 and tempStr is "No"
        parser.level = 31;
        parser.check = true;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);

        // Test when level is 4 and tempStr is not empty
        parser.level = 4;
        parser.check = true;
        parser.local = true;
        parser.location = location;
        parser.characters("Test Data".toCharArray(), 0, 9);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals(location, SAXDirParser.getDirectory().get("Test Data"));
        assertEquals("Test Data", SAXDirParser.getLocalPackages().get(0));

        // Test when level is 4 and tempStr is empty
        parser.level = 4;
        parser.check = true;
        parser.local = true;
        parser.location = location;
        parser.characters("".toCharArray(), 0, 0);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertNull(SAXDirParser.getLocalPackages());
    }

    @Test
    public void testEndElement() throws Exception {
        SAXDirParser parser = new SAXDirParser();

        // Test when qname is "Group"
        parser.endElement("", "", "Group");
        assertFalse(parser.check);

        // Test when qname is not "Group"
        parser.endElement("", "", "Test");
        assertFalse(parser.check);
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser.directory = new HashMap<>();
        SAXDirParser.directory.put("Test Key", "Test Value");
        HashMap<String, String> expected = new HashMap<>();
        expected.put("Test Key", "Test Value");
        assertEquals(expected, SAXDirParser.getDirectory());
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages = new StringList();
        SAXDirParser.localPackages.add("Test Package");
        StringList expected = new StringList();
        expected.add("Test Package");
        assertEquals(expected, SAXDirParser.getLocalPackages());
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
}