import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class CleanerTest {
  
  @Test
  public void testClean() {
    // Test case 1: Empty dirty document
    Document dirty = Jsoup.parse("");
    Cleaner cleaner = new Cleaner(Whitelist.basic());
    Document clean = cleaner.clean(dirty);
    assert clean.body().children().size() == 0;

    // Test case 2: Dirty document with safe elements
    dirty = Jsoup.parse("<p>This is a paragraph</p><strong>This is bold text</strong>");
    clean = cleaner.clean(dirty);
    assert clean.body().children().size() == 2;

    // Test case 3: Dirty document with unsafe elements
    dirty = Jsoup.parse("<script>alert('Hello');</script>");
    clean = cleaner.clean(dirty);
    assert clean.body().children().size() == 0;
  }

  @Test
  public void testIsValid() {
    // Test case 1: Valid document
    Document valid = Jsoup.parse("<p>This is a valid document</p>");
    Cleaner cleaner = new Cleaner(Whitelist.basic());
    assert cleaner.isValid(valid);

    // Test case 2: Invalid document
    Document invalid = Jsoup.parse("<iframe></iframe>");
    assert !cleaner.isValid(invalid);
  }

  @Test
  public void testCopySafeNodes() {
    // Test case 1: Only safe nodes
    Document dirty = Jsoup.parse("<p>This is a paragraph</p><strong>This is bold text</strong>");
    Cleaner cleaner = new Cleaner(Whitelist.basic());
    Document clean = Document.createShell(dirty.baseUri());
    int numDiscarded = cleaner.copySafeNodes(dirty.body(), clean.body());
    assert clean.body().children().size() == 2;
    assert numDiscarded == 0;

    // Test case 2: Unsafe nodes
    dirty = Jsoup.parse("<script>alert('Hello');</script>");
    clean = Document.createShell(dirty.baseUri());
    numDiscarded = cleaner.copySafeNodes(dirty.body(), clean.body());
    assert clean.body().children().size() == 0;
    assert numDiscarded == 1;
  }

  @Test
  public void testCreateSafeElement() {
    // Test case 1: Element with safe attributes
    Document dirty = Jsoup.parse("<div class='container' id='main'></div>");
    Cleaner cleaner = new Cleaner(Whitelist.basic());
    ElementMeta meta = cleaner.createSafeElement(dirty.select("div").first());
    assert meta.el.tagName().equals("div");
    assert meta.el.attr("class").equals("container");
    assert meta.el.attr("id").equals("main");
    assert meta.numAttribsDiscarded == 0;

    // Test case 2: Element with unsafe attribute
    dirty = Jsoup.parse("<div onload='alert(\"Hello\")'></div>");
    meta = cleaner.createSafeElement(dirty.select("div").first());
    assert meta.el.attr("onload").isEmpty();
    assert meta.numAttribsDiscarded == 1;
  }
}