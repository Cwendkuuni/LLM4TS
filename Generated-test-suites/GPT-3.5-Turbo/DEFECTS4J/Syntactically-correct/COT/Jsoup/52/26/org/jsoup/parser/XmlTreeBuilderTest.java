
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.*;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("input", "baseUri", new ParseErrorList());

        // Assert the document is placed on the stack
        Assert.assertEquals(builder.getStack().get(0), builder.getDoc());

        // Assert the output settings syntax is set to XML
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, builder.getDoc().outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = builder.insert(startTag);

        // Assert the element is inserted into the current element's children
        Assert.assertEquals(element, builder.getCurrentElement().children().get(0));

        // Assert the element is added to the stack if it's not self-closing
        Assert.assertEquals(element, builder.getStack().get(builder.getStack().size() - 1));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = builder.insert(startTag);
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.process(endTag);

        // Assert the element is removed from the stack
        Assert.assertEquals(0, builder.getStack().size());

        // Assert the element is the last child of the document
        Assert.assertEquals(element, builder.getDoc().children().get(builder.getDoc().children().size() - 1));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        builder.process(comment);

        // Assert the comment is inserted into the current element's children
        Assert.assertEquals(comment, builder.getCurrentElement().children().get(0));
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("character");
        builder.process(character);

        // Assert the character node is inserted into the current element's children
        Assert.assertEquals(character, builder.getCurrentElement().children().get(0));
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("name", "publicIdentifier", "systemIdentifier");
        builder.process(doctype);

        // Assert the doctype node is inserted into the current element's children
        Assert.assertEquals(doctype, builder.getCurrentElement().children().get(0));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("element"), "", null);
        builder.insertNode(element);

        // Assert the element is inserted into the current element's children
        Assert.assertEquals(element, builder.getCurrentElement().children().get(0));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        builder.insert(comment);

        // Assert the comment is inserted into the current element's children
        Assert.assertEquals(comment, builder.getCurrentElement().children().get(0));
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("character");
        builder.insert(character);

        // Assert the character node is inserted into the current element's children
        Assert.assertEquals(character, builder.getCurrentElement().children().get(0));
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("name", "publicIdentifier", "systemIdentifier");
        builder.insert(doctype);

        // Assert the doctype node is inserted into the current element's children
        Assert.assertEquals(doctype, builder.getCurrentElement().children().get(0));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag1 = new Token.StartTag("tag1");
        builder.insert(startTag1);
        Token.StartTag startTag2 = new Token.StartTag("tag2");
        builder.insert(startTag2);
        Token.EndTag endTag1 = new Token.EndTag("tag1");
        builder.popStackToClose(endTag1);

        // Assert the first occurrence of tag1 is removed from the stack
        Assert.assertEquals(startTag2.asStartTag(), builder.getCurrentElement().asStartTag());

        // Assert there are no more occurrences of tag1 in the stack
        for (Element element : builder.getStack()) {
            Assert.assertNotEquals("tag1", element.nodeName());
        }
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<tag>text</tag>", "baseUri", new ParseErrorList());

        // Assert there is one element in the nodes list
        Assert.assertEquals(1, nodes.size());

        // Assert the element has the correct tag and text
        Element element = (Element) nodes.get(0);
        Assert.assertEquals("tag", element.tag().getName());
        Assert.assertEquals("text", element.text());
    }

}
