package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;

import java.util.ArrayList;

public class TreeBuilderTest {

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com/";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };

        Document doc = builder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("html", doc.tagName());

        Element html = doc.body();
        assertNotNull(html);
        assertEquals("body", html.tagName());

        Element title = html.head();
        assertNotNull(title);
        assertEquals("head", title.tagName());

        Element h1 = html.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new TreeBuilder() {
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

        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com/";

        builder.initialiseParse(input, baseUri, ParseErrorList.noTracking());
        builder.runParser();
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder builder = new TreeBuilder() {
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

        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com/";

        builder.initialiseParse(input, baseUri, ParseErrorList.noTracking());
        Token endTagToken = null;
        while ((endTagToken = builder.tokeniser.read()) != null) {
            if (endTagToken.type == Token.TokenType.END_TAG) {
                break;
            }
        }
        EndTag endTag = (EndTag) endTagToken;
        assertEquals("html", endTag.name());

        builder.runParser();
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    StartTag startTag = (StartTag) token;
                    Attributes attributes = startTag.attributes();
                    System.out.println(startTag.name());
                    for (String name : attributes.keySet()) {
                        System.out.println(name + "=" + attributes.get(name));
                    }
                    return true;
                }
                return false;
            }
        };

        String input = "<p class='test' id='test-1'>Hello World</p>";
        String baseUri = "http://example.com/";

        builder.initialiseParse(input, baseUri, ParseErrorList.noTracking());
        builder.runParser();
    }
}