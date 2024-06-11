import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.transform.JDOMResult;

public class JDOMResultTest {
    
    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        
        List<Content> testData = Arrays.asList(new Text("test"));
        result.setResult(testData);
        
        assertEquals(testData, result.getResult());
    }
    
    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        
        List<Content> testData = Arrays.asList(new Element("test"));
        result.setResult(testData);
        
        assertEquals(testData, result.getResult());
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        
        Document testData = new Document(new Element("test"));
        result.setDocument(testData);
        
        assertEquals(testData, result.getDocument());
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        
        Document testData = new Document();
        result.setDocument(testData);
        
        assertEquals(testData, result.getDocument());
    }
    
    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        
        JDOMFactory testData = new DefaultJDOMFactory();
        result.setFactory(testData);
        
        assertEquals(testData, result.getFactory());
    }
    
    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        
        JDOMFactory testData = new DefaultJDOMFactory();
        result.setFactory(testData);
        
        assertEquals(testData, result.getFactory());
    }
}