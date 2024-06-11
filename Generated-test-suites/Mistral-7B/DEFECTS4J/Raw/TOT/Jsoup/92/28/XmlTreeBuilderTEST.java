import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        Parser parser = new Parser(xmlTreeBuilder);

        String xml = "<>";
        Document document = xmlTreeBuilder.parse(new StringReader(xml), null, parser);

        Tokenizer tokenizer = new Tokenizer(xml);
        // Test if the parsed document matches the original input
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xml, null, null, parser);
        for (Node node : parsedNodes) {
            Element parsedElement = (Element) node;
            Element expectedElement = tokenizer.nextTag();
            assertEquals(expectedElement.nodeName(), parsedElement.nodeName());
            assertNotNull(parsedElement.attributes());
            assertEquals(expectedElement.attributes(), parsedElement.attributes());
        }
        // Test if the document is empty
        assertEquals(0, document.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        Parser parser = new Parser(xmlTreeBuilder);

        String xml = "<root><element attr='value'>Hello World</element></root>";
        Document document = xmlTreeBuilder.parse(new StringReader(xml), null, parser);

        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xml, null, null, parser);

        assertEquals(1, document.childNodes().size());
        Element root = (Element) document.childNodes().get(0);
        assertEquals("root", root.nodeName());

        assertEquals(1, root.childNodes().size());
        Element element = (Element) root.childNodes().get(0);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attributes().get("attr"));
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseComplexXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        Parser parser = new Parser(xmlTreeBuilder);

        String xml = "<root><element attr1='value1'><subelement attr2='value2'>Content</subelement></element></root>";
        Document document = xmlTreeBuilder.parse(new StringReader(xml), null, parser);

        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xml, null, null, parser);

        assertEquals(1, document.childNodes().size());
        Element root = (Element) document.childNodes().get(0);
        assertEquals("root", root.nodeName());

        assertEquals(1, root.childNodes().size());
        Element element = (Element) root.childNodes().get(0);
        assertEquals("element", element.nodeName());

        assertEquals(1, element.childNodes().size());
        Element subElement = (Element) element.childNodes().get(0);
        assertEquals("subelement", subElement.nodeName());
        assertEquals("value2", subElement.attributes().get("attr2"));
        assertEquals("Content", subElement.text());
    }

    @Test
    public void testParseInvalidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        Parser parser = new Parser(xmlTreeBuilder);

        String xml = "<root><element attr='value><subelement>"; // Missing '>' in tag 'subelement'
        try {
            xmlTreeBuilder.parse(new StringReader(xml), null, parser);
            assertEquals(true, false); // This should not reach here
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }
    }
}