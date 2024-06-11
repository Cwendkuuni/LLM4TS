import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private static abstract class TestBase {
    protected Compiler compiler;
    protected NodeFactory nodeFactory;

    TestBase() {
      compiler = new TestCompiler();
      compiler.setOptions(getOptions());
      nodeFactory = compiler.getFactory();
    }

    abstract CompilerOptions getOptions();

    Node getLabelStatement() {
      Node label = nodeFactory.createLabel("a");
      Node statement = nodeFactory.createStatement(label);
      return statement;
    }

    Node getBreakStatement(String labelName) {
      Node label = nodeFactory.createLabel(labelName);
      Node statement = nodeFactory.createStatement(nodeFactory.createBreakStatement(nodeFactory.createIdentifier("x")));
      statement.addChildToStatementList(label);
      return statement;
    }

    Node getContinueStatement(String labelName) {
      Node label = nodeFactory.createLabel(labelName);
      Node statement = nodeFactory.createStatement(nodeFactory.createContinueStatement(nodeFactory.createIdentifier("x")));
      statement.addChildToStatementList(label);
      return statement;
    }

    Node getLabelStatementWithStatementBlock() {
      Node label = nodeFactory.createLabel("a");
      Node block = nodeFactory.createBlock();
      Node statement = nodeFactory.createStatement(nodeFactory.createExpressionStatement(nodeFactory.createPrintStatement("Hello World!")));
      block.addChildToStatementList(statement);
      label.addChildToStatementList(block);
      return label;
    }

    Node getLabelStatementWithStatementList() {
      Node label = nodeFactory.createLabel("a");
      Node statement1 = nodeFactory.createStatement(nodeFactory.createPrintStatement("Statement 1"));
      Node statement2 = nodeFactory.createStatement(nodeFactory.createPrintStatement("Statement 2"));
      Node statementList = nodeFactory.createStatementList();
      statementList.addChildToStatementList(statement1);
      statementList.addChildToStatementList(statement2);
      label.addChildToStatementList(statementList);
      return label;
    }

    Node getLabelStatementWithInsideLabel() {
      Node labelOuter = nodeFactory.createLabel("outer");
      Node labelInner = nodeFactory.createLabel("inner");
      Node statement = nodeFactory.createStatement(nodeFactory.createPrintStatement("Hello World!"));
      Node block = nodeFactory.createBlock();
      block.addChildToStatementList(labelInner);
      labelOuter.addChildToStatementList(block);
      labelOuter.addChildToStatementList(statement);
      return labelOuter;
    }

    Node getLabelStatementWithNestedLabel() {
      Node labelOuter = nodeFactory.createLabel("outer");
      Node labelInner = nodeFactory.createLabel("inner");
      Node statementOuter = nodeFactory.createStatement(nodeFactory.createPrintStatement("Statement Outer"));
      Node statementInner = nodeFactory.createStatement(nodeFactory.createPrintStatement("Statement Inner"));
      Node blockInner = nodeFactory.createBlock();
      blockInner.addChildToStatementList(labelInner);
      labelOuter.addChildToStatementList(blockInner);
      labelOuter.addChildToStatementList(statementOuter);
      labelOuter.addChildToStatementList(statementInner);
      return labelOuter;
    }

    Node getLabelStatementWithKeyword() {
      Node label = nodeFactory.createLabel("return");
      Node statement = nodeFactory.createStatement(nodeFactory.createPrintStatement("Hello World!"));
      label.addChildToStatementList(statement);
      return label;
    }

    Map<String, String> getExpectedRenameMap(int depth) {
      Map<String, String> map = new HashMap<>();
      for (int i = 0; i < depth; i++) {
        map.put("a" + i, "b" + i);
      }
      return map;
    }
  }

  @org.junit.Test
  public void testRenameLabels() {
    TestBase test = new TestLabelRenameNoUnused();

    Node root = test.nodeFactory.createBlock();

    // Test renaming a simple label
    Node label = test.getLabelStatement();
    Node expected = test.nodeFactory.createLabel("b0");
    label.setString(expected.getString());
    test.compiler.process(test.nodeFactory.getExterns(), root);
    Node actual = label;
    assertEquals(expected, actual);

    // Test renaming a label in a break statement
    Node breakStatement = test.getBreakStatement("a");
    expected = test.nodeFactory.createLabel("b0");
    breakStatement.getFirstChild().setString(expected.getString());
    test.compiler.process(test.nodeFactory.getExterns(), root);
    actual = breakStatement.getFirstChild();
    assertEquals(expected, actual);

    // Test renaming a label in a continue statement
    Node continueStatement = test.getContinueStatement("a");
    expected = test.nodeFactory.createLabel("b0");
    continueStatement.getFirstChild().setString(expected.getString());
    test.compiler.process(test.nodeFactory.getExterns(), root);
    actual = continueStatement.getFirstChild();
    assertEquals(expected, actual);

    // Test renaming a label inside a statement block
    Node labelStatementWithBlock = test.getLabelStatementWithStatementBlock();
    Map<String, String> expectedRenameMap = test.getExpectedRenameMap(1);
    test.compiler.process(test.nodeFactory.getExterns(), root);
    actual = labelStatementWithBlock;
    testLabelRename(test, actual, expectedRenameMap);

    // Test renaming a label inside a statement list
    Node labelStatementWithStatementList = test.getLabelStatementWithStatementList();
    expectedRenameMap = test.getExpectedRenameMap(2);
    test.compiler.process(test.nodeFactory.getExterns(), root);
    actual = labelStatementWithStatementList;
    testLabelRename(test, actual, expectedRenameMap);

    // Test renaming a label inside a label
    Node labelStatementWithInsideLabel = test.getLabelStatementWithInsideLabel();
    expectedRenameMap = test.getExpectedRenameMap(3);
    test.compiler.process(test.nodeFactory.getExterns(), root);
    actual = labelStatementWithInsideLabel;
    testLabelRename(test, actual, expectedRenameMap);

    // Test renaming a label inside a nested label
    Node labelStatementWithNestedLabel = test.getLabelStatementWithNestedLabel();
    expectedRenameMap = test.getExpectedRenameMap(4);
    test.compiler.process(test.nodeFactory.getExterns(), root);
    actual = labelStatementWithNestedLabel;
    testLabelRename(test, actual, expectedRenameMap);

    // Test renaming a label with a keyword
    Node labelStatementWithKeyword = test.getLabelStatementWithKeyword();
    test.assertCompilationError(compiler -> {
      test.compiler.process(test.nodeFactory.getExterns(), root);
      test.compilationErrorMessageEquals(compiler, "LABEL_NAME_CONFLICT", "Keyword 'return' is not a valid label name.");
    }, labelStatementWithKeyword);

  }

  private void testLabelRename(TestBase test, Node actual, Map<String, String> expectedRenameMap) {
    Map<String, LabelInfo> detectedRenameMap = new HashMap<>();
    NodeTraversal nodeTraversal = new NodeTraversal(test.compiler.getFactory(), new ProcessLabels() {
      @Override
      public void visitLabel(Node node, Node parent) {
        super.visitLabel(node, parent);
        LabelInfo li = getLabelInfo(node);
        detectedRenameMap.put(li.name, li);
      }
    });
    nodeTraversal.traverse(test.compiler, actual, null);
    for (Map.Entry<String, String> entry : expectedRenameMap.entrySet()) {
      String key = entry.getKey();
      LabelInfo detectedLi = detectedRenameMap.get(key);
      assertEquals(key, detectedLi.name);
      assertEquals(entry.getValue(), detectedLi.renamedName);
    }
  }

  private LabelInfo getLabelInfo(Node node) {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler.getFactory(), new ProcessLabels());
    nodeTraversal.traverse(compiler, node, null);
    return nodeTraversal.getCurrentLabelInfo();
  }

  private void assertCompilationError(TestBase test, Runnable runnable, String errorMessage) {
    try {
      runnable.run();
      fail("Expected compilation error.");
    } catch (AssertionError e) {
      Map<String, String> compilationErrors = test.compiler.getDiagnostics();
      String error = compilationErrors.get(errorMessage).getErrorMessage();
      assertEquals(errorMessage, error);
    }
  }

  private void compilationErrorMessageEquals(Compiler compiler, String errorMessage, String expectedErrorMessage) {
    Map<String, String> compilationErrors = compiler.getDiagnostics();
    String error = compilationErrors.get(errorMessage).getErrorMessage();
    assertEquals(expectedErrorMessage, error);
  }

  private static class TestLabelRenameNoUnused extends TestBase {
    @Override
    CompilerOptions getOptions() {
      return new CompilerOptions();
    }
  }

  private static class TestLabelRenameWithUnused extends TestBase {
    @Override
    CompilerOptions getOptions() {
      CompilerOptions options = new CompilerOptions();
      options.setRemoveUnused(false);
      return options;
    }
  }
}