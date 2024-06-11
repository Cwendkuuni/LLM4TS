package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        // initialize the Parser object
        parser = new Parser(null, null, false);
    }

    @Test
    public void testParse() {
        // test the parse method
        Document document = parser.parse(null, null);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragment() {
        // test the parseBodyFragment method
        Document document = parser.parseBodyFragment(null, null);
        assertNotNull(document);
    }

    @Test
    public void testParseComment() {
        // test the parseComment method
        parser.parseComment();
    }

    @Test
    public void testParseXmlDecl() {
        // test the parseXmlDecl method
        parser.parseXmlDecl();
    }

    @Test
    public void testParseEndTag() {
        // test the parseEndTag method
        parser.parseEndTag();
    }

    @Test
    public void testParseStartTag() {
        // test the parseStartTag method
        parser.parseStartTag();
    }

    @Test
    public void testParseAttribute() {
        // test the parseAttribute method
        Attribute attribute = parser.parseAttribute();
        assertNull(attribute);
    }

    @Test
    public void testParseTextNode() {
        // test the parseTextNode method
        parser.parseTextNode();
    }

    @Test
    public void testParseCdata() {
        // test the parseCdata method
        parser.parseCdata();
    }

    @Test
    public void testAddChildToParent() {
        // test the addChildToParent method
        Element element = new Element(Tag.valueOf("test"), "");
        Element parent = parser.addChildToParent(element, true);
        assertNotNull(parent);
    }

    @Test
    public void testStackHasValidParent() {
        // test the stackHasValidParent method
        boolean validParent = parser.stackHasValidParent(Tag.valueOf("div"));
        assertFalse(validParent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        // test the popStackToSuitableContainer method
        Element element = new Element(Tag.valueOf("test"), "");
        Element parent = parser.popStackToSuitableContainer(element.tag());
        assertNull(parent);
    }

    @Test
    public void testPopStackToClose() {
        // test the popStackToClose method
        Element element = new Element(Tag.valueOf("test"), "");
        Element closedElement = parser.popStackToClose(element.tag());
        assertNull(closedElement);
    }

    @Test
    public void testLast() {
        // test the last method
        Element element = parser.last();
        assertNull(element);
    }
}