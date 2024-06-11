import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.CheckGlobalThis;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String jsCode = "function evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public DiagnosticGroup getDiagnostics() {
        return DiagnosticGroup.empty();
      }

      @Override
      public Node parseTree(Node root) {
        return Parser.parseCompilationUnit(jsCode, null, null, null);
      }
    };

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    Node root = compiler.parseTree(Parser.parseCompilationUnit(jsCode, null, null, null));
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().getErrorCount() == 0);

    jsCode = "function evil() { this.useful = 1; }";

    compiler = new AbstractCompiler() {
      @Override
      public DiagnosticGroup getDiagnostics() {
        return DiagnosticGroup.empty();
      }

      @Override
      public Node parseTree(Node root) {
        return Parser.parseCompilationUnit(jsCode, null, null, null);
      }
    };

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    root = compiler.parseTree(Parser.parseCompilationUnit(jsCode, null, null, null));
    traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse();

    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertTrue(compiler.getDiagnostics().hasErrorExact(diagnosticType));

    jsCode = "function evil() { this.prototype.useful = 1; }";

    compiler = new AbstractCompiler() {
      @Override
      public DiagnosticGroup getDiagnostics() {
        return DiagnosticGroup.empty();
      }

      @Override
      public Node parseTree(Node root) {
        return Parser.parseCompilationUnit(jsCode, null, null, null);
      }
    };

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    root = compiler.parseTree(Parser.parseCompilationUnit(jsCode, null, null, null));
    traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse();

    assertFalse(compiler.getDiagnostics().hasErrorExact(diagnosticType));

    jsCode = "function evil() { var a = this; this.useful = 1; }";

    compiler = new AbstractCompiler() {
      @Override
      public DiagnosticGroup getDiagnostics() {
        return DiagnosticGroup.empty();
      }

      @Override
      public Node parseTree(Node root) {
        return Parser.parseCompilationUnit(jsCode, null, null, null);
      }
    };

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    root = compiler.parseTree(Parser.parseCompilationUnit(jsCode, null, null, null));
    traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse();

    assertFalse(compiler.getDiagnostics().hasErrorExact(diagnosticType));

    jsCode = "function evil() { this.x = function() {}; }";

    compiler = new AbstractCompiler() {
      @Override
      public DiagnosticGroup getDiagnostics() {
        return DiagnosticGroup.empty();
      }

      @Override
      public Node parseTree(Node root) {
        return Parser.parseCompilationUnit(jsCode, null, null, null);
      }
    };

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    root = compiler.parseTree(Parser.parseCompilationUnit(jsCode, null, null, null));
    traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse();

    assertFalse(compiler.getDiagnostics().hasErrorExact(diagnosticType));

    jsCode = "function evil() { this.prototype.x = function() {}; }";

    compiler = new AbstractCompiler() {
      @Override
      public DiagnosticGroup getDiagnostics() {
        return DiagnosticGroup.empty();
      }

      @Override
      public Node parseTree(Node root) {
        return Parser.parseCompilationUnit(jsCode, null, null, null);
      }
    };

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    root = compiler.parseTree(Parser.parseCompilationUnit(jsCode, null, null, null));
    traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse();

    assertFalse(compiler.getDiagnostics().hasErrorExact(diagnosticType));

    jsCode = "function evil() { this.prototype.x = function() {}; this.useful = 1; }";

    compiler = new AbstractCompiler() {
      @Override
      public DiagnosticGroup getDiagnostics() {
        return DiagnosticGroup.empty();
      }

      @Override
      public Node parseTree(Node root) {
        return Parser.parseCompilationUnit(jsCode, null, null, null);
      }
    };

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    root = compiler.parseTree(Parser.parseCompilationUnit(jsCode, null, null, null));
    traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().hasErrorExact(diagnosticType));

    jsCode = "function evil() { var a = this; this.prototype.useful = 1; }";

    compiler = new AbstractCompiler() {
      @Override
      public DiagnosticGroup getDiagnostics() {
        return DiagnosticGroup.empty();
      }

      @Override
      public Node parseTree(Node root) {
        return Parser.parseCompilationUnit(jsCode, null, null, null);
      }
    };

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    root = compiler.parseTree(Parser.parseCompilationUnit(jsCode, null, null, null));
    traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse();

    assertFalse(compiler.getDiagnostics().hasErrorExact(diagnosticType));

    jsCode = "function evil() { this.prototype.x = function() {}; this.useful = 1; }";

    compiler = new AbstractCompiler() {
      @Override
      public DiagnosticGroup getDiagnostics() {
        return DiagnosticGroup.empty();
      }

      @Override
      public Node parseTree(Node root) {
        return Parser.parseCompilationUnit(jsCode, null, null, null);
      }
    };

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    root = compiler.parseTree(Parser.parseCompilationUnit(jsCode, null, null, null));
    traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().hasErrorExact(diagnosticType));
  }
}