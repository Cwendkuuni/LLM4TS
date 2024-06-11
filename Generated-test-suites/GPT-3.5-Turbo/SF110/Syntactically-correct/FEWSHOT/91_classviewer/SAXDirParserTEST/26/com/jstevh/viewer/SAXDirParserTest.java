
package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class SAXDirParserTest {

    private SAXDirParser parser;

    @Before
    public void setup() throws Exception {
        parser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertNull(parser.getWebData());
    }

    @Test
    public void testSAXDirParser() {
        assertNull(parser.getWebData());
    }

    // startElement method tests
    @Test
    public void testStartElement_BrowserLoc() {
        parser.startElement("", "", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
    }
    
    @Test
    public void testStartElement_Editor() {
        parser.startElement("", "", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
    }

    @Test
    public void testStartElement_acceptsLineNumber() {
        parser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
    }

    @Test
    public void testStartElement_parameter() {
        parser.startElement("", "", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
    }

    @Test
    public void testStartElement_Group() {
        parser.startElement("", "", "Group", null);
        assertEquals(2, parser.level);
        assertFalse(parser.check);
    }

    @Test
    public void testStartElement_Web() {
        parser.startElement("", "", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
    }

    @Test
    public void testStartElement_Local() {
        parser.startElement("", "", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
    }

    @Test
    public void testStartElement_Names() {
        parser.startElement("", "", "Names", null);
        assertTrue(parser.check);
    }

    @Test
    public void testStartElement_pkg() {
        parser.level = 4;
        parser.startElement("", "", "pkg", null);
        assertEquals(4, parser.level);
    }

    // characters method tests
    @Test
    public void testCharacters_BrowserLoc() {
        parser.check = true;
        parser.level = 0;

        parser.characters("test".toCharArray(), 0, 4);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testCharacters_Editor() {
        parser.check = true;
        parser.level = 10;

        parser.characters("test".toCharArray(), 0, 4);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.getEditor());
    }

    @Test
    public void testCharacters_acceptsLineNumber() {
        parser.check = true;
        parser.level = 11;

        parser.characters("Yes".toCharArray(), 0, 3);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testCharacters_parameter() {
        parser.check = true;
        parser.level = 12;

        parser.characters("test".toCharArray(), 0, 4);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testCharacters_Web() {
        parser.location = null;
        parser.check = true;
        parser.level = 30;

        parser.characters("test".toCharArray(), 0, 4);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", parser.location);
    }

    @Test
    public void testCharacters_Local() {
        parser.check = true;
        parser.level = 31;

        parser.characters("Yes".toCharArray(), 0, 3);

        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
    }

    @Test
    public void testCharacters_pkg() {
        parser.level = 4;

        parser.characters("test".toCharArray(), 0, 4);

        assertEquals(4, parser.level);
        assertEquals("test", SAXDirParser.getDirectory().get("test"));
        assertNull(SAXDirParser.getLocalPackages());
    }

    // endElement method tests
    @Test
    public void testEndElement_Group() {
        parser.check = true;
        parser.endElement("", "", "Group");
        assertFalse(parser.check);
    }
    
    // getDirectory method tests
    @Test
    public void testGetDirectory() {
        HashMap<String, String> directory = SAXDirParser.getDirectory();
        assertEquals(0, directory.size());
    }

    // getLocalPackages method tests
    @Test
    public void testGetLocalPackages_Empty() {
        assertNull(SAXDirParser.getLocalPackages());
    }

    // getLocalBrowser method tests
    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "test";
        assertEquals("test", SAXDirParser.getLocalBrowser());
    }

    // getEditor method tests
    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "test";
        assertEquals("test", SAXDirParser.getEditor());
    }

    // acceptsLineNumber method tests
    @Test
    public void testAcceptsLineNumber_False() {
        SAXDirParser.lineNumAccepted = false;
        assertFalse(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testAcceptsLineNumber_True() {
        SAXDirParser.lineNumAccepted = true;
        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    // lineNumberParameter method tests
    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "test";
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }

    // main method test
    @Test
    public void testMain() throws Exception {
        SAXDirParser.directory.clear();
        SAXDirParser.localBrowser = "";
        
        SAXDirParser.main(null);
        
        assertEquals("test", SAXDirParser.directory.get("java.lang"));
        assertEquals("test", SAXDirParser.directory.get("javax.swing.border"));
        assertEquals("test", SAXDirParser.localBrowser);
    }

}
