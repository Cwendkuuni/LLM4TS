import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        String webData = "Test web data";
        parser.webData = webData;
        assertEquals(webData, parser.getWebData());
    }

    @Test
    public void testSAXDirParser() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        assertNotNull(parser);
    }

    @Test
    public void testStartElement_BrowserLoc() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("namespace", "local", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
    }

    @Test
    public void testStartElement_Editor() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("namespace", "local", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
    }

    @Test
    public void testStartElement_AcceptsLineNumber() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("namespace", "local", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
    }

    @Test
    public void testStartElement_Parameter() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("namespace", "local", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
    }

    @Test
    public void testStartElement_Group() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("namespace", "local", "Group", null);
        assertFalse(parser.check);
        assertEquals(2, parser.level);
    }

    @Test
    public void testStartElement_Web() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("namespace", "local", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
    }

    @Test
    public void testStartElement_Local() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("namespace", "local", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
    }

    @Test
    public void testStartElement_Names() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("namespace", "local", "Names", null);
        assertTrue(parser.check);
    }

    @Test
    public void testStartElement_Pkg() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("namespace", "local", "pkg", null);
        assertEquals(4, parser.level);
    }

    @Test
    public void testCharacters_Level0() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 0;
        String tempStr = "Test browser";
        parser.characters(tempStr.toCharArray(), 0, tempStr.length());
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals(tempStr, SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testCharacters_Level10() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 10;
        String tempStr = "Test editor";
        parser.characters(tempStr.toCharArray(), 0, tempStr.length());
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals(tempStr, SAXDirParser.getEditor());
    }

    @Test
    public void testCharacters_Level11_Yes() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 11;
        String tempStr = "Yes";
        parser.characters(tempStr.toCharArray(), 0, tempStr.length());
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testCharacters_Level11_No() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 11;
        String tempStr = "No";
        parser.characters(tempStr.toCharArray(), 0, tempStr.length());
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testCharacters_Level12() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 12;
        String tempStr = "Test parameter";
        parser.characters(tempStr.toCharArray(), 0, tempStr.length());
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals(tempStr, SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testCharacters_Level30() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 30;
        String tempStr = "Test location";
        parser.characters(tempStr.toCharArray(), 0, tempStr.length());
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals(tempStr, parser.location);
    }

    @Test
    public void testCharacters_Level31_No() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 31;
        String tempStr = "No";
        parser.characters(tempStr.toCharArray(), 0, tempStr.length());
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
    }

    @Test
    public void testCharacters_Level31_Other() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 31;
        String tempStr = "Yes";
        parser.characters(tempStr.toCharArray(), 0, tempStr.length());
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
    }

    @Test
    public void testCharacters_Level4() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 4;
        parser.check = true;
        parser.local = true;
        String tempStr = "Test package";
        parser.location = "Test location";
        parser.characters(tempStr.toCharArray(), 0, tempStr.length());
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals(parser.location, SAXDirParser.getDirectory().get(tempStr));
        assertEquals(tempStr, SAXDirParser.getLocalPackages().get(0));
    }

    @Test
    public void testEndElement_Group() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.endElement("namespace", "local", "Group");
        assertFalse(parser.check);
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser.parser = new SAXDirParser();
        SAXDirParser.directory.put("Test package", "Test location");
        assertNotNull(SAXDirParser.getDirectory().get("Test package"));
    }

    @Test
    public void testGetLocalPackages_Null() {
        SAXDirParser.parser = new SAXDirParser();
        assertNull(SAXDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalPackages_NotNull() {
        SAXDirParser.parser = new SAXDirParser();
        SAXDirParser.localPackages.add("Test package");
        assertNotNull(SAXDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.parser = new SAXDirParser();
        SAXDirParser.localBrowser = "Test browser";
        assertEquals(SAXDirParser.localBrowser, SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        SAXDirParser.parser = new SAXDirParser();
        SAXDirParser.file_editor = "Test editor";
        assertEquals(SAXDirParser.file_editor, SAXDirParser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser.parser = new SAXDirParser();
        SAXDirParser.lineNumAccepted = true;
        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser.parser = new SAXDirParser();
        SAXDirParser.lineNumParam = "Test parameter";
        assertEquals(SAXDirParser.lineNumParam, SAXDirParser.lineNumberParameter());
    }

    @Test (expected = Exception.class)
    public void testMain_ThrowsException() throws Exception {
        SAXDirParser.main(new String[] {});
    }

    @Test
    public void testMain_Directory() throws Exception {
        SAXDirParser.parser = new SAXDirParser();
        SAXDirParser.directory.put("java.lang", "Test location");
        assertEquals("Test location", SAXDirParser.directory.get("java.lang"));
    }

    @Test
    public void testMain_LocalBrowser() throws Exception {
        SAXDirParser.parser = new SAXDirParser();
        SAXDirParser.localBrowser = "Test browser";
        assertEquals("Test browser", SAXDirParser.localBrowser);
    }

    @Test
    public void testMain_LocalBrowser_NotEqual() throws Exception {
        SAXDirParser.parser = new SAXDirParser();
        SAXDirParser.localBrowser = "Test browser";
        assertNotEquals("Not equal", SAXDirParser.localBrowser);
    }

    @After
    public void tearDown() {
        SAXDirParser.directory.clear();
        SAXDirParser.localPackages.clear();
        SAXDirParser.localBrowser = "";
        SAXDirParser.file_editor = "";
        SAXDirParser.lineNumAccepted = false;
        SAXDirParser.lineNumParam = "";
    }
}