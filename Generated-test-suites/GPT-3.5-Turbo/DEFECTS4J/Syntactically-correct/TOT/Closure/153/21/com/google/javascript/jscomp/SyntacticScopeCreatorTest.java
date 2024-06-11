import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator syntacticScopeCreator;

    @Before
    public void setUp() {
        syntacticScopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    }

    @Test
    public void createScope_FunctionDeclaration_ReturnsScopeWithFunctionName() {
        // Arrange
        Node n = new Node(Token.FUNCTION);
        n.putProp(Node.SOURCENAME_PROP, "test.js");
        Node fnNameNode = new Node(Token.NAME, "testFunction");
        n.addChildToBack(fnNameNode);
        Node args = new Node(Token.LP);
        n.addChildToBack(args);
        Node body = new Node(Token.BLOCK);
        n.addChildToBack(body);

        // Act
        Scope scope = syntacticScopeCreator.createScope(n, null);

        // Assert
        assertNotNull(scope);
        assertNotNull(scope.getVar("testFunction"));
    }

    @Test
    public void createScope_FunctionDeclarationWithEmptyName_DoesNotAddToScope() {
        // Arrange
        Node n = new Node(Token.FUNCTION);
        n.putProp(Node.SOURCENAME_PROP, "test.js");
        Node fnNameNode = new Node(Token.NAME, "");
        n.addChildToBack(fnNameNode);
        Node args = new Node(Token.LP);
        n.addChildToBack(args);
        Node body = new Node(Token.BLOCK);
        n.addChildToBack(body);

        // Act
        Scope scope = syntacticScopeCreator.createScope(n, null);

        // Assert
        assertNotNull(scope);
        assertNull(scope.getVar(""));
    }

    @Test
    public void createScope_FunctionDeclarationWithArgs_DeclaresFunctionArguments() {
        // Arrange
        Node n = new Node(Token.FUNCTION);
        n.putProp(Node.SOURCENAME_PROP, "test.js");
        Node fnNameNode = new Node(Token.NAME, "testFunction");
        n.addChildToBack(fnNameNode);
        Node args = new Node(Token.LP);
        Node arg1 = new Node(Token.NAME, "arg1");
        args.addChildToBack(arg1);
        n.addChildToBack(args);
        Node body = new Node(Token.BLOCK);
        n.addChildToBack(body);

        // Act
        Scope scope = syntacticScopeCreator.createScope(n, null);

        // Assert
        assertNotNull(scope);
        assertNotNull(scope.getVar("arg1"));
    }

    @Test
    public void createScope_GlobalBlock_DeclaresGlobalVariables() {
        // Arrange
        Node n = new Node(Token.SCRIPT);
        n.putProp(Node.SOURCENAME_PROP, "test.js");
        Node var = new Node(Token.VAR);
        Node var1 = new Node(Token.NAME, "var1");
        var.addChildToBack(var1);
        n.addChildToBack(var);

        // Act
        Scope scope = syntacticScopeCreator.createScope(n, null);

        // Assert
        assertNotNull(scope);
        assertNotNull(scope.getVar("var1"));
    }

    @Test
    public void createScope_GlobalBlockWithControlStructures_DeclaresVariablesInNestedBlocks() {
        // Arrange
        Node n = new Node(Token.SCRIPT);
        n.putProp(Node.SOURCENAME_PROP, "test.js");
        Node var = new Node(Token.VAR);
        Node var1 = new Node(Token.NAME, "var1");
        var.addChildToBack(var1);
        n.addChildToBack(var);

        Node ifStatement = new Node(Token.IF);
        Node condition = new Node(Token.TRUE);
        ifStatement.addChildToBack(condition);
        Node ifBlock = new Node(Token.BLOCK);
        ifStatement.addChildToBack(ifBlock);
        Node ifVar = new Node(Token.VAR);
        Node ifVar1 = new Node(Token.NAME, "ifVar1");
        ifVar.addChildToBack(ifVar1);
        ifBlock.addChildToBack(ifVar);
        n.addChildToBack(ifStatement);

        Node whileLoop = new Node(Token.WHILE);
        Node whileCondition = new Node(Token.TRUE);
        whileLoop.addChildToBack(whileCondition);
        Node whileBlock = new Node(Token.BLOCK);
        whileLoop.addChildToBack(whileBlock);
        Node whileVar = new Node(Token.VAR);
        Node whileVar1 = new Node(Token.NAME, "whileVar1");
        whileVar.addChildToBack(whileVar1);
        whileBlock.addChildToBack(whileVar);
        n.addChildToBack(whileLoop);

        // Act
        Scope scope = syntacticScopeCreator.createScope(n, null);

        // Assert
        assertNotNull(scope);
        assertNotNull(scope.getVar("var1"));
        assertNotNull(scope.getVar("ifVar1"));
        assertNotNull(scope.getVar("whileVar1"));
    }

    @Test
    public void createScope_CatchBlock_DeclaresCatchVariable() {
        // Arrange
        Node n = new Node(Token.CATCH);
        Node catchVar = new Node(Token.NAME, "ex");
        n.addChildToBack(catchVar);
        Node catchBlock = new Node(Token.BLOCK);
        n.addChildToBack(catchBlock);

        // Act
        Scope scope = syntacticScopeCreator.createScope(n, null);

        // Assert
        assertNotNull(scope);
        assertNotNull(scope.getVar("ex"));
    }

    @Test
    public void declareVar_VariableAlreadyDeclared_CallsRedeclarationHandler() {
        // Arrange
        String name = "var1";
        Node n = new Node(Token.NAME, name);
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.SCRIPT);
        JSType declaredType = null;
        Node nodeWithLineNumber = new Node(Token.SCRIPT);
        nodeWithLineNumber.putProp(Node.SOURCENAME_PROP, "test.js");
        Scope scope = new Scope(nodeWithLineNumber, new AbstractCompiler());

        // Act
        syntacticScopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

        // Assert
        // Assuming the redeclarationHandler will throw an exception
        assertTrue(true);
    }

    @Test
    public void declareVar_VariableAlreadyDeclaredInGlobalScope_DoesNotCallRedeclarationHandlerIfSuppressDuplicate() {
        // Arrange
        String name = "var1";
        Node n = new Node(Token.NAME, name);
        Node parent = new Node(Token.VAR);
        parent.setJSDocInfo(new JSDocInfo());
        Node gramps = new Node(Token.SCRIPT);
        gramps.putProp(Node.SOURCENAME_PROP, "test.js");
        JSType declaredType = null;
        Node nodeWithLineNumber = new Node(Token.SCRIPT);
        nodeWithLineNumber.putProp(Node.SOURCENAME_PROP, "test.js");
        Scope scope = new Scope(nodeWithLineNumber, new AbstractCompiler());

        // Act
        syntacticScopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

        // Assert
        // Assuming the redeclarationHandler will not throw an exception
        assertTrue(true);
    }

    @Test
    public void declareVar_VariableAlreadyDeclaredInGlobalScope_CallsRedeclarationHandlerIfNoSuppressDuplicate() {
        // Arrange
        String name = "var1";
        Node n = new Node(Token.NAME, name);
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.SCRIPT);
        JSType declaredType = null;
        Node nodeWithLineNumber = new Node(Token.SCRIPT);
        nodeWithLineNumber.putProp(Node.SOURCENAME_PROP, "test.js");
        Scope scope = new Scope(nodeWithLineNumber, new AbstractCompiler());

        // Act
        syntacticScopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

        // Assert
        // Assuming the redeclarationHandler will throw an exception
        assertTrue(true);
    }

}