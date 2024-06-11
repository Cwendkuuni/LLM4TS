import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import java.util.*;
import org.jdom2.input.SAXBuilder;

public class JDOMResultTest {
    
    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
        jdomResult.setResult(null);
        assertEquals(Collections.emptyList(), jdomResult.getResult());
    }
    
    @Test
    public void testGetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element"));
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
        assertEquals(result, jdomResult.getResult());
        jdomResult.setResult(null);
        assertEquals(result, jdomResult.getResult());
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
        jdomResult.setDocument(null);
        assertNull(jdomResult.getDocument());
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
        assertEquals(document, jdomResult.getDocument());
        jdomResult.setDocument(null);
        assertEquals(document, jdomResult.getDocument());
    }
    
    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }
    
    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }
    
}