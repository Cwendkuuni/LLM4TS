import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class MaybeReachingVariableUseTest {
  @Test
  public void testMaybeReachingVariableUse() {
    // Create mock Scope and ControlFlowGraph
    Scope jsScope = mock(Scope.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);

    // Define test data
    Node entryNode = Node.newInstance(Token.BLOCK, null, null, null);
    Node defNode1 = Node.newInstance(Token.ASSIGN, null, entryNode, Node.newInstance(Token.NAME, "x"));
    Node useNode1 = Node.newInstance(Token.NAME, "x");
    Node defNode2 = Node.newInstance(Token.ASSIGN, null, defNode1, Node.newInstance(Token.NAME, "x"));
    Node useNode2 = Node.newInstance(Token.NAME, "x");
    Node conditionalNode = Node.newInstance(Token.IF, null, defNode1, useNode2, Node.newInstance(Token.NAME, "true"));
    Node defNode3 = Node.newInstance(Token.ASSIGN, null, defNode2, Node.newInstance(Token.NAME, "x"));
    Node useNode3 = Node.newInstance(Token.NAME, "x");

    // Set up mocks
    when(cfg.getNode(entryNode)).thenReturn(new GraphNode<Node, Branch>() {
      @Override
      public Branch getEdgeTo(Node node) {
        return null;
      }

      @Override
      public DiGraphEdge getEdgeFrom(Node node) {
        return null;
      }

      @Override
      public void addSuccessor(Node node) {}

      @Override
      public void addPredecessor(Node node) {}

      @Override
      public List<Node> getChildren() {
        return List.of(defNode1);
      }

      @Override
      public Node getParent() {
        return null;
      }
    });
    when(cfg.getNode(defNode1)).thenReturn(new GraphNode<Node, Branch>() {
      @Override
      public Branch getEdgeTo(Node node) {
        if (node == useNode1) {
          return new Branch(useNode1, defNode1, Token.ASSIGN);
        } else if (node == conditionalNode) {
          return new Branch(conditionalNode, defNode1, Token.IF);
        } else {
          return null;
        }
      }

      @Override
      public DiGraphEdge getEdgeFrom(Node node) {
        return null;
      }

      @Override
      public void addSuccessor(Node node) {}

      @Override
      public void addPredecessor(Node node) {}

      @Override
      public List<Node> getChildren() {
        return List.of(defNode2);
      }

      @Override
      public Node getParent() {
        return entryNode;
      }
    });
    when(cfg.getNode(defNode2)).thenReturn(new GraphNode<Node, Branch>() {
      @Override
      public Branch getEdgeTo(Node node) {
        if (node == defNode3) {
          return new Branch(defNode3, defNode2, Token.ASSIGN);
        } else if (node == useNode3) {
          return new Branch(useNode3, defNode2, Token.NAME);
        } else {
          return null;
        }
      }

      @Override
      public DiGraphEdge getEdgeFrom(Node node) {
        return null;
      }

      @Override
      public void addSuccessor(Node node) {}

      @Override
      public void addPredecessor(Node node) {}

      @Override
      public List<Node> getChildren() {
        return List.of();
      }

      @Override
      public Node getParent() {
        return defNode1;
      }
    });
    when(cfg.getNode(defNode3)).thenReturn(new GraphNode<Node, Branch>() {
      @Override
      public Branch getEdgeTo(Node node) {
        return null;
      }

      @Override
      public DiGraphEdge getEdgeFrom(Node node) {
        return null;
      }

      @Override
      public void addSuccessor(Node node) {}

      @Override
      public void addPredecessor(Node node) {}

      @Override
      public List<Node> getChildren() {
        return List.of();
      }

      @Override
      public Node getParent() {
        return defNode2;
      }
    });
    when(cfg.getNode(useNode1)).thenReturn(new GraphNode<Node, Branch>() {
      @Override
      public Branch getEdgeTo(Node node) {
        return null;
      }

      @Override
      public DiGraphEdge getEdgeFrom(Node node) {
        return null;
      }

      @Override
      public void addSuccessor(Node node) {}

      @Override
      public void addPredecessor(Node node) {}

      @Override
      public List<Node> getChildren() {
        return List.of();
      }

      @Override
      public Node getParent() {
        return defNode1;
      }
    });
    when(cfg.getNode(useNode2)).thenReturn(new GraphNode<Node, Branch>() {
      @Override
      public Branch getEdgeTo(Node node) {
        return null;
      }

      @Override
      public DiGraphEdge getEdgeFrom(Node node) {
        return null;
      }

      @Override
      public void addSuccessor(Node node) {}

      @Override
      public void addPredecessor(Node node) {}

      @Override
      public List<Node> getChildren() {
        return List.of();
      }

      @Override
      public Node getParent() {
        return conditionalNode;
      }
    });
    when(cfg.getNode(useNode3)).thenReturn(new GraphNode<Node, Branch>() {
      @Override
      public Branch getEdgeTo(Node node) {
        return null;
      }

      @Override
      public DiGraphEdge getEdgeFrom(Node node) {
        return null;
      }

      @Override
      public void addSuccessor(Node node) {}

      @Override
      public void addPredecessor(Node node) {}

      @Override
      public List<Node> getChildren() {
        return List.of();
      }

      @Override
      public Node getParent() {
        return defNode2;
      }
    });
    when(jsScope.getVar("x")).thenReturn(new Var("x", 0));

    // Create MaybeReachingVariableUse instance
    MaybeReachingVariableUse maybeReachingVariableUse =  new MaybeReachingVariableUse(cfg, jsScope, null);

    // Test getUses method
    Collection<Node> uses1 = maybeReachingVariableUse.getUses("x", defNode1);
    assertEquals(List.of(useNode1), uses1);

    Collection<Node> uses2 = maybeReachingVariableUse.getUses("x", defNode2);
    assertEquals(List.of(useNode1, useNode3), uses2);

    Collection<Node> uses3 = maybeReachingVariableUse.getUses("x", defNode3);
    assertEquals(List.of(useNode3), uses3);
  }
}