package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class W3CDomTest {
    private W3CDom w3cDom;
    private Document jsoupDoc;
    private Document w3cDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testFromJsoup() {
        assertThat(w3cDoc, is(notNullValue()));
    }

    @Test
    public void testConvert() {
        Element rootElement = w3cDoc.createElement("root");
        jsoupDoc.appendChild(rootElement);

        Element element = w3cDoc.createElement("element");
        rootElement.appendChild(element);

        w3cDom.convert(jsoupDoc, w3cDoc);

        Node convertedRootElement = w3cDoc.getDocumentElement();
        assertThat(convertedRootElement, is(instanceOf(Element.class)));
        assertThat(convertedRootElement.getNodeName(), is(equalTo("root")));
        
        Node convertedElement = convertedRootElement.getFirstChild();
        assertThat(convertedElement, is(instanceOf(Element.class)));
        assertThat(convertedElement.getNodeName(), is(equalTo("element")));
    }

    @Test
    public void testAsString() {
        String xmlString = w3cDom.asString(w3cDoc);
        assertThat(xmlString, is(notNullValue()));
        // Add more assertions to check the correctness of the XML string
    }

}