import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        
        assertThat(docType.nodeName()).isEqualTo("#doctype");
        assertThat(docType.attributes().get("name")).isEqualTo("html");
        assertThat(docType.attributes().get("publicId")).isEqualTo("-//W3C//DTD HTML 4.01//EN");
        assertThat(docType.attributes().get("systemId")).isEqualTo("http://www.w3.org/TR/html4/strict.dtd");
        assertThat(docType.baseUri()).isEqualTo("http://www.example.com");
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        docType.outerHtmlHead(accum, depth, out);
        String output = accum.toString();
        
        assertThat(output).isEqualTo("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
    }
    
    @Test
    public void testOuterHtmlHead_withNoPublicId() {
        DocumentType docType = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        docType.outerHtmlHead(accum, depth, out);
        String output = accum.toString();
        
        assertThat(output).isEqualTo("<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\">");
    }
    
    @Test
    public void testOuterHtmlHead_withNoSystemId() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://www.example.com");
        
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        docType.outerHtmlHead(accum, depth, out);
        String output = accum.toString();
        
        assertThat(output).isEqualTo("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">");
    }
    
    @Test
    public void testOuterHtmlHead_withNoName() {
        DocumentType docType = new DocumentType("", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        docType.outerHtmlHead(accum, depth, out);
        String output = accum.toString();
        
        assertThat(output).isEqualTo("<!DOCTYPE PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        docType.outerHtmlTail(accum, depth, out);
        String output = accum.toString();
        
        assertThat(output).isEqualTo("");
    }
}