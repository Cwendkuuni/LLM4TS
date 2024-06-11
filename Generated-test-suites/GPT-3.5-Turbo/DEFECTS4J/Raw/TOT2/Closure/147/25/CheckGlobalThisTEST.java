import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    // Test when n is a function annotated with @constructor
    // Edge case scenario
    // Test that shouldTraverse returns false
    // Given
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder(true).build());
    CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);

    assertFalse(checker.shouldTraverse(new NodeTraversal(), n, null));
  }

  @Test
  public void testVisit() {
    // Test when n is a THIS node and parent is a property access
    // Edge case scenario
    // Test that report method is called
    // Given
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    // When
    checker.visit(new NodeTraversal(), thisNode, parent);

    // Assert
    // Verify that report method is called
    assertEquals(1, compiler.getErrorCount());
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    // Test when a function has JSDoc information
    // Typical use case scenario
    // Test that getFunctionJsDocInfo returns the JSDocInfo object
    // Given
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsdocInfo = new JSDocInfoBuilder(true).build();
    functionNode.putProp(Node.JSDOC_INFO_PROP, jsdocInfo);

    CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);

    // When
    JSDocInfo result = checker.getFunctionJsDocInfo(functionNode);

    // Assert
    assertEquals(jsdocInfo, result);
  }

}