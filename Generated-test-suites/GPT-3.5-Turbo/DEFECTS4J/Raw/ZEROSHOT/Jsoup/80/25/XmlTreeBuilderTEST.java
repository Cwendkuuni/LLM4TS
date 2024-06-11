package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private String baseUri;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        baseUri = "http://example.com/";
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParseReaderString() {
        String input = "<root><child>Content</child></root>";
        Reader reader = new StringReader(input);
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("<root>\n" +
                " <child>\n" +
                "  Content\n" +
                " </child>\n" +
                "</root>", document.outerHtml());
    }

    @Test
    public void testParseStringString() {
        String input = "<root><child>Content</child></root>";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("<root>\n" +
                " <child>\n" +
                "  Content\n" +
                " </child>\n" +
                "</root>", document.outerHtml());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child>Content</child></root>";
        Reader reader = new StringReader(input);
        TestXmlTreeBuilder testXmlTreeBuilder = new TestXmlTreeBuilder();
        testXmlTreeBuilder.initialiseParse(reader, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        List<Node> stack = testXmlTreeBuilder.getStack();
        Document doc = testXmlTreeBuilder.getDoc();

        Assert.assertNotNull(stack);
        Assert.assertEquals(1, stack.size());
        Assert.assertEquals(doc, stack.get(0));

        Document.OutputSettings.Syntax syntax = doc.outputSettings().syntax();
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, syntax);
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("child");
        startTag.attributes(new Attributes());

        TestXmlTreeBuilder testXmlTreeBuilder = new TestXmlTreeBuilder();
        Element element = testXmlTreeBuilder.insert(startTag);

        Assert.assertNotNull(element);
        Assert.assertEquals("child", element.tagName());
        Assert.assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("child");

        TestXmlTreeBuilder testXmlTreeBuilder = new TestXmlTreeBuilder();
        testXmlTreeBuilder.getStack().add(new Element("child"));

        testXmlTreeBuilder.popStackToClose(endTag);

        List<Element> stack = testXmlTreeBuilder.getStack();
        Assert.assertEquals(0, stack.size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("Comment");

        TestXmlTreeBuilder testXmlTreeBuilder = new TestXmlTreeBuilder();
        testXmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = testXmlTreeBuilder.getDoc().childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals("Comment", childNodes.get(0).outerHtml());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character();
        character.data("Content");

        TestXmlTreeBuilder testXmlTreeBuilder = new TestXmlTreeBuilder();
        testXmlTreeBuilder.insert(character);

        List<Node> childNodes = testXmlTreeBuilder.getDoc().childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals("Content", childNodes.get(0).outerHtml());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");

        TestXmlTreeBuilder testXmlTreeBuilder = new TestXmlTreeBuilder();
        testXmlTreeBuilder.insert(doctype);

        List<Node> childNodes = testXmlTreeBuilder.getDoc().childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals("<!DOCTYPE html>", childNodes.get(0).outerHtml());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("child");
        TestXmlTreeBuilder testXmlTreeBuilder = new TestXmlTreeBuilder();
        testXmlTreeBuilder.insertNode(element);

        Assert.assertEquals(1, testXmlTreeBuilder.getStack().size());
        Assert.assertEquals(element, testXmlTreeBuilder.getStack().get(0).child(0));
    }

    @Test
    public void testInsertFragment() {
        String inputFragment = "<child>Content</child>";
        TestXmlTreeBuilder testXmlTreeBuilder = new TestXmlTreeBuilder();
        List<Node> childNodes = testXmlTreeBuilder.parseFragment(inputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals("<child>Content</child>", childNodes.get(0).outerHtml());
    }

    private class TestXmlTreeBuilder extends XmlTreeBuilder {

        public List<Element> getStack() {
            return stack;
        }

        public Document getDoc() {
            return doc;
        }
    }
}