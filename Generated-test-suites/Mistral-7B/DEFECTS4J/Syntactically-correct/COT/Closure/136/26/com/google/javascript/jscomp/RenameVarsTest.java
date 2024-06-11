import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.NodeFactory;
import java.util.*;

public class RenameVarsTest {

  @Test
  public void testRenameVarsBasic() {
    AbstractCompiler compiler = new AbstractCompiler();
    String prefix = "test_";
    CompilerPass renamer = new RenameVars(compiler, prefix, false, false, false, null, null, null);
    Node root = NodeFactory.createProgram();
    NodeTraversal traversal = new NodeTraversal(compiler, root);

    // Test renaming local variables
    Node localVarTest = NodeFactory.createBlock(
        NodeFactory.createVariable("a"),
        NodeFactory.createVariable("b"),
        NodeFactory.createExpressionStatement(NodeFactory.createAssignment(
            NodeFactory.createVariable("c"), NodeFactory.createNumberLiteral(10)
        ))
    );
    Node localScope = NodeFactory.createBlock(localVarTest);
    traversal.traverse(new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.BLOCK) {
          t.setCurrentScope(new Scope(null, null, null, null, null));
          t.pushCurrentScope();
          t.traverse(localScope);
          t.popCurrentScope();
        }
      }
    });

    renamer.process(NodeFactory.createProgram(), root);
    assertEquals("a => ta", renamer.getVariableMap().lookupName("a"));
    assertEquals("b => tb", renamer.getVariableMap().lookupName("b"));
    assertEquals("c => tc", renamer.getVariableMap().lookupName("c"));

    // Test renaming global variables
    Node globalsTest = NodeFactory.createBlock(
        NodeFactory.createVariable("x"),
        NodeFactory.createVariable("y"),
        NodeFactory.createExpressionStatement(NodeFactory.createAssignment(
            NodeFactory.createVariable("z"), NodeFactory.createNumberLiteral(20)
        ))
    );
    Node globalScope = NodeFactory.createBlock(globalsTest);
    traversal.traverse(new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.BLOCK) {
          t.setCurrentScope(new Scope(null, null, null, globalScope, null));
          t.traverse(globalScope);
        }
      }
    });

    renamer.process(NodeFactory.createProgram(), root);
    assertEquals("x => tx", renamer.getVariableMap().lookupName("x"));
    assertEquals("y => ty", renamer.getVariableMap().lookupName("y"));
    assertEquals("z => tz", renamer.getVariableMap().lookupName("z"));
  }

  @Test
  public void testRenameVarsLocalOnly() {
    AbstractCompiler compiler = new AbstractCompiler();
    String prefix = "test_";
    CompilerPass renamer = new RenameVars(compiler, prefix, true, false, false, null, null, null);
    Node root = NodeFactory.createProgram();
    NodeTraversal traversal = new NodeTraversal(compiler, root);

    // Test renaming only local variables
    Node localVarTest = NodeFactory.createBlock(
        NodeFactory.createVariable("a"),
        NodeFactory.createVariable("b"),
        NodeFactory.createExpressionStatement(NodeFactory.createAssignment(
            NodeFactory.createVariable("c"), NodeFactory.createNumberLiteral(10)
        ))
    );
    Node localScope = NodeFactory.createBlock(localVarTest);
    traversal.traverse(new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.BLOCK) {
          t.setCurrentScope(new Scope(null, null, null, null, null));
          t.pushCurrentScope();
          t.traverse(localScope);
          t.popCurrentScope();
        }
      }
    });

    renamer.process(NodeFactory.createProgram(), root);
    assertEquals("a => la", renamer.getVariableMap().lookupName("a"));
    assertEquals("b => lb", renamer.getVariableMap().lookupName("b"));
    assertEquals("c => lc", renamer.getVariableMap().lookupName("c"));

    // Test renaming globals are ignored
    Node globalsTest = NodeFactory.createBlock(
        NodeFactory.createVariable("x"),
        NodeFactory.createVariable("y"),
        NodeFactory.createExpressionStatement(NodeFactory.createAssignment(
            NodeFactory.createVariable("z"), NodeFactory.createNumberLiteral(20)
        ))
    );
    Node globalScope = NodeFactory.createBlock(globalsTest);
    traversal.traverse(new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.BLOCK) {
          t.setCurrentScope(new Scope(null, null, null, globalScope, null));
          t.traverse(globalScope);
        }
      }
    });

    renamer.process(NodeFactory.createProgram(), root);
    assertEquals("x => x", renamer.getVariableMap().lookupName("x"));
    assertEquals("y => y", renamer.getVariableMap().lookupName("y"));
    assertEquals("z => z", renamer.getVariableMap().lookupName("z"));
  }

  @Test
  public void testRenameVarsPreserveAnonymousFunctionNames() {
    AbstractCompiler compiler = new AbstractCompiler();
    String prefix = "test_";
    CompilerPass renamer = new RenameVars(compiler, prefix, false, true, false, null, null, null);
    Node root = NodeFactory.createProgram();
    NodeTraversal traversal = new NodeTraversal(compiler, root);

    // Test preserving anonymous function names
    Node anonymousFunctionTest = NodeFactory.createExpressionStatement(
        NodeFactory.createAssignment(
            NodeFactory.createVariable("a"),
            NodeFactory.createFunction(
                NodeFactory.createBlock(
                    NodeFactory.createVariable("x"),
                    NodeFactory.createExpressionStatement(
                        NodeFactory.createAssignment(
                            NodeFactory.createVariable("y"),
                            NodeFactory.createBinaryOperator(
                                NodeFactory.createIdentifier("x"),
                                Token.PLUS,
                                NodeFactory.createNumberLiteral(1)
                            )
                        )
                    )
                ),
                null,
                null,
                null,
                null,
                NodeFactory.createProgram()
            )
        )
    );
    Node anonymousFunctionScope = NodeFactory.createBlock(anonymousFunctionTest);
    traversal.traverse(new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.BLOCK) {
          t.setCurrentScope(new Scope(null, null, null, null, null));
          t.pushCurrentScope();
          t.traverse(anonymousFunctionScope);
          t.popCurrentScope();
        }
      }
    });

    renamer.process(NodeFactory.createProgram(), root);
    assertEquals("a => a", renamer.getVariableMap().lookupName("a"));
    assertEquals("x => x", renamer.getVariableMap().lookupName("x"));
    assertEquals("y => y", renamer.getVariableMap().lookupName("y"));
  }

  @Test
  public void testRenameVarsGeneratePseudoNames() {
    AbstractCompiler compiler = new AbstractCompiler();
    String prefix = "test_";
    CompilerPass renamer = new RenameVars(compiler, prefix, false, false, true, null, null, null);
    Node root = NodeFactory.createProgram();
    NodeTraversal traversal = new NodeTraversal(compiler, root);

    // Test generating pseudo names for local variables
    Node localVarTest = NodeFactory.createBlock(
        NodeFactory.createVariable("a"),
        NodeFactory.createVariable("b"),
        NodeFactory.createExpressionStatement(NodeFactory.createAssignment(
            NodeFactory.createVariable("c"), NodeFactory.createNumberLiteral(10)
        ))
    );
    Node localScope = NodeFactory.createBlock(localVarTest);
    traversal.traverse(new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.BLOCK) {
          t.setCurrentScope(new Scope(null, null, null, null, null));
          t.pushCurrentScope();
          t.traverse(localScope);
          t.popCurrentScope();
        }
      }
    });

    renamer.process(NodeFactory.createProgram(), root);
    assertEquals("a => $a$0", renamer.getVariableMap().lookupName("a"));
    assertEquals("b => $b$1", renamer.getVariableMap().lookupName("b"));
    assertEquals("c => $c$2", renamer.getVariableMap().lookupName("c"));

    // Test generating pseudo names for global variables
    Node globalsTest = NodeFactory.createBlock(
        NodeFactory.createVariable("x"),
        NodeFactory.createVariable("y"),
        NodeFactory.createExpressionStatement(NodeFactory.createAssignment(
            NodeFactory.createVariable("z"), NodeFactory.createNumberLiteral(20)
        ))
    );
    Node globalScope = NodeFactory.createBlock(globalsTest);
    traversal.traverse(new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.BLOCK) {
          t.setCurrentScope(new Scope(null, null, null, globalScope, null));
          t.traverse(globalScope);
        }
      }
    });

    renamer.process(NodeFactory.createProgram(), root);
    assertEquals("x => $x$0", renamer.getVariableMap().lookupName("x"));
    assertEquals("y => $y$1", renamer.getVariableMap().lookupName("y"));
    assertEquals("z => $z$2", renamer.getVariableMap().lookupName("z"));
  }

  @Test
  public void testRenameVarsWithExistingNames() {
    AbstractCompiler compiler = new AbstractCompiler();
    String prefix = "test_";
    CompilerPass renamer = new RenameVars(compiler, prefix, false, false, false, null, null, null);
    Node root = NodeFactory.createProgram();
    NodeTraversal traversal = new NodeTraversal(compiler, root);

    // Test renaming variables with existing names
    Node conflictingTest = NodeFactory.createBlock(
        NodeFactory.createVariable("x"),
        NodeFactory.createVariable("x"),
        NodeFactory.createExpressionStatement(NodeFactory.createAssignment(
            NodeFactory.createVariable("x"), NodeFactory.createNumberLiteral(10)
        ))
    );
    Node conflictingScope = NodeFactory.createBlock(conflictingTest);
    traversal.traverse(new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.BLOCK) {
          t.setCurrentScope(new Scope(null, null, null, null, null));
          t.pushCurrentScope();
          t.traverse(conflictingScope);
          t.popCurrentScope();
        }
      }
    });

    renamer.process(NodeFactory.createProgram(), root);
    assertEquals("x => x$0", renamer.getVariableMap().lookupName("x"));
    assertEquals("x => x$1", renamer.getVariableMap().lookupName("x"));
  }
}

final class Scope {
  // Scope internal implementation
}