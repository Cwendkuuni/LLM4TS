import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.*;

public class JDOMResultTest {
    
    private JDOMResult jdomResult;
    
    @Before
    public void setUp() {
        jdomResult = new JDOMResult();
    }
    
    @Test
    public void testSetResult() {
        List<Content> result = new ArrayList<Content>();
        jdomResult.setResult(result);
        
        List<Content> actualResult = jdomResult.getResult();
        assertEquals(result, actualResult);
    }
    
    @Test
    public void testGetResult() {
        List<Content> result = new ArrayList<Content>();
        jdomResult.setResult(result);
        
        List<Content> actualResult = jdomResult.getResult();
        assertEquals(result, actualResult);
    }
    
    @Test
    public void testGetResultWhenResultListNotNullAndQueried() {
        List<Content> result = new ArrayList<Content>();
        jdomResult.setResult(result);
        jdomResult.getResult();
        
        List<Content> actualResult = jdomResult.getResult();
        assertEquals(result, actualResult);
    }
    
    @Test
    public void testSetDocument() {
        Document document = new Document();
        jdomResult.setDocument(document);
        
        Document actualDocument = jdomResult.getDocument();
        assertEquals(document, actualDocument);
    }
    
    @Test
    public void testGetDocument() {
        Document document = new Document();
        jdomResult.setDocument(document);
        
        Document actualDocument = jdomResult.getDocument();
        assertEquals(document, actualDocument);
    }
    
    @Test
    public void testGetDocumentWhenResultDocNotNullAndQueried() {
        Document document = new Document();
        jdomResult.setDocument(document);
        jdomResult.getDocument();
        
        Document actualDocument = jdomResult.getDocument();
        assertEquals(document, actualDocument);
    }
    
    @Test
    public void testSetFactory() {
        JDOMFactory factory = new JDOMFactory();
        jdomResult.setFactory(factory);
        
        JDOMFactory actualFactory = jdomResult.getFactory();
        assertEquals(factory, actualFactory);
    }
    
    @Test
    public void testGetFactory() {
        JDOMFactory factory = new JDOMFactory();
        jdomResult.setFactory(factory);
        
        JDOMFactory actualFactory = jdomResult.getFactory();
        assertEquals(factory, actualFactory);
    }
    
    @Test
    public void testRetrieveResult() {
        Document document = new Document();
        jdomResult.setDocument(document);
        
        jdomResult.retrieveResult();
        
        List<Content> actualResult = jdomResult.getResult();
        assertEquals(document.getContent(), actualResult);
    }
    
    @Test
    public void testSetHandler() {
        // Test that the method does not throw any exceptions
        jdomResult.setHandler(null);
    }
    
    @Test
    public void testSetLexicalHandler() {
        // Test that the method does not throw any exceptions
        jdomResult.setLexicalHandler(null);
    }
    
    @Test
    public void testFragmentHandlerConstructor() {
        JDOMFactory factory = new JDOMFactory();
        JDOMResult.FragmentHandler fragmentHandler = new JDOMResult.FragmentHandler(factory);
        
        assertNotNull(fragmentHandler.dummyRoot);
    }
    
    @Test
    public void testFragmentHandlerGetResult() {
        JDOMFactory factory = new JDOMFactory();
        JDOMResult.FragmentHandler fragmentHandler = new JDOMResult.FragmentHandler(factory);
        
        List<Content> result = fragmentHandler.getResult();
        
        assertNotNull(result);
    }
    
    @Test
    public void testDocumentBuilderConstructor() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        assertNull(documentBuilder.saxHandler);
        assertFalse(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderGetResult() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        List<Content> result = documentBuilder.getResult();
        
        assertNull(result);
    }
    
    @Test
    public void testDocumentBuilderStartDocument() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.startDocument();
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderStartElement() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.startElement("", "", "", new AttributesImpl());
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderStartPrefixMapping() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.startPrefixMapping("", "");
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderCharacters() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.characters(new char[] {'a', 'b', 'c'}, 0, 3);
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderIgnorableWhitespace() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.ignorableWhitespace(new char[] {' ', ' ', ' '}, 0, 3);
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderProcessingInstruction() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.processingInstruction("", "");
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderSkippedEntity() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.skippedEntity("");
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderStartDTD() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.startDTD("", "", "");
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderEndDTD() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.endDTD();
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderStartEntity() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.startEntity("");
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderEndEntity() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.endEntity("");
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderStartCDATA() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.startCDATA();
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderEndCDATA() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.endCDATA();
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderComment() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        
        try {
            documentBuilder.comment(new char[] {'a', 'b', 'c'}, 0, 3);
        } catch (SAXException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertTrue(documentBuilder.startDocumentReceived);
    }
}