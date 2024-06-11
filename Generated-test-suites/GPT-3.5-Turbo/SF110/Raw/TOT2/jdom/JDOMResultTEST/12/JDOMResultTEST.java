import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.JDOMFactory;

public class JDOMResultTest {
    
    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        jdomResult.setResult(resultList);
        assertEquals(resultList, jdomResult.getResult());
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }
    
    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }
    
    @Test
    public void testFragmentHandler() {
        JDOMResult.FragmentHandler fragmentHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        List<Content> contentList = new ArrayList<>();
        assertEquals(contentList, fragmentHandler.getResult());
    }
    
    @Test
    public void testDocumentBuilder() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMResult.DocumentBuilder documentBuilder = jdomResult.new DocumentBuilder();
        assertNotNull(documentBuilder);
    }
}