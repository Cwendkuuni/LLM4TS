
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collections;
import java.util.List;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.transform.JDOMResult;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = Collections.emptyList();
        result.setResult(content);
        assertEquals(content, result.getResult());
    }

    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = Collections.emptyList();
        result.setResult(content);
        assertEquals(content, result.getResult());
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
        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

    @Test
    public void testSetHandler() {
        JDOMResult result = new JDOMResult();
        // This method does not have any effect, so nothing to test
    }

    @Test
    public void testSetLexicalHandler() {
        JDOMResult result = new JDOMResult();
        // This method does not have any effect, so nothing to test
    }

}
