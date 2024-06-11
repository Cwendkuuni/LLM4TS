package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.rhino.NodeOperations;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {
  @Test
  public void testCollapsingSimpleVariableDeclarations() {
    Scope scope = new Scope();
    Node root = NodeOperations.createTopLevelProgram();
    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(new CompilerPassManager().getCompilerPassManager(scope));

    root.addChildToBack(NodeOperations.createVariableDeclaration("a", null, null));
    root.addChildToBack(NodeOperations.createVariableDeclaration("b", null, null));

    NodeTraversal.traverse(collapser, root, new CollapseFinder());

    List<Node> variables = Lists.newArrayList();
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isVar()) {
        variables.add(child);
      }
    }

    assertEquals(1, variables.size());
    Node variable = variables.get(0);
    assertEquals("a", variable.getString());
    assertTrue(variable.hasProperty("init"));
    assertNull(variable.getFirstChild());
    assertSame(variable, variable.getNext());

    List<Node> initializations = Lists.newArrayList();
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isExpressionStatement()) {
        initializations.add(child);
      }
    }

    assertEquals(0, initializations.size());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithAssignments() {
    Scope scope = new Scope();
    Node root = NodeOperations.createTopLevelProgram();
    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(new CompilerPassManager().getCompilerPassManager(scope));

    Node varA = NodeOperations.createVariableDeclaration("a", null, null);
    Node assignA = NodeOperations.createExpressionStatement(NodeOperations.assign(varA, NodeOperations.literal(true)));
    root.addChildToBack(varA);
    root.addChildToBack(assignA);

    Node varB = NodeOperations.createVariableDeclaration("b", null, null);
    Node assignB = NodeOperations.createExpressionStatement(NodeOperations.assign(varB, NodeOperations.literal(true)));
    root.addChildToBack(varB);
    root.addChildToBack(assignB);

    Node varC = NodeOperations.createVariableDeclaration("c", null, null);
    root.addChildToBack(varC);

    NodeTraversal.traverse(collapser, root, new CollapseFinder());

    List<Node> variables = Lists.newArrayList();
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isVar()) {
        variables.add(child);
      }
    }

    assertEquals(1, variables.size());
    Node variable = variables.get(0);
    assertEquals("a", variable.getString());
    assertTrue(variable.hasProperty("init"));
    assertTrue(variable.getFirstChild() instanceof NodeOperations.AssertStatement);
    assertTrue(((NodeOperations.AssertStatement) variable.getFirstChild()).getCondition().isBooleanLiteral());
    assertNull(variable.getNext());

    List<Node> initializations = Lists.newArrayList();
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isExpressionStatement()) {
        initializations.add(child);
      }
    }

    assertEquals(2, initializations.size());
    Node initA = initializations.get(0);
    Node initB = initializations.get(1);
    assertFalse(initA.getFirstChild() instanceof NodeOperations.Assign);
    assertSame(initB.getFirstChild(), initA.getFirstChild());
  }

  @Test
  public void testCollapsingVariableAndFunctionDeclarations() {
    Scope scope = new Scope();
    Node root = NodeOperations.createTopLevelProgram();
    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(new CompilerPassManager().getCompilerPassManager(scope));

    Node functionDeclaration = NodeOperations.createFunctionDeclaration("foo", null, Lists.newArrayList(NodeOperations.createParameter("x", null, null)));
    functionDeclaration.addChildToBack(NodeOperations.createVariableDeclaration("a", null, null));
    functionDeclaration.addChildToBack(NodeOperations.createVariableDeclaration("b", null, null));
    root.addChildToBack(functionDeclaration);

    NodeTraversal.traverse(collapser, root, new CollapseFinder());

    List<Node> variables = Lists.newArrayList();
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isFunctionDeclaration()) {
        variables.add(child);
      }
    }

    Node function = variables.get(0);
    List<Node> variableDeclarations = Lists.newArrayList();
    for (Node child = function.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isVar()) {
        variableDeclarations.add(child);
      }
    }

    assertEquals(1, variableDeclarations.size());
    Node variable = variableDeclarations.get(0);
    assertEquals("a", variable.getString());
    assertTrue(variable.hasProperty("init"));
    assertNull(variable.getFirstChild());
    assertNull(variable.getNext());

    List<Node> functionBody = Lists.newArrayList();
    for (Node child = function.getLastChild(); child != null; child = child.getPrevious()) {
      if (child.isStatement()) {
        functionBody.add(child);
      }
    }

    assertEquals(0, functionBody.size());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithSameNameButDifferentParents() {
    Scope scope = new Scope();
    Node root = NodeOperations.createTopLevelProgram();
    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(new CompilerPassManager().getCompilerPassManager(scope));

    Node varA = NodeOperations.createVariableDeclaration("a", null, null);
    Node assignA = NodeOperations.createExpressionStatement(NodeOperations.assign(varA, NodeOperations.literal(true)));
    root.addChildToBack(NodeOperations.createBlock(NodeOperations.createStatementList(varA, assignA)));

    Node varB = NodeOperations.createVariableDeclaration("a", null, null);
    Node assignB = NodeOperations.createExpressionStatement(NodeOperations.assign(varB, NodeOperations.literal(true)));
    root.addChildToBack(NodeOperations.createBlock(NodeOperations.createStatementList(varB, assignB)));

    NodeTraversal.traverse(collapser, root, new CollapseFinder());

    List<Node> variables = Lists.newArrayList();
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isBlock()) {
        for (Node node = child.getFirstChild(); node != null; node = node.getNext()) {
          if (node.isVar()) {
            variables.add(node);
          }
        }
      }
    }

    assertEquals(1, variables.size());
    Node variable = variables.get(0);
    assertEquals("a", variable.getString());
    assertTrue(variable.hasProperty("init"));
    assertNull(variable.getFirstChild());
    assertSame(root.getLastChild(), variable.getNext());
  }

  @Test
  public void testCollapsingVariableAndFunctionDeclarationsWithSameNameButDifferentParents() {
    Scope scope = new Scope();
    Node root = NodeOperations.createTopLevelProgram();
    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(new CompilerPassManager().getCompilerPassManager(scope));

    Node functionDeclaration = NodeOperations.createFunctionDeclaration("foo", null, Lists.newArrayList(NodeOperations.createParameter("x", null, null)));
    functionDeclaration.addChildToBack(NodeOperations.createVariableDeclaration("a", null, null));
    root.addChildToBack(NodeOperations.createBlock(NodeOperations.createStatementList(functionDeclaration)));

    Node varB = NodeOperations.createVariableDeclaration("a", null, null);
    Node assignB = NodeOperations.createExpressionStatement(NodeOperations.assign(varB, NodeOperations.literal(true)));
    root.addChildToBack(NodeOperations.createBlock(NodeOperations.createStatementList(varB, assignB)));

    NodeTraversal.traverse(collapser, root, new CollapseFinder());

    List<Node> variables = Lists.newArrayList();
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isBlock()) {
        for (Node node = child.getFirstChild(); node != null; node = node.getNext()) {
          if (node.isVar()) {
            variables.add(node);
          }
        }
      }
    }

    assertEquals(1, variables.size());
    Node variable = variables.get(0);
    assertEquals("a", variable.getString());
    assertTrue(variable.hasProperty("init"));
    assertFalse(variable.getFirstChild() instanceof NodeOperations.FunctionDeclaration);
    assertNull(variable.getNext());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithDifferentScope() {
    Scope parentScope = new Scope();
    Scope childScope = new Scope(parentScope);
    Node root = NodeOperations.createTopLevelProgram();
    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(new CompilerPassManager().getCompilerPassManager(childScope));

    Node varA = NodeOperations.createVariableDeclaration("a", null, null);
    root.addChildToBack(NodeOperations.createBlock(NodeOperations.createStatementList(varA)));
    childScope.setVar(varA.getString(), new Var(childScope, varA));

    Node varB = NodeOperations.createVariableDeclaration("a", null, null);
    root.addChildToBack(NodeOperations.createBlock(NodeOperations.createStatementList(varB)));
    parentScope.setVar(varB.getString(), new Var(parentScope, varB));

    NodeTraversal.traverse(collapser, root, new CollapseFinder());

    List<Node> variables = Lists.newArrayList();
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isBlock()) {
        for (Node node = child.getFirstChild(); node != null; node = node.getNext()) {
          if (node.isVar()) {
            variables.add(node);
          }
        }
      }
    }

    assertEquals(2, variables.size());
    Node variableA = variables.get(0);
    assertEquals("a", variableA.getString());
    assertFalse(variableA.hasProperty("init"));
    assertNull(variableA.getFirstChild());
    assertNull(variableA.getNext());

    Node variableB = variables.get(1);
    assertEquals("a", variableB.getString());
    assertFalse(variableB.hasProperty("init"));
    assertNull(variableB.getFirstChild());
    assertSame(root.getLastChild(), variableB.getNext());
  }

  @Test
  public void testCollapsingVariableDeclarationsWithAssignmentsAndDifferentScope() {
    Scope parentScope = new Scope();
    Scope childScope = new Scope(parentScope);
    Node root = NodeOperations.createTopLevelProgram();
    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(new CompilerPassManager().getCompilerPassManager(childScope));

    Node varA = NodeOperations.createVariableDeclaration("a", null, null);
    Node assignA = NodeOperations.createExpressionStatement(NodeOperations.assign(varA, NodeOperations.literal(true)));
    root.addChildToBack(NodeOperations.createBlock(NodeOperations.createStatementList(varA, assignA)));
    childScope.setVar(varA.getString(), new Var(childScope, varA));

    Node varB = NodeOperations.createVariableDeclaration("a", null, null);
    Node assignB = NodeOperations.createExpressionStatement(NodeOperations.assign(varB, NodeOperations.literal(true)));
    root.addChildToBack(NodeOperations.createBlock(NodeOperations.createStatementList(varB, assignB)));
    parentScope.setVar(varB.getString(), new Var(parentScope, varB));

    NodeTraversal.traverse(collapser, root, new CollapseFinder());

    List<Node> variables = Lists.newArrayList();
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isBlock()) {
        for (Node node = child.getFirstChild(); node != null; node = node.getNext()) {
          if (node.isVar()) {
            variables.add(node);
          }
        }
      }
    }

    assertEquals(1, variables.size());
    Node variable = variables.get(0);
    assertEquals("a", variable.getString());
    assertTrue(variable.hasProperty("init"));
    assertTrue(variable.getFirstChild() instanceof NodeOperations.AssertStatement);
    assertTrue(((NodeOperations.AssertStatement) variable.getFirstChild()).getCondition().isBooleanLiteral());
    assertNull(variable.getNext());
  }

  private static class CollapseFinder extends AbstractPostOrderCallback {
  }
}