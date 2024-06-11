import org.jdom2.*;
import org.jdom2.transform.*;
import org.junit.*;
import java.util.*;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<>();
        content.add(new Element("element"));
        result.setResult(content);
        Assert.assertEquals(content, result.getResult());
    }

    @Test
    public void testGetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<>();
        content.add(new Element("element"));
        result.setResult(content);
        Assert.assertEquals(content, result.getResult());
    }

    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        result.setDocument(document);
        Assert.assertEquals(document, result.getDocument());
    }

    @Test
    public void testGetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        result.setDocument(document);
        Assert.assertEquals(document, result.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);
        Assert.assertEquals(factory, result.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);
        Assert.assertEquals(factory, result.getFactory());
    }

    @Test
    public void testSetHandler() {
        JDOMResult result = new JDOMResult();
        ContentHandler handler = new DefaultHandler();
        result.setHandler(handler);
    }

    @Test
    public void testSetLexicalHandler() {
        JDOMResult result = new JDOMResult();
        LexicalHandler handler = new DefaultHandler();
        result.setLexicalHandler(handler);
    }

}