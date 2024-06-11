import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = Mockito.mock(Compiler.class);
    preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    transformationHandler = Mockito.mock(AliasTransformationHandler.class);
    options = new CompilerOptions();
    options.aliasTransformation = ALWAYS;
  }

  @Test
  public void testProcess() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Mockito.mock(Node.class);
    Node externs = Mockito.mock(Node.class);

    scopedAliases.process(externs, root);

    // TODO: Add assertions to check that the code was processed correctly.
  }

  @Test
  public void testHotSwapScript() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Mockito.mock(Node.class);
    Node originalRoot = Mockito.mock(Node.class);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.hotSwapScript(root, originalRoot);

    // TODO: Add assertions to check that the aliases were applied and the scopes were collapsed correctly.
  }

  @Test
  public void testValidateScopeCall() {
    Traversal traversal = new Traversal();
    Node root = Mockito.mock(Node.class);

    Node googScopeCall = new Node(Token.CALL, null);
    Node googScopeFunction = new Node(Token.FUNCTION, null);
    Node paramNode = new Node(Token.NAME, "param");

    Node parentNode = new Node(Token.ASSIGN, null);
    parentNode.setFirstChild(googScopeCall);

    googScopeCall.setFirstChild(googScopeFunction);
    googScopeFunction.addChildToEnd(paramNode);

    traversal.enterScope(null);
    traversal.visit(null, root, parentNode);
    traversal.exitScope(null);

    // TODO: Add assertions to check that the scope call was validated correctly.
  }

  @Test(expected = AssertionError.class)
  public void testInvalidScopeCall() {
    Traversal traversal = new Traversal();
    Node root = Mockito.mock(Node.class);

    Node googScopeCall = new Node(Token.CALL, null);
    Node googScopeFunction = new Node(Token.FUNCTION, null);
    Node paramNode = new Node(Token.NAME, "param");

    Node parentNode = new Node(Token.ASSIGN, null);
    parentNode.setFirstChild(googScopeCall);

    Node childNode = new Node(Token.NAME, "child");
    Node childFunction = new Node(Token.FUNCTION, null);
    childFunction.addChildToEnd(childNode);

    googScopeFunction.addChildToEnd(childFunction);

    traversal.enterScope(null);
    traversal.visit(null, root, parentNode);
    traversal.exitScope(null);

    // TODO: Add assertions to check that an error was thrown for an invalid scope call.
  }

  @Test
  public void testAliasDefinition() {
    Traversal traversal = new Traversal();
    Node root = Mockito.mock(Node.class);

    Node googScopeCall = new Node(Token.CALL, null);
    Node googScopeFunction = new Node(Token.FUNCTION, null);

    Node aliasNode = new Node(Token.NAME, "alias");
    Node assignmentNode = new Node(Token.ASSIGN, null);
    assignmentNode.setFirstChild(aliasNode);

    Node paramNode = new Node(Token.NAME, "param");
    Node assignmentValueNode = new Node(Token.NAME, "value");
    Node aliasValueNode = new Node(Token.NAME, "aliasValue");
    assignmentValueNode.setFirstChild(aliasValueNode);

    googScopeFunction.addChildToEnd(assignmentNode);

    googScopeCall.setFirstChild(googScopeFunction);

    traversal.enterScope(null);
    traversal.visit(null, root, googScopeCall);
    traversal.exitScope(null);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("alias", aliasDefinition.getFirstChild().getQualifiedName());
    assertEquals("aliasValue", aliasDefinition.getLastChild().getQualifiedName());

    // TODO: Add assertions to check that the alias definition was recorded correctly.
  }

  @Test(expected = AssertionError.class)
  public void testDuplicateAliasDefinition() {
    Traversal traversal = new Traversal();
    Node root = Mockito.mock(Node.class);

    Node googScopeCall = new Node(Token.CALL, null);
    Node googScopeFunction = new Node(Token.FUNCTION, null);

    Node aliasNode1 = new Node(Token.NAME, "alias");
    Node assignmentNode1 = new Node(Token.ASSIGN, null);
    assignmentNode1.setFirstChild(aliasNode1);

    Node aliasNode2 = new Node(Token.NAME, "alias");
    Node assignmentNode2 = new Node(Token.ASSIGN, null);
    assignmentNode2.setFirstChild(aliasNode1);

    Node paramNode = new Node(Token.NAME, "param");
    Node assignmentValueNode = new Node(Token.NAME, "value");
    Node aliasValueNode = new Node(Token.NAME, "aliasValue");
    assignmentValueNode.setFirstChild(aliasValueNode);

    googScopeFunction.addChildToEnd(assignmentNode1);
    googScopeFunction.addChildToEnd(assignmentNode2);

    googScopeCall.setFirstChild(googScopeFunction);

    traversal.enterScope(null);
    traversal.visit(null, root, googScopeCall);
    traversal.exitScope(null);

    // TODO: Add assertions to check that an error was thrown for a duplicate alias definition.
  }

  @Test
  public void testAliasUsage() {
    Traversal traversal = new Traversal();
    Node root = Mockito.mock(Node.class);

    Node googScopeCall = new Node(Token.CALL, null);
    Node googScopeFunction = new Node(Token.FUNCTION, null);

    Node aliasDefinition = new Node(Token.ASSIGN, null);
    Node aliasNode = new Node(Token.NAME, "alias");
    Node aliasValueNode = new Node(Token.NAME, "aliasValue");
    aliasDefinition.setFirstChild(aliasNode);
    aliasDefinition.setLastChild(aliasValueNode);

    Node paramNode = new Node(Token.NAME, "param");
    Node assignmentNode = new Node(Token.ASSIGN, null);
    assignmentNode.setFirstChild(aliasNode);

    Node aliasUsageNode = new Node(Token.NAME, "alias");
    Node aliasValueUsageNode = new Node(Token.NAME, "aliasValue");

    Node valueNode = new Node(Token.NAME, "value");
    Node assignmentValueNode = new Node(Token.ASSIGN, null);
    assignmentValueNode.setFirstChild(valueNode);

    Node assignmentNode2 = new Node(Token.ASSIGN, null);
    assignmentNode2.setFirstChild(aliasUsageNode);
    assignmentNode2.setLastChild(assignmentValueNode);

    Node bodyNode = new Node(Token.BLOCK, null);
    bodyNode.addChildToEnd(aliasDefinition);
    bodyNode.addChildToEnd(assignmentNode);
    bodyNode.addChildToEnd(paramNode);
    bodyNode.addChildToEnd(aliasUsageNode);
    bodyNode.addChildToEnd(assignmentValueNode);

    googScopeFunction.addChildToEnd(bodyNode);

    googScopeCall.setFirstChild(googScopeFunction);

    traversal.enterScope(null);
    traversal.visit(null, root, googScopeCall);
    traversal.exitScope(null);

    assertEquals(1, traversal.getAliasUsages().size());
    AliasedNode aliasUsage = (AliasedNode) traversal.getAliasUsages().get(0);
    assertEquals("alias", aliasUsage.aliasReference.getString());
    assertEquals("aliasValue", aliasUsage.aliasDefinition.getQualifiedName());

    // TODO: Add assertions to check that the alias usage was recorded correctly.
  }

  @Test(expected = AssertionError.class)
  public void testNonAliasLocalVariable() {
    Traversal traversal = new Traversal();
    Node root = Mockito.mock(Node.class);

    Node googScopeCall = new Node(Token.CALL, null);
    Node googScopeFunction = new Node(Token.FUNCTION, null);

    Node aliasDefinition = new Node(Token.ASSIGN, null);
    Node aliasNode = new Node(Token.NAME, "alias");
    Node aliasValueNode = new Node(Token.NAME, "aliasValue");
    aliasDefinition.setFirstChild(aliasNode);
    aliasDefinition.setLastChild(aliasValueNode);

    Node paramNode = new Node(Token.NAME, "param");
    Node localNode = new Node(Token.NAME, "local");

    Node assignmentNode = new Node(Token.ASSIGN, null);
    assignmentNode.setFirstChild(localNode);

    Node bodyNode = new Node(Token.BLOCK, null);
    bodyNode.addChildToEnd(aliasDefinition);
    bodyNode.addChildToEnd(assignmentNode);
    bodyNode.addChildToEnd(paramNode);

    Node assignmentNode2 = new Node(Token.ASSIGN, null);
    assignmentNode2.setFirstChild(localNode);

    Node valueNode = new Node(Token.NAME, "value");
    Node assignmentValueNode = new Node(Token.ASSIGN, null);
    assignmentValueNode.setFirstChild(valueNode);

    Node assignmentNode3 = new Node(Token.ASSIGN, null);
    assignmentNode3.setFirstChild(localNode);
    assignmentNode3.setLastChild(assignmentValueNode);

    Node bodyNode2 = new Node(Token.BLOCK, null);
    bodyNode2.addChildToEnd(bodyNode);
    bodyNode2.addChildToEnd(assignmentNode2);
    bodyNode2.addChildToEnd(assignmentValueNode);
    bodyNode2.addChildToEnd(assignmentNode3);

    googScopeFunction.addChildToEnd(bodyNode2);

    googScopeCall.setFirstChild(googScopeFunction);

    traversal.enterScope(null);
    traversal.visit(null, root, googScopeCall);
    traversal.exitScope(null);

    // TODO: Add assertions to check that an error was thrown for a non-alias local variable.
  }
}