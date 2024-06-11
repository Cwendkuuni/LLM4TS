package org.jdom2.transform;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.sax.SAXResult;
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
    public void setResult_setResultList() {
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element"));

        jdomResult.setResult(contentList);

        Assert.assertEquals(contentList, jdomResult.getResult());
    }

    @Test
    public void getResult_noResultList() {
        Assert.assertEquals(Collections.emptyList(), jdomResult.getResult());
    }

    @Test
    public void getResult_resultList() {
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element"));

        jdomResult.setResult(contentList);

        Assert.assertEquals(contentList, jdomResult.getResult());
        Assert.assertTrue(jdomResult.getResult().isEmpty());
    }

    @Test
    public void getResult_resultDocument() {
        Element element = new Element("element");
        Document document = new Document(element);

        jdomResult.setDocument(document);

        Assert.assertEquals(Collections.emptyList(), jdomResult.getResult());
    }

    @Test
    public void getResult_queryResultList() {
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element"));

        jdomResult.setResult(contentList);

        Assert.assertEquals(contentList, jdomResult.getResult());

        jdomResult.setResult(null);

        Assert.assertEquals(contentList, jdomResult.getResult());
    }

    @Test
    public void setDocument_setResultDocument() {
        Element element = new Element("element");
        Document document = new Document(element);

        jdomResult.setDocument(document);

        Assert.assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void getDocument_noResultDocument() {
        Assert.assertNull(jdomResult.getDocument());
    }

    @Test
    public void getDocument_resultList() {
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element"));

        jdomResult.setResult(contentList);

        Assert.assertNull(jdomResult.getDocument());
    }

    @Test
    public void getDocument_resultDocument() {
        Element element = new Element("element");
        Document document = new Document(element);

        jdomResult.setDocument(document);

        Assert.assertEquals(document, jdomResult.getDocument());
        Assert.assertNull(jdomResult.getDocument());
    }

    @Test
    public void getDocument_queryResultDocument() {
        Element element = new Element("element");
        Document document = new Document(element);

        jdomResult.setDocument(document);

        Assert.assertEquals(document, jdomResult.getDocument());

        jdomResult.setDocument(null);

        Assert.assertEquals(document, jdomResult.getDocument());
    }

    @Test
    public void setFactory_getFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();

        jdomResult.setFactory(factory);

        Assert.assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void retrieveResult_resultListNotNull() {
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element"));

        jdomResult.setResult(contentList);

        jdomResult.retrieveResult();

        Assert.assertEquals(contentList, jdomResult.getResult());
    }

    @Test
    public void retrieveResult_resultDocumentNotNull() {
        Element element = new Element("element");
        Document document = new Document(element);

        jdomResult.setDocument(document);

        jdomResult.retrieveResult();

        Assert.assertNull(jdomResult.getResult());
        Assert.assertEquals(document, jdomResult.getDocument());
    }
}