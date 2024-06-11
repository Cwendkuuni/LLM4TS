import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {
    // 
    @org.junit.Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        Document document = new Document();
        DocumentType doctype = new DocumentType(name, publicId, systemId, baseUri);
        document.appendChild(doctype);

        assertEquals("#doctype", doctype.nodeName());
        assertEquals(name, doctype.attr("name"));
        assertEquals(publicId, doctype.attr("publicId"));
        assertEquals(systemId, doctype.attr("systemId"));
        assertEquals(baseUri, doctype.baseUri());
        assertNotNull(doctype.outerHtml());
    }
    // 
}