import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.jscomp.VariableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testRenameVarsBasic() {
    String sourceCode = "function foo(a, b) { var c = a + b; }";
    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        // Empty
      }
    });

    passManager.addPass(new RenameVars(compiler, null, false, false, false, null, null, null));

    TestSourceFile sourceFile = new TestSourceFile("test.js", sourceCode);
    Node externs = null;
    Node root = compiler.parseTree(sourceFile.getSourceCode(), null, sourceFile.getSourceFileName(), null, options);

    passManager.process(externs, root);

    Node fooFunction = root.getFirstChild();
    Node cVariable = fooFunction.getFirstChild().getNext();

    assertEquals("a", cVariable.getString());
  }

  @Test
  public void testRenameVarsWithPrefix() {
    String sourceCode = "function foo(a, b) { var _prefix_c = a + b; }";
    CompilerOptions options = new CompilerOptions();
    options.setOption("--jscomp_prefix", "_prefix_");

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        // Empty
      }
    });

    passManager.addPass(new RenameVars(compiler, "_prefix_", false, false, false, null, null, null));

    TestSourceFile sourceFile = new TestSourceFile("test.js", sourceCode);
    Node externs = null;
    Node root = compiler.parseTree(sourceFile.getSourceCode(), null, sourceFile.getSourceFileName(), null, options);

    passManager.process(externs, root);

    Node fooFunction = root.getFirstChild();
    Node cVariable = fooFunction.getFirstChild().getNext();

    assertEquals("_prefix_c", cVariable.getString());
  }

  @Test
  public void testRenameVarsLocalVariables() {
    String sourceCode = "function foo(a) { var a = a; var b = a; var c = a; }";
    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        // Empty
      }
    });

    passManager.addPass(new RenameVars(compiler, null, false, false, false, null, null, null));

    TestSourceFile sourceFile = new TestSourceFile("test.js", sourceCode);
    Node externs = null;
    Node root = compiler.parseTree(sourceFile.getSourceCode(), null, sourceFile.getSourceFileName(), null, options);

    passManager.process(externs, root);

    Node fooFunction = root.getFirstChild();
    Node aVariable1 = fooFunction.getFirstChild();
    Node aVariable2 = aVariable1.getNext();
    Node bVariable = aVariable2.getNext();
    Node cVariable = bVariable.getNext();

    // Check that local variables were renamed
    assertEquals("a", aVariable1.getString());
    assertEquals("a1", aVariable2.getString());
    assertEquals("a2", bVariable.getString());
    assertEquals("a3", cVariable.getString());
  }

  @Test
  public void testRenameVarsGlobalVariables() {
    String sourceCode = "var a = 1; function foo(b) { var c = a; }";
    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        externs.setFirstChild(externs.getFirstChild().getNext());
      }
    });

    passManager.addPass(new RenameVars(compiler, null, false, false, false, null, null, null));

    TestSourceFile sourceFile = new TestSourceFile("test.js", sourceCode);
    Node externs = null;
    Node root = compiler.parseTree(sourceFile.getSourceCode(), null, sourceFile.getSourceFileName(), null, options);

    passManager.process(externs, root);

    Node aVariable = root.getFirstChild();
    Node fooFunction = aVariable.getNext();
    Node cVariable = fooFunction.getFirstChild().getNext();

    // Check that global variable was renamed
    assertEquals("a0", aVariable.getString());
    assertEquals("c", cVariable.getString());
  }

  @Test
  public void testRenameVarsExternVariables() {
    String sourceCode = "var a = 1; function foo(b) { var c = a; }";
    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        externs.setFirstChild(externs.getFirstChild().getNext());
      }
    });

    passManager.addPass(new RenameVars(compiler, null, false, false, false, null, null, null));

    TestSourceFile sourceFile = new TestSourceFile("test.js", sourceCode);
    Node externs = null;
    Node root = compiler.parseTree(sourceFile.getSourceCode(), null, sourceFile.getSourceFileName(), null, options);

    passManager.process(externs, root);

    Node aVariable = root.getFirstChild();
    Node fooFunction = aVariable.getNext();
    Node cVariable = fooFunction.getFirstChild().getNext();

    // Check that global variable was NOT renamed
    assertEquals("a", aVariable.getString());
    assertEquals("c", cVariable.getString());
  }

  @Test
  public void testRenameVarsWithReservedNames() {
    String sourceCode = "var if = 1; function foo() { var for = 2; }";
    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    List<String> reservedNames = new ArrayList<>();
    reservedNames.add("if");
    reservedNames.add("for");

    passManager.addPass(new RenameVars(compiler, null, false, false, false, null, null, reservedNames));

    TestSourceFile sourceFile = new TestSourceFile("test.js", sourceCode);
    Node externs = null;
    Node root = compiler.parseTree(sourceFile.getSourceCode(), null, sourceFile.getSourceFileName(), null, options);

    passManager.process(externs, root);

    Node ifVariable = root.getFirstChild();
    Node fooFunction = ifVariable.getNext();
    Node forVariable = fooFunction.getFirstChild().getNext();

    // Check that reserved names were not renamed
    assertEquals("if", ifVariable.getString());
    assertEquals("for", forVariable.getString());
  }

  @Test(expected = AssertionError.class)
  public void testRenameVarsWithInvalidCharacters() {
    String sourceCode = "function foo(a) { var ¥ = a; }";
    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    char[] reservedCharacters = {'¥'};

    passManager.addPass(new RenameVars(compiler, null, false, false, false, null, reservedCharacters, null));

    TestSourceFile sourceFile = new TestSourceFile("test.js", sourceCode);
    Node externs = null;
    Node root = compiler.parseTree(sourceFile.getSourceCode(), null, sourceFile.getSourceFileName(), null, options);

    try {
      passManager.process(externs, root);
      fail("Expected an AssertionError");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testRenameVarsWithEdgeCases() {
    String sourceCode = "function foo(a, b) { var a = b; } function bar() { var a = 1; }";
    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        // Empty
      }
    });

    passManager.addPass(new RenameVars(compiler, null, false, false, false, null, null, null));

    TestSourceFile sourceFile = new TestSourceFile("test.js", sourceCode);
    Node externs = null;
    Node root = compiler.parseTree(sourceFile.getSourceCode(), null, sourceFile.getSourceFileName(), null, options);

    passManager.process(externs, root);

    Node fooFunction = root.getFirstChild();
    Node aVariable = fooFunction.getFirstChild();
    Node bVariable = aVariable.getPrevious();

    Node barFunction = fooFunction.getNext();

    // Check that foo's a was renamed
    assertEquals("b", aVariable.getString());

    // Check that bar's a was not renamed
    assertEquals("a", barFunction.getFirstChild().getString());
  }

  @Test
  public void testRenameVarsWithVariableMap() {
    String sourceCode = "function foo(a, b) { var c = a + b; }";
    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        // Empty
      }
    });

    passManager.addPass(new RenameVars(compiler, null, false, false, false, null, null, null));

    TestSourceFile sourceFile = new TestSourceFile("test.js", sourceCode);
    Node externs = null;
    Node root = compiler.parseTree(sourceFile.getSourceCode(), null, sourceFile.getSourceFileName(), null, options);

    passManager.process(externs, root);

    VariableMap variableMap = new RenameVars(compiler, null, false, false, false, null, null, null).getVariableMap();

    Node cVariable = root.getFirstChild().getFirstChild().getNext();

    // Check that c was assigned a new name
    String newName = variableMap.lookupName("c");
    assertEquals("c", cVariable.getString());
    assertEquals(newName, cVariable.getString(compiler.getSourceFile()));
  }
}