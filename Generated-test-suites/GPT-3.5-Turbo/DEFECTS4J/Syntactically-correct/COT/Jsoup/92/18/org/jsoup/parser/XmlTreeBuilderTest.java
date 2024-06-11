
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<tag>content</tag>");
        String baseUri = "baseUri";
        Parser parser = new Parser();
        builder.initialiseParse(input, baseUri, parser);
        List<Node> stack = builder.getStack();
        Assert.assertNotNull(stack);
        Assert.assertEquals(1, stack.size());
        Document doc = builder.getDoc();
        Assert.assertNotNull(doc);
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testParseReader() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<tag>content</tag>");
        String baseUri = "baseUri";
        Document doc = builder.parse(input, baseUri);
        Assert.assertNotNull(doc);
        Element root = doc.children().first();
        Assert.assertEquals("tag", root.tagName());
        Assert.assertEquals("content", root.text());
    }

    @Test
    public void testParseString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<tag>content</tag>";
        String baseUri = "baseUri";
        Document doc = builder.parse(input, baseUri);
        Assert.assertNotNull(doc);
        Element root = doc.children().first();
        Assert.assertEquals("tag", root.tagName());
        Assert.assertEquals("content", root.text());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attr", "value");
        builder.process(startTag);
        List<Node> stack = builder.getStack();
        Assert.assertEquals(2, stack.size());
        Element element = builder.getCurrentElement();
        Assert.assertNotNull(element);
        Assert.assertEquals("tag", element.tagName());
        Assert.assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element parentElement = new Element("parent");
        Element element = new Element("tag");
        parentElement.appendChild(element);
        builder.getStack().add(parentElement);
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.process(endTag);
        List<Node> stack = builder.getStack();
        Assert.assertEquals(1, stack.size());
        Assert.assertFalse(parentElement.childNodes().contains(element));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("Hello");
        builder.process(comment);
        List<Node> stack = builder.getStack();
        Assert.assertEquals(1, stack.size());
        Comment commentNode = (Comment) stack.get(0);
        Assert.assertEquals("Hello", commentNode.getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("content");
        builder.process(character);
        List<Node> stack = builder.getStack();
        Assert.assertEquals(1, stack.size());
        TextNode textNode = (TextNode) stack.get(0);
        Assert.assertEquals("content", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "pubId", "sysId");
        doctype.setPubSysKey("pubSysKey");
        builder.process(doctype);
        List<Node> stack = builder.getStack();
        Assert.assertEquals(1, stack.size());
        DocumentType documentType = (DocumentType) stack.get(0);
        Assert.assertEquals("html", documentType.name());
        Assert.assertEquals("pubId", documentType.publicId());
        Assert.assertEquals("sysId", documentType.systemId());
        Assert.assertEquals("pubSysKey", documentType.pubSysKey());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("tag");
        builder.getStack().add(element);
        Token.StartTag startTag = new Token.StartTag("child");
        builder.insert(startTag);
        List<Node> stack = builder.getStack();
        Assert.assertEquals(2, stack.size());
        Element childElement = (Element) stack.get(1);
        Assert.assertNotNull(childElement);
        Assert.assertEquals("child", childElement.tagName());
        Assert.assertEquals(element, childElement.parent());
    }

    @Test
    public void testInsertSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.getStack().add(new Element("tag"));
        builder.insert(new Token.StartTag("child", true));
        List<Node> stack = builder.getStack();
        Assert.assertEquals(1, stack.size());
        Element childElement = (Element) stack.get(0);
        Assert.assertNotNull(childElement);
        Assert.assertTrue(childElement.tag().isSelfClosing());
    }

    @Test
    public void testInsertCommentToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("Hello");
        builder.insert(commentToken);
        List<Node> stack = builder.getStack();
        Assert.assertEquals(1, stack.size());
        Comment commentNode = (Comment) stack.get(0);
        Assert.assertEquals("Hello", commentNode.getData());
    }

    @Test
    public void testInsertCharacterToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("content");
        builder.insert(characterToken);
        List<Node> stack = builder.getStack();
        Assert.assertEquals(1, stack.size());
        TextNode textNode = (TextNode) stack.get(0);
        Assert.assertEquals("content", textNode.getWholeText());
    }

    @Test
    public void testInsertDoctypeToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "pubId", "sysId");
        doctypeToken.setPubSysKey("pubSysKey");
        builder.insert(doctypeToken);
        List<Node> stack = builder.getStack();
        Assert.assertEquals(1, stack.size());
        DocumentType documentType = (DocumentType) stack.get(0);
        Assert.assertEquals("html", documentType.name());
        Assert.assertEquals("pubId", documentType.publicId());
        Assert.assertEquals("sysId", documentType.systemId());
        Assert.assertEquals("pubSysKey", documentType.pubSysKey());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");
        parentElement.appendChild(childElement);
        builder.getStack().add(parentElement);
        builder.getStack().add(childElement);
        Token.EndTag endTag = new Token.EndTag("child");
        builder.popStackToClose(endTag);
        List<Node> stack = builder.getStack();
        Assert.assertEquals(1, stack.size());
        Assert.assertFalse(parentElement.childNodes().contains(childElement));
    }

    @Test
    public void testParseFragmentString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>content</tag>";
        String baseUri = "baseUri";
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, new Parser());
        Assert.assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        Assert.assertNotNull(root);
        Assert.assertEquals("tag", root.tagName());
        Assert.assertEquals("content", root.text());
    }

    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>content</tag>";
        Element context = new Element("context");
        String baseUri = "baseUri";
        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, new Parser());
        Assert.assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        Assert.assertNotNull(root);
        Assert.assertEquals("tag", root.tagName());
        Assert.assertEquals("content", root.text());
    }
}
