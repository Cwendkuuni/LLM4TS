import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticMessage;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.compiler.AbstractCompiler.CompilerResult;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerResult compile(SourceFile src, CompilerOptions options) {
        return new CompilerResult(src, null, new DiagnosticMessage[0]);
      }
    };
    CheckLevel level = new CheckLevel(WARNING, DiagnosticGroups.MAYBE);
    NodeTraversal traversal = new NodeTraversal(compiler, level, new CheckGlobalThis(compiler, level));
    Parser parser = new Parser(new NodeFactory());

    String code = "function evil() { this.useful = undefined; }";
    SourceFile source = new SourceFile("Test.js", code, null);
    Node root = parser.parse(source, null, null);
    traversal.traverse(root);

    DiagnosticMessage[] diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(DiagnosticMessage[].class));
    assertThat(diagnostics.length, is(1));
    DiagnosticMessage diagnostic = diagnostics[0];
    assertThat(diagnostic.getDiagnostics()[0].getCode(), is(DiagnosticType.WARNING));
    assertThat(diagnostic.getDiagnostics()[0].getMessage(), is("dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckGlobalThisWithConstructor() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerResult compile(SourceFile src, CompilerOptions options) {
        return new CompilerResult(src, null, new DiagnosticMessage[0]);
      }
    };
    CheckLevel level = new CheckLevel(WARNING, DiagnosticGroups.MAYBE);
    NodeTraversal traversal = new NodeTraversal(compiler, level, new CheckGlobalThis(compiler, level));
    Parser parser = new Parser(new NodeFactory());

    String code = "function EvilConstructor() { }";
    SourceFile source = new SourceFile("Test.js", code, null);
    Node root = parser.parse(source, null, null);
    traversal.traverse(root);

    DiagnosticMessage[] diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(DiagnosticMessage[].class));
    assertThat(diagnostics.length, is(0));
  }

  @Test
  public void testCheckGlobalThisWithPrototypeMethod() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerResult compile(SourceFile src, CompilerOptions options) {
        return new CompilerResult(src, null, new DiagnosticMessage[0]);
      }
    };
    CheckLevel level = new CheckLevel(WARNING, DiagnosticGroups.MAYBE);
    NodeTraversal traversal = new NodeTraversal(compiler, level, new CheckGlobalThis(compiler, level));
    Parser parser = new Parser(new NodeFactory());

    String code = "var MyObject = function() { }; MyObject.prototype.evil = function() { this.useful = undefined; }";
    SourceFile source = new SourceFile("Test.js", code, null);
    Node root = parser.parse(source, null, null);
    traversal.traverse(root);

    DiagnosticMessage[] diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(DiagnosticMessage[].class));
    assertThat(diagnostics.length, is(0));
  }

  @Test
  public void testCheckGlobalThisWithThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerResult compile(SourceFile src, CompilerOptions options) {
        return new CompilerResult(src, null, new DiagnosticMessage[0]);
      }
    };
    CheckLevel level = new CheckLevel(WARNING, DiagnosticGroups.MAYBE);
    NodeTraversal traversal = new NodeTraversal(compiler, level, new CheckGlobalThis(compiler, level));
    Parser parser = new Parser(new NodeFactory());

    String code = "function MyFunction(@this) { }";
    SourceFile source = new SourceFile("Test.js", code, null);
    Node root = parser.parse(source, null, null);
    traversal.traverse(root);

    DiagnosticMessage[] diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(DiagnosticMessage[].class));
    assertThat(diagnostics.length, is(0));
  }

  @Test
  public void testCheckGlobalThisWithNoAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerResult compile(SourceFile src, CompilerOptions options) {
        return new CompilerResult(src, null, new DiagnosticMessage[0]);
      }
    };
    CheckLevel level = new CheckLevel(WARNING, DiagnosticGroups.MAYBE);
    NodeTraversal traversal = new NodeTraversal(compiler, level, new CheckGlobalThis(compiler, level));
    Parser parser = new Parser(new NodeFactory());

    String code = "function MyFunction() { this.useful = undefined; }";
    SourceFile source = new SourceFile("Test.js", code, null);
    Node root = parser.parse(source, null, null);
    traversal.traverse(root);

    DiagnosticMessage[] diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(DiagnosticMessage[].class));
    assertThat(diagnostics.length, is(1));
    DiagnosticMessage diagnostic = diagnostics[0];
    assertThat(diagnostic.getDiagnostics()[0].getCode(), is(DiagnosticType.WARNING));
    assertThat(diagnostic.getDiagnostics()[0].getMessage(), is("dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckGlobalThisWithAssignment() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerResult compile(SourceFile src, CompilerOptions options) {
        return new CompilerResult(src, null, new DiagnosticMessage[0]);
      }
    };
    CheckLevel level = new CheckLevel(WARNING, DiagnosticGroups.MAYBE);
    NodeTraversal traversal = new NodeTraversal(compiler, level, new CheckGlobalThis(compiler, level));
    Parser parser = new Parser(new NodeFactory());

    String code = "var a = this;";
    SourceFile source = new SourceFile("Test.js", code, null);
    Node root = parser.parse(source, null, null);
    traversal.traverse(root);

    DiagnosticMessage[] diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(DiagnosticMessage[].class));
    assertThat(diagnostics.length, is(0));
  }

  @Test
  public void testCheckGlobalThisWithAssignmentToPrototypeProperty() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerResult compile(SourceFile src, CompilerOptions options) {
        return new CompilerResult(src, null, new DiagnosticMessage[0]);
      }
    };
    CheckLevel level = new CheckLevel(WARNING, DiagnosticGroups.MAYBE);
    NodeTraversal traversal = new NodeTraversal(compiler, level, new CheckGlobalThis(compiler, level));
    Parser parser = new Parser(new NodeFactory());

    String code = "function MyFunction() { this.prototype.useful = undefined; }";
    SourceFile source = new SourceFile("Test.js", code, null);
    Node root = parser.parse(source, null, null);
    traversal.traverse(root);

    DiagnosticMessage[] diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(DiagnosticMessage[].class));
    assertThat(diagnostics.length, is(0));
  }
}