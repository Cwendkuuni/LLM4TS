import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFlag.*;

import com.google.common.base.Suppliers;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  private static final String INLINE_OBJECT_LITERALS_PASS_NAME =
      "InlineObjectLiterals";

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(compiler, Suppliers.memoFunction(
        () -> getUniqueNameGenerator()));
  }

  @Override
  protected String getPassName() {
    return INLINE_OBJECT_LITERALS_PASS_NAME;
  }

  // Basic test case to check if the pass correctly identifies and inlines
  // object literals that are only ever assigned to and never used in full
  public void testInlineObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: {d: 3}" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  var z = obj.c.d;" +
        "}" +
        "foo();";

    Node root = parse(jsCode);
    runPasses(root);
    assertCompiledTo("function foo() {" +
        "  var a = JSCompiler_object_inline_a_;" +
        "  var b = JSCompiler_object_inline_b_;" +
        "  var d = JSCompiler_object_inline_c_d;" +
        "  var x = a;" +
        "  var y = b;" +
        "  var z = d;" +
        "}");
  }

  // Test case for object literals with self-referential assignments
  public void testSelfReferentialObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: obj" +
        "    }" +
        "  };" +
        "}" +
        "foo();";

    Node root = parse(jsCode);
    try {
      runPasses(root);
      fail("Expected CompilationError: Self-referential assignment");
    } catch (CompilerException e) {
      assertCompilationErrorWithMessage(e, "Self-referential assignment");
    }
  }

  // Test case for object literals with conflicting keys
  public void testConflictingKeysObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    a: 2" +
        "  };" +
        "}" +
        "foo();";

    Node root = parse(jsCode);
    try {
      runPasses(root);
      fail("Expected CompilationError: Duplicate property name 'a'");
    } catch (CompilerException e) {
      assertCompilationErrorWithMessage(e, "Duplicate property name 'a'");
    }
  }

  // Test case for object literals with missing keys
  public void testMissingKeysObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b:" +
        "  };" +
        "}" +
        "foo();";

    Node root = parse(jsCode);
    runPasses(root);
    assertCompiledTo("function foo() {" +
        "  var a = JSCompiler_object_inline_a_;" +
        "  var b = JSCompiler_object_inline_b_;" +
        "}");
  }

  // Test case for object literals with empty initial values
  public void testEmptyInitialValuesObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: {}, " +
        "    b: {}" +
        "  };" +
        "}" +
        "foo();";

    Node root = parse(jsCode);
    runPasses(root);
    assertCompiledTo("function foo() {" +
        "  var a = {}; " +
        "  var b = {}; " +
        "}");
  }

  // Test case for object literals with undefined initial values
  public void testUndefinedInitialValuesObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: undefined," +
        "    b: undefined" +
        "  };" +
        "}" +
        "foo();";

    Node root = parse(jsCode);
    runPasses(root);
    assertCompiledTo("function foo() {" +
        "  var a = undefined; " +
        "  var b = undefined; " +
        "}");
  }

  // Test case for object literals with cyclic references
  public void testCyclicReferencesObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var objA = {" +
        "    a: 1," +
        "    b: objB" +
        "  };" +
        "  var objB = {" +
        "    a: objA" +
        "  };" +
        "}" +
        "foo();";

    Node root = parse(jsCode);
    try {
      runPasses(root);
      fail("Expected CompilationError: Cyclic object reference");
    } catch (CompilerException e) {
      assertCompilationErrorWithMessage(e, "Cyclic object reference");
    }
  }
}