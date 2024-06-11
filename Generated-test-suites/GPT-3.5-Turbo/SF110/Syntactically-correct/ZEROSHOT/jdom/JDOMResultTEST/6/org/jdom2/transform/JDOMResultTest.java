import org.jdom2.*;
import org.jdom2.transform.*;
import org.jdom2.input.*;
import org.xml.sax.helpers.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class JDOMResultTest {
  
  @Test
  public void testSetResult() {
    JDOMResult result = new JDOMResult();
    List<Content> contentList = new ArrayList<Content>();
    contentList.add(new Element("element"));
    result.setResult(contentList);
    List<Content> resultContent = result.getResult();
    assertEquals(contentList, resultContent);
  }
  
  @Test
  public void testGetResult() {
    JDOMResult result = new JDOMResult();
    List<Content> contentList = new ArrayList<Content>();
    contentList.add(new Element("element"));
    result.setResult(contentList);
    List<Content> resultContent = result.getResult();
    assertEquals(contentList, resultContent);
  }
  
  @Test
  public void testSetDocument() {
    JDOMResult result = new JDOMResult();
    Document document = new Document();
    result.setDocument(document);
    Document resultDocument = result.getDocument();
    assertEquals(document, resultDocument);
  }
  
  @Test
  public void testGetDocument() {
    JDOMResult result = new JDOMResult();
    Document document = new Document();
    result.setDocument(document);
    Document resultDocument = result.getDocument();
    assertEquals(document, resultDocument);
  }
  
  @Test
  public void testSetFactory() {
    JDOMResult result = new JDOMResult();
    JDOMFactory factory = new DefaultJDOMFactory();
    result.setFactory(factory);
    JDOMFactory resultFactory = result.getFactory();
    assertEquals(factory, resultFactory);
  }
  
  @Test
  public void testGetFactory() {
    JDOMResult result = new JDOMResult();
    JDOMFactory factory = new DefaultJDOMFactory();
    result.setFactory(factory);
    JDOMFactory resultFactory = result.getFactory();
    assertEquals(factory, resultFactory);
  }
  
  @Test
  public void testSetHandler() {
    JDOMResult result = new JDOMResult();
    result.setHandler(null);
    // No assertion, as the method does not return anything
  }
  
  @Test
  public void testSetLexicalHandler() {
    JDOMResult result = new JDOMResult();
    result.setLexicalHandler(null);
    // No assertion, as the method does not return anything
  }
  
}