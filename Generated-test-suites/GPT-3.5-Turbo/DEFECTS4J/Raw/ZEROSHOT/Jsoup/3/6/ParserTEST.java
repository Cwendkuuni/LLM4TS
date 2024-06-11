import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {
  
  @Test
  public void testParse() {
    // Test case 1
    String html1 = "<html><head><title>Test</title></head><body><h1>Hello World</h1><p>This is a paragraph.</p></body></html>";
    String baseUri1 = "http://www.example.com";
    Document doc1 = Parser.parse(html1, baseUri1);
    assertNotNull(doc1);
    assertEquals("http://www.example.com", doc1.baseUri());

    // Test case 2
    String html2 = "<html><head><title>Test</title></head><body><h1>Hello World</h1><p>This is a paragraph.</p></body></html>";
    String baseUri2 = "http://www.example.com";
    Document doc2 = Parser.parse(html2, baseUri2);
    assertNotNull(doc2);
    assertEquals("http://www.example.com", doc2.baseUri());
  }
  
  @Test
  public void testParseBodyFragment() {
    // Test case 1
    String bodyHtml1 = "<h1>Hello World</h1><p>This is a paragraph.</p>";
    String baseUri1 = "http://www.example.com";
    Document doc1 = Parser.parseBodyFragment(bodyHtml1, baseUri1);
    assertNotNull(doc1);
    assertEquals("http://www.example.com", doc1.baseUri());

    // Test case 2
    String bodyHtml2 = "<h1>Hello World</h1><p>This is a paragraph.</p>";
    String baseUri2 = "http://www.example.com";
    Document doc2 = Parser.parseBodyFragment(bodyHtml2, baseUri2);
    assertNotNull(doc2);
    assertEquals("http://www.example.com", doc2.baseUri());
  }
  
  @Test
  public void testParseComment() {
    Parser parser = new Parser("", "", true);
    parser.parseComment();
  }
  
  @Test
  public void testParseXmlDecl() {
    Parser parser = new Parser("", "", true);
    parser.parseXmlDecl();
  }
  
  @Test
  public void testParseEndTag() {
    Parser parser = new Parser("", "", true);
    parser.parseEndTag();
  }
  
  @Test
  public void testParseStartTag() {
    Parser parser = new Parser("", "", true);
    parser.parseStartTag();
  }
  
  @Test
  public void testParseAttribute() {
    Parser parser = new Parser("", "", true);
    parser.parseAttribute();
  }
  
  @Test
  public void testParseTextNode() {
    Parser parser = new Parser("", "", true);
    parser.parseTextNode();
  }
  
  @Test
  public void testParseCdata() {
    Parser parser = new Parser("", "", true);
    parser.parseCdata();
  }
  
  @Test
  public void testAddChildToParent() {
    Parser parser = new Parser("", "", true);
    parser.addChildToParent(null, false);
  }
  
  @Test
  public void testStackHasValidParent() {
    Parser parser = new Parser("", "", true);
    parser.stackHasValidParent(null);
  }
  
  @Test
  public void testPopStackToSuitableContainer() {
    Parser parser = new Parser("", "", true);
    parser.popStackToSuitableContainer(null);
  }
  
  @Test
  public void testPopStackToClose() {
    Parser parser = new Parser("", "", true);
    parser.popStackToClose(null);
  }
  
  @Test
  public void testLast() {
    Parser parser = new Parser("", "", true);
    parser.last();
  }
  
  @Test
  public void testParse() {
    Parser parser = new Parser("", "", true);
    parser.parse();
  }
  
  @Test
  public void testParseBodyFragment() {
    Parser parser = new Parser("", "", true);
    parser.parseBodyFragment();
  }
  
}