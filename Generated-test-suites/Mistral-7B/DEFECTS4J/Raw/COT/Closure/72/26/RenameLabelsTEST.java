import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @SuppressWarnings({"unchecked", "rawtypes"})
  private AbstractCompiler compiler = new AbstractCompiler() {};
  private RenameLabels renameLabels;

  @org.junit.Before
  public void setup() {
    renameLabels = new RenameLabels(compiler);
  }

  @org.junit.Test
  public void testSimpleLabel() {
    Node root = Node.createString("function foo() { a: break a; }");
    renameLabels.process(Node.SCOPE, root);
    Node labelNode = root.getFirstChild().getLastChild().getLastChild();
    String expectedLabelName = "a";
    String actualLabelName = labelNode.getString();
    assertEquals(expectedLabelName, actualLabelName);
  }

  @org.junit.Test
  public void testLabelWithNewName() {
    Node root = Node.createString("function foo() { b: break b; }");
    renameLabels.process(Node.SCOPE, root);
    Node labelNode = root.getFirstChild().getLastChild().getLastChild();
    String expectedLabelName = "b";
    String actualLabelName = labelNode.getString();
    assertEquals(expectedLabelName, actualLabelName);
  }

  @org.junit.Test
  public void testUnusedLabel() {
    Node root = Node.createString("function foo() { a: {}}");
    renameLabels.process(Node.SCOPE, root);
    Node labelNode = root.getFirstChild().getLastChild().getLastChild();
    assertTrue(labelNode.getParent().hasBeenRemoved());
  }

  @org.junit.Test
  public void testLabelNesting() {
    Node root = Node.createString("function foo() { a: { b: {}} }");
    renameLabels.process(Node.SCOPE, root);
    Node labelNodeA = root.getFirstChild().getLastChild().getLastChild();
    Node labelNodeB = labelNodeA.getLastChild().getLastChild();
    String expectedLabelNameA = "a";
    String actualLabelNameA = labelNodeA.getString();
    assertEquals(expectedLabelNameA, actualLabelNameA);
    String expectedLabelNameB = "b";
    String actualLabelNameB = labelNodeB.getString();
    assertEquals(expectedLabelNameB, actualLabelNameB);
  }

  @org.junit.Test
  public void testLabelWithKeyword() {
    Node root = Node.createString("function foo() { case: break case; }");
    try {
      renameLabels.process(Node.SCOPE, root);
      fail("Should not be able to rename a label with a keyword.");
    } catch (RuntimeException e) {
      // Expected exception.
    }
  }

  @org.junit.Test
  public void testLabelWithSameNameInDifferentScopes() {
    Node root = Node.createString("function foo() { a: { b: break a; } } function bar() { a: { b: break a; } }");
    try {
      renameLabels.process(Node.SCOPE, root);
      fail("Should not be able to rename a label with the same name in different scopes.");
    } catch (RuntimeException e) {
      // Expected exception.
    }
  }

  @org.junit.Test
  public void testLabelWithLongName() {
    Node root = Node.createString("function foo() { labelWithALongName: break labelWithALongName; }");
    renameLabels.process(Node.SCOPE, root);
    Node labelNode = root.getFirstChild().getLastChild().getLastChild();
    String expectedLabelName = "labelWithALongName";
    String actualLabelName = labelNode.getString();
    assertEquals(expectedLabelName, actualLabelName);
  }

  @org.junit.Test
  public void testLabelWithLongNameInNestedScope() {
    Node root = Node.createString("function foo() { a: { b: { labelWithALongName: break labelWithALongName; } } }");
    renameLabels.process(Node.SCOPE, root);
    Node labelNode = root.getFirstChild().getLastChild().getLastChild().getLastChild();
    String expectedLabelName = "labelWithALongName";
    String actualLabelName = labelNode.getString();
    assertEquals(expectedLabelName, actualLabelName);
  }
}