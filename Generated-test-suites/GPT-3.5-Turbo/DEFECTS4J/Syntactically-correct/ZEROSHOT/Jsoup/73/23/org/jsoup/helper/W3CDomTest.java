import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.w3c.dom.Element;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

public class W3CDomTest {
  
  @Test
  public void testFromJsoup() {
    W3CDom w3cDom = new W3CDom();
    Document jsoupDoc = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
    org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    
    assertNotNull(w3cDoc);
    assertEquals("html", w3cDoc.getDocumentElement().getTagName());
  }
  
  @Test
  public void testConvert() {

    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
    org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
    org.w3c.dom.Document w3cDoc = w3cBuilder.convert(jsoupDoc);
    
    assertNotNull(w3cDoc);
    assertEquals("html", w3cDoc.getDocumentElement().getTagName());
  }
  
  @Test
  public void testCopyAttributes() {
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
    org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
    jsoupElement.attr("class", "container");
    jsoupElement.attr("id", "main");

    org.w3c.dom.Document w3cDoc = null;
    org.w3c.dom.Element w3cElement = w3cDoc.createElement("div");
    
    w3cBuilder.copyAttributes(jsoupElement, w3cElement);
    
    assertEquals("container", w3cElement.getAttribute("class"));
    assertEquals("main", w3cElement.getAttribute("id"));
  }
  
  @Test
  public void testUpdateNamespaces() {
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
    org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
    jsoupElement.attr("xmlns:custom", "http://www.example.com");

    String namespace = w3cBuilder.updateNamespaces(jsoupElement);
    
    assertEquals("custom", namespace);
  }
  
  @Test
  public void testAsString() {
    W3CDom w3cDom = new W3CDom();
    org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
    org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    
    String w3cDocAsString = w3cDom.asString(w3cDoc);
    
    assertNotNull(w3cDocAsString);
  }
  
}