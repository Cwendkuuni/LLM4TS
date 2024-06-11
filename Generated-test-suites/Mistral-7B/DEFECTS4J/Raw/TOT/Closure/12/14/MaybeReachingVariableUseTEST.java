import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleVariableUse() {
    String jsCode = "function foo() {"
        + "var a = 1;"
        + "var b = a;"
        + "return a;}"
        + "function bar() {"
        + "return foo();}"
        + "var x = bar();";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);

    Node rootNode = compiler.parseAndCompile(jsCode, null, null).getRootNode();
    analysis.compute();

    Collection<Node> uses = analysis.getUses("a", rootNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(rootNode.getLastChild(), uses.iterator().next());
  }

  @Test
  public void testNoUseVariable() {
    String jsCode = "function foo() {"
        + "var a = 1;"
        + "return a;"
        + "}"
        + "function bar() {"
        + "return foo();}"
        + "var x = 2;";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);

    Node rootNode = compiler.parseAndCompile(jsCode, null, null).getRootNode();
    analysis.compute();

    Collection<Node> uses = analysis.getUses("a", rootNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableEscaped() {
    String jsCode = "function foo() {"
        + "var a = 1;"
        + "function bar() {"
        + "return a;"
        + "}"
        + "var b = bar;"
        + "return a;}"
        + "function main() {"
        + "return foo();}"
        + "main();";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);

    Node rootNode = compiler.parseAndCompile(jsCode, null, null).getRootNode();
    analysis.compute();

    Collection<Node> uses = analysis.getUses("a", rootNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableUnused() {
    String jsCode = "function foo() {"
        + "var a = 1;"
        + "}"
        + "function bar() {"
        + "return foo();}"
        + "function main() {"
        + "return foo();"
        + "var a = 2;"
        + "}"
        + "main();";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);

    Node rootNode = compiler.parseAndCompile(jsCode, null, null).getRootNode();
    analysis.compute();

    Collection<Node> uses = analysis.getUses("a", rootNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableDeadCode() {
    String jsCode = "function foo() {"
        + "var a = 1;"
        + "if (true) {"
        + " return;"
        + "}"
        + "return a;}"
        + "function main() {"
        + "return foo();"
        + "var a = 2;"
        + "}"
        + "main();";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);

    Node rootNode = compiler.parseAndCompile(jsCode, null, null).getRootNode();
    analysis.compute();

    Collection<Node> uses = analysis.getUses("a", rootNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableDefinedAfterUse() {
    String jsCode = "function foo() {"
        + "return 1;"
        + "var a = 1;"
        + "}"
        + "function main() {"
        + "var a = 2;"
        + "return foo();}"
        + "main();";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);

    Node rootNode = compiler.parseAndCompile(jsCode, null, null).getRootNode();
    analysis.compute();

    Collection<Node> uses = analysis.getUses("a", rootNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(rootNode.getFirstChild(), uses.iterator().next());
  }

  @Test
  public void testVariableDefinedBeforeUse() {
    String jsCode = "function foo() {"
        + "var a = 1;"
        + "return a;"
        + "}"
        + "function main() {"
        + "return foo();"
        + "var a = 2;"
        + "}"
        + "main();";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);

    Node rootNode = compiler.parseAndCompile(jsCode, null, null).getRootNode();
    analysis.compute();

    Collection<Node> uses = analysis.getUses("a", rootNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableWithConditionalUse() {
    String jsCode = "function foo() {"
        + "var a = 1;"
        + "if (true) {"
        + " return a;"
        + "}"
        + "}"
        + "function main() {"
        + "var a = 2;"
        + "if (false) {"
        + " return foo();"
        + "}"
        + "}"
        + "main();";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);

    Node rootNode = compiler.parseAndCompile(jsCode, null, null).getRootNode();
    analysis.compute();

    Collection<Node> uses = analysis.getUses("a", rootNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(rootNode.getFirstChild(), uses.iterator().next());
  }

  @Test
  public void testVariableWithFunctionCallUse() {
    String jsCode = "function foo(a) {"
        + "return a;"
        + "}"
        + "function main() {"
        + "return foo(1);"
        + "}"
        + "main();";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);

    Node rootNode = compiler.parseAndCompile(jsCode, null, null).getRootNode();
    analysis.compute();

    Collection<Node> uses = analysis.getUses("a", rootNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(rootNode.getFirstChild(), uses.iterator().next());
  }

  @Test
  public void testVariableWithForLoopUse() {
    String jsCode = "function foo(a) {"
        + "return a;"
        + "}"
        + "function main() {"
        + "for (var i = 0; i < 10; i++) {"
        + " return foo(i);"
        + "}"
        + "}"
        + "main();";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);

    Node rootNode = compiler.parseAndCompile(jsCode, null, null).getRootNode();
    analysis.compute();

    Collection<Node> uses = analysis.getUses("a", rootNode);
    assertNotNull(uses);
    assertEquals(10, uses.size());
  }

  @Test
  public void testVariableWithWhileLoopUse() {
    String jsCode = "function foo(a) {"
        + "return a;"
        + "}"
        + "function main() {"
        + "var i = 0;"
        + "while (true) {"
        + " if (i > 10) {"
        + " break;"
        + "}"
        + " return foo(i);"
        + " i++;"
        + "}"
        + "}"
        + "main();";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        cfg, jsScope, compiler);

    Node rootNode = compiler.parseAndCompile(jsCode, null, null).getRootNode();
    analysis.compute();

    Collection<Node> uses = analysis.getUses("a", rootNode);
    assertNotNull(uses);
    assertTrue(uses.size() > 10);
  }
}