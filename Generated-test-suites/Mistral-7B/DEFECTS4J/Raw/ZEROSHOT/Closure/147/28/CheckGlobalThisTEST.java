import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.parser.ParserOptions;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private Checker checker;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = Warning;
    checker = CheckerFactory.getChecker(options, level);
    compiler = new AbstractCompiler() {
      @Override
      protected Checker getChecker() {
        return checker;
      }
    };
  }

  @Test
  public void testSafeUsage() {
    String code = "var x = function() { this.x = 1; }; x.y = 2;";
    Node ast = Parser.parse(new ParserOptions(), code, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast, null);
    traversal.traverse(new CheckGlobalThis(compiler, Warning));
    assertEquals(0, checker.getNumErrors());
  }

  @Test
  public void testUnsafeAssignment() {
    String code = "var x = function() { this = {}; x.y = 1; }";
    Node ast = Parser.parse(new ParserOptions(), code, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast, null);
    traversal.traverse(new CheckGlobalThis(compiler, Warning));
    assertEquals(1, checker.getNumErrors());
    DiagnosticType diagnosticType = checker.getError(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  @Test
  public void testUnsafePropertyAccess() {
    String code = "var x = function() {}; x.prototype.y = 1";
    Node ast = Parser.parse(new ParserOptions(), code, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast, null);
    traversal.traverse(new CheckGlobalThis(compiler, Warning));
    assertEquals(1, checker.getNumErrors());
    DiagnosticType diagnosticType = checker.getError(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  @Test
  public void testFunctionAnnotatedWithConstructor() {
    String code = "function Foo() { this.x = 1; }";
    Node ast = Parser.parse(new ParserOptions(), code, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast, null);
    traversal.traverse(new CheckGlobalThis(compiler, Warning));
    assertEquals(0, checker.getNumErrors());
  }

  @Test
  public void testFunctionAnnotatedWithThis() {
    String code = "function Foo@this() { this.x = 1; }";
    Node ast = Parser.parse(new ParserOptions(), code, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast, null);
    traversal.traverse(new CheckGlobalThis(compiler, Warning));
    assertEquals(0, checker.getNumErrors());
  }

  @Test
  public void testFunctionAnnotatedWithNoClearThis() {
    String code = "function Foo() { this.x = 1; }";
    Node ast = Parser.parse(new ParserOptions(), code, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast, null);
    traversal.traverse(new CheckGlobalThis(compiler, Error));
    assertEquals(1, checker.getNumErrors());
    DiagnosticType diagnosticType = checker.getError(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }
}