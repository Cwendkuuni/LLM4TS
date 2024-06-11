package org.jsoup.parser; // Import necessary classes for testing
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<root><child/></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(input, baseUri, errors);
        assertEquals("root", builder.doc.tagName());
        assertNotNull(builder.doc.childNodes());
    }

    @Test
    public void testProcessStartTag() {
        String input = "<root><child/></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = builder.tokeniser.next(); // Assuming a StartTag token is returned
        Element child = builder.insert(token);
        assertEquals("child", child.tagName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", null);
        Element child = new Element("child", null);
        builder.stack.add(root);
        builder.stack.add(child);
        builder.popStackToClose(new Token.EndTag("child"));
        assertNotNull(builder.stack.get(builder.stack.size() - 1)); // root should still be on the stack
        assertNull(builder.stack.get(builder.stack.size() - 2)); // child should have been removed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInvalidToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(-1, null); // Invalid token type
        builder.process(token);
    }
}