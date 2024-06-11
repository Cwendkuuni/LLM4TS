package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilationTestFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class CollapseVariableDeclarationsTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;
  private Node root;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    options = new CompilerOptions();
    nodeFactory = NodeFactory.getRoot();
    root = nodeFactory.createProgram();
  }

  @Test
  public void testCollapseSingleVariableDeclaration() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    CompilerPassManager manager = new CompilerPassManager(options, compiler);
    manager.addPass(pass);
    CompilationTestFactory.testSingleCompilation(manager, root);

    Node varNode = root.getFirstChild().getFirstChild();
    Node collapsedVarNode = findCollapsedVariable(root, varNode.getString());
    assertNotNull(collapsedVarNode);
  }

  @Test
  public void testCollapseMultipleVariableDeclarations() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    CompilerPassManager manager = new CompilerPassManager(options, compiler);
    manager.addPass(pass);
    root.setFirstChild(
        nodeFactory.createBlock("var a;", root.getFirstChild())
            .addChild(nodeFactory.createVar("a"))
            .addChild(nodeFactory.createBlock("var b = 1;", null))
            .addChild(nodeFactory.createVar("b"))
            .addChild(nodeFactory.createBlock("var c = 2;", null))
            .addChild(nodeFactory.createVar("c"))
    );
    CompilationTestFactory.testSingleCompilation(manager, root);

    Node varA = findVariable(root, "a");
    Node collapsedVarA = findCollapsedVariable(root, "a");
    Node varB = findVariable(root, "b");
    Node collapsedVarB = findCollapsedVariable(root, "b");
    Node varC = findVariable(root, "c");
    Node collapsedVarC = findCollapsedVariable(root, "c");

    assertNotNull(collapsedVarA);
    assertNotNull(collapsedVarB);
    assertNotNull(collapsedVarC);

    assertNotNull(varA.getParent());
    assertNotNull(varB.getParent());
    assertNotNull(varC.getParent());

    assertEquals(collapsedVarA, varA.getParent());
    assertEquals(collapsedVarB, varB.getParent());
    assertEquals(collapsedVarC, varC.getParent());
  }

  @Test
  public void testCollapseMultipleVariableDeclarationsAndAssignments() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    CompilerPassManager manager = new CompilerPassManager(options, compiler);
    manager.addPass(pass);
    root.setFirstChild(
        nodeFactory.createBlock("var a;", root.getFirstChild())
            .addChild(nodeFactory.createVar("a"))
            .addChild(nodeFactory.createBlock("var b = 1;", null))
            .addChild(nodeFactory.createVar("b"))
            .addChild(nodeFactory.createBlock("var c = 2;", null))
            .addChild(nodeFactory.createVar("c"))
            .addChild(nodeFactory.createBlock("a = true;", null))
            .addChild(nodeFactory.createAssign(nodeFactory.createName("a"), nodeFactory.createBool(true)))
            .addChild(nodeFactory.createBlock("b = true;", null))
            .addChild(nodeFactory.createAssign(nodeFactory.createName("b"), nodeFactory.createBool(true)))
            .addChild(nodeFactory.createBlock("c = true;", null))
            .addChild(nodeFactory.createAssign(nodeFactory.createName("c"), nodeFactory.createBool(true)))
    );
    CompilationTestFactory.testSingleCompilation(manager, root);

    Node varA = findVariable(root, "a");
    Node collapsedVarA = findCollapsedVariable(root, "a");
    Node varB = findVariable(root, "b");
    Node collapsedVarB = findCollapsedVariable(root, "b");
    Node varC = findVariable(root, "c");
    Node collapsedVarC = findCollapsedVariable(root, "c");

    assertNotNull(collapsedVarA);
    assertNotNull(collapsedVarB);
    assertNotNull(collapsedVarC);

    assertNotNull(varA.getParent());
    assertNotNull(varB.getParent());
    assertNotNull(varC.getParent());

    assertEquals(collapsedVarA, varA.getParent());
    assertEquals(collapsedVarB, varB.getParent());
    assertEquals(collapsedVarC, varC.getParent());

    Node collapsedAssignA = findCollapsedAssign(root, "a");
    Node collapsedAssignB = findCollapsedAssign(root, "b");
    Node collapsedAssignC = findCollapsedAssign(root, "c");

    assertNotNull(collapsedAssignA);
    assertNotNull(collapsedAssignB);
    assertNotNull(collapsedAssignC);

    assertEquals(collapsedVarA, collapsedAssignA.getParent());
    assertEquals(collapsedVarB, collapsedAssignB.getParent());
    assertEquals(collapsedVarC, collapsedAssignC.getParent());
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationInsideIfStatement() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    CompilerPassManager manager = new CompilerPassManager(options, compiler);
    manager.addPass(pass);
    root.setFirstChild(
        nodeFactory.createBlock("if (true) { var a; }", root.getFirstChild())
            .addChild(nodeFactory.createIf(Token.IF, null, null))
            .addChild(nodeFactory.createBlock("var a;", null))
            .addChild(nodeFactory.createVar("a"))
    );
    CompilationTestFactory.testSingleCompilation(manager, root);
  }

  @Test
  public void testCollapseVariableDeclarationInsideFunction() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    CompilerPassManager manager = new CompilerPassManager(options, compiler);
    manager.addPass(pass);
    root.setFirstChild(
        nodeFactory.createFunction(Token.FUNCTION, "f", null, null)
            .addChild(nodeFactory.createBlock("var a;", null))
            .addChild(nodeFactory.createVar("a"))
            .addChild(nodeFactory.createBlock("return a;", null))
            .addChild(nodeFactory.createReturn(null))
    );
    CompilationTestFactory.testSingleCompilation(manager, root);

    Node function = root.getFirstChild();
    Node collapsedVar = findCollapsedVariable(function, "a");
    Node collapsedReturn = findCollapsedReturn(function);

    assertNotNull(collapsedVar);
    assertNotNull(collapsedReturn);

    assertEquals(collapsedVar, collapsedReturn.getFirstChild());
  }

  @Test
  public void testCollapseVariableDeclarationInsideSwitchStatement() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    CompilerPassManager manager = new CompilerPassManager(options, compiler);
    manager.addPass(pass);
    root.setFirstChild(
        nodeFactory.createBlock("switch (1) {", root.getFirstChild())
            .addChild(nodeFactory.createSwitch(null, Token.SWITCH))
            .addChild(nodeFactory.createBlock("var a;", null))
            .addChild(nodeFactory.createVar("a"))
            .addChild(nodeFactory.createBlock("case 1:", null))
            .addChild(nodeFactory.createBlock("", null))
            .addChild(nodeFactory.createBlock("}", null))
    );
    CompilationTestFactory.testSingleCompilation(manager, root);

    Node switchStatement = root.getFirstChild();
    Node collapsedVar = findCollapsedVariable(switchStatement, "a");
    Node collapsedCase = findCollapsedCase(switchStatement, "1");

    assertNotNull(collapsedVar);
    assertNotNull(collapsedCase);

    assertEquals(collapsedVar, collapsedCase.getFirstChild());
  }

  private Node findCollapsedVariable(Node root, String name) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child.isVar() && child.getString().equals(name)) {
        return findCollapsedVariable(child);
      }
    }
    return null;
  }

  private Node findCollapsedVariable(Node node) {
    for (Node child = node; child != null; child = child.getPrevious()) {
      if (child instanceof CollapseVariableDeclarations.Collapse) {
        return ((CollapseVariableDeclarations.Collapse) child).startNode;
      }
    }
    return null;
  }

  private Node findCollapsedAssign(Node root, String name) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child instanceof CollapseVariableDeclarations.Collapse) {
        Node parent = ((CollapseVariableDeclarations.Collapse) child).parent;
        for (Node grandchild = parent; grandchild != null; grandchild = grandchild.getPrevious()) {
          if (grandchild instanceof Node.Assign) {
            Node lhs = grandchild.getFirstChild();
            if (lhs.isName() && lhs.getString().equals(name)) {
              return grandchild;
            }
          }
        }
      }
    }
    return null;
  }

  private Node findCollapsedReturn(Node root) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child instanceof CollapseVariableDeclarations.Collapse) {
        Node parent = ((CollapseVariableDeclarations.Collapse) child).parent;
        for (Node grandchild = parent; grandchild != null; grandchild = grandchild.getNext()) {
          if (grandchild instanceof Node.Return) {
            return grandchild;
          }
        }
      }
    }
    return null;
  }

  private Node findCollapsedCase(Node root, String value) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child instanceof Node.SwitchCase) {
        Node data = child.getFirstChild();
        if (data != null && data.isNumericLiteral() && data.getIntValue() == Integer.parseInt(value)) {
          return child;
        }
      }
    }
    return null;
  }
}