package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder.ParseErrorList;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * XmlTreeBuilderTest
 */
public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag attr='value'/></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);
        assertTrue(xmlTreeBuilder.doc != null);
    }

    @Test
    public void testProcess_StartTag() {
        String input = "<xml><tag attr='value'/></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", errors);
        Element tagElement = (Element) nodes.get(0).childNodes().get(0);
        assertEquals("tag", tagElement.nodeName());
        assertEquals("value", tagElement.attr("attr"));
    }

    @Test
    public void testProcess_EndTag() {
        String input = "<xml><tag><subtag/></tag></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", errors);
        Element tagElement = (Element) nodes.get(0).childNodes().get(0);
        assertEquals("tag", tagElement.nodeName());

        Element subtagElement = (Element) tagElement.childNodes().get(0);
        assertEquals("subtag", subtagElement.nodeName());

        xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
        nodes = xmlTreeBuilder.parseFragment(input, "", errors);
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testProcess_Comment() {
        String input = "<xml><!-- comment --></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", errors);
        Comment commentNode = (Comment) nodes.get(0);
        assertEquals("comment", commentNode.nodeName());
        assertEquals("", commentNode.text());
    }

    @Test
    public void testProcess_Character() {
        String input = "<xml>&#x1F600;<tag attr='value'/></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", errors);
        TextNode characterNode = (TextNode) nodes.get(0).childNodes().get(0);
        assertEquals("\uD83C\uDF00", characterNode.text());
    }

    @Test(expected = AssertionError.class)
    public void testProcess_UnexpectedTokenType() {
        String input = "<xml><!-- comment -->5</xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);
        xmlTreeBuilder.parseFragment(input, "", errors);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element tagElement = new Element("tag", "", null);
        xmlTreeBuilder.insertNode(tagElement);
        Element rootElement = xmlTreeBuilder.doc;
        assertEquals(tagElement, rootElement.childNodes().get(0));
    }

    @Test
    public void testParseFragmentEmpty() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment("", "", errors);
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testParseFragmentInvalidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        String input = "<xml><tag><</tag></xml>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", errors);
        assertTrue(nodes.isEmpty());
        assertEquals(1, errors.size());
    }

    @Test
    public void testParseFragmentValidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        String input = "<xml><tag attr='value'><subtag/></tag></xml>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", errors);
        assertEquals(3, nodes.size());
        Element tagElement = (Element) nodes.get(0).childNodes().get(0);
        Element subtagElement = (Element) tagElement.childNodes().get(0);
        assertEquals("tag", tagElement.nodeName());
        assertEquals("value", tagElement.attr("attr"));
        assertEquals("subtag", subtagElement.nodeName());
        assertTrue(errors.isEmpty());
    }
}