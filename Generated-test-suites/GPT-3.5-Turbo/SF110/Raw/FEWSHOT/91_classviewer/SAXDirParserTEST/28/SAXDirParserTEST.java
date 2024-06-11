package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SAXDirParserTest {
    @Test
    public void testStartElementBrowserLoc() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        
        saxDirParser.startElement("", "BrowserLoc", "", null);
        
        assertTrue(saxDirParser.check);
        assertEquals(0, saxDirParser.level);
    }
    
    @Test
    public void testStartElementEditor() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        
        saxDirParser.startElement("", "Editor", "", null);
        
        assertTrue(saxDirParser.check);
        assertEquals(10, saxDirParser.level);
    }
    
    @Test
    public void testStartElementAcceptsLineNumber() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        
        saxDirParser.startElement("", "acceptsLineNumber", "", null);
        
        assertTrue(saxDirParser.check);
        assertEquals(11, saxDirParser.level);
    }
    
    @Test
    public void testStartElementParameter() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        
        saxDirParser.startElement("", "parameter", "", null);
        
        assertTrue(saxDirParser.check);
        assertEquals(12, saxDirParser.level);
    }
    
    @Test
    public void testStartElementGroup() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        
        saxDirParser.startElement("", "Group", "", null);
        
        assertFalse(saxDirParser.check);
        assertEquals(2, saxDirParser.level);
    }
    
    @Test
    public void testStartElementWeb() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        
        saxDirParser.startElement("", "Web", "", null);
        
        assertTrue(saxDirParser.check);
        assertEquals(30, saxDirParser.level);
    }
    
    @Test
    public void testStartElementLocal() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        
        saxDirParser.startElement("", "Local", "", null);
        
        assertTrue(saxDirParser.check);
        assertEquals(31, saxDirParser.level);
    }
    
    @Test
    public void testStartElementNames() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        
        saxDirParser.startElement("", "Names", "", null);
        
        assertTrue(saxDirParser.check);
    }
    
    @Test
    public void testStartElementPkg() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        
        saxDirParser.startElement("", "pkg", "", null);
        
        assertEquals(4, saxDirParser.level);
    }
    
    @Test
    public void testCharactersWithCheckAndLevel0() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.check = true;
        saxDirParser.level = 0;
        
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.localBrowser);
    }
    
    @Test
    public void testCharactersWithCheckAndLevel10() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.check = true;
        saxDirParser.level = 10;
        
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.file_editor);
    }
    
    @Test
    public void testCharactersWithCheckAndLevel11() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.check = true;
        saxDirParser.level = 11;
        
        saxDirParser.characters(new char[] {'Y', 'e', 's'}, 0, 3);
        
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(SAXDirParser.lineNumAccepted);
    }
    
    @Test
    public void testCharactersWithCheckAndLevel12() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.check = true;
        saxDirParser.level = 12;
        
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", SAXDirParser.lineNumParam);
    }
    
    @Test
    public void testCharactersWithCheckAndLevel30() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.check = true;
        saxDirParser.level = 30;
        
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertEquals("test", saxDirParser.location);
    }
    
    @Test
    public void testCharactersWithCheckAndLevel31() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.check = true;
        saxDirParser.level = 31;
        
        saxDirParser.characters(new char[] {'Y', 'e', 's'}, 0, 3);
        
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(saxDirParser.local);
    }
    
    @Test
    public void testCharactersWithCheckAndLevel4() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.check = true;
        saxDirParser.level = 4;
        saxDirParser.location = "testLocation";
        saxDirParser.local = true;
        
        saxDirParser.characters(new char[] {'t', 'e', 's', 't'}, 0, 4);
        
        assertFalse(saxDirParser.check);
        assertEquals(-1, saxDirParser.level);
        assertTrue(SAXDirParser.directory.containsKey("test"));
        assertEquals("testLocation", SAXDirParser.directory.get("test"));
        assertTrue(SAXDirParser.localPackages.contains("test"));
    }
    
    @Test
    public void testEndElementGroup() throws SAXException {
        SAXDirParser saxDirParser = new SAXDirParser();
        
        saxDirParser.endElement("", "Group", "");
        
        assertFalse(saxDirParser.check);
    }
    
    @Test
    public void testGetDirectory() {
        SAXDirParser.directory.put("java.example", "exampleLocation");
        
        HashMap<String, String> result = SAXDirParser.getDirectory();
        assertEquals("exampleLocation", result.get("java.example"));
    }
    
    @Test
    public void testGetLocalPackagesNull() {
        SAXDirParser.localPackages.clear();
        
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages.add("java.example");
        
        StringList result = SAXDirParser.getLocalPackages();
        assertEquals("java.example", result.get(0));
    }
    
    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "testBrowser";
        
        assertEquals("testBrowser", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "testEditor";
        
        assertEquals("testEditor", SAXDirParser.getEditor());
    }
    
    @Test
    public void testAcceptsLineNumberTrue() {
        SAXDirParser.lineNumAccepted = true;
        
        assertTrue(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testAcceptsLineNumberFalse() {
        SAXDirParser.lineNumAccepted = false;
        
        assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "testParameter";
        
        assertEquals("testParameter", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testMain() throws Exception {
        SAXDirParser.main(null);
    }
}