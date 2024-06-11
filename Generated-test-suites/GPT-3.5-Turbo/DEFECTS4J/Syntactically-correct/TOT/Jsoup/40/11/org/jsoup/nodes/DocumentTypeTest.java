
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;

import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setup() {
        documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd", "");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, outputSettings);
        assertEquals("", accum.toString());
    }
}
