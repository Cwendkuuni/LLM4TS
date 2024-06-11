import static org.junit.Assert.*;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        jdomResult.setResult(contentList);
        assertEquals(contentList, jdomResult.getResult());
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
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        jdomResult.setResult(contentList);
        jdomResult.retrieveResult();
        assertNotNull(jdomResult.getResult());
    }

    @Test
    public void testDocumentBuilder() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMResult.DocumentBuilder documentBuilder = jdomResult.new DocumentBuilder();
        List<Content> contentList = new ArrayList<>();
        jdomResult.setResult(contentList);
        documentBuilder.startDocument();
        assertEquals(contentList, documentBuilder.getResult());
    }

    @Test
    public void testFragmentHandler() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMResult.FragmentHandler fragmentHandler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
        assertNotNull(fragmentHandler.getResult());
    }
}