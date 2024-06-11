import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.transform.JDOMResult;
import org.jdom2.input.SAXBuilder;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        result.setResult(contentList);
        List<Content> resultContentList = result.getResult();
        Assert.assertEquals(contentList, resultContentList);
    }

    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        result.setResult(contentList);
        List<Content> resultContentList = result.getResult();
        Assert.assertEquals(contentList, resultContentList);
    }

    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        result.setDocument(doc);
        Document resultDoc = result.getDocument();
        Assert.assertEquals(doc, resultDoc);
    }

    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        result.setDocument(doc);
        Document resultDoc = result.getDocument();
        Assert.assertEquals(doc, resultDoc);
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);
        JDOMFactory resultFactory = result.getFactory();
        Assert.assertEquals(factory, resultFactory);
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);
        JDOMFactory resultFactory = result.getFactory();
        Assert.assertEquals(factory, resultFactory);
    }

    @Test
    public void testSetHandler() {
        JDOMResult result = new JDOMResult();
        // TODO: Implement test case
    }

    @Test
    public void testSetLexicalHandler() {
        JDOMResult result = new JDOMResult();
        // TODO: Implement test case
    }

    @Test
    public void testFragmentHandlerGetResult() {
        JDOMResult.FragmentHandler fragmentHandler = new JDOMResult.FragmentHandler(null);
        List<Content> result = fragmentHandler.getResult();
        Assert.assertNotNull(result);
    }

    @Test
    public void testDocumentBuilderGetResult() {
        JDOMResult.DocumentBuilder documentBuilder = new JDOMResult.DocumentBuilder();
        List<Content> result = documentBuilder.getResult();
        Assert.assertNull(result);
    }

}