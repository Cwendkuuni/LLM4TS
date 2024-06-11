package com.jstevh.viewer;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SAXDirParserTest {

    private SAXDirParser saxDirParser;
    private HashMap<String, String> directory;

    @Before
    public void setUp() throws Exception {
        saxDirParser = new SAXDirParser();
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        myParser.getXMLReader().setContentHandler(saxDirParser);
        myParser.getXMLReader().parse("packagedirectory.xml");
        directory = (HashMap<String, String>) SAXDirParser.getDirectory().clone();
    }

    @Test
    public void testStartElement() throws SAXException {
        saxDirParser.startElement("namespace", "local", "BrowserLoc", null);
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);

        saxDirParser.startElement("namespace", "local", "Editor", null);
        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);

        saxDirParser.startElement("namespace", "local", "acceptsLineNumber", null);
        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);

        saxDirParser.startElement("namespace", "local", "parameter", null);
        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);

        saxDirParser.startElement("namespace", "local", "Group", null);
        assertFalse(saxDirParser.check);
        assertEquals(2, saxDirParser.level);

        saxDirParser.startElement("namespace", "local", "Web", null);
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);

        saxDirParser.startElement("namespace", "local", "Local", null);
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);

        saxDirParser.startElement("namespace", "local", "Names", null);
        assertTrue(saxDirParser.check);

        saxDirParser.startElement("namespace", "local", "pkg", null);
        assertEquals(4, saxDirParser.level);
    }

    @Test
    public void testCharacters() {
        char[] charArray = "123".toCharArray();
        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters(charArray, 0, 3);
        assertEquals("123", SAXDirParser.getLocalBrowser());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 10;
        saxDirParser.characters(charArray, 0, 3);
        assertEquals("123", SAXDirParser.getEditor());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(charArray, 0, 3);
        assertFalse(SAXDirParser.acceptsLineNumber());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 12;
        saxDirParser.characters(charArray, 0, 3);
        assertEquals("123", SAXDirParser.lineNumberParameter());
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 30;
        saxDirParser.characters(charArray, 0, 3);
        assertEquals("123", saxDirParser.location);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(charArray, 0, 3);
        assertTrue(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.level = 4;
        saxDirParser.characters(charArray, 0, 3);
        assertEquals("123", SAXDirParser.getDirectory().get("123"));
        assertTrue(SAXDirParser.getLocalPackages().contains("123"));
    }

    @Test
    public void testEndElement() throws SAXException {
        saxDirParser.endElement("namespace", "local", "Group");
        assertFalse(saxDirParser.check);
    }

    @Test
    public void testGetDirectory() {
        HashMap<String, String> clonedDirectory = SAXDirParser.getDirectory();
        assertEquals(clonedDirectory, directory);
        assertNotSame(clonedDirectory, directory);
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
        // Test the output of the main method to verify if the program runs correctly
        // Since the main method prints to console, it can't be tested directly
    }
}