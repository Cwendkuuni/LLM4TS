package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlInput = "<root><element attr=\"value\">Content</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "");

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.child(0).text());
    }

    @Test
    public void testProcessStartTag() {
        String xmlInput = "<root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        TreeBuilder.TokenStream tokens = new TreeBuilder.TokenStream();
        XmlTreeBuilder.XmlTreeParser parser = new XmlTreeBuilder.XmlTreeParser(builder, tokens);
        parser.parseComplete(Jsoup.parse(xmlInput, ""));

        assertEquals(1, tokens.size());

        XmlTreeBuilder.Token token = tokens.get(0);
        assertEquals(XmlTreeBuilder.Token.StartTag.class, token.getClass());

        XmlTreeBuilder.Token.StartTag startTag = token.asStartTag();
        assertEquals("element", startTag.name());
        assertEquals(1, startTag.children().size());
        assertEquals("attr", startTag.attributes().get("value").getName());
        assertEquals("value", startTag.attributes().get("value").getValue());
    }

    @Test
    public void testProcessEndTag() {
        String xmlInput = "<root><element><subElement/></element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        TreeBuilder.TokenStream tokens = new TreeBuilder.TokenStream();
        XmlTreeBuilder.XmlTreeParser parser = new XmlTreeBuilder.XmlTreeParser(builder, tokens);
        parser.parseComplete(Jsoup.parse(xmlInput, ""));

        assertEquals(4, tokens.size());

        XmlTreeBuilder.Token startTagToken = tokens.get(0);
        XmlTreeBuilder.Token.StartTag startTag = startTagToken.asStartTag();
        assertEquals("root", startTag.name());

        XmlTreeBuilder.Token endTagToken = tokens.get(2);
        XmlTreeBuilder.Token.EndTag endTag = endTagToken.asEndTag();
        assertEquals("element", endTag.name());

        XmlTreeBuilder.Token characterToken = tokens.get(3);
        XmlTreeBuilder.Token.Character character = characterToken.asCharacter();
        assertEquals("", character.data());
    }

    @Test
    public void testProcessComment() {
        String xmlInput = "<!-- This is a comment -->";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        TreeBuilder.TokenStream tokens = new TreeBuilder.TokenStream();
        XmlTreeBuilder.XmlTreeParser parser = new XmlTreeBuilder.XmlTreeParser(builder, tokens);
        parser.parseComplete(Jsoup.parse(xmlInput, ""));

        assertEquals(1, tokens.size());

        XmlTreeBuilder.Token commentToken = tokens.get(0);
        assertEquals(XmlTreeBuilder.Token.Comment.class, commentToken.getClass());

        XmlTreeBuilder.Token.Comment comment = commentToken.asComment();
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        String xmlInput = "<root>&copy; 2023</root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        TreeBuilder.TokenStream tokens = new TreeBuilder.TokenStream();
        XmlTreeBuilder.XmlTreeParser parser = new XmlTreeBuilder.XmlTreeParser(builder, tokens);
        parser.parseComplete(Jsoup.parse(xmlInput, ""));

        assertEquals(1, tokens.size());

        XmlTreeBuilder.Token characterToken = tokens.get(0);
        assertEquals(XmlTreeBuilder.Token.Character.class, characterToken.getClass());

        XmlTreeBuilder.Token.Character character = characterToken.asCharacter();
        assertEquals("&copy; 2023", character.data());
    }

    @Test
    public void testProcessDoctype() {
        String xmlInput = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        TreeBuilder.TokenStream tokens = new TreeBuilder.TokenStream();
        XmlTreeBuilder.XmlTreeParser parser = new XmlTreeBuilder.XmlTreeParser(builder, tokens);
        parser.parseComplete(Jsoup.parse(xmlInput, ""));

        assertEquals(1, tokens.size());

        XmlTreeBuilder.Token doctypeToken = tokens.get(0);
        assertEquals(XmlTreeBuilder.Token.Doctype.class, doctypeToken.getClass());

        XmlTreeBuilder.Token.Doctype doctype = doctypeToken.asDoctype();
        assertEquals("root", doctype.name());
        assertEquals("root", doctype.publicId());
        assertEquals(null, doctype.systemId());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.insertNode(root);
        assertEquals(1, builder.stack.size());
        assertEquals(root, builder.stack.get(0));
    }

    @Test
    public void testInsertElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlInput = "<root><element attr=\"value\">Content</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "");
        Element root = xmlDoc.rootElement();
        Element element = root.child(0);

        builder.insert(element);
        assertEquals(1, builder.stack.size());
        assertEquals(element, builder.stack.get(0));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlInput = "<!-- This is a comment -->";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "");
        Comment comment = xmlDoc.child(0);

        builder.insert(comment);
        assertEquals(1, builder.stack.size());
        assertEquals(comment, builder.stack.get(0));
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlInput = "<root>&copy; 2023</root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "");
        TextNode character = xmlDoc.child(0);

        builder.insert(character);
        assertEquals(1, builder.stack.size());
        assertEquals(character, builder.stack.get(0));
    }

    @Test
    public void testInsertCDataNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlInput = "<root><![CDATA[Content]]></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "");
        CDataNode cdata = xmlDoc.child(0);

        builder.insert(cdata);
        assertEquals(1, builder.stack.size());
        assertEquals(cdata, builder.stack.get(0));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        builder.stack.add(root);
        builder.stack.add(element1);
        builder.stack.add(element2);

        builder.popStackToClose(element2.tagName());
        assertEquals(2, builder.stack.size());
        assertEquals(element1, builder.stack.get(0));
        assertEquals(root, builder.stack.get(1));
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Content</element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.child(0).text());
    }
}