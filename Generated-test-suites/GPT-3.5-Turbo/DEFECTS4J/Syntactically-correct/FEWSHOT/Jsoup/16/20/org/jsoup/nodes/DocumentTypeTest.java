import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.DocumentType;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "");

        assertEquals("html", docType.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr("systemId"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "");

        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, new Document.OutputSettings());

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" http://www.w3.org/TR/html4/loose.dtd\">", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "");

        StringBuilder sb = new StringBuilder();
        docType.outerHtmlTail(sb, 0, new Document.OutputSettings());

        assertEquals("", sb.toString());
    }
}