import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import org.jdom2.util.NamespaceStack;
import java.util.*;

public class NamespaceStackTest {

@Test
public void testPush_Element() {
    NamespaceStack stack = new NamespaceStack();
    Element element1 = new Element("element1", Namespace.getNamespace("test", "http://www.test.com"));
    stack.push(element1);
    Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element1.getNamespace()};
    assertArrayEquals(expectedScope, stack.getScope());

    Element element2 = new Element("element2", Namespace.getNamespace("test2", "http://www.test2.com"));
    stack.push(element2);
    Namespace[] expectedScope2 = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element2.getNamespace()};
    assertArrayEquals(expectedScope2, stack.getScope());
}

@Test
public void testPush_Attribute() {
    NamespaceStack stack = new NamespaceStack();
    Attribute attribute1 = new Attribute("attribute1", "value1", Namespace.getNamespace("test", "http://www.test.com"));
    stack.push(attribute1);
    Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, attribute1.getNamespace()};
    assertArrayEquals(expectedScope, stack.getScope());

    Attribute attribute2 = new Attribute("attribute2", "value2", Namespace.getNamespace("test2", "http://www.test2.com"));
    stack.push(attribute2);
    Namespace[] expectedScope2 = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, attribute2.getNamespace()};
    assertArrayEquals(expectedScope2, stack.getScope());
}

@Test
public void testPop() {
    NamespaceStack stack = new NamespaceStack();
    Element element1 = new Element("element1", Namespace.getNamespace("test", "http://www.test.com"));
    stack.push(element1);
    stack.pop();
    Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
    assertArrayEquals(expectedScope, stack.getScope());

    Attribute attribute1 = new Attribute("attribute1", "value1", Namespace.getNamespace("test", "http://www.test.com"));
    stack.push(attribute1);
    stack.pop();
    assertArrayEquals(expectedScope, stack.getScope());
}

@Test
public void testAddedForward() {
    NamespaceStack stack = new NamespaceStack();
    Element element1 = new Element("element1", Namespace.getNamespace("test", "http://www.test.com"));
    stack.push(element1);
    Element element2 = new Element("element2", Namespace.getNamespace("test2", "http://www.test2.com"));
    stack.push(element2);
    Iterable<Namespace> added = stack.addedForward();
    List<Namespace> expectedAdded = new ArrayList<Namespace>();
    expectedAdded.add(Namespace.getNamespace("test", "http://www.test.com"));
    expectedAdded.add(Namespace.getNamespace("test2", "http://www.test2.com"));
    assertEquals(expectedAdded, added);
}

@Test
public void testAddedReverse() {
    NamespaceStack stack = new NamespaceStack();
    Element element1 = new Element("element1", Namespace.getNamespace("test", "http://www.test.com"));
    stack.push(element1);
    Element element2 = new Element("element2", Namespace.getNamespace("test2", "http://www.test2.com"));
    stack.push(element2);
    Iterable<Namespace> added = stack.addedReverse();
    List<Namespace> expectedAdded = new ArrayList<Namespace>();
    expectedAdded.add(Namespace.getNamespace("test2", "http://www.test2.com"));
    expectedAdded.add(Namespace.getNamespace("test", "http://www.test.com"));
    assertEquals(expectedAdded, added);
}

@Test
public void testIterator() {
    NamespaceStack stack = new NamespaceStack();
    Element element1 = new Element("element1", Namespace.getNamespace("test", "http://www.test.com"));
    stack.push(element1);
    Element element2 = new Element("element2", Namespace.getNamespace("test2", "http://www.test2.com"));
    stack.push(element2);
    Iterator<Namespace> iterator = stack.iterator();
    List<Namespace> expectedScope = new ArrayList<Namespace>();
    expectedScope.add(Namespace.NO_NAMESPACE);
    expectedScope.add(Namespace.XML_NAMESPACE);
    expectedScope.add(element2.getNamespace());
    expectedScope.add(element1.getNamespace());
    int i = 0;
    while (iterator.hasNext()) {
        assertEquals(expectedScope.get(i), iterator.next());
        i++;
    }
}

@Test
public void testGetScope() {
    NamespaceStack stack = new NamespaceStack();
    Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
    assertArrayEquals(expectedScope, stack.getScope());

    Element element1 = new Element("element1", Namespace.getNamespace("test", "http://www.test.com"));
    stack.push(element1);
    Namespace[] expectedScope2 = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element1.getNamespace()};
    assertArrayEquals(expectedScope2, stack.getScope());

    stack.pop();
    assertArrayEquals(expectedScope, stack.getScope());
}

@Test
public void testIsInScope() {
    NamespaceStack stack = new NamespaceStack();
    Element element1 = new Element("element1", Namespace.getNamespace("test", "http://www.test.com"));
    stack.push(element1);
    assertTrue(stack.isInScope(Namespace.getNamespace("test", "http://www.test.com")));
    assertFalse(stack.isInScope(Namespace.getNamespace("test2", "http://www.test2.com")));
}

}