

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.Arrays;
import java.util.List;

public class NamespaceStackTest {

    private NamespaceStack nsStack;

    @Before
    public void setup() {
        nsStack = new NamespaceStack();
    }
    
    @Test
    public void testPushElement() {
        Element element = new Element("element", Namespace.NO_NAMESPACE);
        nsStack.push(element);
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedScope, nsStack.getScope());
    }
    
    @Test
    public void testPushAttribute() {
        Attribute attribute = new Attribute("attribute", "value", Namespace.XML_NAMESPACE);
        nsStack.push(attribute);
        Namespace[] expectedScope = {Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedScope, nsStack.getScope());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        nsStack.pop();
    }
    
    @Test
    public void testPop() {
        Element element = new Element("element", Namespace.NO_NAMESPACE);
        nsStack.push(element);
        nsStack.pop();
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedScope, nsStack.getScope());
    }
    
    @Test
    public void testAddedForwardEmpty() {
        Iterable<Namespace> iterable = nsStack.addedForward();
        Assert.assertFalse(iterable.iterator().hasNext());
    }
    
    @Test
    public void testAddedForward() {
        Element element = new Element("element", Namespace.NO_NAMESPACE);
        Namespace mns = element.getNamespace();
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        
        nsStack.push(element);
        
        Iterable<Namespace> iterable = nsStack.addedForward();
        
        List<Namespace> expectedList = Arrays.asList(expectedScope);
        int i = 1;
        for (Namespace ns : iterable) {
            Assert.assertEquals(expectedList.get(i), ns);
            i++;
        }
    }
    
    @Test
    public void testAddedReverseEmpty() {
        Iterable<Namespace> iterable = nsStack.addedReverse();
        Assert.assertFalse(iterable.iterator().hasNext());
    }
    
    @Test
    public void testAddedReverse() {
        Element element = new Element("element", Namespace.NO_NAMESPACE);
        Namespace mns = element.getNamespace();
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        
        nsStack.push(element);
        
        Iterable<Namespace> iterable = nsStack.addedReverse();
        
        List<Namespace> expectedList = Arrays.asList(expectedScope);
        int i = expectedList.size() - 1;
        for (Namespace ns : iterable) {
            Assert.assertEquals(expectedList.get(i), ns);
            i--;
        }
    }
    
    @Test
    public void testIteratorEmptyStack() {
        Iterable<Namespace> iterable = nsStack.iterator();
        Assert.assertFalse(iterable.iterator().hasNext());
    }
    
    @Test
    public void testIterator() {
        Element element = new Element("element", Namespace.NO_NAMESPACE);
        nsStack.push(element);
        nsStack.pop();
        
        Iterable<Namespace> iterable = nsStack.iterator();
        Assert.assertTrue(iterable.iterator().hasNext());
        
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        List<Namespace> expectedList = Arrays.asList(expectedScope);
        int i = 0;
        for (Namespace ns : iterable) {
            Assert.assertEquals(expectedList.get(i), ns);
            i++;
        }
    }
    
    @Test
    public void testIsInScopeTrue() {
        Element element = new Element("element", Namespace.NO_NAMESPACE);
        nsStack.push(element);
        Assert.assertTrue(nsStack.isInScope(Namespace.NO_NAMESPACE));
        
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        for (Namespace ns : expectedScope) {
            Assert.assertTrue(nsStack.isInScope(ns));
        }
    }
    
    @Test
    public void testIsInScopeFalse() {
        Element element = new Element("element", Namespace.NO_NAMESPACE);
        nsStack.push(element);
        Assert.assertFalse(nsStack.isInScope(Namespace.XML_NAMESPACE));
    }
    
}
