import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.transform.*;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<>();
        Element element = new Element("test");
        content.add(element);
        result.setResult(content);
        List<Content> resultContent = result.getResult();
        assertEquals(content, resultContent);
    }
    
    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<>();
        Element element = new Element("test");
        content.add(element);
        result.setResult(content);
        List<Content> resultContent = result.getResult();
        assertEquals(content, resultContent);
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        result.setDocument(document);
        Document resultDocument = result.getDocument();
        assertEquals(document, resultDocument);
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        result.setDocument(document);
        Document resultDocument = result.getDocument();
        assertEquals(document, resultDocument);
    }
    
    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        JDOMFactory resultFactory = result.getFactory();
        assertEquals(factory, resultFactory);
    }
    
    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        JDOMFactory resultFactory = result.getFactory();
        assertEquals(factory, resultFactory);
    }
}