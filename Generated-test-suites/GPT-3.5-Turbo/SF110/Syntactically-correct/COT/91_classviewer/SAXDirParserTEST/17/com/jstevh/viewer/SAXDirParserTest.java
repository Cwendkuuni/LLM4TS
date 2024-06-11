
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser saxDirParser = new SAXDirParser();
        assertNull(saxDirParser.getWebData());
    }

    @Test
    public void testStartElement() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        assertTrue(saxDirParser.level == 0);
        saxDirParser.startElement("", "", "BrowserLoc", null);
        assertTrue(saxDirParser.check);
        assertTrue(saxDirParser.level == 0);
    }

    @Test
    public void testCharacters() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();

        saxDirParser.startElement("", "", "BrowserLoc", null);
        saxDirParser.characters("Chrome".toCharArray(), 0, "Chrome".length());
        assertFalse(saxDirParser.check);
        assertEquals("Chrome", SAXDirParser.getLocalBrowser());

        saxDirParser.startElement("", "", "Editor", null);
        saxDirParser.characters("Notepad".toCharArray(), 0, "Notepad".length());
        assertFalse(saxDirParser.check);
        assertEquals("Notepad", SAXDirParser.getEditor());

        saxDirParser.startElement("", "", "acceptsLineNumber", null);
        saxDirParser.characters("Yes".toCharArray(), 0, "Yes".length());
        assertFalse(saxDirParser.check);
        assertTrue(SAXDirParser.acceptsLineNumber());

        saxDirParser.startElement("", "", "parameter", null);
        saxDirParser.characters("param".toCharArray(), 0, "param".length());
        assertFalse(saxDirParser.check);
        assertEquals("param", SAXDirParser.lineNumberParameter());

        saxDirParser.startElement("", "", "Web", null);
        saxDirParser.characters("Location".toCharArray(), 0, "Location".length());
        assertFalse(saxDirParser.check);
        assertEquals("Location", saxDirParser.location);

        saxDirParser.startElement("", "", "Local", null);
        saxDirParser.characters("Yes".toCharArray(), 0, "Yes".length());
        assertFalse(saxDirParser.check);
        assertTrue(saxDirParser.local);

        saxDirParser.startElement("", "", "pkg", null);
        saxDirParser.characters("java.lang.".toCharArray(), 0, "java.lang.".length());
        saxDirParser.level = 4;
        saxDirParser.check = true;
        saxDirParser.characters("Location".toCharArray(), 0, "Location".length());
        assertFalse(saxDirParser.check);
        assertNull(SAXDirParser.getLocalPackages());

    }

    @Test
    public void testEndElement() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.endElement("", "", "Group");
        assertFalse(saxDirParser.check);
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser saxDirParser = new SAXDirParser();
        assertNotNull(saxDirParser.getDirectory());
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser saxDirParser = new SAXDirParser();
        assertNull(saxDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser saxDirParser = new SAXDirParser();
        assertEquals("",saxDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        SAXDirParser saxDirParser = new SAXDirParser();
        assertEquals("",saxDirParser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser saxDirParser = new SAXDirParser();
        assertFalse(saxDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser saxDirParser = new SAXDirParser();
        assertEquals("", saxDirParser.lineNumberParameter());
    }

    @Test
    public void testMain() throws ParserConfigurationException, SAXException {
        // test for main method will be ignored as we cannot test it using JUnit
    }

}
