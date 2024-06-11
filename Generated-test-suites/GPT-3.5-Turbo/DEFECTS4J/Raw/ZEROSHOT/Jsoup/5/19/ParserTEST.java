package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser("", "", false);
    }
    
    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
    }
    
    @Test
    public void testParseBodyFragment() {
        Document doc = parser.parseBodyFragment("", "");
        assertNotNull(doc);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = parser.parseBodyFragmentRelaxed("", "");
        assertNotNull(doc);
    }
    
    @Test
    public void testParseComment() {
        parser.parseComment();
        // TODO: Add assertion
    }
    
    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        // TODO: Add assertion
    }
    
    @Test
    public void testParseEndTag() {
        parser.parseEndTag();
        // TODO: Add assertion
    }
    
    @Test
    public void testParseStartTag() {
        parser.parseStartTag();
        // TODO: Add assertion
    }
    
    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
        // TODO: Add assertion
    }
    
    @Test
    public void testParseCdata() {
        parser.parseCdata();
        // TODO: Add assertion
    }
    
    @Test
    public void testAddChildToParent() {
        Element child = new Element(Tag.valueOf("tag"), "");
        boolean isEmptyElement = true;
        Element parent = parser.addChildToParent(child, isEmptyElement);
        assertNotNull(parent);
    }
    
    @Test
    public void testStackHasValidParent() {
        Tag childTag = Tag.valueOf("tag");
        boolean validParent = parser.stackHasValidParent(childTag);
        assertEquals(validParent, false);
    }
    
    @Test
    public void testPopStackToSuitableContainer() {
        Tag tag = Tag.valueOf("tag");
        Element parent = parser.popStackToSuitableContainer(tag);
        assertNotNull(parent);
    }
    
    @Test
    public void testPopStackToClose() {
        Tag tag = Tag.valueOf("tag");
        Element elToClose = parser.popStackToClose(tag);
        assertNotNull(elToClose);
    }
    
    @Test
    public void testLast() {
        Element last = parser.last();
        assertNotNull(last);
    }
}