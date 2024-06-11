import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParse_withReaderAndBaseUri_shouldReturnDocumentObject() {
        // Typical use case
        String input = "<xml><body><div><p>Hello World!</p></div></body></xml>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("<xml>\n <body>\n  <div>\n   <p>Hello World!</p>\n  </div>\n </body>\n</xml>",
                document.toString());
    }

    @Test
    public void testParse_withInputAndBaseUri_shouldReturnDocumentObject() {
        // Typical use case
        String input = "<xml><body><div><p>Hello World!</p></div></body></xml>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("<xml>\n <body>\n  <div>\n   <p>Hello World!</p>\n  </div>\n </body>\n</xml>",
                document.toString());
    }

    @Test
    public void testProcess_withStartTag_shouldInsertElement() {
        // Typical use case
        Token.StartTag startTag = new Token.StartTag();
        startTag.setTagName("div");
        startTag.setSelfClosing(false);
        Element element = xmlTreeBuilder.insert(startTag);
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals(1, element.siblingIndex());
    }

    @Test
    public void testProcess_withEndTag_shouldPopStack() {
        // Typical use case
        Token.EndTag endTag = new Token.EndTag();
        endTag.setTagName("div");
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.stack.add(new Element("p"));
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("p", xmlTreeBuilder.stack.get(0).tagName());
    }

    @Test
    public void testProcess_withCommentToken_shouldInsertCommentNode() {
        // Typical use case
        String commentData = "This is a comment";
        Token.Comment commentToken = new Token.Comment();
        commentToken.setData(commentData);
        xmlTreeBuilder.insert(commentToken);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(commentData, childNodes.get(0).outerHtml());
    }

    @Test
    public void testProcess_withCharacterToken_shouldInsertTextNode() {
        // Typical use case
        String characterData = "Hello World!";
        Token.Character characterToken = new Token.Character();
        characterToken.setData(characterData);
        xmlTreeBuilder.insert(characterToken);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(characterData, childNodes.get(0).outerHtml());
    }

    @Test
    public void testProcess_withDoctypeToken_shouldInsertDoctypeNode() {
        // Typical use case
        String doctypeName = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.setName(doctypeName);
        doctypeToken.setPublicIdentifier(publicIdentifier);
        doctypeToken.setSystemIdentifier(systemIdentifier);
        xmlTreeBuilder.insert(doctypeToken);
        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", childNodes.get(0).outerHtml());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcess_withUnknownTokenType_shouldThrowIllegalArgumentException() {
        // Error scenario - unexpected token type
        Token.Unknown unknownToken = new Token.Unknown();
        unknownToken.setType("UnknownType");
        xmlTreeBuilder.process(unknownToken);
    }

    @Test
    public void testParseFragment_withInputFragmentAndBaseUri_shouldReturnListOfNodes() {
        // Typical use case
        String inputFragment = "<div><p>Hello World!</p></div>";
        String baseUri = "http://example.com";
        List<Node> nodeList = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new Parser(xmlTreeBuilder));
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("<div> \n <p>Hello World!</p> \n</div>", nodeList.get(0).outerHtml());
    }

    @Test
    public void testParseFragment_withInputFragmentAndContextAndBaseUri_shouldReturnListOfNodes() {
        // Typical use case
        String inputFragment = "<div><p>Hello World!</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodeList = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, new Parser(xmlTreeBuilder));
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("<div> \n <p>Hello World!</p> \n</div>", nodeList.get(0).outerHtml());
    }

}