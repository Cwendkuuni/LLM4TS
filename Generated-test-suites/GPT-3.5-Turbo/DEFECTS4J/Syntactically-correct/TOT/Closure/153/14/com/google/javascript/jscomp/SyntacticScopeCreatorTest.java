import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator scopeCreator;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        scopeCreator = new SyntacticScopeCreator(compiler);
    }

    @Test
    public void testCreateScope_GlobalBlock_Success() {
        // Arrange
        Node rootNode = new Node(Token.SCRIPT);

        // Act
        Scope scope = scopeCreator.createScope(rootNode, null);

        // Assert
        assertNull(scope.getParent());
        assertNull(scope.getRootNode());
    }

    @Test
    public void testCreateScope_FunctionBlock_Success() {
        // Arrange
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(functionNode);

        // Act
        Scope scope = scopeCreator.createScope(functionNode, null);

        // Assert
        assertNotNull(scope.getParent());
        assertEquals(parent, scope.getParent().getRootNode());
        assertNotNull(scope.getRootNode());
        assertEquals(functionNode, scope.getRootNode());
    }

    @Test
    public void testScanVars_VariableDeclaration_Success() {
        // Arrange
        Node rootNode = new Node(Token.SCRIPT);
        Node varNode = new Node(Token.VAR);
        rootNode.addChildToFront(varNode);
        Node varNameNode = new Node(Token.NAME, "x");
        varNode.addChildToBack(varNameNode);

        // Act
        scopeCreator.createScope(rootNode, null);

        // Assert
        assertTrue(scopeCreator.scope.isDeclared("x", false));
    }

    @Test
    public void testScanVars_FunctionDeclaration_Success() {
        // Arrange
        Node rootNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        rootNode.addChildToFront(functionNode);
        Node functionNameNode = new Node(Token.NAME, "foo");
        functionNode.addChildToBack(functionNameNode);

        // Act
        scopeCreator.createScope(rootNode, null);

        // Assert
        assertTrue(scopeCreator.scope.isDeclared("foo", false));
    }

    @Test
    public void testScanVars_CatchBlock_Success() {
        // Arrange
        Node rootNode = new Node(Token.SCRIPT);
        Node catchNode = new Node(Token.CATCH);
        rootNode.addChildToFront(catchNode);
        Node catchVarNode = new Node(Token.NAME, "error");
        catchNode.addChildToBack(catchVarNode);

        // Act
        scopeCreator.createScope(rootNode, null);

        // Assert
        assertTrue(scopeCreator.scope.isDeclared("error", false));
    }

    @Test
    public void testScanVars_StatementBlock_Success() {
        // Arrange
        Node rootNode = new Node(Token.SCRIPT);
        Node statementBlockNode = new Node(Token.BLOCK);
        rootNode.addChildToFront(statementBlockNode);
        Node varNode = new Node(Token.VAR);
        statementBlockNode.addChildToBack(varNode);
        Node varNameNode = new Node(Token.NAME, "x");
        varNode.addChildToBack(varNameNode);

        // Act
        scopeCreator.createScope(rootNode, null);

        // Assert
        assertTrue(scopeCreator.scope.isDeclared("x", false));
    }
}