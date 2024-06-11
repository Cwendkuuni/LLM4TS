import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test case 1
        NodeTraversal t1 = new NodeTraversal();
        Node n1 = new Node(Token.CONST, new Node(Token.NAME, new Node(Token.BLOCK)));
        Node parent1 = new Node(Token.FUNCTION);
        assertEquals(true, checkGlobalThis.shouldTraverse(t1, n1, parent1));

        // Test case 2
        NodeTraversal t2 = new NodeTraversal();
        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.NAME);
        assertEquals(false, checkGlobalThis.shouldTraverse(t2, n2, parent2));

        // Test case 3
        NodeTraversal t3 = new NodeTraversal();
        Node n3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.ASSIGN, new Node(Token.NAME), new Node(Token.NAME));
        assertEquals(true, checkGlobalThis.shouldTraverse(t3, n3, parent3));

        // Test case 4
        NodeTraversal t4 = new NodeTraversal();
        Node n4 = new Node(Token.FUNCTION);
        Node parent4 = new Node(Token.BLOCK, new Node(Token.NAME));
        assertEquals(true, checkGlobalThis.shouldTraverse(t4, n4, parent4));
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test case 1
        NodeTraversal t1 = new NodeTraversal();
        Node n1 = new Node(Token.THIS);
        Node parent1 = new Node(Token.FUNCTION);
        checkGlobalThis.visit(t1, n1, parent1);
        assertEquals(1, compiler.warnings.size());

        // Test case 2
        NodeTraversal t2 = new NodeTraversal();
        Node n2 = new Node(Token.THIS);
        Node parent2 = new Node(Token.GETPROP);
        checkGlobalThis.visit(t2, n2, parent2);
        assertEquals(1, compiler.warnings.size());

        // Test case 3
        NodeTraversal t3 = new NodeTraversal();
        Node n3 = new Node(Token.THIS);
        Node parent3 = new Node(Token.NAME);
        checkGlobalThis.visit(t3, n3, parent3);
        assertEquals(0, compiler.warnings.size());
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        // Test case 1
        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.NAME);
        JSDocInfo result1 = checkGlobalThis.getFunctionJsDocInfo(n1);
        assertNull(result1);

        // Test case 2
        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.ASSIGN);
        JSDocInfo result2 = checkGlobalThis.getFunctionJsDocInfo(n2);
        assertNull(result2);

        // Test case 3
        Node n3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.VAR, new Node(Token.NAME));
        JSDocInfo result3 = checkGlobalThis.getFunctionJsDocInfo(n3);
        assertNull(result3);
    }
}