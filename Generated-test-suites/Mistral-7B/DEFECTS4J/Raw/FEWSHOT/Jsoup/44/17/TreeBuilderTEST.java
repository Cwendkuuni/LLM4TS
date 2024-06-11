package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Tokeniser;

public class TreeBuilderTest {

    TreeBuilder builder;
    Tokeniser tokeniser;
    Document doc;

    @Before
    public void setUp() {
        builder = new WhitespaceHandlerTreeBuilder(); // use a specific subclass of TreeBuilder for testing
        doc = new Document("");
    }

    @Test
    public void testParseWithNullInput() {
        try {
            builder.parse(null, "");
            assertFalse("parse should fail with null input", true);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testParseWithEmptyInput() {
        builder.initialiseParse("", "", ParseErrorList.noTracking());
        builder.runParser();
        assertNotNull("doc should not be null", builder.doc);
        assertNull("stack should be empty", builder.stack.size());
    }

    @Test
    public void testParseWithInputContainingWhitespace() {
        String input = "   <html>\n <head>\n    <title>Test</title>\n </head>\n <body>\n <p>Hello world!</p>\n </body>\n";
        builder.initialiseParse(input, "", ParseErrorList.noTracking());
        builder.runParser();

        Element root = builder.doc.rootElement();
        assertSame("root element should be html tag", "html", root.tagName());

        Element title = root.select("title").first();
        assertNotNull("title element should not be null", title);
        assertEquals("title text should be Test", "Test", title.text());

        Element body = root.select("body").first();
        assertNotNull("body element should not be null", body);

        Element p = body.select("p").first();
        assertNotNull("p element should not be null", p);
        assertEquals("p text should be Hello world!", "Hello world!", p.text());

        assertEquals("stack size should be 0 after parsing", 0, builder.stack.size());
    }

    @Test
    public void testParseWithInputContainingErrors() {
        String input = "<html><title<script>alert('Hello World')</script>";
        ParseErrorList errors = new ParseErrorList();
        builder.initialiseParse(input, "", errors);
        builder.runParser();

        Element root = builder.doc.rootElement();
        assertNotNull("root element should not be null", root);
        assertEquals("stack size should be 0 after parsing", 0, builder.stack.size());

        assertFalse("errors should not be empty", errors.isEmpty());
        assertEquals("first error message should be '<title:1:18> Unexpected character '<' (code 60)",
                "<title:1:18> Unexpected character '<' (code 60)", errors.get(0).getMessage());
    }

    @Test
    public void testProcessStartTag() {
        Token token = new Token.StartTag("html");
        Attribute attributes = new Attributes();
        attributes.put("lang", "en");
        token.attrs(attributes);

        builder.processStartTag("html", token.attrs());

        Element root = builder.currentElement();
        assertNotNull("root element should not be null", root);
        assertEquals("root tag name should be html", "html", root.tagName());
        assertNull("root attributes should be empty", root.attributes().size());
    }

    @Test
    public void testProcessEndTag() {
        Token token = new Token.EndTag("html");

        builder.processEndTag("html");

        Element root = builder.currentElement();
        assertNull("current element should be null after processing end tag", root);
    }
}

class WhitespaceHandlerTreeBuilder extends TreeBuilder {
    @Override
    protected boolean process(Token token) {
        if (token.type == Token.TokenType.START_TAG) {
            return processStartTag(token.getName());
        } else if (token.type == Token.TokenType.END_TAG) {
            return processEndTag(token.getName());
        } else if (token.type == Token.TokenType.WHITESPACE) {
            // ignore
            return true;
        } else {
            errors.error(token, "Unexpected token: " + token.type);
            return false;
        }
    }
}