import org.jdom2.*;
import org.junit.*;
import static org.junit.Assert.*;

public class JDOMResultTest {
    
    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<Content>();
        contentList.add(new Element("element"));
        result.setResult(contentList);
        List<Content> resultContentList = result.getResult();
        
        assertEquals(contentList, resultContentList);
    }
    
    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<Content>();
        contentList.add(new Element("element"));
        result.setResult(contentList);
        List<Content> resultContentList = result.getResult();
        
        assertEquals(contentList, resultContentList);
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document(new Element("element"));
        result.setDocument(document);
        Document resultDocument = result.getDocument();
        
        assertEquals(document, resultDocument);
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document(new Element("element"));
        result.setDocument(document);
        Document resultDocument = result.getDocument();
        
        assertEquals(document, resultDocument);
    }
    
    @Test
    public void testSetFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        
        JDOMResult result = new JDOMResult();
        result.setFactory(factory);
        
        assertEquals(factory, result.getFactory());
    }
    
    @Test
    public void testGetFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        
        JDOMResult result = new JDOMResult();
        result.setFactory(factory);
        
        assertEquals(factory, result.getFactory());
    }
}