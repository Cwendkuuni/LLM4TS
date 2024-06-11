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
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals("preserveCase", settings.toString());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child></child></root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com/";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("root", doc.tagName());
        Element root = doc.getRootElement();
        Assert.assertEquals("child", root.child(0).tagName());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child></child></root>";
        String baseUri = "http://example.com/";
        Document doc = xmlTreeBuilder.parse(xmlString, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("root", doc.tagName());
        Element root = doc.getRootElement();
        Assert.assertEquals("child", root.child(0).tagName());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<root></root>");
        String baseUri = "http://example.com/";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Assert.assertNotNull(xmlTreeBuilder.doc);
        Assert.assertEquals("root", xmlTreeBuilder.doc.tagName());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child></child></root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com/";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();
        Element root = xmlTreeBuilder.doc.getRootElement();
        Element child = root.child(0);
        Assert.assertEquals("root", root.tagName());
        Assert.assertEquals("child", child.tagName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child></child></root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com/";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();
        Element root = xmlTreeBuilder.doc.getRootElement();
        Element child = root.child(0);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
        List<Node> nodeList = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals(root, nodeList.get(0));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = Jsoup.parse("<root><child></child></root>", "http://example.com/");
        Element root = doc.getRootElement();
        Node node = new Element("newTag", null);
        xmlTreeBuilder.insertNode(node);
        root.appendChild(node);
        Assert.assertEquals(2, root.childNodes().size());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<!-- Test Comment -->";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com/";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();
        List<Node> nodeList = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals(nodeList.get(0).getClass(), Comment.class);
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><![CDATA[Test Character]]></root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com/";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();
        List<Node> nodeList = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals(nodeList.get(0).getClass(), CDataNode.class);
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<!DOCTYPE root SYSTEM \"root.dtd\">";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com/";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();
        List<Node> nodeList = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals(nodeList.get(0).getClass(), DocumentType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnexpectedTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://example.com/";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        xmlTreeBuilder.runParser();
        Token token = xmlTreeBuilder.parseNext();
        xmlTreeBuilder.process(token);
        Validate.fail("Unexpected test case execution.");
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><child></child></root>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodeList = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, errors, settings);
        Assert.assertEquals(2, nodeList.size());
        Assert.assertEquals(nodeList.get(0).getClass(), Element.class);
        Assert.assertEquals(nodeList.get(1).getClass(), Element.class);
    }
}