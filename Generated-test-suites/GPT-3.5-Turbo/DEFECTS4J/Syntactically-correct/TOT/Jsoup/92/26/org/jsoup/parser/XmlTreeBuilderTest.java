import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParse_BasicXML_ReturnsParsedDocument() {
        String xml = "<root><element attribute=\"value\">Text</element></root>";
        Document doc = xmlTreeBuilder.parse(xml, "");
        assertNotNull(doc);
        assertEquals("root", doc.child(0).nodeName());
        assertEquals("element", doc.child(0).child(0).nodeName());
        assertEquals("value", doc.child(0).child(0).attr("attribute"));
        assertEquals("Text", doc.child(0).child(0).text());
    }

    @Test
    public void testParse_XMLWithComment_ReturnsDocumentWithCommentNode() {
        String xml = "<root><!-- This is a comment --></root>";
        Document doc = xmlTreeBuilder.parse(xml, "");
        assertNotNull(doc);
        assertEquals("root", doc.child(0).nodeName());
        assertTrue(doc.child(0).childNode(0) instanceof Comment);
        Comment comment = (Comment) doc.child(0).childNode(0);
        assertEquals(" This is a comment ", comment.getData());
    }

    @Test
    public void testParse_XMLWithCDATA_ReturnsDocumentWithCDataNode() {
        String xml = "<root><![CDATA[This is a CDATA section]]></root>";
        Document doc = xmlTreeBuilder.parse(xml, "");
        assertNotNull(doc);
        assertEquals("root", doc.child(0).nodeName());
        assertTrue(doc.child(0).childNode(0) instanceof CDataNode);
        CDataNode cdataNode = (CDataNode) doc.child(0).childNode(0);
        assertEquals("This is a CDATA section", cdataNode.getData());
    }

    @Test
    public void testParse_XMLWithDoctype_ReturnsDocumentWithDoctypeNode() {
        String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" " +
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><root></root>";
        Document doc = xmlTreeBuilder.parse(xml, "");
        assertNotNull(doc);
        assertEquals("root", doc.child(0).nodeName());
        assertTrue(doc.childNode(0) instanceof DocumentType);
        DocumentType doctype = (DocumentType) doc.childNode(0);
        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testParse_EmptyXML_ReturnsDocumentWithEmptyRootElement() {
        String xml = "<root></root>";
        Document doc = xmlTreeBuilder.parse(xml, "");
        assertNotNull(doc);
        assertEquals("root", doc.child(0).nodeName());
        assertTrue(doc.child(0).childNodes().isEmpty());
    }

    @Test
    public void testParse_InvalidXML_ThrowsException() {
        String xml = "<root><element></root>";
        try {
            xmlTreeBuilder.parse(xml, "");
            fail("Expected exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            assertEquals("Unexpected token type: EndTag", e.getMessage());
        }
    }

    // Add more test cases for other methods as per requirement

}