
package org.junit.tests.xmltreebuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseFromReader() throws Exception {
        String xmlInput = "<root><child>Hello World</child></root>";
        Reader reader = new StringReader(xmlInput);
        String baseUri = "http://example.com";

        Document doc = XmlTreeBuilder.parse(reader, baseUri);

        assertEquals("<root><child>Hello World</child></root>", doc.html());
    }

    @Test
    public void testParseFromString() throws Exception {
        String xmlInput = "<root><child>Hello World</child></root>";

        String baseUri = "http://example.com";

        Document doc = XmlTreeBuilder.parse(xmlInput, baseUri);

        assertEquals("<root><child>Hello World</child></root>", doc.html());
    }

    @Test
    public void testInitialiseParse() throws Exception {
        String xmlInput = "<root><child>Hello World</child></root>";
        Reader reader = new StringReader(xmlInput);
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(reader, baseUri, errors, settings);

        assertTrue(doc.childNodes().size() > 0);
    }

    @Test
    public void testPopStackToClose() {
        String baseUri = "http://example.com";

        XmlTreeBuilder builder = new XmlTreeBuilder();

        Element root = builder.newElement("root", baseUri);
        Element child1 = builder.newElement("child1", baseUri);
        Element child2 = builder.newElement("child2", baseUri);
        root.appendChild(child1);
        root.appendChild(child2);
        builder.stack.add(root);

        builder.popStackToClose(new Token.EndTag("root"));

        assertEquals(1, builder.stack.size());
        assertEquals(child1, builder.stack.get(0));
    }

    @Test
    public void testParseFragment() throws Exception {
        String xmlFragment = "<root><child>Hello World</child></root>";
        String baseUri = "http://example.com";

        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, baseUri, null, ParseSettings.preserveCase);

        assertEquals(1, nodes.size());
        assertEquals("<root><child>Hello World</child></root>", nodes.get(0).outerHtml());
    }
}
