package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SAXDirParserTest {
    
    private SAXDirParser parser;
    
    @Before
    public void setUp() {
        parser = new SAXDirParser();
    }
    
    @Test
    public void testGetWebData() {
        assertEquals("", parser.getWebData());
    }
    
    @Test
    public void testSAXDirParser() throws Exception {
        assertNotNull(parser);
    }
    
    @Test
    public void testStartElement() throws SAXException {
        // Test when qname is "BrowserLoc"
        parser.startElement("", "", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
        
        // Test when qname is "Editor"
        parser.startElement("", "", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
        
        // Test when qname is "acceptsLineNumber"
        parser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
        
        // Test when qname is "parameter"
        parser.startElement("", "", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
        
        // Test when qname is "Group"
        parser.startElement("", "", "Group", null);
        assertEquals(2, parser.level);
        assertFalse(parser.check);
        
        // Test when qname is "Web"
        parser.startElement("", "", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
        
        // Test when qname is "Local"
        parser.startElement("", "", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
        
        // Test when qname is "Names"
        parser.startElement("", "", "Names", null);
        assertTrue(parser.check);
        
        // Test when qname is "pkg"
        parser.startElement("", "", "pkg", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters() {
        // Test when check is true and level is 0
        parser.check = true;
        parser.level = 0;
        parser.characters(new char[] {'a', 'b', 'c'}, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        // Test when check is true and level is 10
        parser.check = true;
        parser.level = 10;
        parser.characters(new char[] {'a', 'b', 'c'}, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        // Test when check is true and level is 11
        parser.check = true;
        parser.level = 11;
        parser.characters(new char[] {'Y', 'e', 's'}, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        // Test when check is true and level is 12
        parser.check = true;
        parser.level = 12;
        parser.characters(new char[] {'a', 'b', 'c'}, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        // Test when check is true and level is 30
        parser.check = true;
        parser.level = 30;
        parser.characters(new char[] {'a', 'b', 'c'}, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        // Test when check is true and level is 31
        parser.check = true;
        parser.level = 31;
        parser.characters(new char[] {'N', 'o'}, 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        
        // Test when check is true and level is 4
        parser.check = true;
        parser.level = 4;
        parser.local = true;
        parser.characters(new char[] {'a', 'b', 'c'}, 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("abc", SAXDirParser.directory.get("abc"));
        assertTrue(SAXDirParser.localPackages.contains("abc"));
    }
    
    @Test
    public void testEndElement() throws SAXException {
        // Test when qname is "Group"
        parser.endElement("", "", "Group");
        assertFalse(parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        assertNotNull(SAXDirParser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages() {
        // Test when localPackages is empty
        assertNull(SAXDirParser.getLocalPackages());
        
        // Test when localPackages is not empty
        SAXDirParser.localPackages.add("abc");
        assertNotNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalBrowser() {
        assertNotNull(SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        assertNotNull(SAXDirParser.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumber() {
        assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        assertNotNull(SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testMain() throws Exception {
        SAXDirParser.main(new String[0]);
    }
}