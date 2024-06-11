import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
  
  private Cleaner cleaner;
  
  @Before
  public void setUp() {
    cleaner = new Cleaner(new Whitelist());
  }
  
  @Test
  public void testClean() {
    Document dirtyDocument = new Document("<html><head></head><body><div><span>Hello World!</span></div></body></html>");
    Document cleanDocument = cleaner.clean(dirtyDocument);
    
    Assert.assertNotNull(cleanDocument);
    Assert.assertEquals("<html><head></head><body><div><span>Hello World!</span></div></body></html>", cleanDocument.html());
  }
  
  @Test
  public void testIsValid() {
    Document dirtyDocument = new Document("<html><head></head><body><div><span>Hello World!</span></div></body></html>");
    boolean isValid = cleaner.isValid(dirtyDocument);
    
    Assert.assertTrue(isValid);
  }
  
  @Test
  public void testCopySafeNodes() {
    Element source = new Element("div");
    Element dest = new Element("div");
    
    int numDiscarded = cleaner.copySafeNodes(source, dest);
    
    Assert.assertEquals(0, numDiscarded);
    Assert.assertEquals("<div></div>", dest.html());
  }
  
  @Test
  public void testCreateSafeElement() {
    Element sourceEl = new Element("div");
    sourceEl.attr("id", "test");
    sourceEl.attr("class", "test");
    
    Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
    
    Assert.assertNotNull(meta.el);
    Assert.assertEquals("<div id=\"test\" class=\"test\"></div>", meta.el.outerHtml());
    Assert.assertEquals(0, meta.numAttribsDiscarded);
  }
  
}