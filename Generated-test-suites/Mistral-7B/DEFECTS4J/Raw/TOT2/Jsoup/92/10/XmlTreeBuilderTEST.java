package org.jsoup.parser; // OR whatever is the package name for the XmlTreeBuilder class

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        // Given: empty XML input
        String xmlInput = "";
        Document document = new XmlTreeBuilder().parse(new StringReader(xmlInput));

        // When: parsing the empty XML
        // Then: the resulting document should not be empty
        Assert.assertNotEquals(0, document.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        // Given: simple XML input
        String xmlInput = "<root></root>";
        Document document = new XmlTreeBuilder().parse(new StringReader(xmlInput));

        // When: parsing the simple XML
        // Then: the root element should be present in the document
        Element rootElement = document.child(0);
        Assert.assertEquals("root", rootElement.nodeName());
    }

    @Test
    public void testParseXmlWithElementsAndText() {
        // Given: XML input with elements and text
        String xmlInput = "<root><child>Hello World</child></root>";
        Document document = new XmlTreeBuilder().parse(new StringReader(xmlInput));

        // When: parsing the XML
        // Then: the root element should have a child element with the text "Hello World"
        Element rootElement = document.child(0);
        Element childElement = (Element) rootElement.child(0);
        Assert.assertEquals("Hello World", childElement.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXml() {
        // Given: invalid XML input
        String xmlInput = "<root></root><root>"; // duplicate root element
        try {
            new XmlTreeBuilder().parse(new StringReader(xmlInput));
            // This statement should not be reached if the test passes
            Assert.fail("Expected an IllegalArgumentException, but none was thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception, test passes
        }
    }

    @Test
    public void testParseXmlWithComments() {
        // Given: XML input with comments
        String xmlInput = "<comment><root><child>Hello World</child></root></comment>";
        Document document = new XmlTreeBuilder().parse(new StringReader(xmlInput));

        // When: parsing the XML
        // Then: the comment node should be present in the document
        List<Node> commentNodes = document.childNodes();
        Assert.assertEquals(1, commentNodes.size());
        Node commentNode = commentNodes.get(0);
        Assert.assertEquals(Node.COMMENT_NODE, commentNode.nodeType());
        Assert.assertTrue(commentNode.nodeValue().startsWith("<!--"));
    }

    @Test
    public void testParseXmlWithDoctype() {
        // Given: XML input with doctype
        String xmlInput = "<!DOCTYPE root SYSTEM \"root.dtd\">\n<root></root>";
        Document document = new XmlTreeBuilder().parse(new StringReader(xmlInput));

        // When: parsing the XML
        // Then: the document should contain a doctype node
        DocumentType doctypeNode = (DocumentType) document.childNodes().get(0);
        Assert.assertEquals("root", doctypeNode.nodeName());
        Assert.assertNotNull(doctypeNode.getPublicIdentifier());
        Assert.assertNotNull(doctypeNode.getSystemIdentifier());
    }

    @Test
    public void testParseXmlWithSelfClosingTags() {
        // Given: XML input with self-closing tags
        String xmlInput = "<root><img src=\"image.jpg\" /></root>";
        Document document = new XmlTreeBuilder().parse(new StringReader(xmlInput));

        // When: parsing the XML
        // Then: the 'img' tag should be present as an Element with the 'self-closing' flag set to true
        Element imgElement = (Element) document.childNodes().get(1);
        Assert.assertTrue(imgElement.isSelfClosing());
    }
}