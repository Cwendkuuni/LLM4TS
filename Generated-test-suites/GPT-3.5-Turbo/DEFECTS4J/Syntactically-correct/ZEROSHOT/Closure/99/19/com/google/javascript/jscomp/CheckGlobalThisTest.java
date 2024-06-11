import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        checkGlobalThis.visit(t, n, parent);

        // Assert that no exception is thrown
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        Node n = new Node();
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertNull(result);
    }

}