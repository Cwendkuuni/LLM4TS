package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {
    private static final String PROTECTOR_FN = "JSCOMPILER_PRESERVE";

    @Test
    public void testCheckSideEffects() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

        // Test empty root
        Node root = new Node(Token.BLOCK);
        NodeTraversal.traverse(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());

        // Test void statement
        Node voidStatement = new Node(Token.VOID);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(voidStatement);
        NodeTraversal.traverse(null, parent, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());

        // Test simple statement
        Node simpleStatement = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 42));
        parent.addChildToBack(simpleStatement);
        NodeTraversal.traverse(null, parent, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());

        // Test expression statement
        Node expressionStatement = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
        parent.addChildToBack(expressionStatement);
        NodeTraversal.traverse(null, parent, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with side effect
        Node sideEffect = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
        Node sideEffectParent = new Node(Token.BLOCK);
        sideEffectParent.addChildToBack(sideEffect);
        NodeTraversal.traverse(null, sideEffectParent, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());

        // Test statement with side effect protected by JSCOMPILER_PRESERVE
        Node protectedSideEffect = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, PROTECTOR_FN));
        Node protectedSideEffectParent = new Node(Token.BLOCK);
        protectedSideEffectParent.addChildToBack(protectedSideEffect);
        NodeTraversal.traverse(null, protectedSideEffectParent, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with multiple side effects
        Node multipleSideEffects = new Node(Token.BLOCK);
        multipleSideEffects.addChildToBack(new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 42)));
        multipleSideEffects.addChildToBack(new Node(Token.ASSIGN, new Node(Token.NAME, "y"), new Node(Token.NUMBER, 43)));
        NodeTraversal.traverse(null, multipleSideEffects, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());

        // Test statement with side effect and JSDoc info
        Node jsdocInfoStatement = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
        jsdocInfoStatement.setJSDocInfo(new JSDocInfoBuilder(false)
                .recordParam("p", "Description of p")
                .recordReturn("Description of return value")
                .build(jsdocInfoStatement));
        Node jsdocInfoParent = new Node(Token.BLOCK);
        jsdocInfoParent.addChildToBack(jsdocInfoStatement);
        NodeTraversal.traverse(null, jsdocInfoParent, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with side effect and JSDoc info and side effect protected by JSCOMPILER_PRESERVE
        Node protectedJsdocInfoStatement = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, PROTECTOR_FN));
        protectedJsdocInfoStatement.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        Node protectedJsdocInfoParent = new Node(Token.BLOCK);
        protectedJsdocInfoParent.addChildToBack(protectedJsdocInfoStatement);
        NodeTraversal.traverse(null, protectedJsdocInfoParent, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with side effect and side effect free statement
        Node sideEffectFreeStatement = new Node(Token.ASSIGN, new Node(Token.NAME, "z"), new Node(Token.NUMBER, 45));
        Node sideEffectFreeParent = new Node(Token.BLOCK);
        sideEffectFreeParent.addChildToBack(sideEffectFreeStatement);
        Node sideEffectStatement = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
        Node sideEffectParent = new Node(Token.BLOCK);
        sideEffectParent.addChildToBack(sideEffectStatement);
        sideEffectParent.addChildToBack(sideEffectFreeParent);
        NodeTraversal.traverse(null, sideEffectParent, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());

        // Test statement with side effect and side effect free statement protected by JSCOMPILER_PRESERVE
        Node protectedSideEffectFreeStatement = new Node(Token.ASSIGN, new Node(Token.NAME, "z"), new Node(Token.NUMBER, 45));
        protectedSideEffectFreeStatement.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        Node protectedSideEffectParent = new Node(Token.BLOCK);
        protectedSideEffectParent.addChildToBack(protectedSideEffect);
        Node protectedSideEffectFreeParent = new Node(Token.BLOCK);
        protectedSideEffectFreeParent.addChildToBack(protectedSideEffectFreeStatement);
        Node protectedSideEffectStatement = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, PROTECTOR_FN));
        protectedSideEffectStatement.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        Node protectedSideEffectParentStatement = new Node(Token.BLOCK);
        protectedSideEffectParentStatement.addChildToBack(protectedSideEffectStatement);
        protectedSideEffectParentStatement.addChildToBack(protectedSideEffectParent);
        protectedSideEffectParentStatement.addChildToBack(protectedSideEffectFreeParent);
        NodeTraversal.traverse(null, protectedSideEffectParentStatement, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with side effect and side effect free statement with side effect protected by JSCOMPILER_PRESERVE
        Node sideEffectProtectedStatement = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, PROTECTOR_FN));
        sideEffectProtectedStatement.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        Node sideEffectFreeStatement = new Node(Token.ASSIGN, new Node(Token.NAME, "z"), new Node(Token.NUMBER, 45));
        sideEffectFreeStatement.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        Node sideEffectParent = new Node(Token.BLOCK);
        sideEffectParent.addChildToBack(sideEffectProtectedStatement);
        sideEffectParent.addChildToBack(sideEffectFreeStatement);
        NodeTraversal.traverse(null, sideEffectParent, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with side effect and side effect free statement with side effect not protected by JSCOMPILER_PRESERVE
        Node unprotectedSideEffectStatement = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "foo"));
        Node unprotectedSideEffectParent = new Node(Token.BLOCK);
        unprotectedSideEffectParent.addChildToBack(unprotectedSideEffectStatement);
        NodeTraversal.traverse(null, unprotectedSideEffectParent, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());
    }

    @Test
    public void testStripProtection() {
        Node root = new Node(Token.BLOCK);
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
        NodeTraversal.traverse(null, root, checkSideEffects);
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(null);
        NodeTraversal.traverse(null, root, stripProtection);
        assertNull(checkSideEffects.problemNodes.get(0));
    }
}