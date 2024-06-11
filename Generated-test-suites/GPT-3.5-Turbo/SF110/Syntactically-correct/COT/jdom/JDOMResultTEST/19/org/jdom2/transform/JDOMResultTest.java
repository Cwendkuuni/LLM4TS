import org.junit.Test;
import org.jdom2.*;
import org.jdom2.transform.JDOMResult;

import java.util.*;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        // Test case 1: Check if result list is set correctly
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        Content content = new Element("element");
        resultList.add(content);
        jdomResult.setResult(resultList);
        assert(jdomResult.getResult().contains(content));
    }

    @Test
    public void testGetResult() {
        // Test case 1: Check if result list is retrieved correctly
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        Content content = new Element("element");
        resultList.add(content);
        jdomResult.setResult(resultList);
        assert(jdomResult.getResult().contains(content));

        // Test case 2: Check if result list is empty when not set
        jdomResult = new JDOMResult();
        assert(jdomResult.getResult().isEmpty());

        // Test case 3: Check if result document is converted correctly to result list
        jdomResult = new JDOMResult();
        Document document = new Document();
        Element element = new Element("element");
        document.addContent(element);
        jdomResult.setDocument(document);
        assert(jdomResult.getResult().contains(element));
    }

    @Test
    public void testSetDocument() {
        // Test case 1: Check if result document is set correctly
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        Element element = new Element("element");
        document.addContent(element);
        jdomResult.setDocument(document);
        assert(jdomResult.getDocument().getRootElement().equals(element));
    }

    @Test
    public void testGetDocument() {
        // Test case 1: Check if result document is retrieved correctly
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        Element element = new Element("element");
        document.addContent(element);
        jdomResult.setDocument(document);
        assert(jdomResult.getDocument().getRootElement().equals(element));

        // Test case 2: Check if result document is null when not set
        jdomResult = new JDOMResult();
        assert(jdomResult.getDocument() == null);
    }

    @Test
    public void testSetFactory() {
        // Test case 1: Check if JDOM factory is set correctly
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assert(jdomResult.getFactory().equals(factory));
    }

    @Test
    public void testGetFactory() {
        // Test case 1: Check if JDOM factory is retrieved correctly
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assert(jdomResult.getFactory().equals(factory));
    }
}