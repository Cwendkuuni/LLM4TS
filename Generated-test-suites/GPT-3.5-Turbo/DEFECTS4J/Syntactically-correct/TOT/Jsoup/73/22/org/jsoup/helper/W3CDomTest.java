
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup_validInput_success() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        // Add additional assertions here
    }

    @Test
    public void testFromJsoup_nullInput_throwIllegalArgumentException() {
        try {
            w3cDom.fromJsoup(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Input document cannot be null", e.getMessage());
        }
    }

    @Test
    public void testConvert_validInput_success() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupRoot = new org.jsoup.nodes.Element("root");

        w3cDom.convert(jsoupDoc, w3cDoc);

        // Add additional assertions here
    }

    @Test
    public void testConvert_blankLocationSet_setDocumentURI() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.setBaseUri("http://example.com");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupRoot = new org.jsoup.nodes.Element("root");

        w3cDom.convert(jsoupDoc, w3cDoc);

        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_nullLocation_notSetDocumentURI() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupRoot = new org.jsoup.nodes.Element("root");

        w3cDom.convert(jsoupDoc, w3cDoc);

        assertEquals("", w3cDoc.getDocumentURI());
    }

    @Test
    public void testW3CBuilder_headElement_success() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupRoot = new org.jsoup.nodes.Element("root");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("element");
        jsoupElement.attr("attribute", "value");
        jsoupRoot.appendChild(jsoupElement);
        jsoupDoc.appendChild(jsoupRoot);

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(jsoupElement, 0);
        Element w3cElement = (Element) w3cDoc.getDocumentElement().getFirstChild();

        assertNotNull(w3cElement);
        assertEquals("element", w3cElement.getNodeName());
        assertEquals("value", w3cElement.getAttribute("attribute"));
    }

    @Test
    public void testW3CBuilder_textNode_success() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupRoot = new org.jsoup.nodes.Element("root");
        org.jsoup.nodes.TextNode jsoupText = new org.jsoup.nodes.TextNode("text");
        jsoupRoot.appendChild(jsoupText);
        jsoupDoc.appendChild(jsoupRoot);

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(jsoupText, 0);
        Text w3cText = (Text) w3cDoc.getDocumentElement().getFirstChild();

        assertNotNull(w3cText);
        assertEquals("text", w3cText.getWholeText());
    }

    @Test
    public void testW3CBuilder_commentElement_success() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupRoot = new org.jsoup.nodes.Element("root");
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("comment");
        jsoupRoot.appendChild(jsoupComment);
        jsoupDoc.appendChild(jsoupRoot);

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(jsoupComment, 0);
        org.w3c.dom.Comment w3cComment = (org.w3c.dom.Comment) w3cDoc.getDocumentElement().getFirstChild();

        assertNotNull(w3cComment);
        assertEquals("comment", w3cComment.getNodeValue());
    }

    @Test
    public void testW3CBuilder_dataNode_success() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupRoot = new org.jsoup.nodes.Element("root");
        org.jsoup.nodes.DataNode jsoupData = new org.jsoup.nodes.DataNode("data");
        jsoupRoot.appendChild(jsoupData);
        jsoupDoc.appendChild(jsoupRoot);

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(jsoupData, 0);
        Text w3cData = (Text) w3cDoc.getDocumentElement().getFirstChild();

        assertNotNull(w3cData);
        assertEquals("data", w3cData.getWholeText());
    }

    @Test
    public void testW3CBuilder_tailElement_success() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupRoot = new org.jsoup.nodes.Element("root");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("element");
        jsoupRoot.appendChild(jsoupElement);
        jsoupDoc.appendChild(jsoupRoot);

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(jsoupElement, 0);
        w3cBuilder.tail(jsoupElement, 0);

        assertEquals(w3cDoc, w3cDoc.getDocumentElement().getFirstChild());
    }

    @Test
    public void testAsString_validInput_success() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String xmlString = w3cDom.asString(w3cDoc);

        assertNotNull(xmlString);
        // Add additional assertions here
    }

    @Test
    public void testAsString_nullInput_throwIllegalArgumentException() {
        try {
            w3cDom.asString(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Input document cannot be null", e.getMessage());
        }
    }

}

