import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.SourceFile;

public class RenameLabelsTest {

  @org.junit.Test
  public void testRenameLabels() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new Compiler();
    compiler.addPass(new RenameLabels(compiler));

    String jsCode = "function foo() {" +
        "  a: {break a;}" +
        "  b: {continue a;}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node label = root.getFirstChild();
    assertEquals("a", label.getString());

    Node statements = label.getLastChild();
    Node breakStatement = statements.getFirstChild();
    Node breakLabel = breakStatement.getFirstChild();
    assertEquals("a", breakLabel.getString());

    Node continueStatement = statements.getLastChild();
    Node continueLabel = continueStatement.getFirstChild();
    assertEquals("a", continueLabel.getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithNestedLabels() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new Compiler();
    compiler.addPass(new RenameLabels(compiler));

    String jsCode = "function foo() {" +
        "  a: {b: {break a;}}" +
        "  a: {b: {continue a;}}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node label = root.getFirstChild();
    assertEquals("a", label.getString());

    Node statements = label.getLastChild();
    Node labelNested = statements.getFirstChild();
    assertEquals("b", labelNested.getFirstChild().getString());

    Node breakStatement = labelNested.getLastChild();
    Node breakLabel = breakStatement.getFirstChild();
    assertEquals("b", breakLabel.getString());

    Node continueStatement = labelNested.getPreviousSibling();
    Node continueLabel = continueStatement.getFirstChild();
    assertEquals("b", continueLabel.getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithDuplicateLabel() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new Compiler();
    compiler.addPass(new RenameLabels(compiler));

    String jsCode = "function foo() {" +
        "  a: {break a;}" +
        "  a: {}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node label = root.getFirstChild();
    assertEquals("a", label.getString());

    Node statements = label.getLastChild();
    Node breakStatement = statements.getFirstChild();
    Node breakLabel = breakStatement.getFirstChild();
    assertEquals("a", breakLabel.getString());

    assertTrue(compiler.getDiagnostics().hasError());
    assertEquals("Label 'a' is redeclared", compiler.getDiagnostics().getError(0).getMessage());
  }

  @org.junit.Test
  public void testRenameLabelsWithKeyword() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new Compiler();
    compiler.addPass(new RenameLabels(compiler));

    String jsCode = "function foo() {" +
        "  var label = 0;";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node labelNode = root.getFirstChild();
    assertEquals("label", labelNode.getString());

    assertTrue(compiler.getDiagnostics().hasError());
    assertEquals("Label 'label' is a reserved word", compiler.getDiagnostics().getError(0).getMessage());
  }

  @org.junit.Test
  public void testRenameLabelsWithNoLabel() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new Compiler();
    compiler.addPass(new RenameLabels(compiler));

    String jsCode = "function foo() {var a = 0;}"
        + "function bar() {label: {break label;}}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node labelNode = root.getFirstChild();
    assertEquals("a", labelNode.getString());

