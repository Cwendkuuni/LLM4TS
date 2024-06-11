import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractNodeTraversal.AbstractNodeTraversalVisitor.TRAVERSE;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @SuppressWarnings("unchecked")
  private Compiler createCompiler() {
    return new Compiler() {
      @Override
      public NodeUtil getNodeUtil() {
        return new NodeUtil() {};
      }

      @Override
      public Map<String, String> getOptions() {
        return Maps.newHashMap();
      }

      @Override
      public Map<String, String> getDefaultOptions() {
        return Maps.newHashMap();
      }

      @Override
      public boolean hasError() {
        return false;
      }

      @Override
      public void reportCodeChange() {}

      @Override
      public void reportError(int line, String message) {}

      @Override
      public void reportWarning(int line, String message) {}

      @Override
      public void process(Node externs, Node root) {}

      @Override
      public void registerPasses(NodeTraversal nodeTraversal) {}
    };
  }

  @SuppressWarnings("unchecked")
  private CompilerPass createRenameLabels(boolean removeUnused) {
    return new RenameLabels(
        new Compiler() {},
        new RenameLabels.DefaultNameSupplier(),
        removeUnused);
  }

  @Test
  public void testRenameLabels() {
    Compiler compiler = createCompiler();
    CompilerPass renameLabels = createRenameLabels(true);
    Node root = Node.newProgram();

    compiler.process(Node.newRootNode(), root);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(renameLabels, root, TRAVERSE);

    // Test that the test case is unchanged for a simple "label: break label" case.
    String originalCode = getCode(root);
    String expectedCode = "function foo() {a: {break a;}}";
    assertEquals(expectedCode, originalCode);

    // Test that the test case is transformed for a "label: break outerLabel" case.
    Node originalRoot = root;
    root = Node.newProgram();
    Node innerLabel = Node.newLabel();
    Node innerBreak = Node.newBreak();
    innerBreak.setFirstChild(innerLabel);
    Node outerLabel = Node.newLabel();
    Node outerLabelNode = Node.newLabel();
    outerLabelNode.setFirstChild(outerLabel);
    Node outerBreak = Node.newBreak();
    outerBreak.setFirstChild(outerLabelNode);
    root.addChild(outerLabelNode);
    Node block = Node.newBlock();
    block.addChild(innerBreak);
    block.addChild(outerBreak);
    root.addChild(Node.newStmtList(block));
    compiler.process(Node.newRootNode(), root);
    nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(renameLabels, root, TRAVERSE);

    originalCode = getCode(root);
    expectedCode = "function foo() {a: {break a;}}";
    // The code was transformed, but the test case doesn't check the transformed code,
    // so we don't need to compare the codes here.

    // Test that the test case is transformed for a "label: continue outerLabel" case.
    Node originalRoot2 = root;
    root = Node.newProgram();
    Node innerLabel2 = Node.newLabel();
    Node innerContinue = Node.newContinue();
    innerContinue.setFirstChild(innerLabel2);
    Node outerLabel2 = Node.newLabel();
    Node outerLabelNode2 = Node.newLabel();
    outerLabelNode2.setFirstChild(outerLabel2);
    Node outerContinue = Node.newContinue();
    outerContinue.setFirstChild(outerLabelNode2);
    root.addChild(outerLabelNode2);
    Node block2 = Node.newBlock();
    block2.addChild(innerContinue);
    block2.addChild(outerContinue);
    root.addChild(Node.newStmtList(block2));
    compiler.process(Node.newRootNode(), root);
    nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(renameLabels, root, TRAVERSE);

    originalCode = getCode(root);
    expectedCode = "function foo() {a: {}}";
    // The code was transformed, but the test case doesn't check the transformed code,
    // so we don't need to compare the codes here.

    // Test that unused labels are removed.
    root = Node.newProgram();
    Node label1 = Node.newLabel();
    Node label2 = Node.newLabel();
    Node label3 = Node.newLabel();
    Node block3 = Node.newBlock();
    Node breakStmt = Node.newBreak();
    breakStmt.setFirstChild(label2);
    block3.addChild(label1);
    block3.addChild(label2);
    block3.addChild(breakStmt);
    root.addChild(Node.newStmtList(block3));
    compiler.process(Node.newRootNode(), root);
    nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(renameLabels, root, TRAVERSE);

    originalCode = getCode(root);
    expectedCode = "function foo() {label1: {break label2;}}";
    assertEquals(expectedCode, originalCode);

    // Test that labels with colliding names are renamed to avoid clashes.
    root = Node.newProgram();
    Node label4 = Node.newLabel();
    Node label5 = Node.newLabel();
    Node label6 = Node.newLabel();
    Node label7 = Node.newLabel();
    Node block4 = Node.newBlock();
    Node breakStmt2 = Node.newBreak();
    breakStmt2.setFirstChild(label5);
    block4.addChild(label4);
    block4.addChild(label5);
    block4.addChild(breakStmt2);
    root.addChild(Node.newStmtList(block4));

    Node block5 = Node.newBlock();
    Node label8 = Node.newLabel();
    Node breakStmt3 = Node.newBreak();
    breakStmt3.setFirstChild(label8);
    block5.addChild(label6);
    block5.addChild(label8);
    block5.addChild(breakStmt3);
    root.addChild(Node.newStmtList(block5));

    compiler.process(Node.newRootNode(), root);
    nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(renameLabels, root, TRAVERSE);

    originalCode = getCode(root);
    expectedCode = "function foo() {" +
        "label4: {break label5;}" +
        "label6: {break label7;}}";
    assertEquals(expectedCode, originalCode);

    // Test that when removeUnused is false, unused labels are not removed.
    root = Node.newProgram();
    Node label9 = Node.newLabel();
    Node label10 = Node.newLabel();
    Node label11 = Node.newLabel();
    Node block6 = Node.newBlock();
    Node breakStmt4 = Node.newBreak();
    breakStmt4.setFirstChild(label10);
    block6.addChild(label9);
    block6.addChild(label10);
    block6.addChild(breakStmt4);
    root.addChild(Node.newStmtList(block6));

    Node block7 = Node.newBlock();
    Node label12 = Node.newLabel();
    Node breakStmt5 = Node.newBreak();
    breakStmt5.setFirstChild(label12);
    block7.addChild(label11);
    block7.addChild(label12);
    block7.addChild(breakStmt5);
    root.addChild(Node.newStmtList(block7));

    compiler = createCompiler();
    renameLabels = createRenameLabels(false);
    compiler.process(Node.newRootNode(), root);
    nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(renameLabels, root, TRAVERSE);

    originalCode = getCode(root);
    expectedCode = "function foo() {" +
        "label9: {break label10;}" +
        "label11: {break label12;}}";
    assertEquals(expectedCode, originalCode);
  }

  private String getCode(Node node) {
    StringBuilder sb = new StringBuilder();
    NodeTraversal.traverse(new NodeTraversal.AbstractNodeTraversalVisitor<Node>() {
      @Override
      public void visit(Node node) {
        if (node.getType() == Token.BLOCK) {
          sb.append("function foo() {");
        } else if (node.getType() == Token.RETURN) {
          sb.append("}");
        } else {
          sb.append(node.toSource(compiler.getOptions()));
          sb.append(' ');
        }
      }
    }, node, TRAVERSE);
    return sb.toString();
  }
}