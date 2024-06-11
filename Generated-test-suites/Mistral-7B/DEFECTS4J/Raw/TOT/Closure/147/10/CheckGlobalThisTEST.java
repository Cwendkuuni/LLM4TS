import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserOptions;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the {@link CheckGlobalThis} class.
 */
public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(true);
    options.setWarningLevel(CHEAP_CODE, OFF);
    options.setWarningLevel(DEAD_CODE, OFF);
    options.setWarningLevel(UNDEFINED, OFF);
    options.setWarningLevel(UNREACHABLE, OFF);
    options.setWarningLevel(EXPERIMENTAL, OFF);
    options.setWarningLevel(UNSAFE, OFF);
    options.setWarningLevel(UNDEFINED_PROPERTY, OFF);
    options.setWarningLevel(UNUSED_EXPORT, OFF);
    options.setWarningLevel(UNUSED_PRIVATE_FIELD, OFF);
    options.setWarningLevel(UNUSED_PRIVATE_TYPE, OFF);
    options.setWarningLevel(UNUSED_QUALIFIED_NAME, OFF);
    options.setWarningLevel(USELESS_ELSE, OFF);
    options.setWarningLevel(VAR_DECLARED_BUT_NEVER_USED, OFF);
    options.setWarningLevel(MISSING_RETURN, OFF);
    options.setWarningLevel(TYPE_MISMATCH, OFF);
    options.setWarningLevel(EXCEPTION_CAUGHT, OFF);
    options.setWarningLevel(UNDEFINED_NAME, OFF);
    options.setWarningLevel(TYPE_INCOMPATIBLE, OFF);
    options.setWarningLevel(WRONG_ASSIGN, OFF);
    options.setWarningLevel(DEBUG, OFF);
    options.setWarningLevel(RC_UNUSED_LABEL, OFF);
    options.setWarningLevel(INCOMPATIBLE_TYPE, OFF);
    options.setWarningLevel(TYPE_OF_UNDEFINED, OFF);
    options.setWarningLevel(OBJECT_TYPE_MISMATCH, OFF);
    options.setWarningLevel(MISSING_THROW, OFF);
    options.setWarningLevel(MISSING_SUPER_CALL, OFF);
    options.setWarningLevel(WRONG_SUPER_TYPE, OFF);
    options.setWarningLevel(MISSING_NEW_KEYWORD, OFF);
    options.setWarningLevel(UNSAFE_FINAL_VAR, OFF);
    options.setWarningLevel(UNSAFE_PROTOTYPE_ACCESS, OFF);
    options.setWarningLevel(UNSAFE_ASSIGN, OFF);
    options.setWarningLevel(UNSAFE_PROPERTY_ACCESS, OFF);
    options.setWarningLevel(UNSAFE_FUNCTION_CALL, OFF);
    options.setWarningLevel(UNSAFE_THIS_ASSIGN, OFF);
    options.setWarningLevel(UNSAFE_THIS_ACCESS, OFF);
    options.setWarningLevel(UNSAFE_THIS_FUNCTION_CALL, OFF);
    options.setWarningLevel(UNSAFE_THIS_PROPERTY_ACCESS, OFF);
    options.setWarningLevel(UNSAFE_THIS_ASSIGN_PROPERTY, OFF);
    options.setWarningLevel(UNSAFE_THIS_ASSIGN_FIELD, OFF);
    options.setWarningLevel(UNSAFE_THIS_ASSIGN_METHOD, OFF);
    options.setWarningLevel(UNSAFE_THIS_ASSIGN_VAR, OFF);
    options.setWarningLevel(UNSAFE_THIS_ASSIGN_ARRAY, OFF);
    options.setWarningLevel(UNSAFE_THIS_ASSIGN_OBJECT, OFF);
    options.setWarningLevel(UNSAFE_THIS_CALL_PROPERTY, OFF);
    options.setWarningLevel(UNSAFE_THIS_CALL_METHOD, OFF);
    options.setWarningLevel(UNSAFE_THIS_CALL_VAR, OFF);
    options.setWarningLevel(UNSAFE_THIS_CALL_ARRAY, OFF);
    options.setWarningLevel(UNSAFE_THIS_CALL_OBJECT, OFF);
    options.setWarningLevel(UNSAFE_THIS_NEW, OFF);
    options.setWarningLevel(UNSAFE_THIS_NEW_ARRAY, OFF);
    options.setWarningLevel(UNSAFE_THIS_NEW_OBJECT, OFF);
    options.setWarningLevel(UNSAFE_THIS_NEW_CLASS, OFF);
    options.setWarningLevel(UNSAFE_THIS_INSTANCEOF, OFF);
    options.setWarningLevel(UNSAFE_THIS_INSTANCEOF_ARRAY, OFF);
    options.setWarningLevel(UNSAFE_THIS_INSTANCEOF_OBJECT, OFF);

    ParserOptions parserOptions = new ParserOptions();
    parserOptions.setSourceFile("CheckGlobalThisTest.js");

    compiler = new AbstractCompiler(options, parserOptions, null);
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  /**
   * Test for a valid usage of 'this' inside a constructor.
   */
  @Test
  public void testConstructor() {
    String jsCode = "function Foo() { this.x = 1; }";
    Node tree = parserOptions.parse(jsCode, null, null);
    checker.traverse(tree, null);
    Node error = compiler.getDiagnosticMessages().get(0);
    assertNull(error);
  }

  /**
   * Test for a valid usage of 'this' inside a prototype method.
   */
  @Test
  public void testPrototypeMethod() {
    String jsCode = "function Foo() {} Foo.prototype.bar = function() { this.x = 1; }";
    Node tree = parserOptions.parse(jsCode, null, null);
    checker.traverse(tree, null);
    Node error = compiler.getDiagnosticMessages().get(0);
    assertNull(error);
  }

  /**
   * Test for a valid usage of 'this' inside an @this-annotated function.
   */
  @Test
  public void testThisAnnotatedFunction() {
    String jsCode = "/** @this {Foo} */ function Foo() {}";
    Node tree = parserOptions.parse(jsCode, null, null);
    checker.traverse(tree, null);
    Node error = compiler.getDiagnosticMessages().get(0);
    assertNull(error);
  }

  /**
   * Test for a valid usage of 'this' where there's no logical place to put
   * a @this annotation.
   */
  @Test
  public void testNoThisAnnotation() {
    String jsCode = "function Foo() { this.x = 1; }";
    Node tree = parserOptions.parse(jsCode, null, null);
    checker.traverse(tree, null);
    Node error = compiler.getDiagnosticMessages().get(0);
    assertNull(error);
  }

  /**
   * Test for a global 'this' usage inside an assignment statement.
   */
  @Test
  public void testGlobalThisAssignment() {
    String jsCode = "window.x = this;";
    Node tree = parserOptions.parse(jsCode, null, null);
    checker.traverse(tree, null);
    Node error = compiler.getDiagnosticMessages().get(0);
    assertNotNull(error);
    assertEquals(GLOBAL_THIS, error.getDiagnosticType());
  }

  /**
   * Test for a global 'this' usage inside a property access.
   */
  @Test
  public void testGlobalThisPropertyAccess() {
    String jsCode = "window.x = this.y;";
    Node tree = parserOptions.parse(jsCode, null, null);
    checker.traverse(tree, null);
    Node error = compiler.getDiagnosticMessages().get(0);
    assertNotNull(error);
    assertEquals(GLOBAL_THIS, error.getDiagnosticType());
  }
}