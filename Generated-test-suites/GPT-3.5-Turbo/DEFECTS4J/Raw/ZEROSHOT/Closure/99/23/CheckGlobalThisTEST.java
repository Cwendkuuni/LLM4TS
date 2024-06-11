import org.junit.*;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        // Test shouldTraverse method with function that is a constructor
        NodeTraversal t1 = new NodeTraversal();
        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.FUNCTION);
        CheckGlobalThis checkThis1 = new CheckGlobalThis(null, null);
        boolean result1 = checkThis1.shouldTraverse(t1, n1, parent1);
        assertFalse(result1);

        // Test shouldTraverse method with function annotated with @this
        NodeTraversal t2 = new NodeTraversal();
        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.SCRIPT);
        JSDocInfo jsDoc2 = new JSDocInfo();
        jsDoc2.recordThisType();
        n2.setJSDocInfo(jsDoc2);
        CheckGlobalThis checkThis2 = new CheckGlobalThis(null, null);
        boolean result2 = checkThis2.shouldTraverse(t2, n2, parent2);
        assertFalse(result2);

        // Test shouldTraverse method with function annotated with @override
        NodeTraversal t3 = new NodeTraversal();
        Node n3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.SCRIPT);
        JSDocInfo jsDoc3 = new JSDocInfo();
        jsDoc3.recordOverride();
        n3.setJSDocInfo(jsDoc3);
        CheckGlobalThis checkThis3 = new CheckGlobalThis(null, null);
        boolean result3 = checkThis3.shouldTraverse(t3, n3, parent3);
        assertFalse(result3);

        // Test shouldTraverse method with function with non-traversable parent type
        NodeTraversal t4 = new NodeTraversal();
        Node n4 = new Node(Token.FUNCTION);
        Node parent4 = new Node(Token.STRING);
        CheckGlobalThis checkThis4 = new CheckGlobalThis(null, null);
        boolean result4 = checkThis4.shouldTraverse(t4, n4, parent4);
        assertFalse(result4);

        // Test shouldTraverse method with function with traversable parent type
        NodeTraversal t5 = new NodeTraversal();
        Node n5 = new Node(Token.FUNCTION);
        Node parent5 = new Node(Token.BLOCK);
        CheckGlobalThis checkThis5 = new CheckGlobalThis(null, null);
        boolean result5 = checkThis5.shouldTraverse(t5, n5, parent5);
        assertTrue(result5);
    }

    @Test
    public void testShouldReportThis() {
        // Test shouldReportThis method with assignLhsChild not null
        Node n1 = new Node(Token.THIS);
        Node parent1 = new Node(Token.ADD);
        CheckGlobalThis checkThis1 = new CheckGlobalThis(null, null);
        checkThis1.assignLhsChild = new Node();
        boolean result1 = checkThis1.shouldReportThis(n1, parent1);
        assertTrue(result1);

        // Test shouldReportThis method with parent type not a property access
        Node n2 = new Node(Token.THIS);
        Node parent2 = new Node(Token.ADD);
        CheckGlobalThis checkThis2 = new CheckGlobalThis(null, null);
        checkThis2.assignLhsChild = null;
        boolean result2 = checkThis2.shouldReportThis(n2, parent2);
        assertFalse(result2);

        // Test shouldReportThis method with parent type a property access
        Node n3 = new Node(Token.THIS);
        Node parent3 = new Node(Token.GETELEM);
        CheckGlobalThis checkThis3 = new CheckGlobalThis(null, null);
        checkThis3.assignLhsChild = null;
        boolean result3 = checkThis3.shouldReportThis(n3, parent3);
        assertTrue(result3);
    }
}