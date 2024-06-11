import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNodeVisitor;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.rhino.NodeUtil;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private Scope scope;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    scope = new Scope(null);
    String source = "function foo() {" +
        "var x = 1;" +
        "x = 2;" +
        "var y = x;" +
        "x = 3;" +
        "}";
    Node root = AbstractNodeVisitor.parse(source, compiler);
    cfg = new ControlFlowGraph<Node>(root);
  }

  @Test
  public void testDeadAssignmentsElimination() {
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    NodeTraversal t = NodeTraversal.createGenericTraversal(root);
    t.traverse(eliminator);

    assertFalse(NodeUtil.has(root, new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return n.getType() == ASSIGN;
      }
    }, null));

    // Check that the order of assignments doesn't change.
    Function<Node, Integer> getNodeIndex = new Function<Node, Integer>() {
      private int index = 0;

      @Override
      public Integer apply(Node n) {
        index++;
        return index;
      }
    };

    List<Integer> xAssignments = Lists.transform(
        NodeUtil.getDescendantsOfType(root, ASSIGN, true), getNodeIndex);

    assertEquals(ImmutableList.of(2, 4), xAssignments);

    // Check that the dead assignment is removed.
    assertFalse(NodeUtil.has(root, new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return n.getType() == ASSIGN &&
               ((Assign) n).getLhs().getString().equals("x") &&
               ((Assign) n).getRhs().getType() == NUM_LITERAL &&
               ((NUM_LITERAL) ((Assign) n).getRhs()).getIntValue() == 2;
      }
    }, null));
  }

  @Test
  public void testDeadAssignmentsEliminationWithForLoop() {
    String source = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "var x = 1;" +
        "x = 2;" +
        "}" +
        "}";

    Node root = AbstractNodeVisitor.parse(source, compiler);
    cfg = new ControlFlowGraph<Node>(root);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    NodeTraversal t = NodeTraversal.createGenericTraversal(root);
    t.traverse(eliminator);

    assertFalse(NodeUtil.has(root, new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return n.getType() == ASSIGN &&
               ((Assign) n).getLhs().getString().equals("x") &&
               ((Assign) n).getRhs().getType() == NUM_LITERAL &&
               ((NUM_LITERAL) ((Assign) n).getRhs()).getIntValue() == 2;
      }
    }, null));

    // Check that the order of assignments doesn't change.
    Function<Node, Integer> getNodeIndex = new Function<Node, Integer>() {
      private int index = 0;

      @Override
      public Integer apply(Node n) {
        index++;
        return index;
      }
    };

    List<Integer> iAssignments = Lists.transform(
        NodeUtil.getDescendantsOfType(root, ASSIGN, true), getNodeIndex);

    assertEquals(ImmutableList.of(3, 5), iAssignments);

    // Check that the dead assignment is removed.
    assertNull(NodeUtil.get(root, "i", SCOPABLE));
  }

  @Test
  public void testDeadAssignmentsEliminationWithConditional() {
    String source = "function foo() {" +
        "var x = 1;" +
        "if (true) {" +
        "  x = 2;" +
        "}" +
        "}" +
        "function bar() {}";

    Node root = AbstractNodeVisitor.parse(source, compiler);
    cfg = new ControlFlowGraph<Node>(root);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    NodeTraversal t = NodeTraversal.createGenericTraversal(root);
    t.traverse(eliminator);

    // Check that the dead assignment is removed.
    assertFalse(NodeUtil.has(root, new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return n.getType() == ASSIGN &&
               ((Assign) n).getLhs().getString().equals("x") &&
               ((Assign) n).getRhs().getType() == NUM_LITERAL &&
               ((NUM_LITERAL) ((Assign) n).getRhs()).getIntValue() == 2;
      }
    }, null));

    // Check that the order of assignments doesn't change.
    Function<Node, Integer> getNodeIndex = new Function<Node, Integer>() {
      private int index = 0;

      @Override
      public Integer apply(Node n) {
        index++;
        return index;
      }
    };

    List<Integer> xAssignments = Lists.transform(
        NodeUtil.getDescendantsOfType(root, ASSIGN, true), getNodeIndex);

    assertEquals(ImmutableList.of(2), xAssignments);

    // Check that the dead assignment is removed from the function body.
    NodeTraversal t2 = NodeTraversal.createGenericTraversal(
        NodeUtil.getFunctionBody(root, 1));
    t2.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == ASSIGN &&
            ((Assign) n).getLhs().getString().equals("x") &&
            ((Assign) n).getRhs().getType() == NUM_LITERAL &&
            ((NUM_LITERAL) ((Assign) n).getRhs()).getIntValue() == 2) {
          fail();
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithForInLoop() {
    String source = "function foo() {" +
        "for (var i in [1, 2, 3]) {" +
        "  var x = 1;" +
        "  x = 2;" +
        "}" +
        "}" +
        "function bar() {}";

    Node root = AbstractNodeVisitor.parse(source, compiler);
    cfg = new ControlFlowGraph<Node>(root);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    NodeTraversal t = NodeTraversal.createGenericTraversal(root);
    t.traverse(eliminator);

    // Check that the dead assignment is removed.
    assertFalse(NodeUtil.has(root, new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return n.getType() == ASSIGN &&
               ((Assign) n).getLhs().getString().equals("x") &&
               ((Assign) n).getRhs().getType() == NUM_LITERAL &&
               ((NUM_LITERAL) ((Assign) n).getRhs()).getIntValue() == 2;
      }
    }, null));

    // Check that the order of assignments doesn't change.
    Function<Node, Integer> getNodeIndex = new Function<Node, Integer>() {
      private int index = 0;

      @Override
      public Integer apply(Node n) {
        index++;
        return index;
      }
    };

    List<Integer> assignments = Lists.transform(
        NodeUtil.getDescendantsOfType(root, ASSIGN, true), getNodeIndex);

    assertEquals(ImmutableList.of(5, 7), assignments);
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentsEliminationWithLiveVariable() {
    String source = "function foo() {" +
        "var x = 1;" +
        "if (true) {" +
        "  x = 2;" +
        "}" +
        "var y = x;" +
        "}" +
        "function bar() {}";

    Node root = AbstractNodeVisitor.parse(source, compiler);
    cfg = new ControlFlowGraph<Node>(root);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    NodeTraversal t = NodeTraversal.createGenericTraversal(root);
    t.traverse(eliminator);

    // Check that the dead assignment is removed.
    assertFalse(NodeUtil.has(root, new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return n.getType() == ASSIGN &&
               ((Assign) n).getLhs().getString().equals("x") &&
               ((Assign) n).getRhs().getType() == NUM_LITERAL &&
               ((NUM_LITERAL) ((Assign) n).getRhs()).getIntValue() == 2;
      }
    }, null));

    // Check that the order of assignments doesn't change.
    Function<Node, Integer> getNodeIndex = new Function<Node, Integer>() {
      private int index = 0;

      @Override
      public Integer apply(Node n) {
        index++;
        return index;
      }
    };

    List<Integer> xAssignments = Lists.transform(
        NodeUtil.getDescendantsOfType(root, ASSIGN, true), getNodeIndex);

    assertEquals(ImmutableList.of(2), xAssignments);

    // Check that the dead assignment is removed from the function body.
    NodeTraversal t2 = NodeTraversal.createGenericTraversal(
        NodeUtil.getFunctionBody(root, 1));
    t2.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == ASSIGN &&
            ((Assign) n).getLhs().getString().equals("x") &&
            ((Assign) n).getRhs().getType() == NUM_LITERAL &&
            ((NUM_LITERAL) ((Assign) n).getRhs()).getIntValue() == 2) {
          fail();
        }
      }
    });

    // Check that the variable 'y' is still present.
    Node y = NodeUtil.get(root, "y", SCOPABLE);
    assertNotNull(y);
  }

}