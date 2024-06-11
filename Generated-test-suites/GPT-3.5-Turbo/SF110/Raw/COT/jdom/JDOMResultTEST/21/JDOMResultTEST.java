
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import org.jdom2.*;
import org.jdom2.transform.JDOMResult;

public class JDOMResultTest {

    private JDOMResult jdomResult;

    @Before
    public void setup() {
        jdomResult = new JDOMResult();
    }

    @Test
    public void testSetResult() {
        List<Content> result = new ArrayList<>();
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void testGetResult_withoutSetResult() {
        List<Content> expected = Collections.emptyList();
        assertEquals(expected, jdomResult.getResult());
    }

    @Test
    public void testGetResult_withResultdoc() {
        Document document = new Document();
        jdomResult.setDocument(document);
        List<Content> expected = new ArrayList<>();
        assertEquals(expected, jdomResult.getResult());
    }

    @Test
    public void testSetDocument() {
        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument_withoutSetDocument() {
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory_withoutSetFactory() {
        assertNull(jdomResult.getFactory());
    }
}
