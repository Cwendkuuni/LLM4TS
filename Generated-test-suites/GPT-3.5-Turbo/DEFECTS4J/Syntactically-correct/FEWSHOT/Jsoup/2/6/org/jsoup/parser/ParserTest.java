package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import java.util.*;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        // Assert statements...
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a paragraph.</p>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        // Assert statements...
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("", "", false);
        parser.parseComment();
        // Assert statements...
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("", "", false);
        parser.parseXmlDecl();
        // Assert statements...
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("", "", false);
        parser.parseEndTag();
        // Assert statements...
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("", "", false);
        parser.parseStartTag();
        // Assert statements...
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("", "", false);
        parser.parseAttribute();
        // Assert statements...
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("", "", false);
        parser.parseTextNode();
        // Assert statements...
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("", "", false);
        parser.parseCdata();
        // Assert statements...
    }

    @Test
    public void testAddChildToParent() {
        Parser parser = new Parser("", "", false);
        parser.addChildToParent(new Element(Tag.valueOf("div"), ""), false);
        // Assert statements...
    }

    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "", false);
        parser.stackHasValidParent(Tag.valueOf("body"));
        // Assert statements...
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "", false);
        parser.popStackToSuitableContainer(Tag.valueOf("div"));
        // Assert statements...
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", false);
        parser.popStackToClose(Tag.valueOf("div"));
        // Assert statements...
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("", "", false);
        parser.last();
        // Assert statements...
    }
}