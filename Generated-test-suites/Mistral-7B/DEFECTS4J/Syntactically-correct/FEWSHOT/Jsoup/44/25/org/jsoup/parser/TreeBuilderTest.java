package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };

        builder.initialiseParse(input, baseUri, errors);

        assertTrue(builder.doc != null);
        assertTrue(builder.stack.size() == 0);
        assertTrue(builder.baseUri.equals(baseUri));
        assertTrue(builder.errors == errors);
    }

    @Test
    public void testParseDocument() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>";
        String baseUri = "https://example.com";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.DOCTYPE) {
                    Document doc = new Document(token.docTypeName, token.publicId, token.systemId);
                    builder.doc = doc;
                    return true;
                }
                if (token.type == Token.TokenType.START_TAG) {
                    Element parent = builder.currentElement();
                    Element element = parent.addElement(token.name);
                    builder.stack.add(element);
                    return true;
                }
                if (token.type == Token.TokenType.END_TAG) {
                    Element element = builder.stack.remove(builder.stack.size() - 1);
                    assertEquals(token.name, element.tagName());
                    return true;
                }
                return false;
            }
        };

        Document doc = builder.parse(input, baseUri);

        assertTrue(doc != null);
        assertTrue(doc.childNodeSize() > 0);
        assertTrue(builder.doc.equals(doc));
    }

    @Test
    public void testParseDocumentWithErrors() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title foo></title></head><body><p bar></p></body></html>";
        String baseUri = "https://example.com";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.DOCTYPE) {
                    Document doc = new Document(token.docTypeName, token.publicId, token.systemId);
                    builder.doc = doc;
                    return true;
                }
                if (token.type == Token.TokenType.START_TAG) {
                    Element parent = builder.currentElement();
                    Element element = parent.addElement(token.name);
                    builder.stack.add(element);
                    if (token.name.equals("title")) {
                        token.setExpectedEndTagName("title");
                        token.setLine(1);
                        token.setColumn(6);
                    }
                    return true;
                }
                if (token.type == Token.TokenType.END_TAG) {
                    Element element = builder.stack.remove(builder.stack.size() - 1);
                    assertEquals(token.name, element.tagName());
                    return true;
                }
                if (token.type == Token.TokenType.ERROR) {
                    builder.errors.addError(token.getLocation(), token.getMessage());
                    return true;
                }
                return false;
            }
        };

        ParseErrorList errors = builder.errors;
        Document doc = builder.parse(input, baseUri);

        assertTrue(doc != null);
        assertTrue(doc.childNodeSize() > 0);
        assertTrue(errors.size() > 0);
        assertEquals(1, errors.size());
        assertEquals("Unexpected end of document", errors.getError(0).getMessage());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    return processStartTag(token.name);
                }
                return false;
            }

            protected boolean processStartTag(String name) {
                return true;
            }
        };

        TreeBuilder spy = org.mockito.Mockito.spy(builder);

        Token token = new Token(Token.TokenType.START_TAG, "html");

        org.mockito.Mockito.doReturn(true).when(spy).processStartTag(token.name);

        assertTrue(spy.process(token));
        assertTrue(spy.currentElement() != null);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    return processStartTag(token.name, token.attributes());
                }
                return false;
            }

            protected boolean processStartTag(String name, Attributes attrs) {
                return true;
            }
        };

        TreeBuilder spy = org.mockito.Mockito.spy(builder);

        Token token = new Token(Token.TokenType.START_TAG, "input", new Attributes().put("type", "text"));

        org.mockito.Mockito.doReturn(true).when(spy).processStartTag(token.name, token.attributes());

        assertTrue(spy.process(token));
        assertTrue(spy.currentElement() != null);
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    return processEndTag(token.name);
                }
                return false;
            }

            protected boolean processEndTag(String name) {
                return true;
            }
        };

        TreeBuilder spy = org.mockito.Mockito.spy(builder);

        Token token = new Token(Token.TokenType.END_TAG, "html");

        org.mockito.Mockito.doReturn(true).when(spy).processEndTag(token.name);

        assertTrue(spy.process(token));
        assertNull(spy.currentElement());
    }
}