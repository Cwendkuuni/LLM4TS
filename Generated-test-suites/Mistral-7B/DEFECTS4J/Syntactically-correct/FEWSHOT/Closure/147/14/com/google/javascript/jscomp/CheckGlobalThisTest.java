package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_FunctionAnnotatedWithThis() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node functionNode = Node.newFunction("myFunction");
        JSDocInfo jsDocInfo = Node.newJsDocInfo("@this", "MyType");
        when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

        NodeTraversal traversal = NodeTraversal.create(checkGlobalThis, null, null);
        Node parent = Node.newName("window");

        boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);
        assertThat(shouldTraverse).isFalse();
    }

    @Test
    public void testShouldTraverse_FunctionAnnotatedWithConstructor() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node functionNode = Node.newFunction("myConstructor", Node.newArray(), Node.newBlock(null));
        JSDocInfo jsDocInfo = Node.newJsDocInfo("@constructor", "MyType");
        when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

        NodeTraversal traversal = NodeTraversal.create(checkGlobalThis, null, null);
        Node parent = Node.newName("window");

        boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);
        assertThat(shouldTraverse).isFalse();
    }

    @Test
    public void testShouldTraverse_FunctionAnnotatedWithInterface() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node functionNode = Node.newFunction("myInterface", Node.newArray(), Node.newBlock(null));
        JSDocInfo jsDocInfo = Node.newJsDocInfo("@interface", "MyType");
        when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

        NodeTraversal traversal = NodeTraversal.create(checkGlobalThis, null, null);
        Node parent = Node.newName("window");

        boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);
        assertThat(shouldTraverse).isFalse();
    }

    @Test
    public void testShouldTraverse_FunctionAnnotatedWithOverride() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node functionNode = Node.newFunction("myFunction", Node.newArray(), Node.newBlock(null));
        JSDocInfo jsDocInfo = Node.newJsDocInfo("@override", "MyType");
        when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

        NodeTraversal traversal = NodeTraversal.create(checkGlobalThis, null, null);
        Node parent = Node.newName("window");

        boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);
        assertThat(shouldTraverse).isFalse();
    }

    @Test
    public void testShouldTraverse_FunctionWithoutJSDoc() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node functionNode = Node.newFunction("myFunction");

        NodeTraversal traversal = NodeTraversal.create(checkGlobalThis, null, null);
        Node parent = Node.newName("window");

        boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);
        assertThat(shouldTraverse).isTrue();
    }

    @Test
    public void testShouldTraverse_FunctionWithAssignParent() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node assignParentNode = Node.newAssign(Node.newName("a"), Node.newThis());

        Node functionNode = Node.newFunction("myFunction");

        NodeTraversal traversal = NodeTraversal.create(checkGlobalThis, null, null);
        Node parent = assignParentNode;

        boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);
        assertThat(shouldTraverse).isTrue();
    }

    @Test
    public void testShouldTraverse_FunctionWithGetParent() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node getParentNode = Node.newGetProp(Node.newThis(), "prototype");

        Node functionNode = Node.newFunction("myFunction");

        NodeTraversal traversal = NodeTraversal.create(checkGlobalThis, null, null);
        Node parent = getParentNode;

        boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);
        assertThat(shouldTraverse).isFalse();
    }

    @Test
    public void testShouldTraverse_FunctionWithGetParentAndGetChild() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node getParentNode = Node.newGetProp(Node.newThis(), "prototype");
        Node getChildNode = Node.newGetProp(Node.newName("prototype"), "childProperty");

        Node functionNode = Node.newFunction("myFunction");

        NodeTraversal traversal = NodeTraversal.create(checkGlobalThis, null, null);
        Node parent = getParentNode;

        boolean shouldTraverse = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);
        assertThat(shouldTraverse).isFalse();
    }

    @Test
    public void testShouldReportThis() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node thisNode = Node.newThis();

        NodeTraversal traversal = NodeTraversal.create(checkGlobalThis, null, null);
        Node parent = Node.newName("window");

        boolean shouldReportThis = checkGlobalThis.shouldReportThis(traversal, thisNode, parent);
        assertThat(shouldReportThis).isTrue();
    }

    @Test
    public void testShouldReportThis_PropertyAccess() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node parentNode = Node.newName("window");
        Node getPropertyNode = Node.newGetProp(Node.newThis(), "propertyName");

        NodeTraversal traversal = NodeTraversal.create(checkGlobalThis, null, null);
        Node parent = parentNode;

        boolean shouldReportThis = checkGlobalThis.shouldReportThis(traversal, getPropertyNode, parent);
        assertThat(shouldReportThis).isTrue();
    }

    @Test
    public void testShouldNotReportThis_PrototypeAccess() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node parentNode = Node.newName("window");
        Node getPrototypeNode = Node.newGetProp(Node.newThis(), "prototype");

        NodeTraversal traversal = NodeTraversal.create(checkGlobalThis, null, null);
        Node parent = parentNode;

        boolean shouldReportThis = checkGlobalThis.shouldReportThis(traversal, getPrototypeNode, parent);
        assertThat(shouldReportThis).isFalse();
    }
}