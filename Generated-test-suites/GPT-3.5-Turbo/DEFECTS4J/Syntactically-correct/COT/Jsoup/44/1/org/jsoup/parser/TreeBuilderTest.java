import org.junit.*;
import static org.junit.Assert.*;

public class TreeBuilderTest {
  
  @Test
  public void testInitialiseParse() {
    TreeBuilder tb = new TreeBuilder();
    String input = "<html><head></head><body></body></html>";
    String baseUri = "https://www.example.com";
    ParseErrorList errors = new ParseErrorList();
    
    tb.initialiseParse(input, baseUri, errors);
    
    assertEquals(baseUri, tb.baseUri);
    assertNotNull(tb.reader);
    assertNotNull(tb.tokeniser);
    assertNotNull(tb.doc);
    assertNotNull(tb.stack);
    assertNotNull(tb.errors);
  }
  
  @Test
  public void testParse() {
    TreeBuilder tb = new TreeBuilder();
    String input = "<html><head></head><body></body></html>";
    String baseUri = "https://www.example.com";
    ParseErrorList errors = new ParseErrorList();
    
    Document doc = tb.parse(input, baseUri, errors);
    
    assertNotNull(doc);
    assertEquals(baseUri, doc.baseUri());
  }
  
  @Test
  public void testProcessStartTag() {
    TreeBuilder tb = new TreeBuilder() {
      @Override
      protected boolean process(Token token) {
        return false;
      }
    };
    
    boolean result = tb.processStartTag("div");
    
    assertFalse(result);
  }
  
  @Test
  public void testProcessStartTag_withAttributes() {
    TreeBuilder tb = new TreeBuilder() {
      @Override
      protected boolean process(Token token) {
        return false;
      }
    };
    
    Attributes attrs = new Attributes();
    attrs.put("class", "container");
    boolean result = tb.processStartTag("div", attrs);
    
    assertFalse(result);
  }
  
  @Test
  public void testProcessEndTag() {
    TreeBuilder tb = new TreeBuilder() {
      @Override
      protected boolean process(Token token) {
        return false;
      }
    };
    
    boolean result = tb.processEndTag("div");
    
    assertFalse(result);
  }
  
  @Test
  public void testCurrentElement() {
    TreeBuilder tb = new TreeBuilder();
    Element element = tb.currentElement();
    
    assertNull(element);
  }
  
}