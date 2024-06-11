import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.helpers.DefaultHandler;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMResultTest {
    
    @Test
    public void setResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        Element element = new Element("test");
        contentList.add(element);
        result.setResult(contentList);
        assertEquals(contentList, result.getResult());
    }

    @Test
    public void getResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        Element element = new Element("test");
        contentList.add(element);
        result.setResult(contentList);
        assertEquals(contentList, result.getResult());
    }

    @Test
    public void setDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        result.setDocument(document);
        assertEquals(document, result.getDocument());
    }

    @Test
    public void getDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        result.setDocument(document);
        assertEquals(document, result.getDocument());
    }

    @Test
    public void setFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

    @Test
    public void getFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

    @Test
    public void retrieveResult() {
        JDOMResult result = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        Element element = new Element("test");
        contentList.add(element);
        result.setResult(contentList);
        result.retrieveResult();
        assertEquals(contentList, result.getResult());
    }

    @Test
    public void setHandler() {
        JDOMResult result = new JDOMResult();
        ContentHandler handler = new DefaultHandler();
        result.setHandler(handler);
        // No assertion since method has no effect
    }

    @Test
    public void setLexicalHandler() {
        JDOMResult result = new JDOMResult();
        LexicalHandler handler = new DefaultHandler();
        result.setLexicalHandler(handler);
        // No assertion since method has no effect
    }
}