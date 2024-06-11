
package com.jstevh.viewer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SAXDirParserTest {

    private SAXDirParser saxDirParser;

    @Before
    public void setUp() {
        saxDirParser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertNull(saxDirParser.getWebData());
    }

    @Test
    public void testSAXDirParser() {
        assertNotNull(saxDirParser);
        assertEquals("", saxDirParser.getWebData());
    }

    @Test
    public void testStartElementBrowserLoc() {
        saxDirParser.startElement("", "", "BrowserLoc", null);
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);
    }

    @Test
    public void testStartElementEditor() {
        saxDirParser.startElement("", "", "Editor", null);
        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);
    }

    @Test
    public void testStartElementAcceptsLineNumber() {
        saxDirParser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);
    }

    @Test
    public void testStartElementParameter() {
        saxDirParser.startElement("", "", "parameter", null);
        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);
    }

    @Test
    public void testStartElementGroup() {
        saxDirParser.startElement("", "", "Group", null);
        assertFalse(saxDirParser.check);
        assertEquals(2, saxDirParser.level);
    }

    @Test
    public void testStartElementWeb() {
        saxDirParser.startElement("", "", "Web", null);
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);
    }

    @Test
    public void testStartElementLocal() {
        saxDirParser.startElement("", "", "Local", null);
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);
    }

    @Test
    public void testStartElementNames() {
        saxDirParser.startElement("", "", "Names", null);
        assertTrue(saxDirParser.check);
    }

    @Test
    public void testStartElementPkg() {
        saxDirParser.startElement("", "", "pkg", null);
        assertEquals(4, saxDirParser.level);
    }

    @Test
    public void testCharactersBrowserLoc() {
        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testCharactersEditor() {
        saxDirParser.check = true;
        saxDirParser.level = 10;
        saxDirParser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.getEditor());
    }

    @Test
    public void testCharactersAcceptsLineNumberYes() {
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(new char[]{'Y', 'e', 's'}, 0, 3);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testCharactersAcceptsLineNumberNo() {
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(new char[]{'N', 'o'}, 0, 2);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertFalse(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testCharactersParameter() {
        saxDirParser.check = true;
        saxDirParser.level = 12;
        saxDirParser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testCharactersWeb() {
        saxDirParser.level = 30;
        saxDirParser.check = true;
        saxDirParser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", saxDirParser.location);
    }

    @Test
    public void testCharactersLocalYes() {
        saxDirParser.level = 31;
        saxDirParser.check = true;
        saxDirParser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(saxDirParser.local);
    }

    @Test
    public void testCharactersLocalNo() {
        saxDirParser.level = 31;
        saxDirParser.check = true;
        saxDirParser.characters(new char[]{'N', 'o'}, 0, 2);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertFalse(saxDirParser.local);
    }

    @Test
    public void testCharactersPkg() {
        saxDirParser.level = 4;
        saxDirParser.local = true;
        saxDirParser.location = "test";
        saxDirParser.check = true;
        saxDirParser.characters(new char[]{'t', 'e', 's', 't'}, 0, 4);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.getDirectory().get("test"));
        assertNotNull(SAXDirParser.getLocalPackages().contains("test"));
    }

    @Test
    public void testEndElementGroup() {
        saxDirParser.check = true;
        saxDirParser.endElement("", "", "Group");
        assertFalse(saxDirParser.check);
    }

    @Test
    public void testGetLocalPackages() {
        assertNotNull(SAXDirParser.getLocalPackages());
        assertNull(SAXDirParser.getLocalPackages().clone());
        assertEquals(0, SAXDirParser.getLocalPackages().size());
    }

}
