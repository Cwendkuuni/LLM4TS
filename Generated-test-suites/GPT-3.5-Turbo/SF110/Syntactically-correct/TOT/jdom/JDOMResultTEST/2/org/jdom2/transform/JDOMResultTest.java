
package org.jdom2.transform;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMResultTest {

    @Test
    public void setResult_typicalCase() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        Content content = new Element("test", "ns");
        contentList.add(content);
        jdomResult.setResult(contentList);
        assertEquals(contentList, jdomResult.getResult());
    }

    @Test
    public void setResult_emptyList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        jdomResult.setResult(contentList);
        assertEquals(contentList, jdomResult.getResult());
    }

    @Test
    public void getResult_withoutSettingResult() {
        JDOMResult jdomResult = new JDOMResult();
        assertEquals(Collections.emptyList(), jdomResult.getResult());
    }

    @Test
    public void setResult_multipleCalls() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        Content content = new Element("test", "ns");
        contentList.add(content);
        jdomResult.setResult(contentList);
        assertEquals(contentList, jdomResult.getResult());
        List<Content> contentList2 = new ArrayList<>();
        Content content2 = new Element("test2", "ns");
        contentList2.add(content2);
        jdomResult.setResult(contentList2);
        assertEquals(contentList2, jdomResult.getResult());
    }

    @Test
    public void setDocument_typicalCase() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void setDocument_nullDocument() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(null);
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void getDocument_withoutSettingDocument() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void setDocument_multipleCalls() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
        Document document2 = new Document();
        jdomResult.setDocument(document2);
        assertEquals(document2, jdomResult.getDocument());
    }

    @Test
    public void setFactory_typicalCase() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void setFactory_nullFactory() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(null);
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void getFactory_withoutSettingFactory() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void setFactory_multipleCalls() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
        JDOMFactory factory2 = new DefaultJDOMFactory();
        jdomResult.setFactory(factory2);
        assertEquals(factory2, jdomResult.getFactory());
    }
}

