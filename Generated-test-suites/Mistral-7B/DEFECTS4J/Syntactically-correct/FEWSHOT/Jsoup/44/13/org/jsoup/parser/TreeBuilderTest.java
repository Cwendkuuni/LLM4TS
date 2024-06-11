package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Tokeniser;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testParseEmpty() {
        ParseErrorList errors = new ParseErrorList();
        Document doc = new TreeBuilder().parse("", "", errors);
        assertNotNull(doc);
        assertEquals(0, doc.body().childNodeSize());
    }

    @Test
    public void testParseWithErrors() {
        ParseErrorList errors = new ParseErrorList();
        String input = "<html><body><h1>Hello <script>1</script> World</h1>";
        Document doc = new TreeBuilder().parse(input, "", errors);
        assertNotNull(doc);
        assertEquals(1, doc.body().childNodeSize());
        Element h1 = doc.body().child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello ", h1.text());
        assertEquals(1, h1.childNodeSize());
        Element script = h1.child(0);
        assertEquals("script", script.tagName());
        assertNull(script.text());
        assertEquals(1, errors.size());
        assertEquals("Error parsing start tag : script\n" +
                "  in document line 1 column 25", errors.get(0).getMessage());
    }

    @Test
    public void testParse() {
        ParseErrorList errors = new ParseErrorList();
        String input = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = new TreeBuilder().parse(input, "", errors);
        assertNotNull(doc);
        assertEquals(1, doc.body().childNodeSize());
        Element h1 = doc.body().child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World", h1.text());
        assertEquals(0, errors.size());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder tb = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    StartTag startTag = (StartTag) token;
                    System.out.println(startTag.name());
                    return true;
                }
                return false;
            }
        };
        String input = "<html><body><h1>Hello World</h1></body></html>";
        tb.initialiseParse(input, "", ParseErrorList.noTracking());
        tb.runParser();
        assertEquals("html", tb.currentElement().tagName());
        tb.processStartTag("body");
        assertEquals("body", tb.currentElement().tagName());
        tb.processStartTag("h1");
        assertEquals("h1", tb.currentElement().tagName());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder tb = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    EndTag endTag = (EndTag) token;
                    System.out.println(endTag.name());
                    return true;
                }
                return false;
            }
        };
        String input = "<html><body><h1>Hello World</h1></body></html>";
        tb.initialiseParse(input, "", ParseErrorList.noTracking());
        tb.runParser();
        tb.processEndTag("html");
        tb.processEndTag("head");
        tb.processEndTag("body");
        tb.processEndTag("h1");
        assertEquals(0, tb.stack.size());
    }
}