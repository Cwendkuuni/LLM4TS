package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.InliningBehavior;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {
    @Test
    public void testInlineObjectLiterals() {
        AbstractCompiler compiler = new Compiler(); // Assuming Compiler is a valid AbstractCompiler implementation
        Supplier<String> safeNameIdSupplier = () -> "id";

        InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

        // Set up test case
        Node root = Node.newObjectLiteral();
        Node externs = Node.newObjectLiteral();
        Scope scope = new Scope(Node.newScopeDeclarationNode(null), null, null);

        inlineObjectLiterals.process(externs, root);

        InliningBehavior inliningBehavior = inlineObjectLiterals.new InliningBehavior();
        ReferenceCollectingCallback callback =
                new ReferenceCollectingCallback(compiler, inliningBehavior);

        // Test case 1: Object literal with no references
        Node objectLiteral1 = Node.newObjectLiteral();
        Node objectLiteral1Assignment =
                Node.newAssignment(NodeUtil.newVarNode("obj1", null), objectLiteral1);
        scope.setVar("obj1", null);

        Node testNode1 = Node.newExpressionStatement(objectLiteral1Assignment);
        scope.addChildToFront(testNode1);

        callback.process(externs, root);
        assertEquals(0, inliningBehavior.staleVars.size());

        // Test case 2: Object literal with direct references
        Node objectLiteral2 = Node.newObjectLiteral();
        Node key1 = Node.newString("key1");
        Node value1 = Node.newNumberLiteral(1);
        Node key1Assignment = Node.newAssignment(key1, value1);
        Node objectLiteral2Assignment =
                Node.newAssignment(NodeUtil.newVarNode("obj2", null), objectLiteral2);
        scope.setVar("obj2", null);

        Node testNode2 = Node.newExpressionStatement(objectLiteral2Assignment);
        scope.addChildToFront(testNode2);

        Node key1Ref = Node.newGetProperty(NodeUtil.newVarNode("obj2"), key1);
        Node testNode3 = Node.newExpressionStatement(key1Ref);
        scope.addChildToFront(testNode3);

        callback.process(externs, root);
        assertEquals(1, inliningBehavior.staleVars.size());
        assertSame(key1, inliningBehavior.staleVars.iterator().next().node);

        // Test case 3: Object literal with indirect references
        Node objectLiteral3 = Node.newObjectLiteral();
        Node key2 = Node.newString("key2");
        Node value2 = Node.newObjectLiteral();
        Node key2Assignment = Node.newAssignment(key2, value2);
        Node objectLiteral3Assignment =
                Node.newAssignment(NodeUtil.newVarNode("obj3", null), objectLiteral3);
        scope.setVar("obj3", null);

        Node testNode4 = Node.newExpressionStatement(objectLiteral3Assignment);
        scope.addChildToFront(testNode4);

        Node key2Ref = Node.newGetProperty(NodeUtil.newVarNode("obj3"), key2);
        Node testNode5 = Node.newExpressionStatement(key2Ref);
        scope.addChildToFront(testNode5);

        Node innerKey3 = Node.newString("key3");
        Node innerValue3 = Node.newString("value3");
        Node innerKey3Assignment = Node.newAssignment(innerKey3, innerValue3);
        Node innerObjectLiteral = Node.newObjectLiteral();
        innerObjectLiteral.addChild(innerKey3Assignment);
        innerObjectLiteral.addChild(innerValue3);
        Node key2ValueAssignment = Node.newAssignment(value2, innerObjectLiteral);

        Node testNode6 = Node.newExpressionStatement(key2ValueAssignment);
        scope.addChildToFront(testNode6);

        Node innerKey3Ref = Node.newGetProperty(NodeUtil.newVarNode("obj3"), key2);
        Node innerKey3InnerRef = Node.newGetProperty(innerKey3Ref, innerKey3);
        Node testNode7 = Node.newExpressionStatement(innerKey3InnerRef);
        scope.addChildToFront(testNode7);

        callback.process(externs, root);
        assertEquals(1, inliningBehavior.staleVars.size());
        assertSame(key2, inliningBehavior.staleVars.iterator().next().node);

        // Test case 4: Object literal with self-referential assignments
        Node objectLiteral4 = Node.newObjectLiteral();
        Node key4 = Node.newString("key4");
        Node value4 = Node.newObjectLiteral();
        Node key4Assignment = Node.newAssignment(key4, value4);
        Node objectLiteral4Assignment =
                Node.newAssignment(NodeUtil.newVarNode("obj4", null), objectLiteral4);
        scope.setVar("obj4", null);

        Node testNode8 = Node.newExpressionStatement(objectLiteral4Assignment);
        scope.addChildToFront(testNode8);

        Node key4Ref = Node.newGetProperty(NodeUtil.newVarNode("obj4"), key4);
        Node testNode9 = Node.newExpressionStatement(key4Ref);
        scope.addChildToFront(testNode9);

        Node innerKey5 = Node.newString("key5");
        Node innerValue5 = Node.newObjectLiteral();
        Node innerKey5Assignment = Node.newAssignment(innerKey5, innerValue5);
        Node innerObjectLiteralInner = Node.newObjectLiteral();
        innerObjectLiteralInner.addChild(innerKey5Assignment);
        innerObjectLiteralInner.addChild(Node.newString("innerValue5"));
        Node key4ValueAssignment = Node.newAssignment(value4, innerObjectLiteralInner);

        Node testNode10 = Node.newExpressionStatement(key4ValueAssignment);
        scope.addChildToFront(testNode10);

        Node innerKey5Ref = Node.newGetProperty(NodeUtil.newVarNode("obj4"), key4);
        Node innerKey5InnerRef = Node.newGetProperty(innerKey5Ref, innerKey5);
        Node testNode11 = Node.newExpressionStatement(innerKey5InnerRef);
        scope.addChildToFront(testNode11);

        callback.process(externs, root);
        assertEquals(1, inliningBehavior.staleVars.size());
        assertSame(key4, inliningBehavior.staleVars.iterator().next().node);

        // Test case 5: Object literal with ES5 getters/setters
        Node objectLiteral5 = Node.newObjectLiteral();
        Node getterGetter = Node.newGetProperty(objectLiteral5, "getter");
        Node getterSetter = Node.newSetProperty(objectLiteral5, "setter", Node.newNull());

        Node testNode12 = Node.newExpressionStatement(getterGetter);
        scope.addChildToFront(testNode12);

        Node getterValue = Node.newObjectLiteral();
        Node getterValueGetter = Node.newGetProperty(getterValue, "value");
        Node getterValueSetter = Node.newSetProperty(getterValue, "value", Node.newNumberLiteral(5));

        Node getterSetterAssignment = Node.newAssignment(getterSetter, getterValue);
        Node testNode13 = Node.newExpressionStatement(getterSetterAssignment);
        scope.addChildToFront(testNode13);

        Node testNode14 = Node.newExpressionStatement(getterGetter);
        scope.addChildToFront(testNode14);

        callback.process(externs, root);
        assertEquals(0, inliningBehavior.staleVars.size());
    }
}