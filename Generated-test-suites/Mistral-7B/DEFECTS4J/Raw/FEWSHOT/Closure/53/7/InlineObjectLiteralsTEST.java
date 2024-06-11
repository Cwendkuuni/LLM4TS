package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

public class InlineObjectLiteralsTest {

  private static class TestCompiler extends AbstractCompiler {

    @Override
    protected List<CompilerPass> getPasses() {
      return Lists.newArrayList(new InlineObjectLiterals(this, new TestNameIdSupplier()));
    }

    @Override
    protected void initialize() throws Exception {
      super.initialize();
    }
  }

  private static class TestNameIdSupplier implements Supplier<String> {

    private int counter = 0;

    @Override
    public String get() {
      return Integer.toString(counter++);
    }
  }

  @Test
  public void testInlineObjectLiterals() {
    String sourceCode = "function test() {" +
      "  var obj = {" +
      "    a: 1," +
      "    b: function() {" +
      "      return 2;" +
      "    }," +
      "  };" +
      "  var c = obj.a;" +
      "  var d = obj.b();" +
      "  var x = obj;" +
      "}";
    Node externs = new Node(Token.PROGRAM, null);
    Node root = parser.parse(sourceCode, externs);

    TestCompiler compiler = new TestCompiler();
    compiler.compile(externs, root);

    Scope scope = root.getFirstChild();
    for (Node child = scope.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.VAR) {
        Var var = (Var) child;
        if (var.name.equals("obj")) {
          ReferenceCollection referenceInfo =
              ReferenceCollectingCallback.getReferenceInfo(root, var);
          assertNotNull(referenceInfo);
          List<Reference> references = referenceInfo.references;
          assertEquals(3, references.size());
          assertEquals("a", references.get(0).getName());
          assertEquals("b", references.get(1).getName());
          assertEquals("b", references.get(2).getName());

          // Check that the object was split into individual variables.
          for (Node statement = var.getFirstChild(); statement != null;
               statement = statement.getNext()) {
            if (statement.getType() == Token.VAR) {
              Var varStatement = (Var) statement;
              String varName = varStatement.name;
              if ("a".equals(varName) || "b".equals(varName)) {
                Node assignedValue = varStatement.getAssignedValue();
                assertNotNull(assignedValue);
                assertEquals(Token.UNDEFINED, assignedValue.getType());
              }
            }
          }
          break;
        }
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionExpression() {
    String sourceCode = "function test() {" +
      "  var obj = {" +
      "    a: 1," +
      "    b: function() {" +
      "      return function() {" +
      "        return 2;" +
      "      };" +
      "    }," +
      "  };" +
      "  var c = obj.a;" +
      "  var d = obj.b();" +
      "  var e = obj.b().call(null);" +
      "  var x = obj;" +
      "}";
    Node externs = new Node(Token.PROGRAM, null);
    Node root = parser.parse(sourceCode, externs);

    TestCompiler compiler = new TestCompiler();
    compiler.compile(externs, root);

    Scope scope = root.getFirstChild();
    for (Node child = scope.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.VAR) {
        Var var = (Var) child;
        if (var.name.equals("obj")) {
          ReferenceCollection referenceInfo =
              ReferenceCollectingCallback.getReferenceInfo(root, var);
          assertNotNull(referenceInfo);
          List<Reference> references = referenceInfo.references;
          assertEquals(4, references.size());
          assertEquals("a", references.get(0).getName());
          assertEquals("b", references.get(1).getName());
          assertEquals("b", references.get(2).getName());
          assertEquals("b", references.get(3).getName());

          // Check that the object was split into individual variables.
          for (Node statement = var.getFirstChild(); statement != null;
               statement = statement.getNext()) {
            if (statement.getType() == Token.VAR) {
              Var varStatement = (Var) statement;
              String varName = varStatement.name;
              if ("a".equals(varName) || "b".equals(varName)) {
                Node assignedValue = varStatement.getAssignedValue();
                assertNotNull(assignedValue);
                assertEquals(Token.UNDEFINED, assignedValue.getType());
              }
            }
            if (statement.getType() == Token.FUNCTION) {
              Node functionNode = statement;
              String functionName = functionNode.getFirstChild().getString();
              if ("b".equals(functionName)) {
                Node functionBody = functionNode.getLastChild();
                Node functionCall = new Node(Token.CALL, functionNode);
                Node callExpression = new Node(Token.EXPR_RESULT, functionCall);
                Node statementReplacement = new Node(Token.ASSIGN,
                    Node.newString(Token.NAME, "e"), callExpression);
                Node statementParent = statement.getParent();
                statementParent.replaceChild(statement, statementReplacement);
              }
            }
          }
          break;
        }
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjectLiterals() {
    String sourceCode = "function test() {" +
      "  var obj1 = {" +
      "    a: 1," +
      "    b: {" +
      "      c: 2," +
      "      d: 3" +
      "    }" +
      "  };" +
      "  var obj2 = obj1.b;" +
      "  var c = obj1.b.c;" +
      "  var d = obj1.b.d;" +
      "  var e = obj1.a;" +
      "}";
    Node externs = new Node(Token.PROGRAM, null);
    Node root = parser.parse(sourceCode, externs);

    TestCompiler compiler = new TestCompiler();
    compiler.compile(externs, root);

    Scope scope = root.getFirstChild();
    for (Node child = scope.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.VAR) {
        Var var = (Var) child;
        if (var.name.equals("obj1")) {
          ReferenceCollection referenceInfo =
              ReferenceCollectingCallback.getReferenceInfo(root, var);
          assertNotNull(referenceInfo);
          List<Reference> references = referenceInfo.references;
          assertEquals(7, references.size());
          assertEquals("a", references.get(0).getName());
          assertEquals("b", references.get(1).getName());
          assertEquals("b.c", references.get(2).getName());
          assertEquals("b.d", references.get(3).getName());
          assertEquals("b.c", references.get(4).getName());
          assertEquals("b.d", references.get(5).getName());
          assertEquals("a", references.get(6).getName());

          // Check that the object was split into individual variables.
          for (Node statement = var.getFirstChild(); statement != null;
               statement = statement.getNext()) {
            if (statement.getType() == Token.VAR) {
              Var varStatement = (Var) statement;
              String varName = varStatement.name;
              if ("a".equals(varName) || "b.c".equals(varName) || "b.d".equals(varName)) {
                Node assignedValue = varStatement.getAssignedValue();
                assertNotNull(assignedValue);
                assertEquals(Token.UNDEFINED, assignedValue.getType());
              }
            }
          }
          break;
        }
      }
    }
  }
}