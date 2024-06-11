
import org.jsoup.helper.W3CDom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {
  
  private W3CDom w3CDom;
  
  @Before
  public void setup() {
    w3CDom = new W3CDom();
  }
  
  @Test
  public void testFromJsoup() {
    Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Content</p></body></html>");
    org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
    
    assertEquals("HTML", w3cDoc.getDocumentElement().getNodeName());
    assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
    assertEquals("Content", w3cDoc.getElementsByTagName("p").item(0).getTextContent());
  }
  
  @Test
  public void testConvert() {
    Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Content</p></body></html>");
    org.w3c.dom.Document w3cDoc = null; // initialize w3cDoc
    w3CDom.convert(jsoupDoc, w3cDoc);
    
    assertEquals("HTML", w3cDoc.getDocumentElement().getNodeName());
    assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
    assertEquals("Content", w3cDoc.getElementsByTagName("p").item(0).getTextContent());
  }
  
  @Test
  public void testAsString() {
    Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Content</p></body></html>");
    org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
    String xmlString = w3CDom.asString(w3cDoc);
    
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><HTML><HEAD><TITLE>Test</TITLE></HEAD><BODY><P>Content</P></BODY></HTML>", xmlString);
  }
  
}
