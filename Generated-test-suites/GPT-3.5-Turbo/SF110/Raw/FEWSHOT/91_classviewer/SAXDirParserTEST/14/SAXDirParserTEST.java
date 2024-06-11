package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class SAXDirParserTest {

    private SAXDirParser parser;

    @Before
    public void setup() throws Exception {
        parser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        parser.webData = "test data";
        assertEquals("test data", parser.getWebData());
    }

    @Test
    public void testSAXDirParser() throws Exception {
        parser = new SAXDirParser();
        assertNull(parser.webData);
    }

    @Test
    public void testStartElement() {
        // TODO: Write test cases to test the startElement() method
    }

    @Test
    public void testCharacters() {
        // TODO: Write test cases to test the characters() method
    }

    @Test
    public void testEndElement() {
        // TODO: Write test cases to test the endElement() method
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser.directory = new HashMap<>();
        SAXDirParser.directory.put("java.lang", "path1");
        SAXDirParser.directory.put("javax.swing.border", "path2");
        assertEquals(SAXDirParser.directory, SAXDirParser.getDirectory());
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages = new StringList();
        SAXDirParser.localPackages.add("package1");
        SAXDirParser.localPackages.add("package2");
        assertEquals(SAXDirParser.localPackages, SAXDirParser.getLocalPackages());
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

    // Main method test is excluded as it requires external file and prints to console
}