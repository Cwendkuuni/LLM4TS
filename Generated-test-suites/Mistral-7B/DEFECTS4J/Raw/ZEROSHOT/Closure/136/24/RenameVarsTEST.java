import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVarsTest.createCompilerContext;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class RenameVarsTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testRenameVars() {
    String prefix = "test_";
    RenameVars renamer = new RenameVars(compiler, prefix, false, false, false, null, null, null);

    Node root = NodeFactory.createProgram();
    NodeTraversal traversal = NodeTraversal.create(compiler, root);

    // Test renaming local variables
    Node functionNode = NodeFactory.createFunctionDeclaration("test", NodeFactory.createList(NodeFactory.createIdentifier("a")), NodeFactory.createBlock(NodeFactory.createStatementList()));
    Node block = functionNode.getFirstChild().getLastChild();
    Node localVariableDeclaration = NodeFactory.createVariableDeclarationList(NodeFactory.createList(createIdentifier("b")), NodeFactory.createNull(), NodeFactory.createList(createIdentifier("b")));
    block.setFirstChild(localVariableDeclaration);
    traversal.traverse(new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Node.FUNCTION && n.getFirstChild().getFirstChild().getString().equals("test")) {
          renamer.process(null, n);
        }
      }
    });
    assertEquals("test_b", block.getLastChild().getLastChild().getString());

    // Test renaming global variables
    Node globalVariable = NodeFactory.createVariableDeclarationList(NodeFactory.createList(createIdentifier("test")), NodeFactory.createNull(), NodeFactory.createList(createIdentifier("test")));
    Node globals = NodeFactory.createVariableDeclarationList(NodeFactory.createList(), NodeFactory.createNull(), NodeFactory.createList(globalVariable));
    Node body = NodeFactory.createBlock(NodeFactory.createStatementList(NodeFactory.createVariableDeclarationStatement(NodeFactory.createIdentifier("test"), NodeFactory.createNull())));
    Node program = NodeFactory.createProgram(NodeFactory.createList(globals, body));
    traversal = NodeTraversal.create(compiler, program);
    renamer.process(NodeFactory.createList(NodeFactory.createIdentifier("test")), root);
    assertEquals(prefix + "test", globalVariable.getFirstChild().getString());

    // Test renaming local variables only
    renamer = new RenameVars(compiler, prefix, true, false, false, null, null, null);
    functionNode = NodeFactory.createFunctionDeclaration("test", NodeFactory.createList(NodeFactory.createIdentifier("a")), NodeFactory.createBlock(NodeFactory.createStatementList()));
    block = functionNode.getFirstChild().getLastChild();
    localVariableDeclaration = NodeFactory.createVariableDeclarationList(NodeFactory.createList(createIdentifier("b")), NodeFactory.createNull(), NodeFactory.createList(createIdentifier("b")));
    block.setFirstChild(localVariableDeclaration);
    traversal.traverse(new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Node.FUNCTION && n.getFirstChild().getFirstChild().getString().equals("test")) {
          renamer.process(null, n);
        }
      }
    });
    assertNull(block.getLastChild().getLastChild().getString());

    // Test renaming with previous used map
    VariableMap prevUsedRenameMap = new VariableMap();
    prevUsedRenameMap.put("test", "test_old");
    renamer = new RenameVars(compiler, prefix, false, false, false, prevUsedRenameMap, null, null);
    Node renameTest = NodeFactory.createIdentifier("test");
    Node renameTestRenamed = renamer.process(NodeFactory.createList(renameTest), root).getLastChild();
    assertEquals("test_old", renameTestRenamed.getString());

  }

  private static NodeFactory NodeFactory = new NodeFactory();
  private static CompilerInput createCompilerInput(String code) {
    return new CompilerInput(null, null, code, null, null, null, null, null, null, null, null, null);
  }
  private static CompilerContext createCompilerContext() {
    return new CompilerContext(null, null, null, null, null, null, null, null);
  }
}