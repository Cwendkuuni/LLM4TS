
import org.junit.Test;
import org.jdom2.*;

public class NamespaceStackTest {
  
  @Test
  public void testPushWithElement() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
    stack.push(element);
    assert stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")) == true;
  }
  
  @Test
  public void testPushWithAttribute() {
    NamespaceStack stack = new NamespaceStack();
    Attribute attribute = new Attribute("testAttribute", "test", "http://www.example.com");
    stack.push(attribute);
    assert stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")) == true;
  }
  
  @Test
  public void testPop() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
    stack.push(element);
    stack.pop();
    assert stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")) == false;
  }
  
  @Test
  public void testAddedForward() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
    stack.push(element);
    assert stack.addedForward().iterator().next() == Namespace.getNamespace("test", "http://www.example.com");
  }
  
  @Test
  public void testAddedReverse() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
    stack.push(element);
    assert stack.addedReverse().iterator().next() == Namespace.getNamespace("test", "http://www.example.com");
  }
  
  @Test
  public void testIterator() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
    stack.push(element);
    assert stack.iterator().next() == Namespace.getNamespace("test", "http://www.example.com");
  }
  
  @Test
  public void testGetScope() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
    stack.push(element);
    assert stack.getScope()[0] == Namespace.getNamespace("test", "http://www.example.com");
  }
  
  @Test
  public void testIsInScope() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
    stack.push(element);
    assert stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")) == true;
  }
}
