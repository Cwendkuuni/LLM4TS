import org.jdom2.transform.*;
import org.jdom2.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
       
        result.setResult(contentList);
       
        assertEquals(contentList, result.getResult());
    }
    
    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element"));
       
        result.setResult(contentList);
       
        assertEquals(contentList, result.getResult());
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
       
        result.setDocument(document);
       
        assertEquals(document, result.getDocument());
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        Element rootElement = new Element("root");
        document.setRootElement(rootElement);
       
        result.setDocument(document);
       
        assertEquals(document, result.getDocument());
    }
    
    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
       
        result.setFactory(factory);
       
        assertEquals(factory, result.getFactory());
    }

}