
package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;

public class SAXDirParserTest {
    private SAXDirParser parser;

    @Before
    public void setUp() {
        parser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertNull(parser.getWebData());
    }

    @Test
    public void testSAXDirParser() throws Exception {
        assertNull(parser.getWebData());
    }

    @Test
    public void testStartElement() throws SAXException {
        Attributes attributes = new TestAttributes();
        parser.startElement("", "BrowserLoc", "BrowserLoc", attributes);
        assertTrue(parser.check);
        assertEquals(0, parser.level);

        parser.startElement("", "Editor", "Editor", attributes);
        assertTrue(parser.check);
        assertEquals(10, parser.level);

        parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", attributes);
        assertTrue(parser.check);
        assertEquals(11, parser.level);

        parser.startElement("", "parameter", "parameter", attributes);
        assertTrue(parser.check);
        assertEquals(12, parser.level);

        parser.startElement("", "Group", "Group", attributes);
        assertFalse(parser.check);
        assertEquals(2, parser.level);

        parser.startElement("", "Web", "Web", attributes);
        assertTrue(parser.check);
        assertEquals(30, parser.level);

        parser.startElement("", "Local", "Local", attributes);
        assertTrue(parser.check);
        assertEquals(31, parser.level);

        parser.startElement("", "Names", "Names", attributes);
        assertTrue(parser.check);

        parser.startElement("", "pkg", "pkg", attributes);
        assertEquals(4, parser.level);
    }

    @Test
    public void testCharacters() {
        char[] charArray = {'t', 'e', 's', 't'};
        parser.level = 0;
        parser.check = true;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("test", SAXDirParser.localBrowser);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.level = 10;
        parser.check = true;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("test", SAXDirParser.file_editor);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.level = 11;
        parser.check = true;
        parser.characters(charArray, 0, charArray.length);
        assertFalse(SAXDirParser.lineNumAccepted);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.level = 12;
        parser.check = true;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("test", SAXDirParser.lineNumParam);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.level = 30;
        parser.check = true;
        parser.characters(charArray, 0, charArray.length);
        assertEquals("test", parser.location);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.level = 31;
        parser.check = true;
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.local);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.level = 4;
        parser.check = true;
        parser.local = true;
        SAXDirParser.directory = new HashMap<>();
        SAXDirParser.localPackages = new StringList();
        parser.characters(charArray, 0, charArray.length);
        assertEquals("test", SAXDirParser.directory.get("test"));
        assertTrue(SAXDirParser.localPackages.contains("test"));
    }

    @Test
    public void testEndElement() throws SAXException {
        parser.check = true;
        parser.level = 2;
        parser.endElement("", "Group", "Group");
        assertFalse(parser.check);
    }

    @Test
    public void testGetDirectory() {
        HashMap<String, String> expected = new HashMap<>();
        SAXDirParser.directory = expected;
        assertEquals(expected, SAXDirParser.getDirectory());
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages = new StringList();
        assertNull(SAXDirParser.getLocalPackages());
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
        assertTrue(SAXDirParser.acceptsLineNumber());

        SAXDirParser.lineNumAccepted = false;
        assertFalse(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "test";
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }
}

class TestAttributes implements Attributes {
    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public String getURI(int index) {
        return null;
    }

    @Override
    public String getLocalName(int index) {
        return null;
    }

    @Override
    public String getQName(int index) {
        return null;
    }

    @Override
    public String getType(int index) {
        return null;
    }

    @Override
    public String getValue(int index) {
        return null;
    }

    @Override
    public int getIndex(String uri, String localName) {
        return 0;
    }

    @Override
    public int getIndex(String qName) {
        return 0;
    }

    @Override
    public String getType(String uri, String localName) {
        return null;
    }

    @Override
    public String getType(String qName) {
        return null;
    }

    @Override
    public String getValue(String uri, String localName) {
        return null;
    }

    @Override
    public String getValue(String qName) {
        return null;
    }
}
