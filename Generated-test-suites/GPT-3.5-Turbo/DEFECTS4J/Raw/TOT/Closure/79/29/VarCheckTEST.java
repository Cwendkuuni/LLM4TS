import static org.junit.Assert.*;
import org.junit.Test;

public class VarCheckTest {

    @Test
    public void testProcess_VariableDefined_Success() {
        // Arrange
        VarCheck varCheck = new VarCheck(new AbstractCompiler());
        Node rootNode = new Node(Token.SCRIPT);
        Node externNode = new Node(Token.SCRIPT);
        rootNode.addChildToFront(new Node(Token.NAME, "var1"));
        rootNode.getFirstChild().putBooleanProp(Node.IS_CONSTANT_NAME, true);
        rootNode.getFirstChild().setString("");
        rootNode.getFirstChild().addChildToBack(new Node(Token.EMPTY));
        
        // Act
        varCheck.process(externNode, rootNode);
        
        // Assert
        // No exception should be thrown
    }

    @Test
    public void testProcess_VariableNotDefined_Error() {
        // Arrange
        VarCheck varCheck = new VarCheck(new AbstractCompiler());
        Node rootNode = new Node(Token.SCRIPT);
        Node externNode = new Node(Token.SCRIPT);
        rootNode.addChildToFront(new Node(Token.NAME, "var1"));
        
        // Act
        try {
            varCheck.process(externNode, rootNode);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Assert
            assertEquals("Unexpected variable var1", e.getMessage());
        }
    }

    @Test
    public void testProcess_ModuleDependencyError_Error() {
        // Arrange
        VarCheck varCheck = new VarCheck(new AbstractCompiler());
        Node rootNode = new Node(Token.SCRIPT);
        Node externNode = new Node(Token.SCRIPT);
        rootNode.addChildToFront(new Node(Token.NAME, "var1"));
        rootNode.getFirstChild().putBooleanProp(Node.IS_CONSTANT_NAME, true);
        rootNode.getFirstChild().setString("var1");
        rootNode.getFirstChild().addChildToBack(new Node(Token.EMPTY));
        rootNode.getFirstChild().getSourceFile().setModuleName("module1");
        externNode.getFirstChild().getSourceFile().setModuleName("module2");
        
        // Act
        try {
            varCheck.process(externNode, rootNode);
            fail("Expected JSError to be thrown");
        } catch (JSError e) {
            // Assert
            assertEquals("module module1 cannot reference var1, defined in module module2, since module2 loads after module1", e.getMessage());
        }
    }

    @Test
    public void testProcess_DuplicateVariable_Error() {
        // Arrange
        VarCheck varCheck = new VarCheck(new AbstractCompiler());
        Node rootNode = new Node(Token.SCRIPT);
        Node externNode = new Node(Token.SCRIPT);
        rootNode.addChildToFront(new Node(Token.VAR, new Node(Token.NAME, "var1")));
        rootNode.getFirstChild().setString("var1");
        rootNode.getFirstChild().addChildToBack(new Node(Token.EMPTY));
        rootNode.getFirstChild().getSourceFile().setModuleName("module1");
        externNode.getFirstChild().getSourceFile().setModuleName("module2");
        varCheck.varsToDeclareInExterns.add("var1");
        
        // Act
        varCheck.process(externNode, rootNode);
        
        // Assert
        assertFalse(varCheck.varsToDeclareInExterns.contains("var1"));
    }
}