import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.*;
import org.jdom2.transform.JDOMResult;

import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {
    
    private JDOMResult jdomResult;
    private DocumentBuilder documentBuilder;
    
    @Before
    public void setup() {
        jdomResult = new JDOMResult();
        documentBuilder = jdomResult.new DocumentBuilder();
    }
    
    @Test
    public void testSetResult() {
        List<Content> result = new ArrayList<Content>();
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }
    
    @Test
    public void testGetResultWithResultListNull() {
        jdomResult.setResult(null);
        assertEquals(Collections.emptyList(), jdomResult.getResult());
    }
    
    @Test
    public void testGetResultWithResultListNotNullAndNotQueried() {
        List<Content> result = new ArrayList<Content>();
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
        
        List<Content> content = new ArrayList<Content>();
        content.add(new Element("test"));
        jdomResult.setDocument(new Document(content));
        assertEquals(content, jdomResult.getResult());
    }
    
    @Test
    public void testSetDocument() {
        List<Content> content = new ArrayList<Content>();
        content.add(new Element("test"));
        Document document = new Document(content);
        
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }
    
    @Test
    public void testGetDocumentWithResultDocNull() {
        jdomResult.setDocument(null);
        assertNull(jdomResult.getDocument());
    }
    
    @Test
    public void testGetDocumentWithResultListNotNullAndNotQueried() {
        List<Content> content = new ArrayList<Content>();
        content.add(new Element("test"));
        jdomResult.setResult(content);
        
        assertNotNull(jdomResult.getDocument());
    }
    
    @Test
    public void testSetFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }
    
    @Test
    public void testRetrieveResultWithResultListNullAndResultDocNull() {
        jdomResult.setResult(null);
        jdomResult.setDocument(null);
        documentBuilder.startDocumentReceived = false;
        documentBuilder.saxHandler = null;
        
        documentBuilder.getResult();
        assertNotNull(jdomResult.getResult());
    }
    
    @Test
    public void testSetHandler() {
        jdomResult.setHandler(null);
    }
    
    @Test
    public void testSetLexicalHandler() {
        jdomResult.setLexicalHandler(null);
    }
    
    @Test
    public void testFragmentHandlerGetResult() {
        JDOMResult.FragmentHandler fragmentHandler = jdomResult.new FragmentHandler(jdomResult.getFactory());
        assertNotNull(fragmentHandler.getResult());
    }
    
    @Test
    public void testDocumentBuilderGetResultWithSaxHandlerNotNull() {
        documentBuilder.saxHandler = jdomResult.new FragmentHandler(jdomResult.getFactory());
        assertNotNull(documentBuilder.getResult());
    }
    
    @Test
    public void testDocumentBuilderGetResultWithSaxHandlerNull() {
        documentBuilder.saxHandler = null;
        assertNull(documentBuilder.getResult());
    }
    
    @Test
    public void testDocumentBuilderStartDocument() {
        documentBuilder.startDocumentReceived = false;
        jdomResult.setResult(null);
        documentBuilder.startDocument();
        
        assertNotNull(jdomResult.getResult());
        assertTrue(documentBuilder.startDocumentReceived);
    }
    
    @Test
    public void testDocumentBuilderStartElement() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderStartPrefixMapping() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderCharacters() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderIgnorableWhitespace() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderProcessingInstruction() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderSkippedEntity() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderStartDTD() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderEndDTD() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderStartEntity() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderEndEntity() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderStartCDATA() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderEndCDATA() {
        documentBuilder.ensureInitialization();
    }
    
    @Test
    public void testDocumentBuilderComment() {
        documentBuilder.ensureInitialization();
    }
}