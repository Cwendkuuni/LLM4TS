import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {
  
  @Test
  public void testDocumentConstructor() {
    Document doc = new Document("https://www.example.com");
    assertNotNull(doc);
    assertEquals("#root", doc.nodeName());
    assertEquals("https://www.example.com", doc.baseUri());
    assertNotNull(doc.head());
    assertNotNull(doc.body());
    assertEquals("html", doc.getElementsByTag("html").first().nodeName());
    assertEquals("head", doc.getElementsByTag("head").first().nodeName());
    assertEquals("body", doc.getElementsByTag("body").first().nodeName());
  }
  
  @Test
  public void testCreateShell() {
    Document doc = Document.createShell("https://www.example.com");
    assertNotNull(doc);
    assertEquals("#root", doc.nodeName());
    assertEquals("https://www.example.com", doc.baseUri());
    assertNotNull(doc.head());
    assertNotNull(doc.body());
    assertEquals("html", doc.getElementsByTag("html").first().nodeName());
    assertEquals("head", doc.getElementsByTag("head").first().nodeName());
    assertEquals("body", doc.getElementsByTag("body").first().nodeName());
  }
  
  @Test
  public void testHead() {
    Document doc = new Document("https://www.example.com");
    assertNull(doc.head());
    
    doc.appendElement("head");
    assertNotNull(doc.head());
    assertEquals("head", doc.head().nodeName());
  }
  
  @Test
  public void testBody() {
    Document doc = new Document("https://www.example.com");
    assertNull(doc.body());
    
    doc.appendElement("body");
    assertNotNull(doc.body());
    assertEquals("body", doc.body().nodeName());
  }
  
  @Test
  public void testTitle() {
    Document doc = new Document("https://www.example.com");
    assertEquals("", doc.title());
    
    Element head = doc.appendElement("head");
    head.appendElement("title").text("Example Title");
    assertEquals("Example Title", doc.title());
    
    doc.title("New Title");
    assertEquals("New Title", doc.title());
  }
  
  @Test
  public void testTitleEmpty() {
    Document doc = new Document("https://www.example.com");
    assertEquals("", doc.title());
    
    Element head = doc.appendElement("head");
    assertEquals("", doc.title());
    
    head.appendElement("title");
    assertEquals("", doc.title());
  }
  
  @Test
  public void testTitleNull() {
    Document doc = new Document("https://www.example.com");
    assertEquals("", doc.title());
    
    Element head = doc.appendElement("head");
    head.appendElement("title").text("Example Title");
    assertEquals("Example Title", doc.title());
    
    doc.title(null);
    assertEquals("", doc.title());
  }
  
  @Test
  public void testTitleElementNotPresent() {
    Document doc = new Document("https://www.example.com");
    assertEquals("", doc.title());
    
    doc.title("New Title");
    assertEquals("New Title", doc.title());
  }
  
  @Test
  public void testCreateElement() {
    Document doc = new Document("https://www.example.com");
    
    Element element = doc.createElement("div");
    assertNotNull(element);
    assertEquals("div", element.nodeName());
    assertEquals("https://www.example.com", element.baseUri());
  }
  
  @Test
  public void testNormalise() {
    Document doc = new Document("https://www.example.com");
    doc.normalise();
    
    Element html = doc.getElementsByTag("html").first();
    assertNotNull(html);
    
    Element head = doc.getElementsByTag("head").first();
    assertNotNull(head);
    
    Element body = doc.getElementsByTag("body").first();
    assertNotNull(body);
    
    assertTrue(doc.select("html").isEmpty());
    assertNotNull(doc.head());
    assertNotNull(doc.body());
  }
  
  @Test
  public void testNormaliseWithExistingHtmlHeadBody() {
    Document doc = new Document("https://www.example.com");
    doc.appendElement("html");
    doc.appendElement("head");
    doc.appendElement("body");
    
    doc.normalise();
    
    assertEquals(1, doc.select("html").size());
    assertEquals(1, doc.select("head").size());
    assertEquals(1, doc.select("body").size());
  }
  
  @Test
  public void testOuterHtml() {
    Document doc = new Document("https://www.example.com");
    
    Element div = doc.appendElement("div");
    div.text("Test Element");
    
    assertEquals("<div>Test Element</div>", doc.outerHtml());
  }
  
  @Test
  public void testText() {
    Document doc = new Document("https://www.example.com");
    doc.appendElement("body").text("Test Text");
    
    assertEquals("Test Text", doc.text());
  }
  
  @Test
  public void testNodeName() {
    Document doc = new Document("https://www.example.com");
    assertEquals("#document", doc.nodeName());
  }
  
}