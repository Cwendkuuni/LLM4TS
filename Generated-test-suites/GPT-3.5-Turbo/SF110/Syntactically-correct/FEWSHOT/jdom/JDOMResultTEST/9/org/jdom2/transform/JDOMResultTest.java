package org.jdom2.transform;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JDOMResultTest {
    private JDOMResult jdomResult;

    @Before
    public void setUp() {
        jdomResult = new JDOMResult();
    }

    @Test
    public void testSetResult() {
        jdomResult.setResult(null);
        assertNull(jdomResult.getResult());

        jdomResult.setResult(Collections.emptyList());
        assertEquals(Collections.emptyList(), jdomResult.getResult());

        List<Content> contentList = new ArrayList<>();
        Element element = new Element("test");
        element.setText("content");
        contentList.add(element);
        jdomResult.setResult(contentList);
        assertEquals(contentList, jdomResult.getResult());
    }

    @Test
    public void testGetResult() {
        assertNull(jdomResult.getResult());

        List<Content> contentList = new ArrayList<>();
        Element element = new Element("test");
        element.setText("content");
        contentList.add(element);
        jdomResult.setResult(contentList);
        assertEquals(contentList, jdomResult.getResult());
    }

    @Test
    public void testSetDocument() {
        jdomResult.setDocument(null);
        assertNull(jdomResult.getDocument());

        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument() {
        assertNull(jdomResult.getDocument());

        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void testSetFactory() {
        jdomResult.setFactory(null);
        assertNull(jdomResult.getFactory());

        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory() {
        assertNull(jdomResult.getFactory());

        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }
}