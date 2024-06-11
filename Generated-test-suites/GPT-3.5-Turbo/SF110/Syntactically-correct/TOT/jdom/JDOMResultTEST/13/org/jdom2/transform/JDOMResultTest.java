import org.junit.*;
import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.transform.*;
import org.xml.sax.*;
import javax.xml.transform.sax.*;
import java.util.*;

public class JDOMResultTest {
  
  @Test
  public void testSetResult() {
    JDOMResult result = new JDOMResult();
    
    Element element1 = new Element("element1");
    Element element2 = new Element("element2");
    List<Content> resultList = Arrays.asList(element1, element2);
    
    result.setResult(resultList);
    
    assertEquals(resultList, result.getResult());
  }
  
  @Test
  public void testSetResult_emptyList() {
    JDOMResult result = new JDOMResult();
    
    List<Content> resultList = Collections.emptyList();
    
    result.setResult(resultList);
    
    assertEquals(resultList, result.getResult());
  }
  
  @Test
  public void testGetResult_retrieveResultNotCalled() {
    JDOMResult result = new JDOMResult();
    
    Element element1 = new Element("element1");
    Element element2 = new Element("element2");
    List<Content> resultList = Arrays.asList(element1, element2);
    result.setResult(resultList);
    
    assertEquals(resultList, result.getResult());
  }
  
  @Test
  public void testGetResult_resultListNull() {
    JDOMResult result = new JDOMResult();
    
    result.retrieveResult();
    
    assertEquals(Collections.emptyList(), result.getResult());
  }
  
  @Test
  public void testGetResult_resultDocNull() {
    JDOMResult result = new JDOMResult();
    result.setDocument(null);
    
    List<Content> contentList = Arrays.asList(new Element("element1"), new Element("element2"));
    result.setResult(contentList);

    assertEquals(contentList, result.getResult());
  }
  
  @Test
  public void testGetResult_resultDocNotNull_queriedFalse() {
    JDOMResult result = new JDOMResult();
    
    List<Content> contentList = Arrays.asList(new Element("element1"), new Element("element2"));
    Document document = new Document(contentList);
    
    result.setDocument(document);
    
    assertEquals(contentList, result.getResult());
  }
  
  @Test
  public void testGetResult_resultDocNotNull_queriedTrue() {
    JDOMResult result = new JDOMResult();
    
    List<Content> contentList = Arrays.asList(new Element("element1"), new Element("element2"));
    Document document = new Document(contentList);
    
    result.setDocument(document);
    
    result.getResult();
    
    assertEquals(contentList, result.getResult());
  }
  
  @Test
  public void testSetDocument() {
    JDOMResult result = new JDOMResult();
    
    List<Content> contentList = Arrays.asList(new Element("element1"), new Element("element2"));
    Document document = new Document(contentList);
    
    result.setDocument(document);
    
    assertEquals(document, result.getDocument());
  }
  
  @Test
  public void testGetDocument_retrieveResultNotCalled() {
    JDOMResult result = new JDOMResult();
    
    List<Content> contentList = Arrays.asList(new Element("element1"), new Element("element2"));
    Document document = new Document(contentList);
    
    result.setDocument(document);
    
    assertEquals(document, result.getDocument());
  }
  
  @Test
  public void testGetDocument_resultListNotNull() {
    JDOMResult result = new JDOMResult();
    
    Element element1 = new Element("element1");
    Element element2 = new Element("element2");
    List<Content> resultList = Arrays.asList(element1, element2);
    
    result.setResult(resultList);
    
    assertEquals(null, result.getDocument());
  }
  
  @Test
  public void testGetDocument_resultDocNull() {
    JDOMResult result = new JDOMResult();
    
    result.retrieveResult();
    
    assertEquals(null, result.getDocument());
  }
  
  @Test
  public void testGetDocument_resultDocNotNull_queriedFalse() {
    JDOMResult result = new JDOMResult();
    
    List<Content> contentList = Arrays.asList(new Element("element1"), new Element("element2"));
    Document document = new Document(contentList);
    
    result.setDocument(document);
    
    assertEquals(document, result.getDocument());
  }
  
  @Test
  public void testGetDocument_resultDocNotNull_queriedTrue() {
    JDOMResult result = new JDOMResult();
    
    List<Content> contentList = Arrays.asList(new Element("element1"), new Element("element2"));
    Document document = new Document(contentList);
    
    result.setDocument(document);
    
    result.getResult();
    
    assertEquals(document, result.getDocument());
  }
  
  @Test
  public void testSetFactory() {
    JDOMResult result = new JDOMResult();
    JDOMFactory factory = new DefaultJDOMFactory();
    
    result.setFactory(factory);
    
    assertEquals(factory, result.getFactory());
  }
  
  @Test
  public void testGetFactory() {
    JDOMResult result = new JDOMResult();
    JDOMFactory factory = new DefaultJDOMFactory();
    
    result.setFactory(factory);
    
    assertEquals(factory, result.getFactory());
  }
  
  @Test
  public void testSetHandler() {
    JDOMResult result = new JDOMResult();
    ContentHandler handler = new DefaultHandler();
    
    result.setHandler(handler);
    
    // No assertions made as this method does not modify any values in the class
  }
  
  @Test
  public void testSetLexicalHandler() {
    JDOMResult result = new JDOMResult();
    LexicalHandler handler = new DefaultHandler();
    
    result.setLexicalHandler(handler);
    
    // No assertions made as this method does not modify any values in the class
  }
  
  @Test
  public void testFragmentHandler() {
    JDOMResult.FragmentHandler handler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
    
    Element element1 = new Element("element1");
    Element element2 = new Element("element2");
    
    handler.startElement(null, "element1", "element1", null);
    handler.startElement(null, "element2", "element2", null);
    handler.endElement(null, "element1", "element1");
    handler.endElement(null, "element2", "element2");
    
    List<Content> expectedResult = Arrays.asList(element1, element2);
    
    assertEquals(expectedResult, handler.getResult());
  }
  
  @Test
  public void testDocumentBuilder() throws SAXException {
    JDOMResult.DocumentBuilder builder = new JDOMResult.DocumentBuilder();
    JDOMResult.FragmentHandler handler = new JDOMResult.FragmentHandler(new DefaultJDOMFactory());
    
    Element element1 = new Element("element1");
    Element element2 = new Element("element2");
    
    builder.startElement(null, "element1", "element1", null);
    builder.startElement(null, "element2", "element2", null);
    builder.endElement(null, "element1", "element1");
    builder.endElement(null, "element2", "element2");
    
    List<Content> expectedResult = Arrays.asList(element1, element2);
    
    assertEquals(expectedResult, builder.getResult());
  }
  
}