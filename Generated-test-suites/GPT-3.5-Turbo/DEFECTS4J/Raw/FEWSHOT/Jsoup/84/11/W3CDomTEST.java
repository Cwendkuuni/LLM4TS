import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;

public class W3CDomTest {

  @Test
  public void testFromJsoup() {
    org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
    W3CDom w3cDom = new W3CDom();
    Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    assertNotNull(w3cDoc);
    Element rootElement = w3cDoc.getDocumentElement();
    assertNotNull(rootElement);
    assertEquals("html", rootElement.getTagName());
    Element headElement = (Element) rootElement.getElementsByTagName("head").item(0);
    Element titleElement = (Element) headElement.getElementsByTagName("title").item(0);
    assertEquals("Test", titleElement.getTextContent());
    Element bodyElement = (Element) rootElement.getElementsByTagName("body").item(0);
    Element h1Element = (Element) bodyElement.getElementsByTagName("h1").item(0);
    assertEquals("Hello World", h1Element.getTextContent());
  }

  @Test
  public void testConvert() {
    org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
    Document w3cDoc = getW3CDocument(jsoupDoc);
    assertNotNull(w3cDoc);
    Element rootElement = w3cDoc.getDocumentElement();
    assertNotNull(rootElement);
    assertEquals("html", rootElement.getTagName());
    Element headElement = (Element) rootElement.getElementsByTagName("head").item(0);
    Element titleElement = (Element) headElement.getElementsByTagName("title").item(0);
    assertEquals("Test", titleElement.getTextContent());
    Element bodyElement = (Element) rootElement.getElementsByTagName("body").item(0);
    Element h1Element = (Element) bodyElement.getElementsByTagName("h1").item(0);
    assertEquals("Hello World", h1Element.getTextContent());
  }

  @Test
  public void testAsString() {
    org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
    Document w3cDoc = getW3CDocument(jsoupDoc);
    W3CDom w3cDom = new W3CDom();
    String xmlString = w3cDom.asString(w3cDoc);
    assertNotNull(xmlString);
    assertTrue(xmlString.contains("<title>Test</title>"));
    assertTrue(xmlString.contains("<h1>Hello World</h1>"));
  }

  private Document getW3CDocument(org.jsoup.nodes.Document jsoupDoc) {
    W3CDom w3cDom = new W3CDom();
    Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    return w3cDoc;
  }
}