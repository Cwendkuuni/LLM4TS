import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.helpers.*;
import java.util.*;
import com.jstevh.tools.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

public class SAXDirParserTest {
    
    private SAXDirParser saxDirParser;
    
    @Before
    public void setUp() throws Exception {
        saxDirParser = new SAXDirParser();
    }
    
    @Test
    public void testGetWebData() {
        assertEquals("", saxDirParser.getWebData());
    }
    
    @Test
    public void testStartElement_WhenQnameIsBrowserLoc() throws SAXException {
        saxDirParser.startElement("", "", "BrowserLoc", null);
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_WhenQnameIsEditor() throws SAXException {
        saxDirParser.startElement("", "", "Editor", null);
        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_WhenQnameIsAcceptsLineNumber() throws SAXException {
        saxDirParser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_WhenQnameIsParameter() throws SAXException {
        saxDirParser.startElement("", "", "parameter", null);
        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_WhenQnameIsGroup() throws SAXException {
        saxDirParser.startElement("", "", "Group", null);
        assertEquals(2, saxDirParser.level);
        assertFalse(saxDirParser.check);
    }
    
    @Test
    public void testStartElement_WhenQnameIsWeb() throws SAXException {
        saxDirParser.startElement("", "", "Web", null);
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_WhenQnameIsLocal() throws SAXException {
        saxDirParser.startElement("", "", "Local", null);
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);
    }
    
    @Test
    public void testStartElement_WhenQnameIsNames() throws SAXException {
        saxDirParser.startElement("", "", "Names", null);
        assertTrue(saxDirParser.check);
    }
    
    @Test
    public void testStartElement_WhenQnameIsPkg() throws SAXException {
        saxDirParser.startElement("", "", "pkg", null);
        assertEquals(4, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenCheckIsTrueAndLevelIs0() {
        char[] charArray = {'t', 'e', 's', 't'};
        saxDirParser.check = true;
        saxDirParser.level = 0;
        
        saxDirParser.characters(charArray, 0, 4);
        
        assertEquals("test", SAXDirParser.localBrowser);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenCheckIsTrueAndLevelIs10() {
        char[] charArray = {'t', 'e', 's', 't'};
        saxDirParser.check = true;
        saxDirParser.level = 10;
        
        saxDirParser.characters(charArray, 0, 4);
        
        assertEquals("test", SAXDirParser.file_editor);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenCheckIsTrueAndLevelIs11() {
        char[] charArray = {'y', 'e', 's'};
        saxDirParser.check = true;
        saxDirParser.level = 11;
        
        saxDirParser.characters(charArray, 0, 3);
        
        assertTrue(SAXDirParser.lineNumAccepted);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenCheckIsTrueAndLevelIs11_WithNo() {
        char[] charArray = {'n', 'o'};
        saxDirParser.check = true;
        saxDirParser.level = 11;
        
        saxDirParser.characters(charArray, 0, 2);
        
        assertFalse(SAXDirParser.lineNumAccepted);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenCheckIsTrueAndLevelIs12() {
        char[] charArray = {'t', 'e', 's', 't'};
        saxDirParser.check = true;
        saxDirParser.level = 12;
        
        saxDirParser.characters(charArray, 0, 4);
        
        assertEquals("test", SAXDirParser.lineNumParam);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenCheckIsTrueAndLevelIs30() {
        char[] charArray = {'t', 'e', 's', 't'};
        saxDirParser.check = true;
        saxDirParser.level = 30;
        
        saxDirParser.characters(charArray, 0, 4);
        
        assertEquals("test", saxDirParser.location);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenCheckIsTrueAndLevelIs31() {
        char[] charArray = {'y', 'e', 's'};
        saxDirParser.check = true;
        saxDirParser.level = 31;
        
        saxDirParser.characters(charArray, 0, 3);
        
        assertTrue(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenCheckIsTrueAndLevelIs31_WithNo() {
        char[] charArray = {'n', 'o'};
        saxDirParser.check = true;
        saxDirParser.level = 31;
        
        saxDirParser.characters(charArray, 0, 2);
        
        assertFalse(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenLevelIs4_AndTempStrIsNotEmpty() {
        char[] charArray = {'p', 'k', 'g'};
        saxDirParser.level = 4;
        
        saxDirParser.characters(charArray, 0, 3);
        
        assertEquals("", saxDirParser.location);
        assertEquals(4, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenLevelIs4_AndTempStrEndsWithDot() {
        char[] charArray = {'p', 'k', 'g', '.'};
        saxDirParser.level = 4;
        
        saxDirParser.characters(charArray, 0, 4);
        
        assertEquals("", saxDirParser.location);
        assertEquals(4, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenLevelIs4_AndTempStrIsNotEmptyAndLocalIsTrue() {
        char[] charArray = {'p', 'k', 'g'};
        saxDirParser.level = 4;
        saxDirParser.local = true;
        
        saxDirParser.characters(charArray, 0, 3);
        
        assertEquals("", saxDirParser.location);
        assertEquals(4, saxDirParser.level);
    }
    
    @Test
    public void testCharacters_WhenLevelIs4_AndTempStrIsNotEmptyAndLocalIsFalse() {
        char[] charArray = {'p', 'k', 'g'};
        saxDirParser.level = 4;
        saxDirParser.local = false;
        
        saxDirParser.characters(charArray, 0, 3);
        
        assertEquals("", saxDirParser.location);
        assertEquals(4, saxDirParser.level);
    }
    
    @Test
    public void testEndElement_WhenQnameIsGroup() throws SAXException {
        saxDirParser.endElement("", "", "Group");
        assertFalse(saxDirParser.check);
    }
    
    @Test
    public void testGetDirectory() {
        HashMap<String, String> directory = SAXDirParser.getDirectory();
        assertEquals(0, directory.size());
    }
    
    @Test
    public void testGetLocalPackages_WhenLocalPackagesIsEmpty() {
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalPackages_WhenLocalPackagesIsNotEmpty() {
        SAXDirParser.localPackages.add("package1");
        SAXDirParser.localPackages.add("package2");
        
        StringList result = SAXDirParser.getLocalPackages();
        
        assertArrayEquals(new String[]{"package1", "package2"}, result.toArray());
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