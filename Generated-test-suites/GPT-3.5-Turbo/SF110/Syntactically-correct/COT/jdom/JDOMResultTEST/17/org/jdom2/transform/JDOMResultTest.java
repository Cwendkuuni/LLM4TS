
import org.junit.Test;

public class JDOMResultTest {

  @Test
  public void testSetResult() {
    JDOMResult result = new JDOMResult();
    
    // Create a list of contents
    List<Content> contents = new ArrayList<>();
    Content content1 = new Element("element1");
    contents.add(content1);
    
    // Set the result
    result.setResult(contents);
    
    // Verify that the result is set correctly
    List<Content> resultContents = result.getResult();
    assertEquals(contents, resultContents);
  }
  
  @Test
  public void testGetResult() {
    JDOMResult result = new JDOMResult();
    
    // Create a list of contents
    List<Content> contents = new ArrayList<>();
    Content content1 = new Element("element1");
    contents.add(content1);
    
    // Set the result
    result.setResult(contents);
    
    // Get the result and verify that it is returned correctly
    List<Content> resultContents = result.getResult();
    assertEquals(contents, resultContents);
    
    // Verify that querying the result again returns the same contents
    List<Content> queriedContents = result.getResult();
    assertEquals(contents, queriedContents);
  }
  
  @Test
  public void testSetDocument() {
    JDOMResult result = new JDOMResult();
    
    // Create a document
    Document document = new Document();
    Element root = new Element("root");
    document.setRootElement(root);
    
    // Set the document
    result.setDocument(document);
    
    // Verify that the document is set correctly
    Document resultDocument = result.getDocument();
    assertEquals(document, resultDocument);
  }
  
  @Test
  public void testGetDocument() {
    JDOMResult result = new JDOMResult();
    
    // Create a document
    Document document = new Document();
    Element root = new Element("root");
    document.setRootElement(root);
    
    // Set the document
    result.setDocument(document);
    
    // Get the document and verify that it is returned correctly
    Document resultDocument = result.getDocument();
    assertEquals(document, resultDocument);
    
    // Verify that querying the document again returns the same document
    Document queriedDocument = result.getDocument();
    assertEquals(document, queriedDocument);
  }
  
  @Test
  public void testSetFactory() {
    JDOMResult result = new JDOMResult();
    
    // Create a factory
    JDOMFactory factory = new DefaultJDOMFactory();
    
    // Set the factory
    result.setFactory(factory);
    
    // Verify that the factory is set correctly
    JDOMFactory resultFactory = result.getFactory();
    assertEquals(factory, resultFactory);
  }
  
  @Test
  public void testGetFactory() {
    JDOMResult result = new JDOMResult();
    
    // Create a factory
    JDOMFactory factory = new DefaultJDOMFactory();
    
    // Set the factory
    result.setFactory(factory);
    
    // Get the factory and verify that it is returned correctly
    JDOMFactory resultFactory = result.getFactory();
    assertEquals(factory, resultFactory);
    
    // Verify that querying the factory again returns the same factory
    JDOMFactory queriedFactory = result.getFactory();
    assertEquals(factory, queriedFactory);
  }
}
