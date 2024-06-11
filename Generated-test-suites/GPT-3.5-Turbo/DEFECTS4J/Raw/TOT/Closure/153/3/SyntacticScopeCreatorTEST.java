import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator scopeCreator;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        scopeCreator = new SyntacticScopeCreator(compiler);
    }

    @Test
    public void createScope_GlobalBlock_ReturnsScopeWithCorrectParentAndNode() {
        // Arrange
        Node globalBlock = new Node(Token.SCRIPT);
        globalBlock.setProp(Node.SOURCENAME_PROP, "test.js");

        // Act
        Scope result = scopeCreator.createScope(globalBlock, null);

        // Assert
        assertNull(result.getParent());
        assertEquals("test.js", result.getRoot().getProp(Node.SOURCENAME_PROP));
    }

    @Test
    public void createScope_FunctionNoName_DeclaresFunctionNameInScope() {
        // Arrange
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToFront(new Node(Token.NAME, "fnName"));
        functionNode.addChildToBack(new Node(Token.EMPTY));
        functionNode.addChildToBack(new Node(Token.EMPTY));

        // Act
        Scope result = scopeCreator.createScope(functionNode, null);

        // Assert
        assertNotNull(result.getDeclared("fnName"));
    }

    @Test
    public void createScope_GlobalBlockWithVar_DeclaresVariablesInScope() {
        // Arrange
        Node globalBlock = new Node(Token.SCRIPT);
        globalBlock.setProp(Node.SOURCENAME_PROP, "test.js");
        Node varStatement = new Node(Token.VAR);
        varStatement.addChildToBack(new Node(Token.NAME, "x"));
        varStatement.addChildToBack(new Node(Token.NAME, "y"));
        varStatement.addChildToBack(new Node(Token.NAME, "z"));
        globalBlock.addChildToBack(varStatement);

        // Act
        Scope result = scopeCreator.createScope(globalBlock, null);

        // Assert
        assertNotNull(result.getDeclared("x"));
        assertNotNull(result.getDeclared("y"));
        assertNotNull(result.getDeclared("z"));
    }

    @Test
    public void createScope_FunctionWithArguments_DeclaresArgumentsInScope() {
        // Arrange
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setProp(Node.SOURCENAME_PROP, "test.js");
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("fnName");
        Node argsNode = new Node(Token.LP);
        Node argNode = new Node(Token.NAME, "arg1");
        argsNode.addChildToBack(argNode);
        Node bodyNode = new Node(Token.NAME);
        bodyNode.setString("body");
        functionNode.addChildToFront(nameNode);
        functionNode.addChildToBack(argsNode);
        functionNode.addChildToBack(bodyNode);

        // Act
        Scope result = scopeCreator.createScope(functionNode, null);

        // Assert
        assertNotNull(result.getDeclared("arg1"));
    }

    @Test
    public void createScope_FunctionWithCatch_DeclaresCatchVariableInScope() {
        // Arrange
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setProp(Node.SOURCENAME_PROP, "test.js");
        Node nameNode = new Node(Token.EMPTY);
        Node argsNode = new Node(Token.LP);
        Node bodyNode = new Node(Token.SCRIPT);
        Node catchNode = new Node(Token.CATCH);
        catchNode.addChildToFront(new Node(Token.NAME, "err"));
        catchNode.addChildToBack(bodyNode);
        functionNode.addChildToFront(nameNode);
        functionNode.addChildToBack(argsNode);
        functionNode.addChildToBack(catchNode);

        // Act
        Scope result = scopeCreator.createScope(functionNode, null);

        // Assert
        assertNotNull(result.getDeclared("err"));
    }

    @Test
    public void scanVars_ForLoop_DeclaresLoopVariablesInScope() {
        // Arrange
        Node forNode = new Node(Token.FOR);
        Node initNode = new Node(Token.VAR);
        initNode.addChildToBack(new Node(Token.NAME, "i"));
        initNode.addChildToBack(new Node(Token.NAME, "j"));
        Node bodyNode = new Node(Token.SCRIPT);
        forNode.addChildToFront(initNode);
        forNode.addChildToBack(bodyNode);

        // Act
        scopeCreator.scanVars(forNode, null);

        // Assert
        assertNotNull(scopeCreator.scope.getDeclared("i"));
        assertNotNull(scopeCreator.scope.getDeclared("j"));
    }

    @Test
    public void scanVars_IfStatement_DeclaresVariablesInScope() {
        // Arrange
        Node ifStatement = new Node(Token.IF);
        Node expr = new Node(Token.NAME, "x");
        Node thenBlock = new Node(Token.BLOCK);
        Node thenStatement = new Node(Token.SCRIPT);
        thenStatement.addChildToFront(new Node(Token.VAR, new Node(Token.NAME, "i")));
        thenBlock.addChildToBack(thenStatement);
        ifStatement.addChildToBack(thenBlock);

        // Act
        scopeCreator.scanVars(ifStatement, null);

        // Assert
        assertNotNull(scopeCreator.scope.getDeclared("i"));
    }

    @Test
    public void declareVar_VarAlreadyDeclared_CallsRedeclarationHandler() {
        // Arrange
        scopeCreator.createScope(new Node(Token.SCRIPT), null);
        String name = "x";
        Node n = new Node(Token.NAME, name);
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.SCRIPT);
        JSType declaredType = null;
        Node nodeWithLineNumber = new Node(Token.NAME, "anonymous");
        scopeCreator.scope.declare(name, null, declaredType, compiler.getInput("test.js"));

        // Act
        try {
            scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);
            fail("Expected exception to be thrown");
        } catch (RuntimeException ex) {
            assertEquals("Duplicate declaration error message", "Variable x first declared in anonymous", ex.getMessage());
        }
    }

    @Test
    public void declareVar_GlobalScopeArgumentsAlreadyDeclared_CallsRedeclarationHandler() {
        // Arrange
        scopeCreator.createScope(new Node(Token.SCRIPT), null);
        String name = "arguments";
        Node n = new Node(Token.NAME, name);
        Node parent = new Node(Token.FUNCTION);
        Node gramps = new Node(Token.SCRIPT);
        JSType declaredType = null;
        Node nodeWithLineNumber = new Node(Token.NAME, "anonymous");

        // Act
        try {
            scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);
            fail("Expected exception to be thrown");
        } catch (RuntimeException ex) {
            assertEquals("Shadowing arguments error message", "Shadowing \"arguments\" is not allowed", ex.getMessage());
        }
    }
}