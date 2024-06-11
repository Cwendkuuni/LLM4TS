import org.junit.*;
import org.xml.sax.helpers.DefaultHandler;
import java.util.HashMap;
import com.jstevh.tools.StringList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        Assert.assertEquals("", saxDirParser.getWebData());
    }
    
    @Test
    public void testStartElement() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.startElement("", "BrowserLoc", "BrowserLoc", null);
        Assert.assertTrue(saxDirParser.check);
        Assert.assertEquals(0, saxDirParser.level);
        
        saxDirParser.startElement("", "Editor", "Editor", null);
        Assert.assertTrue(saxDirParser.check);
        Assert.assertEquals(10, saxDirParser.level);
        
        saxDirParser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
        Assert.assertTrue(saxDirParser.check);
        Assert.assertEquals(11, saxDirParser.level);
        
        saxDirParser.startElement("", "parameter", "parameter", null);
        Assert.assertTrue(saxDirParser.check);
        Assert.assertEquals(12, saxDirParser.level);
        
        saxDirParser.startElement("", "Group", "Group", null);
        Assert.assertFalse(saxDirParser.check);
        Assert.assertEquals(2, saxDirParser.level);
        
        saxDirParser.startElement("", "Web", "Web", null);
        Assert.assertTrue(saxDirParser.check);
        Assert.assertEquals(30, saxDirParser.level);
        
        saxDirParser.startElement("", "Local", "Local", null);
        Assert.assertTrue(saxDirParser.check);
        Assert.assertEquals(31, saxDirParser.level);
        
        saxDirParser.startElement("", "Names", "Names", null);
        Assert.assertTrue(saxDirParser.check);
        
        saxDirParser.startElement("", "pkg", "pkg", null);
        Assert.assertEquals(4, saxDirParser.level);
    }
    
    @Test
    public void testCharacters() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        
        char[] charArray = "MyBrowser".toCharArray();
        saxDirParser.startElement("", "BrowserLoc", "BrowserLoc", null);
        saxDirParser.characters(charArray, 0, charArray.length);
        Assert.assertFalse(saxDirParser.check);
        Assert.assertEquals(-1, saxDirParser.level);
        Assert.assertEquals("MyBrowser", SAXDirParser.getLocalBrowser());
        
        charArray = "MyEditor".toCharArray();
        saxDirParser.startElement("", "Editor", "Editor", null);
        saxDirParser.characters(charArray, 0, charArray.length);
        Assert.assertFalse(saxDirParser.check);
        Assert.assertEquals(-1, saxDirParser.level);
        Assert.assertEquals("MyEditor", SAXDirParser.getEditor());
        
        charArray = "No".toCharArray();
        saxDirParser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
        saxDirParser.characters(charArray, 0, charArray.length);
        Assert.assertFalse(saxDirParser.check);
        Assert.assertEquals(-1, saxDirParser.level);
        Assert.assertFalse(SAXDirParser.acceptsLineNumber());
        
        charArray = "Param".toCharArray();
        saxDirParser.startElement("", "parameter", "parameter", null);
        saxDirParser.characters(charArray, 0, charArray.length);
        Assert.assertEquals("Param", SAXDirParser.lineNumberParameter());
        
        charArray = "Location".toCharArray();
        saxDirParser.startElement("", "Web", "Web", null);
        saxDirParser.characters(charArray, 0, charArray.length);
        Assert.assertEquals("Location", saxDirParser.location);
        
        charArray = "Yes".toCharArray();
        saxDirParser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
        saxDirParser.characters(charArray, 0, charArray.length);
        Assert.assertEquals(11, saxDirParser.level);
        Assert.assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testEndElement() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.endElement("", "Group", "Group");
        Assert.assertFalse(saxDirParser.check);
    }
    
    @Test
    public void testGetDirectory() throws Exception {
        SAXDirParser.directory.put("java.lang", "location1");
        SAXDirParser.directory.put("javax.swing.border", "location2");
        
        HashMap<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "location1");
        expectedDirectory.put("javax.swing.border", "location2");
        
        Assert.assertEquals(expectedDirectory, SAXDirParser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages() throws Exception {
        SAXDirParser.localPackages.add("package1");
        SAXDirParser.localPackages.add("package2");
        
        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("package1");
        expectedLocalPackages.add("package2");
        
        Assert.assertEquals(expectedLocalPackages, SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalBrowser() throws Exception {
        SAXDirParser.localBrowser = "Browser";
        Assert.assertEquals("Browser", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() throws Exception {
        SAXDirParser.file_editor = "Editor";
        Assert.assertEquals("Editor", SAXDirParser.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumber() throws Exception {
        SAXDirParser.lineNumAccepted = true;
        Assert.assertTrue(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() throws Exception {
        SAXDirParser.lineNumParam = "Param";
        Assert.assertEquals("Param", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testMain() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        XMLReader myReader = myParser.getXMLReader();
        myReader.setContentHandler(saxDirParser);
        myReader.parse("packagedirectory.xml");
    }
}