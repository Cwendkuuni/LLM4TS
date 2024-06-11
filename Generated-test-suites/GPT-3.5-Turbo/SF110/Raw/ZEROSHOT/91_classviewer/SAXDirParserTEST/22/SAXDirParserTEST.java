
package com.jstevh.viewer;

import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.util.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        String webData = parser.getWebData();
        assertEquals("", webData);
    }

    @Test
    public void testSAXDirParserConstructor() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        assertNotNull(parser);
        assertEquals("", parser.getWebData());
    }
    
    @Test
    public void testStartElement_BrowserLoc() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "BrowserLoc", null);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
    }
    
    @Test
    public void testStartElement_Editor() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Editor", null);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
    }
    
    @Test
    public void testStartElement_acceptsLineNumber() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "acceptsLineNumber", null);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
    }
    
    @Test
    public void testStartElement_parameter() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "parameter", null);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
    }
    
    @Test
    public void testStartElement_Group() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Group", null);
        assertFalse(parser.check);
        assertEquals(2, parser.level);
    }
    
    @Test
    public void testStartElement_Web() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Web", null);
        assertTrue(parser.check);
        assertEquals(30, parser.level);
    }
    
    @Test
    public void testStartElement_Local() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Local", null);
        assertTrue(parser.check);
        assertEquals(31, parser.level);
    }
    
    @Test
    public void testStartElement_Names() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "Names", null);
        assertTrue(parser.check);
    }
    
    @Test
    public void testStartElement_pkg() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "", "pkg", null);
        assertEquals(4, parser.level);
    }
    
    @Test
    public void testCharacters_level0() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 0;
        parser.check = true;
        char[] charArray = "testStr".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("testStr", SAXDirParser.getLocalBrowser());
    }
    
    @Test
    public void testCharacters_level10() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 10;
        parser.check = true;
        char[] charArray = "testStr".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("testStr", SAXDirParser.getEditor());
    }
    
    @Test
    public void testCharacters_level11() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 11;
        parser.check = true;
        char[] charArray = "Yes".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(SAXDirParser.acceptsLineNumber());
        
        charArray = "No".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testCharacters_level12() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 12;
        parser.check = true;
        char[] charArray = "testParam".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("testParam", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testCharacters_level30() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 30;
        parser.check = true;
        char[] charArray = "testLocation".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("testLocation", parser.location);
    }
    
    @Test
    public void testCharacters_level31() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 31;
        parser.check = true;
        char[] charArray = "No".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertFalse(parser.local);
        
        charArray = "Yes".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertTrue(parser.local);
    }
    
    @Test
    public void testCharacters_level4() {
        SAXDirParser parser = new SAXDirParser();
        parser.level = 4;
        parser.check = true;
        parser.local = true;
        parser.location = "testLocation";
        char[] charArray = "testPkg".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
        assertEquals("testLocation", SAXDirParser.getDirectory().get("testPkg"));
        assertNotNull(SAXDirParser.getLocalPackages());
        assertEquals("testPkg", SAXDirParser.getLocalPackages().get(0));
        
        parser.local = false;
        charArray = "testPkg".toCharArray();
        parser.characters(charArray, 0, charArray.length);
        assertNotNull(SAXDirParser.getLocalPackages());
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testEndElement_Group() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        parser.check = true;
        parser.endElement("", "", "Group");
        assertFalse(parser.check);
    }
    
    @Test
    public void testGetDirectory() {
        SAXDirParser.directory.put("testPkg", "testLocation");
        HashMap<String, String> directoryClone = SAXDirParser.getDirectory();
        assertEquals("testLocation", directoryClone.get("testPkg"));
    }
    
    @Test
    public void testGetLocalPackages_empty() {
        SAXDirParser.localPackages.clear();
        assertNull(SAXDirParser.getLocalPackages());
    }
    
    @Test
    public void testGetLocalPackages_notEmpty() {
        SAXDirParser.localPackages.add("testPkg");
        StringList localPackagesClone = SAXDirParser.getLocalPackages();
        assertNotNull(localPackagesClone);
        assertEquals("testPkg", localPackagesClone.get(0));
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
    public void testAcceptsLineNumber() {
        SAXDirParser.lineNumAccepted = true;
        assertTrue(SAXDirParser.acceptsLineNumber());
        
        SAXDirParser.lineNumAccepted = false;
        assertFalse(SAXDirParser.acceptsLineNumber());
    }
    
    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "testParam";
        assertEquals("testParam", SAXDirParser.lineNumberParameter());
    }
    
    @Test
    public void testMain() throws Exception {
        // TODO: Implement test case
    }
}
