package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler compiler;
    private NodeTraversal nodeTraversal;
    private Node node;
    private Node parent;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        nodeTraversal = mock(NodeTraversal.class);
        node = mock(Node.class);
        parent = mock(Node.class);
        checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    }

    @Test
    public void shouldTraverse_ReturnsFalse_WhenNodeTypeIsFunctionAndConstructorAnnotationPresent() {
        Node functionNode = createNode(Token.FUNCTION);
        JSDocInfo jsDocInfo = createJSDocInfo(true, false);
        
        when(nodeTraversal.getCallback()).thenReturn(checkGlobalThis);
        when(node.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.FUNCTION);
        when(node.getJSDocInfo()).thenReturn(jsDocInfo);
        
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        
        assertEquals(false, result);
    }

    @Test
    public void shouldTraverse_ReturnsFalse_WhenNodeTypeIsFunctionAndThisAnnotationPresent() {
        Node functionNode = createNode(Token.FUNCTION);
        JSDocInfo jsDocInfo = createJSDocInfo(false, true);

        when(nodeTraversal.getCallback()).thenReturn(checkGlobalThis);
        when(node.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.FUNCTION);
        when(node.getJSDocInfo()).thenReturn(jsDocInfo);
        
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        assertEquals(false, result);
    }

    @Test
    public void shouldTraverse_ReturnsFalse_WhenNodeTypeIsAssign() {
        Node assignNode = createNode(Token.ASSIGN);

        when(nodeTraversal.getCallback()).thenReturn(checkGlobalThis);
        when(node.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(parent.getFirstChild()).thenReturn(assignNode);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        assertEquals(true, result);
    }

    @Test
    public void shouldTraverse_ReturnsTrue_WhenNodeTypeIsAssignAndLeftHandSideIsAssignLhsChild() {
        Node assignNode = createNode(Token.ASSIGN);

        when(nodeTraversal.getCallback()).thenReturn(checkGlobalThis);
        when(node.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(parent.getFirstChild()).thenReturn(assignNode);
        when(node).thenReturn(assignNode.getFirstChild());

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        assertEquals(false, result);
    }

    @Test
    public void shouldTraverse_ReturnsFalse_WhenNodeTypeIsAssignAndLeftHandSideIsAssignmentToPrototypeProperty() {
        Node assignNode = createNode(Token.ASSIGN);
        Node getPropNode = createNode(Token.GETPROP);
        Node prototypeNode = createNode(Token.NAME, "prototype");
        Node lhsChildNode = createNode(Token.STRING, "property");

        getPropNode.addChildToBack(prototypeNode);
        getPropNode.addChildToBack(lhsChildNode);
        assignNode.addChildToBack(getPropNode);

        when(nodeTraversal.getCallback()).thenReturn(checkGlobalThis);
        when(node.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(parent.getFirstChild()).thenReturn(assignNode);
        when(node).thenReturn(assignNode.getSecondChild());

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        assertEquals(false, result);
    }

    @Test
    public void shouldTraverse_ReturnsFalse_WhenNodeTypeIsAssignAndLeftHandSideIsAssignmentToSubproperty() {
        Node assignNode = createNode(Token.ASSIGN);
        Node getPropNode = createNode(Token.GETPROP);
        Node lhsChildNode = createNode(Token.STRING, "obj.prototype.property");

        getPropNode.addChildToBack(lhsChildNode);
        assignNode.addChildToBack(getPropNode);

        when(nodeTraversal.getCallback()).thenReturn(checkGlobalThis);
        when(node.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(parent.getFirstChild()).thenReturn(assignNode);
        when(node).thenReturn(assignNode.getSecondChild());

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        assertEquals(false, result);
    }

    @Test
    public void shouldTraverse_ReturnsTrue_WhenNodeTypeIsNotFunctionOrAssign() {
        Node node = createNode(Token.SCRIPT);
        Node parent = createNode(Token.NAME, "parent");

        when(nodeTraversal.getCallback()).thenReturn(checkGlobalThis);
        when(node.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.SCRIPT);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        assertEquals(true, result);
    }

    @Test
    public void visit_CallsReport_WhenNodeTypeIsThisAndShouldReportThisReturnsTrue() {
        Node node = createNode(Token.THIS);
        Node parent = createNode(Token.GETPROP);

        when(nodeTraversal.getCompiler()).thenReturn(compiler);
        when(node.getParent()).thenReturn(parent);
        when(nodeTraversal.makeError(node, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS)).thenReturn(new JSError(null, CheckGlobalThis.GLOBAL_THIS));
        when(compiler.report(Mockito.any(JSError.class))).thenReturn(compiler);

        checkGlobalThis.visit(nodeTraversal, node, parent);

        verify(compiler).report(Mockito.any(JSError.class));
    }

    @Test
    public void visit_DoesNotCallReport_WhenNodeTypeIsThisAndShouldReportThisReturnsFalse() {
        Node node = createNode(Token.THIS);
        Node parent = createNode(Token.GETPROP);
        parent.putProp(Node.ORIGINALNAME_PROP, "property");

        when(nodeTraversal.getCompiler()).thenReturn(compiler);
        when(node.getParent()).thenReturn(parent);
        when(nodeTraversal.makeError(node, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS)).thenReturn(new JSError(null, CheckGlobalThis.GLOBAL_THIS));
        when(compiler.report(Mockito.any(JSError.class))).thenReturn(compiler);

        checkGlobalThis.visit(nodeTraversal, node, parent);

        verify(compiler, never()).report(Mockito.any(JSError.class));
    }

    private Node createNode(int type) {
        return new Node(type);
    }

    private Node createNode(int type, String stringValue) {
        Node node = new Node(type);
        node.setString(stringValue);
        return node;
    }

    private JSDocInfo createJSDocInfo(boolean isConstructor, boolean hasThisType) {
        JSDocInfo jsDocInfo = mock(JSDocInfo.class);

        when(jsDocInfo.isConstructor()).thenReturn(isConstructor);
        when(jsDocInfo.hasThisType()).thenReturn(hasThisType);

        return jsDocInfo;
    }

}