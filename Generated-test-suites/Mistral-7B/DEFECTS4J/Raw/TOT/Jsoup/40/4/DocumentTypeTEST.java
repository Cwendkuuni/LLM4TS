import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeCreation() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "publicId", "systemId", "baseUri");
        Element element = doc.createElement("#doctype");
        element.add(doctype);

        Assert.assertEquals("#doctype", element.nodeName());
        Assert.assertEquals("html", doctype.attr("name"));
        Assert.assertEquals("publicId", doctype.attr("publicId"));
        Assert.assertEquals("systemId", doctype.attr("systemId"));
        Assert.assertEquals("baseUri", doctype.baseUri());
    }
}