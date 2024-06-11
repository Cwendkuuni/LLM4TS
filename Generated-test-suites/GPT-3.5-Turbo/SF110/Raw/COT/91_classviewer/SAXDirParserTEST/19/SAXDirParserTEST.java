import static org.junit.Assert.*;
import org.junit.Test;

public class SAXDirParserTest {
    
    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }
    
    @Test
    public void testSAXDirParser() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.webData);
    }
    
    @Test
    public void testStartElement() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "BrowserLoc", "", null);
        assertEquals(true, parser.check);
        assertEquals(0, parser.level);
        
        parser.startElement("", "Editor", "", null);
        assertEquals(true, parser.check);
        assertEquals(10, parser.level);
        
        parser.startElement("", "acceptsLineNumber", "", null);
        assertEquals(true, parser.check);
        assertEquals(11, parser.level);
        
        parser.startElement("", "parameter", "", null);
        assertEquals(true, parser.check);
        assertEquals(12, parser.level);
        
        parser.startElement("", "Group", "", null);
        assertEquals(false, parser.check);
        assertEquals(2, parser.level);
        
        parser.startElement("", "Web", "", null);
        assertEquals(true, parser.check);
        assertEquals(30, parser.level);
        
        parser.startElement("", "Local", "", null);
        assertEquals(true, parser.check);
        assertEquals(31, parser.level);
        
        parser.startElement("", "Names", "", null);
        assertEquals(true, parser.check);
        
        parser.startElement("", "pkg", "", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters() {
        SAXDirParser parser = new SAXDirParser();
        char[] charArray = {'t', 'e', 's', 't'};
        
        parser.check = true;
        parser.level = 0;
        parser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.localBrowser);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 10;
        parser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.file_editor);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 11;
        parser.characters(charArray, 0, 3);
        assertEquals(false, SAXDirParser.lineNumAccepted);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 12;
        parser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.lineNumParam);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 30;
        parser.characters(charArray, 0, 4);
        assertEquals("test", parser.location);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 31;
        parser.characters(charArray, 0, 4);
        assertEquals(true, parser.local);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.check = true;
        parser.level = 4;
        parser.characters(charArray, 0, 4);
        assertEquals(1, SAXDirParser.directory.size());
        assertEquals(1, SAXDirParser.localPackages.size());
        assertEquals("test", SAXDirParser.directory.get("test"));
        assertEquals("test", SAXDirParser.localPackages.get(0));
    }
    
    @Test
    public void testEndElement() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.endElement("", "Group", "");
        assertEquals(false, parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        SAXDirParser.directory.put("com.test", "test");
        SAXDirParser.directory.put("org.test", "test");
        SAXDirParser directory = new SAXDirParser();
        assertEquals(SAXDirParser.directory, directory.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages.add("com.test");
        SAXDirParser.localPackages.add("org.test");
        SAXDirParser localPackages = new SAXDirParser();
        assertEquals(SAXDirParser.localPackages, localPackages.getLocalPackages());
    }
    
    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "test";
        SAXDirParser localBrowser = new SAXDirParser();
        assertEquals(SAXDirParser.localBrowser, localBrowser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "test";
        SAXDirParser editor = new SAXDirParser();
        assertEquals(SAXDirParser.file_editor, editor.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser.lineNumAccepted = true;
        SAXDirParser acceptsLineNumber = new SAXDirParser();
        assertEquals(true, acceptsLineNumber.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "test";
        SAXDirParser lineNumberParam = new SAXDirParser();
        assertEquals(SAXDirParser.lineNumParam, lineNumberParam.lineNumberParameter());
    }
    
    @Test
    public void testMain() throws Exception {
        // No need to test main method as it does not return a value
    }
}