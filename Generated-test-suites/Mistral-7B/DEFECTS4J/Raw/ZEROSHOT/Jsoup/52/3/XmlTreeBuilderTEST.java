package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenCollector;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilder() {
        // Test initialisation
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Test parseFragment method with valid XML input
        InputStream xmlInput = getClass().getResourceAsStream("/test.xml");
        Document xmlDoc = Jsoup.parse(xmlInput, Parser.xmlParser());
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlBuilder.parseFragment(xmlInput.toString(), "", new ParseErrorList());
        assertEquals(xmlDoc.body().childNodes().size(), nodes.size());

        // Test process method with different token types
        TokenCollector tokenCollector = new TokenCollector();
        xmlBuilder.initialiseParse("", "", new ParseErrorList());
        xmlBuilder.tokeniser = tokenCollector;

        xmlBuilder.process(new Token.StartTag("root", null));
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());

        xmlBuilder.process(new Token.EndTag("root"));
        assertEquals(1, stack.size());

        xmlBuilder.process(new Token.Character(' '));
        TextNode textNode = (TextNode) nodes.get(1);
        assertEquals(" ", textNode.data());

        xmlBuilder.process(new Token.EndTag("root")); // should not pop the stack as root tag is not closed
        assertEquals(2, stack.size());

        xmlBuilder.process(new Token.EOF());
        assertEquals(0, stack.size());

        // Test insertNode method
        Node node = new Element("test");
        xmlBuilder.insertNode(node);
        Element element = (Element) nodes.get(0);
        assertEquals(node, element.child(0));

        // Test popStackToClose method
        xmlBuilder.stack.add(new Element("root"));
        xmlBuilder.stack.add(new Element("test"));
        xmlBuilder.popStackToClose(new Token.EndTag("test"));
        assertEquals(1, stack.size());
        xmlBuilder.popStackToClose(new Token.EndTag("root"));
        assertEquals(0, stack.size());

        // Test parseFragment method with invalid XML input
        xmlBuilder.initialiseParse("", "", new ParseErrorList());
        xmlBuilder.tokeniser = tokenCollector;
        xmlBuilder.process(new Token.StartTag("root", null));
        xmlBuilder.process(new Token.EndTag("root")); // should not pop the stack as root tag is not closed
        xmlBuilder.process(new Token.Character(' '));
        xmlBuilder.process(new Token.EOF());
        List<Error> errors = xmlBuilder.errors;
        assertEquals(1, errors.size());
        assertEquals("unexpected EOF", errors.get(0).getMessage());
    }
}