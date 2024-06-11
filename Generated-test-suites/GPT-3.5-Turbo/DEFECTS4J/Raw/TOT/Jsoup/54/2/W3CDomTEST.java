import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private W3CDom w3cdom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setup() {
        w3cdom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
    }

    // Test fromJsoup() method
    @Test
    public void testFromJsoup() {
        Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
        assertTrue(w3cDoc instanceof org.w3c.dom.Document);
    }

    // Test convert() method
    @Test
    public void testConvert() {
        org.w3c.dom.Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
        w3cdom.convert(jsoupDoc, w3cDoc);

        Element rootElement = jsoupDoc.child(0);
        assertEquals(rootElement.tagName(), w3cDoc.getDocumentElement().getNodeName());
    }

    // Test W3CBuilder head() method for Element node
    @Test
    public void testW3CBuilderHeadForElement() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div", "");
        org.w3c.dom.Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        w3cBuilder.head(jsoupElement, 0);

        Element w3cElement = w3cDoc.getDocumentElement();
        assertEquals(jsoupElement.tagName(), w3cElement.getTagName());
    }

    // Test W3CBuilder head() method for TextNode
    @Test
    public void testW3CBuilderHeadForTextNode() {
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("Some Text", "");
        org.w3c.dom.Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        w3cBuilder.head(jsoupTextNode, 0);

        Text w3cText = (Text) w3cDoc.getChildNodes().item(0);
        assertEquals(jsoupTextNode.getWholeText(), w3cText.getWholeText());
    }

    // Test W3CBuilder head() method for Comment
    @Test
    public void testW3CBuilderHeadForComment() {
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("Some Comment", "");
        org.w3c.dom.Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        w3cBuilder.head(jsoupComment, 0);

        org.w3c.dom.Comment w3cComment = (org.w3c.dom.Comment) w3cDoc.getChildNodes().item(0);
        assertEquals(jsoupComment.getData(), w3cComment.getData());
    }

    // Test W3CBuilder head() method for DataNode
    @Test
    public void testW3CBuilderHeadForDataNode() {
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("Some Data", "");
        org.w3c.dom.Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        w3cBuilder.head(jsoupDataNode, 0);

        Text w3cText = (Text) w3cDoc.getChildNodes().item(0);
        assertEquals(jsoupDataNode.getWholeData(), w3cText.getWholeText());
    }

    // Test W3CBuilder tail() method
    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div", "");
        org.w3c.dom.Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        w3cBuilder.head(jsoupElement, 0);
        w3cBuilder.tail(jsoupElement, 0);

        assertEquals(null, w3cBuilder.dest);
    }

    // Test asString() method
    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
        String xmlString = w3cdom.asString(w3cDoc);

        assertTrue(xmlString instanceof String);
    }
}