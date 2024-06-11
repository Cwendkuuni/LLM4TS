import org.junit.Before;
import org.junit.Test;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class NamespaceStackTest {

    private NamespaceStack stack;
    private Namespace[] seed;

    @Before
    public void setup() {
        seed = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        stack = new NamespaceStack(seed);
    }

    @Test
    public void testPush_Element() {
        Element element = new Element("testElement");
        stack.push(element);
        Namespace[] expectedScope = {element.getNamespace(), seed[1], seed[0]};
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testPush_Element_WithAdditionalNamespaces() {
        Namespace additionalNamespace = Namespace.getNamespace("test", "http://example.com");
        Element element = new Element("testElement");
        element.addNamespaceDeclaration(additionalNamespace);
        stack.push(element);
        Namespace[] expectedScope = {element.getNamespace(), additionalNamespace, seed[1], seed[0]};
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testPush_Element_WithAttributes() {
        Namespace attributeNamespace = Namespace.getNamespace("test", "http://example.com");
        Attribute attribute = new Attribute("test", "value", attributeNamespace);
        Element element = new Element("testElement");
        element.setAttribute(attribute);
        stack.push(element);
        Namespace[] expectedScope = {element.getNamespace(), attributeNamespace, seed[1], seed[0]};
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testPush_Attribute() {
        Namespace attributeNamespace = Namespace.getNamespace("test", "http://example.com");
        Attribute attribute = new Attribute("test", "value", attributeNamespace);
        stack.push(attribute);
        Namespace[] expectedScope = {attribute.getNamespace(), seed[1], seed[0]};
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testPop() {
        stack.pop();
        assertEquals(seed.length, stack.getScope().length);
    }

    @Test(expected = NoSuchElementException.class)
    public void testAddedForward_Empty() {
        Iterator<Namespace> iterator = stack.addedForward().iterator();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void testAddedForward() {
        Namespace additionalNamespace = Namespace.getNamespace("test", "http://example.com");
        Element element = new Element("testElement");
        element.addNamespaceDeclaration(additionalNamespace);
        stack.push(element);
        Iterator<Namespace> iterator = stack.addedForward().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(additionalNamespace, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testAddedReverse_Empty() {
        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void testAddedReverse() {
        Namespace additionalNamespace = Namespace.getNamespace("test", "http://example.com");
        Element element = new Element("testElement");
        element.addNamespaceDeclaration(additionalNamespace);
        stack.push(element);
        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(additionalNamespace, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator() {
        Namespace additionalNamespace = Namespace.getNamespace("test", "http://example.com");
        Element element = new Element("testElement");
        element.addNamespaceDeclaration(additionalNamespace);
        stack.push(element);
        Iterator<Namespace> iterator = stack.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(element.getNamespace(), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(additionalNamespace, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetScope() {
        Namespace[] expectedScope = {seed[1], seed[0]};
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testIsInScope_True() {
        assertTrue(stack.isInScope(seed[1]));
    }

    @Test
    public void testIsInScope_False() {
        assertFalse(stack.isInScope(Namespace.getNamespace("test", "http://example.com")));
    }
}