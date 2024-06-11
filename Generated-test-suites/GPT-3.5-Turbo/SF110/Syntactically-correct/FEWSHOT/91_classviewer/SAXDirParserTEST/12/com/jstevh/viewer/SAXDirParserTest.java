
package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

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
    public void testSAXDirParser() throws Exception {
        assertNotNull(new SAXDirParser());
    }
    
    @Test
    public void testStartElement() {
        // Test when qname is "BrowserLoc"
        saxDirParser.startElement("", "", "BrowserLoc", null);
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);
        
        // Test when qname is "Editor"
        saxDirParser.startElement("", "", "Editor", null);
        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);
        
        // Test when qname is "acceptsLineNumber"
        saxDirParser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);
        
        // Test when qname is "parameter"
        saxDirParser.startElement("", "", "parameter", null);
        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);
        
        // Test when qname is "Group"
        saxDirParser.startElement("", "", "Group", null);
        assertEquals(2, saxDirParser.level);
        assertFalse(saxDirParser.check);
        
        // Test when qname is "Web"
        saxDirParser.startElement("", "", "Web", null);
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);
        
        // Test when qname is "Local"
        saxDirParser.startElement("", "", "Local", null);
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);
        
        // Test when qname is "Names"
        saxDirParser.startElement("", "", "Names", null);
        assertTrue(saxDirParser.check);
        
        // Test when qname is "pkg"
        saxDirParser.startElement("", "", "pkg", null);
        assertEquals(4, saxDirParser.level);
    }
    
    @Test
    public void testCharacters() {
        // Test when check is true and level is 0
        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters("test".toCharArray(), 0, 4);
        assertEquals("test", SAXDirParser.localBrowser);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        
        // Test when check is true and level is 10
        saxDirParser.check = true;
        saxDirParser.level = 10;
        saxDirParser.characters("test".toCharArray(), 0, 4);
        assertEquals("test", SAXDirParser.file_editor);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        
        // Test when check is true and level is 11
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters("Yes".toCharArray(), 0, 3);
        assertTrue(SAXDirParser.lineNumAccepted);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        
        saxDirParser.check = true;
        saxDirParser.level = 11;
        saxDirParser.characters("No".toCharArray(), 0, 2);
        assertFalse(SAXDirParser.lineNumAccepted);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        
        // Test when check is true and level is 12
        saxDirParser.check = true;
        saxDirParser.level = 12;
        saxDirParser.characters("test".toCharArray(), 0, 4);
        assertEquals("test", SAXDirParser.lineNumParam);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        
        // Test when check is true and level is 30
        saxDirParser.check = true;
        saxDirParser.level = 30;
        saxDirParser.characters("test".toCharArray(), 0, 4);
        assertEquals("test", saxDirParser.location);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        
        // Test when check is true and level is 31
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters("No".toCharArray(), 0, 2);
        assertFalse(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        
        saxDirParser.check = true;
        saxDirParser.level = 31;
        saxDirParser.characters("Yes".toCharArray(), 0, 3);
        assertTrue(saxDirParser.local);
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        
        // Test when check is true and level is 4
        saxDirParser.check = true;
        saxDirParser.level = 4;
        SAXDirParser.directory = new HashMap<>();
        saxDirParser.location = "location";
        saxDirParser.local = true;
        saxDirParser.characters("pkg".toCharArray(), 0, 3);
        assertEquals("location", SAXDirParser.directory.get("pkg"));
        assertEquals("pkg", SAXDirParser.localPackages.get(0));
    }
    
    @Test
    public void testEndElement() {
        // Test when qname is "Group"
        saxDirParser.endElement("", "", "Group");
        assertFalse(saxDirParser.check);
    }
    
    @Test
    public void testGetDirectory() {
        SAXDirParser.directory = new HashMap<>();
        assertNull(SAXDirParser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages = new StringList();
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "test";
        assertEquals("test", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "test";
        assertEquals("test", SAXDirParser.getEditor());
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
        SAXDirParser.lineNumParam = "test";
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }
}
