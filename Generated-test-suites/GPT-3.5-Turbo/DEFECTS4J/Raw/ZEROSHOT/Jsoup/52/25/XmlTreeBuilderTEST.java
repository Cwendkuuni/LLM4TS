import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Assert.assertEquals("http://example.com", xmlTreeBuilder.baseUri);
        Assert.assertEquals(errors, xmlTreeBuilder.errors);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        
        xmlTreeBuilder.process(startTag);

        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("tag", xmlTreeBuilder.currentElement().tagName());
        Assert.assertEquals(xmlTreeBuilder.currentElement(), xmlTreeBuilder.doc.childNodes().get(0));
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag");
        Element element = new Element(Tag.valueOf("tag"), "", new Attributes());

        xmlTreeBuilder.stack.add(element);

        xmlTreeBuilder.process(endTag);

        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment();
        
        xmlTreeBuilder.process(comment);

        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character();
        
        xmlTreeBuilder.process(character);

        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        xmlTreeBuilder.process(doctype);

        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr1", "value1");
        
        xmlTreeBuilder.stack.add(xmlTreeBuilder.doc);

        Element element = xmlTreeBuilder.insert(startTag);

        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
        Assert.assertEquals(1, element.attributes().size());
        Assert.assertEquals("value1", element.attributes().get("attr1"));
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment();
        
        xmlTreeBuilder.insert(commentToken);

        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character();
        
        xmlTreeBuilder.insert(characterToken);

        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        xmlTreeBuilder.insert(doctype);

        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag");

        Element element = new Element(Tag.valueOf("tag"), "", new Attributes());
        xmlTreeBuilder.stack.add(element);

        xmlTreeBuilder.popStackToClose(endTag);

        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag></tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("tag", ((Element) nodes.get(0)).tagName());
    }
}