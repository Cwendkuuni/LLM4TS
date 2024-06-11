
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
  
  @Test
  public void testDocument() {
    Document doc = new Document("http://www.example.com");
    assertNotNull(doc);
  }
  
  @Test
  public void testCreateShell() {
    Document doc = Document.createShell("http://www.example.com");
    assertNotNull(doc);
    assertEquals("html", doc.tagName());
    assertNotNull(doc.head());
    assertNotNull(doc.body());
  }
  
  @Test
  public void testHead() {
    Document doc = Document.createShell("http://www.example.com");
    Element head = doc.head();
    assertNotNull(head);
    assertEquals("head", head.tagName());
  }
  
  @Test
  public void testBody() {
    Document doc = Document.createShell("http://www.example.com");
    Element body = doc.body();
    assertNotNull(body);
    assertEquals("body", body.tagName());
  }
  
  @Test
  public void testTitle() {
    Document doc = Document.createShell("http://www.example.com");
    String title = doc.title();
    assertEquals("", title);
    
    doc.title("Example Title");
    title = doc.title();
    assertEquals("Example Title", title);
  }
  
  @Test
  public void testCreateElement() {
    Document doc = Document.createShell("http://www.example.com");
    Element element = doc.createElement("a");
    assertNotNull(element);
    assertEquals("a", element.tagName());
    assertEquals("http://www.example.com", element.baseUri());
  }
  
  @Test
  public void testNormalise() {
    Document doc = Document.createShell("http://www.example.com");
    Element div = doc.createElement("div");
    div.appendText("Hello World");
    doc.body().appendChild(div);
    
    doc.normalise();
    
    assertEquals(1, doc.body().children().size());
    assertEquals(1, doc.select("html").size());
    assertEquals(1, doc.select("head").size());
    assertEquals(0, doc.select("div").size());
    assertEquals(1, doc.select("body").size());
    assertEquals("Hello World", doc.body().text());
  }
  
  @Test
  public void testOuterHtml() {
    Document doc = Document.createShell("http://www.example.com");
    String html = doc.outerHtml();
    assertEquals("", html);
  }
  
  @Test
  public void testText() {
    Document doc = Document.createShell("http://www.example.com");
    doc.text("Example Text");
    assertEquals("Example Text", doc.body().text());
  }
  
  @Test
  public void testNodeName() {
    Document doc = Document.createShell("http://www.example.com");
    String nodeName = doc.nodeName();
    assertEquals("#document", nodeName);
  }
}
