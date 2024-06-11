import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @org.junit.Test
  public void testRenameLabels() {
    Compiler compiler = new Compiler();
    RenameLabels renameLabels = new RenameLabels(compiler);

    String sourceCode = "function bar() {" +
        "  a: {" +
        "    b: {" +
        "      foo();" +
        "    }" +
        "  }" +
        "  a: {" +
        "    b: break a;" +
        "  }" +
        "}";

    Node externs = Node.external();
    Node root = Node.parse(sourceCode);

    renameLabels.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    // Check that the labels have been renamed correctly
    Node expectedLabelNames = Node.objectLiteral();
    expectedLabelNames.setProperty("a", Node.string("labelA"));
    expectedLabelNames.setProperty("b", Node.string("labelB"));

    Map<String, Object> actualLabelNames = new HashMap<>();
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.OBJECT_LITERAL) {
        for (Node property = currentNode.getFirstChild();
             property != null;
             property = property.getNextSibling()) {
          if (property.getType() == Token.PROPERTIES) {
            for (Node nameNode = property.getFirstChild();
                 nameNode != null;
                 nameNode = nameNode.getNextSibling()) {
              if (nameNode.getType() == Token.IDENTIFIER) {
                String name = nameNode.getString();
                Object actualLabelInfo = processLabels.namespaceStack.peek()
                    .renameMap.get(name);
                actualLabelNames.put(name, actualLabelInfo);
              }
            }
          }
        }
      }
      currentNode = currentNode.getParent();
    }

    assertEquals(expectedLabelNames, Node.objectLiteral().setProperty("a",
        Node.objectLiteral().setProperty("labelA", Node.objectLiteral())));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testRenameLabelWithKeyword() {
    Compiler compiler = new Compiler();
    RenameLabels renameLabels = new RenameLabels(compiler);

    String sourceCode = "function bar() {" +
        "  continue: {" +
        "    break continue;" +
        "  }" +
        "}";

    Node externs = Node.external();
    Node root = Node.parse(sourceCode);

    renameLabels.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testRenameLabelWithDuplicateName() {
    Compiler compiler = new Compiler();
    RenameLabels renameLabels = new RenameLabels(compiler);

    String sourceCode = "function bar() {" +
        "  a: {" +
        "    a: break a;" +
        "  }" +
        "}";

    Node externs = Node.external();
    Node root = Node.parse(sourceCode);

    renameLabels.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);
  }

  @org.junit.Test
  public void testRemoveUnusedLabel() {
    Compiler compiler = new Compiler();
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);

    String sourceCode = "function bar() {" +
        "  a: {" +
        "    b: break a;" +
        "  }" +
        "  a: {" +
        "  }" +
        "}";

    Node externs = Node.external();
    Node root = Node.parse(sourceCode);

    renameLabels.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    // Check that the unused label has been removed
    Node actualLabels = Node.objectLiteral();
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.OBJECT_LITERAL) {
        for (Node property = currentNode.getFirstChild();
             property != null;
             property = property.getNextSibling()) {
          if (property.getType() == Token.PROPERTIES) {
            for (Node nameNode = property.getFirstChild();
                 nameNode != null;
                 nameNode = nameNode.getNextSibling()) {
              if (nameNode.getType() == Token.IDENTIFIER) {
                String name = nameNode.getString();
                Object actualLabelInfo = processLabels.namespaceStack.peek()
                    .renameMap.get(name);
                if (actualLabelInfo != null && !actualLabelInfo.referenced) {
                  fail("Unused label '" + name + "' should have been removed.");
                }
              }
            }
          }
        }
      }
      currentNode = currentNode.getParent();
    }
  }

  @org.junit.Test
  public void testRenameLabelInsideBlock() {
    Compiler compiler = new Compiler();
    RenameLabels renameLabels = new RenameLabels(compiler);

    String sourceCode = "function bar() {" +
        "  if (true) {" +
        "    a: {" +
        "      b: break a;" +
        "    }" +
        "  }" +
        "}";

    Node externs = Node.external();
    Node root = Node.parse(sourceCode);

    renameLabels.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    // Check that the label has been renamed correctly
    Node expectedLabelNames = Node.objectLiteral();
    expectedLabelNames.setProperty("a", Node.objectLiteral().setProperty("labelA", Node.objectLiteral()));
    Node actualLabelNames = NodeUtil.getDeepChild(root, "if", "boolean", "true", 0).asObjectLiteral().getNamedChild("a").asObjectLiteral();
    assertEquals(expectedLabelNames, actualLabelNames);
  }

  @org.junit.Test
  public void testRenameLabelInsideLoop() {
    Compiler compiler = new Compiler();
    RenameLabels renameLabels = new RenameLabels(compiler);

    String sourceCode = "function bar() {" +
        "  for (var i = 0; i < 5; i++) {" +
        "    a: {" +
        "      b: break a;" +
        "    }" +
        "  }" +
        "}";

    Node externs = Node.external();
    Node root = Node.parse(sourceCode);

    renameLabels.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    // Check that the label has been renamed correctly
    Node expectedLabelNames = Node.objectLiteral();
    expectedLabelNames.setProperty("a", Node.objectLiteral().setProperty("labelA", Node.objectLiteral()));
    Node actualLabelNames = NodeUtil.getDeepChild(root, "for", "VariableDeclarationList", 0, "VariableDeclarations", 0, "VariableDeclaration", 0, "Identifier", 0).asObjectLiteral().getNamedChild("a").asObjectLiteral();
    assertEquals(expectedLabelNames, actualLabelNames);
  }

  @org.junit.Test
  public void testRenameLabelInsideSwitch() {
    Compiler compiler = new Compiler();
    RenameLabels renameLabels = new RenameLabels(compiler);

    String sourceCode = "function bar() {" +
        "  switch (true) {" +
        "    case 0:" +
        "      a: {" +
        "        b: break a;" +
        "      }" +
        "  }" +
        "}";

    Node externs = Node.external();
    Node root = Node.parse(sourceCode);

    renameLabels.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    // Check that the label has been renamed correctly
    Node expectedLabelNames = Node.objectLiteral();
    expectedLabelNames.setProperty("a", Node.objectLiteral().setProperty("labelA", Node.objectLiteral()));
    Node actualLabelNames = NodeUtil.getDeepChild(root, "switch", "ExpressionStatement", 0, "Expression", 0, "ObjectLiteral", 0, "Properties", 0, "Property", 0, "Identifier", 0).asObjectLiteral().getNamedChild("a").asObjectLiteral();
    assertEquals(expectedLabelNames, actualLabelNames);
  }
}