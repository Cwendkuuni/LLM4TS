import org.junit.Test;
import org.junit.Assert;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import java.io.Reader;
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
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<html></html>");
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<html></html>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<html></html>");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Assert.assertEquals(true, xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("div", null);
        boolean result = xmlTreeBuilder.process(token);
        Assert.assertEquals(true, result);
        Assert.assertEquals("div", xmlTreeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element("div"));
        boolean result = xmlTreeBuilder.process(token);
        Assert.assertEquals(true, result);
        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Comment("test comment");
        boolean result = xmlTreeBuilder.process(token);
        Assert.assertEquals(true, result);
        Assert.assertEquals("#comment", xmlTreeBuilder.currentElement().nodeName());
        Assert.assertEquals("test comment", xmlTreeBuilder.currentElement().data());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Character("test character");
        boolean result = xmlTreeBuilder.process(token);
        Assert.assertEquals(true, result);
        Assert.assertEquals("#text", xmlTreeBuilder.currentElement().nodeName());
        Assert.assertEquals("test character", xmlTreeBuilder.currentElement().data());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Doctype("html", "", "");
        boolean result = xmlTreeBuilder.process(token);
        Assert.assertEquals(true, result);
        Assert.assertEquals("#doctype", xmlTreeBuilder.currentElement().nodeName());
        Assert.assertEquals("html", xmlTreeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new Element("p");
        xmlTreeBuilder.insertNode(node);
        Assert.assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div", null);
        Element element = xmlTreeBuilder.insert(startTag);
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals(element, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
    }
    
    @Test
    public void testInsertSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("img", null);
        startTag.selfClosing = true;
        Element element = xmlTreeBuilder.insert(startTag);
        Assert.assertEquals("img", element.tagName());
        Assert.assertTrue(xmlTreeBuilder.currentElement().tag().isKnownTag());
        Assert.assertTrue(xmlTreeBuilder.currentElement().tag().isSelfClosing());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test comment");
        xmlTreeBuilder.insert(commentToken);
        Assert.assertEquals("#comment", xmlTreeBuilder.currentElement().nodeName());
        Assert.assertEquals("test comment", xmlTreeBuilder.currentElement().data());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("test character");
        xmlTreeBuilder.insert(characterToken);
        Assert.assertEquals("#text", xmlTreeBuilder.currentElement().nodeName());
        Assert.assertEquals("test character", xmlTreeBuilder.currentElement().data());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctypeToken);
        Assert.assertEquals("#doctype", xmlTreeBuilder.currentElement().nodeName());
        Assert.assertEquals("html", xmlTreeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div></div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("div", nodes.get(0).nodeName());
    }
    
}