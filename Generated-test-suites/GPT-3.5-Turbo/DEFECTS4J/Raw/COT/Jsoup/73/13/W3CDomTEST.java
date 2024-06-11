
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.jsoup.nodes.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class W3CDomTest {
  
  @Test
  public void testFromJsoup() {
    // Test setup
    org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document();
    W3CDom w3cDom = new W3CDom();
    
    // Test
    Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    
    // Assertions
    assertNotNull(w3cDoc);
  }
  
  @Test
  public void testConvert() {
    // Test setup
    org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document();
    Document w3cDoc = new Document();
    W3CDom w3cDom = new W3CDom();
    
    // Test
    w3cDom.convert(jsoupDoc, w3cDoc);
    
    // Assertions
    assertNotNull(w3cDoc);
  }
  
  @Test
  public void testAsString() {
    // Test setup
    Document w3cDoc = new Document();
    W3CDom w3cDom = new W3CDom();
    
    // Test
    String w3cDocString = w3cDom.asString(w3cDoc);
    
    // Assertions
    assertNotNull(w3cDocString);
  }
}
