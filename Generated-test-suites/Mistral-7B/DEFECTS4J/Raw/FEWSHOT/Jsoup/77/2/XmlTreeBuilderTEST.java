package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<Root><Child1><SubChild1></SubChild1></Child1><Child2/></Root>";
        String baseUri = "http://test.com";

        Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);

        assertEquals("Root", doc.tagName());
        assertEquals(2, doc.childNodeSize());

        Element root = (Element) doc.child(0);
        assertEquals("Child1", root.tagName());
        assertEquals(1, root.childNodeSize());

        Element child1 = (Element) root.child(0);
        assertEquals("SubChild1", child1.tagName());
        assertNull(child1.childNodes());

        Element child2 = (Element) doc.child(1);
        assertEquals("Child2", child2.tagName());
        assertNull(child2.childNodes());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<Root><Child1><SubChild1></SubChild1></Child1><Child2/></Root>";
        String baseUri = "http://test.com";
        ParseSettings settings = new ParseSettings();

        Token startTag = new Token.StartTag("Root", 0, 0, 1);
        goldenPathTest(xmlTreeBuilder, startTag, settings, baseUri);

        Token startTagChild1 = new Token.StartTag("Child1", 0, 0, 1);
        goldenPathTest(xmlTreeBuilder, startTagChild1, settings, baseUri);

        Token startTagSubChild1 = new Token.StartTag("SubChild1", 0, 0, 1);
        goldenPathTest(xmlTreeBuilder, startTagSubChild1, settings, baseUri);

        Token startTagChild2 = new Token.StartTag("Child2", 0, 0, 1);
        goldenPathTest(xmlTreeBuilder, startTagChild2, settings, baseUri);
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<Root><Child1><SubChild1></SubChild1></Child1><Child2/></Root>";
        String baseUri = "http://test.com";
        ParseSettings settings = new ParseSettings();

        Token startTag = new Token.StartTag("Root", 0, 0, 1);
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), baseUri, ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();

        Token endTag = new Token.EndTag("Root", 0, 0, 1);
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());

        Token startTagChild1 = new Token.StartTag("Child1", 0, 0, 1);
        Token endTagChild1 = new Token.EndTag("Child1", 0, 0, 1);
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), baseUri, ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();
        xmlTreeBuilder.popStackToClose(endTagChild1);
        assertEquals(0, xmlTreeBuilder.stack.size());

        Token startTagSubChild1 = new Token.StartTag("SubChild1", 0, 0, 1);
        Token endTagSubChild1 = new Token.EndTag("SubChild1", 0, 0, 1);
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), baseUri, ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();
        xmlTreeBuilder.popStackToClose(endTagSubChild1);
        assertEquals(0, xmlTreeBuilder.stack.size());

        Token startTagChild2 = new Token.StartTag("Child2", 0, 0, 1);
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), baseUri, ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();
        xmlTreeBuilder.popStackToClose(endTagChild2);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<!--This is a comment -->";
        String baseUri = "http://test.com";

        Token commentToken = new Token.Comment("This is a comment", 0, 0, 1);
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        xmlTreeBuilder.process(commentToken);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(Comment.class, xmlTreeBuilder.stack.get(0).getClass());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<Root><Child1>&amp;<SubChild1></SubChild1></Child1></Root>";
        String baseUri = "http://test.com";

        Token characterToken = new Token.Character("&amp;", 0, 0, 1);
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        xmlTreeBuilder.process(characterToken);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(CDataNode.class, xmlTreeBuilder.stack.get(0).getClass());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<!DOCTYPE Root SYSTEM \"http://test.com/root.dtd\">";
        String baseUri = "http://test.com";

        Token doctypeToken = new Token.Doctype("Root", null, null, null, "http://test.com/root.dtd");
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        xmlTreeBuilder.process(doctypeToken);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(DocumentType.class, xmlTreeBuilder.stack.get(0).getClass());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInputFragment = "<Root><Child1><SubChild1></SubChild1></Child1><Child2/></Root>";
        String baseUri = "http://test.com";
        ParseSettings settings = new ParseSettings();

        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInputFragment, baseUri, ParseErrorList.noTracking(), settings);

        assertEquals(11, nodes.size());
        assertEquals(Document.class, nodes.get(0).getClass());
        assertEquals("Root", nodes.get(0).tagName());

        Element root = (Element) nodes.get(0);
        assertEquals(2, root.childNodeSize());

        Element child1 = (Element) root.child(0);
        assertEquals("Child1", child1.tagName());
        assertEquals(1, child1.childNodeSize());

        Element subChild1 = (Element) child1.child(0);
        assertEquals("SubChild1", subChild1.tagName());
        assertNull(subChild1.childNodes());

        Element child2 = (Element) root.child(1);
        assertEquals("Child2", child2.tagName());
        assertNull(child2.childNodes());
    }

    private void goldenPathTest(XmlTreeBuilder xmlTreeBuilder, Token token, ParseSettings settings, String baseUri) {
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), baseUri, ParseErrorList.noTracking(), settings);
        xmlTreeBuilder.runParser();
        xmlTreeBuilder.process(token);
    }
}