package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parse.ParseSettings;
import org.jsoup.parse.ParseErrorList;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr='value'></element></root>";
        ParseSettings settings = new ParseSettings();
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        Element root = doc.getRootElement();
        Element element = root.getFirstElementChild();
        assertNotNull(root);
        assertNotNull(element);
        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
    }

    @Test
    public void testProcessStartTag() {
        String xml = "<root><element attr='value'/></root>";
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();;
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        xmlTreeBuilder.initialiseParse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
        Stack<Node> stack = xmlTreeBuilder.stack;
        Element root = null;
        while (!xmlTreeBuilder.runParser().isClosed()) {
            Node node = xmlTreeBuilder.currentElement();
            if (node instanceof Element) {
                root = (Element) node;
                break;
            }
        }
        assertEquals("root", root.tagName());
        assertNull(root.getFirstElementChild());
    }

    @Test
    public void testProcessEndTag() {
        String xml = "<root><element attr='value'></element></root>";
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        xmlTreeBuilder.initialiseParse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();
        Stack<Node> stack = xmlTreeBuilder.stack;
        Element root = (Element) stack.get(0);
        Element element = (Element) stack.get(1);
        assertSame(root, element.parent());
        assertNull(stack.get(1));
        assertNull(stack.get(0));
    }

    @Test
    public void testProcessComment() {
        String xml = "<root><!-- this is a comment --></root>";
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        xmlTreeBuilder.initialiseParse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();
        Element comment = (Element) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals("!-- this is a comment -->", comment.outerHtml());
    }

    @Test
    public void testProcessCharacter() {
        String xml = "<root>text<root>";
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        xmlTreeBuilder.initialiseParse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();
        TextNode text = (TextNode) xmlTreeBuilder.doc.childNodes().get(1);
        assertEquals("text", text.text());
    }

    @Test
    public void testProcessDoctype() {
        String xml = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root></root>";
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        xmlTreeBuilder.initialiseParse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();
        DocumentType doctype = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals("root", doctype.nodeName());
        assertEquals("<!ATTLIST element attr CDATA #REQUIRED >", doctype.getInputSpecification().getData());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedToken() {
        String xml = "<root><!--this is a comment--><script>alert('Hello World')</script></root>";
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        xmlTreeBuilder.initialiseParse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();
        Validate.fail("Unexpected token type: SCRIPT");
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr='value'></element>";
        ParseSettings settings = new ParseSettings();
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "", ParseErrorList.noTracking(), settings);
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
    }

    @Test
    public void testInsertNode() {
        String xml = "<root><element attr='value'></element></root>";
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        xmlTreeBuilder.initialiseParse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();
        Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
        Element element = (Element) xmlTreeBuilder.doc.childNodes().get(1);
        Node newElement = new Element("newElement");
        xmlTreeBuilder.insertNode(newElement);
        root.appendChild(newElement);
        assertEquals(3, root.childElementCount());
    }

    @Test
    public void testParseFragmentInvalidXml() {
        String xmlFragment = "<root><!--this is a comment--><script>alert('Hello World')</script></root>";
        ParseSettings settings = new ParseSettings();
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "", ParseErrorList.noTracking(), settings);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testPopStackToClose() {
        String xml = "<root><element attr='value'><subElement/></element></root>";
        ParseSettings settings = new ParseSettings();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        xmlTreeBuilder.initialiseParse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();
        Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
        Element element = (Element) xmlTreeBuilder.doc.childNodes().get(1);
        Element subElement = (Element) xmlTreeBuilder.doc.childNodes().get(2);
        xmlTreeBuilder.popStackToClose(element.asEndTag());
        assertEquals(1, root.childElementCount());
    }
}