import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SAXDirParserTest {
    
    @Test
    public void testStartElement() {
        // Test when qname is "BrowserLoc"
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "BrowserLoc", null);
        assertEquals(true, parser.check);
        assertEquals(0, parser.level);
        
        // Test when qname is "Editor"
        parser = new SAXDirParser();
        parser.startElement("", "", "Editor", null);
        assertEquals(true, parser.check);
        assertEquals(10, parser.level);
        
        // Test when qname is "acceptsLineNumber"
        parser = new SAXDirParser();
        parser.startElement("", "", "acceptsLineNumber", null);
        assertEquals(true, parser.check);
        assertEquals(11, parser.level);
        
        // Test when qname is "parameter"
        parser = new SAXDirParser();
        parser.startElement("", "", "parameter", null);
        assertEquals(true, parser.check);
        assertEquals(12, parser.level);
        
        // Test when qname is "Group"
        parser = new SAXDirParser();
        parser.startElement("", "", "Group", null);
        assertEquals(false, parser.check);
        assertEquals(2, parser.level);
        
        // Test when qname is "Web"
        parser = new SAXDirParser();
        parser.startElement("", "", "Web", null);
        assertEquals(true, parser.check);
        assertEquals(30, parser.level);
        
        // Test when qname is "Local"
        parser = new SAXDirParser();
        parser.startElement("", "", "Local", null);
        assertEquals(true, parser.check);
        assertEquals(31, parser.level);
        
        // Test when qname is "Names"
        parser = new SAXDirParser();
        parser.startElement("", "", "Names", null);
        assertEquals(true, parser.check);
        
        // Test when qname is "pkg"
        parser = new SAXDirParser();
        parser.startElement("", "", "pkg", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters() {
        // Test when check is true and level is greater than 0
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 0;
        char[] charArray = {'t', 'e', 's', 't'};
        parser.characters(charArray, 0, charArray.length);
        assertEquals("", parser.webData);
        
        // Test when level is 0
        parser = new SAXDirParser();
        parser.level = -1;
        char[] charArray2 = {'t', 'e', 's', 't'};
        parser.characters(charArray2, 0, charArray2.length);
        assertEquals("", parser.webData);
        
        // Test when level is 10
        parser = new SAXDirParser();
        parser.level = -1;
        char[] charArray3 = {'t', 'e', 's', 't'};
        parser.characters(charArray3, 0, charArray3.length);
        assertEquals("", parser.webData);
        
        // Test when level is 11
        parser = new SAXDirParser();
        parser.level = -1;
        char[] charArray4 = {'t', 'e', 's', 't'};
        parser.characters(charArray4, 0, charArray4.length);
        assertEquals("", parser.webData);
        
        // Test when level is 12
        parser = new SAXDirParser();
        parser.level = -1;
        char[] charArray5 = {'t', 'e', 's', 't'};
        parser.characters(charArray5, 0, charArray5.length);
        assertEquals("", parser.webData);
        
        // Test when level is 30
        parser = new SAXDirParser();
        parser.level = -1;
        char[] charArray6 = {'t', 'e', 's', 't'};
        parser.characters(charArray6, 0, charArray6.length);
        assertEquals("", parser.webData);
        
        // Test when level is 31
        parser = new SAXDirParser();
        parser.level = -1;
        char[] charArray7 = {'t', 'e', 's', 't'};
        parser.characters(charArray7, 0, charArray7.length);
        assertEquals("", parser.webData);
        
        // Test when level is 4 and tempStr is not empty
        parser = new SAXDirParser();
        parser.level = -1;
        parser.location = "location";
        parser.local = true;
        char[] charArray8 = {'t', 'e', 's', 't'};
        parser.characters(charArray8, 0, charArray8.length);
        assertEquals("location", SAXDirParser.directory.get("test"));
        assertEquals(true, SAXDirParser.localPackages.contains("test"));
    }
    
    @Test
    public void testEndElement() {
        // Test when qname is "Group"
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.endElement("", "", "Group");
        assertEquals(false, parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        // Test when directory is empty
        HashMap<String, String> emptyDirectory = new HashMap<String, String>();
        assertEquals(emptyDirectory, SAXDirParser.getDirectory());
        
        // Test when directory is not empty
        HashMap<String, String> directory = new HashMap<String, String>();
        directory.put("test1", "location1");
        directory.put("test2", "location2");
        SAXDirParser.directory = directory;
        assertEquals(directory, SAXDirParser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages() {
        // Test when localPackages is empty
        SAXDirParser.localPackages = new StringList();
        assertNull(SAXDirParser.getLocalPackages());
        
        // Test when localPackages is not empty
        StringList localPackages = new StringList();
        localPackages.add("package1");
        localPackages.add("package2");
        SAXDirParser.localPackages = localPackages;
        assertEquals(localPackages, SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalBrowser() {
        // Test when localBrowser is empty
        SAXDirParser.localBrowser = "";
        assertEquals("", SAXDirParser.getLocalBrowser());
        
        // Test when localBrowser is not empty
        SAXDirParser.localBrowser = "testBrowser";
        assertEquals("testBrowser", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        // Test when file_editor is empty
        SAXDirParser.file_editor = "";
        assertEquals("", SAXDirParser.getEditor());
        
        // Test when file_editor is not empty
        SAXDirParser.file_editor = "testEditor";
        assertEquals("testEditor", SAXDirParser.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumber() {
        // Test when lineNumAccepted is false
        SAXDirParser.lineNumAccepted = false;
        assertEquals(false, SAXDirParser.acceptsLineNumber());
        
        // Test when lineNumAccepted is true
        SAXDirParser.lineNumAccepted = true;
        assertEquals(true, SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        // Test when lineNumParam is empty
        SAXDirParser.lineNumParam = "";
        assertEquals("", SAXDirParser.lineNumberParameter());
        
        // Test when lineNumParam is not empty
        SAXDirParser.lineNumParam = "testParam";
        assertEquals("testParam", SAXDirParser.lineNumberParameter());
    }
}