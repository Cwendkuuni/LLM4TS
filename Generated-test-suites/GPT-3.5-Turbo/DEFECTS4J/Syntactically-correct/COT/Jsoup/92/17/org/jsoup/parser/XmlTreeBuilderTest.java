
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("input");
        String baseUri = "baseUri";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        Assert.assertEquals("baseUri", baseUri);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("input");
        String baseUri = "baseUri";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "input";
        String baseUri = "baseUri";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("name");
        Tag tag = Tag.valueOf(startTag.name(), xmlTreeBuilder.defaultSettings());
        startTag.isSelfClosing(false);
        startTag.attributes("");
        Element element = new Element(tag, "baseUri", xmlTreeBuilder.defaultSettings().normalizeAttributes(startTag.attributes));
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.insert(startTag);
        Assert.assertEquals(element, xmlTreeBuilder.insert(startTag));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("tagName");
        xmlTreeBuilder.stack.add(new Element("tagName", "baseUri"));
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment();
        comment.data("data");
        xmlTreeBuilder.insert(comment);
        Assert.assertEquals(new Comment("data"), xmlTreeBuilder.insert(comment));
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character();
        character.data("data");
        xmlTreeBuilder.insert(character);
        Assert.assertEquals(new TextNode("data"), xmlTreeBuilder.insert(character));
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("name");
        doctype.publicIdentifier("publicIdentifier");
        doctype.systemIdentifier("systemIdentifier");
        doctype.pubSysKey("pubSysKey");
        xmlTreeBuilder.insert(doctype);
        Assert.assertEquals(new DocumentType("name", "publicIdentifier", "systemIdentifier"), xmlTreeBuilder.insert(doctype));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new Node();
        xmlTreeBuilder.insertNode(node);
        Assert.assertEquals(node, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("name");
        Tag tag = Tag.valueOf(startTag.name(), xmlTreeBuilder.defaultSettings());
        startTag.isSelfClosing(false);
        startTag.attributes("");
        Element element = new Element(tag, "baseUri", xmlTreeBuilder.defaultSettings().normalizeAttributes(startTag.attributes));
        xmlTreeBuilder.insertNode(element);
        Assert.assertEquals(element, xmlTreeBuilder.insert(startTag));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "inputFragment";
        String baseUri = "baseUri";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        Assert.assertNotNull(nodes);
    }

    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "inputFragment";
        Element context = new Element("context");
        String baseUri = "baseUri";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        Assert.assertNotNull(nodes);
    }
}

