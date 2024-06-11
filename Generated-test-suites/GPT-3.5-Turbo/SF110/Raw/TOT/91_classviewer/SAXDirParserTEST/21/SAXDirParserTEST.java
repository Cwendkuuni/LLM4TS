
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;

public class SAXDirParserTest {

    private SAXDirParser saxDirParser;

    @Before
    public void setup() {
        saxDirParser = new SAXDirParser();
    }
  
    @Test
    public void testGetWebData() {
        saxDirParser.webData = "Test Data";
        assertEquals("Test Data", saxDirParser.getWebData());
    }
  
    @Test
    public void testConstructor() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }
  
    @Test
    public void testStartElement_WhenQNameIsBrowserLoc() throws Exception {
        saxDirParser.startElement("", "", "BrowserLoc", null);
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);
    }
  
    @Test
    public void testStartElement_WhenQNameIsEditor() throws Exception {
        saxDirParser.startElement("", "", "Editor", null);
        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);
    }
  
    @Test
    public void testStartElement_WhenQNameIsAcceptsLineNumber() throws Exception {
        saxDirParser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);
    }
  
    @Test
    public void testStartElement_WhenQNameIsParameter() throws Exception {
        saxDirParser.startElement("", "", "parameter", null);
        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);
    }
  
    @Test
    public void testStartElement_WhenQNameIsGroup() throws Exception {
        saxDirParser.startElement("", "", "Group", null);
        assertFalse(saxDirParser.check);
        assertEquals(2, saxDirParser.level);
    }
  
    @Test
    public void testStartElement_WhenQNameIsWeb() throws Exception {
        saxDirParser.startElement("", "", "Web", null);
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);
    }
  
    @Test
    public void testStartElement_WhenQNameIsLocal() throws Exception {
        saxDirParser.startElement("", "", "Local", null);
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);
    }
  
    @Test
    public void testStartElement_WhenQNameIsNames() throws Exception {
        saxDirParser.startElement("", "", "Names", null);
        assertTrue(saxDirParser.check);
    }
  
    @Test
    public void testStartElement_WhenQNameIsPkg() throws Exception {
        saxDirParser.startElement("", "", "pkg", null);
        assertEquals(4, saxDirParser.level);
    }
  
    @Test
    public void testCharacters_WhenCheckIsTrueAndLevelIsInvalid() {
        saxDirParser.check = true;
        saxDirParser.level = -1;
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
    }
  
    @Test
    public void testCharacters_WhenLevelIs0() {
        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        assertEquals("test", SAXDirParser.getLocalBrowser());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
  
    @Test
    public void testCharacters_WhenLevelIs10() {
        saxDirParser.check = true;
        saxDirParser.level = 10;
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        assertEquals("test", SAXDirParser.getEditor());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
  
    @Test
    public void testCharacters_WhenLevelIs11AndTempStrIsYes() {
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(new char[] {'Y', 'e', 's'}, 0, 3);
        assertTrue(SAXDirParser.acceptsLineNumber());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
  
    @Test
    public void testCharacters_WhenLevelIs11AndTempStrIsNo() {
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(new char[] {'N', 'o'}, 0, 2);
        assertFalse(SAXDirParser.acceptsLineNumber());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
  
    @Test
    public void testCharacters_WhenLevelIs12() {
        saxDirParser.check = true;
        saxDirParser.level = 12;
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        assertEquals("test", SAXDirParser.lineNumberParameter());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
  
    @Test
    public void testCharacters_WhenLevelIs30() {
        saxDirParser.check = true;
        saxDirParser.level = 30;
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        assertEquals("test", saxDirParser.location);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
  
    @Test
    public void testCharacters_WhenLevelIs31AndTempStrIsNotNo() {
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        assertTrue(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
  
    @Test
    public void testCharacters_WhenLevelIs31AndTempStrIsNo() {
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(new char[] {'N', 'o'}, 0, 2);
        assertFalse(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
  
    @Test
    public void testCharacters_WhenLevelIs4AndTempStrIsNotEmpty() {
        saxDirParser.level = 4;
        saxDirParser.location = "test_location";
        saxDirParser.local = true;
    
        SAXDirParser.directory = new HashMap<>();
        SAXDirParser.localPackages = new StringList();
    
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
    
        HashMap<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("test", "test_location");
        assertEquals(expectedDirectory, SAXDirParser.getDirectory());
    
        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("test");
        assertEquals(expectedLocalPackages, SAXDirParser.getLocalPackages());
    
        assertFalse(saxDirParser.check);
    }
  
}
