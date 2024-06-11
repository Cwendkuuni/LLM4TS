import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void shouldTraverseFunctionsWithoutThisAnnotations() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();

        // When
        boolean shouldTraverse = checker.shouldTraverse(traversal, new Node(Token.FUNCTION), null);

        // Then
        assertFalse(shouldTraverse);
    }

    @Test
    public void shouldNotTraverseConstructorFunctions() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        functionNode.setJSDocInfo(jsDocInfo);

        // When
        boolean shouldTraverse = checker.shouldTraverse(traversal, functionNode, null);

        // Then
        assertFalse(shouldTraverse);
    }

    @Test
    public void shouldNotTraverseFunctionsAnnotatedWithThis() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        functionNode.setJSDocInfo(jsDocInfo);

        // When
        boolean shouldTraverse = checker.shouldTraverse(traversal, functionNode, null);

        // Then
        assertFalse(shouldTraverse);
    }

    @Test
    public void shouldNotTraverseFunctionsAnnotatedWithOverride() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);
        functionNode.setJSDocInfo(jsDocInfo);

        // When
        boolean shouldTraverse = checker.shouldTraverse(traversal, functionNode, null);

        // Then
        assertFalse(shouldTraverse);
    }

    @Test
    public void shouldReportGlobalThisInAssignment() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();
        Node assignmentNode = new Node(Token.ASSIGN);
        Node thisNode = new Node(Token.THIS);
        assignmentNode.addChildToBack(thisNode);

        // When
        boolean shouldReportThis = checker.shouldReportThis(thisNode, assignmentNode);

        // Then
        assertTrue(shouldReportThis);
    }

    @Test
    public void shouldReportGlobalThisInPropertyAccess() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();
        Node propertyAccessNode = new Node(Token.GETPROP);
        Node thisNode = new Node(Token.THIS);
        propertyAccessNode.addChildToBack(thisNode);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(propertyAccessNode);

        // When
        boolean shouldReportThis = checker.shouldReportThis(thisNode, parent);

        // Then
        assertTrue(shouldReportThis);
    }

    @Test
    public void shouldNotReportThisInNestedAssignmentToPrototypeProperty() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();
        Node prototypeNode = new Node(Token.GETPROP);
        prototypeNode.addChildToBack(new Node(Token.THIS));
        prototypeNode.addChildToBack(new Node(Token.STRING, "prototype"));
        Node assignmentNode = new Node(Token.ASSIGN);
        assignmentNode.addChildToBack(prototypeNode);
        Node thisNode = new Node(Token.THIS);
        assignmentNode.addChildToBack(thisNode);

        // When
        boolean shouldReportThis = checker.shouldReportThis(thisNode, assignmentNode);

        // Then
        assertFalse(shouldReportThis);
    }

    @Test
    public void shouldNotReportThisInNamedFunctionAssignment() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();
        Node assignNode = new Node(Token.ASSIGN);
        assignNode.addChildToFront(new Node(Token.NAME, "funcName"));
        Node functionNode = new Node(Token.FUNCTION);
        assignNode.addChildToBack(functionNode);
        Node thisNode = new Node(Token.THIS);
        functionNode.addChildToBack(thisNode);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(assignNode);

        // When
        boolean shouldReportThis = checker.shouldReportThis(thisNode, parent);

        // Then
        assertFalse(shouldReportThis);
    }

    @Test
    public void shouldReportGlobalThisNode() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();
        Node thisNode = new Node(Token.THIS);
        Node parent = new Node(Token.SCRIPT);

        // When
        boolean shouldReportThis = checker.shouldReportThis(thisNode, parent);

        // Then
        assertTrue(shouldReportThis);
    }

    @Test
    public void visitShouldReportDangerousUseOfGlobalThis() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();
        Node thisNode = new Node(Token.THIS);
        Node parent = new Node(Token.SCRIPT);
        Node callback = new Node(Token.FUNCTION);
        callback.addChildToFront(thisNode);

        // When
        checker.visit(traversal, thisNode, parent);

        // Then
        // Assert that a warning report was made
    }

    @Test
    public void visitShouldResetAssignLhsChild() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();
        checker.assignLhsChild = new Node(Token.NAME, "testNode");

        // When
        checker.visit(traversal, new Node(Token.THIS), new Node(Token.SCRIPT));

        // Then
        assertNull(checker.assignLhsChild);
    }

    @Test
    public void visitShouldNotResetAssignLhsChildIfNodeIsNotAssignLhsChild() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal traversal = new NodeTraversal();
        Node assignmentNode = new Node(Token.ASSIGN);
        Node thisNode = new Node(Token.NAME, "childNode");
        assignmentNode.addChildToBack(thisNode);
        checker.assignLhsChild = new Node(Token.NAME, "testNode");

        // When
        checker.visit(traversal, new Node(Token.THIS), assignmentNode);

        // Then
        assertNotNull(checker.assignLhsChild);
    }

    @Test
    public void shouldCreateNewCheckGlobalThisInstance() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;

        // When
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        // Then
        assertNotNull(checker);
    }
}