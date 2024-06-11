import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");

        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype1 = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
        StringBuilder accum1 = new StringBuilder();
        Document.OutputSettings out1 = new Document.OutputSettings();
        
        doctype1.outerHtmlHead(accum1, 0, out1);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">", accum1.toString());
        
        DocumentType doctype2 = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
        StringBuilder accum2 = new StringBuilder();
        Document.OutputSettings out2 = new Document.OutputSettings();
        
        doctype2.outerHtmlHead(accum2, 0, out2);
        
        assertEquals("<!DOCTYPE html http://www.w3.org/TR/html4/strict.dtd\">", accum2.toString());
        
        DocumentType doctype3 = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://www.w3.org/TR/html4/");
        StringBuilder accum3 = new StringBuilder();
        Document.OutputSettings out3 = new Document.OutputSettings();
        
        doctype3.outerHtmlHead(accum3, 0, out3);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum3.toString());
        
        DocumentType doctype4 = new DocumentType("html", "", "", "http://www.w3.org/TR/html4/");
        StringBuilder accum4 = new StringBuilder();
        Document.OutputSettings out4 = new Document.OutputSettings();
        
        doctype4.outerHtmlHead(accum4, 0, out4);
        
        assertEquals("<!DOCTYPE html>", accum4.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        doctype.outerHtmlTail(accum, 0, out);
        
        assertEquals("", accum.toString());
    }
}