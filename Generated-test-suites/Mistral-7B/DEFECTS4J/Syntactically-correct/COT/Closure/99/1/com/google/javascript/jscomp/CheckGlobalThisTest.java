import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.Parser.ParseTree;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() throws IOException {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public List<Checker> getCheckers() {
        return Lists.newArrayList(new CheckGlobalThis(this, WARNING));
      }
    };

    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    NodeFactory nodeFactory = new NodeFactory();

    String code =
        "function evil() { " +
            "this.useful = undefined;" +
        " }";

    ParseTree tree = parser.parse(code, null, null);
    Checker checker = compiler.getCheckers().get(0);
    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    checker.visitTree(traversal, root, null);

    List<DiagnosticType> diagnostics = traversal.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.warning, diagnostic.getSeverity());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType());
  }

  @Test
  public void testCheckGlobalThisConstructor() throws IOException {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public List<Checker> getCheckers() {
        return Lists.newArrayList(new CheckGlobalThis(this, WARNING));
      }
    };

    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    NodeFactory nodeFactory = new NodeFactory();

    String code =
        "function MyConstructor() { }";

    ParseTree tree = parser.parse(code, null, null);
    Checker checker = compiler.getCheckers().get(0);
    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    checker.visitTree(traversal, root, null);

    List<DiagnosticType> diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testCheckGlobalThisPrototype() throws IOException {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public List<Checker> getCheckers() {
        return Lists.newArrayList(new CheckGlobalThis(this, WARNING));
      }
    };

    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    NodeFactory nodeFactory = new NodeFactory();

    String code =
        "function MyObject() { }" +
            "MyObject.prototype.myMethod = function() { }";

    ParseTree tree = parser.parse(code, null, null);
    Checker checker = compiler.getCheckers().get(0);
    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    checker.visitTree(traversal, root, null);

    List<DiagnosticType> diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testCheckGlobalThisFunctionWithThisAnnotation() throws IOException {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public List<Checker> getCheckers() {
        return Lists.newArrayList(new CheckGlobalThis(this, WARNING));
      }
    };

    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    NodeFactory nodeFactory = new NodeFactory();

    String code =
        "/** @this {MyObject} */" +
            "function MyFunction() { }";

    ParseTree tree = parser.parse(code, null, null);
    Checker checker = compiler.getCheckers().get(0);
    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    checker.visitTree(traversal, root, null);

    List<DiagnosticType> diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testCheckGlobalThisFunctionWithoutAnnotation() throws IOException {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public List<Checker> getCheckers() {
        return Lists.newArrayList(new CheckGlobalThis(this, WARNING));
      }
    };

    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    NodeFactory nodeFactory = new NodeFactory();

    String code =
        "function MyFunction() { }";

    ParseTree tree = parser.parse(code, null, null);
    Checker checker = compiler.getCheckers().get(0);
    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    checker.visitTree(traversal, root, null);

    List<DiagnosticType> diagnostics = traversal.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.warning, diagnostic.getSeverity());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckGlobalThisInvalidAssignment() throws IOException {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public List<Checker> getCheckers() {
        return Lists.newArrayList(new CheckGlobalThis(this, WARNING));
      }
    };

    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    NodeFactory nodeFactory = new NodeFactory();

    String code =
        "function evil() { " +
            "this = 5; " + // Invalid assignment to this
        " }";

    ParseTree tree = parser.parse(code, null, null);
    Checker checker = compiler.getCheckers().get(0);
    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    checker.visitTree(traversal, root, null);
  }

  @Test
  public void testCheckGlobalThisFunctionWithMultipleAssignments() throws IOException {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public List<Checker> getCheckers() {
        return Lists.newArrayList(new CheckGlobalThis(this, WARNING));
      }
    };

    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    NodeFactory nodeFactory = new NodeFactory();

    String code =
        "function evil() { " +
            "this.a = 1; " +
            "this.b = 2; " +
            "this.c = 3;" +
        " }";

    ParseTree tree = parser.parse(code, null, null);
    Checker checker = compiler.getCheckers().get(0);
    Node root = tree.getRootNode();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    checker.visitTree(traversal, root, null);

    List<DiagnosticType> diagnostics = traversal.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.warning, diagnostic.getSeverity());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType());
  }
}