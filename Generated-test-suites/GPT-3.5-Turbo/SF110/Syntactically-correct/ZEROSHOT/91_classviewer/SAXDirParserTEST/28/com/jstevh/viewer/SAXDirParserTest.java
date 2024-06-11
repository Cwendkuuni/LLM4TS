import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SAXDirParserTest {
    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        parser.webData = "test";
        
        assertEquals("test", parser.getWebData());
    }
    
    @Test
    public void testStartElement() {
        SAXDirParser parser = new SAXDirParser();
        Attributes atts = null;
        
        parser.startElement("", "", "BrowserLoc", atts);
        assertEquals(true, parser.check);
        assertEquals(0, parser.level);
        
        parser.startElement("", "", "Editor", atts);
        assertEquals(true, parser.check);
        assertEquals(10, parser.level);
        
        parser.startElement("", "", "acceptsLineNumber", atts);
        assertEquals(true, parser.check);
        assertEquals(11, parser.level);
        
        parser.startElement("", "", "parameter", atts);
        assertEquals(true, parser.check);
        assertEquals(12, parser.level);
        
        parser.startElement("", "", "Group", atts);
        assertEquals(2, parser.level);
        assertEquals(false, parser.check);
        
        parser.startElement("", "", "Web", atts);
        assertEquals(true, parser.check);
        assertEquals(30, parser.level);
        
        parser.startElement("", "", "Local", atts);
        assertEquals(true, parser.check);
        assertEquals(31, parser.level);
        
        parser.startElement("", "", "Names", atts);
        assertEquals(true, parser.check);
        
        parser.startElement("", "", "pkg", atts);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters() {
        SAXDirParser parser = new SAXDirParser();
        
        char[] charArray = {'t', 'e', 's', 't'};
        int start = 0;
        int length = charArray.length;
        
        parser.level = 0;
        parser.check = true;
        parser.characters(charArray, start, length);
        assertEquals("test", SAXDirParser.localBrowser);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 10;
        parser.check = true;
        parser.characters(charArray, start, length);
        assertEquals("test", SAXDirParser.file_editor);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 11;
        parser.check = true;
        parser.characters(charArray, start, length);
        assertEquals(false, SAXDirParser.lineNumAccepted);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 12;
        parser.check = true;
        parser.characters(charArray, start, length);
        assertEquals("test", SAXDirParser.lineNumParam);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 30;
        parser.check = true;
        parser.characters(charArray, start, length);
        assertEquals("test", parser.location);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 31;
        parser.check = true;
        parser.characters(charArray, start, length);
        assertEquals(true, parser.local);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 4;
        parser.check = true;
        parser.characters(charArray, start, length);
        HashMap<String, String> directory = new HashMap<String, String>();
        directory.put("test", parser.location);
        assertEquals(directory, SAXDirParser.directory);
        assertEquals(true, SAXDirParser.localPackages.contains("test"));
    }
    
    @Test
    public void testEndElement() {
        SAXDirParser parser = new SAXDirParser();
        
        parser.check = true;
        parser.endElement("", "", "Group");
        assertEquals(false, parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        SAXDirParser.directory = new HashMap<String, String>();
        SAXDirParser.directory.put("test", "location");
        
        HashMap<String, String> expectedDirectory = new HashMap<String, String>();
        expectedDirectory.put("test", "location");
        
        assertEquals(expectedDirectory, SAXDirParser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages = new StringList();
        SAXDirParser.localPackages.add("test");
        
        StringList expectedList = new StringList();
        expectedList.add("test");
        
        assertEquals(expectedList, SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "test";
        
        assertEquals("test", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "test";
        
        assertEquals("test", SAXDirParser.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser.lineNumAccepted = true;
        
        assertEquals(true, SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "test";
        
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }
}