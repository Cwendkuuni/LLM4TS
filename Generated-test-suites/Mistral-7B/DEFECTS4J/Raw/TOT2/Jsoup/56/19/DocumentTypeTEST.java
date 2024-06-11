import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", "http://example.com");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertEquals("HTML", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("HTML5", docType.attr(DocumentType.SYSTEM_ID));
        assertEquals("http://example.com", docType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", "http://example.com");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", "http://example.com");
        docType.attr(DocumentType.NAME, "html");
        assertTrue(docType.has(DocumentType.NAME));
    }

    @Test
    public void testHasAttributeEmptyString() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", "http://example.com");
        assertFalse(docType.has(""));
    }

    @Test(expected = IOException.class)
    public void testOuterHtmlHeadWithUnsupportedOutputSettings() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", "http://example.com");
        doc.outerHtml(new Appendable() {
            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException();
            }

            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException();
            }

            @Override
            public Appendable append(char[] cbuf) throws IOException {
                throw new IOException();
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException();
            }
        }, 0, Document.OutputSettings.xml());
        docType.outerHtmlHead(new Appendable() {
            @Override
            public Appendable append(char c) throws IOException {
                fail();
            }

            @Override
            public Appendable append(CharSequence csq) throws IOException {
                fail();
            }

            @Override
            public Appendable append(char[] cbuf) throws IOException {
                fail();
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                fail();
            }
        }, 0, Document.OutputSettings.xml());
    }

    @Test
    public void testDocumentTypeCreationWithNullAttributes() {
        assertThrows(NullPointerException.class, () -> new DocumentType(null, "HTML", "HTML5", "http://example.com"));
        assertThrows(NullPointerException.class, () -> new DocumentType("html", null, "HTML5", "http://example.com"));
        assertThrows(NullPointerException.class, () -> new DocumentType("html", "HTML", null, "http://example.com"));
        assertThrows(NullPointerException.class, () -> new DocumentType("html", "HTML", "HTML5", null));
    }
}