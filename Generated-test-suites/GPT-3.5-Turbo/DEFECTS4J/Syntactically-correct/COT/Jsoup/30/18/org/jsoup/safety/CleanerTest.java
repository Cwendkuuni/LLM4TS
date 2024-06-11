
import static org.junit.Assert.*;
import org.jsoup.safety.*;
import org.junit.Test;

public class CleanerTest {
  
  @Test
  public void testClean() {
    Whitelist whitelist = Whitelist.relaxed();
    Cleaner cleaner = new Cleaner(whitelist);
    
    Document document = new Document("<html><body><p>Hello World!</p></body></html>");
    Document cleanDocument = cleaner.clean(document);
    
    assertEquals("<html><head></head><body><p>Hello World!</p></body></html>", cleanDocument.html());
  }
  
  @Test
  public void testIsValid() {
    Whitelist whitelist = Whitelist.relaxed();
    Cleaner cleaner = new Cleaner(whitelist);
    
    Document validDocument = new Document("<html><body><p>Hello World!</p></body></html>");
    boolean isValid = cleaner.isValid(validDocument);
    
    assertTrue(isValid);
  }
  
  @Test
  public void testCopySafeNodes() {
    Whitelist whitelist = Whitelist.relaxed();
    Cleaner cleaner = new Cleaner(whitelist);
    
    Element root = new Element("root");
    Element destination = new Element("destination");
    
    int numDiscarded = cleaner.copySafeNodes(root, destination);
    
    assertEquals(0, numDiscarded);
  }
  
  @Test
  public void testCreateSafeElement() {
    Whitelist whitelist = Whitelist.relaxed();
    Cleaner cleaner = new Cleaner(whitelist);
    
    Element sourceEl = new Element("source");
    ElementMeta meta = cleaner.createSafeElement(sourceEl);
    
    assertNotNull(meta.el);
    assertEquals(0, meta.numAttribsDiscarded);
  }
  
}
