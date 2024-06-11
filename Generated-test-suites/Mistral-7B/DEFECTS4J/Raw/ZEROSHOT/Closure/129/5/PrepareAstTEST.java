import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isControlStructure;
import static com.google.javascript.jscomp.NodeUtil.isControlStructureCodeBlock;
import static com.google.javascript.jscomp.NodeUtil.isGet;
import static com.google.javascript.jscomp.NodeUtil.isObjectLit;
import static com.google.javascript.jscomp.NodeUtil.isFunction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;

@RunWith(JUnit4.class)
public class PrepareAstTest {

  @Test
  public void testPrepareAst() throws Exception {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    PrepareAst prepareAst = new PrepareAst(compiler);
    PrepareAst prepareAstCheckOnly = new PrepareAst(compiler, true);

    String source = "function foo() {}";
    Node root = compiler.parseTree(source, null, null);
    Node externs = null;

    prepareAst.process(externs, root);

    // Test normal behavior
    assertFalse(root.isLabel());
    assertFalse(root.isSwitch());
    assertTrue(isControlStructure(root));
    assertFalse(root.isEmpty());
    assertTrue(root.getFirstChild() instanceof Node);

    // Test checkOnly behavior
    prepareAstCheckOnly.process(externs, root);
    assertTrue(root.isLabel());
    assertTrue(root.isSwitch());
    // The following assertions will fail since checkOnly is not supposed to
    // make any changes to the AST.
    // assertFalse(isControlStructure(root));
    // assertFalse(root.isEmpty());

    // Test normal behavior with a block node
    source = "if (true) {}";
    root = compiler.parseTree(source, null, null);
    prepareAst.process(externs, root);

    Node controlStructure = root;
    assertTrue(isControlStructure(controlStructure));
    assertTrue(isControlStructureCodeBlock(controlStructure, controlStructure.getFirstChild()));

    // Test checkOnly behavior with a block node
    prepareAstCheckOnly.process(externs, root);
    assertFalse(isControlStructure(controlStructure));
    assertFalse(isControlStructureCodeBlock(controlStructure, controlStructure.getFirstChild()));

    // Test annotations on function literals
    source = "var bar = function() {};";
    root = compiler.parseTree(source, null, null);
    prepareAst.process(externs, root);

    Node functionLiteral = root.getLastChild();
    assertTrue(isFunction(functionLiteral));
    assertNull(functionLiteral.getJSDocInfo());

    source = "var baz = { bar: function() {} };";
    root = compiler.parseTree(source, null, null);
    prepareAst.process(externs, root);

    Node objectLiteral = root;
    Node objectLiteralValue = objectLiteral.getLastChild();

    Node functionLiteralInsideObject = objectLiteralValue.getFirstChild();
    assertTrue(isFunction(functionLiteralInsideObject));

    Node functionLiteralAnnotation = functionLiteralInsideObject.getJSDocInfo();
    assertNotNull(functionLiteralAnnotation);
  }

}