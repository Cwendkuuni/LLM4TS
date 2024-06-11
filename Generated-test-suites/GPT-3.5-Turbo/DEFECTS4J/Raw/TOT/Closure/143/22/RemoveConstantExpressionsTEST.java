
import static org.junit.Assert.*;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

    @Test
    public void testProcess_CorrectInput_CallsNotifyCompiler() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node externs = new Node();
        Node root = new Node();

        // Act
        pass.process(externs, root);

        // Assert
        assertTrue(pass.getResult().changed);
    }

    @Test
    public void testProcess_InputWithExpressionStatements_SimplifiesExpressionStatements() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node externs = createExpressionStatement(1 + foo() + bar());
        Node root = new Node(Token.BLOCK, externs);

        // Act
        pass.process(externs, root);

        // Assert
        Node expectedExterns = createExpressionStatement(foo());
        assertEquals(expectedExterns, externs);
    }

    @Test
    public void testProcess_InputWithSideEffectFreeSubexpressionNode_LeavesNodeUnchanged() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node externs = createExpressionStatement(foo() + bar());
        Node root = new Node(Token.BLOCK, externs);

        // Act
        pass.process(externs, root);

        // Assert
        Node expectedExterns = createExpressionStatement(foo() + bar());
        assertEquals(expectedExterns, externs);
    }

    @Test
    public void testProcess_InputWithSideEffectNode_ReplacesNodeWithSideEffectNodes() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node expressionBody = createExpressionStatement(foo() + bar());
        Node externs = createExpressionStatement(expressionBody);
        Node root = new Node(Token.BLOCK, externs);

        // Act
        pass.process(externs, root);

        // Assert
        Node expectedExterns = createExpressionStatement(foo());
        assertEquals(expectedExterns, externs);
    }

    private Node createExpressionStatement(Node firstChild) {
        return new Node(Token.EXPR_RESULT, firstChild);
    }

    private Node createExpressionStatement(String expression) {
        return new Node(Token.EXPR_RESULT, new Node(Token.STRING, expression));
    }

    private Node foo() {
        return new Node(Token.NAME, "foo");
    }

    private Node bar() {
        return new Node(Token.NAME, "bar");
    }
}

