package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.util.HashMap;

public class SAXDirParserTest {

    private SAXDirParser saxDirParser;

    @Before
    public void setUp() throws Exception {
        saxDirParser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertNotNull(saxDirParser.getWebData());
    }

    @Test
    public void testStartElement() throws SAXException {
        Attributes attributes = null;

        saxDirParser.startElement("", "BrowserLoc", "BrowserLoc", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);

        saxDirParser.startElement("", "Editor", "Editor", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);

        saxDirParser.startElement("", "acceptsLineNumber", "acceptsLineNumber", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);

        saxDirParser.startElement("", "parameter", "parameter", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);

        saxDirParser.startElement("", "Group", "Group", attributes);
        assertEquals(2, saxDirParser.level);
        assertFalse(saxDirParser.check);

        saxDirParser.startElement("", "Web", "Web", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);

        saxDirParser.startElement("", "Local", "Local", attributes);
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);

        saxDirParser.startElement("", "Names", "Names", attributes);
        assertTrue(saxDirParser.check);

        saxDirParser.startElement("", "pkg", "pkg", attributes);
        assertEquals(4, saxDirParser.level);
    }

    @Test
    public void testCharacters() {
        char[] charArray = {};
        int start = 0;
        int length = 0;

        // Test BrowserLoc
        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters(charArray, start, length);
        assertEquals("", SAXDirParser.localBrowser);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Test Editor
        saxDirParser.check = true;
        saxDirParser.level = 10;
        saxDirParser.characters(charArray, start, length);
        assertEquals("", SAXDirParser.file_editor);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Test acceptsLineNumber
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters("Yes".toCharArray(), start, length);
        assertTrue(SAXDirParser.lineNumAccepted);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Test parameter
        saxDirParser.check = true;
        saxDirParser.level = 12;
        saxDirParser.characters(charArray, start, length);
        assertEquals("", SAXDirParser.lineNumParam);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Test Web
        saxDirParser.level = 30;
        saxDirParser.location = "";
        saxDirParser.check = true;
        saxDirParser.characters("test".toCharArray(), start, length);
        assertEquals("test", saxDirParser.location);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Test Local
        saxDirParser.level = 31;
        saxDirParser.check = true;
        saxDirParser.characters("No".toCharArray(), start, length);
        assertFalse(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        // Test pkg
        saxDirParser.level = 4;
        saxDirParser.check = true;
        saxDirParser.local = false;
        saxDirParser.location = "";
        saxDirParser.directory = new HashMap<String, String>();
        saxDirParser.localPackages = new StringList();
        saxDirParser.characters("test.package.".toCharArray(), start, length);
        assertEquals(0, saxDirParser.directory.size());
        assertEquals(0, saxDirParser.localPackages.size());

        saxDirParser.level = 4;
        saxDirParser.check = true;
        saxDirParser.local = true;
        saxDirParser.location = "";
        saxDirParser.directory = new HashMap<String, String>();
        saxDirParser.localPackages = new StringList();
        saxDirParser.characters("test.package.".toCharArray(), start, length);
        assertEquals(1, saxDirParser.directory.size());
        assertEquals("test.package", saxDirParser.directory.get("test.package"));
        assertNotNull(saxDirParser.localPackages);
        assertEquals(1, saxDirParser.localPackages.size());
        assertEquals("test.package", saxDirParser.localPackages.get(0));
    }

    @Test
    public void testEndElement() throws SAXException {
        saxDirParser.endElement("", "Group", "Group");
        assertFalse(saxDirParser.check);
    }

    @Test
    public void testGetDirectory() {
        assertNotNull(SAXDirParser.getDirectory());
    }

    @Test
    public void testGetLocalPackages() {
        assertNull(SAXDirParser.getLocalPackages());
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

    @Test
    public void testMain() throws Exception {
        // TODO: Implement test for main method
    }
}