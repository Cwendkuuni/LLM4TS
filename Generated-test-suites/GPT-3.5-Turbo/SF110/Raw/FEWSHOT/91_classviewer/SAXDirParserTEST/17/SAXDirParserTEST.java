package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.jstevh.tools.StringList;

public class SAXDirParserTest {

    private SAXDirParser parser;
    private char[] charArray;

    @Before
    public void setup() throws Exception {
        parser = new SAXDirParser();
        charArray = "test".toCharArray();
    }

    @Test
    public void testGetWebData() {
        assertNull(parser.getWebData());
    }

    @Test
    public void testStartElement() throws SAXException {
        Attributes atts = null;
        
        parser.startElement("", "", "BrowserLoc", atts);
        assertEquals(true, parser.check);
        assertEquals(0, parser.level);
        
        parser.startElement("", "", "Editor", atts);
        assertEquals(true, parser.check);
        assertEquals(10, parser.level);
        
        parser.startElement("", "", "acceptsLineNumber", atts);
        assertEquals(true, parser.check);
        assertEquals(11, parser.level);
        
        parser.startElement("", "", "parameter", atts);
        assertEquals(true, parser.check);
        assertEquals(12, parser.level);
        
        parser.startElement("", "", "Group", atts);
        assertEquals(false, parser.check);
        assertEquals(2, parser.level);
        
        parser.startElement("", "", "Web", atts);
        assertEquals(true, parser.check);
        assertEquals(30, parser.level);
        
        parser.startElement("", "", "Local", atts);
        assertEquals(true, parser.check);
        assertEquals(31, parser.level);
        
        parser.startElement("", "", "Names", atts);
        assertEquals(true, parser.check);
        
        parser.startElement("", "", "pkg", atts);
        assertEquals(4, parser.level);
    }

    @Test
    public void testCharacters() throws SAXException {
        parser.check = true;
        parser.level = 0;

        parser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.localBrowser);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);

        parser.level = 10;
        parser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.file_editor);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);

        parser.level = 11;
        parser.characters(charArray, 0, 4);
        assertEquals(false, SAXDirParser.lineNumAccepted);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);

        parser.level = 11;
        charArray = "Yes".toCharArray();
        parser.characters(charArray, 0, 3);
        assertEquals(true, SAXDirParser.lineNumAccepted);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);

        parser.level = 12;
        parser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.lineNumParam);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);

        parser.level = 30;
        parser.characters(charArray, 0, 4);
        assertEquals("test", parser.location);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 31;
        parser.characters(charArray, 0, 4);
        assertEquals(false, parser.local);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);
        
        parser.level = 31;
        charArray = "No".toCharArray();
        parser.characters(charArray, 0, 2);
        assertEquals(true, parser.local);
        assertEquals(false, parser.check);
        assertEquals(-1, parser.level);

        parser.level = 4;
        charArray = "test".toCharArray();
        parser.characters(charArray, 0, 4);
        assertEquals("test", SAXDirParser.directory.get("test"));
        assertEquals("test", parser.location);
        assertEquals(true, parser.local);     
    }
    
    @Test
    public void testEndElement() throws SAXException {
        parser.endElement("", "", "Group");
        assertEquals(false, parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        assertEquals(parser.directory.clone(), SAXDirParser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages() {
        assertNull(SAXDirParser.getLocalPackages());
        SAXDirParser.localPackages.add("test");
        assertEquals(SAXDirParser.localPackages.clone(), SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalBrowser() {
        assertEquals("", SAXDirParser.getLocalBrowser());
        SAXDirParser.localBrowser = "test";
        assertEquals("test", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        assertEquals("", SAXDirParser.getEditor());
        SAXDirParser.file_editor = "test";
        assertEquals("test", SAXDirParser.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumber() {
        assertEquals(false, SAXDirParser.acceptsLineNumber());
        SAXDirParser.lineNumAccepted = true;
        assertEquals(true, SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        assertEquals("", SAXDirParser.lineNumberParameter());
        SAXDirParser.lineNumParam = "test";
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }
}