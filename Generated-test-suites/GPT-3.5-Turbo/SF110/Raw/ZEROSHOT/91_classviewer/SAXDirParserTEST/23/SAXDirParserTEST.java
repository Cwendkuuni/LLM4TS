package com.jstevh.viewer;

import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.helpers.*;
import java.util.*;
import com.jstevh.tools.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        parser.webData = "Test data";
        
        String result = parser.getWebData();
        
        assertEquals("Test data", result);
    }
    
    @Test
    public void testStartElement() {
        // TODO: Write test cases for startElement() method
    }
    
    @Test
    public void testCharacters() {
        // TODO: Write test cases for characters() method
    }
    
    @Test
    public void testEndElement() {
        // TODO: Write test cases for endElement() method
    }
    
    @Test
    public void testGetDirectory() {
        SAXDirParser.directory.put("java.lang", "/path/to/java/lang");
        SAXDirParser.directory.put("javax.swing.border", "/path/to/java/swing/border");
        
        HashMap<String, String> result = SAXDirParser.getDirectory();
        
        assertEquals("/path/to/java/lang", result.get("java.lang"));
        assertEquals("/path/to/java/swing/border", result.get("javax.swing.border"));
    }
    
    @Test
    public void testGetLocalPackages() {
        SAXDirParser.localPackages.add("com.example.package1");
        SAXDirParser.localPackages.add("com.example.package2");
        
        StringList result = SAXDirParser.getLocalPackages();
        
        assertEquals(2, result.size());
        assertEquals("com.example.package1", result.get(0));
        assertEquals("com.example.package2", result.get(1));
    }
    
    @Test
    public void testGetLocalBrowser() {
        SAXDirParser.localBrowser = "Chrome";
        
        String result = SAXDirParser.getLocalBrowser();
        
        assertEquals("Chrome", result);
    }
    
    @Test
    public void testGetEditor() {
        SAXDirParser.file_editor = "Notepad";
        
        String result = SAXDirParser.getEditor();
        
        assertEquals("Notepad", result);
    }
    
    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser.lineNumAccepted = true;
        
        boolean result = SAXDirParser.acceptsLineNumber();
        
        assertTrue(result);
    }
    
    @Test
    public void testLineNumberParameter() {
        SAXDirParser.lineNumParam = "Parameter value";
        
        String result = SAXDirParser.lineNumberParameter();
        
        assertEquals("Parameter value", result);
    }
    
}