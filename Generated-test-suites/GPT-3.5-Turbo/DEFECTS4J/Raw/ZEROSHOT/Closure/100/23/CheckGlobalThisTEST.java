import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(null, null, null);
        assertFalse(result);
    }

    @Test
    public void testVisit() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        checkGlobalThis.visit(t, n, parent);
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = null;
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(result);
    }
}