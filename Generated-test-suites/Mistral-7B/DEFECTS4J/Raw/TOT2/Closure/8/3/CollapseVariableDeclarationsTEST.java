import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.Set;

public class CollapseVariableDeclarationsTest {

  @org.junit.Test
  public void testCollapseVariableDeclarations() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    String testCode =
        "function foo() {"
            + "var a;"
            + "var b = 1;"
            + "var c = 2;"
            + "var d = 3;"
            + "var e = 4;"
            + "var a, b = 1, c = 2;"
            + "var x, y = 1, z = 2;"
            + "}"
            + "function bar() {"
            + "var a = 1;"
            + "var a = 2;"
            + "}"
            + "function baz() {"
            + "var a = 1;"
            + "var a = 2;"
            + "var a = 3;"
            + "}"
            + "function qux() {"
            + "var x = 1;"
            + "var x = 2;"
            + "var x = 3;"
            + "}"
            + "function quux() {"
            + "var a = 1;"
            + "var b = 1;"
            + "var a = 2;"
            + "}";

    Node root = AbstractCompiler.parseSingleFile(compiler, options, testCode);

    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(CompilerOpt.Opt.empty(), root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);

    assertEquals("function foo() {"
        + "var a, b = 1, c = 2;"
        + "}", nodeTraversal.print(root).getCode());

    Node functionNode = root.lookupByName("foo");
    Scope functionScope = functionNode.getFunctionScope();

    Set<String> variables = Sets.newHashSet();
    for (String variableName : functionNode.getChildNodes().stream()
            .map(Node::toString)
            .map(name -> name.substring(1, name.length() - 1))
            .collect(Collectors.toList())) {
      Var variable = functionScope.getVar(variableName);
      variables.add(variableName + " = " + variable.getInitialValue());
    }

    assertTrue(variables.containsAll(Arrays.asList("a = null", "b = 1", "c = 2")));

    Node functionBar = root.lookupByName("bar");
    assertNull(functionBar);

    Node functionBaz = root.lookupByName("baz");
    assertFalse(functionBaz.isFunctionDeclaration());

    Node functionQux = root.lookupByName("qux");
    assertNull(functionQux);

    Node functionQuux = root.lookupByName("quux");
    assertFalse(functionQuux.isFunctionDeclaration());

    Node assignA = functionQuux.getChild(1).getFirstChild();
    assertFalse(assignA.isName());

    Node assignB = functionQuux.getChild(2).getFirstChild();
    assertTrue(assignB.isName());
  }

  @org.junit.Test
  public void testCollapsingMultipleAssignments() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    String testCode =
        "function foo() {"
            + "var a = 1;"
            + "var a = 2;"
            + "var a = 3;"
            + "}"
            + "function bar() {"
            + "var a = 1;"
            + "var a = 2;"
            + "var a = 3;"
            + "var a = 4;"
            + "}"
            + "function baz() {"
            + "var a = 1;"
            + "var a = 2;"
            + "var a = 3;"
            + "var a = 4;"
            + "var a = 5;"
            + "}"
            + "function qux() {"
            + "var x = 1;"
            + "var x = 2;"
            + "var x = 3;"
            + "var x = 4;"
            + "}"
            + "function quux() {"
            + "var a = 1;"
            + "var b = 1;"
            + "var a = 2;"
            + "}";

    Node root = AbstractCompiler.parseSingleFile(compiler, options, testCode);

    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(CompilerOpt.Opt.empty(), root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);

    assertEquals("function foo() {"
        + "var a = 3;"
        + "}", nodeTraversal.print(root.getChild(0)).getCode());

    assertEquals("function bar() {"
        + "var a = 4;"
        + "}", nodeTraversal.print(root.getChild(1)).getCode());

    assertEquals("function baz() {"
        + "var a = 5;"
        + "}", nodeTraversal.print(root.getChild(2)).getCode());

    assertEquals("function qux() {"
        + "var x = 4;"
        + "}", nodeTraversal.print(root.getChild(3)).getCode());
  }

  @org.junit.Test
  public void testCollapsingAssignmentsAndDeclarations() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    String testCode =
        "function foo() {"
            + "var a = 1;"
            + "var a = 2;"
            + "var a = 3;"
            + "var a = 4;"
            + "}"
            + "function bar() {"
            + "var a = 1;"
            + "var a = 2;"
            + "var a = 3;"
            + "var a = 4;"
            + "var a = 5;"
            + "}"
            + "function baz() {"
            + "var a = 1;"
            + "var a = 2;"
            + "var a = 3;"
            + "var a = 4;"
            + "var a = 5;"
            + "var a = 6;"
            + "}"
            + "function qux() {"
            + "var x = 1;"
            + "var x = 2;"
            + "var x = 3;"
            + "var x = 4;"
            + "var x = 5;"
            + "}"
            + "function quux() {"
            + "var a = 1;"
            + "var b = 1;"
            + "var a = 2;"
            + "}";

    Node root = AbstractCompiler.parseSingleFile(compiler, options, testCode);

    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(CompilerOpt.Opt.empty(), root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);

    assertEquals("function foo() {"
        + "var a = 4;"
        + "}", nodeTraversal.print(root.getChild(0)).getCode());

    assertEquals("function bar() {"
        + "var a = 5;"
        + "}", nodeTraversal.print(root.getChild(1)).getCode());

    assertEquals("function baz() {"
        + "var a = 6;"
        + "}", nodeTraversal.print(root.getChild(2)).getCode());

    assertEquals("function qux() {"
        + "var x = 5;"
        + "}", nodeTraversal.print(root.getChild(3)).getCode());
  }
}