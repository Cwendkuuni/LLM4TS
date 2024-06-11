import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  @SuppressWarnings("unchecked")
  private static final Class<LiveVariablesAnalysis> CLASS =
      (Class<LiveVariablesAnalysis>) LiveVariablesAnalysis.class;

  private AbstractCompiler compiler;
  private Scope jsScope;
  private ControlFlowGraph<Node> cfg;

  @org.junit.Before
  public void setUp() {
    // Initialize test environment
    compiler = new AbstractCompiler();
    jsScope = compiler.getTopLevelScope();
  }

  // Test method for constructor
  @org.junit.Test
  public void testConstructor() {
    Node rootNode = new Node("function foo() {}");
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, compiler);
    assertNotNull(analysis);
  }

  // Test method for getEscapedLocals()
  @org.junit.Test
  public void testGetEscapedLocals() {
    String functionDefinition = "function bar(x, y) {" +
        "var z = x + y;" +
        "return z; }";
    Node rootNode = new Node(functionDefinition);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, compiler);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertTrue(escapedLocals.isEmpty());

    String functionWithArgumentsNameAsVariable = "function baz(x, y)" +
        "{ var args = arguments; return args[0] + args[1]; }";
    rootNode = new Node(functionWithArgumentsNameAsVariable);
    analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, compiler);
    escapedLocals = analysis.getEscapedLocals();
    assertEquals(2, escapedLocals.size());
    assertTrue(escapedLocals.contains(Var.forName("arguments")));
    assertTrue(escapedLocals.contains(Var.forName("args")));
  }

  // Test method for isForward()
  @org.junit.Test
  public void testIsForward() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(new Node("")), jsScope, compiler);
    assertFalse(analysis.isForward());
  }

  // Test method for createEntryLattice()
  @org.junit.Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(new Node("")), jsScope, compiler);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    assertEquals(jsScope.getVarCount(), entryLattice.liveSet.size());
  }

  // Test method for createInitialEstimateLattice()
  @org.junit.Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(new Node("")), jsScope, compiler);
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    assertEquals(jsScope.getVarCount(), initialEstimateLattice.liveSet.size());
  }

  // Test method for flowThrough() - empty graph
  @org.junit.Test
  public void testFlowThroughEmptyGraph() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(new Node("")), jsScope, compiler);
    Node emptyNode = new Node("{}");
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(emptyNode, input);
    assertSame(input, output);
  }

  // Test method for flowThrough() - simple assignment
  @org.junit.Test
  public void testFlowThroughSimpleAssignment() {
    String functionDefinition = "function foo() {" +
        "var x = 1;" +
        "x = 2;" +
        "}";
    Node rootNode = new Node(functionDefinition);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, compiler);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(rootNode, input);

    assertTrue(output.isLive(Var.forName("x")));
  }

  // Test method for flowThrough() - multiple assignments
  @org.junit.Test
  public void testFlowThroughMultipleAssignments() {
    String functionDefinition = "function foo() {" +
        "var x = 1;" +
        "x = 2;" +
        "x = 3;" +
        "}";
    Node rootNode = new Node(functionDefinition);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
      new ControlFlowGraph<>(rootNode), jsScope, compiler);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(rootNode, input);

    assertTrue(output.isLive(Var.forName("x")));
  }

  // Test method for flowThrough() - conditional assignment
  @org.junit.Test
  public void testFlowThroughConditionalAssignment() {
    String functionDefinition = "function foo() {" +
        "var x = 0;" +
        "if (true) {" +
        "  x = 1;" +
        "}" +
        "}";
    Node rootNode = new Node(functionDefinition);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, compiler);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(rootNode, input);

    assertTrue(output.isLive(Var.forName("x")));
  }

  // Test method for flowThrough() - conditional branch
  @org.junit.Test
  public void testFlowThroughConditionalBranch() {
    String functionDefinition = "function foo() {" +
        "var x = 0;" +
        "if (true) {" +
        "  x = 1;" +
        "} else {" +
        "  x = 2;" +
        "}" +
        "}";
    Node rootNode = new Node(functionDefinition);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, compiler);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(rootNode, input);

    assertTrue(output.isLive(Var.forName("x")));
  }

  // Test method for flowThrough() - for loop
  @org.junit.Test
  public void testFlowThroughForLoop() {
    String functionDefinition = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "  i += 1;" +
        "}" +
        "}";
    Node rootNode = new Node(functionDefinition);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, compiler);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(rootNode, input);

    assertTrue(output.isLive(Var.forName("i")));
  }

  // Test method for flowThrough() - if statement
  @org.junit.Test
  public void testFlowThroughIfStatement() {
    String functionDefinition = "function foo() {" +
        "if (true) {" +
        "  return 1;" +
        "}" +
        "else {" +
        "  return 2;" +
        "}" +
        "}";
    Node rootNode = new Node(functionDefinition);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, compiler);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(rootNode, input);

    assertFalse(output.isLive(Var.forName("this")));
  }

  // Test method for flowThrough() - return statement
  @org.junit.Test
  public void testFlowThroughReturnStatement() {
    String functionDefinition = "function foo() {" +
        "return 1;" +
        "}";
    Node rootNode = new Node(functionDefinition);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, compiler);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(rootNode, input);

    assertTrue(output.isLive(Var.forName("this")));
  }

  // Test method for flowThrough() - throw statement
  @org.junit.Test
  public void testFlowThroughThrowStatement() {
    String functionDefinition = "function foo() {" +
        "throw new Error('Error');" +
        "}";
    Node rootNode = new Node(functionDefinition);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, compiler);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(rootNode, input);

    assertFalse(output.isLive(Var.forName("this")));
  }
}