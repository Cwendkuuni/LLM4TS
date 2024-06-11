package com.google.javascript.jscomp;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior.AFTER_SCOPE_EXIT;
import static com.google.javascript.jscomp. ReferenceCollectingCallback.Behavior.BEFORE_SCOPE_ENTRY;
import static com.google.javascript.rhino.NodeFactory.createObject;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createVar;
import static com.google.javascript.rhino.NodeFactory.createVarDecl;
import static com.google.javascript.rhino.TokenType.*;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiteralsWithSimpleObject() {
    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tmp_");

    String sourceCode = "function f() {" +
        "var x = {a: 1, b: 2};" +
        "var y = x.a; // Use a value from the object." +
        "}" +
        "f();";

    Node root = ParserUtil.parse(sourceCode);

    inlineObjectLiterals.process(null, root);

    // Check the inlined variables have been recognized and expanded.
    NodeTraversal t = new NodeTraversal(root);
    Node f = t.findFunction("f", null);

    // Check x has been replaced with inline variables.
    Var x = t.getScope().getVar("x");
    Node xAssignments = (Node) x.getInitialization();
    List<Node> xAssignmentsList = NodeUtil.asList(xAssignments);
    for (int i = 0; i < xAssignmentsList.size(); i++) {
      Node assignment = xAssignmentsList.get(i);
      Node lhs = assignment.getFirstChild();
      Node rhs = assignment.getLastChild();
      String varName = VAR_PREFIX + lhs.getString() + "_";
      assertThat(lhs.getString()).isEqualTo(varName + "a");
      assertThat(rhs.isNumberLiteral()).isTrue();
      assertThat(rhs.getInt()).isEqualTo(1);
    }

    // Check y has been updated to reference the inline variables.
    Var y = t.getScope().getVar("y");
    Node yGetProp = (Node) y.getInitialization();
    Node yGetPropTarget = yGetProp.getFirstChild();
    Node yGetPropProperty = yGetProp.getLastChild();
    String varNameA = VAR_PREFIX + "xa_";
    String varNameB = VAR_PREFIX + "xb_";
    assertThat(yGetPropTarget.getString()).isEqualTo(varNameA);
    assertThat(yGetPropProperty.getString()).isEqualTo("a");

    // Check no unused variables remain.
    Set<String> unusedVariables = Sets.newHashSet();
    t.traverse(new NodeUtil.Visitor<Void, Void>() {
      @Override
      public Void visitVar(Node node) {
        Var var = node.getFirstChild();
        if (var.isGlobal()) {
          unusedVariables.add(var.name);
        }
        return null;
      }
    });
    assertThat(unusedVariables).isEmpty();
  }

  @Test
  public void testInlineObjectLiteralsWithComplexObject() {
    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tmp_");

    String sourceCode = "function f() {" +
        "var x = {a: {b: 1}, c: {d: 2}};" +
        "var y = x.a.b; // Use a value from the object." +
        "}" +
        "f();";

    Node root = ParserUtil.parse(sourceCode);

    inlineObjectLiterals.process(null, root);

    // Check the inlined variables have been recognized and expanded.
    NodeTraversal t = new NodeTraversal(root);
    Node f = t.findFunction("f", null);

    // Check x has been replaced with inline variables.
    Var x = t.getScope().getVar("x");
    Node xAssignments = (Node) x.getInitialization();
    List<Node> xAssignmentsList = NodeUtil.asList(xAssignments);

    // Check a.b has also been expanded.
    Var nestedVar = t.getScope().getVar("nestedVar");
    Node nestedVarAssignments = (Node) nestedVar.getInitialization();
    List<Node> nestedVarAssignmentsList = NodeUtil.asList(nestedVarAssignments);

    // Check xAssignments
    for (int i = 0; i < xAssignmentsList.size(); i++) {
      Node assignment = xAssignmentsList.get(i);
      Node lhs = assignment.getFirstChild();
      Node rhs = assignment.getLastChild();
      String varName = null;
      if (lhs.isGetProp()) {
        lhs = lhs.getFirstChild();
        varName = VAR_PREFIX + lhs.getString() + "_";
      } else {
        varName = VAR_PREFIX + lhs.getString() + "_";
      }

      if (lhs.getString().equals("a")) {
        Node aAssignments = (Node) lhs.getNextSibling();
        List<Node> aAssignmentsList = NodeUtil.asList(aAssignments);
        for (int j = 0; j < aAssignmentsList.size(); j++) {
          Node innerAssgn = aAssignmentsList.get(j);
          Node innerLhs = innerAssgn.getFirstChild();
          Node innerRhs = innerAssgn.getLastChild();
          String innerVarName = null;
          if (innerLhs.isGetProp()) {
            innerLhs = innerLhs.getFirstChild();
            innerVarName = VAR_PREFIX + innerLhs.getString() + "_";
          } else {
            innerVarName = VAR_PREFIX + innerLhs.getString() + "_";
          }

          if (innerLhs.getString().equals("b")) {
            assertThat(lhs.getString()).isEqualTo(varName + "a_");
            assertThat(innerVarName).isEqualTo(VAR_PREFIX + "aa_");
            assertThat(rhs.isObjectLit()).isFalse();
            assertThat(rhs.isNumberLiteral()).isTrue();
            assertThat(rhs.getInt()).isEqualTo(1);
            break;
          }
        }
      }

      if (lhs.getString().equals("c")) {
        Node cAssignments = (Node) lhs.getNextSibling();
        List<Node> cAssignmentsList = NodeUtil.asList(cAssignments);
        for (int j = 0; j < cAssignmentsList.size(); j++) {
          Node innerAssgn = cAssignmentsList.get(j);
          Node innerLhs = innerAssgn.getFirstChild();
          Node innerRhs = innerAssgn.getLastChild();
          String innerVarName = null;
          if (innerLhs.isGetProp()) {
            innerLhs = innerLhs.getFirstChild();
            innerVarName = VAR_PREFIX + innerLhs.getString() + "_";
          } else {
            innerVarName = VAR_PREFIX + innerLhs.getString() + "_";
          }

          if (innerLhs.getString().equals("d")) {
            assertThat(lhs.getString()).isEqualTo(varName + "c_");
            assertThat(innerVarName).isEqualTo(VAR_PREFIX + "ac_");
            assertThat(rhs.isObjectLit()).isFalse();
            assertThat(rhs.isNumberLiteral()).isTrue();
            assertThat(rhs.getInt()).isEqualTo(2);
            break;
          }
        }
      }
    }

    // Check y has been updated to reference the inline variables.
    Var y = t.getScope().getVar("y");
    Node yGetProp = (Node) y.getInitialization();
    Node yGetPropTarget = yGetProp.getFirstChild();
    Node yGetPropProperty = yGetProp.getLastChild();
    String varNameA = VAR_PREFIX + "aa_";
    String varNameC = VAR_PREFIX + "ac_";

    assertThat(yGetPropTarget.getString()).isEqualTo(varNameA);
    assertThat(yGetPropProperty.getString()).isEqualTo("b");

    // Check no unused variables remain.
    Set<String> unusedVariables = Sets.newHashSet();
    t.traverse(new NodeUtil.Visitor<Void, Void>() {
      @Override
      public Void visitVar(Node node) {
        Var var = node.getFirstChild();
        if (var.isGlobal()) {
          unusedVariables.add(var.name);
        }
        return null;
      }
    });
    assertThat(unusedVariables).isEmpty();
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReferentialObjectLiterals() {
    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tmp_");

    String sourceCode = "function f() {" +
        "var x = {" +
        "  a: {b: 1, c: x}" +
        "};" +
        "}" +
        "f();";

    Node root = ParserUtil.parse(sourceCode);

    inlineObjectLiterals.process(null, root);

    // Check x has not been replaced with inline variables due to self-referential object literals.
    NodeTraversal t = new NodeTraversal(root);
    Node f = t.findFunction("f", null);

    Var x = t.getScope().getVar("x");
    Node xAssignments = (Node) x.getInitialization();
    List<Node> xAssignmentsList = NodeUtil.asList(xAssignments);

    for (Node assignment : xAssignmentsList) {
      Node lhs = assignment.getFirstChild();
      Node rhs = assignment.getLastChild();
      String varName = VAR_PREFIX + lhs.getString() + "_";

      if (lhs.getString().equals("x")) {
        // Object literals containing self-referential properties cannot be inlined.
        assertThat(lhs.getString()).isEqualTo(varName + "x_");
        assertThat(rhs.isObjectLit()).isTrue();
        break;
      }
    }

    // Check no unused variables remain.
    Set<String> unusedVariables = Sets.newHashSet();
    t.traverse(new NodeUtil.Visitor<Void, Void>() {
      @Override
      public Void visitVar(Node node) {
        Var var = node.getFirstChild();
        if (var.isGlobal()) {
          unusedVariables.add(var.name);
        }
        return null;
      }
    });
    assertThat(unusedVariables).isEmpty();
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObjectLiterals() {
    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tmp_");

    String sourceCode = "function f() {" +
        "var x = {};" + // Empty object literal
        "}" +
        "f();";

    Node root = ParserUtil.parse(sourceCode);

    inlineObjectLiterals.process(null, root);

    // Check x has not been replaced with inline variables since empty object literals are not expanded.
    NodeTraversal t = new NodeTraversal(root);
    Node f = t.findFunction("f", null);

    Var x = t.getScope().getVar("x");
    Node xAssignments = (Node) x.getInitialization();
    List<Node> xAssignmentsList = NodeUtil.asList(xAssignments);

    for (Node assignment : xAssignmentsList) {
      Node lhs = assignment.getFirstChild();
      String varName = VAR_PREFIX + lhs.getString() + "_";

      if (lhs.getString().equals("x")) {
        assertThat(lhs.getString()).isEqualTo(varName + "x_");
        assertThat(rhs.isUndefined()).isTrue();
        break;
      }
    }

    // Check no unused variables remain.
    Set<String> unusedVariables = Sets.newHashSet();
    t.traverse(new NodeUtil.Visitor<Void, Void>() {
      @Override
      public Void visitVar(Node node) {
        Var var = node.getFirstChild();
        if (var.isGlobal()) {
          unusedVariables.add(var.name);
        }
        return null;
      }
    });
    assertThat(unusedVariables).isEmpty();
  }

  @Test
  public void testInlineObjectLiteralsWithInvalidObjectLiterals() {
    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tmp_");

    String sourceCode = "function f() {" +
        "var x = {a: 1, b: function() {}};" + // Object literal containing a function.
        "}" +
        "f();";

    Node root = ParserUtil.parse(sourceCode);

    inlineObjectLiterals.process(null, root);

    // Check x has not been replaced with inline variables since object literals containing functions are not expanded.
    NodeTraversal t = new NodeTraversal(root);
    Node f = t.findFunction("f", null);

    Var x = t.getScope().getVar("x");
    Node xAssignments = (Node) x.getInitialization();
    List<Node> xAssignmentsList = NodeUtil.asList(xAssignments);

    for (Node assignment : xAssignmentsList) {
      Node lhs = assignment.getFirstChild();
      String varName = VAR_PREFIX + lhs.getString() + "_";

      if (lhs.getString().equals("x")) {
        assertThat(lhs.getString()).isEqualTo(varName + "x_");
        assertThat(rhs.isObjectLit()).isTrue();
        break;
      }
    }

    // Check no unused variables remain.
    Set<String> unusedVariables = Sets.newHashSet();
    t.traverse(new NodeUtil.Visitor<Void, Void>() {
      @Override
      public Void visitVar(Node node) {
        Var var = node.getFirstChild();
        if (var.isGlobal()) {
          unusedVariables.add(var.name);
        }
        return null;
      }
    });
    assertThat(unusedVariables).isEmpty();
  }
}