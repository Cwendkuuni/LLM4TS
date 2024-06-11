import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {
  
  @Test
  public void testCreateDocument() {
    Document doc = new Document("https://example.com");
    Element head = doc.head();
    Element body = doc.body();
    
    Assert.assertEquals(Tag.valueOf("#root"), doc.tag());
    Assert.assertEquals("https://example.com", doc.baseUri());
    Assert.assertEquals("head", head.tagName());
    Assert.assertEquals("body", body.tagName());
  }
  
  @Test
  public void testCreateShell() {
    Document doc = Document.createShell("https://example.com");
    Element html = doc.getElementsByTag("html").first();
    Element head = doc.head();
    Element body = doc.body();
    
    Assert.assertEquals("https://example.com", doc.baseUri());
    Assert.assertEquals("html", html.tagName());
    Assert.assertEquals("head", head.tagName());
    Assert.assertEquals("body", body.tagName());
  }
  
  @Test
  public void testHead() {
    Document doc = new Document("https://example.com");
    Element head = doc.head();
    
    Assert.assertEquals("head", head.tagName());
  }
  
  @Test
  public void testBody() {
    Document doc = new Document("https://example.com");
    Element body = doc.body();
    
    Assert.assertEquals("body", body.tagName());
  }
  
  @Test
  public void testTitle() {
    Document doc = new Document("https://example.com");
    doc.title("Example Page");
    
    Assert.assertEquals("Example Page", doc.title());
  }
  
  @Test
  public void testTitleNoElement() {
    Document doc = new Document("https://example.com");
    
    Assert.assertEquals("", doc.title());
  }
  
  @Test
  public void testTitleElementExists() {
    Document doc = new Document("https://example.com");
    doc.appendElement("title").text("Example Page");
    
    Assert.assertEquals("Example Page", doc.title());
  }
  
  @Test
  public void testCreateElement() {
    Document doc = new Document("https://example.com");
    Element div = doc.createElement("div");
    
    Assert.assertEquals("div", div.tagName());
    Assert.assertEquals("https://example.com", div.baseUri());
  }
  
  @Test
  public void testNormalise() {
    Document doc = new Document("https://example.com");
    doc.normalise();
    
    Element html = doc.getElementsByTag("html").first();
    Element head = doc.head();
    Element body = doc.body();
    
    Assert.assertNotNull(html);
    Assert.assertNotNull(head);
    Assert.assertNotNull(body);
  }
  
  @Test
  public void testNormaliseTextNodes() {
    Document doc = new Document("https://example.com");
    Element body = doc.body();
    
    body.appendText("This is some text.");
    body.prependElement("p");
    body.prependElement("h1");
    doc.normalise();
    
    Assert.assertEquals(1, body.children().size());
    Assert.assertEquals("This is some text.", body.text());
  }
  
  @Test
  public void testOuterHtml() {
    Document doc = new Document("https://example.com");
    Element body = doc.body();
    
    body.appendElement("div").text("Hello World");
    
    Assert.assertEquals("<div>Hello World</div>", body.outerHtml());
  }
  
  @Test
  public void testText() {
    Document doc = new Document("https://example.com");
    Element body = doc.body();
    
    doc.text("This is some text.");
    
    Assert.assertEquals("This is some text.", body.text());
  }
  
  @Test
  public void testNodeName() {
    Document doc = new Document("https://example.com");
    
    Assert.assertEquals("#document", doc.nodeName());
  }
  
  @Test
  public void testOutputSettingsEscapeMode() {
    Document doc = new Document("https://example.com");
    Document.OutputSettings outputSettings = doc.outputSettings();
    
    Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
    
    outputSettings.escapeMode(Entities.EscapeMode.extended);
    
    Assert.assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
  }
  
  @Test
  public void testOutputSettingsCharset() {
    Document doc = new Document("https://example.com");
    Document.OutputSettings outputSettings = doc.outputSettings();
    
    Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
    
    outputSettings.charset(Charset.forName("ISO-8859-1"));
    
    Assert.assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
  }
  
  @Test
  public void testOutputSettingsCharsetByName() {
    Document doc = new Document("https://example.com");
    Document.OutputSettings outputSettings = doc.outputSettings();
    
    Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
    
    outputSettings.charset("ISO-8859-1");
    
    Assert.assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
  }
  
  @Test
  public void testOutputSettingsPrettyPrint() {
    Document doc = new Document("https://example.com");
    Document.OutputSettings outputSettings = doc.outputSettings();
    
    Assert.assertTrue(outputSettings.prettyPrint());
    
    outputSettings.prettyPrint(false);
    
    Assert.assertFalse(outputSettings.prettyPrint());
  }
  
  @Test
  public void testOutputSettingsIndentAmount() {
    Document doc = new Document("https://example.com");
    Document.OutputSettings outputSettings = doc.outputSettings();
    
    Assert.assertEquals(1, outputSettings.indentAmount());
    
    outputSettings.indentAmount(2);
    
    Assert.assertEquals(2, outputSettings.indentAmount());
  }
  
  @Test
  public void testOutputSettingsEncoder() {
    Document doc = new Document("https://example.com");
    Document.OutputSettings outputSettings = doc.outputSettings();
    
    Assert.assertNotNull(outputSettings.encoder());
  }
  
  @Test
  public void testOutputSettings() {
    Document doc = new Document("https://example.com");
    Document.OutputSettings outputSettings = doc.outputSettings();
    
    Assert.assertEquals(outputSettings, doc.outputSettings());
  }
}