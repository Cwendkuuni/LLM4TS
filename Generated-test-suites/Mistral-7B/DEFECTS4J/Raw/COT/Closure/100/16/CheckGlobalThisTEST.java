import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String jsCode = "function evil() { this.useful = undefined; }";
    Node root = ParserTestUtil.parse(jsCode);

    AbstractCompiler compiler = newAbstractCompilerWithChecks(CheckLevel.WARNING);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(root, null);

    Node errorNode = findFirstErrorNode(root);
    assertNotNull(errorNode);
    assertEquals(JSC_USED_GLOBAL_THIS, getDiagnosticType(errorNode));
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    String jsCode = "function EvilConstructor() { this.useful = undefined; }";
    Node root = ParserTestUtil.parse(jsCode);

    AbstractCompiler compiler = newAbstractCompilerWithChecks(CheckLevel.WARNING);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(root, null);

    Node errorNode = findFirstErrorNode(root);
    assertNull(errorNode);
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() {
    String jsCode = "var EvilPrototype = {}; EvilPrototype.prototype.evilPrototype = function() { this.useful = undefined; }";
    Node root = ParserTestUtil.parse(jsCode);

    AbstractCompiler compiler = newAbstractCompilerWithChecks(CheckLevel.WARNING);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(root, null);

    Node errorNode = findFirstErrorNode(root);
    assertNull(errorNode);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    String jsCode = "function evil() { this.useful = undefined; }";
    Node root = ParserTestUtil.parse(jsCode);

    AbstractCompiler compiler = newAbstractCompilerWithChecks(CheckLevel.ERROR);
    new CheckGlobalThis(compiler, CheckLevel.ERROR).process(root, null);

    Node errorNode = findFirstErrorNode(root);
    assertNotNull(errorNode);
    assertEquals(JSC_USED_GLOBAL_THIS, getDiagnosticType(errorNode));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithFunctionDeclaration() {
    String jsCode = "function evil() { function innerEvil() { this.useful = undefined; } }";
    Node root = ParserTestUtil.parse(jsCode);

    AbstractCompiler compiler = newAbstractCompilerWithChecks(CheckLevel.ERROR);
    new CheckGlobalThis(compiler, CheckLevel.ERROR).process(root, null);

    Node errorNode = findFirstErrorNode(root);
    assertNull(errorNode);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithVarDeclaration() {
    String jsCode = "function evil() { var innerEvil = function() { this.useful = undefined; } }";
    Node root = ParserTestUtil.parse(jsCode);

    AbstractCompiler compiler = newAbstractCompilerWithChecks(CheckLevel.ERROR);
    new CheckGlobalThis(compiler, CheckLevel.ERROR).process(root, null);

    Node errorNode = findFirstErrorNode(root);
    assertNull(errorNode);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssignment() {
    String jsCode = "function evil() { var a = this; }";
    Node root = ParserTestUtil.parse(jsCode);

    AbstractCompiler compiler = newAbstractCompilerWithChecks(CheckLevel.WARNING);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(root, null);

    Node errorNode = findFirstErrorNode(root);
    assertNull(errorNode);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithPropertyAccess() {
    String jsCode = "function evil() { this.prototype.useful = undefined; }";
    Node root = ParserTestUtil.parse(jsCode);

    AbstractCompiler compiler = newAbstractCompilerWithChecks(CheckLevel.WARNING);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(root, null);

    Node errorNode = findFirstErrorNode(root);
    assertNotNull(errorNode);
    assertEquals(JSC_USED_GLOBAL_THIS, getDiagnosticType(errorNode));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithPrototypePropertyAccess() {
    String jsCode = "function evil() { this.prototype.prototype.useful = undefined; }";
    Node root = ParserTestUtil.parse(jsCode);

    AbstractCompiler compiler = newAbstractCompilerWithChecks(CheckLevel.WARNING);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(root, null);

    Node errorNode = findFirstErrorNode(root);
    assertNull(errorNode);
  }

  private Node findFirstErrorNode(Node root) {
    for (NodeTraversal traversal = root.traverse(new CheckGlobalThis(new AbstractCompiler(null, null), CheckLevel.WARNING)); traversal.moveNext();) {
      if (traversal.getCurrentNode().getDiagnosticMessages().length > 0) {
        return traversal.getCurrentNode();
      }
    }
    return null;
  }

  private DiagnosticType getDiagnosticType(Node node) {
    if (node.getDiagnosticMessages().length > 0) {
      return node.getDiagnosticMessages()[0].getType();
    }
    return null;
  }

  private AbstractCompiler newAbstractCompilerWithChecks(CheckLevel checkLevel) {
    return new AbstractCompiler(null, null) {
      @Override
      public boolean hasCheck(DiagnosticType type) {
        return CheckGlobalThis.GLOBAL_THIS.equals(type);
      }

      @Override
      public boolean shouldRunCheck(DiagnosticType type, CheckLevel checkLevel) {
        return checkLevel.compareTo(CheckLevel.OFF) >= 0;
      }
    };
  }
}