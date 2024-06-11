import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;

/**
 * Tests for {@link CheckGlobalThis}.
 */
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String jsCode = "function evil() { this.useful = undefined; }";

    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    SourceFile source = new SourceFile("TestFile.js", jsCode);
    Node rootNode = compiler.parseTree(source);

    // Create a visitor for CheckGlobalThis.
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    // Traverse the tree.
    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    // There should be no errors reported.
    assertEquals(0, checkGlobalThis.compiler.getDiagnostics().size());

    // Now let's test some cases that should trigger a warning.

    jsCode = "function evil() { this = {}; }";
    source = new SourceFile("TestFile2.js", jsCode);
    rootNode = compiler.parseTree(source);

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);
    traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    // There should be one warning reported.
    DiagnosticType warning = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertTrue(checkGlobalThis.compiler.hasExactlyOneError(warning));

    jsCode = "function evil() { this.useful = 42; }";
    source = new SourceFile("TestFile3.js", jsCode);
    rootNode = compiler.parseTree(source);

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);
    traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    // There should be one warning reported.
    assertTrue(checkGlobalThis.compiler.hasExactlyOneError(warning));

    jsCode = "function evil() { function foo() { this.useful = 42; } }";
    source = new SourceFile("TestFile4.js", jsCode);
    rootNode = compiler.parseTree(source);

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);
    traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    // There should be no warnings reported.
    assertEquals(0, checkGlobalThis.compiler.getDiagnostics().size());

    jsCode = "function evil() { var a = this; }";
    source = new SourceFile("TestFile5.js", jsCode);
    rootNode = compiler.parseTree(source);

    checkGlobalThis = new CheckGlobalThis(compiler, WARNING);
    traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    // There should be no warnings reported.
    assertEquals(0, checkGlobalThis.compiler.getDiagnostics().size());
  }
}