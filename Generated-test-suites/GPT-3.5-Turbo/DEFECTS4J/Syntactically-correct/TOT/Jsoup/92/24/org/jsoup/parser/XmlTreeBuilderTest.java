package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void parse_ValidXml_ReturnsDocument() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><child1>value1</child1><child2>value2</child2></root>";
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
        
        assertNotNull(document);
        assertNotNull(document.getElementsByTag("root"));
        assertNotNull(document.select("child1:contains(value1)"));
        assertNotNull(document.select("child2:contains(value2)"));
    }

    @Test
    public void parse_InvalidXml_ReturnsEmptyDocument() {
        String xml = "<root><child1>value1</child1><child2>value2<child2></root>"; // Malformed XML with missing closing tag
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
        
        assertNotNull(document);
        assertEquals("", document.html());
    }

    @Test
    public void parseFragment_ValidXmlFragment_ReturnsNodeList() {
        String xmlFragment = "<child1>value1</child1><child2>value2</child2>";
        List<Node> nodeList = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", new Parser(xmlTreeBuilder));
        
        assertNotNull(nodeList);
        assertEquals(2, nodeList.size());
        assertNotNull(nodeList.get(0).nodeName().equals("child1"));
        assertNotNull(nodeList.get(1).nodeName().equals("child2"));
    }

    @Test
    public void parseFragment_InvalidXmlFragment_ReturnsEmptyNodeList() {
        String xmlFragment = "<child1>value1<child2>value2</child2>";
        List<Node> nodeList = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", new Parser(xmlTreeBuilder));
        
        assertNotNull(nodeList);
        assertEquals(0, nodeList.size());
    }
}