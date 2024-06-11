
import org.jdom2.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.xml.sax.*;

public class JDOMResultTest {
    
    @Test
    public void setResult_validList_setsResult() {
        JDOMResult result = new JDOMResult();
        List<Content> expected = new ArrayList<>();
        expected.add(new Element("element"));
        
        result.setResult(expected);
        
        List<Content> actual = result.getResult();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void setResult_nullList_setsResultToNull() {
        JDOMResult result = new JDOMResult();
        
        result.setResult(null);
        
        List<Content> actual = result.getResult();
        
        assertNull(actual);
    }
    
    @Test
    public void getResult_resultListNotNull_returnsResultList() {
        JDOMResult result = new JDOMResult();
        List<Content> expected = new ArrayList<>();
        expected.add(new Element("element"));
        result.setResult(expected);
        
        List<Content> actual = result.getResult();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void getResult_resultListNullAndResultDocNotNull_returnsContentList() {
        JDOMResult result = new JDOMResult();
        List<Content> expected = new ArrayList<>();
        expected.add(new Element("element"));
        result.setDocument(new Document(expected));
        
        List<Content> actual = result.getResult();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void getResult_resultListNullAndResultDocNullAndNotQueried_returnsEmptyList() {
        JDOMResult result = new JDOMResult();
        
        List<Content> actual = result.getResult();
        
        assertTrue(actual.isEmpty());
    }
    
    @Test
    public void getResult_resultListNullAndResultDocNullAndQueried_returnsEmptyList() {
        JDOMResult result = new JDOMResult();
        result.getResult();
        
        List<Content> actual = result.getResult();
        
        assertTrue(actual.isEmpty());
    }
    
    @Test
    public void setDocument_validDocument_setsResultDoc() {
        JDOMResult result = new JDOMResult();
        Document expected = new Document();
        
        result.setDocument(expected);
        
        Document actual = result.getDocument();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void setDocument_nullDocument_setsResultDocToNull() {
        JDOMResult result = new JDOMResult();
        result.setDocument(new Document());
        
        result.setDocument(null);
        
        Document actual = result.getDocument();
        
        assertNull(actual);
    }
    
    @Test
    public void getDocument_resultDocNotNull_returnsResultDoc() {
        JDOMResult result = new JDOMResult();
        Document expected = new Document();
        result.setDocument(expected);
        
        Document actual = result.getDocument();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void getDocument_resultListNotNullAndNotQueried_returnsDocumentWithResultList() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("element"));
        result.setResult(list);
        
        Document actual = result.getDocument();
        
        assertNotNull(actual);
        assertEquals(list, actual.getContent());
    }
    
    @Test
    public void getDocument_resultListNotNullAndQueried_returnsDocumentWithResultList() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("element"));
        result.setResult(list);
        result.getDocument();
        
        Document actual = result.getDocument();
        
        assertNotNull(actual);
        assertEquals(list, actual.getContent());
    }
    
    @Test
    public void setFactory_validFactory_setsFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory expected = new DefaultJDOMFactory();
        
        result.setFactory(expected);
        
        JDOMFactory actual = result.getFactory();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void getFactory_factoryNotSet_returnsNull() {
        JDOMResult result = new JDOMResult();
        
        JDOMFactory actual = result.getFactory();
        
        assertNull(actual);
    }
    
    @Test
    public void retrieveResult_resultListAndResultDocNull_callsSetResult() {
        JDOMResult result = new JDOMResult();
        MockDocumentBuilder builder = new MockDocumentBuilder();
        result.setHandler(builder);
        
        result.retrieveResult();
        
        assertTrue(builder.setResultCalled);
    }
    
    @Test
    public void setHandler_validHandler_doesNotThrowException() {
        JDOMResult result = new JDOMResult();
        result.setHandler(new MockContentHandler());
    }
    
    @Test
    public void setLexicalHandler_validHandler_doesNotThrowException() {
        JDOMResult result = new JDOMResult();
        result.setLexicalHandler(new MockLexicalHandler());
    }
    
    private static class MockDocumentBuilder extends JDOMResult.DocumentBuilder {
        public boolean setResultCalled = false;
        
        @Override
        public List<Content> getResult() {
            setResultCalled = true;
            return null;
        }
    }
    
    private static class MockContentHandler implements ContentHandler {
        public void startDocument() {}
        public void endDocument() {}
        public void startPrefixMapping(String prefix, String uri) {}
        public void endPrefixMapping(String prefix) {}
        public void startElement(String uri, String localName, String qName, Attributes atts) {}
        public void endElement(String uri, String localName, String qName) {}
        public void characters(char[] ch, int start, int length) {}
        public void ignorableWhitespace(char[] ch, int start, int length) {}
        public void processingInstruction(String target, String data) {}
        public void skippedEntity(String name) {}
    }
    
    private static class MockLexicalHandler implements LexicalHandler {
        public void startDTD(String name, String publicId, String systemId) {}
        public void endDTD() {}
        public void startEntity(String name) {}
        public void endEntity(String name) {}
        public void startCDATA() {}
        public void endCDATA() {}
        public void comment(char[] ch, int start, int length) {}
    }
}
