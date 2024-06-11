import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.test_util.AbstractCompilerTestCase;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class RenameLabelsTest extends AbstractCompilerTestCase {

  private Node root;
  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    root = createRootNode();
    compiler = newCompiler();
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testSimpleLabelRename() {
    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }

  @Test(expected = AssertionError.class)
  public void testLabelNameCollision() {
    root.setFirstChild(nodeFactory.createLabel("a"));
    root.setLastChild(nodeFactory.createLabel("a"));
    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }

  @Test
  public void testLabelRenamingInsideBlock() {
    Node block = nodeFactory.createBlock();
    Node label1 = nodeFactory.createLabel("a");
    Node label2 = nodeFactory.createLabel("b");
    block.addChild(label1);
    block.addChild(nodeFactory.createLabel("b"));
    root.addChild(block);

    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }

  @Test
  public void testLabelRenamingInsideFunction() {
    Node function = nodeFactory.createFunction("foo", new ArrayList<>(), nodeFactory.createBlock());
    Node label1 = nodeFactory.createLabel("a");
    Node label2 = nodeFactory.createLabel("b");
    function.getLastChild().addChild(label1);
    function.getLastChild().addChild(label2);
    root.addChild(function);

    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }

  @Test
  public void testLabelRenamingInsideFunctionWithReturn() {
    Node function = nodeFactory.createFunction("foo", new ArrayList<>(), nodeFactory.createBlock());
    Node label = nodeFactory.createLabel("a");
    function.getLastChild().addChild(label);
    function.addChild(nodeFactory.createReturnValue(null));
    root.addChild(function);

    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }

  @Test
  public void testLabelRenamingInsideFunctionWithBreak() {
    Node function = nodeFactory.createFunction("foo", new ArrayList<>(), nodeFactory.createBlock());
    Node label1 = nodeFactory.createLabel("a");
    Node label2 = nodeFactory.createLabel("b");
    Node breakStatement = nodeFactory.createBreakStatement();
    function.getLastChild().addChild(label1);
    function.getLastChild().addChild(nodeFactory.createForStatement(nodeFactory.createLabel("i"), null, null, null, null, null, null, null, null, null, null, null, null, null, breakStatement));
    function.getLastChild().addChild(label2);
    root.addChild(function);

    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }

  @Test
  public void testLabelRenamingInsideFunctionWithContinue() {
    Node function = nodeFactory.createFunction("foo", new ArrayList<>(), nodeFactory.createBlock());
    Node label1 = nodeFactory.createLabel("a");
    Node label2 = nodeFactory.createLabel("b");
    Node continueStatement = nodeFactory.createContinueStatement();
    function.getLastChild().addChild(label1);
    function.getLastChild().addChild(nodeFactory.createForStatement(nodeFactory.createLabel("i"), null, null, null, null, null, null, null, null, null, null, null, null, null, null, continueStatement));
    function.getLastChild().addChild(label2);
    root.addChild(function);

    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }

  @Test
  public void testLabelRenamingInsideFunctionWithNestedLoop() {
    Node function = nodeFactory.createFunction("foo", new ArrayList<>(), nodeFactory.createBlock());
    Node label1 = nodeFactory.createLabel("a");
    Node label2 = nodeFactory.createLabel("b");
    Node forStatement1 = nodeFactory.createForStatement(nodeFactory.createLabel("i"), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node forStatement2 = nodeFactory.createForStatement(nodeFactory.createLabel("j"), null, null, null, null, null, null, null, null, null, null, null, null, nodeFactory.createBlock(nodeFactory.createBreakStatement()), null, null);
    function.getLastChild().addChild(label1);
    function.getLastChild().addChild(forStatement1);
    function.getLastChild().addChild(label2);
    function.getLastChild().addChild(forStatement2);
    root.addChild(function);

    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }

  @Test(expected = RuntimeException.class)
  public void testLabelRenamingInsideFunctionWithIllegalLabelName() {
    Node function = nodeFactory.createFunction("foo", new ArrayList<>(), nodeFactory.createBlock());
    Node label1 = nodeFactory.createLabel("__iterator__");
    Node label2 = nodeFactory.createLabel("b");
    function.getLastChild().addChild(label1);
    function.getLastChild().addChild(label2);
    root.addChild(function);

    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLabelRenamingInsideFunctionWithNullLabel() {
    Node function = nodeFactory.createFunction("foo", new ArrayList<>(), nodeFactory.createBlock());
    Node label = null;
    function.getLastChild().addChild(label);
    root.addChild(function);

    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLabelRenamingInsideFunctionWithEmptyLabelList() {
    Node function = nodeFactory.createFunction("foo", new ArrayList<>(), nodeFactory.createBlock());
    Node labels = nodeFactory.createList(new ArrayList<>());
    function.getLastChild().addChild(labels);
    root.addChild(function);

    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }

  @Test
  public void testLabelRenamingInsideFunctionWithKilledLabel() {
    Node function = nodeFactory.createFunction("foo", new ArrayList<>(), nodeFactory.createBlock());
    Node label1 = nodeFactory.createLabel("a");
    Node killStatement = nodeFactory.createAssign(nodeFactory.createIdentifier("x"), nodeFactory.createLabel("a"));
    Node label2 = nodeFactory.createLabel("b");
    function.getLastChild().addChild(label1);
    function.getLastChild().addChild(killStatement);
    function.getLastChild().addChild(label2);
    root.addChild(function);

    RenameLabels pass = new RenameLabels(compiler);
    CompilerOptions options = compiler.getOptions();
    options.setAssumptions(options.getAssumptions() | CompilerOptions.AssumeTopLevelAccess);
    assertCompilesTo("expected", pass, root);
  }
}