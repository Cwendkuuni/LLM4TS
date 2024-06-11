import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

public class W3CDomTest {
  
  private static W3CDom w3CDom;
  
  @BeforeClass
  public static void setUp(){
    w3CDom = new W3CDom();
  }
  
  @Test
  public void testFromJsoup() {
    // Test case
    org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document();
    // Set up the jsoup document
    // ...
    
    Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
    // Assertions
    // ...
  }
  
  @Test
  public void testConvert() {
    // Test case
    org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document();
    // Set up the jsoup document
    // ...
    Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
    
    Document out = w3CDom.convert(jsoupDoc, w3cDoc);
    // Assertions
    // ...
  }
  
  @Test
  public void testW3CBuilder() {
    // Test case
    Document doc = new Document();
    // Set up the W3C document
    // ...
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
    
    // Test head method
    int depth = 0;
    w3cBuilder.head(source, depth);
    // Assertions
    // ...
    
    // Test tail method
    w3cBuilder.tail(source, depth);
    // Assertions
    // ...
  }
  
  @Test
  public void testCopyAttributes() {
    // Test case
    org.jsoup.nodes.Element source = new org.jsoup.nodes.Element();
    // Set up the source element
    // ...
    Element el = new Element();
    // Set up the W3C element
    // ...
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(new Document());
    
    w3cBuilder.copyAttributes(source, el);
    // Assertions
    // ...
  }
  
  @Test
  public void testUpdateNamespaces() {
    // Test case
    org.jsoup.nodes.Element el = new org.jsoup.nodes.Element();
    // Set up the element
    // ...
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(new Document());
    
    String prefix = w3cBuilder.updateNamespaces(el);
    // Assertions
    // ...
  }
  
  @Test
  public void testAsString() {
    // Test case
    Document doc = new Document();
    // Set up the W3C document
    // ...
    W3CDom w3cDom = new W3CDom();
    
    String result = w3cDom.asString(doc);
    // Assertions
    // ...
  }
}