import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.GLOBAL_THIS;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.List;

public class ScopeTest {

  private static Node createVarNode(String name) {
    return new Node(Token.VAR, new Node(Token.IDENT, name), null);
  }

  private static Node createAssignNode(Node lhs, Node rhs) {
    return new Node(Token.ASSIGN, lhs, rhs);
  }

  private static Node createFunctionNode() {
    return new Node(Token.FUNCTION, null, new Node[0]);
  }

  private static Node createCallNode(Node callee, List<Node> args) {
    return new Node(Token.CALL, callee, args.toArray(new Node[0]));
  }

  private static Node createBlockNode(List<Node> children) {
    return new Node(Token.BLOCK, null, children.toArray(new Node[0]));
  }

  private static Node createInputNode(String name, boolean isExtern) {
    Node input = new Node(Token.IDENT, name);
    input.setExtern(isExtern);
    return input;
  }

  private static ObjectType createNativeObjectType(String name) {
    return (ObjectType) JSType.getNativeType(name);
  }

  private static Scope createScope(Node rootNode) {
    return new Scope(null, rootNode) {
      @Override
      ObjectType getTypeOfThis() {
        return createNativeObjectType("Object");
      }
    };
  }

  private static Scope createFunctionScope(Node rootNode) {
    return new Scope(createScope(null), rootNode) {
      @Override
      ObjectType getTypeOfThis() {
        return createNativeObjectType("Function");
      }
    };
  }

  private static void assertType(Scope scope, String name, JSType expectedType) {
    Var var = scope.getVar(name);
    assertEquals("Variable not found: " + name, expectedType, var.getType());
  }

  private static void assertVarCount(Scope scope, int expectedCount) {
    assertEquals("Unexpected number of variables in scope: " + scope, expectedCount, scope.getVarCount());
  }

  @Test
  public void testGlobalScope() {
    Scope scope = createScope(null);

    Var globalVar = scope.declare("x", null, IntType.INSTANCE, null);
    assertType(scope, "x", IntType.INSTANCE);
    assertVarCount(scope, 1);

    Var globalVar2 = scope.declare("x", null, StringType.INSTANCE, null);
    assertType(scope, "x", StringType.INSTANCE);
    assertVarCount(scope, 2);

    // Test getting variables from parent scope
    Var parentVar = scope.getParent().getVar("x");
    assertEquals("Incorrect variable in parent scope: " + parentVar, globalVar, parentVar);

    scope.undeclare(globalVar);
    assertNull("Variable not undeclared: " + globalVar, globalVar);

    scope.undeclare(globalVar2);
    assertNull("Variable not undeclared: " + globalVar2, globalVar2);
  }

