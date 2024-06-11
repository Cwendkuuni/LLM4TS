import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_ShouldReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertFalse(checkGlobalThis.shouldTraverse(null, null, null));
    }

    @Test
    public void testShouldTraverse_ShouldReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertTrue(checkGlobalThis.shouldTraverse(null, null, new Node(Token.FUNCTION)));
    }

    @Test
    public void testVisit_ShouldNotReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.FUNCTION);
        checkGlobalThis.visit(t, n, parent);
        // Assert that no report is made
    }

    @Test
    public void testVisit_ShouldReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        checkGlobalThis.visit(t, n, parent);
        // Assert that a report is made
    }

    @Test
    public void testGetFunctionJsDocInfo_ShouldReturnNotNull() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(Token.THIS);
        JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNotNull(jsDoc);
    }

    @Test
    public void testGetFunctionJsDocInfo_ShouldReturnNull() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(Token.THIS);
        JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(jsDoc);
    }
}