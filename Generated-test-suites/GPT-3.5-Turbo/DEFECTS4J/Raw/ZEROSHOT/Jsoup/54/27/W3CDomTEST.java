import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private org.w3c.dom.Document w3cDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testFromJsoupNotNull() {
        assertNotNull(w3cDoc);
    }

    @Test
    public void testFromJsoupSameDocumentType() {
        assertEquals(w3cDoc.getClass(), org.w3c.dom.Document.class);
    }

    @Test
    public void testConvert() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document expectedW3CDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(expectedW3CDoc);
            builder.head(new org.jsoup.nodes.Element("html"), 0);
            builder.head(new org.jsoup.nodes.Element("body"), 1);
            Element element1 = expectedW3CDoc.createElementNS("", "html");
            Element element2 = expectedW3CDoc.createElementNS("", "body");
            element1.appendChild(element2);
            expectedW3CDoc.appendChild(element1);
            W3CDom.W3CBuilder actualBuilder = new W3CDom.W3CBuilder(w3cDoc);
            actualBuilder.head(new org.jsoup.nodes.Element("html"), 0);
            actualBuilder.head(new org.jsoup.nodes.Element("body"), 1);
            Element actualElement1 = w3cDoc.createElementNS("", "html");
            Element actualElement2 = w3cDoc.createElementNS("", "body");
            actualElement1.appendChild(actualElement2);
            w3cDoc.appendChild(actualElement1);
            assertEquals(expectedW3CDoc, w3cDoc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertDocumentUri() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("https://www.example.com");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document expectedW3CDoc = builder.newDocument();
            expectedW3CDoc.setDocumentURI("https://www.example.com");
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(expectedW3CDoc);
            builder.head(new org.jsoup.nodes.Element("html"), 0);
            Element element1 = expectedW3CDoc.createElementNS("", "html");
            expectedW3CDoc.appendChild(element1);
            W3CDom.W3CBuilder actualBuilder = new W3CDom.W3CBuilder(w3cDoc);
            actualBuilder.head(new org.jsoup.nodes.Element("html"), 0);
            Element actualElement1 = w3cDoc.createElementNS("", "html");
            w3cDoc.appendChild(actualElement1);
            assertEquals(expectedW3CDoc.getDocumentURI(), w3cDoc.getDocumentURI());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertElement() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        jsoupElement.attr("class", "test-class");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document expectedW3CDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(expectedW3CDoc);
            builder.head(jsoupElement, 0);
            Element element = expectedW3CDoc.createElementNS("", "div");
            element.setAttribute("class", "test-class");
            expectedW3CDoc.appendChild(element);
            W3CDom.W3CBuilder actualBuilder = new W3CDom.W3CBuilder(w3cDoc);
            actualBuilder.head(jsoupElement, 0);
            Element actualElement = w3cDoc.createElementNS("", "div");
            actualElement.setAttribute("class", "test-class");
            w3cDoc.appendChild(actualElement);
            assertEquals(expectedW3CDoc.getFirstChild(), w3cDoc.getFirstChild());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertTextNode() {
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("test text", "");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document expectedW3CDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(expectedW3CDoc);
            builder.head(jsoupTextNode, 0);
            Text textNode = expectedW3CDoc.createTextNode("test text");
            expectedW3CDoc.appendChild(textNode);
            W3CDom.W3CBuilder actualBuilder = new W3CDom.W3CBuilder(w3cDoc);
            actualBuilder.head(jsoupTextNode, 0);
            Text actualTextNode = w3cDoc.createTextNode("test text");
            w3cDoc.appendChild(actualTextNode);
            assertEquals(expectedW3CDoc.getFirstChild(), w3cDoc.getFirstChild());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertComment() {
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("test comment", "");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document expectedW3CDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(expectedW3CDoc);
            builder.head(jsoupComment, 0);
            Comment comment = expectedW3CDoc.createComment("test comment");
            expectedW3CDoc.appendChild(comment);
            W3CDom.W3CBuilder actualBuilder = new W3CDom.W3CBuilder(w3cDoc);
            actualBuilder.head(jsoupComment, 0);
            Comment actualComment = w3cDoc.createComment("test comment");
            w3cDoc.appendChild(actualComment);
            assertEquals(expectedW3CDoc.getFirstChild(), w3cDoc.getFirstChild());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertDataNode() {
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("test data", "");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document expectedW3CDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(expectedW3CDoc);
            builder.head(jsoupDataNode, 0);
            Text dataNode = expectedW3CDoc.createTextNode("test data");
            expectedW3CDoc.appendChild(dataNode);
            W3CDom.W3CBuilder actualBuilder = new W3CDom.W3CBuilder(w3cDoc);
            actualBuilder.head(jsoupDataNode, 0);
            Text actualDataNode = w3cDoc.createTextNode("test data");
            w3cDoc.appendChild(actualDataNode);
            assertEquals(expectedW3CDoc.getFirstChild(), w3cDoc.getFirstChild());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsString() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDoc = builder.newDocument();
            W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
            builder.head(new org.jsoup.nodes.Element("html"), 0);
            Element element = w3cDoc.createElementNS("", "html");
            w3cDoc.appendChild(element);
            String expectedString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html></html>";
            assertEquals(expectedString, w3cDom.asString(w3cDoc));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

}