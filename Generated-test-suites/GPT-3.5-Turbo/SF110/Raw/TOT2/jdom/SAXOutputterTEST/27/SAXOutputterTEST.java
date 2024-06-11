import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXParseException;
import org.jdom2.*;
import org.jdom2.output.support.Format;

public class SAXOutputterTest {
    
    @Test
    public void testSAXOutputter_DefaultConstructor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertNotNull(saxOutputter);
        assertEquals(false, saxOutputter.getReportNamespaceDeclarations());
        assertEquals(true, saxOutputter.getReportDTDEvents());
    }
    
    @Test
    public void testSetContentHandler() {
        SAXOutputter saxOutputter = new SAXOutputter();
        ContentHandler contentHandler = new DummyContentHandler();
        saxOutputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
    }
    
    @Test
    public void testSetFeature_NamespacePrefixes() throws Exception {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        assertEquals(true, saxOutputter.getReportNamespaceDeclarations());
    }
    
    @Test
    public void testSetFeature_InvalidFeature() throws SAXNotSupportedException {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertThrows(SAXNotRecognizedException.class, () -> saxOutputter.setFeature("invalid_feature", true));
    }
    
    @Test
    public void testSetProperty_LexicalHandler() throws Exception {
        SAXOutputter saxOutputter = new SAXOutputter();
        LexicalHandler lexicalHandler = new DummyLexicalHandler();
        saxOutputter.setProperty("http://xml.org/sax/properties/lexical-handler", lexicalHandler);
        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
    }
    
    @Test
    public void testOutput() throws JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        assertDoesNotThrow(() -> saxOutputter.output(document));
    }
    
    @Test
    public void testOutputFragment_NullContent() throws JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertDoesNotThrow(() -> saxOutputter.outputFragment(null));
    }
    
    private class DummyContentHandler implements ContentHandler {}

    private class DummyLexicalHandler implements LexicalHandler {}
}