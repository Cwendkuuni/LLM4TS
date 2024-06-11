import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.ScopedCallback.*;
import static com.google.javascript.rhino.Token.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DiGraphEdge;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {
  @Test
  public void testUnreachableCodeElimination() {
    // Basic test case: Remove dead code following a return statement.
    String sourceCode = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  alert('unreachable');" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.compile(sourceCode);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  if (true) {" +
        "    return;" +
        "  }" +
        "}", root.toString());

    // Edge case: Dead code in try-catch block.
    sourceCode = "function foo() {" +
        "  try {" +
        "    throw new Error('foo');" +
        "  } catch (e) {" +
        "    alert('catch');" +
        "  }" +
        "  alert('unreachable');" +
        "}";

    root = NodeFactory.compile(sourceCode);
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  try {" +
        "    throw new Error('foo');" +
        "  } catch (e) {" +
        "    alert('catch');" +
        "  }" +
        "}", root.toString());

    // Error scenario: Dead code in for loop.
    sourceCode = "function foo() {" +
        "  for (int i = 0; i < 10; i++) {" +
        "    if (i == 5) {" +
        "      return;" +
        "    }" +
        "    alert('unreachable');" +
        "  }" +
        "}" +
        "foo();";

    root = NodeFactory.compile(sourceCode);
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  for (int i = 0; i < 10; i++) {" +
        "    if (i == 5) {" +
        "      return;" +
        "    }" +
        "  }" +
        "}", root.toString());

    // Error scenario: Dead code in while loop.
    sourceCode = "function foo() {" +
        "  int i = 0;" +
        "  while (true) {" +
        "    if (i > 10) {" +
        "      return;" +
        "    }" +
        "    alert('unreachable');" +
        "    i++;" +
        "  }" +
        "}" +
        "foo();";

    root = NodeFactory.compile(sourceCode);
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  int i = 0;" +
        "  while (true) {" +
        "    if (i > 10) {" +
        "      return;" +
        "    }" +
        "    i++;" +
        "  }" +
        "}", root.toString());
  }

  @Test
  public void testRemoveNoOpStatements() {
    // Basic test case: Remove dead code with no side effects.
    String sourceCode = "function foo() {" +
        "  alert('before');" +
        "  a.b.MyClass.prototype.propertyName;" +
        "  alert('after');" +
        "}" +
        "foo();";

    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.compile(sourceCode);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  alert('before');" +
        "  alert('after');" +
        "}", root.toString());

    // Edge case: Dead code in try-catch block with no side effects.
    sourceCode = "function foo() {" +
        "  try {" +
        "    throw new Error('foo');" +
        "  } catch (e) {" +
        "    a.b.MyClass.prototype.propertyName;" +
        "  }" +
        "}" +
        "foo();";

    root = NodeFactory.compile(sourceCode);
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  try {" +
        "    throw new Error('foo');" +
        "  } catch (e) {" +
        "  }" +
        "}", root.toString());

    // Error scenario: Dead code in for loop with no side effects.
    sourceCode = "function foo() {" +
        "  for (int i = 0; i < 10; i++) {" +
        "    if (i == 5) {" +
        "      return;" +
        "    }" +
        "    a.b.MyClass.prototype.propertyName;" +
        "  }" +
        "}" +
        "foo();";

    root = NodeFactory.compile(sourceCode);
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  for (int i = 0; i < 10; i++) {" +
        "    if (i == 5) {" +
        "      return;" +
        "    }" +
        "  }" +
        "}", root.toString());

    // Error scenario: Dead code in while loop with no side effects.
    sourceCode = "function foo() {" +
        "  int i = 0;" +
        "  while (true) {" +
        "    if (i > 10) {" +
        "      return;" +
        "    }" +
        "    a.b.MyClass.prototype.propertyName;" +
        "    i++;" +
        "  }" +
        "}" +
        "foo();";

    root = NodeFactory.compile(sourceCode);
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  int i = 0;" +
        "  while (true) {" +
        "    if (i > 10) {" +
        "      return;" +
        "    }" +
        "    i++;" +
        "  }" +
        "}", root.toString());
  }

  @Test
  public void testRemoveUnconditionalBranching() {
    // Basic test case: Remove unconditional branching that targets the same node.
    String sourceCode = "function foo() {" +
        "  if (true) {" +
        "    break;" +
        "  }" +
        "  if (true) {" +
        "    break;" +
        "  }" +
        "  alert('unreachable');" +
        "}" +
        "foo();";

    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.compile(sourceCode);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  if (true) {" +
        "    break;" +
        "  }" +
        "  if (true) {" +
        "    break;" +
        "  }" +
        "}", root.toString());

    root = NodeFactory.compile(sourceCode);
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  if (true) {" +
        "    break;" +
        "  }" +
        "}", root.toString());

    // Edge case: Remove unconditional branching that targets a different unconditional branch.
    sourceCode = "function foo() {" +
        "  if (true) {" +
        "    break;" +
        "  }" +
        "  if (true) {" +
        "    break;" +
        "  } else {" +
        "    break;" +
        "  }" +
        "  alert('unreachable');" +
        "}" +
        "foo();";

    root = NodeFactory.compile(sourceCode);
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  if (true) {" +
        "    break;" +
        "  }" +
        "  if (true) {" +
        "    break;" +
        "  }" +
        "}", root.toString());

    root = NodeFactory.compile(sourceCode);
    elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  if (true) {" +
        "    break;" +
        "  }" +
        "}", root.toString());

    // Error scenario: Remove unconditional branching that targets a conditional branch.
    sourceCode = "function foo() {" +
        "  if (true) {" +
        "    if (true) {" +
        "      break;" +
        "    }" +
        "    alert('unreachable');" +
        "  }" +
        "}" +
        "foo();";

    root = NodeFactory.compile(sourceCode);
    elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertEquals("function foo() {" + //
        "  if (true) {" +
        "    if (true) {" +
        "    }" +
        "  }" +
        "}", root.toString());

    root = NodeFactory.compile(sourceCode);
    elimination = new UnreachableCodeElimination(compiler, true);
    try {
      elimination.process(null, root);
      fail("Expected IllegalArgumentException from removeDeadExprStatementSafely()");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
}