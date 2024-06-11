package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup_validInput_returnsW3CDocument() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_nullInput_throwsIllegalArgumentException() {
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert_validInput_convertsToW3CDocument() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document out = w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_nullInput_throwsIllegalArgumentException() {
        w3cDom.convert(null, null);
    }

    @Test
    public void testConvert_blankLocation_setsDocumentURI() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals("", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_nonBlankLocation_setsDocumentURI() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("http://example.com");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_Element_addsElementToDocument() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        w3cDom.convert(jsoupDoc, w3cDoc);

        Element[] elements = w3cDoc.getElementsByTagName("div");
        assertEquals(1, elements.length);
        assertEquals("div", elements[0].getTagName());
    }

    @Test
    public void testConvert_TextNode_addsTextNodeToDocument() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("Hello World");
        w3cDom.convert(jsoupDoc, w3cDoc);

        Element[] elements = w3cDoc.getElementsByTagName("body");
        assertEquals(1, elements.length);
        
        Text[] textNodes = elements[0].getChildNodes().getTextContent();
        assertEquals(1, textNodes.length);
        assertEquals("Hello World", textNodes[0].getNodeValue());
    }

    @Test
    public void testConvert_Comment_addsCommentToDocument() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("This is a comment");
        w3cDom.convert(jsoupDoc, w3cDoc);

        Element[] elements = w3cDoc.getElementsByTagName("body");
        assertEquals(1, elements.length);
        
        Text[] comments = elements[0].getChildNodes().getComments();
        assertEquals(1, comments.length);
        assertEquals("This is a comment", comments[0].getData());
    }

    @Test
    public void testConvert_DataNode_addsDataNodeToDocument() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("This is a data node");
        w3cDom.convert(jsoupDoc, w3cDoc);

        Element[] elements = w3cDoc.getElementsByTagName("body");
        assertEquals(1, elements.length);
        
        Text[] dataNodes = elements[0].getChildNodes().getTextContent();
        assertEquals(1, dataNodes.length);
        assertEquals("This is a data node", dataNodes[0].getNodeValue());
    }

    @Test
    public void testAsString_validDocument_returnsStringRepresentation() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        String result = w3cDom.asString(w3cDoc);
        assertFalse(result.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsString_nullInput_throwsIllegalArgumentException() {
        w3cDom.asString(null);
    }
}