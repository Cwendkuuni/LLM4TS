import static org.junit.Assert.*;
import static com.google.javascript.jscomp.VarCheck.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ParserOptions;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;

public class VarCheckTest {

  @Test
  public void testValidVariableDeclaration() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(ParserOptions.standardJS());
    CompilerPass varCheck = new VarCheck(compiler);

    String sourceCode = "function foo() {" +
        "var x = 1;" +
        "}";

    SourceFile input = new SourceFile("Test.js", sourceCode);
    Node root = NodeFactory.compile(input, options).getRootNode();

    compiler.process(root, new Node[0]);
    compiler.getDiagnostics().printDiagnostics();

    varCheck.process(null, root);
    compiler.getDiagnostics().printDiagnostics();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testUndefinedVariable() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(ParserOptions.standardJS());
    CompilerPass varCheck = new VarCheck(compiler);

    String sourceCode = "function foo() {" +
        "var x = 1;" +
        "y = 2;" +
        "}";

    SourceFile input = new SourceFile("Test.js", sourceCode);
    Node root = NodeFactory.compile(input, options).getRootNode();

    compiler.process(root, new Node[0]);
    compiler.getDiagnostics().printDiagnostics();

    varCheck.process(null, root);
    compiler.getDiagnostics().printDiagnostics();

    assertEquals("variable y is undefined",
        compiler.getDiagnostics().getError(0).getMessage());
  }

  @Test
  public void testModuleDependency() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(ParserOptions.standardJS());
    CompilerPass varCheck = new VarCheck(compiler);

    String module1SourceCode = "module Test1 {" +
        "var x = 1;" +
        "export function foo() {" +
        "  import y from './Test2';" +
        "  print(y);" +
        "}" +
        "}";

    String module2SourceCode = "module Test2 {" +
        "var y = 'Hello';" +
        "}";

    SourceFile module1 = new SourceFile("Test1.js", module1SourceCode);
    SourceFile module2 = new SourceFile("Test2.js", module2SourceCode);

    Node module1Root = NodeFactory.compile(module1, options).getRootNode();
    Node module2Root = NodeFactory.compile(module2, options).getRootNode();

    Node[] roots = new Node[]{module1Root, module2Root};
    Node[] dependencies = new Node[0];

    compiler.process(roots, dependencies);
    compiler.getDiagnostics().printDiagnostics();

    varCheck.process(null, module1Root);
    compiler.getDiagnostics().printDiagnostics();

    assertEquals("module Test1 cannot reference Test2, defined in Test2, since Test2",
        compiler.getDiagnostics().getError(0).getMessage());
  }

  @Test
  public void testMissingModuleDependency() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(ParserOptions.standardJS());
    CompilerPass varCheck = new VarCheck(compiler);

    String module1SourceCode = "module Test1 {" +
        "import y from './Test2';" +
        "print(y);" +
        "}";

    String module2SourceCode = "module Test2 {}";

    SourceFile module1 = new SourceFile("Test1.js", module1SourceCode);
    SourceFile module2 = new SourceFile("Test2.js", module2SourceCode);

    Node module1Root = NodeFactory.compile(module1, options).getRootNode();
    Node module2Root = NodeFactory.compile(module2, options).getRootNode();

    Node[] roots = new Node[]{module1Root, module2Root};
    Node[] dependencies = new Node[0];

    compiler.process(roots, dependencies);
    compiler.getDiagnostics().printDiagnostics();

    varCheck.process(null, module1Root);
    compiler.getDiagnostics().printDiagnostics();

    assertEquals("missing module dependency; module Test1 should depend " +
        "on Test2 because it references Test2",
        compiler.getDiagnostics().getWarning(0).getMessage());
  }

  @Test
  public void testStrictModuleDependency() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(ParserOptions.standardJS());
    CompilerPass varCheck = new VarCheck(compiler, true);

    String module1SourceCode = "module Test1 {" +
        "import y from './Test2';" +
        "print(y);" +
        "}";

    String module2SourceCode = "module Test2 {}";

    SourceFile module1 = new SourceFile("Test1.js", module1SourceCode);
    SourceFile module2 = new SourceFile("Test2.js", module2SourceCode);

    Node module1Root = NodeFactory.compile(module1, options).getRootNode();
    Node module2Root = NodeFactory.compile(module2, options).getRootNode();

    Node[] roots = new Node[]{module1Root, module2Root};
    Node[] dependencies = new Node[0];

    compiler.process(roots, dependencies);
    compiler.getDiagnostics().printDiagnostics();

    varCheck.process(null, module1Root);
    compiler.getDiagnostics().printDiagnostics();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedVariableInSanityCheck() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(ParserOptions.standardJS());
    CompilerPass varCheck = new VarCheck(compiler, true);

    String sourceCode = "function foo() {" +
        "y = 2;" +
        "}";

    SourceFile input = new SourceFile("Test.js", sourceCode);
    Node root = NodeFactory.compile(input, options).getRootNode();

    varCheck.process(null, root);
  }
}