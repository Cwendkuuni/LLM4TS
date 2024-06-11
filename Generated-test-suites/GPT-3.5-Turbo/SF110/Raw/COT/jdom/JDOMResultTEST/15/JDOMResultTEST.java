
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class JDOMResultTest {
    private JDOMResult jdomResult;

    @Before
    public void setUp() {
        jdomResult = new JDOMResult();
    }

    @Test
    public void testSetResult() {
        List<Content> result = new ArrayList<>();
        Element element = new Element("test");
        result.add(element);
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void testGetResult_nullResult() {
        List<Content> expectedResult = Collections.emptyList();
        assertEquals(expectedResult, jdomResult.getResult());
    }

    @Test
    public void testGetResult_resultNotNull() {
        List<Content> expectedResult = new ArrayList<>();
        Element element = new Element("test");
        expectedResult.add(element);
        jdomResult.setResult(expectedResult);
        assertEquals(expectedResult, jdomResult.getResult());
    }

    @Test
    public void testSetDocument() {
        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument_nullDocument() {
        Document expectedResult = null;
        assertEquals(expectedResult, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument_documentNotNull() {
        List<Content> content = new ArrayList<>();
        Element element = new Element("test");
        content.add(element);
        jdomResult.setResult(content);
        Document expectedDocument = new Document();
        expectedDocument.setContent(content);
        assertEquals(expectedDocument, jdomResult.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }
}

