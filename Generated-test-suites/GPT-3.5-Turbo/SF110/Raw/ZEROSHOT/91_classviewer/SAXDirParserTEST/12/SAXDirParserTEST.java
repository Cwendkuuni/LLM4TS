
package com.jstevh.viewer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SAXDirParserTest {

    SAXDirParser saxDirParser;

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
    public void testStartElement() throws Exception {
        saxDirParser.startElement("", "BrowserLoc", "", null);
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);

        saxDirParser.startElement("", "Editor", "", null);
        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);

        saxDirParser.startElement("", "acceptsLineNumber", "", null);
        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);

        saxDirParser.startElement("", "parameter", "", null);
        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);

        saxDirParser.startElement("", "Group", "", null);
        assertEquals(2, saxDirParser.level);
        assertFalse(saxDirParser.check);

        saxDirParser.startElement("", "Web", "", null);
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);

        saxDirParser.startElement("", "Local", "", null);
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);

        saxDirParser.startElement("", "Names", "", null);
        assertTrue(saxDirParser.check);

        saxDirParser.startElement("", "pkg", "", null);
        assertEquals(4, saxDirParser.level);
    }

    @Test
    public void testCharacters() {
        saxDirParser.check = true;

        saxDirParser.level = 0;
        saxDirParser.characters("TestBrowser".toCharArray(), 0, "TestBrowser".length());
        assertEquals("TestBrowser", SAXDirParser.localBrowser);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.level = 10;
        saxDirParser.characters("TestEditor".toCharArray(), 0, "TestEditor".length());
        assertEquals("TestEditor", SAXDirParser.file_editor);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.level = 11;
        saxDirParser.characters("Yes".toCharArray(), 0, "Yes".length());
        assertTrue(SAXDirParser.lineNumAccepted);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.level = 12;
        saxDirParser.characters("TestParameter".toCharArray(), 0, "TestParameter".length());
        assertEquals("TestParameter", SAXDirParser.lineNumParam);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.level = 30;
        saxDirParser.characters("TestLocation".toCharArray(), 0, "TestLocation".length());
        assertEquals("TestLocation", saxDirParser.location);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.level = 31;
        saxDirParser.characters("No".toCharArray(), 0, "No".length());
        assertFalse(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.level = 4;
        saxDirParser.characters("Test".toCharArray(), 0, "Test".length());
        assertEquals("Test", SAXDirParser.directory.get("TestLocation"));
        assertEquals("Test", SAXDirParser.localPackages.get(0));
    }

    @Test
    public void testEndElement() throws Exception {
        saxDirParser.endElement("", "Group", "");
        assertFalse(saxDirParser.check);
    }

    @Test
    public void testGetDirectory() {
        assertNotNull(SAXDirParser.getDirectory());
    }

    @Test
    public void testGetLocalPackages() {
        assertNull(SAXDirParser.getLocalPackages());

        SAXDirParser.localPackages.add("TestPackage");
        assertNotNull(SAXDirParser.getLocalPackages());
        assertEquals("TestPackage", SAXDirParser.getLocalPackages().get(0));
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "TestBrowser";
        assertEquals("TestBrowser", SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "TestEditor";
        assertEquals("TestEditor", SAXDirParser.getEditor());
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
        SAXDirParser.lineNumParam = "TestParameter";
        assertEquals("TestParameter", SAXDirParser.lineNumberParameter());
    }

}
