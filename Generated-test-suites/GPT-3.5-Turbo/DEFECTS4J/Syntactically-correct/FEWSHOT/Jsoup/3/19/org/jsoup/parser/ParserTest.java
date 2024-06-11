package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser("", "");
    }

    @Test
    public void testParse() {
        assertNull(parser.parse());
    }

    @Test
    public void testParseBodyFragment() {
        assertNull(parser.parseBodyFragment("", ""));
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
        assertNull(parser.parseAttribute());
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
        assertNull(parser.addChildToParent(null, false));
    }

    @Test
    public void testStackHasValidParent() {
        parser.stackHasValidParent(null);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        assertNull(parser.popStackToSuitableContainer(null));
    }

    @Test
    public void testPopStackToClose() {
        assertNull(parser.popStackToClose(null));
    }

    @Test
    public void testLast() {
        assertNull(parser.last());
    }
}