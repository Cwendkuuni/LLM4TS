import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.*;
import org.jdom2.transform.DefaultJDOMFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMResultTest {

    private JDOMResult jdomResult;

    @Before
    public void setUp() {
        jdomResult = new JDOMResult();
    }

    @Test
    public void setResult_shouldSetResultListAndResetQueried() {
        List<Content> result = new ArrayList<>();
        Content content = new Element("test");
        result.add(content);

        jdomResult.setResult(result);

        Assert.assertEquals(result, jdomResult.getResult());
        Assert.assertFalse(jdomResult.isQueried());
    }

    @Test
    public void getResult_shouldReturnEmptyListWhenQueriedAndResultListIsNull() {
        jdomResult.setResult(null);

        jdomResult.setQueried(true);

        List<Content> expected = Collections.emptyList();
        Assert.assertEquals(expected, jdomResult.getResult());
    }

    @Test
    public void getResult_shouldReturnResultListWhenResultListIsNotNullAndNotQueried() {
        List<Content> result = new ArrayList<>();
        Content content = new Element("test");
        result.add(content);
        jdomResult.setResult(result);

        jdomResult.setQueried(false);

        Assert.assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void getResult_shouldReturnResultListWhenResultListIsNotNullAndQueried() {
        List<Content> result = new ArrayList<>();
        Content content = new Element("test");
        result.add(content);
        jdomResult.setResult(result);
        jdomResult.setQueried(true);

        Assert.assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void getResult_shouldReturnEmptyListWhenResultListIsNullAndQueried() {
        jdomResult.setResult(null);
        jdomResult.setQueried(true);

        List<Content> expected = Collections.emptyList();
        Assert.assertEquals(expected, jdomResult.getResult());
    }

    @Test
    public void setDocument_shouldSetResultDocumentAndResetResultListAndQueried() {
        Document document = new Document();
        jdomResult.setDocument(document);

        Assert.assertEquals(document, jdomResult.getDocument());
        Assert.assertNull(jdomResult.getResult());
        Assert.assertFalse(jdomResult.isQueried());
    }

    @Test
    public void getDocument_shouldReturnNullWhenQueriedAndResultDocumentIsNull() {
        jdomResult.setDocument(null);

        jdomResult.setQueried(true);

        Assert.assertNull(jdomResult.getDocument());
    }

    @Test
    public void getDocument_shouldReturnResultDocumentWhenResultDocumentIsNotNullAndNotQueried() {
        Document document = new Document();
        jdomResult.setDocument(document);

        jdomResult.setQueried(false);

        Assert.assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void getDocument_shouldCreateResultDocumentWhenResultListIsNotNullAndNotQueried() {
        List<Content> result = new ArrayList<>();
        Content content = new Element("test");
        result.add(content);
        jdomResult.setResult(result);
        jdomResult.setQueried(false);

        JDOMFactory factory = new DefaultJDOMFactory();
        Document expected = factory.document(null);
        expected.setContent(result);

        Assert.assertEquals(expected, jdomResult.getDocument());
    }

    @Test
    public void setFactory_shouldSetJDOMFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);

        Assert.assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void retrieveResult_shouldSetResultListWhenResultListAndResultDocumentAreNull() {
        jdomResult = new JDOMResult();

        DocumentBuilder documentBuilder = (DocumentBuilder) jdomResult.getHandler();
        documentBuilder.setResult(null);

        jdomResult.retrieveResult();

        Assert.assertNotNull(jdomResult.getResult());
    }

    @Test
    public void setHandler_shouldNotSetHandler() {
        ContentHandler handler = new DefaultHandler();
        jdomResult.setHandler(handler);

        Assert.assertNull(jdomResult.getHandler());
    }

    @Test
    public void setLexicalHandler_shouldNotSetLexicalHandler() {
        LexicalHandler handler = new DefaultHandler();
        jdomResult.setLexicalHandler(handler);

        Assert.assertNull(jdomResult.getLexicalHandler());
    }
}