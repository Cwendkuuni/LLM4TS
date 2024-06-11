import org.junit.Before;
import org.junit.Test;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Attribute;

import static org.junit.Assert.*;

public class NamespaceStackTest {
  
  private NamespaceStack stack;
  
  @Before
  public void setup() {
    stack = new NamespaceStack();
  }
  
  @Test
  public void testPushElementWithAdditionalNamespaces() {
    Element element = new Element("testElement");
    Namespace defaultNamespace = Namespace.getNamespace("http://www.example.com/default");
    Namespace customNamespace1 = Namespace.getNamespace("http://www.example.com/custom");
    Namespace customNamespace2 = Namespace.getNamespace("http://www.example.com/custom2");
    
    element.addNamespaceDeclaration(defaultNamespace);
    element.addNamespaceDeclaration(customNamespace1);
    element.addNamespaceDeclaration(customNamespace2);
    
    stack.push(element);
    
    assertTrue(stack.isInScope(defaultNamespace));
    assertTrue(stack.isInScope(customNamespace1));
    assertTrue(stack.isInScope(customNamespace2));
  }
  
  @Test
  public void testPushElementWithAttributes() {
    Element element = new Element("testElement");
    Namespace defaultNamespace = Namespace.getNamespace("http://www.example.com/default");
    Namespace customNamespace = Namespace.getNamespace("http://www.example.com/custom");
    Attribute attribute = new Attribute("testAttribute", "testValue", customNamespace);
    
    element.addNamespaceDeclaration(defaultNamespace);
    element.setAttribute(attribute);
    
    stack.push(element);
    
    assertTrue(stack.isInScope(defaultNamespace));
    assertTrue(stack.isInScope(customNamespace));
  }
  
  @Test
  public void testPushElementWithNoAdditionalNamespacesAndAttributes() {
    Element element = new Element("testElement");
    Namespace defaultNamespace = Namespace.getNamespace("http://www.example.com/default");
    
    element.addNamespaceDeclaration(defaultNamespace);

    stack.push(element);
    
    assertTrue(stack.isInScope(defaultNamespace));
  }
  
  @Test
  public void testPushAttribute() {
    Attribute attribute = new Attribute("testAttribute", "testValue");
    
    stack.push(attribute);
    
    assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testPopWhenStackIsEmpty() {
    stack.pop();
  }
  
  @Test
  public void testPopValidCase() {
    Element element1 = new Element("testElement1");
    Element element2 = new Element("testElement2");
    Namespace defaultNamespace1 = Namespace.getNamespace("http://www.example.com/default1");
    Namespace defaultNamespace2 = Namespace.getNamespace("http://www.example.com/default2");
    
    element1.addNamespaceDeclaration(defaultNamespace1);
    stack.push(element1);
    
    element2.addNamespaceDeclaration(defaultNamespace2);
    stack.push(element2);
    
    stack.pop();
    
    assertTrue(stack.isInScope(defaultNamespace1));
    assertFalse(stack.isInScope(defaultNamespace2));
  }
  
  @Test
  public void testAddedForwardIteratorWhenStackIsEmpty() {
    assertFalse(stack.addedForward().iterator().hasNext());
  }
  
  @Test
  public void testAddedReverseIteratorWhenStackIsEmpty() {
    assertFalse(stack.addedReverse().iterator().hasNext());
  }
  
  @Test
  public void testGetScopeWhenStackIsEmpty() {
    assertEquals(0, stack.getScope().length);
  }
  
  @Test
  public void testIsInScopeWhenNamespaceIsInScope() {
    Element element = new Element("testElement");
    Namespace customNamespace = Namespace.getNamespace("http://www.example.com/custom");
    element.addNamespaceDeclaration(customNamespace);
    stack.push(element);
    
    assertTrue(stack.isInScope(customNamespace));
  }
  
  @Test
  public void testIsInScopeWhenNamespaceIsNotInScope() {
    Element element = new Element("testElement");
    Namespace customNamespace = Namespace.getNamespace("http://www.example.com/custom");
    stack.push(element);
    
    assertFalse(stack.isInScope(customNamespace));
  }

}