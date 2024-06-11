import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {
    
    private DocumentType doctype;
    
    @Before
    public void setUp() {
        doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#doctype", doctype.nodeName());
    }
    
    @Test
    public void testOuterHtmlHeadHtmlSyntaxAndNoPublicIdAndNoSystemId() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder accum = new StringBuilder();
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!doctype", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadHtmlSyntaxAndPublicIdAndNoSystemId() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        doctype.attr(DocumentType.PUBLIC_ID, "-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.removeAttr(DocumentType.SYSTEM_ID);
        StringBuilder accum = new StringBuilder();
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadHtmlSyntaxAndNoPublicIdAndSystemId() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        doctype.attr(DocumentType.PUBLIC_ID, "");
        doctype.attr(DocumentType.SYSTEM_ID, "http://www.w3.org/TR/html4/loose.dtd");
        StringBuilder accum = new StringBuilder();
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!doctype html \"http://www.w3.org/TR/html4/loose.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadHtmlSyntaxAndPublicIdAndSystemId() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder accum = new StringBuilder();
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadXmlSyntaxAndNoPublicIdAndNoSystemId() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        StringBuilder accum = new StringBuilder();
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!DOCTYPE html", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadXmlSyntaxAndPublicIdAndNoSystemId() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        doctype.attr(DocumentType.PUBLIC_ID, "-//W3C//DTD XHTML 1.0 Strict//EN");
        doctype.removeAttr(DocumentType.SYSTEM_ID);
        StringBuilder accum = new StringBuilder();
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadXmlSyntaxAndNoPublicIdAndSystemId() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        doctype.removeAttr(DocumentType.PUBLIC_ID);
        doctype.attr(DocumentType.SYSTEM_ID, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        StringBuilder accum = new StringBuilder();
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadXmlSyntaxAndPublicIdAndSystemId() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        StringBuilder accum = new StringBuilder();
        try {
            doctype.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHas() {
        assertTrue(doctype.has(DocumentType.NAME));
        assertTrue(doctype.has(DocumentType.PUBLIC_ID));
        assertTrue(doctype.has(DocumentType.SYSTEM_ID));
    }
}