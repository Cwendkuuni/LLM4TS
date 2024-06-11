import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.w3.org/");
        assertNotNull(documentType);
        assertEquals("html", documentType.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", documentType.attr("systemId"));
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.w3.org/");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" http://www.w3.org/TR/html4/loose.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.w3.org/");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, depth, out);
        assertEquals("", accum.toString());
    }
}