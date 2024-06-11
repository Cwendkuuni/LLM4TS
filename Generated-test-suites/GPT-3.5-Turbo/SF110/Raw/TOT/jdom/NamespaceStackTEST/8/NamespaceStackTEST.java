import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NamespaceStackTest {

    private NamespaceStack stack;
    
    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }
    
    @Test
    public void testPush_ElementWithAdditionalNamespaces() {
        stack.push(new Element("test", Namespace.getNamespace("prefix1", "uri1")));
        stack.push(new Element("test", Namespace.getNamespace("prefix2", "uri2")));
        stack.push(new Element("test", Namespace.getNamespace("prefix3", "uri3")));
        
        Assert.assertTrue(stack.isInScope(Namespace.getNamespace("prefix1", "uri1")));
        Assert.assertTrue(stack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
        Assert.assertTrue(stack.isInScope(Namespace.getNamespace("prefix3", "uri3")));
        
        Namespace[] scope = stack.getScope();
        Assert.assertNotNull(scope);
        Assert.assertEquals(4, scope.length);
        Assert.assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        Assert.assertEquals(Namespace.XML_NAMESPACE, scope[1]);
        Assert.assertEquals(Namespace.getNamespace("prefix1", "uri1"), scope[2]);
        Assert.assertEquals(Namespace.getNamespace("prefix2", "uri2"), scope[3]);
    }
    
    @Test
    public void testPush_ElementWithAttributes() {
        Element element = new Element("test");
        element.setAttribute(new Attribute("attr1", "value1", Namespace.getNamespace("prefix1", "uri1")));
        element.setAttribute(new Attribute("attr2", "value2", Namespace.getNamespace("prefix2", "uri2")));
        
        stack.push(element);
        
        Assert.assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        Assert.assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        Assert.assertTrue(stack.isInScope(Namespace.getNamespace("prefix1", "uri1")));
        Assert.assertTrue(stack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
        
        Namespace[] scope = stack.getScope();
        Assert.assertNotNull(scope);
        Assert.assertEquals(4, scope.length);
        Assert.assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        Assert.assertEquals(Namespace.XML_NAMESPACE, scope[1]);
        Assert.assertEquals(Namespace.getNamespace("prefix1", "uri1"), scope[2]);
        Assert.assertEquals(Namespace.getNamespace("prefix2", "uri2"), scope[3]);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack() {
        stack.pop();
    }
    
    @Test
    public void testPop() {
        stack.push(new Element("test", Namespace.getNamespace("prefix1", "uri1")));
        stack.push(new Element("test", Namespace.getNamespace("prefix2", "uri2")));
        stack.pop();
        
        Assert.assertTrue(stack.isInScope(Namespace.getNamespace("prefix1", "uri1")));
        Assert.assertFalse(stack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
        
        Namespace[] scope = stack.getScope();
        Assert.assertNotNull(scope);
        Assert.assertEquals(3, scope.length);
        Assert.assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        Assert.assertEquals(Namespace.XML_NAMESPACE, scope[1]);
        Assert.assertEquals(Namespace.getNamespace("prefix1", "uri1"), scope[2]);
    }
    
    @Test
    public void testAddedForward_NoNamespaces() {
        Iterable<Namespace> added = stack.addedForward();
        
        Assert.assertNotNull(added);
        Assert.assertFalse(added.iterator().hasNext());
    }
    
    @Test
    public void testAddedForward() {
        stack.push(new Element("test", Namespace.getNamespace("prefix1", "uri1")));
        stack.push(new Element("test", Namespace.getNamespace("prefix2", "uri2")));
        
        Iterable<Namespace> added = stack.addedForward();
        
        Assert.assertNotNull(added);
        Assert.assertTrue(added.iterator().hasNext());
        Assert.assertEquals(Namespace.getNamespace("prefix1", "uri1"), added.iterator().next());
    }
    
    @Test
    public void testAddedReverse_NoNamespaces() {
        Iterable<Namespace> added = stack.addedReverse();
        
        Assert.assertNotNull(added);
        Assert.assertFalse(added.iterator().hasNext());
    }
    
    @Test
    public void testAddedReverse() {
        stack.push(new Element("test", Namespace.getNamespace("prefix1", "uri1")));
        stack.push(new Element("test", Namespace.getNamespace("prefix2", "uri2")));
        
        Iterable<Namespace> added = stack.addedReverse();
        
        Assert.assertNotNull(added);
        Assert.assertTrue(added.iterator().hasNext());
        Assert.assertEquals(Namespace.getNamespace("prefix2", "uri2"), added.iterator().next());
    }
    
    @Test
    public void testIterator() {
        stack.push(new Element("test", Namespace.getNamespace("prefix1", "uri1")));
        stack.push(new Element("test", Namespace.getNamespace("prefix2", "uri2")));
        
        Iterator<Namespace> iterator = stack.iterator();
        
        Assert.assertNotNull(iterator);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        Assert.assertEquals(Namespace.XML_NAMESPACE, iterator.next());
        Assert.assertEquals(Namespace.getNamespace("prefix1", "uri1"), iterator.next());
        Assert.assertEquals(Namespace.getNamespace("prefix2", "uri2"), iterator.next());
    }
    
    @Test
    public void testGetScope() {
        stack.push(new Element("test", Namespace.getNamespace("prefix1", "uri1")));
        stack.push(new Element("test", Namespace.getNamespace("prefix2", "uri2")));
        
        Namespace[] scope = stack.getScope();
        
        Assert.assertNotNull(scope);
        Assert.assertEquals(4, scope.length);
        Assert.assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        Assert.assertEquals(Namespace.XML_NAMESPACE, scope[1]);
        Assert.assertEquals(Namespace.getNamespace("prefix1", "uri1"), scope[2]);
        Assert.assertEquals(Namespace.getNamespace("prefix2", "uri2"), scope[3]);
    }
    
    @Test
    public void testIsInScope() {
        stack.push(new Element("test", Namespace.getNamespace("prefix1", "uri1")));
        stack.push(new Element("test", Namespace.getNamespace("prefix2", "uri2")));
        
        Assert.assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        Assert.assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        Assert.assertTrue(stack.isInScope(Namespace.getNamespace("prefix1", "uri1")));
        Assert.assertTrue(stack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
        
        Assert.assertFalse(stack.isInScope(Namespace.getNamespace("prefix3", "uri3")));
    }
}