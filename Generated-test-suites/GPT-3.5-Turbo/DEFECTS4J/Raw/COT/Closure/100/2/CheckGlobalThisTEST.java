import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        // assert result
    }
    
    @Test
    public void testVisit() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        checkGlobalThis.visit(t, n, parent);
        // assert something
    }
}