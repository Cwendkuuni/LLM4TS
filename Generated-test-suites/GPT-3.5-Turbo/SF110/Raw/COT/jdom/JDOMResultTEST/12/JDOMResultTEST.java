import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;

public class JDOMResultTest {
    
    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> expected = new ArrayList<Content>();
        expected.add(new Element("test"));
        result.setResult(expected);
        List<Content> actual = result.getResult();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> expected = new ArrayList<Content>();
        expected.add(new Element("test"));
        result.setResult(expected);
        List<Content> actual = result.getResult();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document expected = new Document(new Element("test"));
        result.setDocument(expected);
        Document actual = result.getDocument();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        Document expected = new Document(new Element("test"));
        result.setDocument(expected);
        Document actual = result.getDocument();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        JDOMFactory actual = result.getFactory();
        assertEquals(factory, actual);
    }
    
    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        JDOMFactory actual = result.getFactory();
        assertEquals(factory, actual);
    }
    
    @Test
    public void testSetHandler() {
        JDOMResult result = new JDOMResult();
        // Testing setHandler does not have any functionality, no assertions needed
        result.setHandler(null);
    }
    
    @Test
    public void testSetLexicalHandler() {
        JDOMResult result = new JDOMResult();
        // Testing setLexicalHandler does not have any functionality, no assertions needed
        result.setLexicalHandler(null);
    }

}