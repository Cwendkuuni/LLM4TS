import org.junit.*;
import static org.junit.Assert.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.webData = "test data";
        assertEquals("test data", parser.getWebData());
    }
    
    @Test
    public void testStartElement() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("namespace", "local", "qname", null);
        // add assertions here
    }
    
    @Test
    public void testCharacters() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 0;
        char[] charArray = {'t', 'e', 's', 't'};
        parser.characters(charArray, 0, charArray.length);
        // add assertions here
    }
    
    @Test
    public void testEndElement() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.endElement("namespace", "local", "qname");
        // add assertions here
    }
    
    @Test
    public void testGetDirectory() {
        // add assertions here
    }
    
    @Test
    public void testGetLocalPackages() {
        // add assertions here
    }
    
    @Test
    public void testGetLocalBrowser() {
        // add assertions here
    }
    
    @Test
    public void testGetEditor() {
        // add assertions here
    }
    
    @Test
    public void testAcceptsLineNumber() {
        // add assertions here
    }
    
    @Test
    public void testLineNumberParameter() {
        // add assertions here
    }
}