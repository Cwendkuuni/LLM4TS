import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;

public class NamespaceStackTest {
    
    private NamespaceStack nsStack;
    
    @Before
    public void setUp() {
        nsStack = new NamespaceStack();
    }
    
    @Test
    public void testPush_WithElement_NamespaceAdded() {
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        
        nsStack.push(element);
        
        assertTrue(nsStack.isInScope(Namespace.getNamespace("prefix", "uri")));
        assertFalse(nsStack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
    }
    
    @Test
    public void testPush_WithElement_AdditionalNamespacesAdded() {
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix2", "uri2"));
        
        nsStack.push(element);
        
        assertTrue(nsStack.isInScope(Namespace.getNamespace("prefix", "uri")));
        assertTrue(nsStack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
    }
    
    @Test
    public void testPush_WithElement_AttributesAdded() {
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        element.setAttribute("attribute", "value", Namespace.getNamespace("prefix2", "uri2"));
        
        nsStack.push(element);
        
        assertTrue(nsStack.isInScope(Namespace.getNamespace("prefix", "uri")));
        assertTrue(nsStack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
    }
    
    @Test
    public void testPush_WithAttribute_NamespaceAdded() {
        Attribute attribute = new Attribute("attribute", "value", Namespace.getNamespace("prefix", "uri"));
        
        nsStack.push(attribute);
        
        assertTrue(nsStack.isInScope(Namespace.getNamespace("prefix", "uri")));
        assertFalse(nsStack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
    }
    
    @Test
    public void testPop_WithOneElement_ScopeReducedByOne() {
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        nsStack.push(element);
        
        nsStack.pop();
        
        assertFalse(nsStack.isInScope(Namespace.getNamespace("prefix", "uri")));
    }
    
    @Test
    public void testAddedForward_EmptyStack_ReturnEmptyIterable() {
        Iterable<Namespace> iter = nsStack.addedForward();
        
        assertFalse(iter.iterator().hasNext());
    }
    
    @Test
    public void testAddedForward_WithAddedNamespaces_ReturnForwardIterable() {
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        nsStack.push(element);
        
        Iterable<Namespace> iter = nsStack.addedForward();
        
        assertTrue(iter.iterator().hasNext());
    }
    
    @Test
    public void testAddedReverse_EmptyStack_ReturnEmptyIterable() {
        Iterable<Namespace> iter = nsStack.addedReverse();
        
        assertFalse(iter.iterator().hasNext());
    }
    
    @Test
    public void testAddedReverse_WithAddedNamespaces_ReturnReverseIterable() {
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        nsStack.push(element);
        
        Iterable<Namespace> iter = nsStack.addedReverse();
        
        assertTrue(iter.iterator().hasNext());
    }
    
    @Test
    public void testIterator_WithEmptyStack_ReturnEmptyIterator() {
        Iterable<Namespace> iter = nsStack;
        
        assertFalse(iter.iterator().hasNext());
    }
    
    @Test
    public void testIterator_WithAddedNamespaces_ReturnIterator() {
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        nsStack.push(element);
        
        Iterable<Namespace> iter = nsStack;
        
        assertTrue(iter.iterator().hasNext());
    }
    
    @Test
    public void testGetScope_EmptyStack_ReturnEmptyArray() {
        Namespace[] scope = nsStack.getScope();
        
        assertEquals(0, scope.length);
    }
    
    @Test
    public void testGetScope_WithAddedNamespaces_ReturnScopeArray() {
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        nsStack.push(element);
        
        Namespace[] scope = nsStack.getScope();
        
        assertEquals(1, scope.length);
    }
    
    @Test
    public void testIsInScope_WithNamespaceInScope_ReturnTrue() {
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        nsStack.push(element);
        
        boolean inScope = nsStack.isInScope(Namespace.getNamespace("prefix", "uri"));
        
        assertTrue(inScope);
    }
    
    @Test
    public void testIsInScope_WithNamespaceNotInScope_ReturnFalse() {
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        nsStack.push(element);
        
        boolean inScope = nsStack.isInScope(Namespace.getNamespace("prefix2", "uri2"));
        
        assertFalse(inScope);
    }
}