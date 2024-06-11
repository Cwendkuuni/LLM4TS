package com.google.javascript.jscomp;

import com.google.common.base.Joiner;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.rhino.NodeFactory.newAssignment;
import static com.google.javascript.rhino.NodeFactory.newBlock;
import static com.google.javascript.rhino.NodeFactory.newExpressionStatement;
import static com.google.javascript.rhino.NodeFactory.newIdentifier;
import static com.google.javascript.rhino.NodeFactory.newIfStatement;
import static com.google.javascript.rhino.NodeFactory.newLocal;
import static com.google.javascript.rhino.NodeFactory.newNumber;
import static com.google.javascript.rhino.NodeFactory.newReturnStatement;
import static com.google.javascript.rhino.NodeFactory.newVariable;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private Traversal traverser;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    compiler = new AbstractCompiler() {};
    nodeFactory = NodeFactory.getPrototype();
    traverser = new Traversal(compiler, null);
  }

  @Test
  public void testDeadAssignmentsElimination_Simple() {
    Node root = newBlock(
        newVariable("a"),
        newAssignment(newIdentifier("a"), newNumber(1)),
        newAssignment(newIdentifier("a"), newNumber(2)),
        newReturnStatement(newIdentifier("a"))
    );

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    Graph<Node> controlFlowGraph = traverser.getControlFlowGraph(root);
    DataFlowAnalysis<LiveVariableLattice> liveness = new LiveVariablesAnalysis(
        controlFlowGraph, null, compiler
    );
    liveness.analyze();

    assertThat(traverser.getTreeDepth(controlFlowGraph.getRootNode()))
        .isEqualTo(3);

    String expectedCode = Joiner.on("\n").join(
        "var a;",
        "a = 1;",
        "a = 2;",
        "return a;"
    );

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsElimination_DeadVariable() {
    Node root = newBlock(
        newVariable("a"),
        newAssignment(newIdentifier("a"), newNumber(1)),
        newReturnStatement(newIdentifier("b"))
    );

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    String expectedCode = "var a;";

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsElimination_AssignmentsBeforeReturn() {
    Node root = newBlock(
        newVariable("a"),
        newReturnStatement(newAssignment(newIdentifier("a"), newNumber(1))),
        newAssignment(newIdentifier("a"), newNumber(2))
    );

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    String expectedCode = "var a;";

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsElimination_ConditionalBranch() {
    Node root = newBlock(
        newVariable("a"),
        newIfStatement(
            newExpressionStatement(newNumber(1)),
            newBlock(
                newAssignment(newIdentifier("a"), newNumber(1))
            ),
            newBlock(
                newAssignment(newIdentifier("a"), newNumber(2))
            )
        ),
        newReturnStatement(newIdentifier("a"))
    );

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    String expectedCode = "var a;";

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsElimination_AssignmentsInConditionalBranch() {
    Node root = newBlock(
        newVariable("a"),
        newIfStatement(
            newExpressionStatement(newNumber(1)),
            newBlock(
                newAssignment(newIdentifier("a"), newNumber(1))
            ),
            newBlock(
                newAssignment(newIdentifier("a"), newNumber(2)),
                newAssignment(newIdentifier("a"), newNumber(3))
            )
        ),
        newReturnStatement(newIdentifier("a"))
    );

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    String expectedCode = "var a;";

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsElimination_FunctionCall() {
    Node root = newBlock(
        newVariable("a"),
        newExpressionStatement(newIdentifier("f")),
        newAssignment(newIdentifier("a"), newNumber(1)),
        newReturnStatement(newIdentifier("a"))
    );

    Node function = newBlock(
        newVariable("x"),
        newAssignment(newIdentifier("x"), newNumber(1)),
        newReturnStatement(newIdentifier("x"))
    );

    Node functionCall = newExpressionStatement(newIdentifier("f"));
    Node functionRoot = newBlock(functionCall);

    root.setChildAt(1, functionRoot);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    String expectedCode = "var a;";

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsElimination_FunctionCallWithArguments() {
    Node root = newBlock(
        newVariable("a"),
        newExpressionStatement(newIdentifier("f")),
        newAssignment(newIdentifier("a"), newNumber(1)),
        newReturnStatement(newIdentifier("a"))
    );

    Node function = newBlock(
        newVariable("x"),
        newAssignment(newIdentifier("x"), newNumber(1)),
        newAssignment(newIdentifier("x"), newNumber(2)),
        newReturnStatement(newIdentifier("x"))
    );

    Node functionCall = newExpressionStatement(
        newAssignment(newIdentifier("f"), newAssignment(newIdentifier("x"), newNumber(3)))
    );

    Node functionRoot = newBlock(functionCall);

    root.setChildAt(1, functionRoot);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    String expectedCode = "var a;";

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsElimination_FunctionCallWithLocalVariables() {
    Node root = newBlock(
        newVariable("a"),
        newExpressionStatement(newIdentifier("f")),
        newAssignment(newIdentifier("a"), newNumber(1)),
        newReturnStatement(newIdentifier("a"))
    );

    Node function = newBlock(
        newVariable("x"),
        newAssignment(newIdentifier("x"), newNumber(1)),
        newAssignment(newIdentifier("y"), newNumber(1)),
        newReturnStatement(newIdentifier("x"))
    );

    Node functionCall = newExpressionStatement(
        newAssignment(newIdentifier("f"), newAssignment(newIdentifier("x"), newNumber(3)))
    );

    Node functionRoot = newBlock(functionCall);

    root.setChildAt(1, functionRoot);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    String expectedCode = "var a;";

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsElimination_Loops() {
    Node root = newBlock(
        newVariable("a"),
        newForStatement(
            newExpressionStatement(newIdentifier("i"), newNumber(1)),
            newIdentifier("i"), newIdentifier("i"),
            newBlock(
                newAssignment(newIdentifier("a"), newNumber(1))
            )
        ),
        newReturnStatement(newIdentifier("a"))
    );

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    String expectedCode = "var a;";

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsElimination_LoopsWithDeadAssignments() {
    Node root = newBlock(
        newVariable("a"),
        newForStatement(
            newExpressionStatement(newIdentifier("i"), newNumber(1)),
            newIdentifier("i"), newIdentifier("i"),
            newBlock(
                newAssignment(newIdentifier("a"), newNumber(1)),
                newAssignment(newIdentifier("a"), newNumber(2))
            )
        ),
        newReturnStatement(newIdentifier("a"))
    );

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    String expectedCode = "var a;";

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsElimination_LoopsWithDeadAssignmentsAndElseBranch() {
    Node root = newBlock(
        newVariable("a"),
        newForStatement(
            newExpressionStatement(newIdentifier("i"), newNumber(1)),
            newIdentifier("i"), newIdentifier("i"),
            newBlock(
                newAssignment(newIdentifier("a"), newNumber(1)),
                newAssignment(newIdentifier("a"), newNumber(2))
            ),
            newBlock(newReturnStatement(newIdentifier("a"))),
            newBlock(newReturnStatement(newNumber(3)))
        )
    );

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    String expectedCode = "var a;";

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testDeadAssignmentsElimination_LoopsWithDeadAssignmentsAndElifBranch() {
    Node root = newBlock(
        newVariable("a"),
        newForStatement(
            newExpressionStatement(newIdentifier("i"), newNumber(1)),
            newIdentifier("i"), newIdentifier("i"),
            newBlock(
                newAssignment(newIdentifier("a"), newNumber(1)),
                newAssignment(newIdentifier("a"), newNumber(2))
            ),
            newBlock(newReturnStatement(newIdentifier("a"))),
            newBlock(newIfStatement(
                newExpressionStatement(newIdentifier("i"), newNumber(5)),
                newBlock(newReturnStatement(newIdentifier("a"))),
                newBlock(newReturnStatement(newNumber(3)))
            ))
        )
    );

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    String expectedCode = "var a;";

    String actualCode = NodePrinter.printNodeToAnnotatedString(root);
    assertThat(actualCode).isEqualTo(expectedCode);
  }
}