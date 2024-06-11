import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserConfiguration;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    String sourceCode = "function foo() { return 5; } var x = 1; x = foo(); ";

    CompilerInput input = new CompilerInput(ParserConfiguration.newBuilder()
        .sourceFile("TestFile.js").build(), sourceCode);
    CompilerOptions options = CompilerOptions.standard();
    options.setWarningLevel(CheckLevel.WARNING, "JSC_USELESS_CODE");

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.compile(input, options);

    CheckSideEffects sideEffects = new CheckSideEffects(compiler, WARNING, false);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, sideEffects);
    assertEquals(0, sideEffects.problemNodes.size());

    sourceCode = "function sideEffectFunction() { alert('Hello World!'); } var x = sideEffectFunction(); ";

    input = new CompilerInput(ParserConfiguration.newBuilder()
        .sourceFile("TestFile.js").build(), sourceCode);
    compiler = new AbstractCompiler();
    compiler.compile(input, options);
    sideEffects = new CheckSideEffects(compiler, WARNING, false);
    root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, sideEffects);
    assertEquals(1, sideEffects.problemNodes.size());

    Node problemNode = sideEffects.problemNodes.get(0);
    assertFalse(problemNode.isRemoved());
    JSDocInfo jsdocInfo = problemNode.getJSDocInfo();
    assertNull(jsdocInfo);

    String msg = sideEffects.compiler.getErrors()
        .getErrorList().get(0).getDescription();
    assertEquals("Suspicious code. sideEffectFunction()", msg);
  }

  @Test
  public void testProtectSideEffects() {
    String sourceCode = "var x = 1; x = 2;";

    CompilerInput input = new CompilerInput(ParserConfiguration.newBuilder()
        .sourceFile("TestFile.js").build(), sourceCode);
    CompilerOptions options = CompilerOptions.standard();
    options.setWarningLevel(CheckLevel.WARNING, "JSC_USELESS_CODE");
    options.setCompilerPass(CheckSideEffects.class);

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.compile(input, options);
    CheckSideEffects sideEffects = (CheckSideEffects) compiler.getPassManager()
        .getPass(CheckSideEffects.class);

    sideEffects.protectSideEffects();

    sourceCode = "JSCOMPILER_PRESERVE beginProtectedCode(); endProtectedCode();";

    input = new CompilerInput(ParserConfiguration.newBuilder()
        .sourceFile("TestFile.js").build(), sourceCode);
    compiler = new AbstractCompiler();
    compiler.compile(input, options);
    sideEffects = (CheckSideEffects) compiler.getPassManager()
        .getPass(CheckSideEffects.class);
    sideEffects.process(input, null);

    Node root = input.getAstRoot(compiler);
    Node protectedCode = root.getFirstChild().getLastChild();
    Node beginProtectedCode = protectedCode.getFirstChild();
    Node endProtectedCode = protectedCode.getLastChild();

    sideEffects.stripProtection();

    root = input.getAstRoot(compiler);
    assertNull(root.getFirstChild().getLastChild());
  }
}