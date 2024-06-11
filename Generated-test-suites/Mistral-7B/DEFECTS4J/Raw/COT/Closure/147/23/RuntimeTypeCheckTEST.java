package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Test class for the RuntimeTypeCheck class.
 */
public class RuntimeTypeCheckTest extends CompilerTestCase {

  /**
   * Test constructor.
   *
   * @param testName name of the test
   * @param compiler the compiler to use for testing
   * @param logFunction the log function to use for type checking errors, or null
   */
  public RuntimeTypeCheckTest(String testName, AbstractCompiler compiler,
                              @Nullable String logFunction) {
    super(testName, compiler, logFunction);
  }

  

  /**
   * Test the process method.
   */
  @Test
  public void testProcess() throws IOException {
    String input = "function Foo() {}";
    String expectedOutput = "function Foo() {\n" +
            "  // ... (original code)\n" +
            "  // type checking boilerplate code\n" +
            "  checkType(this, function() {\n" +
            "    return implements__Foo;\n" +
            "  });\n" +
            "}";

    Node root = parse(input);
    AbstractCompiler compiler = newCompilerPass();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);

    String actualOutput = getOutputFromAST(root);
    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * Test the process method with a log function.
   */
  @Test
  public void testProcessWithLogFunction() throws IOException {
    String input = "function Foo() {}";
    String expectedError = "typecheck.js:1:1: Type 'Foo' is not defined.";
    String expectedOutput = "function Foo() {\n" +
            "  // ... (original code)\n" +
            "  // type checking boilerplate code\n" +
            "  checkType(this, function() {\n" +
            "    return implements__Foo;\n" +
            "  }, function(warning, expr) {\n" +
            "    console.log('" + expectedError + "');\n" +
            "  });\n" +
            "}";

    Node root = parse(input);
    AbstractCompiler compiler = newCompilerPass();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler,
        "function(warning, expr) { console.log(warning); }");
    runtimeTypeCheck.process(null, root);

    String actualOutput = getOutputFromAST(root);
    assertEquals(expectedOutput, actualOutput);

    // Test the log function.
    assertTrue(compiler.getWarningCount() > 0);
  }

  /**
   * Test the process method with a missing class.
   */
  @Test(expected = AssertionError.class)
  public void testProcessMissingClass() throws IOException {
    String input = "function Foo() {} class Bar {} function Baz() {\n" +
            "  new Bar(); // should fail\n" +
            "}";

    Node root = parse(input);
    AbstractCompiler compiler = newCompilerPass();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);

    // The compilation should fail with an error.
  }

  /**
   * Test the process method with a missing interface.
   */
  @Test(expected = AssertionError.class)
  public void testProcessMissingInterface() throws IOException {
    String input = "interface Foo {} function Bar() implements Foo {}";

    Node root = parse(input);
    AbstractCompiler compiler = newCompilerPass();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);

    // The compilation should fail with an error.
  }

  
}