  @Test
  public void testFunctionScope() {
    Scope globalScope = createScope(null);
    Node rootNode = createFunctionNode();

    Scope functionScope = createFunctionScope(rootNode);

    Var argVar = functionScope.getVar("arg");
    assertNull("Argument variable should not be null: " + argVar, argVar);

    // Test declaring local variables
    Var localVar1 = functionScope.declare("x", createVarNode("x"), IntType.INSTANCE, null);
    assertType(functionScope, "x", IntType.INSTANCE);
    assertVarCount(functionScope, 1);

    Var localVar2 = functionScope.declare("y", createVarNode("y"), StringType.INSTANCE, null);
    assertType(functionScope, "y", StringType.INSTANCE);
    assertVarCount(functionScope, 2);

    // Test declaring variable with same name as argument
    Var argVar2 = createScope(rootNode).declare("arg", createVarNode("arg"), StringType.INSTANCE, null);
    Var localVar3 = functionScope.declare("arg", createVarNode("arg"), IntType.INSTANCE, null);
    assertNotSame("Variables with same name should not be the same: " + localVar3, argVar2, localVar3);

    // Test getting variable from parent scope
    Var globalVar = globalScope.getVar("x");
    assertEquals("Incorrect variable in global scope: " + globalVar, null, globalVar);

    assertType(functionScope, "x", IntType.INSTANCE);
    assertType(functionScope, "y", StringType.INSTANCE);
    assertType(functionScope, "arg", StringType.INSTANCE); // Should get argument
    assertType(functionScope, "arg", IntType.INSTANCE); // Should get local variable

    // Test undeclaring local variables
    functionScope.undeclare(localVar1);
    assertNull("Variable not undeclared: " + localVar1, localVar1);
    assertType(functionScope, "x", IntType.INSTANCE);
    assertType(functionScope, "y", StringType.INSTANCE);
    assertType(functionScope, "arg", StringType.INSTANCE);

    functionScope.undeclare(localVar2);
    assertNull("Variable not undeclared: " + localVar2, localVar2);
    assertType(functionScope, "x", IntType.INSTANCE);
    assertType(functionScope, "y", StringType.INSTANCE);
    assertNull(functionScope.getVar("arg"));

    // Test declaring variables with same name in parent and child scope
    Scope parentScope = new Scope(functionScope, null) {
      @Override
      ObjectType getTypeOfThis() {
        return createNativeObjectType("Object");
      }
    };

    Var parentVar = parentScope.declare("x", createVarNode("x"), IntType.INSTANCE, null);
    Var childVar1 = functionScope.declare("x", createVarNode("x"), StringType.INSTANCE, null);
    Var childVar2 = functionScope.declare("x", createVarNode("x"), IntType.INSTANCE, null);

    assertNotSame("Variables with same name in parent and child scope should not be the same: " + childVar1, parentVar, childVar1);
    assertNotSame("Variables with same name in parent and child scope should not be the same: " + childVar2, parentVar, childVar2);
    assertSame("Variables with same name in parent and child scope should be equal: " + childVar1, childVar2, childVar1);

    // Test getting variables from parent scope
    Var globalVar2 = globalScope.declare("x", createVarNode("x"), StringType.INSTANCE, null);
    assertType(functionScope, "x", StringType.INSTANCE);
    assertType(functionScope, "y", StringType.INSTANCE);
    assertType(functionScope, "arg", StringType.INSTANCE);
    assertNotSame("Variables with same name but different types should not be equal: " + childVar1, globalVar2, childVar1);

    // Test accessing variables from grandparent scope
    Var grandparentVar = globalScope.declare("x", createVarNode("x"), IntType.INSTANCE, null);
    assertType(functionScope, "x", IntType.INSTANCE);
    assertType(functionScope, "y", StringType.INSTANCE);
    assertType(functionScope, "arg", StringType.INSTANCE);
    assertEquals("Incorrect variable in grandparent scope: " + grandparentVar, grandparentVar, parentScope.getParent().getVar("x"));

    // Test assigning values to variables
    Var assignVar1 = functionScope.declare("x", createVarNode("x"), IntType.INSTANCE, null);
    Var assignVar2 = functionScope.declare("y", createVarNode("y"), StringType.INSTANCE, null);

    Node assignNode1 = createAssignNode(assignVar1.getNameNode(), createIntLiteral(10));
    Node assignNode2 = createAssignNode(assignVar2.getNameNode(), createStringLiteral("hello"));

    Node blockNode = createBlockNode(ImmutableList.of(assignNode1, assignNode2));

    Node functionNode = createFunctionNode();
    Node callNode = createCallNode(functionNode, ImmutableList.of(rootNode, blockNode));

    Node traversalRoot = rootNode;
    NodeTraversal.traverse(traverse, callNode, new AbstractCompiler() {
      @Override
      public void process(NodeTraversal t, Node n, SourceFile file) {
        if (n.getType() == Token.ASSIGN) {
          Var var1 = t.getScope().getVar(((AssignNode) n).getLhs().getName());
          Var var2 = (Var) t.getScope().getSlot(var1.getName());
          t.setCurrentPosition(var2.getParentNode());
          Var var3 = t.getScope().declare(var1.getName(), var1.getNameNode(), var2.getType(), null);
          t.setCurrentPosition(n);
          var3.setType(IntType.INSTANCE);
          t.setCurrentPosition(n);
        }
      }
    });

    assertType(functionScope, "x", IntType.INSTANCE);
    assertType(functionScope, "y", StringType.INSTANCE);
    assertEquals("Unexpected value for variable x: " + assignVar1.getType(), IntType.INSTANCE, assignVar1.getType());
    assertEquals("Unexpected value for variable y: " + assignVar2.getType(), StringType.INSTANCE, assignVar2.getType());

    // Test type checking for variables
    Var checkVar1 = functionScope.declare("x", createVarNode("x"), IntType.INSTANCE, null);
    Var checkVar2 = functionScope.declare("y", createVarNode("y"), StringType.INSTANCE, null);

    Node checkAssignNode1 = createAssignNode(checkVar1.getNameNode(), createIntLiteral(10));
    Node checkAssignNode2 = createAssignNode(checkVar2.getNameNode(), createIntLiteral(10));

    List<Node> checkBlockNodes = new ArrayList<>();
    checkBlockNodes.add(checkAssignNode1);
    checkBlockNodes.add(checkAssignNode2);

    Node checkFunctionNode = createFunctionNode();
    Node checkCallNode = createCallNode(checkFunctionNode, ImmutableList.of(rootNode, createBlockNode(checkBlockNodes)));

    Node checkTraversalRoot = rootNode;
    NodeTraversal.traverse(traverse, checkCallNode, new AbstractCompiler() {
      @Override
      public void process(NodeTraversal t, Node n, SourceFile file) {
        if (n.getType() == Token.ASSIGN) {
          Var var1 = t.getScope().getVar(((AssignNode) n).getLhs().getName());
          if (var1.getType() != IntType.INSTANCE) {
            fail("Unexpected type for variable x: " + var1.getType());
          }
        }
      }
    });

    Var checkVar3 = functionScope.getVar("x");
    assertEquals("Unexpected value for variable x: " + checkVar3.getType(), IntType.INSTANCE, checkVar3.getType());

    // Test type checking for variables with same name but different types
    Var checkVar4 = functionScope.declare("x", createVarNode("x"), StringType.INSTANCE, null);
    Node checkAssignNode3 = createAssignNode(checkVar4.getNameNode(), createStringLiteral("hello"));

    List<Node> checkBlockNodes2 = new ArrayList<>();
    checkBlockNodes2.add(checkAssignNode3);

    Node checkFunctionNode2 = createFunctionNode();
    Node checkCallNode2 = createCallNode(checkFunctionNode2, ImmutableList.of(rootNode, createBlockNode(checkBlockNodes2)));

    Node checkTraversalRoot2 = rootNode;
    NodeTraversal.traverse(traverse, checkCallNode2, new AbstractCompiler() {
      @Override
      public void process(NodeTraversal t, Node n, SourceFile file) {
        if (n.getType() == Token.ASSIGN) {
          Var var1 = t.getScope().getVar(((AssignNode) n).getLhs().getName());
          if (var1.getType() != StringType.INSTANCE) {
            fail("Unexpected type for variable x: " + var1.getType());
          }
        }
      }
    });

    Var checkVar5 = functionScope.getVar("x");
    assertEquals("Unexpected value for variable x: " + checkVar5.getType(), StringType.INSTANCE, checkVar5.getType());
  }

