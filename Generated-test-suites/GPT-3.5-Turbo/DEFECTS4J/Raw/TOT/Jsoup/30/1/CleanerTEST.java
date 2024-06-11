import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {
  
  @Test
  public void testClean() {
    // Typical use case: Cleaning a document with allowed tags
    Whitelist whitelist = Whitelist.basic();
    Cleaner cleaner = new Cleaner(whitelist);
    Document dirtyDocument = Jsoup.parseBodyFragment("<p>This is a paragraph</p>");
    Document cleanDocument = cleaner.clean(dirtyDocument);
    assertEquals(cleanDocument.body().html(), "<p>This is a paragraph</p>");
    
    // Edge case: Cleaning a document with an allowed tag inside a not allowed tag
    dirtyDocument = Jsoup.parseBodyFragment("<div><p>This is a paragraph</p></div>");
    cleanDocument = cleaner.clean(dirtyDocument);
    assertEquals(cleanDocument.body().html(), "<p>This is a paragraph</p>");
    
    // Error scenario: Cleaning a null document
    dirtyDocument = null;
    cleanDocument = cleaner.clean(dirtyDocument);
    assertNull(cleanDocument);
  }
  
  @Test
  public void testIsValid() {
    // Typical use case: Validating a document with only allowed tags and attributes
    Whitelist whitelist = Whitelist.basic();
    Cleaner cleaner = new Cleaner(whitelist);
    Document dirtyDocument = Jsoup.parseBodyFragment("<p>This is a paragraph</p>");
    boolean isValid = cleaner.isValid(dirtyDocument);
    assertTrue(isValid);
    
    // Edge case: Validating a document with tags and attributes not allowed by the whitelist
    dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS attack');</script>");
    isValid = cleaner.isValid(dirtyDocument);
    assertFalse(isValid);
    
    // Error scenario: Validating a null document
    dirtyDocument = null;
    isValid = cleaner.isValid(dirtyDocument);
    assertFalse(isValid);
  }
  
  @Test
  public void testCopySafeNodes() {
    // Typical use case: Copying a document with allowed tags
    Whitelist whitelist = Whitelist.basic();
    Cleaner cleaner = new Cleaner(whitelist);
    Document dirtyDocument = Jsoup.parseBodyFragment("<p>This is a paragraph</p>");
    Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
    Element destination = cleanDocument.body();
    cleaner.copySafeNodes(dirtyDocument.body(), destination);
    assertEquals(cleanDocument.body().html(), "<p>This is a paragraph</p>");
    
    // Edge case: Copying a document with an allowed tag inside a not allowed tag
    dirtyDocument = Jsoup.parseBodyFragment("<div><p>This is a paragraph</p></div>");
    destination = cleanDocument.body();
    cleaner.copySafeNodes(dirtyDocument.body(), destination);
    assertEquals(cleanDocument.body().html(), "<p>This is a paragraph</p>");
    
    // Error scenario: Dirty document or destination is null
    dirtyDocument = null;
    destination = null;
    int numDiscarded = cleaner.copySafeNodes(dirtyDocument, destination);
    assertEquals(numDiscarded, 0);
  }
  
  @Test
  public void testCreateSafeElement() {
    // Typical use case: Creating a safe element with allowed attributes
    Whitelist whitelist = Whitelist.basic();
    Cleaner cleaner = new Cleaner(whitelist);
    Element sourceEl = new Element(Tag.valueOf("p"), "");
    sourceEl.attr("class", "paragraph");
    sourceEl.attr("id", "1");
    Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
    Element destination = meta.el;
    assertEquals(destination.tagName(), "p");
    assertEquals(destination.attr("class"), "paragraph");
    assertEquals(destination.attr("id"), "1");
    assertEquals(meta.numAttribsDiscarded, 0);
    
    // Edge case: Creating a safe element with not allowed attributes
    sourceEl = new Element(Tag.valueOf("script"), "");
    sourceEl.attr("src", "xss.js");
    sourceEl.attr("type", "text/javascript");
    meta = cleaner.createSafeElement(sourceEl);
    destination = meta.el;
    assertEquals(destination.tagName(), "script");
    assertFalse(destination.hasAttr("src"));
    assertFalse(destination.hasAttr("type"));
    assertEquals(meta.numAttribsDiscarded, 2);
    
    // Error scenario: Source element is null
    sourceEl = null;
    meta = cleaner.createSafeElement(sourceEl);
    assertNull(meta.el);
    assertEquals(meta.numAttribsDiscarded, 0);
  }
  
  @Test
  public void testCleanerConstructor() {
    // Typical use case: Creating a cleaner with a basic whitelist
    Whitelist whitelist = Whitelist.basic();
    Cleaner cleaner = new Cleaner(whitelist);
    assertNotNull(cleaner);
    
    // Edge case: Creating a cleaner with an empty whitelist
    whitelist = Whitelist.none();
    cleaner = new Cleaner(whitelist);
    assertNotNull(cleaner);
    
    // Error scenario: Creating a cleaner with a null whitelist
    whitelist = null;
    cleaner = new Cleaner(whitelist);
    assertNotNull(cleaner);
  }
  
}