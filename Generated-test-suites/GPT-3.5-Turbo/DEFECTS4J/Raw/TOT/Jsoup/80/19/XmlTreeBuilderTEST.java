import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderBaseUri() {
        String input = "<html><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(input), baseUri);

        Assert.assertEquals("html", doc.nodeName());
    }

    @Test
    public void testParseStringBaseUri() {
        String input = "<html><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(input, baseUri);

        Assert.assertEquals("html", doc.nodeName());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, errors, settings);

        Assert.assertEquals(settings, xmlTreeBuilder.settings);
        Assert.assertEquals(baseUri, xmlTreeBuilder.baseUri);
        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
        Assert.assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.currentElement());
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("p");

        Element el = xmlTreeBuilder.insert(startTag);

        Assert.assertEquals("p", el.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("p");

        xmlTreeBuilder.stack.add(new Element("p"));
        xmlTreeBuilder.stack.add(new Element("div"));

        xmlTreeBuilder.popStackToClose(endTag);

        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("div", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("<!-- Comment -->");

        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();

        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(NodeType.COMMENT, childNodes.get(0).nodeType());
        Assert.assertEquals("<!-- Comment -->", childNodes.get(0).toString());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character token = new Token.Character("Hello World!");

        xmlTreeBuilder.insert(token);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();

        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(NodeType.TEXT, childNodes.get(0).nodeType());
        Assert.assertEquals("Hello World!", childNodes.get(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");

        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();

        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(NodeType.DOCTYPE, childNodes.get(0).nodeType());
        Assert.assertEquals("<!DOCTYPE html>", childNodes.get(0).toString());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals(NodeType.ELEMENT, nodes.get(0).nodeType());
        Assert.assertEquals("p", ((Element) nodes.get(0)).nodeName());
    }

}