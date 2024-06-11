import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Content;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Document;
import org.jdom2.JDOMFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMResultTest {
  
  private JDOMResult result;

  @Before
  public void setUp() {
    result = new JDOMResult();
  }

  @Test
  public void testSetResult() {
    List<Content> input = new ArrayList<Content>();
    Content content = new Content();
    input.add(content);
    result.setResult(input);
    List<Content> output = result.getResult();
    assertEquals(input, output);
  }

  @Test
  public void testGetResult() {
    List<Content> input = new ArrayList<Content>();
    Content content = new Content();
    input.add(content);
    result.setResult(input);
    List<Content> output = result.getResult();
    assertEquals(input, output);
  }

  @Test
  public void testSetDocument() {
    Document document = new Document();
    result.setDocument(document);
    Document output = result.getDocument();
    assertEquals(document, output);
  }

  @Test
  public void testGetDocument() {
    Document document = new Document();
    result.setDocument(document);
    Document output = result.getDocument();
    assertEquals(document, output);
  }

  @Test
  public void testSetFactory() {
    JDOMFactory factory = new DefaultJDOMFactory();
    result.setFactory(factory);
    JDOMFactory output = result.getFactory();
    assertEquals(factory, output);
  }

  @Test
  public void testGetFactory() {
    JDOMFactory factory = new DefaultJDOMFactory();
    result.setFactory(factory);
    JDOMFactory output = result.getFactory();
    assertEquals(factory, output);
  }
}