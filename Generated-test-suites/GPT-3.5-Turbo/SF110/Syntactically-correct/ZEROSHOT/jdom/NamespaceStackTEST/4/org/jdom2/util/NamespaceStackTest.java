import org.junit.Test;
import static org.junit.Assert.*;
import org.jdom2.*;
import java.util.*;

public class NamespaceStackTest {

    @Test
    public void testPush_Element() {
        Element element = new Element("testElement");
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(element);
        Namespace[] expectedScope = {
            Namespace.NO_NAMESPACE,
            Namespace.XML_NAMESPACE,
            element.getNamespace()
        };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }

    @Test
    public void testPush_Attribute() {
        Attribute attribute = new Attribute("testAttribute", "testValue");
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(attribute);
        Namespace[] expectedScope = {
            Namespace.NO_NAMESPACE,
            Namespace.XML_NAMESPACE,
            attribute.getNamespace()
        };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }

    @Test
    public void testPop() {
        Element element = new Element("testElement");
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(element);
        nsStack.pop();
        Namespace[] expectedScope = {
            Namespace.NO_NAMESPACE,
            Namespace.XML_NAMESPACE
        };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }

    @Test
    public void testIsInScope_True() {
        Element element = new Element("testElement");
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(element);
        assertTrue(nsStack.isInScope(element.getNamespace()));
    }

    @Test
    public void testIsInScope_False() {
        Element element = new Element("testElement");
        NamespaceStack nsStack = new NamespaceStack();
        assertFalse(nsStack.isInScope(element.getNamespace()));
    }

    @Test
    public void testAddedForward_Empty() {
        NamespaceStack nsStack = new NamespaceStack();
        Iterable<Namespace> addedNamespaces = nsStack.addedForward();
        assertFalse(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testAddedForward_NotEmpty() {
        Element element = new Element("testElement");
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(element);
        Iterable<Namespace> addedNamespaces = nsStack.addedForward();
        Iterator<Namespace> iterator = addedNamespaces.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(element.getNamespace(), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedReverse_Empty() {
        NamespaceStack nsStack = new NamespaceStack();
        Iterable<Namespace> addedNamespaces = nsStack.addedReverse();
        assertFalse(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testAddedReverse_NotEmpty() {
        Element element = new Element("testElement");
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(element);
        Iterable<Namespace> addedNamespaces = nsStack.addedReverse();
        Iterator<Namespace> iterator = addedNamespaces.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(element.getNamespace(), iterator.next());
        assertFalse(iterator.hasNext());
    }
}