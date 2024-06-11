package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

import java.util.HashMap;

public class SAXDirParserTest {
    
    private SAXDirParser parser;
    
    @Before
    public void setUp() {
        parser = new SAXDirParser();
    }
    
    @Test
    public void testStartElement_BrowserLoc() {
        parser.startElement("namespace", "local", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
    }
    
    @Test
    public void testStartElement_Editor() {
        parser.startElement("namespace", "local", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
    }
    
    @Test
    public void testStartElement_AcceptsLineNumber() {
        parser.startElement("namespace", "local", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
    }
    
    @Test
    public void testStartElement_Parameter() {
        parser.startElement("namespace", "local", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
    }
    
    @Test
    public void testStartElement_Group() {
        parser.startElement("namespace", "local", "Group", null);
        assertEquals(2, parser.level);
        assertFalse(parser.check);
    }
    
    @Test
    public void testStartElement_Web() {
        parser.startElement("namespace", "local", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
    }
    
    @Test
    public void testStartElement_Local() {
        parser.startElement("namespace", "local", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
    }
    
    @Test
    public void testStartElement_Names() {
        parser.startElement("namespace", "local", "Names", null);
        assertTrue(parser.check);
    }
    
    @Test
    public void testStartElement_Pkg() {
        parser.startElement("namespace", "local", "pkg", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters_Level0() {
        parser.check = true;
        parser.level = 0;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }
    
    @Test
    public void testCharacters_Level10() {
        parser.check = true;
        parser.level = 10;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }
    
    @Test
    public void testCharacters_Level11_Yes() {
        parser.check = true;
        parser.level = 11;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testCharacters_Level11_No() {
        parser.check = true;
        parser.level = 11;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testCharacters_Level12() {
        parser.check = true;
        parser.level = 12;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testCharacters_Level30() {
        parser.check = true;
        parser.level = 30;
        parser.characters("test".toCharArray(), 0, 4);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("test", parser.location);
    }
    
    @Test
    public void testCharacters_Level31_Yes() {
        parser.check = true;
        parser.level = 31;
        parser.characters("Yes".toCharArray(), 0, 3);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
    }
    
    @Test
    public void testCharacters_Level31_No() {
        parser.check = true;
        parser.level = 31;
        parser.characters("No".toCharArray(), 0, 2);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
    }
    
    @Test
    public void testCharacters_Level4() {
        parser.check = true;
        parser.level = 4;
        parser.location = "location";
        parser.characters("pkg".toCharArray(), 0, 3);
        assertEquals(0, SAXDirParser.directory.size());
        assertEquals(0, SAXDirParser.localPackages.size());
        
        parser.characters("pkg.".toCharArray(), 0, 4);
        assertEquals(1, SAXDirParser.directory.size());
        assertEquals(1, SAXDirParser.localPackages.size());
        assertEquals("location", SAXDirParser.directory.get("pkg"));
        
        parser.characters("pkg_ext.".toCharArray(), 0, 8);
        assertEquals("location", SAXDirParser.directory.get("pkg"));
        assertEquals("location", SAXDirParser.directory.get("pkg_ext"));
        assertEquals(2, SAXDirParser.directory.size());
        assertEquals(2, SAXDirParser.localPackages.size());
    }
    
    @Test
    public void testEndElement_Group() {
        parser.check = true;
        parser.endElement("namespace", "local", "Group");
        assertFalse(parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        SAXDirParser.directory = new HashMap<>();
        SAXDirParser.directory.put("pkg", "location");
        assertEquals(SAXDirParser.directory, SAXDirParser.getDirectory());
    }
    
    @Test
    public void testGetLocalPackages_Empty() {
        SAXDirParser.localPackages = new StringList();
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalPackages_NonEmpty() {
        SAXDirParser.localPackages = new StringList();
        SAXDirParser.localPackages.add("pkg1");
        SAXDirParser.localPackages.add("pkg2");
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
    public void testAcceptsLineNumber() {
        SAXDirParser.lineNumAccepted = true;
        assertTrue(SAXDirParser.acceptsLineNumber());
        
        SAXDirParser.lineNumAccepted = false;
        assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "param";
        assertEquals("param", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testMain() throws Exception {
        SAXDirParser.main(new String[]{});
        // Test console output
    }
}