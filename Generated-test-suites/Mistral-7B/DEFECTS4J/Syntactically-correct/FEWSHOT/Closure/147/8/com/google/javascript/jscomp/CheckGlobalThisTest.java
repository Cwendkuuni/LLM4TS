package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
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
    private AbstractCompiler compiler;
    private CheckGlobalThis checker;
    private NodeTraversal traversal;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        traversal = new NodeTraversal(null, checker, null, null);
    }

    @Test
    public void testShouldTraverse_function() {
        Node functionNode = Node.newFunction("foo");
        Node functionParent = Node.newBlock();
        functionParent.addChild(functionNode);
        Node functionGrandparent = Node.newBlock();
        functionGrandparent.addChild(functionParent);

        assertThat(checker.shouldTraverse(null, functionNode, functionGrandparent)).isTrue();
    }

    @Test
    public void testShouldTraverse_functionWithAnnotation() {
        Node functionNode = Node.newFunction("foo", null, null, null, null, null, null, null);
        Node functionParent = Node.newBlock();
        Node functionAnnotation = Node.newToken(Token.ANNOTATION, "@constructor");
        functionParent.addChild(functionNode);
        functionParent.addChild(functionAnnotation);

        assertThat(checker.shouldTraverse(null, functionNode, functionParent)).isFalse();
    }

    @Test
    public void testShouldTraverse_functionWithThisAnnotation() {
        Node functionNode = Node.newFunction("foo", null, null, null, null, null, null, null);
        Node functionParent = Node.newBlock();
        Node functionAnnotation = Node.newToken(Token.ANNOTATION, "@this");
        functionParent.addChild(functionNode);
        functionParent.addChild(functionAnnotation);

        assertThat(checker.shouldTraverse(null, functionNode, functionParent)).isFalse();
    }

    @Test
    public void testShouldTraverse_functionWithNoAnnotation() {
        Node functionNode = Node.newFunction("foo", null, null, null, null, null, null, null);
        Node functionParent = Node.newBlock();
        functionParent.addChild(functionNode);

        assertThat(checker.shouldTraverse(null, functionNode, functionParent)).isTrue();
    }

    @Test
    public void testShouldTraverse_functionWithPrototypePropertyAccess() {
        Node functionNode = Node.newFunction("foo");
        Node functionParent = Node.newBlock();
        Node prototypePropertyAccess = Node.newGetProp(Node.newIdent("prototype"));
        Node prototypePropertyNode = Node.newBlock();
        prototypePropertyNode.addChild(prototypePropertyAccess);
        Node prototypePropertyParent = Node.newAssign(prototypePropertyAccess, Node.newString("bar"));
        functionParent.addChild(functionNode);
        functionParent.addChild(prototypePropertyParent);

        assertThat(checker.shouldTraverse(null, functionNode, functionParent)).isFalse();
    }

    @Test
    public void testShouldTraverse_functionWithPrototypePropertyAccessDeep() {
        Node functionNode = Node.newFunction("foo");
        Node functionParent = Node.newBlock();
        Node prototypePropertyAccess = Node.newGetProp(Node.newGetProp(Node.newIdent("prototype"), "prototype"));
        Node prototypePropertyNode = Node.newBlock();
        prototypePropertyNode.addChild(prototypePropertyAccess);
        Node prototypePropertyParent = Node.newAssign(prototypePropertyAccess, Node.newString("bar"));
        functionParent.addChild(functionNode);
        functionParent.addChild(prototypePropertyParent);

        assertThat(checker.shouldTraverse(null, functionNode, functionParent)).isFalse();
    }

    @Test
    public void testShouldTraverse_functionWithNoParent() {
        Node functionNode = Node.newFunction("foo");

        assertThat(checker.shouldTraverse(null, functionNode, null)).isFalse();
    }

    @Test
    public void testShouldTraverse_functionWithParentAssign() {
        Node functionNode = Node.newFunction("foo");
        Node parentAssign = Node.newAssign(Node.newIdent("a"), Node.newString("foo"));
        Node functionParent = Node.newBlock();
        functionParent.addChild(parentAssign);
        functionParent.addChild(functionNode);

        assertThat(checker.shouldTraverse(null, functionNode, functionParent)).isTrue();
    }

    @Test
    public void testShouldTraverse_functionWithParentGet() {
        Node functionNode = Node.newFunction("foo");
        Node parentGet = Node.newGetProp(Node.newIdent("a"));
        Node functionParent = Node.newBlock();
        functionParent.addChild(parentGet);
        functionParent.addChild(functionNode);

        assertThat(checker.shouldTraverse(null, functionNode, functionParent)).isTrue();
    }

    @Test
    public void testShouldTraverse_functionWithParentAssignDeep() {
        Node functionNode = Node.newFunction("foo");
        Node parentAssign = Node.newAssign(Node.newIdent("a"), Node.newGetProp(Node.newIdent("prototype"), "x"));
        Node functionParent = Node.newBlock();
        functionParent.addChild(parentAssign);
        functionParent.addChild(functionNode);

        assertThat(checker.shouldTraverse(null, functionNode, functionParent)).isTrue();
    }

    @Test
    public void testShouldTraverse_functionWithParentGetDeep() {
        Node functionNode = Node.newFunction("foo");
        Node parentGet = Node.newGetProp(Node.newIdent("a"), "x");
        Node functionParent = Node.newBlock();
        functionParent.addChild(parentGet);
        functionParent.addChild(functionNode);

        assertThat(checker.shouldTraverse(null, functionNode, functionParent)).isTrue();
    }

    @Test
    public void testVisit_functionWithGlobalThis() {
        Node functionNode = Node.newFunction("foo");
        Node parentBlock = Node.newBlock();
        Node parentAssign = Node.newAssign(Node.newIdent("this"), functionNode);
        parentBlock.addChild(parentAssign);
        parentBlock.addChild(functionNode);

        when(compiler.report(any(NodeTraversal.class), eq(CheckLevel.WARNING), eq(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
                .thenReturn(new Object());

        checker.visit(traversal, null, parentBlock);

        traversal.traverse(functionNode);

        verify(compiler).report(eq(traversal), eq(CheckLevel.WARNING), eq(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")));
    }

    @Test
    public void testVisit_functionWithGlobalThisPropertyAccess() {
        Node functionNode = Node.newFunction("foo");
        Node parentBlock = Node.newBlock();
        Node parentGet = Node.newGetProp(Node.newIdent("this"), "x");
        parentBlock.addChild(parentGet);
        parentBlock.addChild(functionNode);

        when(compiler.report(any(NodeTraversal.class), eq(CheckLevel.WARNING), eq(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
                .thenReturn(new Object());

        checker.visit(traversal, null, parentBlock);

        traversal.traverse(functionNode);

        verify(compiler).report(eq(traversal), eq(CheckLevel.WARNING), eq(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")));
    }

    @Test
    public void testVisit_functionWithPrototypeThis() {
        Node functionNode = Node.newFunction("foo");
        Node constructorNode = Node.newFunction("constructor", null, null, null, null, null, null, null);
        Node prototype = Node.newObject("Object");
        Node constructorPrototype = Node.newObject("Object");
        Node constructorPrototypeProperty = Node.newProperty(constructorPrototype, "prototype");
        Node constructorPrototypePropertyValue = Node.newFunction("constructor", null, null, null, null, null, null, null);
        Node constructorPrototypeValue = Node.newProperty(prototype, "constructor");
        Node constructorPrototypeValueSetter = Node.newAssign(constructorPrototypeValue, constructorPrototypePropertyValue);
        Node constructorProperty = Node.newProperty(constructorPrototype, "constructor");
        Node constructorPropertyValue = Node.newFunction("constructor", null, null, null, null, null, null, null);
        Node constructorPropertySetter = Node.newAssign(constructorProperty, constructorPropertyValue);
        Node prototypeProperty = Node.newProperty(prototype, "prototype");
        Node prototypePropertyValue = Node.newObject("Object");
        Node prototypePropertyValueSetter = Node.newAssign(prototypePropertyValue, constructorPrototype);
        Node prototypeValueSetter = Node.newAssign(prototype, prototypePropertyValue);
        Node constructorParent = Node.newBlock();
        constructorParent.addChild(constructorPrototypePropertySetter);
        constructorParent.addChild(constructorPrototypeValueSetter);
        constructorParent.addChild(constructorNode);
        Node functionParent = Node.newBlock();
        functionParent.addChild(constructorParent);
        functionParent.addChild(prototypeValueSetter);
        functionParent.addChild(functionNode);

        when(compiler.report(any(NodeTraversal.class), eq(CheckLevel.WARNING), eq(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
                .thenReturn(new Object());

        checker.visit(traversal, null, functionParent);

        traversal.traverse(functionNode);

        verifyNoMoreInteractions(compiler);
    }

    @Test
    public void testVisit_functionWithPrototypeThisPropertyAccess() {
        Node functionNode = Node.newFunction("foo");
        Node constructorNode = Node.newFunction("constructor", null, null, null, null, null, null, null);
        Node prototype = Node.newObject("Object");
        Node constructorPrototype = Node.newObject("Object");
        Node constructorPrototypeProperty = Node.newProperty(constructorPrototype, "prototype");
        Node constructorPrototypePropertyValue = Node.newFunction("constructor", null, null, null, null, null, null, null);
        Node constructorPrototypeValue = Node.newProperty(prototype, "constructor");
        Node constructorPrototypeValueSetter = Node.newAssign(constructorPrototypeValue, constructorPrototypePropertyValue);
        Node constructorProperty = Node.newProperty(constructorPrototype, "constructor");
        Node constructorPropertyValue = Node.newFunction("constructor", null, null, null, null, null, null, null);
        Node constructorPropertySetter = Node.newAssign(constructorProperty, constructorPropertyValue);
        Node prototypeProperty = Node.newProperty(prototype, "prototype");
        Node prototypePropertyValue = Node.newObject("Object");
        Node prototypePropertyValueSetter = Node.newAssign(prototypePropertyValue, constructorPrototype);
        Node prototypeValueSetter = Node.newAssign(prototype, prototypePropertyValue);
        Node constructorParent = Node.newBlock();
        constructorParent.addChild(constructorPrototypePropertySetter);
        constructorParent.addChild(constructorPrototypeValueSetter);
        constructorParent.addChild(constructorNode);
        Node functionParent = Node.newBlock();
        functionParent.addChild(prototypeValueSetter);
        functionParent.addChild(functionNode);
        Node propertyAccessNode = Node.newGetProp(Node.newIdent("this"), "prototype");
        Node propertyAccessParent = Node.newBlock();
        propertyAccessParent.addChild(functionNode);
        propertyAccessParent.addChild(propertyAccessNode);

        when(compiler.report(any(NodeTraversal.class), eq(CheckLevel.WARNING), eq(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
                .thenReturn(new Object());

        checker.visit(traversal, null, functionParent);

        traversal.traverse(propertyAccessNode);

        verifyNoMoreInteractions(compiler);
    }

    @Test
    public void testVisit_functionWithNoThis() {
        Node functionNode = Node.newFunction("foo");
        Node parentBlock = Node.newBlock();
        parentBlock.addChild(functionNode);

        checker.visit(traversal, null, parentBlock);

        verifyNoInteractions(compiler);
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithNoAnnotation() {
        Node functionNode = Node.newFunction("foo");
        JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

        assertNull(jsDocInfo);
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithConstructorAnnotation() {
        Node functionNode = Node.newFunction("constructor", null, null, null, null, null, null, null);
        JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

        assertTrue(jsDocInfo.isConstructor());
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithThisAnnotation() {
        Node functionNode = Node.newFunction("foo", null, null, null, null, null, null, null);
        Node functionAnnotation = Node.newToken(Token.ANNOTATION, "@this");
        functionNode.addChild(functionAnnotation);
        JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

        assertTrue(jsDocInfo.hasThisType());
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithNoParent() {
        Node functionNode = Node.newFunction("foo");
        JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

        assertNull(jsDocInfo);
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithParentAssign() {
        Node functionNode = Node.newFunction("foo");
        Node parentAssign = Node.newAssign(Node.newIdent("a"), Node.newString("foo"));
        Node parentBlock = Node.newBlock();
        parentBlock.addChild(parentAssign);
        parentBlock.addChild(functionNode);
        JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

        assertNull(jsDocInfo);
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithParentGet() {
        Node functionNode = Node.newFunction("foo");
        Node parentGet = Node.newGetProp(Node.newIdent("a"));
        Node parentBlock = Node.newBlock();
        parentBlock.addChild(parentGet);
        parentBlock.addChild(functionNode);
        JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

        assertNull(jsDocInfo);
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithParentAssignDeep() {
        Node functionNode = Node.newFunction("foo");
        Node parentAssign = Node.newAssign(Node.newIdent("a"), Node.newGetProp(Node.newIdent("prototype"), "x"));
        Node parentBlock = Node.newBlock();
        parentBlock.addChild(parentAssign);
        parentBlock.addChild(functionNode);
        JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

        assertNull(jsDocInfo);
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithParentGetDeep() {
        Node functionNode = Node.newFunction("foo");
        Node parentGet = Node.newGetProp(Node.newIdent("a"), "x");
        Node parentBlock = Node.newBlock();
        parentBlock.addChild(parentGet);
        parentBlock.addChild(functionNode);
        JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

        assertNull(jsDocInfo);
    }
}