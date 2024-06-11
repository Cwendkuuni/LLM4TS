import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String xmlString = "<root><child>Hello</child></root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com";

        Document doc = new XmlTreeBuilder().parse(input, baseUri);

        Element root = doc.getRootElement();
        Assert.assertEquals("root", root.tagName());
        Assert.assertEquals(1, root.numChildren());

        Element child = root.child(0);
        Assert.assertEquals("child", child.tagName());
        Assert.assertEquals("Hello", child.text());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String xmlString = "<root><child>Hello</child></root>";
        String baseUri = "http://example.com";

        Document doc = new XmlTreeBuilder().parse(xmlString, baseUri);

        Element root = doc.getRootElement();
        Assert.assertEquals("root", root.tagName());
        Assert.assertEquals(1, root.numChildren());

        Element child = root.child(0);
        Assert.assertEquals("child", child.tagName());
        Assert.assertEquals("Hello", child.text());
    }

    @Test
    public void testInitialiseParse() {
        String xmlString = "<root><child>Hello</child></root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        Assert.assertNotNull(xmlTreeBuilder.doc);
        Assert.assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
        Assert.assertEquals("xml", xmlTreeBuilder.doc.outputSettings().syntax().name());
    }

    @Test
    public void testProcessStartTag() {
        String xmlString = "<root><child/></root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();

        Element root = xmlTreeBuilder.doc.getRootElement();
        Assert.assertEquals("root", root.tagName());
        Assert.assertEquals(1, root.numChildren());

        Element child = root.child(0);
        Assert.assertEquals("child", child.tagName());
        Assert.assertFalse(child.hasText());
    }

    @Test
    public void testProcessEndTag() {
        String xmlString = "<root><child><grandchild/></child></root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();

        Element root = xmlTreeBuilder.doc.getRootElement();
        Assert.assertEquals("root", root.tagName());
        Assert.assertEquals(1, root.numChildren());

        Element child = root.child(0);
        Assert.assertEquals("child", child.tagName());
        Assert.assertEquals(1, child.numChildren());

        Element grandchild = child.child(0);
        Assert.assertEquals("grandchild", grandchild.tagName());
        Assert.assertFalse(grandchild.hasText());
        Assert.assertEquals(1, root.numChildren());

        xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(root, childNodes.get(0));
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        String xmlString = "<root><child/><123>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();

        Validate.fail("Error expected but not thrown.");
    }

    @Test
    public void testInsertNode() {
        Element parent = new Element("parent");
        Node child = new TextNode("child");

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insertNode(parent);
        xmlTreeBuilder.insertNode(child);

        parent.appendChild(child);
        Assert.assertEquals(1, parent.numChildren());
    }

    @Test
    public void testInsertComment() {
        String xmlString = "<!-- This is a comment -->";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();

        Comment comment = xmlTreeBuilder.doc.getElementsByTag("comment").get(0);
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        String xmlString = "&lt;Hello&gt; World &amp; &apos; &quot;";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();

        TextNode textNode1 = (TextNode) xmlTreeBuilder.doc.getElementsByTag("text").get(0);
        Assert.assertEquals("&lt;Hello&gt; ", textNode1.text());

        TextNode textNode2 = (TextNode) xmlTreeBuilder.doc.getElementsByTag("text").get(1);
        Assert.assertEquals(" World ", textNode2.text());

        TextNode textNode3 = (TextNode) xmlTreeBuilder.doc.getElementsByTag("text").get(2);
        Assert.assertEquals("&amp; ", textNode3.text());

        TextNode textNode4 = (TextNode) xmlTreeBuilder.doc.getElementsByTag("text").get(3);
        Assert.assertEquals(" &apos; ", textNode4.text());

        TextNode textNode5 = (TextNode) xmlTreeBuilder.doc.getElementsByTag("text").get(4);
        Assert.assertEquals(" &quot; ", textNode5.text());
    }

    @Test
    public void testInsertDocumentType() {
        String xmlString = "<!DOCTYPE root [<!ENTITY entity SYSTEM \"file:///path/to/system\">]>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();

        DocumentType documentType = (DocumentType) xmlTreeBuilder.doc.getElementsByTag("document-type").get(0);
        Assert.assertEquals("root", documentType.getName());
        Assert.assertEquals("file:///path/to/system", documentType.getSystemIdentifier());
    }

    @Test(expected = AssertionError.class)
    public void testPopStackToCloseNonExistentTag() {
        String xmlString = "<root><child></child></root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();

        xmlTreeBuilder.popStackToClose(new Token.EndTag("grandchild"));
        Validate.fail("Error expected but not thrown.");
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><child>Hello</child></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, baseUri, errors, settings);

        Document doc = Jsoup.parseBodyFragment(xmlFragment);
        Element root = doc.getRootElement();
        Assert.assertEquals(1, root.numChildren());

        Element child = root.child(0);
        Assert.assertEquals("child", child.tagName());
        Assert.assertEquals("Hello", child.text());

        Assert.assertEquals(root, nodes.get(0));
    }
}