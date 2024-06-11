import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

   @Test
   public void testDocumentConstructor() {
      Document document = new Document("http://example.com");
      
      Assert.assertEquals("#root", document.tagName());
      Assert.assertEquals("http://example.com", document.baseUri());
   }

   @Test
   public void testCreateShell() {
      Document document = Document.createShell("http://example.com");

      Assert.assertEquals("html", document.head().parent().tagName());
      Assert.assertEquals("html", document.body().parent().tagName());
   }

   @Test
   public void testHead() {
      Document document = Document.createShell("http://example.com");

      Element head = document.head();

      Assert.assertEquals("head", head.tagName());
   }

   @Test
   public void testBody() {
      Document document = Document.createShell("http://example.com");

      Element body = document.body();

      Assert.assertEquals("body", body.tagName());
   }

   @Test
   public void testTitle() {
      Document document = Document.createShell("http://example.com");

      Assert.assertEquals("", document.title());

      document.title("Test Title");

      Assert.assertEquals("Test Title", document.title());
   }

   @Test
   public void testTitleWithExistingElement() {
      Document document = Document.createShell("http://example.com");
      Element titleElement = document.head().appendElement("title");

      Assert.assertEquals("", document.title());

      document.title("Test Title");

      Assert.assertEquals("Test Title", document.title());
      Assert.assertEquals("Test Title", titleElement.text());
   }

   @Test
   public void testNormalise() {
      Document document = Document.createShell("http://example.com");
      Element body = document.body();

      body.appendText("Text 1");
      body.appendElement("div").appendText("Text 2");

      document.normalise();

      Assert.assertEquals("Text 1 Text 2", body.text());
   }

   @Test
   public void testCreateElement() {
      Document document = Document.createShell("http://example.com");

      Element element = document.createElement("div");

      Assert.assertEquals("div", element.tagName());
      Assert.assertEquals("http://example.com", element.baseUri());
   }

   @Test
   public void testOuterHtml() {
      Document document = Document.createShell("http://example.com");
      Element body = document.body();

      body.appendElement("div").attr("class", "test");

      Assert.assertEquals("<div class=\"test\"></div>", document.outerHtml());
   }

   @Test
   public void testTextNode() {
      Document document = Document.createShell("http://example.com");
      Element body = document.body();

      body.appendText("Test Text");

      Assert.assertEquals("Test Text", body.text());
   }

   @Test
   public void testNodeName() {
      Document document = Document.createShell("http://example.com");

      Assert.assertEquals("#document", document.nodeName());
   }
}