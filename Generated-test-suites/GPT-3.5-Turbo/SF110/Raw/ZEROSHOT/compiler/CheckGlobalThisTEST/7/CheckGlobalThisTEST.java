import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);

        assertFalse(checkGlobalThis.shouldTraverse(null, null, null));
    }

    @Test
    public void testVisit() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;

        checkGlobalThis.visit(t, n, parent);
    }

    @Test
    public void testShouldReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = null;

        assertTrue(checkGlobalThis.shouldReportThis(n));
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = null;

        assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
    }
}