
import static org.junit.Assert.*;
import org.junit.Test;

public class W3CDomTest {

  @Test
  public void testFromJsoup() {
    // Create a test jsoup document
    org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
    jsoupDoc.location("http://example.com");
    org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element("html");
    jsoupDoc.appendChild(rootElement);

    // Convert the jsoup document to a w3c document
    W3CDom w3cDom = new W3CDom();
    org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    
    // Check if the document URI is set correctly
    assertEquals("http://example.com", w3cDoc.getDocumentURI());
    
    // Check if the root element is converted correctly
    assertEquals("html", w3cDoc.getDocumentElement().getTagName());
  }

  @Test
  public void testConvert() {
    // Create a test jsoup document
    org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
    org.jsoup.nodes.Element rootElement = new org.jsoup.nodes.Element("html");
    jsoupDoc.appendChild(rootElement);

    // Create a test w3c document
    org.w3c.dom.Document w3cDoc = null;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      w3cDoc = builder.newDocument();
    } catch (ParserConfigurationException e) {
      fail("Failed to create w3c document");
    }

    // Convert the jsoup document to the w3c document
    W3CDom w3cDom = new W3CDom();
    w3cDom.convert(jsoupDoc, w3cDoc);
    
    // Check if the root element is converted correctly
    assertEquals("html", w3cDoc.getDocumentElement().getTagName());
  }

  @Test
  public void testAsString() {
    // Create a test w3c document
    org.w3c.dom.Document w3cDoc = null;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      w3cDoc = builder.newDocument();
    } catch (ParserConfigurationException e) {
      fail("Failed to create w3c document");
    }

    // Create a test element in the w3c document
    org.w3c.dom.Element rootElement = w3cDoc.createElement("html");
    w3cDoc.appendChild(rootElement);

    // Convert the w3c document to a string
    W3CDom w3cDom = new W3CDom();
    String xmlString = w3cDom.asString(w3cDoc);

    // Check if the xml string matches the expected format
    String expectedXmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html/>";
    assertEquals(expectedXmlString, xmlString);
  }
  
}
