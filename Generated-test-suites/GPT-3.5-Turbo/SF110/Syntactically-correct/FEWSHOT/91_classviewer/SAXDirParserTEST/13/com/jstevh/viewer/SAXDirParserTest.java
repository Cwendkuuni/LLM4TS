package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SAXDirParserTest {

    private SAXDirParser parser;

    @Before
    public void setup() throws Exception {
        parser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        parser.webData = "test";
        assertEquals("test", parser.getWebData());
    }

    @Test
    public void testGetDirectory() {
        HashMap<String, String> directory = SAXDirParser.getDirectory();
        assertNull(directory.get("java.lang"));
        assertNull(directory.get("javax.swing.border"));
    }

    @Test
    public void testGetLocalPackages_Empty() {
        assertNull(SAXDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages.add("package1");
        SAXDirParser.localPackages.add("package2");
        assertEquals(SAXDirParser.localPackages.clone(), SAXDirParser.getLocalPackages());
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
    public void testAcceptsLineNumber_True() {
        SAXDirParser.lineNumAccepted = true;
        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testAcceptsLineNumber_False() {
        SAXDirParser.lineNumAccepted = false;
        assertFalse(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "parameter";
        assertEquals("parameter", SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testStartElement_BrowserLoc() throws SAXException {
        parser.startElement(null, null, "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
    }

    @Test
    public void testStartElement_Editor() throws SAXException {
        parser.startElement(null, null, "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
    }

    @Test
    public void testStartElement_AcceptsLineNumber() throws SAXException {
        parser.startElement(null, null, "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
    }

    @Test
    public void testStartElement_Parameter() throws SAXException {
        parser.startElement(null, null, "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
    }

    @Test
    public void testStartElement_Group() throws SAXException {
        parser.startElement(null, null, "Group", null);
        assertEquals(2, parser.level);
        assertFalse(parser.check);
    }

    @Test
    public void testStartElement_Web() throws SAXException {
        parser.startElement(null, null, "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
    }

    @Test
    public void testStartElement_Local() throws SAXException {
        parser.startElement(null, null, "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
    }

    @Test
    public void testStartElement_Names() throws SAXException {
        parser.startElement(null, null, "Names", null);
        assertTrue(parser.check);
    }

    @Test
    public void testStartElement_Pkg() throws SAXException {
        parser.startElement(null, null, "pkg", null);
        assertEquals(4, parser.level);
    }

    @Test
    public void testCharacters_BrowserLoc() {
        parser.check = true;
        parser.level = 0;
        char[] charArray = "browser".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("browser", SAXDirParser.localBrowser);
    }

    @Test
    public void testCharacters_Editor() {
        parser.check = true;
        parser.level = 10;
        char[] charArray = "editor".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("editor", SAXDirParser.file_editor);
    }

    @Test
    public void testCharacters_AcceptsLineNumber_True() {
        parser.check = true;
        parser.level = 11;
        char[] charArray = "Yes".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.lineNumAccepted);
    }

    @Test
    public void testCharacters_AcceptsLineNumber_False() {
        parser.check = true;
        parser.level = 11;
        char[] charArray = "No".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.lineNumAccepted);
    }

    @Test
    public void testCharacters_Parameter() {
        parser.check = true;
        parser.level = 12;
        char[] charArray = "parameter".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("parameter", SAXDirParser.lineNumParam);
    }

    @Test
    public void testCharacters_Web() {
        parser.check = true;
        parser.level = 30;
        char[] charArray = "location".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("location", parser.location);
    }

    @Test
    public void testCharacters_Local_True() {
        parser.check = true;
        parser.level = 31;
        char[] charArray = "Yes".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
    }

    @Test
    public void testCharacters_Local_False() {
        parser.check = true;
        parser.level = 31;
        char[] charArray = "No".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
    }

    @Test
    public void testCharacters_Pkg() {
        parser.check = true;
        parser.level = 4;
        parser.location = "location";
        char[] charArray = "package1".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("location", SAXDirParser.directory.get("package1"));
        assertTrue(SAXDirParser.localPackages.contains("package1"));
    }

    @Test
    public void testEndElement_Group() throws SAXException {
        parser.endElement(null, null, "Group");
        assertFalse(parser.check);
    }
}