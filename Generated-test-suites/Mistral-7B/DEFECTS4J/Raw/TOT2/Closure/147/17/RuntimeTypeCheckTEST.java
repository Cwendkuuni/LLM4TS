import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.CompilerResults.AbstractCompiler;
import com.google.javascript.jscomp.ErrorType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link RuntimeTypeCheck} class.
 */
public class RuntimeTypeCheckTest {

  private static final String TEST_SOURCE = "test.js";

  @org.junit.Test
  public void testRuntimeTypeCheck() throws IOException {
    // Setup
    List<CompilerPass> passes = Collections.singletonList(new RuntimeTypeCheck(null, null));
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    // Test case 1: checking a simple value
    Node testNode1 = createTestNode("function test1() { return 42; }");

    // Expected result: no errors
    CompilerResults results1 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode1)), passes, null);
    assertErrorFree(results1);

    // Test case 2: checking a value against a union type
    Node testNode2 = createTestNode("function test2() { return 42.0; }");

    // Expected result: no errors
    CompilerResults results2 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode2)), passes, null);
    assertErrorFree(results2);

    // Test case 3: checking a value against a class type
    Node testNode3 = createTestNode("function test3() { return new Foo(); }");

    // Expected result: no errors
    CompilerResults results3 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode3)), passes, null);
    assertErrorFree(results3);

    // Test case 4: checking a value against an interface type
    Node testNode4 = createTestNode("function test4() { return new Bar(); }");

    // Expected result: no errors
    CompilerResults results4 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode4)), passes, null);
    assertErrorFree(results4);

    // Test case 5: checking a value against a nullable class type
    Node testNode5 = createTestNode("function test5() { return null; }");

    // Expected result: no errors
    CompilerResults results5 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode5)), passes, null);
    assertErrorFree(results5);

    // Test case 6: checking a value against a non-nullable class type
    Node testNode6 = createTestNode("function test6() { return null; }");

    // Expected result: error
    CompilerResults results6 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode6)), passes, null);
    assertErrorPresent(results6, ErrorType.TYPE_ERROR, "Cannot check null for a non-nullable type.");

    // Test case 7: checking a function against a function type
    Node testNode7 = createTestNode("function test7(x) { return x; }");

    // Expected result: no errors
    CompilerResults results7 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode7)), passes, null);
    assertErrorFree(results7);

    // Test case 8: checking a value against an undefined type
    Node testNode8 = createTestNode("function test8() { return undefined; }");

    // Expected result: no errors
    CompilerResults results8 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode8)), passes, null);
    assertErrorFree(results8);

    // Test case 9: checking a constructor call against a constructor type
    Node testNode9 = createTestNode("function test9() { return new Foo(); }");

    // Expected result: no errors
    CompilerResults results9 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode9)), passes, null);
    assertErrorFree(results9);

    // Test case 10: checking a value against an interface type from an external library
    Node testNode10 = createTestNode("function test10() { return new MyLibrary.MyInterface(); }");

    // Expected result: no errors
    CompilerResults results10 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode10)), passes, null);
    assertErrorFree(results10);

    // Test case 11: checking a value against a non-existent interface type
    Node testNode11 = createTestNode("function test11() { return new NonExistentInterface(); }");

    // Expected result: error
    CompilerResults results11 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode11)), passes, null);
    assertErrorPresent(results11, ErrorType.TYPE_ERROR, "TypeError: NonExistentInterface is not defined.");

    // Test case 12: checking a value against a union type from an external library
    Node testNode12 = createTestNode("function test12() { return 'hello'; }");

    // Expected result: no errors
    CompilerResults results12 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode12)), passes, null);
    assertErrorFree(results12);

    // Test case 13: checking a value against an interface type with a constructor that
    // has no implementation in the source
    Node testNode13 = createTestNode("function test13() { return new MyInterface(); }");

    // Expected result: no errors
    CompilerResults results13 = compiler.compile(Arrays.asList(new SourceFile(TEST_SOURCE, testNode13)), passes, null);
    assertErrorFree(results13);
  }

  private Node createTestNode(String code) {
    SourceFile sourceFile = new SourceFile(TEST_SOURCE, NodeUtil.parseJavaScript(code));
    return sourceFile.getTree();
  }

  private void assertErrorFree(CompilerResults results) {
    assertFalse(results.hasErrors());
  }

  private void assertErrorPresent(CompilerResults results, ErrorType errorType, String message) {
    assertTrue(results.hasErrors());
    Node errorNode = results.getErrors().get(0).getSourceNode();
    assertEquals(errorType, results.getErrors().get(0).getErrorType());
    assertEquals(message, NodeUtil.getErrorMessage(errorNode));
  }
}