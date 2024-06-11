import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Character;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Tag;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private String input;
    private String baseUri;
    
    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        input = "<test>xmlTreeBuilder test</test>";
        baseUri = "http://example.com";
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader(input);
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testInitialiseParse() {
        Reader reader = new StringReader(input);
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        
        Assert.assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("test", "<test>", new ParseSettings());
        Element element = xmlTreeBuilder.insert(startTag);
        
        Assert.assertNotNull(element);
        Assert.assertEquals("test", element.nodeName());
        Assert.assertEquals(baseUri, element.baseUri());
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("test comment", "<!-- test comment -->");
        xmlTreeBuilder.insert(commentToken);
        
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        
        Assert.assertEquals(1, childNodes.size());
        Assert.assertTrue(childNodes.get(0) instanceof Comment);
        Assert.assertEquals("test comment", ((Comment) childNodes.get(0)).getData());
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("test character", false);
        xmlTreeBuilder.insert(characterToken);
        
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        
        Assert.assertEquals(1, childNodes.size());
        Assert.assertTrue(childNodes.get(0) instanceof TextNode);
        Assert.assertEquals("test character", ((TextNode) childNodes.get(0)).getWholeText());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);
        
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        
        Assert.assertEquals(1, childNodes.size());
        Assert.assertTrue(childNodes.get(0) instanceof Doctype);
        Assert.assertEquals("html", ((Doctype) childNodes.get(0)).getName());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.StartTag startTag = new Token.StartTag("test", "<test>", new ParseSettings());
        xmlTreeBuilder.insert(startTag);
        xmlTreeBuilder.stack.add(xmlTreeBuilder.currentElement());
        
        Token.EndTag endTag = new Token.EndTag("test", "</test>");
        xmlTreeBuilder.popStackToClose(endTag);
        
        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        String fragment = "<test>xmlTreeBuilder test</test>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> childNodes = xmlTreeBuilder.parseFragment(fragment, baseUri, errors, settings);
        
        Assert.assertEquals(1, childNodes.size());
        Assert.assertTrue(childNodes.get(0) instanceof Element);
        Assert.assertEquals("test", ((Element) childNodes.get(0)).nodeName());
    }
}