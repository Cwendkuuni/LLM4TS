package com.jstevh.viewer;

import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

import static org.junit.Assert.*;

public class SAXDirParserTest {

    @Test
    public void testStartElement() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "BrowserLoc", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);

        parser.startElement("", "Editor", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);

        parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);

        parser.startElement("", "parameter", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);

        parser.startElement("", "Group", "Group", null);
        assertFalse(parser.check);
        assertEquals(2, parser.level);

        parser.startElement("", "Web", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);

        parser.startElement("", "Local", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);

        parser.startElement("", "Names", "Names", null);
        assertTrue(parser.check);

        parser.startElement("", "pkg", "pkg", null);
        assertEquals(4, parser.level);
    }

    @Test
    public void testCharacters() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.level = 0;
        parser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertEquals("test", SAXDirParser.localBrowser);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 10;
        parser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertEquals("test", SAXDirParser.file_editor);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 11;
        parser.characters(new char[]{'Y', 'e', 's'}, 0, 3);
        assertTrue(SAXDirParser.lineNumAccepted);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 11;
        parser.characters(new char[]{'N', 'o'}, 0, 2);
        assertFalse(SAXDirParser.lineNumAccepted);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 12;
        parser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertEquals("test", SAXDirParser.lineNumParam);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 30;
        parser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertEquals("test", parser.location);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);

        parser.check = true;
        parser.level = 31;
        parser.characters(new char[]{'N', 'o'}, 0, 2);
        assertFalse(parser.local);
    }

    @Test
    public void testEndElement() throws SAXException {
        SAXDirParser parser = new SAXDirParser();
        parser.endElement("", "Group", "Group");
        assertFalse(parser.check);
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser.directory.put("java.lang", "location");
        assertEquals("location", SAXDirParser.getDirectory().get("java.lang"));
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages.add("package1");
        assertNotNull(SAXDirParser.getLocalPackages());
        assertEquals("package1", SAXDirParser.getLocalPackages().get(0));
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "browser";
        assertEquals("browser", SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "editor";
        assertEquals("editor", SAXDirParser.getEditor());
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
        SAXDirParser.lineNumParam = "param";
        assertEquals("param", SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testMain() throws Exception {
        SAXDirParser.main(new String[0]);
    }
}