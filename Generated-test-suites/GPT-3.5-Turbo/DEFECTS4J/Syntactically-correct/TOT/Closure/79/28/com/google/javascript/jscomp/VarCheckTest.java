import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {
  
  private VarCheck varCheck;
  
  @Before
  public void setup() {
    varCheck = new VarCheck();
  }
  
  @Test
  public void testProcess_ValidInput_ShouldNotThrowException() {
    // Arrange
    Node externs = new Node();
    Node root = new Node();
    
    // Act
    varCheck.process(externs, root);
    
    // Assert
    // No exception should be thrown
  }
  
  @Test
  public void testVisit_VariableDeclaredInSameFile_ShouldNotReportError() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node(Token.NAME, "variable");

    // Act
    varCheck.visit(traversal, node, null);

    // Assert
    // No error reported
    assertEquals(0, traversal.getReportedErrors().size());
  }
  
  @Test
  public void testVisit_UndefinedVariable_ShouldReportError() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node(Token.NAME, "undefinedVariable");

    // Act
    varCheck.visit(traversal, node, null);

    // Assert
    // Error reported
    assertEquals(1, traversal.getReportedErrors().size());
    assertEquals("variable undefinedVariable is undefined", traversal.getReportedErrors().get(0).getMessage());
  }
  
  @Test
  public void testVisit_VariableDeclaredInDifferentModuleWithoutDependency_ShouldReportWarning() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node(Token.NAME, "variable");
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(node);

    // Act
    varCheck.visit(traversal, node, parent);

    // Assert
    // Warning reported
    assertEquals(1, traversal.getReportedWarnings().size());
    assertEquals("missing module dependency; module null should depend on module null because it references variable", traversal.getReportedWarnings().get(0).getMessage());
  }
  
  @Test
  public void testVisit_VariableDeclaredInDifferentModuleWithDependency_ShouldNotReportError() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node(Token.NAME, "variable");
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(node);
    parent.setInputId("Input1");

    // Act
    varCheck.visit(traversal, node, parent);

    // Assert
    // No error reported
    assertEquals(0, traversal.getReportedErrors().size());
  }
  
  @Test
  public void testVisit_VariableDeclaredInDifferentModuleWithNoDependency_ShouldReportError() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node(Token.NAME, "variable");
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(node);
    parent.setInputId("Input1");

    // Act
    varCheck.visit(traversal, node, parent);

    // Assert
    // Error reported
    assertEquals(1, traversal.getReportedErrors().size());
    assertEquals("module null cannot reference null, defined in module Input1, since Input1 loads after null", traversal.getReportedErrors().get(0).getMessage());
  }
  
  @Test
  public void testVisit_DuplicateVariableDeclarationInExterns_ShouldCreateSynthesizedExternVar() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node(Token.NAME, "duplicateVariable");
    Node parent = new Node(Token.VAR);
    parent.addChildToBack(node);

    // Act
    varCheck.visit(traversal, node, parent);

    // Assert
    // No error reported
    assertEquals(0, traversal.getReportedErrors().size());
    // Synthesized extern var should be created
    assertNotNull(varCheck.getSynthesizedExternsRoot().getLastChild());
    assertEquals(Token.NAME, varCheck.getSynthesizedExternsRoot().getLastChild().getType());
    assertEquals("duplicateVariable", varCheck.getSynthesizedExternsRoot().getLastChild().getString());
  }
  
  @Test
  public void testVisit_EmptyFunctionName_ShouldReportError() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node(Token.NAME, "");
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToBack(node);

    // Act
    varCheck.visit(traversal, node, parent);

    // Assert
    // Error reported
    assertEquals(1, traversal.getReportedErrors().size());
    assertEquals("Syntax error: function declaration must have a name", traversal.getReportedErrors().get(0).getMessage());
  }

}