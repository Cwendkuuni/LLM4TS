import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsoup_NullInput_ThrowsIllegalArgumentException() {
        w3cDom.fromJsoup(null);
    }

    @Test
    public void fromJsoup_ValidInput_ReturnsW3CDocument() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
    }

    @Test
    public void fromJsoup_ValidInput_ReturnsValidW3CDocument() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        assertEquals("", w3cDocument.getDocumentURI());
    }

    @Test(expected = IllegalStateException.class)
    public void fromJsoup_ParserConfigurationException_ThrowsIllegalStateException() throws ParserConfigurationException {
        W3CDom w3cDom = new W3CDom() {
            @Override
            public DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
                throw new ParserConfigurationException();
            }
        };
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        w3cDom.fromJsoup(jsoupDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void convert_NullInput_ThrowsIllegalArgumentException() {
        w3cDom.convert(null, null);
    }

    @Test
    public void convert_ValidInputs_ConvertsDocument() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        jsoupDocument.appendChild(jsoupElement);
        w3cDom.convert(jsoupDocument, w3cDocument);
        assertEquals(1, w3cDocument.getElementsByTagName("tag").getLength());
    }

    @Test
    public void convert_ValidInputs_ConvertsElementWithAttributes() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        jsoupElement.attr("attr1", "val1");
        jsoupElement.attr("attr2", "val2");
        jsoupDocument.appendChild(jsoupElement);
        w3cDom.convert(jsoupDocument, w3cDocument);
        Element w3cElement = (Element) w3cDocument.getElementsByTagName("tag").item(0);
        assertEquals("val1", w3cElement.getAttribute("attr1"));
        assertEquals("val2", w3cElement.getAttribute("attr2"));
    }

    @Test
    public void convert_ValidInputs_ConvertsElementWithTextNode() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("text");
        jsoupElement.appendChild(jsoupTextNode);
        jsoupDocument.appendChild(jsoupElement);
        w3cDom.convert(jsoupDocument, w3cDocument);
        Element w3cElement = (Element) w3cDocument.getElementsByTagName("tag").item(0);
        Text w3cTextNode = (Text) w3cElement.getFirstChild();
        assertEquals("text", w3cTextNode.getWholeText());
    }

    @Test
    public void convert_ValidInputs_ConvertsElementWithComment() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("comment");
        jsoupElement.appendChild(jsoupComment);
        jsoupDocument.appendChild(jsoupElement);
        w3cDom.convert(jsoupDocument, w3cDocument);
        Element w3cElement = (Element) w3cDocument.getElementsByTagName("tag").item(0);
        Comment w3cComment = (Comment) w3cElement.getFirstChild();
        assertEquals("comment", w3cComment.getData());
    }

    @Test
    public void convert_ValidInputs_ConvertsElementWithDataNode() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("data");
        jsoupElement.appendChild(jsoupDataNode);
        jsoupDocument.appendChild(jsoupElement);
        w3cDom.convert(jsoupDocument, w3cDocument);
        Element w3cElement = (Element) w3cDocument.getElementsByTagName("tag").item(0);
        Text w3cDataNode = (Text) w3cElement.getFirstChild();
        assertEquals("data", w3cDataNode.getWholeData());
    }

    @Test
    public void asString_ValidInput_ReturnsStringDocument() {
        Document doc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        String result = w3cDom.asString(doc);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void asString_TransformerException_ThrowsIllegalStateException() throws TransformerException {
        W3CDom w3cDom = new W3CDom() {
            @Override
            public Transformer getTransformer() throws TransformerException {
                throw new TransformerException("");
            }
        };
        Document doc = w3cDom.fromJsoup(new org.jsoup.nodes.Document(""));
        w3cDom.asString(doc);
    }

}