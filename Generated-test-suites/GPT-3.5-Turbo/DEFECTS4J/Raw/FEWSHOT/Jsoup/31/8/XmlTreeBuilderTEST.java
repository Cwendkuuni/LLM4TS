package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.Iterator;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.getFirst(), builder.doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("id", "123");
        startTag.attributes.put("class", "test");

        builder.insert(startTag);

        assertEquals(builder.stack.getLast().nodeName(), "tag");
        assertEquals(builder.stack.getLast().attributes().size(), 2);
        assertEquals(builder.stack.getLast().attributes().get("id"), "123");
        assertEquals(builder.stack.getLast().attributes().get("class"), "test");
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");

        Element element1 = new Element("tag");
        Element element2 = new Element("tag");
        builder.stack.add(element1);
        builder.stack.add(element2);

        builder.popStackToClose(endTag);

        assertEquals(builder.stack.size(), 0);
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");

        builder.insert(comment);

        assertEquals(builder.stack.getLast().nodeName(), "#comment");
        assertEquals(builder.stack.getLast().toString(), "<!--This is a comment-->");
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("This is a character");

        builder.insert(character);

        assertEquals(builder.stack.getLast().nodeName(), "#text");
        assertEquals(builder.stack.getLast().toString(), "This is a character");
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");

        builder.insert(doctype);

        assertEquals(builder.stack.getLast().nodeName(), "#doctype");
        assertEquals(builder.stack.getLast().toString(), "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
    }
}