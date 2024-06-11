import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

import static org.junit.Assert.*;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void convert_shouldCreateW3CDocumentFromJsoupDocument() {
        // Arrange
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document jsoupDocument = Parser.parseFragment(html, "http://example.com/");
        DocumentBuilder documentBuilder;

        // Act
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document expectedDocument = documentBuilder.newDocument();
            
            // Create the expected W3C document manually
            Element htmlElement = expectedDocument.createElement("html");
            Element bodyElement = expectedDocument.createElement("body");
            Element h1Element = expectedDocument.createElement("h1");
            Element pElement = expectedDocument.createElement("p");

            Text h1Text = expectedDocument.createTextNode("Title");
            Text pText = expectedDocument.createTextNode("Paragraph");

            expectedDocument.appendChild(htmlElement);
            htmlElement.appendChild(bodyElement);
            bodyElement.appendChild(h1Element);
            bodyElement.appendChild(pElement);
            h1Element.appendChild(h1Text);
            pElement.appendChild(pText);

            // Assert
            assertEqualDocuments(expectedDocument, w3cDocument);
        } catch (Exception e) {
            fail("An exception occurred while creating the expected W3C document.");
        }
    }

    @Test
    public void asString_shouldReturnStringRepresentationOfW3CDocument() {
        // Arrange
        DocumentBuilder documentBuilder;
        Document w3cDocument;

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            w3cDocument = documentBuilder.newDocument();

            Element rootElement = w3cDocument.createElement("root");
            w3cDocument.appendChild(rootElement);

            Element childElement = w3cDocument.createElement("child");
            rootElement.appendChild(childElement);

            Text textNode = w3cDocument.createTextNode("Text Node");
            childElement.appendChild(textNode);

            // Act
            String w3cDocumentString = w3cDom.asString(w3cDocument);

            // Assert
            String expectedString =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                            "<root>\n" +
                            "    <child>Text Node</child>\n" +
                            "</root>\n";

            assertEquals(expectedString, w3cDocumentString);
        } catch (Exception e) {
            fail("An exception occurred while creating the W3C document.");
        }
    }

    /**
     * Compare two W3C Documents for equality.
     * Note that this method compares only the contents of the documents,
     * not the order of the elements or the formatting of the XML.
     *
     * @param expectedDoc The expected W3C Document.
     * @param actualDoc   The actual W3C Document.
     */
    private void assertEqualDocuments(Document expectedDoc, Document actualDoc) {
        Element expectedRootElement = expectedDoc.getDocumentElement();
        Element actualRootElement = actualDoc.getDocumentElement();

        assertEqualElements(expectedRootElement, actualRootElement);
    }

    /**
     * Recursive method to compare two W3C Elements for equality.
     *
     * @param expectedElement The expected W3C Element.
     * @param actualElement   The actual W3C Element.
     */
    private void assertEqualElements(Element expectedElement, Element actualElement) {
        assertEquals(expectedElement.getTagName(), actualElement.getTagName());

        NamedNodeMap expectedAttributes = expectedElement.getAttributes();
        NamedNodeMap actualAttributes = actualElement.getAttributes();

        assertEquals(expectedAttributes.getLength(), actualAttributes.getLength());

        for (int i = 0; i < expectedAttributes.getLength(); i++) {
            Attr expectedAttr = (Attr) expectedAttributes.item(i);
            Attr actualAttr = (Attr) actualAttributes.item(i);

            assertEquals(expectedAttr.getName(), actualAttr.getName());
            assertEquals(expectedAttr.getValue(), actualAttr.getValue());
        }

        NodeList expectedChildNodes = expectedElement.getChildNodes();
        NodeList actualChildNodes = actualElement.getChildNodes();

        assertEquals(expectedChildNodes.getLength(), actualChildNodes.getLength());

        for (int i = 0; i < expectedChildNodes.getLength(); i++) {
            Node expectedChildNode = expectedChildNodes.item(i);
            Node actualChildNode = actualChildNodes.item(i);

            if (expectedChildNode.getNodeType() == Node.ELEMENT_NODE &&
                    actualChildNode.getNodeType() == Node.ELEMENT_NODE) {
                assertEqualElements((Element) expectedChildNode, (Element) actualChildNode);
            } else if (expectedChildNode.getNodeType() == Node.TEXT_NODE &&
                    actualChildNode.getNodeType() == Node.TEXT_NODE) {
                assertEquals(expectedChildNode.getNodeValue(), actualChildNode.getNodeValue());
            }
        }
    }
}