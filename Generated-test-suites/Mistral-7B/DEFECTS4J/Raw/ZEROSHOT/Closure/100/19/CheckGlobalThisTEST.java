import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node code = compiler.parseTree("function evil() { this.useful = undefined; }");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();

    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals(DiagnosticType.WARNING, diagnostic.getSeverity());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType());
  }

  @Test
  public void testCheckGlobalThisConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node code = compiler.parseTree("function Evil() {}");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisPrototype() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node code = compiler.parseTree("function Evil() {} Evil.prototype.useful = function() {};");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node code = compiler.parseTree("@this function Evil() {}");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisNoAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node code = compiler.parseTree("function Evil() {} var a = function() {};");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node code = compiler.parseTree("var a = this;");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisAssignmentInsideFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node code = compiler.parseTree("function evil() { var a = this; }");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node code = compiler.parseTree("function evil() { this.useful = undefined; }");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();

    assertEquals(1, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisPropertyAccessInsideFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node code = compiler.parseTree("function evil() { this.useful = undefined; } function useful() {}");
    NodeTraversal traversal = new NodeTraversal(code, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }
}