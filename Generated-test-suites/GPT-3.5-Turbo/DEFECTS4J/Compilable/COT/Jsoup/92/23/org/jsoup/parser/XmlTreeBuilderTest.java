
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void defaultSettings_returnsPreserveCase() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void initialiseParse_initializesParse() {
        // implement test case
    }
    
    @Test
    public void parse_parsesReader() {
        // implement test case
    }
    
    @Test
    public void parse_parsesString() {
        // implement test case
    }
    
    @Test
    public void process_handlesStartTag() {
        // implement test case
    }
    
    @Test
    public void process_handlesEndTag() {
        // implement test case
    }
    
    @Test
    public void process_handlesComment() {
        // implement test case
    }
    
    @Test
    public void process_handlesCharacter() {
        // implement test case
    }
    
    @Test
    public void process_handlesDoctype() {
        // implement test case
    }
    
    @Test
    public void insertNode_insertsNode() {
        // implement test case
    }
    
    @Test
    public void insert_insertsStartTag() {
        // implement test case
    }
    
    @Test
    public void insert_insertsComment() {
        // implement test case
    }
    
    @Test
    public void insert_insertsCharacter() {
        // implement test case
    }
    
    @Test
    public void insert_insertsDoctype() {
        // implement test case
    }
    
    @Test
    public void popStackToClose_popsStack() {
        // implement test case
    }
    
    @Test
    public void parseFragment_parsesFragment() {
        // implement test case
    }
    
    @Test
    public void parseFragment_parsesFragmentWithContext() {
        // implement test case
    }
}

