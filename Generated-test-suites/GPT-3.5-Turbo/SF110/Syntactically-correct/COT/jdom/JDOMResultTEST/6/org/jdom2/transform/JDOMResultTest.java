
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class JDOMResultTest {
    
    @Test
    public void setResult_shouldSetResultListAndResetQueriedFlag(){
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        
        jdomResult.setResult(result);
        
        assertEquals(result, jdomResult.getResult());
        assertFalse(jdomResult.isQueried());
    }
    
    @Test
    public void getResult_whenResultListIsNotNullAndQueriedFlagIsTrue_shouldReturnResultList(){
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setResult(result);
        jdomResult.setQueried(true);
        
        List<Content> actualResult = jdomResult.getResult();
        
        assertEquals(result, actualResult);
    }
    
    @Test
    public void getResult_whenResultDocIsNotNullAndQueriedFlagIsFalse_shouldReturnContentFromResultDoc(){
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new Document();
        doc.addContent(new Element("element"));
        jdomResult.setDocument(doc);
        
        List<Content> actualResult = jdomResult.getResult();
        
        assertEquals(doc.getContent(), actualResult);
        assertNull(jdomResult.getDocument());
        assertTrue(jdomResult.isQueried());
    }
    
    @Test
    public void setDocument_shouldSetResultDocAndResetResultListAndQueriedFlag(){
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new Document();
        doc.addContent(new Element("element"));
        
        jdomResult.setDocument(doc);
        
        assertEquals(doc, jdomResult.getDocument());
        assertNull(jdomResult.getResult());
        assertFalse(jdomResult.isQueried());
    }
    
    @Test
    public void getDocument_whenResultDocIsNotNullAndQueriedFlagIsTrue_shouldReturnResultDoc(){
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new Document();
        doc.addContent(new Element("element"));
        jdomResult.setDocument(doc);
        jdomResult.setQueried(true);
        
        Document actualResult = jdomResult.getDocument();
        
        assertEquals(doc, actualResult);
    }
    
    @Test
    public void getDocument_whenResultListIsNotNullAndQueriedFlagIsFalse_shouldCreateDocumentAndSetContentFromResultList(){
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setResult(result);
        
        Document actualResult = jdomResult.getDocument();
        
        assertNotNull(actualResult);
        assertEquals(result, actualResult.getContent());
        assertNull(jdomResult.getResult());
        assertTrue(jdomResult.isQueried());
    }
    
    @Test
    public void setFactory_shouldSetFactory(){
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = Mockito.mock(JDOMFactory.class);
        
        jdomResult.setFactory(factory);
        
        assertEquals(factory, jdomResult.getFactory());
    }
    
    @Test
    public void setResult_whenDocumentBuilderIsNotNull_shouldCallSetResultOnDocumentBuilder(){
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        DocumentBuilder documentBuilder = Mockito.mock(DocumentBuilder.class);
        jdomResult.setHandler(documentBuilder);
        
        jdomResult.setResult(result);
        
        Mockito.verify(documentBuilder, times(1)).setResult(result);
    }
    
    @Test
    public void setResult_whenDocumentBuilderIsNull_shouldNotCallSetResultOnDocumentBuilder(){
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setHandler(null);
        
        jdomResult.setResult(result);
        
        Mockito.verifyNoInteractions(documentBuilder);
    }
    
    @Test
    public void setLexicalHandler_shouldNotDoAnything(){}
    
    @Test
    public void getResult_whenResultListAndResultDocAreNull_shouldCallGetResultOnDocumentBuilderAndSetResultListAndResetResultDocAndQueriedFlag(){
        JDOMResult jdomResult = new JDOMResult();
        DocumentBuilder documentBuilder = Mockito.mock(DocumentBuilder.class);
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(new Element("element"));
        when(documentBuilder.getResult()).thenReturn(expectedResult);
        jdomResult.setHandler(documentBuilder);
        
        List<Content> actualResult = jdomResult.getResult();
        
        assertEquals(expectedResult, actualResult);
        assertNull(jdomResult.getResultDoc());
        assertTrue(jdomResult.isQueried());
    }
    
    @Test
    public void getResult_whenDocumentBuilderIsNull_shouldReturnNull(){
        JDOMResult jdomResult = new JDOMResult();
        List<Content> actualResult = jdomResult.getResult();
        
        assertNull(actualResult);
    }
    
    @Test
    public void setResult_shouldSetResultListAndSetQueriedFlagToFalse(){}
    
    @Test
    public void getResult_whenResultListIsNotNullAndQueriedFlagIsTrue_shouldReturnResultList(){}
    
    @Test
    public void getResult_whenResultDocIsNotNullAndQueriedFlagIsFalse_shouldCreateNewArrayListAddContentToItResetResultDocSetResultListAndSetQueriedFlag(){}
    
    @Test
    public void setDocument_shouldSetResultDocAndResetResultListAndQueriedFlag(){}
    
    @Test
    public void getDocument_whenResultDocIsNotNullAndQueriedFlagIsTrue_shouldReturnResultDoc(){}
    
    @Test
    public void getDocument_whenResultListIsNotNullAndQueriedFlagIsFalse_shouldCreateDocumentAndSetContentFromResultList(){}
    
    @Test
    public void setFactory_shouldSetFactory(){}
    
    @Test
    public void setResult_whenDocumentBuilderIsNotNull_shouldSetResultOnDocumentBuilder(){}
    
    @Test
    public void setResult_whenDocumentBuilderIsNull_shouldNotSetResultOnDocumentBuilder(){}
    
    @Test
    public void setLexicalHandler_shouldNotDoAnything(){}
    
    @Test
    public void getResult_whenResultListAndResultDocAreNull_shouldCallGetResultOnDocumentBuilderAndSetResultListAndResetResultDocAndQueriedFlag(){}
    
    @Test
    public void getResult_whenDocumentBuilderIsNull_shouldReturnNull(){}

}
