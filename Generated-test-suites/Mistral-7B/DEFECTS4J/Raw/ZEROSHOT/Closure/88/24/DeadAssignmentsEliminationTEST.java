import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> controlFlowGraph = mock(ControlFlowGraph.class);
    Scope scope = mock(Scope.class);
    DataFlowAnalysis dataFlowAnalysis = mock(DataFlowAnalysis.class);
    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(new Iterable<DiGraphNode<Node, Branch>>() {
      @Override
      public Iterator<DiGraphNode<Node, Branch>> iterator() {
        return new Iterator<DiGraphNode<Node, Branch>>() {
          private int index = 0;

          @Override
          public boolean hasNext() {
            return index < 3;
          }

          @Override
          public DiGraphNode<Node, Branch> next() {
            return new DiGraphNode<Node, Branch>(null, getNode(index++));
          }
        };
      }
    });
    when(dataFlowAnalysis.getLiveVariablesAnalysis()).thenReturn(liveness);
    when(liveness.analyze()).thenReturn(null);

    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.process(null, getNode(0));

    verify(compiler, times(1)).reportCodeChange();
  }

  private Node getNode(int index) {
    switch (index) {
      case 0:
        return new Node(Token.IF, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
      case 1:
        return new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
      case 2:
        return new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 1));
      default:
        throw new IllegalArgumentException();
    }
  }
}

class liveness implements DataFlowAnalysis.LiveVariablesAnalysis {
  // Add implementation here
}