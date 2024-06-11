import static org.junit.Assert.*;
import org.junit.Test;

public class RenameVarsTest {
  
  @Test
  public void testProcess_WhenNoVariablesToRename_ExpectNoChange() {
    // Arrange
    RenameVars renameVars = new RenameVars();
    Node externs = null;
    Node root = null;
    
    // Act
    renameVars.process(externs, root);
    
    // Assert
    // No assertions. We are just making sure that the process method does not throw any errors.
  }
  
  @Test
  public void testProcess_WhenGlobalVariableToRename_ExpectVariableRenamed() {
    // Arrange
    RenameVars renameVars = new RenameVars();
    Node externs = null;
    Node root = createRootNodeWithGlobalVariable();
    
    // Act
    renameVars.process(externs, root);
    
    // Assert
    assertEquals("newName", root.getFirstChild().getString());
  }
  
  @Test
  public void testProcess_WhenLocalVariableToRename_ExpectVariableRenamed() {
    // Arrange
    RenameVars renameVars = new RenameVars();
    Node externs = null;
    Node root = createRootNodeWithLocalVariable();
    
    // Act
    renameVars.process(externs, root);
    
    // Assert
    assertEquals("newName", root.getLastChild().getString());
  }
  
  private Node createRootNodeWithGlobalVariable() {
    Node root = new Node(Token.ROOT);
    Node script = new Node(Token.SCRIPT);
    root.addChildToBack(script);
    
    Node var = new Node(Token.VAR);
    script.addChildToBack(var);
    
    Node name = new Node(Token.NAME, "oldName");
    var.addChildToBack(name);
    
    return root;
  }
  
  private Node createRootNodeWithLocalVariable() {
    Node root = new Node(Token.ROOT);
    Node script = new Node(Token.SCRIPT);
    root.addChildToBack(script);
    
    Node function = new Node(Token.FUNCTION);
    script.addChildToBack(function);
    
    Node name1 = new Node(Token.NAME, "oldName");
    Node var1 = new Node(Token.VAR, name1);
    function.getFirstChild().addChildToBack(var1);
    
    Node name2 = new Node(Token.NAME, "otherName");
    Node var2 = new Node(Token.VAR, name2);
    function.getLastChild().addChildToBack(var2);
    
    return root;
  }
}