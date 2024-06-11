import org.junit.Test;

import static org.junit.Assert.*;

public class CheckSideEffectsTest {

    @Test
    public void testVisit() {
        // Typical use case: code with a side effect
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        n.setType(Token.VAR);
        parent.setType(Token.SCRIPT);
        parent.addChildToFront(n);
        t.visit(n, parent);
        // Expect no error output
        assertNull(t.getCompiler().getLastError());

        // Edge case: empty node
        Node emptyNode = new Node();
        Node parent2 = new Node();
        parent2.setType(Token.SCRIPT);
        parent2.addChildToFront(emptyNode);
        t.visit(emptyNode, parent2);
        // Expect no error output
        assertNull(t.getCompiler().getLastError());

        // Error scenario: side-effect free code
        Node sideEffectFreeNode = new Node();
        Node parent3 = new Node();
        parent3.setType(Token.SCRIPT);
        parent3.addChildToFront(sideEffectFreeNode);
        t.visit(sideEffectFreeNode, parent3);
        // Expect an error output
        assertNotNull(t.getCompiler().getLastError());
    }

    @Test
    public void testProtectSideEffects() {
        // Typical use case: code with side effects
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checkSideEffects.addExtern();
        Node sideEffectNode = new Node();
        Node parent = new Node();
        parent.addChildToFront(sideEffectNode);
        checkSideEffects.protectSideEffects();
        // Expect the side effect code to be protected and no error output
        assertEquals(PROTECTOR_FN + "();", parent.getFirstChild().toString());
        assertNull(compiler.getLastError());

        // Edge case: empty problem node list
        NodeTraversal traversal = new NodeTraversal();
        CheckSideEffects checkSideEffects2 = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checkSideEffects2.protectSideEffects();
        // Expect no error output
        assertNull(compiler.getLastError());

        // Error scenario: problem node list with non-side-effect code
        CheckSideEffects checkSideEffects3 = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
        Node nonSideEffectNode = new Node();
        Node parent2 = new Node();
        parent2.addChildToFront(nonSideEffectNode);
        checkSideEffects3.protectSideEffects();
        // Expect an error output and no changes to the code
        assertEquals(nonSideEffectNode.toString(), parent2.getFirstChild().toString());
        assertNotNull(compiler.getLastError());
    }

    @Test
    public void testStripProtection() {
        // Typical use case: protected code
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        Node callNode = new Node();
        Node targetNode = new Node(Token.NAME, new Node("JSCOMPILER_PRESERVE"));
        targetNode.setBooleanProp(Node.IS_CONSTANT_NAME, true);
        callNode.addChildToFront(targetNode);
        stripProtection.visit(null, callNode, null);
        // Expect the protected code to be stripped
        assertEquals("JSCOMPILER_PRESERVE();", callNode.toString());

        // Edge case: empty call node
        Node emptyCallNode = new Node();
        stripProtection.visit(null, emptyCallNode, null);
        // Expect no changes to the code
        assertEquals("", emptyCallNode.toString());

        // Error scenario: call node without protected code
        Node unprotectedCallNode = new Node();
        Node unprotectedTargetNode = new Node(Token.NAME, new Node("UNPROTECTED_CODE"));
        unprotectedTargetNode.setBooleanProp(Node.IS_CONSTANT_NAME, true);
        unprotectedCallNode.addChildToFront(unprotectedTargetNode);
        stripProtection.visit(null, unprotectedCallNode, null);
        // Expect no changes to the code
        assertEquals(unprotectedTargetNode.toString(), unprotectedCallNode.toString());
    }

}