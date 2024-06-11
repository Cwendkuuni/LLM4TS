import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstruction() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name").getValue());
        assertTrue(StringUtil.isBlank(documentType.attr("publicId").getValue()));
        assertTrue(StringUtil.isBlank(documentType.attr("systemId").getValue()));
    }

    @Test
    public void testEmptyDocumentTypeConstruction() {
        DocumentType documentType = new DocumentType("html", "", "", null);
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name").getValue());
        assertTrue(StringUtil.isBlank(documentType.attr("publicId").getValue()));
        assertTrue(StringUtil.isBlank(documentType.attr("systemId").getValue()));
    }

    @Test
    public void testDocumentTypeWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        assertEquals("publicID", documentType.attr("publicId").getValue());
        assertEquals("systemID", documentType.attr("systemId").getValue());
    }

    @Test
    public void testDocumentTypeWithNullPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertTrue(StringUtil.isBlank(documentType.attr("publicId").getValue()));
        assertTrue(StringUtil.isBlank(documentType.attr("systemId").getValue()));
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeWithNullName() {
        new DocumentType(null, "publicID", "systemID", null);
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeWithNullBaseUri() {
        new DocumentType("html", "publicID", "systemID", null);
    }

    @Test
    public void testOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html");
        if (!StringUtil.isBlank(documentType.attr("publicId").getValue()))
            expected.append(" PUBLIC \"").append(documentType.attr("publicId").getValue()).append("\"");
        if (!StringUtil.isBlank(documentType.attr("systemId").getValue()))
            expected.append(' ').append(documentType.attr("systemId").getValue()).append("\"");
        expected.append('>');

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "", null);
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html");
        expected.append('>');

        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlHead(actual, 0, null);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = new StringBuilder();
        documentType.outerHtmlTail(actual, 0, null);
        assertEquals("", actual.toString());
    }

    @Test
    public void testAddAttribute() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        Attribute attribute = new Attribute("data", "test");
        documentType.addAttribute(0, attribute);
        assertEquals("data", documentType.tagName("data").attr("name").getValue());
        assertEquals("test", documentType.tagName("data").attr("value").getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAttributeWithNegativeIndex() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        documentType.addAttribute(-1, new Attribute("data", "test"));
    }

    @Test
    public void testGetFirstChild() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        document.appendChild(documentType);
        Node firstChild = documentType.getFirstChild();
        assertSame(documentType, firstChild);
    }

    @Test
    public void testGetFirstChildWithDocumentTypeAsRoot() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        Node firstChild = documentType.getFirstChild();
        assertNull(firstChild);
    }

    @Test
    public void testRemoveChild() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        document.appendChild(documentType);
        documentType.removeChild(0);
        Node firstChild = document.child(0);
        assertNull(firstChild);
    }

    @Test
    public void testRemoveChildWithDocumentTypeAsLastChild() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        document.appendChild(new Element("html"));
        document.appendChild(documentType);
        documentType.removeChild(1);
        Node lastChild = document.lastChild();
        assertSame(document.child(0), lastChild);
    }

    @Test
    public void testToString() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        String expected = "<!DOCTYPE html";
        if (!StringUtil.isBlank(documentType.attr("publicId").getValue()))
            expected += " PUBLIC \"" + documentType.attr("publicId").getValue() + "\"";
        if (!StringUtil.isBlank(documentType.attr("systemId").getValue()))
            expected += ' ' + documentType.attr("systemId").getValue();
        expected += '>';

        String actual = documentType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithEmptyPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "", "", null);
        String expected = "<!DOCTYPE html>";
        String actual = documentType.toString();
        assertEquals(expected, actual);
    }
}