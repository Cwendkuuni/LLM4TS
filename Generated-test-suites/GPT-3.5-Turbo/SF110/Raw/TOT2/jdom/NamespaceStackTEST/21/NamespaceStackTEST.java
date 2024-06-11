import org.junit.*;
import static org.junit.Assert.*;

public class NamespaceStackTest {

    @Test
    public void testPushStack() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        // Test functionality of push method by checking if element is added to stack
        // Add more test cases for different scenarios
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        // Test functionality of pop method by checking if stack is emptied
        // Add more test cases for different scenarios including exception handling
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        // Test functionality of addedForward method by checking if it returns correct Iterable
        // Add more test cases for different scenarios
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace ns = new Namespace("prefix", "uri");
        // Test functionality of isInScope method by checking if it correctly identifies if namespace is in scope
        // Add more test cases for different scenarios
    }

    // Add more test cases for other public methods in the NamespaceStack class

}