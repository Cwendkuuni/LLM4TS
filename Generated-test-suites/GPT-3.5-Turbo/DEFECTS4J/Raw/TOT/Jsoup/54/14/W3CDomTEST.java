import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {
    
    private static W3CDom w3cDom;
    
    @BeforeClass
    public static void setUp() {
        w3cDom = new W3CDom();
    }
    
    @Test
    public void testFromJsoup_validDocument_returnsW3CDocument() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Ensure that the conversion is successful
        Assert.assertNotNull(w3cDoc);
    }
    
    @Test
    public void testFromJsoup_nullInput_throwsIllegalArgumentException() {
        try {
            w3cDom.fromJsoup(null);
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testConvert_validInput_setsDocumentURI() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document expectedDoc = factory.newDocumentBuilder().newDocument();
        expectedDoc.setDocumentURI(jsoupDoc.location());
        
        w3cDom.convert(jsoupDoc, expectedDoc);
        
        Assert.assertEquals(expectedDoc.getDocumentURI(), w3cDoc.getDocumentURI());
    }
    
    @Test
    public void testConvert_validInput_createsCorrectW3CDocumentStructure() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document expectedDoc = factory.newDocumentBuilder().newDocument();
        Element rootElement = expectedDoc.createElement("html");
        Element bodyElement = expectedDoc.createElement("body");
        Element h1Element = expectedDoc.createElement("h1");
        Text textNode = expectedDoc.createTextNode("Hello World");
        h1Element.appendChild(textNode);
        bodyElement.appendChild(h1Element);
        rootElement.appendChild(bodyElement);
        expectedDoc.appendChild(rootElement);
        
        w3cDom.convert(jsoupDoc, expectedDoc);
        
        // Ensure that the converted W3C document matches the expected structure
        Assert.assertTrue(compareDocuments(expectedDoc, w3cDoc));
    }
    
    @Test
    public void testConvert_nullInput_throwsIllegalArgumentException() {
        try {
            w3cDom.convert(null, null);
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testAsString_validDocument_returnsStringRepresentation() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        String expectedString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><body><h1>Hello World</h1></body></html>";
        
        String actualString = w3cDom.asString(w3cDoc);
        
        Assert.assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testAsString_nullDocument_throwsIllegalArgumentException() {
        try {
            w3cDom.asString(null);
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    private boolean compareDocuments(org.w3c.dom.Document doc1, org.w3c.dom.Document doc2) {
        // Helper method to compare two W3C documents
        
        // Compare document URIs
        if (!doc1.getDocumentURI().equals(doc2.getDocumentURI())) {
            return false;
        }
        
        // Compare document elements
        if (!compareElements(doc1.getDocumentElement(), doc2.getDocumentElement())) {
            return false;
        }
        
        return true;
    }
    
    private boolean compareElements(Element el1, Element el2) {
        // Helper method to compare two W3C elements
        
        // Compare element names
        if (!el1.getTagName().equals(el2.getTagName())) {
            return false;
        }
        
        // Compare attributes
        if (!el1.getAttributes().equals(el2.getAttributes())) {
            return false;
        }
        
        // Compare child nodes
        NodeList childNodes1 = el1.getChildNodes();
        NodeList childNodes2 = el2.getChildNodes();
        
        if (childNodes1.getLength() != childNodes2.getLength()) {
            return false;
        }
        
        for (int i = 0; i < childNodes1.getLength(); i++) {
            Node childNode1 = childNodes1.item(i);
            Node childNode2 = childNodes2.item(i);
            
            if (childNode1 instanceof Element && childNode2 instanceof Element) {
                if (!compareElements((Element) childNode1, (Element) childNode2)) {
                    return false;
                }
            } else {
                if (!childNode1.isEqualNode(childNode2)) {
                    return false;
                }
            }
        }
        
        return true;
    }
}