    Node bar = root.getChildByName("bar");
    assertNull(bar.getFirstChild());
  }

  @org.junit.Test
  public void testRenameLabelsWithUnusedLabel() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new RenameLabels(new Compiler());
    compiler.setRemoveUnused(false);

    String jsCode = "function foo() {"
        + "  a: {}"
        + "  b: break a;"
        + "}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node labelNode = root.getFirstChild();
    assertEquals("a", labelNode.getString());

    Node statements = labelNode.getLastChild();
    Node breakStatement = statements.getLastChild();
    Node breakLabel = breakStatement.getFirstChild();
    assertEquals("a", breakLabel.getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithUnusedLabelRemoved() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new RenameLabels(new Compiler());
    compiler.setRemoveUnused(true);

    String jsCode = "function foo() {"
        + "  a: {}"
        + "  b: break a;"
        + "}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node labelNode = root.getFirstChild();
    assertEquals("b", labelNode.getString());

    Node statements = labelNode.getLastChild();
    Node breakStatement = statements.getLastChild();
    Node breakLabel = breakStatement.getFirstChild();
    assertEquals("b", breakLabel.getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithLabelInsideBlock() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new Compiler();
    compiler.addPass(new RenameLabels(compiler));

    String jsCode = "function foo() {" +
        "  if (true) {" +
        "    a: {break a;}" +
        "  }}"
        + "}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node label = root.getFirstChild();
    assertEquals("a", label.getString());

    Node ifStatement = root.getFirstChild().getLastChild();
    Node statements = ifStatement.getFirstChild();
    Node block = statements.getLastChild();
    Node labelNode = block.getFirstChild();
    assertEquals("a", labelNode.getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithLabelInsideSwitch() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new Compiler();
    compiler.addPass(new RenameLabels(compiler));

    String jsCode = "function foo() {" +
        "  switch (1) {" +
        "    case 0:" +
        "      a: {break a;}" +
        "  }}"
        + "}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node label = root.getFirstChild();
    assertEquals("a", label.getString());

    Node switchStatement = root.getFirstChild().getLastChild();
    Node labelNode = switchStatement.getFirstChild().getLastChild();
    assertEquals("a", labelNode.getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithLabelInsideCondition() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new Compiler();
    compiler.addPass(new RenameLabels(compiler));

    String jsCode = "function foo() {" +
        "  a: {if (true) {break a;}}"
        + "}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node label = root.getFirstChild();
    assertEquals("a", label.getString());

    Node ifStatement = label.getLastChild();
    Node breakStatement = ifStatement.getLastChild();
    Node breakLabel = breakStatement.getFirstChild();
    assertEquals("a", breakLabel.getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithLabelInsideLoop() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new Compiler();
    compiler.addPass(new RenameLabels(compiler));

    String jsCode = "function foo() {" +
        "  for (var i = 0; i < 1; i++) {" +
        "    a: {break a;}" +
        "  }}"
        + "}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node label = root.getFirstChild();
    assertEquals("a", label.getString());

    Node forStatement = label.getLastChild();
    Node breakStatement = forStatement.getLastChild();
    Node breakLabel = breakStatement.getFirstChild();
    assertEquals("a", breakLabel.getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithLabelInsideLoopBody() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new Compiler();
    compiler.addPass(new RenameLabels(compiler));

    String jsCode = "function foo() {" +
        "  for (var i = 0; i < 1; i++) {" +
        "    a: {var a = 0; break a;}" +
        "  }}"
        + "}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node label = root.getFirstChild();
    assertEquals("a", label.getString());

    Node forStatement = label.getLastChild();
    Node statement = forStatement.getFirstChild();
    Node declaration = statement.getLastChild();
    Node labelNode = declaration.getLastChild();
    assertEquals("a", labelNode.getString());

    Node breakStatement = forStatement.getLastChild().getLastChild();
    Node breakLabel = breakStatement.getFirstChild();
    assertEquals("a", breakLabel.getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithLabelInsideBranch() {
    SourceFile source = new SourceFile("Test.js", "test");

    Compiler compiler = new Compiler();
    compiler.addPass(new RenameLabels(compiler));

    String jsCode = "function foo() {" +
        "  if (true) {" +
        "    a: {break a;}" +
        "  } else {" +
        "    b: {break b;}" +
        "  }}"
        + "}";

    Node root = compiler.parseTree(source, jsCode, null).getTree();
    compiler.process(source, root);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new ProcessLabels().traverse(traversal);

    assertNotNull(root.getFirstChild());
    Node labelA = root.getFirstChild();
    assertEquals("a", labelA.getString());

    Node ifStatement = labelA.getLastChild();
    Node breakStatement = ifStatement.getLastChild();
    Node breakLabel = breakStatement.getFirstChild();
    assertEquals("a", breakLabel.getString());

    Node elseStatement = ifStatement.getPreviousSibling();
    Node labelB = elseStatement.getFirstChild();
    assertEquals("b", labelB.getString());

    Node breakStatementB = elseStatement.getLastChild().getLastChild();
    Node breakLabelB = breakStatementB.getFirstChild();
    assertEquals("b", breakLabelB.getString());
  }
}