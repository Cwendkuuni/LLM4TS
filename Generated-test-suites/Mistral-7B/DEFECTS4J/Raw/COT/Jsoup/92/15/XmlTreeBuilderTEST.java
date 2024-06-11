import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        Assert.assertNotNull(xmlTreeBuilder.doc);
        Assert.assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><child/></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(new StringReader(xml), baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(xml, document.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInvalidToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(-1, "InvalidToken");
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("root");
        Element root = xmlTreeBuilder.insert(startTag);
        Assert.assertNotNull(root);
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("<!-- comment -->");
        Comment comment = xmlTreeBuilder.insert(commentToken);
        Assert.assertNotNull(comment);
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('A');
        TextNode textNode = (TextNode) xmlTreeBuilder.insert(characterToken);
        Assert.assertEquals("A", textNode.text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String doctypeName = "HTML";
        String publicId = "public id";
        String systemId = "system id";
        Token doctype = new Token.Doctype(doctypeName, publicId, systemId);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(doctype);
        Assert.assertNotNull(documentType);
        Assert.assertEquals(doctypeName, documentType.nodeName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String baseUri = "http://example.com";
        Element root = new Element("root");
        Element child = new Element("child");
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(child);
        Token endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><child/></root>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, parser);
        Assert.assertNotNull(nodes);
        Assert.assertEquals(2, nodes.size());
    }

    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><child/></root>";
        Element context = new Element("context");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, context, baseUri, parser);
        Assert.assertNotNull(nodes);
        Assert.assertEquals(2, nodes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentWithInvalidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "InvalidInput";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.parseFragment(xmlFragment, baseUri, parser);
    }
}