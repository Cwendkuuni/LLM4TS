import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMResultTest {

    @Test
    public void testSetResult_ValidList_ResultSet() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setResult(result);
        
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(new Element("element"));
        
        assertEquals(expectedResult, jdomResult.getResult());
    }
  
    @Test
    public void testSetResult_EmptyList_ResultSet() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = Collections.emptyList();
        jdomResult.setResult(result);
        
        assertEquals(Collections.emptyList(), jdomResult.getResult());
    }
    
    @Test
    public void testGetResult_NoResult_ReturnsEmptyList() {
        JDOMResult jdomResult = new JDOMResult();
        
        assertEquals(Collections.emptyList(), jdomResult.getResult());
    }
    
    @Test
    public void testGetResult_ResultSet_ReturnsResultList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setResult(result);
        
        assertEquals(result, jdomResult.getResult());
    }
    
    @Test
    public void testGetResult_ResultDocumentSet_ReturnsResultList() {
        JDOMResult jdomResult = new JDOMResult();
        Document result = new Document(new Element("element"));
        jdomResult.setDocument(result);
        
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(new Element("element"));
        
        assertEquals(expectedResult, jdomResult.getResult());
    }
    
    @Test
    public void testGetResult_ResultDocumentAndQueried_ReturnsResultList() {
        JDOMResult jdomResult = new JDOMResult();
        Document result = new Document(new Element("element"));
        jdomResult.setDocument(result);
        
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(new Element("element"));

        // Simulate queried state
        jdomResult.getResult();
        
        assertEquals(expectedResult, jdomResult.getResult());
    }
    
    @Test
    public void testSetDocument_ValidDocument_ResultSet() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("element"));
        jdomResult.setDocument(document);
        
        assertEquals(document, jdomResult.getDocument());
    }
    
    @Test
    public void testGetDocument_NoResult_ReturnsNull() {
        JDOMResult jdomResult = new JDOMResult();
        
        assertNull(jdomResult.getDocument());
    }
    
    @Test
    public void testGetDocument_ResultSet_ReturnsResultDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("element"));
        jdomResult.setDocument(document);
        
        assertEquals(document, jdomResult.getDocument());
    }
    
    @Test
    public void testGetDocument_ResultListSet_ReturnsResultDocument() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setResult(result);
        
        Document expectedDocument = new Document();
        expectedDocument.setContent(result);
        
        assertEquals(expectedDocument, jdomResult.getDocument());
    }
    
    @Test
    public void testGetDocument_ResultListAndQueried_ReturnsResultDocument() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setResult(result);
        
        Document expectedDocument = new Document();
        expectedDocument.setContent(result);

        // Simulate queried state
        jdomResult.getDocument();
        
        assertEquals(expectedDocument, jdomResult.getDocument());
    }
    
    @Test
    public void testSetFactory_ValidFactory_FactorySet() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        
        assertEquals(factory, jdomResult.getFactory());
    }
    
    @Test
    public void testGetFactory_NoFactorySet_ReturnsNull() {
        JDOMResult jdomResult = new JDOMResult();
        
        assertNull(jdomResult.getFactory());
    }
    
    @Test
    public void testGetFactory_FactorySet_ReturnsFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        
        assertEquals(factory, jdomResult.getFactory());
    }
  
    @Test
    public void testRetrieveResult_NoResult_ReturnsNull() {
        JDOMResult jdomResult = new JDOMResult();
        
        assertNull(jdomResult.getResult());
    }
    
    @Test
    public void testRetrieveResult_ResultSet_ReturnsResultList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setResult(result);
        
        assertEquals(result, jdomResult.getResult());
    }
    
    @Test
    public void testFragmentHandler_GetResult_ReturnsDetachedContentList() {
        JDOMResult.FragmentHandler handler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        Element element = new Element("element");
        handler.startElement("", "element", "", null);
        handler.endElement("", "element", "");
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(element);
        
        assertEquals(expectedResult, handler.getResult());
    }
  
    @Test
    public void testDocumentBuilder_GetResult_NullSaxHandler_ReturnsNull() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        assertNull(builder.getResult());
    }
    
    @Test
    public void testDocumentBuilder_GetResult_ValidSaxHandler_ReturnsResult() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        JDOMResult.FragmentHandler saxHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        Element element = new Element("element");
        saxHandler.startElement("", "element", "", null);
        saxHandler.endElement("", "element", "");
        builder.startDocument();
        builder.setContentHandler(saxHandler);
        builder.setLexicalHandler(saxHandler);
        
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(element);
        
        assertEquals(expectedResult, builder.getResult());
        assertNull(builder.getResult()); // SaxHandler should be reset after getResult()
    }
    
    @Test
    public void testDocumentBuilder_GetResult_StartDocumentNotReceived_ReturnsNull() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        assertNull(builder.getResult());
    }
    
    @Test
    public void testDocumentBuilder_StartDocument_StartDocumentReceived() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        assertFalse(builder.startDocumentReceived);
        builder.startDocument();
        assertTrue(builder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilder_StartElement_EnsureInitializationCalled() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.startElement("", "element", "", null);
        assertTrue(builder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilder_StartPrefixMapping_EnsureInitializationCalled() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.startPrefixMapping("", "");
        assertTrue(builder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilder_Characters_EnsureInitializationCalled() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.characters(new char[0], 0, 0);
        assertTrue(builder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilder_IgnorableWhitespace_EnsureInitializationCalled() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.ignorableWhitespace(new char[0], 0, 0);
        assertTrue(builder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilder_ProcessingInstruction_EnsureInitializationCalled() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.processingInstruction("", "");
        assertTrue(builder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilder_SkippedEntity_EnsureInitializationCalled() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.skippedEntity("");
        assertTrue(builder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilder_StartDTD_EnsureInitializationCalled() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.startDTD("", "", "");
        assertTrue(builder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilder_EndDTD() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.endDTD();
        // No assertion needed, just validate if method executes without errors
    }
    
    @Test
    public void testDocumentBuilder_StartEntity_EnsureInitializationCalled() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.startEntity("");
        assertTrue(builder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilder_EndEntity() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.endEntity("");
        // No assertion needed, just validate if method executes without errors
    }
    
    @Test
    public void testDocumentBuilder_StartCDATA_EnsureInitializationCalled() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.startCDATA();
        assertTrue(builder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilder_EndCDATA() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.endCDATA();
        // No assertion needed, just validate if method executes without errors
    }
    
    @Test
    public void testDocumentBuilder_Comment_EnsureInitializationCalled() {
        JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
        
        builder.comment(new char[0], 0, 0);
        assertTrue(builder.startDocumentReceived);
    }
}