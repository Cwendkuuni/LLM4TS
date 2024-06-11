package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Tag;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilder() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertNotNull(xmlTreeBuilder);
    }

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertTrue(settings.isPreserveCase());
    }

    @Test
    public void testParse() {
        String input = "<xml><tag1 attr1=\"value1\">Content1</tag1><tag2 attr2=\"value2\"/></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(input, "baseUri");
        assertNotNull(doc);

        Element root = doc.rootElement();
        List<Element> children = root.children();
        assertEquals(2, children.size());

        Element tag1 = children.get(0);
        assertEquals("tag1", tag1.tagName());
        List<Attribute> attributes = tag1.attributes();
        assertEquals(1, attributes.size());
        assertEquals("value1", attributes.get(0).getValue());
        assertEquals("attr1", attributes.get(0).getKey());

        Element tag2 = children.get(1);
        assertEquals("tag2", tag2.tagName());
        assertNotNull(tag2.children());
        assertEquals(0, tag2.children().size());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml>";
        xmlTreeBuilder.initialiseParse(input, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(Document.class, xmlTreeBuilder.doc.getClass());
    }

    @Test
    public void testProcessTokenStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag attr=\"value\">Content</tag>";
        Tokenizer tokenizer = new Tokenizer(input);
        Token token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        Element element = xmlTreeBuilder.stack.peek();
        assertEquals("tag", element.tagName());
        List<Attribute> attributes = element.attributes();
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get(0).getValue());
        assertEquals("attr", attributes.get(0).getKey());
    }

    @Test
    public void testProcessTokenEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag><subtag/></tag>";
        Tokenizer tokenizer = new Tokenizer(input);
        Token startTagToken = tokenizer.nextToken();
        Token endTagToken = tokenizer.nextToken();

        xmlTreeBuilder.process(startTagToken);
        xmlTreeBuilder.process(tokenizer.nextToken()); // skip character or whitespace
        xmlTreeBuilder.process(endTagToken);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessTokenComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!-- Comment -->";
        Tokenizer tokenizer = new Tokenizer(input);
        Token commentToken = tokenizer.nextToken();

        xmlTreeBuilder.process(commentToken);

        Comment comment = (Comment) xmlTreeBuilder.stack.peek();
        assertEquals("Comment", comment.text());
    }

    @Test
    public void testProcessTokenCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml>Test</xml>";
        Tokenizer tokenizer = new Tokenizer(input);
        Token characterToken = tokenizer.nextToken();

        xmlTreeBuilder.process(characterToken);

        TextNode textNode = (TextNode) xmlTreeBuilder.stack.peek();
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testProcessTokenDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!DOCTYPE xml SYSTEM \"xml.dtd\">";
        Tokenizer tokenizer = new Tokenizer(input);
        Token doctypeToken = tokenizer.nextToken();

        xmlTreeBuilder.process(doctypeToken);

        DocumentType documentType = (DocumentType) xmlTreeBuilder.stack.peek();
        assertEquals("xml", documentType.tagName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag1><tag2></tag2></tag1></xml>";
        Tokenizer tokenizer = new Tokenizer(input);

        // The following tokens represent the input:
        // [StartTag: tag1]
        // [StartTag: tag2]
        // [EndTag: tag2]
        // [EndTag: tag1]

        xmlTreeBuilder.process(tokenizer.nextToken()); // StartTag: tag1
        xmlTreeBuilder.process(tokenizer.nextToken()); // StartTag: tag2
        xmlTreeBuilder.process(tokenizer.nextToken()); // Character or whitespace
        xmlTreeBuilder.process(tokenizer.nextToken()); // EndTag: tag2

        xmlTreeBuilder.popStackToClose(tokenizer.nextToken().asEndTag()); // EndTag: tag1

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<tag attr=\"value\">Content</tag>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(1, nodes.size());
        Element node = (Element) nodes.get(0);
        assertEquals("tag", node.tagName());
        List<Attribute> attributes = node.attributes();
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get(0).getValue());
        assertEquals("attr", attributes.get(0).getKey());
    }
}