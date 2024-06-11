import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import org.jdom2.transform.JDOMResult;

import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        List<Content> contentList = new ArrayList<>();
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

    @Test
    public void testSetFactory() {
        JDOMFactory factory = new JDOMFactory();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.retrieveResult();
        assertNotNull(jdomResult.getResult());
    }

    @Test
    public void testFragmentHandlerGetResult() {
        JDOMFactory factory = new DefaultJDOMFactory();
        JDOMResult.FragmentHandler fragmentHandler = new JDOMResult.FragmentHandler(factory);
        assertNotNull(fragmentHandler.getResult());
    }

    @Test
    public void testDocumentBuilderGetResult() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMResult.DocumentBuilder documentBuilder = jdomResult.new DocumentBuilder();
        assertNotNull(documentBuilder.getResult());
    }

}