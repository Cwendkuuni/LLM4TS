import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.TreeBuilder;

public class TreeBuilderTest {

  // Test cases for initialiseParse() method

  @Test
  public void testInitialiseParse_validInput() {
    TreeBuilder tb = new TreeBuilderImpl();
    String input = "<html><body><h1>Hello World</h1></body></html>";
    String baseUri = "https://example.com";
    ParseErrorList errors = ParseErrorList.noTracking();

    tb.initialiseParse(input, baseUri, errors);

    Document doc = tb.doc;
    CharacterReader reader = tb.reader;
    Tokeniser tokeniser = tb.tokeniser;
    ArrayList<Element> stack = tb.stack;
    String actualBaseUri = tb.baseUri;
    ParseErrorList actualErrors = tb.errors;

    assertNotNull(doc);
    assertNotNull(reader);
    assertNotNull(tokeniser);
    assertNotNull(stack);
    assertEquals(baseUri, actualBaseUri);
    assertEquals(errors, actualErrors);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitialiseParse_nullInput() {
    TreeBuilder tb = new TreeBuilderImpl();
    String input = null;
    String baseUri = "https://example.com";
    ParseErrorList errors = ParseErrorList.noTracking();

    tb.initialiseParse(input, baseUri, errors);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitialiseParse_nullBaseUri() {
    TreeBuilder tb = new TreeBuilderImpl();
    String input = "<html><body><h1>Hello World</h1></body></html>";
    String baseUri = null;
    ParseErrorList errors = ParseErrorList.noTracking();

    tb.initialiseParse(input, baseUri, errors);
  }

  // Test cases for parse() method

  @Test
  public void testParse_validInput() {
    TreeBuilder tb = new TreeBuilderImpl();
    String input = "<html><body><h1>Hello World</h1></body></html>";
    String baseUri = "https://example.com";
    ParseErrorList errors = ParseErrorList.noTracking();

    Document doc = tb.parse(input, baseUri, errors);

    assertNotNull(doc);
    assertEquals(baseUri, doc.baseUri());
    assertEquals(1, doc.getElementsByTag("h1").size());
    assertEquals("Hello World", doc.getElementsByTag("h1").first().text());
  }

  @Test
  public void testParse_invalidInput() {
    TreeBuilder tb = new TreeBuilderImpl();
    String input = "<html><body><h1>Hello World</h1></body>";
    String baseUri = "https://example.com";
    ParseErrorList errors = ParseErrorList.noTracking();

    Document doc = tb.parse(input, baseUri, errors);

    assertNull(doc);
    assertEquals(1, errors.size());
    assertEquals("Unclosed tag error", errors.get(0).errorMessage());
  }

  // Test cases for process() method

  @Test
  public void testProcess_validToken() {
    TreeBuilder tb = new TreeBuilderImpl();
    Token token = new Token.StartTag().name("h1");

    boolean result = tb.process(token);

    assertTrue(result);
    assertEquals(1, tb.stack.size());
    assertEquals("h1", tb.stack.get(0).tagName());
  }

  @Test
  public void testProcess_invalidToken() {
    TreeBuilder tb = new TreeBuilderImpl();
    Token token = new Token.Character().data("Some text");

    boolean result = tb.process(token);

    assertFalse(result);
    assertEquals(0, tb.stack.size());
  }

  // Test cases for processStartTag() method

  @Test
  public void testProcessStartTag_validTagName() {
    TreeBuilder tb = new TreeBuilderImpl();
    String tagName = "h1";

    boolean result = tb.processStartTag(tagName);

    assertTrue(result);
    assertEquals(1, tb.stack.size());
    assertEquals("h1", tb.stack.get(0).tagName());
  }

  @Test
  public void testProcessStartTag_invalidTagName() {
    TreeBuilder tb = new TreeBuilderImpl();
    String tagName = "";

    boolean result = tb.processStartTag(tagName);

    assertFalse(result);
    assertEquals(0, tb.stack.size());
  }

  // Test cases for processStartTag() method with attributes

  @Test
  public void testProcessStartTag_validTagNameAndAttributes() {
    TreeBuilder tb = new TreeBuilderImpl();
    String tagName = "h1";
    Attributes attrs = new Attributes();
    attrs.put("class", "header");

    boolean result = tb.processStartTag(tagName, attrs);

    assertTrue(result);
    assertEquals(1, tb.stack.size());
    assertEquals("h1", tb.stack.get(0).tagName());
    assertEquals("header", tb.stack.get(0).attr("class"));
  }

  @Test
  public void testProcessStartTag_invalidTagNameAndAttributes() {
    TreeBuilder tb = new TreeBuilderImpl();
    String tagName = "";
    Attributes attrs = new Attributes();
    attrs.put("class", "header");

    boolean result = tb.processStartTag(tagName, attrs);

    assertFalse(result);
    assertEquals(0, tb.stack.size());
  }

  // Test case for processEndTag() method

  @Test
  public void testProcessEndTag_validTagName() {
    TreeBuilder tb = new TreeBuilderImpl();
    String tagName = "h1";

    boolean result = tb.processEndTag(tagName);

    assertTrue(result);
    assertEquals(0, tb.stack.size());
  }

  @Test
  public void testProcessEndTag_invalidTagName() {
    TreeBuilder tb = new TreeBuilderImpl();
    String tagName = "";

    boolean result = tb.processEndTag(tagName);

    assertFalse(result);
    assertEquals(0, tb.stack.size());
  }

  // Test case for currentElement() method

  @Test
  public void testCurrentElement_oneElementInStack() {
    TreeBuilder tb = new TreeBuilderImpl();
    Element element = new Element("h1");

    tb.stack.add(element);

    Element currentElement = tb.currentElement();

    assertEquals(element, currentElement);
  }

  @Test
  public void testCurrentElement_emptyStack() {
    TreeBuilder tb = new TreeBuilderImpl();

    Element currentElement = tb.currentElement();

    assertNull(currentElement);
  }
}