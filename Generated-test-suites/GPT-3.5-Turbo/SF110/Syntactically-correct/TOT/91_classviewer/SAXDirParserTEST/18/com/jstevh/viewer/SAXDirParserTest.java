import org.junit.Test;
import static org.junit.Assert.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        String webData = parser.getWebData();
        assertEquals("", webData);
    }

    @Test
    public void testStartElementBrowserLoc() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "BrowserLoc", null);

        assertTrue(parser.check);
        assertEquals(0, parser.level);
    }

    @Test
    public void testStartElementEditor() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Editor", null);

        assertTrue(parser.check);
        assertEquals(10, parser.level);
    }

    @Test
    public void testStartElementAcceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "acceptsLineNumber", null);

        assertTrue(parser.check);
        assertEquals(11, parser.level);
    }

    @Test
    public void testStartElementParameter() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "parameter", null);

        assertTrue(parser.check);
        assertEquals(12, parser.level);
    }

    @Test
    public void testStartElementGroup() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Group", null);

        assertEquals(2, parser.level);
        assertFalse(parser.check);
    }

    @Test
    public void testStartElementWeb() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Web", null);

        assertTrue(parser.check);
        assertEquals(30, parser.level);
    }

    @Test
    public void testStartElementLocal() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Local", null);

        assertTrue(parser.check);
        assertEquals(31, parser.level);
    }

    @Test
    public void testStartElementNames() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Names", null);

        assertTrue(parser.check);
    }

    @Test
    public void testStartElementPkg() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "pkg", null);
        
        assertEquals(4, parser.level);
    }

    @Test
    public void testCharactersLevelZero() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 0;
        char[] charArray = "TestString".toCharArray();
        parser.characters(charArray, 0, charArray.length);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("TestString", SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testCharactersLevelTen() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 10;
        char[] charArray = "TestString".toCharArray();
        parser.characters(charArray, 0, charArray.length);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("TestString", SAXDirParser.getEditor());
    }

    @Test
    public void testCharactersLevelElevenYes() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 11;
        char[] charArray = "Yes".toCharArray();
        parser.characters(charArray, 0, charArray.length);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testCharactersLevelElevenNo() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 11;
        char[] charArray = "No".toCharArray();
        parser.characters(charArray, 0, charArray.length);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testCharactersLevelTwelve() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 12;
        char[] charArray = "TestString".toCharArray();
        parser.characters(charArray, 0, charArray.length);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("TestString", SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testCharactersLevelThirty() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 30;
        char[] charArray = "TestString".toCharArray();
        parser.characters(charArray, 0, charArray.length);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("TestString", parser.location);
    }

    @Test
    public void testCharactersLevelThirtyOneYes() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 31;
        char[] charArray = "Yes".toCharArray();
        parser.characters(charArray, 0, charArray.length);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
    }

    @Test
    public void testCharactersLevelThirtyOneNo() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 31;
        char[] charArray = "No".toCharArray();
        parser.characters(charArray, 0, charArray.length);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
    }

    @Test
    public void testCharactersLevelFour() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 4;
        parser.location = "TestLocation";
        parser.local = true;
        char[] charArray = "java.lang".toCharArray();
        parser.characters(charArray, 0, charArray.length);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("TestLocation", SAXDirParser.getDirectory().get("java.lang"));
        assertNotNull(SAXDirParser.getLocalPackages());
        assertTrue(SAXDirParser.getLocalPackages().contains("java.lang"));
    }

    @Test
    public void testEndElementGroup() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.endElement("", "", "Group");

        assertFalse(parser.check);
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser.directory.put("java.lang", "TestLocation");
        assertNull(SAXDirParser.getDirectory().get("javax.swing.border"));
    }

    @Test
    public void testGetLocalPackagesEmpty() {
        SAXDirParser.localPackages.clear();
        assertNull(SAXDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages.add("java.lang");
        assertNotNull(SAXDirParser.getLocalPackages());
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
        SAXDirParser.lineNumParam = "TestParam";
        assertEquals("TestParam", SAXDirParser.lineNumberParameter());
    }
}