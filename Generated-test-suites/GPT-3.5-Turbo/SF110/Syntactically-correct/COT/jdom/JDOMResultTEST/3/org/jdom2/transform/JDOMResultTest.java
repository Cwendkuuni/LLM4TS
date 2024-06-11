
import static org.junit.Assert.*;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testJDOMResult() {
        JDOMResult result = new JDOMResult();
        assertNotNull(result);
    }

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contents = new ArrayList<>();
        result.setResult(contents);
        assertEquals(contents, result.getResult());
    }

    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contents = new ArrayList<>();
        result.setResult(contents);
        assertEquals(contents, result.getResult());
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

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }
    
}
