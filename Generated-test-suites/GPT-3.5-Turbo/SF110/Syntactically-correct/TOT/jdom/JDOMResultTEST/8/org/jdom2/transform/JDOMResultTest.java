import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.jdom2.*;
import org.xml.sax.*;
import javax.xml.transform.sax.*;

public class JDOMResultTest {

    @Test
    public void testSetResult_typical() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        contentList.add(element1);
        contentList.add(element2);
        
        jdomResult.setResult(contentList);
        
        List<Content> result = jdomResult.getResult();
        
        assertSame(contentList, result);
    }
    
    @Test
    public void testSetResult_emptyList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = Collections.emptyList();
        
        jdomResult.setResult(contentList);
        
        List<Content> result = jdomResult.getResult();
        
        assertSame(contentList, result);
    }
    
    @Test
    public void testSetResult_nullInput() {
        JDOMResult jdomResult = new JDOMResult();
        
        jdomResult.setResult(null);
        
        List<Content> result = jdomResult.getResult();
        
        assertNotSame(null, result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testGetResult_typical() {
        List<Content> contentList = new ArrayList<>();
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        contentList.add(element1);
        contentList.add(element2);
        
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(contentList);
        
        List<Content> result = jdomResult.getResult();
        
        assertSame(contentList, result);
    }
    
    @Test
    public void testGetResult_emptyResultList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = jdomResult.getResult();
        
        assertSame(Collections.emptyList(), result);
    }
    
    @Test
    public void testGetResult_noResultList_noResultDocument() {
        JDOMResult jdomResult = new JDOMResult();
        
        List<Content> result = jdomResult.getResult();
        
        assertSame(Collections.emptyList(), result);
    }
    
    @Test
    public void testSetDocument_typical() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("root"));
        
        jdomResult.setDocument(document);
        
        Document result = jdomResult.getDocument();
        
        assertSame(document, result);
    }
    
    @Test
    public void testSetDocument_nullInput() {
        JDOMResult jdomResult = new JDOMResult();
        
        jdomResult.setDocument(null);
        
        Document result = jdomResult.getDocument();
        
        assertSame(null, result);
    }
    
    @Test
    public void testGetDocument_typical() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document(new Element("root"));
        jdomResult.setDocument(document);
        
        Document result = jdomResult.getDocument();
        
        assertSame(document, result);
    }
    
    @Test
    public void testGetDocument_noResultDocument_noResultList() {
        JDOMResult jdomResult = new JDOMResult();
        
        Document result = jdomResult.getDocument();
        
        assertSame(null, result);
    }
    
    @Test
    public void testSetFactory_typical() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        
        jdomResult.setFactory(factory);
        
        JDOMFactory result = jdomResult.getFactory();
        
        assertSame(factory, result);
    }
    
    @Test
    public void testGetFactory_typical() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        
        JDOMFactory result = jdomResult.getFactory();
        
        assertSame(factory, result);
    }

}