  @Test
  public void testVariableWithNoName() {
    Scope scope = createScope(null);

    try {
      scope.declare(null, null, IntType.INSTANCE, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testVariableWithNullNameNode() {
    Scope scope = createScope(null);

    try {
      scope.declare(null, null, IntType.INSTANCE, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      scope.declare("x", null, IntType.INSTANCE, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testVariableWithNullType() {
    Scope scope = createScope(null);

    try {
      scope.declare("x", createVarNode("x"), null, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testVariableWithNullInput() {
    Scope scope = createScope(null);

    try {
      scope.declare("x", createVarNode("x"), IntType.INSTANCE, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      scope.declare("x", createVarNode("x"), IntType.INSTANCE, null, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testVariableWithDefineAnnotation() {
    Scope scope = createScope(null);

    JSDocInfo info = NodeUtil.createJSDocInfo("@define x");
    Var var = scope.declare("x", createVarNode("x"), IntType.INSTANCE, null, null, info, true);

    assertEquals("Unexpected value for variable x: " + var.getType(), IntType.INSTANCE, var.getType());
    assertTrue("Unexpected value for variable x: " + var.isDefine(), var.isDefine());
    assertEquals("Unexpected value for variable x: " + var.getNameNode().getFirstChild(), info, var.getJSDocInfo());
  }

  @Test
  public void testVariableNoShadow() {
    Scope scope = createScope(null);

    Var var1 = scope.declare("x", createVarNode("x"), IntType.INSTANCE, null);
    Var var2 = scope.declare("x", createVarNode("x"), StringType.INSTANCE, null);

    assertTrue("Variable should have no shadow: " + var1.isNoShadow(), var1.isNoShadow());
    assertTrue("Variable should have no shadow: " + var2.isNoShadow(), var2.isNoShadow());
  }
}