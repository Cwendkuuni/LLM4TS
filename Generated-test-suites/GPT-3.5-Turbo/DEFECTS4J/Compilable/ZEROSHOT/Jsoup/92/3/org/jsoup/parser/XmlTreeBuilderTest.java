package org.jsoup.parser;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        
        assertNotNull(parseSettings);
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }
    
    @Test
    public void testInitialiseParse() {
        // TODO: Implement test case
    }
    
    @Test
    public void testParseReaderString() {
        // TODO: Implement test case
    }

    @Test
    public void testParseStringString() {
        // TODO: Implement test case
    }
    
    @Test
    public void testProcess() {
        // TODO: Implement test case
    }
    
    @Test
    public void testInsertNode() {
        // TODO: Implement test case
    }
    
    @Test
    public void testInsertStartTag() {
        // TODO: Implement test case
    }
    
    @Test
    public void testInsertComment() {
        // TODO: Implement test case
    }
    
    @Test
    public void testInsertCharacter() {
        // TODO: Implement test case
    }
    
    @Test
    public void testInsertDoctype() {
        // TODO: Implement test case
    }
    
    @Test
    public void testPopStackToClose() {
        // TODO: Implement test case
    }
    
    @Test
    public void testParseFragmentStringStringParser() {
        // TODO: Implement test case
    }
    
    @Test
    public void testParseFragmentStringElementStringParser() {
        // TODO: Implement test case
    }
}