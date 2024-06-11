import static org.junit.Assert.*;

import org.junit.Test;

public class JDOMResultTest {
    
    @Test
    public void testSetResult() {
        List<Content> contentList = new ArrayList<>();
        Content content = new Element("test");
        contentList.add(content);
        
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(contentList);
        
        assertEquals(contentList, jdomResult.getResult());
    }
    
    @Test
    public void testGetResult() {
        List<Content> contentList = new ArrayList<>();
        Content content = new Element("test");
        contentList.add(content);
        
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
}