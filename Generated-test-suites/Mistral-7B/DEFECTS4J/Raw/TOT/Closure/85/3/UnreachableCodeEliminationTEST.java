import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.GLOBAL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class UnreachableCodeEliminationTest {

  @Test
  public void testSimpleUnreachableCode() {
    String jsCode = "function foo() {"
        + "if (true) {"
        + "  return;"
        + "  alert('unreachable');"
        + "}"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.QUIET);

    Node rootNode = NodeFactory.compile(jsCode, null, null, options);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, GLOBAL);
    traversal.traverse();

    assertTrue(compiler.getDiagnosticMessages().isEmpty());
    assertTrue(eliminator.cfgStack.isEmpty());
    Node result = eliminator.compiler.getOutputNode();
    assertFalse(result.hasChild(NodeUtil.findByIdent("alert")));
  }

  @Test
  public void testConditionalUnreachableCode() {
    String jsCode = "function foo() {"
        + "if (true) {"
        + "  if (false) {"
        + "    return;"
        + "  }"
        + "}"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.QUIET);

    Node rootNode = NodeFactory.compile(jsCode, null, null, options);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, GLOBAL);
    traversal.traverse();

    assertTrue(compiler.getDiagnosticMessages().isEmpty());
    assertTrue(eliminator.cfgStack.isEmpty());
    Node result = eliminator.compiler.getOutputNode();
    assertFalse(result.hasChild(NodeUtil.findByIdent("return")));
  }

  @Test
  public void testUnreachableNoOpStatement() {
    String jsCode = "function foo() {"
        + "var x = 42;"
        + "true;"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.QUIET);

    Node rootNode = NodeFactory.compile(jsCode, null, null, options);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, GLOBAL);
    traversal.traverse();

    assertTrue(compiler.getDiagnosticMessages().isEmpty());
    assertTrue(eliminator.cfgStack.isEmpty());
    Node result = eliminator.compiler.getOutputNode();
    assertFalse(result.hasChild(NodeUtil.findByIdent("true")));
  }

  @Test
  public void testConditionalUnreachableNoOpStatement() {
    String jsCode = "function foo() {"
        + "if (true) {"
        + "  var x = 42;"
        + "  true;"
        + "}"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.QUIET);

    Node rootNode = NodeFactory.compile(jsCode, null, null, options);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, GLOBAL);
    traversal.traverse();

    assertTrue(compiler.getDiagnosticMessages().isEmpty());
    assertTrue(eliminator.cfgStack.isEmpty());
    Node result = eliminator.compiler.getOutputNode();
    assertFalse(result.hasChild(NodeUtil.findByIdent("true")));
  }

  @Test
  public void testUnreachableCodeWithSideEffect() {
    String jsCode = "function foo() {"
        + "if (true) {"
        + "  return;"
        + "}"
        + "document.body.style.color = 'red';"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.QUIET);

    Node rootNode = NodeFactory.compile(jsCode, null, null, options);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, GLOBAL);
    traversal.traverse();

    assertTrue(compiler.getDiagnosticMessages().isEmpty());
    assertTrue(eliminator.cfgStack.isEmpty());
    Node result = eliminator.compiler.getOutputNode();
    assertFalse(result.hasChild(NodeUtil.findByIdent("document.body.style.color")));
  }

  @Test
  public void testUnreachableCodeWithSideEffectInConditional() {
    String jsCode = "function foo() {"
        + "if (true) {"
        + "  if (true) {"
        + "    return;"
        + "  }"
        + "}"
        + "document.body.style.color = 'red';"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.QUIET);

    Node rootNode = NodeFactory.compile(jsCode, null, null, options);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, GLOBAL);
    traversal.traverse();

    assertTrue(compiler.getDiagnosticMessages().isEmpty());
    assertTrue(eliminator.cfgStack.isEmpty());
    Node result = eliminator.compiler.getOutputNode();
    assertFalse(result.hasChild(NodeUtil.findByIdent("document.body.style.color")));
  }

  @Test
  public void testUnreachableCodeWithSideEffectInLoop() {
    String jsCode = "function foo() {"
        + "for (var i = 0; i < 10; ++i) {"
        + "  if (i == 5) {"
        + "    return;"
        + "  }"
        + "}"
        + "document.body.style.color = 'red';"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.QUIET);

    Node rootNode = NodeFactory.compile(jsCode, null, null, options);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, GLOBAL);
    traversal.traverse();

    assertTrue(compiler.getDiagnosticMessages().isEmpty());
    assertTrue(eliminator.cfgStack.isEmpty());
    Node result = eliminator.compiler.getOutputNode();
    assertFalse(result.hasChild(NodeUtil.findByIdent("document.body.style.color")));
  }

  @Test
  public void testUnreachableCodeWithSideEffectInConditionalLoop() {
    String jsCode = "function foo() {"
        + "for (var i = 0; i < 10; ++i) {"
        + "  if (i == 5) {"
        + "    if (true) {"
        + "      return;"
        + "    }"
        + "  }"
        + "}"
        + "document.body.style.color = 'red';"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.QUIET);

    Node rootNode = NodeFactory.compile(jsCode, null, null, options);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, GLOBAL);
    traversal.traverse();

    assertTrue(compiler.getDiagnosticMessages().isEmpty());
    assertTrue(eliminator.cfgStack.isEmpty());
    Node result = eliminator.compiler.getOutputNode();
    assertFalse(result.hasChild(NodeUtil.findByIdent("document.body.style.color")));
  }

  @Test
  public void testUnreachableCodeInSwitchStatement() {
    String jsCode = "function foo() {"
        + "switch (1) {"
        + "  case 1:"
        + "    return;"
        + "  case 2:"
        + "    break;"
        + "  default:"
        + "}"
        + "document.body.style.color = 'red';"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.QUIET);

    Node rootNode = NodeFactory.compile(jsCode, null, null, options);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, GLOBAL);
    traversal.traverse();

    assertTrue(compiler.getDiagnosticMessages().isEmpty());
    assertTrue(eliminator.cfgStack.isEmpty());
    Node result = eliminator.compiler.getOutputNode();
    assertFalse(result.hasChild(NodeUtil.findByIdent("document.body.style.color")));
  }

  @Test
  public void testUnreachableCodeWithSideEffectInSwitchStatement() {
    String jsCode = "function foo() {"
        + "switch (1) {"
        + "  case 1:"
        + "    if (true) {"
        + "      return;"
        + "    }"
        + "    document.body.style.color = 'red';"
        + "  case 2:"
        + "    break;"
        + "  default:"
        + "}"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.QUIET);

    Node rootNode = NodeFactory.compile(jsCode, null, null, options);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(rootNode, eliminator, GLOBAL);
    traversal.traverse();

    assertTrue(compiler.getDiagnosticMessages().isEmpty());
    assertTrue(eliminator.cfgStack.isEmpty());
    Node result = eliminator.compiler.getOutputNode();
    assertFalse(result.hasChild(NodeUtil.findByIdent("document.body.style.color")));
  }
}