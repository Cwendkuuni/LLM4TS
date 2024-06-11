import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testSimpleXml() throws IOException {
        String xmlString = "<root><element>Hello World</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = Jsoup.parse(xmlString, Parser.xmlParser());
        Element rootElement = doc.getRootElement();

        XmlTreeBuilder testXmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = testXmlTreeBuilder.parseFragment(xmlString, null, new ParseErrorList());

        Assertions.assertEquals(rootElement.nodeName(), parsedNodes.get(0).nodeName());
        Assertions.assertEquals(rootElement.childNodes().size(), parsedNodes.size() - 1);
    }

    @Test
    public void testEmptyXml() throws IOException {
        String xmlString = "<root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = Jsoup.parse(xmlString, Parser.xmlParser());

        XmlTreeBuilder testXmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = testXmlTreeBuilder.parseFragment(xmlString, null, new ParseErrorList());

        Assertions.assertEquals(doc.getRootElement().nodeName(), parsedNodes.get(0).nodeName());
        Assertions.assertEquals(doc.getRootElement().childNodes().size(), parsedNodes.size());
    }

    @Test
    public void testErrorHandling() throws IOException {
        String xmlString = "<root><element></root><!--<script>alert('Hello')-->";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = null;
        try {
            parsedNodes = xmlTreeBuilder.parseFragment(xmlString, null, new ParseErrorList());
        } catch (Exception e) {
            // expected
        }

        Assertions.assertNotNull(parsedNodes);
        Assertions.assertEquals(0, parsedNodes.size());
    }

    @Test
    public void testSelfClosingTag() throws IOException {
        String xmlString = "<img src='image.jpg' />";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = Jsoup.parse(xmlString, Parser.xmlParser());

        XmlTreeBuilder testXmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = testXmlTreeBuilder.parseFragment(xmlString, null, new ParseErrorList());

        Assertions.assertEquals(doc.getElementsByTag("img").size(), parsedNodes.size());
    }

    @Test
    public void testDoctypeDeclaration() throws IOException {
        String xmlString = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = Jsoup.parse(xmlString, Parser.xmlParser());

        XmlTreeBuilder testXmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = testXmlTreeBuilder.parseFragment(xmlString, null, new ParseErrorList());

        Assertions.assertNotNull(parsedNodes.get(0));
        Assertions.assertTrue(parsedNodes.get(0) instanceof DocumentType);
    }
}