import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class NamespaceStackTest {
  
  @Test
  public void testNamespaceStackConstructor() {
    Namespace[] seed = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
    NamespaceStack nsStack = new NamespaceStack(seed);
    assertNotNull(nsStack);
  }
  
  @Test
  public void testNamespaceStackPushElement() {
    NamespaceStack nsStack = new NamespaceStack();
    Element elem = new Element("testElement");
    elem.addNamespaceDeclaration(Namespace.getNamespace("test", "http://www.test.com"));
    nsStack.push(elem);
    assertTrue(nsStack.isInScope(Namespace.getNamespace("test", "http://www.test.com")));
  }
  
  @Test
  public void testNamespaceStackPushAttribute() {
    NamespaceStack nsStack = new NamespaceStack();
    Element elem = new Element("testElement");
    Attribute attr = new Attribute("testAttr", "value", Namespace.getNamespace("test", "http://www.test.com"));
    elem.setAttribute(attr);
    nsStack.push(attr);
    assertTrue(nsStack.isInScope(Namespace.getNamespace("test", "http://www.test.com")));
  }
  
  @Test
  public void testNamespaceStackPop() {
    NamespaceStack nsStack = new NamespaceStack();
    Element elem = new Element("testElement");
    nsStack.push(elem);
    nsStack.pop();
    assertFalse(nsStack.isInScope(Namespace.getNamespace("test", "http://www.test.com")));
  }
  
  @Test
  public void testNamespaceStackAddedForward() {
    NamespaceStack nsStack = new NamespaceStack();
    Element elem = new Element("testElement");
    elem.addNamespaceDeclaration(Namespace.getNamespace("test", "http://www.test.com"));
    nsStack.push(elem);
    Iterable<Namespace> added = nsStack.addedForward();
    assertNotNull(added);
  }
  
  @Test
  public void testNamespaceStackAddedReverse() {
    NamespaceStack nsStack = new NamespaceStack();
    Element elem = new Element("testElement");
    elem.addNamespaceDeclaration(Namespace.getNamespace("test", "http://www.test.com"));
    nsStack.push(elem);
    Iterable<Namespace> added = nsStack.addedReverse();
    assertNotNull(added);
  }
  
  @Test
  public void testNamespaceStackIterator() {
    NamespaceStack nsStack = new NamespaceStack();
    Element elem = new Element("testElement");
    elem.addNamespaceDeclaration(Namespace.getNamespace("test", "http://www.test.com"));
    nsStack.push(elem);
    assertNotNull(nsStack.iterator());
  }
}