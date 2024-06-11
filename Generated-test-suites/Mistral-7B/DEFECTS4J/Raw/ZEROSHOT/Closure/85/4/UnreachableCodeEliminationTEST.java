import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.CallbackFlag.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scan.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {

  AbstractCompiler compiler;
  Node rootNode;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    rootNode = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newStatement(Token.RETURN, Node.newIntLit(42)),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("false"),
          Node.newStatement(Token.RETURN, Node.newIntLit(0))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(1)),
          Node.newStatement(Token.RETURN, Node.newIntLit(1))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("false"),
          Node.newStatement(Token.RETURN, Node.newIntLit(1))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(2)),
          Node.newStatement(Token.RETURN, Node.newIntLit(2))
        })
    ));
  }

  @Test
  public void testUnreachableCodeElimination() {
    CompilerPass unreachableCodeElimination =
        new UnreachableCodeElimination(compiler, true);
    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(unreachableCodeElimination);
    NodeTraversal traverser = new NodeTraversal(compiler, rootNode, passManager);
    traverser.traverse();
    Node expected = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newStatement(Token.RETURN, Node.newIntLit(42)),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("false")
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(1))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("false")
        })
    ));
    assertEquals("Unexpected elimination of reachable code", expected, rootNode);

    // Testing with removeNoOpStatements = false
    unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    passManager = new CompilerPassManager();
    passManager.addPass(unreachableCodeElimination);
    traverser = new NodeTraversal(compiler, rootNode, passManager);
    traverser.traverse();
    expected = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("false"),
          Node.newStatement(Token.RETURN, Node.newIntLit(0))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(1)),
          Node.newStatement(Token.RETURN, Node.newIntLit(1))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("false"),
          Node.newStatement(Token.RETURN, Node.newIntLit(1))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(2)),
          Node.newStatement(Token.RETURN, Node.newIntLit(2))
        })
    ));
    assertEquals("Unexpected elimination of reachable code", expected, rootNode);

    // Testing the elimination of dead code after the first return statement
    rootNode = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newStatement(Token.RETURN, Node.newIntLit(42)),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("false"),
          Node.newStatement(Token.RETURN, Node.newIntLit(0))
        }),
        Node.newStatement(Token.RETURN, Node.newIntLit(1))
    ));
    traverser = new NodeTraversal(compiler, rootNode, passManager);
    traverser.traverse();
    expected = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newStatement(Token.RETURN, Node.newIntLit(42))
    ));
    assertEquals("Unexpected elimination of reachable code", expected, rootNode);

    // Testing the elimination of dead code after a break statement
    rootNode = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newStatement(Token.LABEL, "loop"),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.BREAK, "loop")
        }),
        Node.newStatement(Token.RETURN, Node.newIntLit(1))
    ));
    traverser = new NodeTraversal(compiler, rootNode, passManager);
    traverser.traverse();
    expected = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newStatement(Token.LABEL, "loop")
    ));
    assertEquals("Unexpected elimination of reachable code", expected, rootNode);

    // Testing the elimination of dead code after a continue statement
    rootNode = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newStatement(Token.LABEL, "loop"),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.CONTINUE, "loop")
        }),
        Node.newStatement(Token.RETURN, Node.newIntLit(1))
    ));
    traverser = new NodeTraversal(compiler, rootNode, passManager);
    traverser.traverse();
    expected = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newStatement(Token.LABEL, "loop")
    ));
    assertEquals("Unexpected elimination of reachable code", expected, rootNode);
  }

  @Test
  public void testUnreachableCodeEliminationWithNoOpStatements() {
    // Testing the elimination of dead code with no-op statements
    rootNode = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newStatement(Token.RETURN, Node.newIntLit(42)),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("false"),
          Node.newStatement(Token.RETURN, Node.newIntLit(0)),
          Node.newStatement(Token.TRUE)
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(1)),
          Node.newStatement(Token.RETURN, Node.newIntLit(1))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("false"),
          Node.newStatement(Token.RETURN, Node.newIntLit(1))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(2)),
          Node.newStatement(Token.RETURN, Node.newIntLit(2))
        })
    ));
    traverser = new NodeTraversal(compiler, rootNode, passManager);
    traverser.traverse();
    expected = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newStatement(Token.RETURN, Node.newIntLit(42)),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("false"),
          Node.newStatement(Token.RETURN, Node.newIntLit(0))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(1))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("false")
        })
    ));
    assertEquals("Unexpected elimination of reachable code", expected, rootNode);
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    // Testing the elimination of unreachable break, continue, and return statements
    rootNode = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newLabel("loop"),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.BREAK, "loop")
        }),
        Node.newStatement(Token.RETURN, Node.newIntLit(1)),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.BREAK, "loop")
        }),
        Node.newStatement(Token.RETURN, Node.newIntLit(2)),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.BREAK, "loop")
        }),
        Node.newStatement(Token.RETURN, Node.newIntLit(3))
    ));
    traverser = new NodeTraversal(compiler, rootNode, passManager);
    traverser.traverse();
    expected = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newLabel("loop"),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.BREAK, "loop")
        }),
        Node.newStatement(Token.RETURN, Node.newIntLit(1)),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.BREAK, "loop")
        }),
        Node.newStatement(Token.RETURN, Node.newIntLit(2)),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.BREAK, "loop")
        })
    ));
    assertEquals("Unexpected elimination of reachable code", expected, rootNode);

    // Testing the elimination of unreachable return statements
    rootNode = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newLabel("loop"),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(1))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(2))
        }),
        Node.newStatement(Token.RETURN, Node.newIntLit(3)),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(4))
        })
    ));
    traverser = new NodeTraversal(compiler, rootNode, passManager);
    traverser.traverse();
    expected = Node.newBlock(Arrays.asList(
        Node.newExpr("function test() {}"),
        Node.newLabel("loop"),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(1))
        }),
        Node.newStatement(Token.IF, new Node[] {
          Node.newExpr("true"),
          Node.newStatement(Token.RETURN, Node.newIntLit(2))
        }),
        Node.newStatement(Token.RETURN, Node.newIntLit(3))
    ));
    assertEquals("Unexpected elimination of reachable code", expected, rootNode);
  }
}