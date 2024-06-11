import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenameLabelsTest {

    private RenameLabels renameLabels;

    @Before
    public void setUp() {
        renameLabels = new RenameLabels(new AbstractCompiler());
    }

    @Test
    public void testRenameLabels() {
        // Test renaming labels in a simple function
        String sourceCode = "function foo() { a: { b: { foo(); } } }";
        String expectedCode = "function foo() { b: { c: { foo(); } } }";
        assertEquals(expectedCode, renameLabels.renameLabels(sourceCode));
    }

    @Test
    public void testRenameLabels_WithNestedLabels() {
        // Test renaming labels in a function with nested labels
        String sourceCode = "function bar() { a: { b: { foo(); } } a: { b: break a; } }";
        String expectedCode = "function bar() { b: { c: { foo(); } } b: { c: break b; } }";
        assertEquals(expectedCode, renameLabels.renameLabels(sourceCode));
    }

    @Test
    public void testRenameLabels_WithEmptyLabels() {
        // Test renaming labels in a function with empty labels
        String sourceCode = "function baz() { a: { } }";
        String expectedCode = "function baz() { }";
        assertEquals(expectedCode, renameLabels.renameLabels(sourceCode));
    }
}

