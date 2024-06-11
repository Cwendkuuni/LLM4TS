import org.jsoup.Jsoup;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals(Reader.class, xmlTreeBuilder.input.getClass());
        Assert.assertEquals(baseUri, xmlTreeBuilder.baseUri);
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testParseReaderBaseUri() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertEquals("xml", doc.nodeName());
    }

    @Test
    public void testParseStringBaseUri() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertEquals("xml", doc.nodeName());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("xml");
        Element el = xmlTreeBuilder.insert(startTag);
        Assert.assertEquals("xml", el.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("xml");
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        Assert.assertEquals("This is a comment", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is some text");
        xmlTreeBuilder.insert(characterToken);
        Assert.assertEquals("This is some text", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        xmlTreeBuilder.insert(doctype);
        DocumentType docTypeNode = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertEquals("html", docTypeNode.nodeName());
        Assert.assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", docTypeNode.getPublicIdentifier());
        Assert.assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docTypeNode.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("xml");
        xmlTreeBuilder.stack.add(new Element("xml"));
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        Assert.assertEquals("div", nodes.get(0).nodeName());
    }

    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "<div>Hello World</div>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        Assert.assertEquals("div", nodes.get(0).nodeName());
    }

}