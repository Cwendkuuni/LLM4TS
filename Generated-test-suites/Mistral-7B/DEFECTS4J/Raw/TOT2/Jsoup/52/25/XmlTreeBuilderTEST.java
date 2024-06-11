package org.jsoup.parser; // replace with your package name if needed

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInsertElement() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String input = "<test><element>Value</element></test>";
        Document doc = parser.parseFragment(input, "", new ParseErrorList());

        Element root = doc.getRootElement();
        Element element = root.getElementsByTag("element").first();
        Assert.assertEquals("Value", element.text());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String input = "<!-- This is a comment -->";
        Document doc = parser.parseFragment(input, "", new ParseErrorList());

        Comment comment = doc.select("comment()").first();
        Assert.assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String input = "<test>&#x20ac;</test>"; // euro symbol
        Document doc = parser.parseFragment(input, "", new ParseErrorList());

        TextNode textNode = doc.select("test text()").first();
        Assert.assertEquals("€", textNode.text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String input = "<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">";
        Document doc = parser.parseFragment(input, "", new ParseErrorList());

        DocumentType doctype = doc.doctype();
        Assert.assertEquals("html", doctype.name());
        Assert.assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.systemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String input = "<test><element><subelement></subelement></element></test>";

        Document doc = parser.parseFragment(input, "", new ParseErrorList());

        Element root = doc.getRootElement();
        Element element = root.getElementsByTag("element").first();
        Element subelement = element.getElementsByTag("subelement").first();

        parser.popStackToClose((EndTag) new Token.EndTag("element"));

        List<Node> childNodes = element.children();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertNull(subelement);
    }
}