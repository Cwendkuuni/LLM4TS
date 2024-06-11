import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.transform.JDOMResult;

public class JDOMResultTest {

    @Test
    public void setResultTest() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        Element element1 = new Element("element1");
        Content content1 = new Text("text1");
        element1.addContent(content1);
        result.add(element1);
        
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }
    
    @Test
    public void getResultTest() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        Element element1 = new Element("element1");
        Content content1 = new Text("text1");
        element1.addContent(content1);
        result.add(element1);
        jdomResult.setResult(result);
        
        List<Content> actualResult = jdomResult.getResult();
        
        assertEquals(result, actualResult);
    }
    
    @Test
    public void setDocumentTest() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        document.setRootElement(new Element("root"));
        
        jdomResult.setDocument(document);
        
        assertEquals(document, jdomResult.getDocument());
    }
    
    @Test
    public void getDocumentTest() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        Element element1 = new Element("element1");
        Content content1 = new Text("text1");
        element1.addContent(content1);
        document.setRootElement(element1);
        jdomResult.setDocument(document);
        
        Document actualDocument = jdomResult.getDocument();
        
        assertEquals(document, actualDocument);
    }
    
    @Test
    public void setFactoryTest() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        
        jdomResult.setFactory(factory);
        
        assertEquals(factory, jdomResult.getFactory());
    }
    
    @Test
    public void getFactoryTest() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        jdomResult.setFactory(factory);
        
        JDOMFactory actualFactory = jdomResult.getFactory();
        
        assertEquals(factory, actualFactory);
    }
}