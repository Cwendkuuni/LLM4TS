import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  private Compiler compiler;
  private CompilerOptions options;
  private Traversal traversal;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String fileName, SourcePosition position) {
        System.out.println("Alias Transformation: " + fileName + ":" + position);
        return new AliasTransformation();
      }
    });
    options.setAliasTransformation(REPLACE);
  }

  @Test
  public void testScopedAliases() {
    Node rootNode = createTestNode();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, options.getAliasTransformationHandler());
    scopedAliases.process(null, rootNode);

    assertTrue("Expected no errors", traversal.hasErrors());

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals("Expected 1 alias definition", 1, aliasDefinitionsInOrder.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("Expected alias definition to have a function node as a parent",
        Node.FUNCTION, aliasDefinition.getParent().getType());
    assertEquals("Expected alias definition to have a single child", 1,
        aliasDefinition.getChildCount());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("Expected alias usage to be an assignment node",
        Node.ASSIGN, aliasUsage.getType());
    assertEquals("Expected alias usage to have a left-hand side variable",
        Node.VAR, aliasUsage.getFirstChild().getType());
    assertEquals("Expected alias usage to have a right-hand side alias definition",
        aliasDefinition, aliasUsage.getLastChild().getParent());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals("Expected 1 scope call", 1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("Expected scope call to be a call expression node",
        Node.CALL, scopeCall.getType());
    assertEquals("Expected scope call to have a function node as the callee",
        Node.FUNCTION, scopeCall.getFirstChild().getType());
    assertEquals("Expected scope call to have a single argument, the anonymous function",
        1, scopeCall.getChildCount() - 2);

    Node functionNode = scopeCall.getLastChild();
    assertEquals("Expected function node to be a function declaration node",
        Node.FUNCTION_DECL, functionNode.getType());
    assertEquals("Expected function node to have a single child, the body",
        1, functionNode.getChildCount());
  }

  private Node createTestNode() {
    Node root = Node.newNode(Node.SCRIPT);
    Node scopeCall = Node.newNode(
        Node.CALL, Node.newNode(Node.IDENTIFIER, "goog.scope"),
        Node.newNode(Node.FUNCTION, null, null));
    Node anonymousFunction = Node.newNode(
        Node.FUNCTION_DECL, null, null, null, Node.newNode(Node.BLOCK, null), null);
    Node assignment = Node.newNode(Node.ASSIGN, Node.newNode(Node.IDENTIFIER, "x"),
        Node.newNode(Node.IDENTIFIER, "goog.x"));
    Node body = Node.newNode(Node.BLOCK, null, Node.newNode(Node.STATEMENT, assignment));
    Node scopeBody = Node.newNode(Node.BLOCK, null, body);
    anonymousFunction.setLastChild(scopeBody);
    scopeCall.getLastChild().setLastChild(anonymousFunction);
    root.setLastChild(scopeCall);
    return root;
  }
}