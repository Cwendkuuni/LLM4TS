package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.TernaryValue;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {
  private MinimizeExitPoints minimizeExitPoints;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    compiler = new NoOpCompiler();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testMinimizeExitsLabel() {
    String code = "foo:\n" +
        "for (var i = 0; i < 10; i++) {\n" +
        "  if (i % 2 === 0) break foo;\n" +
        "  console.log(i);\n" +
        "}\n";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertFalse(root.isLabel("foo").getLastChild().hasChildren());
  }

  @Test
  public void testMinimizeExitsFunctionReturn() {
    String code = "function foo() {\n" +
        "  if (x) return;\n" +
        "  console.log('Hello');\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertNull(findReturnStatement(root));
  }

  @Test
  public void testMinimizeExitsFunctionReturnWithExpression() {
    String code = "function foo() {\n" +
        "  return x + 5;\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertSame(findReturnStatement(root), root.getLastChild());
  }

  @Test
  public void testMinimizeExitsIfElse() {
    String code = "function foo() {\n" +
        "  if (x) {\n" +
        "    console.log('True');\n" +
        "  } else {\n" +
        "    console.log('False');\n" +
        "  }\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertNull(findReturnStatement(root));
  }

  @Test
  public void testMinimizeExitsIfElseWithBreak() {
    String code = "function foo() {\n" +
        "  if (x) {\n" +
        "    console.log('True');\n" +
        "    break;\n" +
        "  } else {\n" +
        "    console.log('False');\n" +
        "  }\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertFalse(root.getLastChild().hasChildren());
  }

  @Test
  public void testMinimizeExitsIfElseWithNestedBreak() {
    String code = "function foo() {\n" +
        "  for (var i = 0; i < 2; i++) {\n" +
        "    if (x) {\n" +
        "      console.log('Outer if');\n" +
        "      break;\n" +
        "    } else {\n" +
        "      if (y) {\n" +
        "        console.log('Inner if');\n" +
        "        break;\n" +
        "      } else {\n" +
        "        console.log('Else');\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    Node outerIf = findIfStatement(root);
    Node innerIf = findIfStatement(outerIf);

    assertFalse(outerIf.getLastChild().hasChildren());
    assertFalse(innerIf.getLastChild().hasChildren());
  }

  @Test
  public void testMinimizeExitsTryCatch() {
    String code = "function foo() {\n" +
        "  try {\n" +
        "    throw new Error('Test error');\n" +
        "  } catch (e) {\n" +
        "    console.log(e.message);\n" +
        "    break;\n" +
        "  }\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertFalse(root.getLastChild().hasChildren());
  }

  @Test
  public void testMinimizeExitsTryCatchFinally() {
    String code = "function foo() {\n" +
        "  try {\n" +
        "    throw new Error('Test error');\n" +
        "  } catch (e) {\n" +
        "    console.log(e.message);\n" +
        "  } finally {\n" +
        "    console.log('Finally');\n" +
        "    break;\n" +
        "  }\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertFalse(root.getLastChild().hasChildren());
  }

  @Test
  public void testMinimizeExitsForLoop() {
    String code = "function foo() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (x) break;\n" +
        "    console.log(i);\n" +
        "  }\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertFalse(root.getLastChild().getLastChild().hasChildren());
  }

  @Test
  public void testMinimizeExitsWhileLoop() {
    String code = "function foo() {\n" +
        "  var i = 1;\n" +
        "  while (i < 10) {\n" +
        "    if (x) break;\n" +
        "    console.log(i);\n" +
        "    i++;\n" +
        "  }\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertFalse(root.getLastChild().getLastChild().hasChildren());
  }

  @Test
  public void testMinimizeExitsDoWhileLoop() {
    String code = "function foo() {\n" +
        "  var i = 0;\n" +
        "  do {\n" +
        "    if (x) break;\n" +
        "    console.log(i);\n" +
        "    i++;\n" +
        "  } while (i < 10);\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertFalse(root.getLastChild().getLastChild().hasChildren());
  }

  @Test
  public void testMinimizeExitsNestedIfElse() {
    String code = "function foo() {\n" +
        "  if (x) {\n" +
        "    if (y) return;\n" +
        "    console.log('First if');\n" +
        "  } else {\n" +
        "    console.log('Else');\n" +
        "  }\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertNull(findReturnStatement(root));
  }

  @Test
  public void testMinimizeExitsNestedIfElseWithBreak() {
    String code = "function foo() {\n" +
        "  if (x) {\n" +
        "    if (y) {\n" +
        "      console.log('First if inner');\n" +
        "      break;\n" +
        "    } else {\n" +
        "      console.log('First if else');\n" +
        "    }\n" +
        "  } else {\n" +
        "    console.log('Else');\n" +
        "  }\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertFalse(root.getLastChild().getLastChild().hasChildren());
  }

  @Test
  public void testMinimizeExitsNestedIfElseWithLabel() {
    String code = "foo:\n" +
        "function foo() {\n" +
        "  if (x) {\n" +
        "    if (y) break foo;\n" +
        "    console.log('First if inner');\n" +
        "  } else {\n" +
        "    console.log('Else');\n" +
        "  }\n" +
        "}";

    Node root = parseCode(code);
    minimizeExitPoints.process(null, root);

    assertNull(findReturnStatement(root));
    assertFalse(root.getFirstChild().getLastChild().hasChildren());
  }

  private Node findReturnStatement(Node node) {
    if (node.isReturn()) {
      return node;
    }
    for (Node child : node.children()) {
      Node result = findReturnStatement(child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private Node findIfStatement(Node node) {
    for (Node child : node.children()) {
      if (child.isIf()) {
        return child;
      }
      Node result = findIfStatement(child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private Node parseCode(String code) {
    return compiler.parseJavaScript(code, null, null);
  }
}

class NoOpCompiler implements AbstractCompiler {
  @Override
  public Node parseJavaScript(String jsSourceCode, SourceCode sourceCode,
      Node parent) {
    return IR.parse(jsSourceCode, sourceCode, parent);
  }

  @Override
  public void reportCodeChange(Node oldNode, int start, int end) {}

  @Override
  public void reportCodeChange(Node oldNode, Node newNode) {}

  @Override
  public void reportCodeChange(Node oldNode, Node newNode, int offset) {}

  @Override
  public Node getTopLevelDeclaration(String name) {
    return null;
  }

  @Override
  public void reportTypeError(Node node, String message) {}

  @Override
  public void reportSourceError(String message) {}

  @Override
  public void reportWarning(String message, Node node) {}

  @Override
  public void reportWarning(String message) {}

  @Override
  public void setOptions(CompilerOptions options) {}
}