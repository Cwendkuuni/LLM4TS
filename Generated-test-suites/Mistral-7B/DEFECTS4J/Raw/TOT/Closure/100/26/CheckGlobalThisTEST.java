import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String jsCode = "function evil() { this.useful = undefined; }";
    Node ast = Parser.parse(jsCode, null, null);

    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().isEmpty());

    jsCode = "var a = function() { this.useful = undefined; }";
    ast = Parser.parse(jsCode, null, null);

    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().isEmpty());

    jsCode = "function evil() { this.useful = undefined; }.prototype.test = function() {};";
    ast = Parser.parse(jsCode, null, null);

    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().isEmpty());

    jsCode = "function evil() { return this; }";
    ast = Parser.parse(jsCode, null, null);

    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().isEmpty());

    jsCode = "function evil() { return this.useful; }";
    ast = Parser.parse(jsCode, null, null);

    DiagnosticType diagnostic = new DiagnosticType(JSC_USED_GLOBAL_THIS, "dangerous use of the global 'this' object");
    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().getLength() == 1);
    assertTrue(compiler.getDiagnostics().get(0).getSourceName().equals("evil.js"));
    assertTrue(compiler.getDiagnostics().get(0).getStart().getLine() == 1);
    assertTrue(compiler.getDiagnostics().get(0).getStart().getColumn() == 5);
    assertTrue(compiler.getDiagnostics().get(0).getMessage().equals("dangerous use of the global 'this' object"));

    jsCode = "function evil() { this.prototype.test = function() {}; }";
    ast = Parser.parse(jsCode, null, null);

    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().isEmpty());

    jsCode = "function constructor() { this.x = 1; }";
    ast = Parser.parse(jsCode, null, null);

    checker = new CheckGlobalThis(compiler, QUIET);
    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().isEmpty());

    jsCode = "function constructor() { this.x = 1; }.prototype.test = function() {};";
    ast = Parser.parse(jsCode, null, null);

    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().isEmpty());

    jsCode = "function constructor() { this.x = 1; }.prototype.test = function() { this.y = 2; }";
    ast = Parser.parse(jsCode, null, null);

    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().isEmpty());

    jsCode = "function constructor() { this.x = 1; }.prototype.test = function() { return this; }";
    ast = Parser.parse(jsCode, null, null);

    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().isEmpty());

    jsCode = "function constructor() { this.x = 1; }.prototype.test = function() { return this.y; }";
    ast = Parser.parse(jsCode, null, null);

    diagnostic = new DiagnosticType(JSC_USED_GLOBAL_THIS, "dangerous use of the global 'this' object");
    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().getLength() == 1);
    assertTrue(compiler.getDiagnostics().get(0).getSourceName().equals("constructor.js"));
    assertTrue(compiler.getDiagnostics().get(0).getStart().getLine() == 1);
    assertTrue(compiler.getDiagnostics().get(0).getStart().getColumn() == 1);
    assertTrue(compiler.getDiagnostics().get(0).getMessage().equals("dangerous use of the global 'this' object"));

    jsCode = "function constructor() { this.x = 1; }.prototype.test = function @this() { return this; }";
    ast = Parser.parse(jsCode, null, null);

    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().isEmpty());

    jsCode = "function constructor() { this.x = 1; }.prototype.test = function() { this.y = 2; }";
    ast = Parser.parse(jsCode, null, null);

    checker = new CheckGlobalThis(compiler, QUIET);
    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().isEmpty());

    jsCode = "function constructor() { this.x = 1; }.prototype.test = function() { this.y = 2; }";
    ast = Parser.parse(jsCode, null, null);

    checker = new CheckGlobalThis(compiler, WARNING);
    checker.process(ast, options);
    assertTrue(compiler.getDiagnostics().getLength() == 1);
    assertTrue(compiler.getDiagnostics().get(0).getSourceName().equals("constructor.js"));
    assertTrue(compiler.getDiagnostics().get(0).getStart().getLine() == 1);
    assertTrue(compiler.getDiagnostics().get(0).getStart().getColumn() == 1);
    assertTrue(compiler.getDiagnostics().get(0).getMessage().equals("dangerous use of the global 'this' object"));
  }
}