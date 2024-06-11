import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.IOException;
import java.util.HashMap;

public class W3CDomTest {
    
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document w3cDoc;
    private Element rootElement;
    private Element element;
    private Text textNode;
    private Comment comment;

    @Before
    public void setup() throws ParserConfigurationException {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        w3cDoc = builder.newDocument();
        rootElement = w3cDoc.createElement("root");
        element = w3cDoc.createElement("element");
        textNode = w3cDoc.createTextNode("Text Node");
        comment = w3cDoc.createComment("Comment");
    }

    @Test
    public void testFromJsoup_ValidInput_ReturnsW3CDocument() {
        // Setup
        jsoupDoc.appendChild(rootElement);

        // Exercise
        Document result = w3cDom.fromJsoup(jsoupDoc);

        // Verify
        Assert.assertNotNull(result);
        Assert.assertEquals(w3cDoc, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_NullInput_ThrowsIllegalArgumentException() {
        // Exercise
        w3cDom.fromJsoup(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoup_ParserConfigurationException_ThrowsIllegalStateException() {
        // Setup
        factory.setNamespaceAware(false);

        // Exercise
        w3cDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testConvert_ValidInput_ConvertsJsoupDocumentToW3CDocument() {
        // Setup
        jsoupDoc.appendChild(rootElement);
        rootElement.appendChild(element);
        element.appendChild(textNode);
        element.appendChild(comment);

        // Exercise
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Verify
        Assert.assertEquals(1, w3cDoc.getChildNodes().getLength());
        Assert.assertEquals("root", w3cDoc.getChildNodes().item(0).getNodeName());
        Assert.assertEquals(1, w3cDoc.getChildNodes().item(0).getChildNodes().getLength());
        Assert.assertEquals("element", w3cDoc.getChildNodes().item(0).getChildNodes().item(0).getNodeName());
        Assert.assertEquals("Text Node", w3cDoc.getChildNodes().item(0).getChildNodes().item(0).getTextContent());
        Assert.assertEquals("#comment", w3cDoc.getChildNodes().item(0).getChildNodes().item(1).getNodeName());
        Assert.assertEquals("Comment", w3cDoc.getChildNodes().item(0).getChildNodes().item(1).getTextContent());
    }

    @Test
    public void testConvert_BlankLocation_SetsEmptyDocumentURI() {
        // Setup
        jsoupDoc.setBaseUri("");

        // Exercise
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Verify
        Assert.assertEquals("", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_ValidLocation_SetsDocumentURI() {
        // Setup
        jsoupDoc.setBaseUri("https://www.example.com");

        // Exercise
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Verify
        Assert.assertEquals("https://www.example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_NullInput_NoError() {
        // Exercise
        w3cDom.convert(null, w3cDoc);
    }

    @Test
    public void testConvert_EmptyJsoupDocument_NoError() {
        // Exercise
        w3cDom.convert(jsoupDoc, w3cDoc);
    }

    @Test
    public void testAsString_ValidDocument_ReturnsString() {
        // Setup
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        try {
            // Exercise
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(w3cDoc), result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verify
        String expected = writer.toString();
        String actual = w3cDom.asString(w3cDoc);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsString_TransformerException_ThrowsIllegalStateException() throws Exception {
        // Setup
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty("bad_property", "bad_value");

        // Exercise
        w3cDom.asString(w3cDoc);
    }

}