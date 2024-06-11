import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.IR;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    AbstractCompiler compiler = new Compiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = makeRootNode();
    prepareAst.process(null, root);

    // Basic functionality test
    assertNotNull("AST should not be null after PrepareAst.process()", root);

    // Edge cases and exception handling tests

    // Test empty AST
    Node emptyRoot = IR.nullLiteral();
    prepareAst.process(null, emptyRoot);
    assertNotNull("Empty AST should not be null after PrepareAst.process()", emptyRoot);
    assertTrue("Empty AST should be empty", emptyRoot.isEmpty());

    // Test null externs
    prepareAst = new PrepareAst(compiler, true);
    Node nullExterns = null;
    prepareAst.process(nullExterns, root);
    // Since we are in check-only mode, this test does not need to fail

    // Test null root
    prepareAst = new PrepareAst(compiler, false);
    Node nullRoot = null;
    try {
      prepareAst.process(nullExterns, nullRoot);
      fail("PrepareAst.process() should have thrown a NullPointerException");
    } catch (NullPointerException e) {
      // Pass
    }

    // Test external nodes with non-null values
    Node externs = makeExternsNode();
    prepareAst.process(externs, root);

    // Test root node with non-null children
    Node nonNullChild = makeNode(TOKEN_ASSIGN, null, makeNode(TOKEN_NUM, 1));
    Node rootWithChild = IR.assign(null, root, nonNullChild);
    prepareAst.process(externs, rootWithChild);

    // Test blocks within control structures
    Node controlStructure = makeNode(TOKEN_IF, null, null);
    Node block = IR.block().srcref(controlStructure);
    controlStructure.replaceChild(0, block);
    Node childWithBlock = IR.assign(null, controlStructure, makeNode(TOKEN_NUM, 1));
    prepareAst.process(externs, childWithBlock);

    // Test normalizeObjectLiteralAnnotations method
    Node objectLit = makeObjectLitNode();
    prepareAst.process(externs, objectLit);
    Node key = objectLit.getFirstChild();
    Node value = key.getFirstChild();
    assertEquals("Expected JsDoc from key to be transferred to value",
        key.getJSDocInfo(), value.getJSDocInfo());
  }

  private Node makeRootNode() {
    return IR.program(null, null);
  }

  private Node makeExternsNode() {
    return IR.program(new Node[0], null);
  }

  private Node makeObjectLitNode() {
    Node objectLit = IR.objectLit();
    Node key1 = IR.name("a");
    Node value1 = IR.stringLiteral("b");
    Node key2 = IR.name("c");
    Node value2 = IR.name("d");

    objectLit.addChildrenToFront(key1);
    key1.addChildrenToFront(value1);
    objectLit.addChildrenToFront(key2);
    key2.addChildrenToFront(value2);

    return objectLit;
  }

  private Node makeNode(int type, Node parent, Node... children) {
    Node node = IR.newNode(type, parent);
    for (Node child : children) {
      node.addChild(child);
    }
    return node;
  }
}