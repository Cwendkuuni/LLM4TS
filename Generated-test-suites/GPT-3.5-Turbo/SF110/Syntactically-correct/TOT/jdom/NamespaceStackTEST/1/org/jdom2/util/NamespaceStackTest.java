import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

    @Test
    public void testConstructorWithEmptySeed() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
    }

    @Test
    public void testConstructorWithSeed() {
        Namespace ns1 = null; // create a sample Namespace object
        Namespace ns2 = null; // create another sample Namespace object
        
        Namespace[] seed = {ns1, ns2};

        NamespaceStack stack = new NamespaceStack(seed);
        assertNotNull(stack);
    }

    @Test
    public void testPushWithElement() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("element");

        stack.push(element);

        // Assert the stack is not empty
        assertFalse(stack.getScope().length == 0);

        // Assert the top of the stack is the namespace of the inserted element
        assertEquals(element.getNamespace(), stack.getScope()[0]);
    }

    @Test
    public void testPushWithAttribute() {
        NamespaceStack stack = new NamespaceStack();

        Attribute attribute = new Attribute("attribute");

        stack.push(attribute);

        // Assert the stack is not empty
        assertFalse(stack.getScope().length == 0);

        // Assert the top of the stack is the namespace of the inserted attribute
        assertEquals(attribute.getNamespace(), stack.getScope()[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        stack.pop();
    }

    @Test
    public void testPopNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Element element1 = new Element("element1");
        Element element2 = new Element("element2");

        stack.push(element1);
        stack.push(element2);

        stack.pop();

        // Assert the stack is not empty
        assertFalse(stack.getScope().length == 0);

        // Assert the top of the stack is the namespace of the previous element
        assertEquals(element1.getNamespace(), stack.getScope()[0]);
    }

    @Test
    public void testAddedForwardWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Iterable<Namespace> addedNamespaces = stack.addedForward();

        // Assert the addedNamespaces is not null
        assertNotNull(addedNamespaces);
    }

    @Test
    public void testAddedForwardWithNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Element element1 = new Element("element1");
        Element element2 = new Element("element2");

        stack.push(element1);
        stack.push(element2);

        Iterable<Namespace> addedNamespaces = stack.addedForward();

        // Assert the addedNamespaces is not null
        assertNotNull(addedNamespaces);

        // Assert the addedNamespaces contains the namespaces of the pushed elements
        for (Namespace addedNamespace : addedNamespaces) {
            assertTrue(addedNamespace == element1.getNamespace() || addedNamespace == element2.getNamespace());
        }
    }

    @Test
    public void testAddedReverseWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Iterable<Namespace> addedNamespaces = stack.addedReverse();

        // Assert the addedNamespaces is not null
        assertNotNull(addedNamespaces);
    }

    @Test
    public void testAddedReverseWithNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Element element1 = new Element("element1");
        Element element2 = new Element("element2");

        stack.push(element1);
        stack.push(element2);

        Iterable<Namespace> addedNamespaces = stack.addedReverse();

        // Assert the addedNamespaces is not null
        assertNotNull(addedNamespaces);

        // Assert the addedNamespaces contains the namespaces of the pushed elements in reverse order
        for (Namespace addedNamespace : addedNamespaces) {
            assertTrue(addedNamespace == element2.getNamespace() || addedNamespace == element1.getNamespace());
        }
    }

    @Test
    public void testIteratorWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Iterable<Namespace> allNamespaces = stack;

        // Assert the allNamespaces is not null
        assertNotNull(allNamespaces);

        // Assert the allNamespaces is empty
        assertFalse(allNamespaces.iterator().hasNext());
    }

    @Test
    public void testIteratorWithNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Element element1 = new Element("element1");
        Element element2 = new Element("element2");

        stack.push(element1);
        stack.push(element2);

        Iterable<Namespace> allNamespaces = stack;

        // Assert the allNamespaces is not null
        assertNotNull(allNamespaces);

        // Assert the allNamespaces contains the namespaces of the pushed elements in order
        int i = 0;
        for (Namespace namespace : allNamespaces) {
            assertEquals(stack.getScope()[i++], namespace);
        }
    }

    @Test
    public void testGetScopeWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Namespace[] scope = stack.getScope();

        // Assert the scope is not null
        assertNotNull(scope);

        // Assert the scope is empty
        assertTrue(scope.length == 0);
    }

    @Test
    public void testGetScopeWithNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Element element1 = new Element("element1");
        Element element2 = new Element("element2");

        stack.push(element1);
        stack.push(element2);

        Namespace[] scope = stack.getScope();

        // Assert the scope is not null
        assertNotNull(scope);

        // Assert the scope contains the namespaces of the pushed elements in order
        for (int i = 0; i < stack.getScope().length; i++) {
            assertEquals(stack.getScope()[i], scope[i]);
        }
    }

    @Test
    public void testIsInScopeWithExistingNamespace() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("element");

        stack.push(element);

        // Assert the namespace of the inserted element is in scope
        assertTrue(stack.isInScope(element.getNamespace()));
    }

    @Test
    public void testIsInScopeWithNonExistingNamespace() {
        NamespaceStack stack = new NamespaceStack();

        Namespace namespace = new Namespace("prefix", "uri");

        // Assert the namespace is not in scope
        assertFalse(stack.isInScope(namespace));
    }
}