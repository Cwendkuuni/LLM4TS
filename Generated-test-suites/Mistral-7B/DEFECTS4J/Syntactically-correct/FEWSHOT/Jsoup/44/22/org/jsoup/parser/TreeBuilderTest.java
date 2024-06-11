package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testParseEmptyInput() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        Document doc = builder.parse("", "baseUri");
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertFalse(doc.body().childElements().size() > 0);
    }

    @Test
    public void testParseSingleStartTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    Element element = new Element(token.name());
                    stack.add(element);
                    doc.appendElement(element);
                    return true;
                }
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                return false;
            }
        };
        Document doc = builder.parse("<html/>", "baseUri");
        assertNotNull(doc);
        Element html = doc.html();
        assertNotNull(html);
        assertSame(html, currentElement(builder));
        assertFalse(html.childElements().size() > 0);
    }

    @Test
    public void testParseSingleEndTag() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.END_TAG) {
                    String name = token.name();
                    Element parent = currentElement(builder);
                    if (parent != null && parent.tagName().equals(name)) {
                        stack.remove(stack.size() - 1);
                    }
                    return true;
                }
                return false;
            }

            @Override
            protected boolean processEndTag(String name) {
                return false;
            }
        };
        Document doc = builder.parse("<html><body></body></html>", "baseUri");
        assertNotNull(doc);
        Element html = doc.html();
        assertNotNull(html);
        Element body = html.body();
        assertNotNull(body);
        assertSame(body, currentElement(builder));
        assertFalse(body.childElements().size() > 0);
    }

    @Test
    public void testParseMultipleTags() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    Element element = new Element(token.name());
                    stack.add(element);
                    doc.appendElement(element);
                    return true;
                } else if (token.type == Token.TokenType.END_TAG) {
                    String name = token.name();
                    Element parent = currentElement(builder);
                    if (parent != null && parent.tagName().equals(name)) {
                        stack.remove(stack.size() - 1);
                    }
                    return true;
                }
                return false;
            }

            @Override
            protected boolean processStartTag(String name) {
                return false;
            }

            @Override
            protected boolean processEndTag(String name) {
                return false;
            }
        };
        Document doc = builder.parse("<html><body><p>Hello, world!</p></body></html>", "baseUri");
        assertNotNull(doc);
        Element html = doc.html();
        assertNotNull(html);
        Element body = html.body();
        assertNotNull(body);
        Element p = body.getFirstElementSibling();
        assertNotNull(p);
        assertSame(p, currentElement(builder));
        assertEquals("Hello, world!", p.text());
        assertFalse(body.childElements().size() > 1);
        assertFalse(html.childElements().size() > 1);
    }

    @Test
    public void testParseWithErrors() {
        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.START_TAG) {
                    Element element = new Element(token.name());
                    stack.add(element);
                    doc.appendElement(element);
                    return true;
                } else if (token.type == Token.TokenType.END_TAG) {
                    String name = token.name();
                    Element

parent = currentElement(builder);
if (parent != null && parent.tagName().equals(name)) {
stack.remove(stack.size() - 1);
}
return true;
}
if (errors != null) {
errors.add(new ParseError("Error message", token.line(), token.column()));
}
return false;
}

@Override
protected boolean processStartTag(String name) {
return false;
}

@Override
protected boolean processEndTag(String name) {
return false;
}
};
Document doc = builder.parse("<html><body><p>Hello, world!<invalid></invalid></p></body></html>", "baseUri", new ParseErrorList());
assertNotNull(doc);
assertEquals(1, doc.body().childElements().size());
assertEquals(1, doc.body().getFirstElementSibling().childElements().size());
assertEquals(1, doc.body().getFirstElementSibling().childElements().get(0).childElements().size());
assertNotNull(doc.body().getFirstElementSibling().childElements().get(0).child(0));
assertSame(doc.body().getFirstElementSibling().childElements().get(0).child(0), currentElement(builder));
assertNotNull(errors);
assertEquals(1, errors.size());
assertEquals("Error message", errors.get(0).getMessage());
assertEquals(2, errors.get(0).line());
assertEquals(10, errors.get(0).column());
}

private Element currentElement(TreeBuilder builder) {
int size = builder.stack.size();
return size > 0 ? builder.stack.get(size - 1) : null;
}

}