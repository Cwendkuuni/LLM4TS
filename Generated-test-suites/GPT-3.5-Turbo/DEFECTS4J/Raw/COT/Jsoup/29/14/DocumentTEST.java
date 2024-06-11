
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DocumentTest {
  
  @Test
  public void testCreateShell() {
    Document doc = Document.createShell("https://www.example.com");
    
    // Check if the document has the html tag
    Element htmlElement = doc.getElementsByTag("html").first();
    Assert.assertNotNull("HTML tag is missing", htmlElement);
    
    // Check if the document has the head tag
    Element headElement = doc.head();
    Assert.assertNotNull("Head tag is missing", headElement);
    
    // Check if the document has the body tag
    Element bodyElement = doc.body();
    Assert.assertNotNull("Body tag is missing", bodyElement);
  }
  
  @Test
  public void testHead() {
    Document doc = Jsoup.parse("<html><head></head><body></body></html>");
    
    // Check if the head method returns the correct element
    Element headElement = doc.head();
    Assert.assertNotNull("Head element is null", headElement);
    
    // Check if the element has the correct tag name
    Assert.assertEquals("Head element has incorrect tag name", "head", headElement.tagName());
  }
  
  @Test
  public void testBody() {
    Document doc = Jsoup.parse("<html><head></head><body></body></html>");
    
    // Check if the body method returns the correct element
    Element bodyElement = doc.body();
    Assert.assertNotNull("Body element is null", bodyElement);
    
    // Check if the element has the correct tag name
    Assert.assertEquals("Body element has incorrect tag name", "body", bodyElement.tagName());
  }
  
  @Test
  public void testTitle() {
    Document doc = Jsoup.parse("<html><head><title>Example Title</title></head><body></body></html>");
    
    // Check if the title method returns the correct title
    String title = doc.title();
    Assert.assertNotNull("Title is null", title);
    
    // Check if the title is trimmed correctly
    Assert.assertEquals("Title is not trimmed correctly", "Example Title", title);
  }
  
  @Test
  public void testSetTitle() {
    Document doc = Jsoup.parse("<html><head><title></title></head><body></body></html>");
    
    // Set a title using the title method
    doc.title("New Title");
    
    // Check if the new title is set correctly
    String newTitle = doc.title();
    Assert.assertEquals("Title is not set correctly", "New Title", newTitle);
    
    // Check if the title element is added to the head if not present
    Element titleElement = doc.getElementsByTag("title").first();
    Assert.assertNotNull("Title element is null", titleElement);
    
    // Check if the title element has the correct text value
    Assert.assertEquals("Title element has incorrect text value", "New Title", titleElement.text());
  }
  
  @Test
  public void testNormalise() {
    Document doc = Jsoup.parse("<html><head></head><body></body></html>");
    
    // Add some text nodes outside the body element
    doc.prepend("<span>Text1</span>");
    doc.append("<span>Text2</span>");
    
    // Normalize the document
    doc.normalise();
    
    // Check if the text nodes are moved inside the body element
    Element bodyElement = doc.body();
    Assert.assertEquals("Body element should only have 2 children", 2, bodyElement.children().size());
    
    // Check if the order of the text nodes is maintained
    Assert.assertEquals("First child of body element has incorrect tag name", "span", bodyElement.child(0).tagName());
    Assert.assertEquals("First child of body element has incorrect text", "Text1", bodyElement.child(0).text());
    Assert.assertEquals("Second child of body element has incorrect tag name", "span", bodyElement.child(1).tagName());
    Assert.assertEquals("Second child of body element has incorrect text", "Text2", bodyElement.child(1).text());
  }
  
  @Test
  public void testClone() {
    Document doc = Jsoup.parse("<html><head><title>Example Title</title></head><body></body></html>");
    
    // Clone the document
    Document clone = doc.clone();
    
    // Check if the clone is a new instance
    Assert.assertNotSame("Clone is the same instance as the original document", doc, clone);
    
    // Check if the clone has the same html as the original
    Assert.assertEquals("Clone does not have the same HTML as the original document", doc.html(), clone.html());
    
    // Check if the clone's output settings are the same as the original's
    Assert.assertEquals("Clone does not have the same output settings as the original document", doc.outputSettings(), clone.outputSettings());
  }
  
}
