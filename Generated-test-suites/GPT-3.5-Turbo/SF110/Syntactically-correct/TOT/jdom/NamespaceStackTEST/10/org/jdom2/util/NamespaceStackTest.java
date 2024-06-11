
import org.junit.Test;
import org.jdom2.*;

public class NamespaceStackTest {

    // Test case for push(Element element)
    @Test
    public void testPushElement() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();
        
        // Add Namespace in scope
        Namespace namespace1 = Namespace.getNamespace("prefix1", "URI1");
        element.addNamespaceDeclaration(namespace1);
        
        // Add additional Namespaces
        Namespace namespace2 = Namespace.getNamespace("prefix2", "URI2");
        Namespace namespace3 = Namespace.getNamespace("prefix3", "URI3");
        Namespace namespace4 = Namespace.getNamespace("prefix4", "URI4");
        element.addAdditionalNamespace(namespace2);
        element.addAdditionalNamespace(namespace3);
        element.addAdditionalNamespace(namespace4);
        
        namespaceStack.push(element);
        
        // Assert if the namespaces are pushed correctly
        assert(namespaceStack.isInScope(namespace1));
        assert(namespaceStack.isInScope(namespace2));
        assert(namespaceStack.isInScope(namespace3));
        assert(namespaceStack.isInScope(namespace4));
    }

    // Test case for push(Attribute att)
    @Test
    public void testPushAttribute() {
        Attribute attribute = new Attribute("testAttribute", "value");
        NamespaceStack namespaceStack = new NamespaceStack();
        
        // Add Namespace in scope
        Namespace namespace1 = Namespace.getNamespace("prefix1", "URI1");
        attribute.setNamespace(namespace1);
        
        namespaceStack.push(attribute);
        
        // Assert if the attribute namespace is pushed correctly
        assert(namespaceStack.isInScope(namespace1));
    }

    // Test case for pop()
    @Test
    public void testPop() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();
        
        // Push Namespace into stack
        Namespace namespace = Namespace.getNamespace("prefix", "URI");
        element.addNamespaceDeclaration(namespace);
        namespaceStack.push(element);
        
        // Assert if the namespace is in scope
        assert(namespaceStack.isInScope(namespace));
        
        // Call pop()
        namespaceStack.pop();
        
        // Assert that the namespace is no longer in scope
        assert(!namespaceStack.isInScope(namespace));
    }

    // Test case for addedForward()
    @Test
    public void testAddedForward() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();
        
        // Push Namespace into stack
        Namespace namespace1 = Namespace.getNamespace("prefix1", "URI1");
        element.addNamespaceDeclaration(namespace1);
        
        // Push additional Namespaces into stack
        Namespace namespace2 = Namespace.getNamespace("prefix2", "URI2");
        Namespace namespace3 = Namespace.getNamespace("prefix3", "URI3");
        element.addAdditionalNamespace(namespace2);
        element.addAdditionalNamespace(namespace3);
        
        namespaceStack.push(element);
        
        // Get the added namespaces in forward order and assert if they match the expected order
        Iterable<Namespace> addedNamespaces = namespaceStack.addedForward();

        assert(addedNamespaces.iterator().next() == namespace1);
        addedNamespaces.iterator().next();
        assert(addedNamespaces.iterator().next() == namespace2);
        addedNamespaces.iterator().next();
        assert(addedNamespaces.iterator().next() == namespace3);
    }

    // Test case for addedReverse()
    @Test
    public void testAddedReverse() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();
        
        // Push Namespace into stack
        Namespace namespace1 = Namespace.getNamespace("prefix1", "URI1");
        element.addNamespaceDeclaration(namespace1);
        
        // Push additional Namespaces into stack
        Namespace namespace2 = Namespace.getNamespace("prefix2", "URI2");
        Namespace namespace3 = Namespace.getNamespace("prefix3", "URI3");
        element.addAdditionalNamespace(namespace2);
        element.addAdditionalNamespace(namespace3);
        
        namespaceStack.push(element);
        
        // Get the added namespaces in reverse order and assert if they match the expected order
        Iterable<Namespace> addedNamespaces = namespaceStack.addedReverse();

        assert(addedNamespaces.iterator().next() == namespace3);
        addedNamespaces.iterator().next();
        assert(addedNamespaces.iterator().next() == namespace2);
        addedNamespaces.iterator().next();
        assert(addedNamespaces.iterator().next() == namespace1);
    }

    // Test case for getScope()
    @Test
    public void testGetScope() {
        Element element1 = new Element("testElement1");
        Element element2 = new Element("testElement2");
        NamespaceStack namespaceStack = new NamespaceStack();
        
        // Push Namespace into stack
        Namespace namespace1 = Namespace.getNamespace("prefix1", "URI1");
        element1.addNamespaceDeclaration(namespace1);
        
        // Push additional Namespaces into stack
        Namespace namespace2 = Namespace.getNamespace("prefix2", "URI2");
        Namespace namespace3 = Namespace.getNamespace("prefix3", "URI3");
        element1.addAdditionalNamespace(namespace2);
        element1.addAdditionalNamespace(namespace3);
        
        namespaceStack.push(element1);
        namespaceStack.push(element2);
        
        // Get the current scope and assert if it matches the expected scope
        assert(namespaceStack.getScope().length == 3);
        assert(namespaceStack.getScope()[0] instanceof NamespaceStack.EmptyIterable);
        assert(namespaceStack.getScope()[1] == namespace2);
        assert(namespaceStack.getScope()[2] == namespace3);
    }

    // Test case for isInScope(Namespace ns)
    @Test
    public void testIsInScope() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();
        
        // Push Namespace into stack
        Namespace namespace1 = Namespace.getNamespace("prefix1", "URI1");
        element.addNamespaceDeclaration(namespace1);
        
        // Push additional Namespaces into stack
        Namespace namespace2 = Namespace.getNamespace("prefix2", "URI2");
        element.addAdditionalNamespace(namespace2);
        
        namespaceStack.push(element);
        
        // Assert if the pushed namespaces are in scope
        assert(namespaceStack.isInScope(namespace1));
        assert(namespaceStack.isInScope(namespace2));
        
        // Create a new Namespace and assert if it is not in scope
        Namespace namespace3 = Namespace.getNamespace("prefix3", "URI3");
        assert(!namespaceStack.isInScope(namespace3));
    }
}
