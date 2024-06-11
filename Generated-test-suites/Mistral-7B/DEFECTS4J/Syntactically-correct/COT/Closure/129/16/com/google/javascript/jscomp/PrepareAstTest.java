import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.PrepareAst.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.IR;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

  AbstractCompiler compiler;
  CompilerPassManager passManager;

  @Before
  public void setup() {
    compiler = new Compiler();
    passManager = new CompilerPassManager(compiler);
  }

  @Test
  public void testProcess() {
    Node externs = NodeOperations.createTextNode("");
    Node root = NodeOperations.createTextNode("");

    passManager.runPasses(externs, root, this.passManager.getPasses());

    // Basic functionality test
    assertTrue(PrepareAst.class.isInstance(passManager.getPasses().get(0)));
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node root = NodeOperations.createBlock();
    Node child1 = NodeOperations.createIfStatement();
    Node child2 = NodeOperations.createFunctionDeclaration("test", NodeOperations.createBlock());
    root.addChildrenToFront(child1, child2);

    PrepareAst pa = new PrepareAst(compiler, false);
    pa.process(null, root);

    // Basic functionality test
    assertNotNull(child1.getFirstChild());
    assertNotNull(child2.getFirstChild());

    // Edge case: empty if statement
    Node emptyIf = NodeOperations.createIfStatement();
    PrepareAst.PrepareAnnotations pp = new PrepareAst.PrepareAnnotations();
    NodeTraversal.traverse(compiler, emptyIf, pp);
    assertEquals(Node.IS_CONTROL_STRUCTURE, emptyIf.getType());
    assertTrue(emptyIf.isControlStructure());
    assertFalse(emptyIf.isBlock());

    // Exception handling: null node
    Node nullNode = null;
    try {
      pa.normalizeNodeTypes(nullNode);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testNormalizeBlocks() {
    Node root = NodeOperations.createBlock();
    Node ifStatement = NodeOperations.createIfStatement();
    Node ifBlock = NodeOperations.createBlock();
    Node elseBlock = NodeOperations.createBlock();
    root.addChildrenToFront(ifStatement);
    ifStatement.addChildrenToFront(ifBlock, elseBlock);

    PrepareAst pa = new PrepareAst(compiler, false);
    pa.process(null, root);

    // Basic functionality test
    assertTrue(ifBlock.isBlock());
    assertTrue(elseBlock.isBlock());

    // Edge case: empty if statement
    Node emptyIf = NodeOperations.createIfStatement();
    PrepareAst.PrepareAnnotations pp = new PrepareAst.PrepareAnnotations();
    NodeTraversal.traverse(compiler, emptyIf, pp);
    assertFalse(emptyIf.isBlock());

    // Exception handling: null node
    Node nullNode = null;
    try {
      pa.normalizeBlocks(nullNode);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testPrepareAnnotations() {
    Node objLit = NodeOperations.createObjectLiteral();
    Node key1 = NodeOperations.createName("a");
    Node value1 = NodeOperations.createStringLiteral("b");
    Node key2 = NodeOperations.createName("c");
    Node value2 = NodeOperations.createFunctionDeclaration("test", NodeOperations.createBlock());
    objLit.addChildrenToFront(key1, value1, key2, value2);

    PrepareAst pa = new PrepareAst(compiler, false);
    pa.process(null, objLit);

    // Basic functionality test
    assertEquals(5, objLit.getNumChildren());

    // Edge case: empty object literal
    Node emptyObjLit = NodeOperations.createObjectLiteral();
    PrepareAst.PrepareAnnotations pp = new PrepareAst.PrepareAnnotations();
    NodeTraversal.traverse(compiler, emptyObjLit, pp);
    assertEquals(0, emptyObjLit.getNumChildren());

    // Edge case: existing JSDoc annotations
    String jsdocAnnotation = "/**";
    objLit.setJSDocInfo(jsdocAnnotation);
    PrepareAst.PrepareAnnotations ppAnnotated = new PrepareAst.PrepareAnnotations();
    NodeTraversal.traverse(compiler, objLit, ppAnnotated);
    assertEquals(jsdocAnnotation, objLit.getJSDocInfo());

    // Exception handling: null node
    Node nullNode = null;
    try {
      pa.normalizeObjectLiteralAnnotations(nullNode);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // expected
    }
  }
}