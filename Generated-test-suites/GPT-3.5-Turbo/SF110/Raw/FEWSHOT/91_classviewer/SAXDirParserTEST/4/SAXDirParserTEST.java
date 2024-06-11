
package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SAXDirParserTest {
    
    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertNull(parser.getWebData());
    }
    
    @Test
    public void testSAXDirParserConstructor() {
        try {
            new SAXDirParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testStartElement() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "BrowserLoc", "", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
        parser.startElement("", "Editor", "", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
        parser.startElement("", "acceptsLineNumber", "", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
        parser.startElement("", "parameter", "", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
        parser.startElement("", "Group", "", null);
        assertFalse(parser.check);
        assertEquals(2, parser.level);
        parser.startElement("", "Web", "", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
        parser.startElement("", "Local", "", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
        parser.startElement("", "Names", "", null);
        assertTrue(parser.check);
        parser.startElement("", "pkg", "", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters() {
        SAXDirParser parser = new SAXDirParser();
        char[] charArray = {'t', 'e', 'm', 'p'};
        parser.check = true;
        parser.level = 0;
        parser.characters(charArray, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 10;
        parser.characters(charArray, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 11;
        parser.characters(charArray, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 12;
        parser.characters(charArray, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 30;
        parser.characters(charArray, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 31;
        parser.characters(charArray, 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 4;
        parser.characters(charArray, 0, 4);
        assertEquals("temp", SAXDirParser.directory.get("temp"));
        assertEquals("temp", SAXDirParser.localPackages.get(0));
    }
    
    @Test
    public void testEndElement() {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.endElement("", "Group", "");
        assertFalse(parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals(SAXDirParser.directory, parser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages() {
        SAXDirParser parser = new SAXDirParser();
        assertNull(parser.getLocalPackages());
        SAXDirParser.localPackages.add("temp");
        assertEquals(SAXDirParser.localPackages, parser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalBrowser() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals(SAXDirParser.localBrowser, parser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals(SAXDirParser.file_editor, parser.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        assertFalse(parser.acceptsLineNumber());
        SAXDirParser.lineNumAccepted = true;
        assertTrue(parser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals(SAXDirParser.lineNumParam, parser.lineNumberParameter());
    }
    
    @Test
    public void testMain() {
        try {
            SAXDirParser.main(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
