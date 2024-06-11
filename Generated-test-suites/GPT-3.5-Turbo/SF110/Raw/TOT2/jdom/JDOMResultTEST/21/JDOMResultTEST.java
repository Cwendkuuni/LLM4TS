import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class JDOMResultTest {
    
    @Test
    public void testSetResult() {
        List<Content> contentList = new ArrayList<>();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(contentList);
        assertEquals(contentList, jdomResult.getResult());
    }
    
    @Test
    public void testSetDocument() {
        Document document = new Document();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }
    
    @Test
    public void testSetFactory() {
        JDOMFactory factory = new JDOMFactory();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }
    
    @Test
    public void testRetrieveResult() {
        List<Content> contentList = new ArrayList<>();
        Document document = new Document();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(contentList);
        jdomResult.setDocument(document);
        jdomResult.retrieveResult();
        assertNotNull(jdomResult.getResult());
    }
}