

import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_WithNonGlobalRuleContext_ShouldReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.STRING));
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithConstructorFunction_ShouldReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfoBuilder().addConstructorType().build();
        functionNode.setJSDocInfo(jsDocInfo);
        boolean result = checkGlobalThis.shouldTraverse(null, functionNode, new Node(Token.STRING));
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithInterfaceFunction_ShouldReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfoBuilder().addInterface().build();
        functionNode.setJSDocInfo(jsDocInfo);
        boolean result = checkGlobalThis.shouldTraverse(null, functionNode, new Node(Token.STRING));
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithThisTypeFunction_ShouldReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfoBuilder().addThisType().build();
        functionNode.setJSDocInfo(jsDocInfo);
        boolean result = checkGlobalThis.shouldTraverse(null, functionNode, new Node(Token.STRING));
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithOverrideFunction_ShouldReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfoBuilder().addOverride().build();
        functionNode.setJSDocInfo(jsDocInfo);
        boolean result = checkGlobalThis.shouldTraverse(null, functionNode, new Node(Token.STRING));
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithValidFunction_ShouldReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        boolean result = checkGlobalThis.shouldTraverse(null, functionNode, new Node(Token.NAME));
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_WithGetPropAssignmentToPrototype_ShouldReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node assignmentNode = new Node(Token.ASSIGN);
        Node getPropNode = new Node(Token.GETPROP);
        Node prototypeNode = new Node(Token.NAME, "prototype");
        getPropNode.addChildToBack(prototypeNode);
        assignmentNode.addChildToBack(getPropNode);
        boolean result = checkGlobalThis.shouldTraverse(null, getPropNode, assignmentNode);
        assertFalse(result);
    }

    @Test
    public void testVisit_WithThisOnLeftSideOfAssignment_ShouldReportError() {
        AbstractCompiler compiler = new Compiler();
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        checkGlobalThis.visit(t, n, parent);
        DiagnosticType error = CheckGlobalThis.GLOBAL_THIS;
        List<JSError> errors = compiler.getErrors();
        assertTrue(errors.stream().anyMatch(e -> e.getType() == error));
    }

    @Test
    public void testVisit_WithThisAsProperty_ShouldReportError() {
        AbstractCompiler compiler = new Compiler();
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        checkGlobalThis.visit(t, n, parent);
        DiagnosticType error = CheckGlobalThis.GLOBAL_THIS;
        List<JSError> errors = compiler.getErrors();
        assertTrue(errors.stream().anyMatch(e -> e.getType() == error));
    }

}

