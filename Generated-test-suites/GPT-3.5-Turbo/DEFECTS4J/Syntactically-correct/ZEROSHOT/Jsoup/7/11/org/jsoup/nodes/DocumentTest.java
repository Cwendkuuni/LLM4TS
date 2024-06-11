import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DocumentTest {

  private Document document;

  @BeforeEach
  public void setup() {
    document = new Document("http://example.com");
  }

  @Test
  public void testConstructor() {
    assertEquals("#root", document.nodeName());
    assertEquals("http://example.com", document.baseUri());
  }

  @Test
  public void testCreateShell() {
    Document shell = Document.createShell("http://example.com");
    assertEquals("http://example.com", shell.baseUri());
    assertEquals("html", shell.child(0).nodeName());
    assertEquals("head", shell.head().nodeName());
    assertEquals("body", shell.body().nodeName());
  }

  @Test
  public void testHead() {
    Element head = document.head();
    assertEquals("head", head.nodeName());
  }

  @Test
  public void testBody() {
    Element body = document.body();
    assertEquals("body", body.nodeName());
  }

  @Test
  public void testTitle() {
    Element titleEl = mock(Element.class);
    when(document.getElementsByTag("title")).thenReturn(new Elements(titleEl));
    when(titleEl.text()).thenReturn("Example Title");

    String title = document.title();
    assertEquals("Example Title", title);
    verify(titleEl, times(1)).text();
  }

  @Test
  public void testSetTitle() {
    Element titleEl = mock(Element.class);
    when(document.getElementsByTag("title")).thenReturn(new Elements(titleEl));

    document.title("New Title");
    verify(document, times(1)).getElementsByTag("title");
    verify(titleEl, times(1)).text("New Title");
  }

  @Test
  public void testCreateElement() {
    Element element = document.createElement("div");
    assertEquals("div", element.nodeName());
    assertEquals("http://example.com", element.baseUri());
  }

  @Test
  public void testNormalise() {
    Element htmlEl = mock(Element.class);
    when(document.baseUri()).thenReturn("http://example.com");
    when(document.firstChild()).thenReturn(htmlEl);
    when(htmlEl.nodeName()).thenReturn("html");
    when(document.head()).thenReturn(null);
    when(htmlEl.prependElement(anyString())).thenReturn(mock(Element.class));
    when(document.body()).thenReturn(null);
    when(htmlEl.appendElement(anyString())).thenReturn(mock(Element.class));

    Document normalizedDoc = document.normalise();
    verify(document, times(1)).prependElement("head");
    verify(htmlEl, times(1)).prependElement("head");
    verify(document, times(1)).appendElement("body");
    verify(htmlEl, times(1)).appendElement("body");
    verify(document, times(1)).childNodes();
    verify(document, times(1)).removeChild(any(Node.class));
    verify(document, times(1)).prependChild(any(TextNode.class));
    verify(document, times(2)).prependChild(any(Node.class));
    assertEquals(document, normalizedDoc);
  }

  @Test
  public void testOuterHtml() {
    String html = document.outerHtml();
    assertEquals(document.html(), html);
  }

  @Test
  public void testText() {
    Element body = mock(Element.class);
    when(document.body()).thenReturn(body);

    Element result = document.text("Sample text");
    verify(body, times(1)).text("Sample text");
    assertEquals(document, result);
  }

  @Test
  public void testNodeName() {
    String nodeName = document.nodeName();
    assertEquals("#document", nodeName);
  }

  @Test
  public void testOutputSettings() {
    Document.OutputSettings outputSettings = document.outputSettings();
    assertEquals(Document.OutputSettings.class, outputSettings.getClass());
    assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
    assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
    assertEquals(true, outputSettings.prettyPrint());
    assertEquals(1, outputSettings.indentAmount());
  }
}