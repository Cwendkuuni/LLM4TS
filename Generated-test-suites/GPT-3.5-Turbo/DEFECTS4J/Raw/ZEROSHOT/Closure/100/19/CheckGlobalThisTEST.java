import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void shouldTraverse_returnsTrue_whenNodeIsFunctionAndNotConstructorOrThisAnnotated() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null);
        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldTraverse_returnsFalse_whenNodeIsFunctionAndConstructor() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().setConstructor(true).build());
        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, functionNode, null);
        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldTraverse_returnsFalse_whenNodeIsFunctionAndThisAnnotated() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().setThisType("someType").build());
        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, functionNode, null);
        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldTraverse_returnsTrue_whenNodeIsFunctionAndNoLogicalPlaceForThisAnnotation() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        Node functionNode = new Node(Token.FUNCTION);
        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, functionNode, null);
        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldTraverse_returnsTrue_whenParentIsAssignAndNodeIsChildOfAssignLhs() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node assignNode = new Node(Token.ASSIGN);
        Node lhsNode = new Node(Token.NAME);
        Node rhsNode = new Node(Token.NAME);
        assignNode.addChildToBack(lhsNode);
        assignNode.addChildToBack(rhsNode);
        Node parentNode = new Node(Token.FUNCTION);
        parentNode.addChildToBack(assignNode);
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, lhsNode, parentNode);
        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldTraverse_returnsFalse_whenParentIsAssignAndNodeIsNotChildOfAssignLhsAndAssignLhsIsGetProp() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node assignNode = new Node(Token.ASSIGN);
        Node lhsNode = new Node(Token.GETPROP);
        Node rhsNode = new Node(Token.NAME);
        assignNode.addChildToBack(lhsNode);
        assignNode.addChildToBack(rhsNode);
        Node parentNode = new Node(Token.FUNCTION);
        parentNode.addChildToBack(assignNode);
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, rhsNode, parentNode);
        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldTraverse_returnsFalse_whenParentIsAssignAndNodeIsNotChildOfAssignLhsAndAssignLhsIsGetPropPrototype() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node assignNode = new Node(Token.ASSIGN);
        Node lhsNode = new Node(Token.GETPROP);
        Node prototypeNode = new Node(Token.STRING, "prototype");
        lhsNode.addChildToBack(prototypeNode);
        Node rhsNode = new Node(Token.NAME);
        assignNode.addChildToBack(lhsNode);
        assignNode.addChildToBack(rhsNode);
        Node parentNode = new Node(Token.FUNCTION);
        parentNode.addChildToBack(assignNode);
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, rhsNode, parentNode);
        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldTraverse_returnsFalse_whenParentIsAssignAndNodeIsNotChildOfAssignLhsAndAssignLhsContainsPrototype() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node assignNode = new Node(Token.ASSIGN);
        Node lhsNode = new Node(Token.GETPROP);
        Node prototypeNode = new Node(Token.STRING, "prototype");
        lhsNode.addChildToBack(new Node(Token.STRING, "someObject"));
        lhsNode.addChildToBack(prototypeNode);
        Node rhsNode = new Node(Token.NAME);
        assignNode.addChildToBack(lhsNode);
        assignNode.addChildToBack(rhsNode);
        Node parentNode = new Node(Token.FUNCTION);
        parentNode.addChildToBack(assignNode);
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, rhsNode, parentNode);
        // Assert
        assertFalse(result);
    }

    @Test
    public void visit_reportsError_whenNodeIsThisAndShouldReportThis() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node thisNode = new Node(Token.THIS);
        compiler.setReport(new DiagnosticType());
        // Act
        checkGlobalThis.visit(t, thisNode, null);
        // Assert
        // Assert that the report method in the compiler is called with the expected arguments
    }

    @Test
    public void visit_doesNotReportError_whenNodeIsThisAndShouldNotReportThis() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node thisNode = new Node(Token.THIS);
        Node parentNode = new Node(Token.ASSIGN);
        checkGlobalThis.shouldReportThis(thisNode, parentNode);
        compiler.setReport(new DiagnosticType());
        // Act
        checkGlobalThis.visit(t, thisNode, parentNode);
        // Assert
        // Assert that the report method in the compiler is not called
    }
}