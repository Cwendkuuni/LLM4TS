
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertThat(documentType.nodeName()).isEqualTo("#doctype");
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        assertThat(accum.toString()).isEqualTo("<!DOCTYPE html PUBLIC \"publicId\" systemId\">");
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, depth, out);
        assertThat(accum.toString()).isEmpty();
    }
}
