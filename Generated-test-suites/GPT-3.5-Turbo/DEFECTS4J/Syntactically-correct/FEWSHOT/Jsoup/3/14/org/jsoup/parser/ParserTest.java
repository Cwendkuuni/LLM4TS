package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.LinkedList;

public class ParserTest {
    private Parser parser;
    
    @Before
    public void setup() {
        parser = new Parser("", "", false);
    }

    @Test
    public void testParse() {
        Document document = parser.parse();
        assertNotNull(document);
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
    }

    @Test
    public void testParseEndTag() {
        parser.parseEndTag();
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag();
    }

    @Test
    public void testParseAttribute() {
        Attribute attribute = parser.parseAttribute();
        assertNull(attribute);
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();
    }

    @Test
    public void testAddChildToParent() {
        Element child = new Element(Tag.valueOf("tag"), "", null);
        boolean isEmptyElement = true;
        Element parent = parser.addChildToParent(child, isEmptyElement);
        assertNull(parent);
    }

    @Test
    public void testStackHasValidParent() {
        boolean valid = parser.stackHasValidParent(Tag.valueOf("tag"));
        assertFalse(valid);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Element element = parser.popStackToSuitableContainer(Tag.valueOf("tag"));
        assertNull(element);
    }

    @Test
    public void testPopStackToClose() {
        Element element = parser.popStackToClose(Tag.valueOf("tag"));
        assertNull(element);
    }

    @Test
    public void testLast() {
        Element element = parser.last();
        assertNull(element);
    }
}