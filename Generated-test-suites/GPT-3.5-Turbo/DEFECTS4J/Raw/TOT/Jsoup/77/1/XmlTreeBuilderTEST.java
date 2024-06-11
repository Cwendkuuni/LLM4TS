import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XmlTreeBuilderTest {
  
    private XmlTreeBuilder xmlTreeBuilder;
  
    @BeforeEach
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
  
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
  
    @Test
    public void testParseReaderString() {
        String input = "<root><element>text</element></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        assertEquals("<root><element>text</element></root>", document.html());
        assertEquals(baseUri, document.baseUri());
    }
  
    @Test
    public void testParseStringString() {
        String input = "<root><element>text</element></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertEquals("<root><element>text</element></root>", document.html());
        assertEquals(baseUri, document.baseUri());
    }
  
    @Test
    public void testInitialiseParse() {
        String input = "<root><element>text</element></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
      
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, errors, settings);
      
        assertEquals(ParseErrorList.noTracking(), xmlTreeBuilder.getErrors());
        assertEquals(settings, xmlTreeBuilder.getSettings());
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(baseUri, xmlTreeBuilder.getStack().get(0).baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getStack().get(0).outputSettings().syntax());
    }
  
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", "div", new Attributes());
        xmlTreeBuilder.getStack().add(new Element(Tag.valueOf("parent"), "", ParseSettings.preserveCase));
        Element result = xmlTreeBuilder.insert(startTag);
        assertEquals("div", result.nodeName());
        assertEquals(1, xmlTreeBuilder.getStack().size());
    }
  
    @Test
    public void testProcessEndTag() {
        Element parentElement = new Element(Tag.valueOf("parent"), "", ParseSettings.preserveCase);
        Element childElement = new Element(Tag.valueOf("child"), "", ParseSettings.preserveCase);
        xmlTreeBuilder.getStack().add(parentElement);
        xmlTreeBuilder.getStack().add(childElement);
        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(parentElement, xmlTreeBuilder.getStack().get(0));
    }
  
    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("test comment");
        xmlTreeBuilder.insert(comment);
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertEquals("<!--test comment-->", xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml());
    }
  
    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("test character");
        xmlTreeBuilder.insert(character);
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertEquals("test character", xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml());
    }
  
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertEquals("<!doctype html>", xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml());
    }
  
    @Test
    public void testInsertNode() {
        Document document = Jsoup.parse("<root><element>text</element></root>");
        Element root = document.selectFirst("root");
        Element element = document.selectFirst("element");
      
        xmlTreeBuilder.setCurrentElement(root);
        xmlTreeBuilder.insertNode(element);
      
        assertEquals(root, xmlTreeBuilder.getCurrentElement());
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertEquals(element, xmlTreeBuilder.getCurrentElement().childNode(0));
    }
  
    @Test
    public void testParseFragment() {
        String inputFragment = "<root><element>text</element></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
      
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
      
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("root", ((Element) nodes.get(0)).nodeName());
    }
  
    @Test
    public void testParseFragmentNoInput() {
        String inputFragment = "";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
      
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
      
        assertEquals(0, nodes.size());
    }
  
    @Test
    public void testParseFragmentInvalidInput() {
        String inputFragment = "<root><element>text</element>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
      
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
      
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("root", ((Element) nodes.get(0)).nodeName());
      
        assertEquals(1, errors.size());
        assertEquals("Unexpected Token : Invalid tokenization. EOF expected.", errors.get(0).getErrorMessage());
    }
}