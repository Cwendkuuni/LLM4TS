import org.junit.Test;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope_functionDeclaration() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator();
        Node functionNode = Node.newString(Token.FUNCTION, "functionName");
        scopeCreator.createScope(functionNode, null);
        // Verify that the function name is declared in the scope
        assertNotNull(scopeCreator.getScope().getVar("functionName"));
    }

    @Test
    public void testCreateScope_functionExpression() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator();
        Node functionNode = Node.newString(Token.FUNCTION, "");
        scopeCreator.createScope(functionNode, null);
        // Verify that the function expression's name is not declared in the scope
        assertNull(scopeCreator.getScope().getVar(""));
    }

    @Test
    public void testCreateScope_functionParameters() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator();
        Node functionNode = Node.newString(Token.FUNCTION, "functionName");
        Node parametersNode = Node.newString(Token.LP, "");
        functionNode.addChildToFront(parametersNode);
        Node parameter1 = Node.newString(Token.NAME, "param1");
        parametersNode.addChildToFront(parameter1);
        Node parameter2 = Node.newString(Token.NAME, "param2");
        parametersNode.addChildToBack(parameter2);

        scopeCreator.createScope(functionNode, null);
        // Verify that the parameters are declared in the scope
        assertNotNull(scopeCreator.getScope().getVar("param1"));
        assertNotNull(scopeCreator.getScope().getVar("param2"));
    }

    @Test
    public void testCreateScope_variableDeclaration() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator();
        Node varNode = Node.newString(Token.VAR, "");
        Node var1 = Node.newString(Token.NAME, "var1");
        Node var2 = Node.newString(Token.NAME, "var2");
        varNode.addChildToFront(var1);
        varNode.addChildToBack(var2);

        scopeCreator.createScope(varNode, null);
        // Verify that the variables are declared in the scope
        assertNotNull(scopeCreator.getScope().getVar("var1"));
        assertNotNull(scopeCreator.getScope().getVar("var2"));
    }

    @Test
    public void testCreateScope_catchBlock() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator();
        Node catchNode = Node.newString(Token.CATCH, "");
        Node catchVar = Node.newString(Token.NAME, "catchVar");
        Node catchBlock = Node.newString(Token.BLOCK, "");

        catchNode.addChildToFront(catchVar);
        catchNode.addChildToBack(catchBlock);

        scopeCreator.createScope(catchNode, null);
        // Verify that the catch variable is declared in the scope
        assertNotNull(scopeCreator.getScope().getVar("catchVar"));
    }

    @Test
    public void testCreateScope_globalBlock() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator();
        Node globalBlock = Node.newString(Token.BLOCK, "");

        scopeCreator.createScope(globalBlock, null);
        // Verify that the global block is created as the root scope
        assertNull(scopeCreator.getScope().getParent());
    }

    @Test
    public void testCreateScope_script() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator();
        Node script = Node.newString(Token.SCRIPT, "");

        scopeCreator.createScope(script, null);
        // Verify that the source name is set properly
        assertEquals("", scopeCreator.getSourceName());
    }

}