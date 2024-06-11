import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.EMPTY_LIST;
import static com.google.javascript.rhino.NodeFlag.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CollapseVariableDeclarationsTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testCollapsingSingleDeclaration() {
    Node root = new Node(Token.PROGRAM, null);
    Node varNode = new Node(Token.VAR, null);
    varNode.setString("a");
    root.addChildToBack(varNode);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.VAR, null);
    expected.setString("a");
    Node actual = findNodeByType(root, Token.VAR);
    assertEquals(expected, actual);
  }

  @Test
  public void testCollapsingMultipleDeclarations() {
    Node root = new Node(Token.PROGRAM, null);
    Node var1 = new Node(Token.VAR, null);
    var1.setString("a");
    Node var2 = new Node(Token.VAR, null);
    var2.setString("b");
    root.addChildToBack(var1);
    root.addChildToBack(var2);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.VAR, null);
    expected.setString("a, b");
    Node actual = findNodeByType(root, Token.VAR);
    assertEquals(expected, actual.getFirstChild());
    assertEquals(VarFlag.FINAL, actual.getFirstChild().getFlags());
  }

  @Test
  public void testCollapsingMultipleAssignments() {
    Node root = new Node(Token.PROGRAM, null);
    Node var = new Node(Token.VAR, null);
    var.setString("a");
    Node assign1 = new Node(Token.ASSIGN, null);
    assign1.setRightChild(new Node(Token.BOOL, null, true));
    Node assign2 = new Node(Token.ASSIGN, null);
    assign2.setRightChild(new Node(Token.BOOL, null, true));
    var.addChildToBack(assign1);
    var.addChildToBack(assign2);
    root.addChildToBack(var);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.ASSIGN, null);
    Node actual = findNodeByType(root, Token.ASSIGN);
    assertEquals(expected, actual);
    Node varNode = actual.getFirstChild();
    Node left = varNode.getFirstChild();
    Node right = varNode.getLastChild();
    assertEquals(new Node(Token.VAR, null, left), varNode);
    assertEquals(new Node(Token.BOOL, null, true), right.getFirstChild());
    assertEquals(new Node(Token.BOOL, null, true), right.getLastChild());
  }

  @Test
  public void testCollapsingMultipleDeclarationsWithJSDocs() {
    Node root = new Node(Token.PROGRAM, null);
    Node var1 = new Node(Token.VAR, null);
    var1.setString("a");
    Node jsdoc1 = new Node(Token.JSDOCINFO, null);
    JSDocInfo info1 = new JSDocInfo();
    info1.addTag("@var", "a");
    jsdoc1.setJSDocInfo(info1);
    var1.addChildToBack(jsdoc1);
    Node var2 = new Node(Token.VAR, null);
    var2.setString("b");
    Node jsdoc2 = new Node(Token.JSDOCINFO, null);
    JSDocInfo info2 = new JSDocInfo();
    info2.addTag("@var", "b");
    jsdoc2.setJSDocInfo(info2);
    var2.addChildToBack(jsdoc2);
    root.addChildToBack(var1);
    root.addChildToBack(var2);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.VAR, null);
    expected.setString("a, b");
    Node actual = findNodeByType(root, Token.VAR);
    assertEquals(expected, actual);
    Node jsdoc = actual.getFirstChild();
    assertEquals(2, jsdoc.getChildCount());
    assertEquals(info1, jsdoc.getChild(0).getJSDocInfo());
    assertEquals(info2, jsdoc.getChild(1).getJSDocInfo());
  }

  @Test
  public void testCollapsingMultipleDeclarationsWithJSDocsAndAssignments() {
    Node root = new Node(Token.PROGRAM, null);
    Node var = new Node(Token.VAR, null);
    var.setString("a");
    Node jsdoc = new Node(Token.JSDOCINFO, null);
    JSDocInfo info = new JSDocInfo();
    info.addTag("@var", "a");
    jsdoc.setJSDocInfo(info);
    var.addChildToBack(jsdoc);
    Node assign1 = new Node(Token.ASSIGN, null);
    assign1.setRightChild(new Node(Token.BOOL, null, true));
    var.addChildToBack(assign1);
    Node var2 = new Node(Token.VAR, null);
    var2.setString("b");
    Node jsdoc2 = new Node(Token.JSDOCINFO, null);
    JSDocInfo info2 = new JSDocInfo();
    info2.addTag("@var", "b");
    jsdoc2.setJSDocInfo(info2);
    var2.addChildToBack(jsdoc2);
    Node assign2 = new Node(Token.ASSIGN, null);
    assign2.setRightChild(new Node(Token.BOOL, null, true));
    var2.addChildToBack(assign2);
    root.addChildToBack(var);
    root.addChildToBack(var2);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.ASSIGN, null);
    Node actual = findNodeByType(root, Token.ASSIGN);
    assertEquals(expected, actual);
    Node varNode = actual.getFirstChild();
    Node left = varNode.getFirstChild();
    Node right = varNode.getLastChild();
    assertEquals(new Node(Token.VAR, null, left), varNode);
    Node jsdoc = left.getFirstChild();
    assertEquals(2, jsdoc.getChildCount());
    assertEquals(info, jsdoc.getChild(0).getJSDocInfo());
    assertEquals(info2, jsdoc.getChild(1).getJSDocInfo());
  }

  @Test
  public void testCollapsingMultipleDeclarationsWithSameJSDoc() {
    Node root = new Node(Token.PROGRAM, null);
    Node var1 = new Node(Token.VAR, null);
    var1.setString("a");
    Node jsdoc = new Node(Token.JSDOCINFO, null);
    JSDocInfo info = new JSDocInfo();
    info.addTag("@var", "a");
    jsdoc.setJSDocInfo(info);
    var1.addChildToBack(jsdoc);
    Node var2 = new Node(Token.VAR, null);
    var2.setString("a");
    Node jsdoc2 = new Node(Token.JSDOCINFO, null);
    jsdoc2.setJSDocInfo(info);
    var2.addChildToBack(jsdoc2);
    root.addChildToBack(var1);
    root.addChildToBack(var2);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.VAR, null);
    expected.setString("a");
    Node actual = findNodeByType(root, Token.VAR);
    assertEquals(expected, actual);
  }

  @Test
  public void testCollapsingMultipleDeclarationsWithDifferentJSDocs() {
    Node root = new Node(Token.PROGRAM, null);
    Node var1 = new Node(Token.VAR, null);
    var1.setString("a");
    Node jsdoc = new Node(Token.JSDOCINFO, null);
    JSDocInfo info = new JSDocInfo();
    info.addTag("@var", "a1");
    jsdoc.setJSDocInfo(info);
    var1.addChildToBack(jsdoc);
    Node var2 = new Node(Token.VAR, null);
    var2.setString("a");
    Node jsdoc2 = new Node(Token.JSDOCINFO, null);
    JSDocInfo info2 = new JSDocInfo();
    info2.addTag("@var", "a2");
    jsdoc2.setJSDocInfo(info2);
    var2.addChildToBack(jsdoc2);
    root.addChildToBack(var1);
    root.addChildToBack(var2);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.VAR, null, ImmutableList.of(
        new Node(Token.JSDOCINFO, null, new Node(Token.TAG, null, new Node(Token.TAG, null, new Node(Token.IDENT, null, "var"), new Node(Token.IDENT, null, "a1")))),
        new Node(Token.VAR, null, new Node(Token.VAR, null, ImmutableList.of(
            new Node(Token.IDENT, null, "a"),
            new Node(Token.IDENT, null, "a2")
        )))
    ));
    Node actual = findNodeByType(root, Token.VAR);
    assertEquals(expected, actual);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariableThatCannotBeRedeclared() {
    Node root = new Node(Token.PROGRAM, null);
    Node var1 = new Node(Token.VAR, null);
    var1.setString("a");
    Node assign = new Node(Token.ASSIGN, null);
    assign.setRightChild(new Node(Token.BOOL, null, true));
    var1.addChildToBack(assign);
    Node var2 = new Node(Token.VAR, null);
    var2.setString("a");
    root.addChildToBack(var1);
    root.addChildToBack(var2);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    fail("Expected an AssertionError");
  }

  @Test
  public void testCollapsingVariablesInsideIfStatement() {
    Node root = new Node(Token.PROGRAM, null);
    Node ifNode = new Node(Token.IF, null);
    Node condition = new Node(Token.BOOL, null, true);
    ifNode.addChildToBack(new Node(Token.ELSE, null));
    Node var1 = new Node(Token.VAR, null);
    var1.setString("a");
    Node assign1 = new Node(Token.ASSIGN, null);
    assign1.setRightChild(new Node(Token.BOOL, null, true));
    var1.addChildToBack(assign1);
    ifNode.addChildToBack(var1);
    Node var2 = new Node(Token.VAR, null);
    var2.setString("b");
    Node assign2 = new Node(Token.ASSIGN, null);
    assign2.setRightChild(new Node(Token.BOOL, null, true));
    var2.addChildToBack(assign2);
    ifNode.addChildToBack(var2);
    root.addChildToBack(ifNode);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.VAR, null);
    Node actual = findNodeByType(root, Token.VAR);
    assertEquals(expected, actual);
    Node varNode = actual.getFirstChild();
    Node left = varNode.getFirstChild();
    Node right = varNode.getLastChild();
    assertEquals(new Node(Token.IF, null, ImmutableList.of(condition)), left);
    assertEquals(new Node(Token.VAR, null, ImmutableList.of(
        new Node(Token.VAR, null, ImmutableList.of(
            new Node(Token.IDENT, null, "a"),
            new Node(Token.IDENT, null, "b")
        )),
        new Node(Token.ASSIGN, null, new Node(Token.BOOL, null, true))
    )), right);
  }

  private Node findNodeByType(Node node, int type) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.getType() == type) {
        return child;
      }
    }
    return null;
  }
}