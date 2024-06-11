
import org.jsoup.helper.W3CDom;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;

import static org.junit.jupiter.api.Assertions.*;

class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @BeforeEach
    void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
    }

    @Test
    void testFromJsoup() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    void testConvert() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        assertNotNull(xmlString);
    }

    @Test
    void testW3CBuilderHeadElement() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tagName", "");
        w3cBuilder.head(jsoupElement, 0);
        Element w3cElement = (Element) w3cDoc.getDocumentElement();
        assertNotNull(w3cElement);
    }

    @Test
    void testW3CBuilderHeadTextNode() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("text", "");
        w3cBuilder.head(jsoupTextNode, 0);
        Text w3cText = (Text) w3cDoc.getDocumentElement();
        assertNotNull(w3cText);
    }

    @Test
    void testW3CBuilderHeadComment() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("comment", "");
        w3cBuilder.head(jsoupComment, 0);
        Comment w3cComment = (Comment) w3cDoc.getDocumentElement();
        assertNotNull(w3cComment);
    }

    @Test
    void testW3CBuilderHeadDataNode() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("data", "");
        w3cBuilder.head(jsoupDataNode, 0);
        Text w3cText = (Text) w3cDoc.getDocumentElement();
        assertNotNull(w3cText);
    }

    @Test
    void testW3CBuilderTail() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tagName", "");
        w3cBuilder.head(jsoupElement, 0);
        w3cBuilder.tail(jsoupElement, 0);
        Element w3cElement = (Element) w3cDoc.getDocumentElement();
        assertNotNull(w3cElement);
    }

}
