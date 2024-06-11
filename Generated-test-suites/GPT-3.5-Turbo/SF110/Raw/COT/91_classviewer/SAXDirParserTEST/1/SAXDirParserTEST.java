import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SAXDirParserTest {
    
    private SAXDirParser saxDirParser;

    @Before
    public void setUp() throws Exception {
        saxDirParser = new SAXDirParser();
    }

    @After
    public void tearDown() throws Exception {
        saxDirParser = null;
    }
    
    @Test
    public void testGetWebData() {
        assertNull(saxDirParser.getWebData());
    }

    @Test
    public void testSAXDirParser() {
        assertNotNull(saxDirParser);
    }

    @Test
    public void testStartElement() throws Exception {
        saxDirParser.startElement("", "", "BrowserLoc", null);
        assertEquals(true, saxDirParser.check);
        assertEquals(0, saxDirParser.level);
        
        saxDirParser.startElement("", "", "Editor", null);
        assertEquals(true, saxDirParser.check);
        assertEquals(10, saxDirParser.level);

        saxDirParser.startElement("", "", "acceptsLineNumber", null);
        assertEquals(true, saxDirParser.check);
        assertEquals(11, saxDirParser.level);

        saxDirParser.startElement("", "", "parameter", null);
        assertEquals(true, saxDirParser.check);
        assertEquals(12, saxDirParser.level);

        saxDirParser.startElement("", "", "Group", null);
        assertEquals(false, saxDirParser.check);
        assertEquals(2, saxDirParser.level);

        saxDirParser.startElement("", "", "Web", null);
        assertEquals(true, saxDirParser.check);
        assertEquals(30, saxDirParser.level);

        saxDirParser.startElement("", "", "Local", null);
        assertEquals(true, saxDirParser.check);
        assertEquals(31, saxDirParser.level);

        saxDirParser.startElement("", "", "Names", null);
        assertEquals(true, saxDirParser.check);

        saxDirParser.startElement("", "", "pkg", null);
        assertEquals(4, saxDirParser.level);
    }

    @Test
    public void testCharacters() {
        char[] charArray = {'t', 'e', 's', 't'};
        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.localBrowser);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        
        saxDirParser.check = true;
        saxDirParser.level = 10;
        saxDirParser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.file_editor);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(charArray, 0, 4);
        assertEquals(false, SAXDirParser.lineNumAccepted);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters("Yes ".toCharArray(), 0, 3);
        assertEquals(true, SAXDirParser.lineNumAccepted);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 12;
        saxDirParser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.lineNumParam);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 30;
        saxDirParser.characters(charArray, 0, 4);
        assertEquals("test", saxDirParser.location);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters("No ".toCharArray(), 0, 2);
        assertEquals(false, saxDirParser.local);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters("Yes ".toCharArray(), 0, 3);
        assertEquals(true, saxDirParser.local);
        assertEquals(false, saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 4;
        saxDirParser.location = "location";
        saxDirParser.characters("pkg".toCharArray(), 0, 3);
        assertEquals("location", SAXDirParser.directory.get("pkg"));
        assertEquals(true, SAXDirParser.localPackages.contains("pkg"));
    }

    @Test
    public void testEndElement() throws Exception {
        saxDirParser.endElement("", "", "Group");
        assertEquals(false, saxDirParser.check);
    }

    @Test
    public void testGetDirectory() {
        HashMap<String, String> directory = SAXDirParser.getDirectory();
        assertNotNull(directory);
    }

    @Test
    public void testGetLocalPackages() {
        StringList localPackages = SAXDirParser.getLocalPackages();
        assertNull(localPackages);

        SAXDirParser.localPackages.add("pkg");
        localPackages = SAXDirParser.getLocalPackages();
        assertNotNull(localPackages);
        assertEquals("pkg", localPackages.get(0));
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "test";
        String localBrowser = SAXDirParser.getLocalBrowser();
        assertEquals("test", localBrowser);
    }

    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "test";
        String file_editor = SAXDirParser.getEditor();
        assertEquals("test", file_editor);
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser.lineNumAccepted = true;
        boolean lineNumAccepted = SAXDirParser.acceptsLineNumber();
        assertEquals(true, lineNumAccepted);
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "test";
        String lineNumParam = SAXDirParser.lineNumberParameter();
        assertEquals("test", lineNumParam);
    }

    @Test
    public void testMain() throws Exception {
        SAXDirParser.main(null);
    }
}