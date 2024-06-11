
package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class SAXDirParserTest {

    private SAXDirParser parser;

    @Before
    public void setup() {
        parser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        parser.webData = "test";
        assertEquals("test", parser.getWebData());
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser.directory.put("java.lang", "location1");
        SAXDirParser.directory.put("javax.swing.border", "location2");
        assertEquals(SAXDirParser.directory, SAXDirParser.getDirectory());
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages.add("package1");
        assertEquals(SAXDirParser.localPackages, SAXDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalPackages_empty() {
        assertNull(SAXDirParser.getLocalPackages());
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
        assertEquals(true, SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "param";
        assertEquals("param", SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testStartElement_BrowserLoc() {
        parser.startElement("", "", "BrowserLoc", null);
        assertEquals(true, parser.check);
        assertEquals(0, parser.level);
    }

    @Test
    public void testStartElement_Editor() {
        parser.startElement("", "", "Editor", null);
        assertEquals(true, parser.check);
        assertEquals(10, parser.level);
    }

    @Test
    public void testStartElement_acceptsLineNumber() {
        parser.startElement("", "", "acceptsLineNumber", null);
        assertEquals(true, parser.check);
        assertEquals(11, parser.level);
    }

    @Test
    public void testStartElement_parameter() {
        parser.startElement("", "", "parameter", null);
        assertEquals(true, parser.check);
        assertEquals(12, parser.level);
    }

    @Test
    public void testStartElement_Group() {
        parser.startElement("", "", "Group", null);
        assertEquals(false, parser.check);
        assertEquals(2, parser.level);
    }

    @Test
    public void testStartElement_Web() {
        parser.startElement("", "", "Web", null);
        assertEquals(true, parser.check);
        assertEquals(30, parser.level);
    }

    @Test
    public void testStartElement_Local() {
        parser.startElement("", "", "Local", null);
        assertEquals(true, parser.check);
        assertEquals(31, parser.level);
    }

    @Test
    public void testStartElement_Names() {
        parser.startElement("", "", "Names", null);
        assertEquals(true, parser.check);
        assertEquals(-1, parser.level);
    }

    @Test
    public void testStartElement_pkg() {
        parser.startElement("", "", "pkg", null);
        assertEquals(4, parser.level);
    }

    @Test
    public void testCharacters_level0() {
        parser.check = true;
        parser.level = 0;
        parser.characters("browser".toCharArray(), 0, 7);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        assertEquals("browser", SAXDirParser.localBrowser);
    }

    @Test
    public void testCharacters_level10() {
        parser.check = true;
        parser.level = 10;
        parser.characters("editor".toCharArray(), 0, 6);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        assertEquals("editor", SAXDirParser.file_editor);
    }

    @Test
    public void testCharacters_level11_true() {
        parser.check = true;
        parser.level = 11;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        assertEquals(true, SAXDirParser.lineNumAccepted);
    }

    @Test
    public void testCharacters_level11_false() {
        parser.check = true;
        parser.level = 11;
        parser.characters("No".toCharArray(), 0, 2);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        assertEquals(false, SAXDirParser.lineNumAccepted);
    }

    @Test
    public void testCharacters_level12() {
        parser.check = true;
        parser.level = 12;
        parser.characters("param".toCharArray(), 0, 5);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        assertEquals("param", SAXDirParser.lineNumParam);
    }

    @Test
    public void testCharacters_level30() {
        parser.check = true;
        parser.level = 30;
        parser.characters("location".toCharArray(), 0, 8);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        assertEquals("location", parser.location);
    }

    @Test
    public void testCharacters_level31_true() {
        parser.check = true;
        parser.level = 31;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        assertEquals(true, parser.local);
    }

    @Test
    public void testCharacters_level31_false() {
        parser.check = true;
        parser.level = 31;
        parser.characters("No".toCharArray(), 0, 2);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        assertEquals(false, parser.local);
    }

    @Test
    public void testCharacters_level4() {
        parser.level = 4;
        parser.local = true;
        parser.characters("package1".toCharArray(), 0, 8);
        assertEquals(false, parser.check);
        assertEquals(4, parser.level);
        assertEquals(true, parser.local);
        assertFalse(SAXDirParser.localPackages.isEmpty());
    }

    @Test
    public void testCharacters_level4_trimEmptyString() {
        parser.level = 4;
        parser.local = true;
        parser.characters("".toCharArray(), 0, 0);
        assertEquals(false, parser.check);
        assertEquals(4, parser.level);
        assertEquals(true, parser.local);
        assertEquals(0, SAXDirParser.directory.size());
    }

    @Test
    public void testEndElement_Group() {
        parser.check = true;
        parser.endElement("", "", "Group");
        assertEquals(false, parser.check);
    }

    @Test
    public void testMain() throws Exception {
        SAXDirParser.main(null);
    }
}
