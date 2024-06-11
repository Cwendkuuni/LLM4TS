import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.*;

public class JDOMResultTest {
    
    @Test
    public void testSetResult() {
        List<Content> contentList = new ArrayList<Content>();
        contentList.add(new Element("testElement"));
        JDOMResult jdomResult = new JDOMResult();
        
        jdomResult.setResult(contentList);
        
        assertEquals(contentList, jdomResult.getResult());
    }
    
    @Test
    public void testSetDocumentAndGetDocument() throws IOException, JDOMException {
        List<Content> contentList = new ArrayList<Content>();
        contentList.add(new Element("testElement"));
        
        Document testDoc = new Document();
        testDoc.setContent(contentList);
        
        JDOMResult jdomResult = new JDOMResult();
        
        jdomResult.setDocument(testDoc);
        
        assertEquals(testDoc, jdomResult.getDocument());
    }
    
    @Test
    public void testSetFactoryAndGetFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        JDOMResult jdomResult = new JDOMResult();
        
        jdomResult.setFactory(factory);
        
        assertEquals(factory, jdomResult.getFactory());
    }
}