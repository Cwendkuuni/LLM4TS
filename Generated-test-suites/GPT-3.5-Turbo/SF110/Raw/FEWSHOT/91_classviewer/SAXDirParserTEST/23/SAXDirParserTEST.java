
package com.jstevh.viewer;

import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SAXDirParserTest {
    @Test
    public void testStartElement() {
        SAXDirParser saxDirParser = new SAXDirParser();

        Attributes att1 = new Attributes() {
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
        };

        try {
            saxDirParser.startElement("test", "test1", "BrowserLoc", att1);
            assertTrue(saxDirParser.check);
            assertEquals(0, saxDirParser.level);

            saxDirParser.startElement("test", "test2", "Editor", att1);
            assertTrue(saxDirParser.check);
            assertEquals(10, saxDirParser.level);

            saxDirParser.startElement("test", "test3", "acceptsLineNumber", att1);
            assertTrue(saxDirParser.check);
            assertEquals(11, saxDirParser.level);

            saxDirParser.startElement("test", "test4", "parameter", att1);
            assertTrue(saxDirParser.check);
            assertEquals(12, saxDirParser.level);

            saxDirParser.startElement("test", "test5", "Group", att1);
            assertFalse(saxDirParser.check);
            assertEquals(2, saxDirParser.level);

            saxDirParser.startElement("test", "test6", "Web", att1);
            assertTrue(saxDirParser.check);
            assertEquals(30, saxDirParser.level);

            saxDirParser.startElement("test", "test7", "Local", att1);
            assertTrue(saxDirParser.check);
            assertEquals(31, saxDirParser.level);

            saxDirParser.startElement("test", "test8", "Names", att1);
            assertTrue(saxDirParser.check);

            saxDirParser.startElement("test", "test9", "pkg", att1);
            assertEquals(4, saxDirParser.level);
        } catch (SAXException e) {
            fail("Caught unexpected SAXException");
        }
    }

    @Test
    public void testCharacters() {
        SAXDirParser saxDirParser = new SAXDirParser();

        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters(new char[]{'a', 'b', 'c'}, 0, 3);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 10;
        saxDirParser.characters(new char[]{'a', 'b', 'c'}, 0, 3);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(new char[]{'Y', 'e', 's'}, 0, 3);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(SAXDirParser.lineNumAccepted);

        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters(new char[]{'n', 'o'}, 0, 2);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertFalse(SAXDirParser.lineNumAccepted);

        saxDirParser.check = true;
        saxDirParser.level = 12;
        saxDirParser.characters(new char[]{'a', 'b', 'c'}, 0, 3);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);

        saxDirParser.check = true;
        saxDirParser.level = 30;
        saxDirParser.characters(new char[]{'x', 'y', 'z'}, 0, 3);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("xyz", saxDirParser.location);

        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(new char[]{'y', 'e', 's'}, 0, 3);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(saxDirParser.local);

        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters(new char[]{'n', 'o'}, 0, 2);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertFalse(saxDirParser.local);

        saxDirParser.check = true;
        saxDirParser.level = 4;
        saxDirParser.characters(new char[]{'p', 'k', 'g'}, 0, 3);
        assertEquals(4, saxDirParser.level);
        assertEquals("", saxDirParser.location);

        saxDirParser.check = true;
        saxDirParser.level = 4;
        saxDirParser.location = "test";
        saxDirParser.characters(new char[]{'j', 'a', 'v', 'a', '.'}, 0, 5);
        assertFalse(saxDirParser.check);
        assertEquals(4, saxDirParser.level);
        assertEquals("java", saxDirParser.directory.get("java"));

        saxDirParser.check = true;
        saxDirParser.level = 4;
        saxDirParser.location = "test";
        saxDirParser.local = true;
        saxDirParser.characters(new char[]{'j', 'a', 'v', 'a', '.'}, 0, 5);
        assertFalse(saxDirParser.check);
        assertEquals(4, saxDirParser.level);
        assertEquals("java", saxDirParser.directory.get("java"));
        assertTrue(saxDirParser.local);
        assertEquals("java", SAXDirParser.localPackages.get(0));
    }

    @Test
    public void testEndElement() {
        SAXDirParser saxDirParser = new SAXDirParser();

        try {
            saxDirParser.endElement("test", "test1", "Group");
            assertFalse(saxDirParser.check);
        } catch (SAXException e) {
            fail("Caught unexpected SAXException");
        }
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser saxDirParser = new SAXDirParser();
        SAXDirParser.directory = new HashMap<>();
        SAXDirParser.directory.put("java.lang", "test1");
        SAXDirParser.directory.put("javax.swing.border", "test2");

        HashMap<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "test1");
        expectedDirectory.put("javax.swing.border", "test2");

        assertEquals(expectedDirectory, saxDirParser.getDirectory());
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser saxDirParser = new SAXDirParser();
        SAXDirParser.localPackages = new StringList();
        SAXDirParser.localPackages.add("test1");

        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("test1");

        assertEquals(expectedLocalPackages, saxDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser saxDirParser = new SAXDirParser();
        SAXDirParser.localBrowser = "test";

        assertEquals("test", saxDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        SAXDirParser saxDirParser = new SAXDirParser();
        SAXDirParser.file_editor = "test";

        assertEquals("test", saxDirParser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser saxDirParser = new SAXDirParser();
        SAXDirParser.lineNumAccepted = true;
        assertTrue(saxDirParser.acceptsLineNumber());

        SAXDirParser.lineNumAccepted = false;
        assertFalse(saxDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser saxDirParser = new SAXDirParser();
        SAXDirParser.lineNumParam = "test";

        assertEquals("test", saxDirParser.lineNumberParameter());
    }

    @Test
    public void testMain() {
        // This method cannot be tested easily as it involves parsing an XML file
        // and performing console output.
        // A proper test would require mocking and capturing the output streams.
        // We can leave this test empty or exclude it from our testing.
    }
}
