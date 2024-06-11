package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_shouldReturnTrueForFunctionsWithoutConstructorOrThisAnnotation() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        assertTrue(checker.shouldTraverse(null, createFunctionNode(null), null));
        assertTrue(checker.shouldTraverse(null, createFunctionNode(createJsDocInfo(false, false)), null));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForFunctionsWithConstructorAnnotation() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        assertFalse(checker.shouldTraverse(null, createFunctionNode(createJsDocInfo(true, false)), null));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForFunctionsWithThisAnnotation() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        assertFalse(checker.shouldTraverse(null, createFunctionNode(createJsDocInfo(false, true)), null));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForNestedAssignmentsToPrototypeProperty() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        Node parent = createAssignmentNode(
                createGetPropNode(
                        createGetPropNode(
                                createNameNode("a"),
                                createStringNode("prototype")
                        ),
                        createStringNode("property")
                ),
                createNameNode("c")
        );
        assertFalse(checker.shouldTraverse(null, createNameNode("a"), parent));
        assertFalse(checker.shouldTraverse(null, createStringNode("prototype"), parent));
        assertFalse(checker.shouldTraverse(null, createStringNode("property"), parent));
        assertFalse(checker.shouldTraverse(null, createNameNode("c"), parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForAssignmentsToPrototypeProperty() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        Node parent = createAssignmentNode(
                createGetPropNode(
                        createNameNode("a"),
                        createStringNode("prototype")
                ),
                createNameNode("b")
        );
        assertFalse(checker.shouldTraverse(null, createNameNode("a"), parent));
        assertFalse(checker.shouldTraverse(null, createStringNode("prototype"), parent));
        assertFalse(checker.shouldTraverse(null, createNameNode("b"), parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForFunctions() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        assertFalse(checker.shouldTraverse(null, createFunctionNode(null), null));
        assertFalse(checker.shouldTraverse(null, createFunctionNode(createJsDocInfo(false, false)), null));
        assertFalse(checker.shouldTraverse(null, createFunctionNode(createJsDocInfo(true, false)), null));
        assertFalse(checker.shouldTraverse(null, createFunctionNode(createJsDocInfo(false, true)), null));
    }

    @Test
    public void testVisit_shouldReportDangerousUseOfGlobalThis() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        assertReportedError(checker, createThisNode(), DiagnosticType.warning("JSC_USED_GLOBAL_THIS"));
    }

    @Test
    public void testVisit_shouldNotReportWhenAssigningThisToVariable() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        assertNoReportedError(checker, createAssignmentNode(createNameNode("a"), createThisNode()));
    }

    @Test
    public void testVisit_shouldNotReportWhenAssigningThisToPrototypeProperty() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        assertNoReportedError(checker, createAssignmentNode(
                createGetPropNode(createNameNode("a"), createStringNode("prototype")),
                createThisNode()
        ));
    }

    @Test
    public void testVisit_shouldNotReportWhenAssigningThisToSubproperty() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        assertNoReportedError(checker, createAssignmentNode(
                createGetPropNode(createNameNode("a"), createStringNode("prototype.property")),
                createThisNode()
        ));
    }

    private Node createThisNode() {
        return new Node(Token.THIS);
    }

    private Node createAssignmentNode(Node lhs, Node rhs) {
        Node assign = new Node(Token.ASSIGN);
        assign.addChildToBack(lhs);
        assign.addChildToBack(rhs);
        return assign;
    }

    private Node createGetPropNode(Node receiver, Node property) {
        Node getprop = new Node(Token.GETPROP);
        getprop.addChildToBack(receiver);
        getprop.addChildToBack(property);
        return getprop;
    }

    private Node createNameNode(String name) {
        return new Node(Token.NAME, name);
    }

    private Node createStringNode(String value) {
        return new Node(Token.STRING, value);
    }

    private Node createFunctionNode(JSDocInfo jsDoc) {
        Node function = new Node(Token.FUNCTION);
        function.setJSDocInfo(jsDoc);
        return function;
    }

    private JSDocInfo createJsDocInfo(boolean isConstructor, boolean hasThisType) {
        JSDocInfo.Builder builder = new JSDocInfo.Builder();
        if (isConstructor) {
            builder.recordConstructor();
        }
        if (hasThisType) {
            builder.recordThisType();
        }
        return builder.build();
    }

    private void assertNoReportedError(CheckGlobalThis checker, Node node) {
        MockDiagnosticReporter reporter = new MockDiagnosticReporter();
        checker.visit(null, node, null);
        assertFalse(reporter.hasErrors());
    }

    private void assertReportedError(CheckGlobalThis checker, Node node, DiagnosticType errorType) {
        MockDiagnosticReporter reporter = new MockDiagnosticReporter();
        checker.visit(null, node, null);
        assertTrue(reporter.hasErrors());
        assertTrue(reporter.getError().getType().equals(errorType));
    }

}

class MockDiagnosticReporter {
    private boolean hasErrors;
    private JSError error;

    public boolean hasErrors() {
        return hasErrors;
    }

    public JSError getError() {
        return error;
    }

    public void report(JSError error) {
        hasErrors = true;
        this.error = error;
    }
}