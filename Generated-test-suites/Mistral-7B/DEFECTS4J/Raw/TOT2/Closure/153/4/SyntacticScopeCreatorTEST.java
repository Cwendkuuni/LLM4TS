import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.*;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSDocInfo;

public class SyntacticScopeCreatorTest {

  private static final String SOURCE_NAME = "testSourceName";

  @Test
  public void testCreateScope_EmptyNode_ReturnsNull() throws Exception {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler);
    Scope parent = null;

    // When
    Scope scope = syntacticScopeCreator.createScope(Node.NULL, parent);

    // Then
    assertNull(scope);
  }

  @Test
  public void testCreateScope_FunctionNode_CreatesScopeWithFunctionName() throws Exception {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler);
    Scope parent = null;
    Node functionNode = createFunctionNode("testFunction");

    // When
    Scope scope = syntacticScopeCreator.createScope(functionNode, parent);

    // Then
    assertEquals(NODE_FUNCTION, scope.getNodeType());
    assertEquals("testFunction", scope.getName());
  }

  @Test
  public void testCreateScope_FunctionNodeWithArguments_CreatesScopeWithArguments() throws Exception {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler);
    Scope parent = null;
    Node functionNode = createFunctionNode("testFunction", "arg1", "arg2");

    // When
    Scope scope = syntacticScopeCreator.createScope(functionNode, parent);

    // Then
    assertEquals(NODE_FUNCTION, scope.getNodeType());
    assertEquals("testFunction", scope.getName());
    assertEquals("arg1", scope.getFirstVariable().getName());
    assertEquals("arg2", scope.getSecondVariable().getName());
  }

  // ... Add more test cases for other methods and edge cases

  private Node createFunctionNode(String name, String... argNames) {
    Node functionNode = Node.newFunction();
    Node fnNameNode = Node.newName(name);
    Node argsNode = Node.newList();
    for (String argName : argNames) {
      argsNode.addChild(Node.newName(argName));
    }
    functionNode.addChild(fnNameNode);
    functionNode.addChild(argsNode);
    return functionNode;
  }
}