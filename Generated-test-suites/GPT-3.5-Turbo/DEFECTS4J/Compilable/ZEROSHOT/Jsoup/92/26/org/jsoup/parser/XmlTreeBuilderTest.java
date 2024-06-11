package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder builder;
    
    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testInitialiseParse() {
        // TODO: Implement this test
    }
    
    @Test
    public void testParseReaderString() {
        // TODO: Implement this test
    }
    
    @Test
    public void testParseStringString() {
        // TODO: Implement this test
    }
    
    @Test
    public void testProcessStartTagToken() {
        // TODO: Implement this test
    }
    
    @Test
    public void testProcessEndTagToken() {
        // TODO: Implement this test
    }
    
    @Test
    public void testProcessCommentToken() {
        // TODO: Implement this test
    }
    
    @Test
    public void testProcessCharacterToken() {
        // TODO: Implement this test
    }
    
    @Test
    public void testProcessDoctypeToken() {
        // TODO: Implement this test
    }
    
    @Test
    public void testInsertNode() {
        // TODO: Implement this test
    }
    
    @Test
    public void testInsertStartTagToken() {
        // TODO: Implement this test
    }
    
    @Test
    public void testInsertCommentToken() {
        // TODO: Implement this test
    }
    
    @Test
    public void testInsertCharacterToken() {
        // TODO: Implement this test
    }
    
    @Test
    public void testInsertDoctypeToken() {
        // TODO: Implement this test
    }
    
    @Test
    public void testPopStackToClose() {
        // TODO: Implement this test
    }
    
    @Test
    public void testParseFragmentStringStringParser() {
        // TODO: Implement this test
    }
    
    @Test
    public void testParseFragmentStringElementStringParser() {
        // TODO: Implement this test
    }
}