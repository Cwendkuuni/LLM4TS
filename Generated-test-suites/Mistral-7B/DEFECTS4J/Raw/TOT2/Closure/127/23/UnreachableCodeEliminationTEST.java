import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeFactory.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {

  @org.junit.Before
  public void setup() {
    AbstractCompiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new UnreachableCodeElimination(compiler, true));
  }

  @org.junit.Test
  public void testSimpleReturn() {
    Node rootNode = script(
        "function test() {" +
        "  if (true) {" +
        "    return; // unreachable code" +
        "  }" +
        "  alert('This message should not be displayed');" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script("function test() {}");
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testSimpleFunctionCall() {
    Node rootNode = script(
        "function test() {" +
        "  function foo() {" +
        "    return; // unreachable code" +
        "  }" +
        "  foo();" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script("function test() {}");
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testEmptyFunction() {
    Node rootNode = script("function test() {}");
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script("function test() {}");
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithTryCatch() {
    Node rootNode = script(
        "function test() {" +
        "  try {" +
        "    throw new Error('Test error');" +
        "  } catch (e) {" +
        "    return; // unreachable code" +
        "  }" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script("function test() {}");
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithBreak() {
    Node rootNode = script(
        "function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 5) {" +
        "      break; // unreachable code" +
        "    }" +
        "    alert(i);" +
        "  }" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script(
        "function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    alert(i);" +
        "  }" +
        "}"
    );
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithContinue() {
    Node rootNode = script(
        "function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 5) {" +
        "      continue; // unreachable code" +
        "    }" +
        "    alert(i);" +
        "  }" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script(
        "function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    alert(i);" +
        "  }" +
        "}"
    );
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithNoOpExpressions() {
    Node rootNode = script(
        "function test() {" +
        "  var x = a.b.MyClass.prototype.propertyName; // no-op expression" +
        "  var y = true;" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script("function test() {}");
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithDeadCodeAfterReturn() {
    Node rootNode = script(
        "function test() {" +
        "  return;" +
        "  alert('This message should not be displayed');" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script("function test() {return;}");
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithDeadCodeBeforeReturn() {
    Node rootNode = script(
        "function test() {" +
        "  alert('This message should not be displayed');" +
        "  return;" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script("function test() {return;}");
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithNoOpStatements() {
    Node rootNode = script(
        "function test() {" +
        "  return;" +
        "  // no-op statement" +
        "  true;" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script("function test() {return;}");
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithConditionalBranching() {
    Node rootNode = script(
        "function test() {" +
        "  if (true) {" +
        "    return; // unreachable code if condition is true" +
        "  } else {" +
        "    alert('This message should be displayed');" +
        "  }" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script(
        "function test() {" +
        "  alert('This message should be displayed');" +
        "}"
    );
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithMultipleConditionalBranching() {
    Node rootNode = script(
        "function test() {" +
        "  if (true) {" +
        "    if (false) {" +
        "      return; // unreachable code if both conditions are true" +
        "    } else {" +
        "      alert('This message should be displayed');" +
        "    }" +
        "  } else {" +
        "    alert('This message should be displayed');" +
        "  }" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script(
        "function test() {" +
        "  alert('This message should be displayed');" +
        "}"
    );
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithSwitchCase() {
    Node rootNode = script(
        "function test() {" +
        "  switch (1) {" +
        "    case 1:" +
        "      return; // unreachable code" +
        "    case 2:" +
        "      alert('This message should be displayed');" +
        "  }" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script(
        "function test() {" +
        "  alert('This message should be displayed');" +
        "}"
    );
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithNestedSwitchCase() {
    Node rootNode = script(
        "function test() {" +
        "  switch (1) {" +
        "    case 1:" +
        "      switch (2) {" +
        "        case 1:" +
        "          return; // unreachable code" +
        "        case 2:" +
        "          alert('This message should be displayed');" +
        "      }" +
        "    case 2:" +
        "      alert('This message should be displayed');" +
        "  }" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script(
        "function test() {" +
        "  alert('This message should be displayed');" +
        "}"
    );
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithEmptyBlock() {
    Node rootNode = script(
        "function test() {" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script("function test() {}");
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @org.junit.Test
  public void testUnreachableCodeWithComplexControlFlow() {
    Node rootNode = script(
        "function test() {" +
        "  if (true) {" +
        "    if (true) {" +
        "      return; // unreachable code if first condition is true" +
        "    } else {" +
        "      if (true) {" +
        "        if (false) {" +
        "          return; // unreachable code if second condition is false" +
        "        } else {" +
        "          alert('This message should be displayed');" +
        "        }" +
        "      }" +
        "    } else {" +
        "      alert('This message should be displayed');" +
        "    }" +
        "}"
    );
    traverse(new TraversingCompilerPass(), rootNode);
    passManager.runPasses(rootNode);
    Node expectedResult = script(
        "function test() {" +
        "  alert('This message should be displayed');" +
        "}"
    );
    Node actualResult = rootNode;
    assertEquals(expectedResult.toString(), actualResult.toString());
  }
}