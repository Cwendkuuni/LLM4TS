package org.apache.commons.cli;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testContextualRenamer_simple() {
    String jsCode = "function foo() { var x = 1; var x = 2; }";
    Node rootNode = parseJsCodeToNode(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(renamer);

    List<String> expectedRenamedNames = Lists.newArrayList("x_0", "x_1");
    checkRenamedNames(nodeTraversal, expectedRenamedNames);
  }

  @Test
  public void testContextualRenamer_withFunctionDeclarations() {
    String jsCode = "function foo() { function bar() {} var x = 1; function bar() {} }";
    Node rootNode = parseJsCodeToNode(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(renamer);

    List<String> expectedRenamedNames = Lists.newArrayList("bar_0", "x_0", "bar_1");
    checkRenamedNames(nodeTraversal, expectedRenamedNames);
  }

  @Test
  public void testContextualRenamer_withGlobalVariables() {
    String jsCode = "var x = 1; var x = 2;";
    Node rootNode = parseJsCodeToNode(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(renamer);

    List<String> expectedRenamedNames = Lists.newArrayList("x_0", "x_1");
    checkRenamedNames(nodeTraversal, expectedRenamedNames);
  }

  @Test
  public void testContextualRenamer_withFunctionAndGlobalVariables() {
    String jsCode = "function foo() { var x = 1; var x = 2; } var x = 1;";
    Node rootNode = parseJsCodeToNode(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(renamer);

    List<String> expectedRenamedNames = Lists.newArrayList("x_0", "x_1", "x_2");
    checkRenamedNames(nodeTraversal, expectedRenamedNames);
  }

  @Test
  public void testInlineRenamer_simple() {
    String jsCode = "function foo() { var x = 1; var x = 2; }";
    Node rootNode = parseJsCodeToNode(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique(new InlineRenamer(
        () -> "_" + System.currentTimeMillis(), "", true));
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(renamer);

    List<String> expectedRenamedNames = Lists.newArrayList("x_1584983621355_0", "x_1584983621355_1");
    checkRenamedNames(nodeTraversal, expectedRenamedNames);
  }

  @Test
  public void testInlineRenamer_withFunctionDeclarations() {
    String jsCode = "function foo() { function bar() {} var x = 1; function bar() {} }";
    Node rootNode = parseJsCodeToNode(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique(new InlineRenamer(
        () -> "_" + System.currentTimeMillis(), "", true));
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(renamer);

    List<String> expectedRenamedNames = Lists.newArrayList("bar_1584983621355_0",
                                                           "x_1584983621355_0",
                                                           "bar_1584983621355_1");
    checkRenamedNames(nodeTraversal, expectedRenamedNames);
  }

  @Test
  public void testInlineRenamer_withGlobalVariables() {
    String jsCode = "var x = 1; var x = 2;";
    Node rootNode = parseJsCodeToNode(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique(new InlineRenamer(
        () -> "_" + System.currentTimeMillis(), "", true));
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(renamer);

    List<String> expectedRenamedNames = Lists.newArrayList("x_1584983621355_0",
                                                           "x_1584983621355_1");
    checkRenamedNames(nodeTraversal, expectedRenamedNames);
  }

  @Test
  public void testInlineRenamer_withFunctionAndGlobalVariables() {
    String jsCode = "function foo() { var x = 1; var x = 2; } var x = 1;";
    Node rootNode = parseJsCodeToNode(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique(new InlineRenamer(
        () -> "_" + System.currentTimeMillis(), "", true));
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(renamer);

    List<String> expectedRenamedNames = Lists.newArrayList("x_1584983621355_0",
                                                           "x_1584983621355_1",
                                                           "x_1584983621355_2");
    checkRenamedNames(nodeTraversal, expectedRenamedNames);
  }

  private Node parseJsCodeToNode(String jsCode) {
    TokenStream tokenStream = new TokenStream(new IndentingReader(new StringReader(jsCode)));
    Node rootNode = tokenStream.readTree();
    return rootNode;
  }

  private void checkRenamedNames(NodeTraversal nodeTraversal, List<String> expectedRenamedNames) {
    Map<String, Node> nameNodeMap = new TreeMap<>();
    Iterator<Node> nodeIterator = nodeTraversal.getRootNode().iterateChildren();
    while (nodeIterator.hasNext()) {
      Node node = nodeIterator.next();
      nameNodeMap.put(node.getString(), node);
    }

    for (String name : expectedRenamedNames) {
      Node renamedNode = nameNodeMap.get(name);
      String replacementName = renamerGetReplacementName(renamerNode(renamedNode));
      assertThat(replacementName).isEqualTo(name.substring(0, name.length() - 1));
    }
  }

  private Node renamerNode(Node node) {
    if (node instanceof Node) {
      return (Node) node;
    }
    return null;
  }

  private String renamerGetReplacementName(Node node) {
    MakeDeclaredNamesUnique renamer = (MakeDeclaredNamesUnique) node.getUserData();
    return renamer.getReplacementName(node.getString());
  }
}