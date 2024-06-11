import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.AbstractShallowCallbackType.VISIT;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test.BaseCompilerTestCase;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest extends BaseCompilerTestCase {

  public void testUnreachableCodeElimination() throws Exception {
    String input = ""
        + "function foo() {"
        + "  if (true) {"
        + "    return;"
        + "  }"
        + "  alert('unreachable');"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";

    Compiler compiler = newCompiler();
    Node toplevel = parseTree(input);

    // Basic test case to check the functionality of the class.
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, toplevel);
    checkEqual("function foo() {" + "  if (true) {" + "  }" + "}", getRhsText(toplevel));

    // Test for edge cases and exception handling scenarios.

    // Test for a method with no public methods.
    input = "class MyClass {}";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass1 = new UnreachableCodeElimination(compiler, false);
    pass1.process(null, toplevel);
    assertTrue(getRhsText(toplevel).isEmpty());

    // Test for a method with a return statement followed by unreachable code.
    input = ""
        + "function foo() {"
        + "  if (false) {"
        + "    return;"
        + "  }"
        + "  alert('unreachable');"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass2 = new UnreachableCodeElimination(compiler, false);
    pass2.process(null, toplevel);
    checkEqual("function foo() {" + "  if (false) {" + "  }" + "}", getRhsText(toplevel));

    // Test for a method with unreachable statements without side effects.
    input = ""
        + "function foo() {"
        + "  a.b.MyClass.prototype.propertyName;"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass3 = new UnreachableCodeElimination(compiler, true);
    pass3.process(null, toplevel);
    checkEqual("function foo() {}", getRhsText(toplevel));

    // Test for a method with unreachable statements and side effects.
    input = ""
        + "function foo() {"
        + "  a.b.MyClass.prototype.propertyName = 1;"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass4 = new UnreachableCodeElimination(compiler, true);
    pass4.process(null, toplevel);
    checkEqual("function foo() {", getRhsText(toplevel));

    // Test for a method with a break statement followed by unreachable code.
    input = ""
        + "function foo() {"
        + "  for (var i = 0; i < 10; i++) {"
        + "    if (true) {"
        + "      break;"
        + "    }"
        + "    alert('unreachable');"
        + "  }"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass5 = new UnreachableCodeElimination(compiler, false);
    pass5.process(null, toplevel);
    checkEqual("function foo() {" + "  for (var i = 0; i < 10; i++) {" + "    if (true) {" + "      break;" + "    }" + "  }" + "}", getRhsText(toplevel));

    // Test for a method with a continue statement followed by unreachable code.
    input = ""
        + "function foo() {"
        + "  for (var i = 0; i < 10; i++) {"
        + "    if (true) {"
        + "      continue;"
        + "    }"
        + "    alert('unreachable');"
        + "  }"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass6 = new UnreachableCodeElimination(compiler, false);
    pass6.process(null, toplevel);
    checkEqual("function foo() {" + "  for (var i = 0; i < 10; i++) {" + "    if (true) {" + "      continue;" + "    }" + "  }" + "}", getRhsText(toplevel));

    // Test for a method with a return statement followed by unreachable code.
    input = ""
        + "function foo() {"
        + "  if (false) {"
        + "    return;"
        + "  }"
        + "  alert('unreachable');"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass7 = new UnreachableCodeElimination(compiler, false);
    pass7.process(null, toplevel);
    checkEqual("function foo() {" + "  if (false) {" + "    return;" + "  }" + "}", getRhsText(toplevel));

    // Test for a method with unreachable statements inside a try block.
    input = ""
        + "function foo() {"
        + "  try {"
        + "    throw new Error();"
        + "    alert('unreachable');"
        + "  } catch (e) {"
        + "  }"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass8 = new UnreachableCodeElimination(compiler, false);
    pass8.process(null, toplevel);
    checkEqual("function foo() {" + "  try {" + "    throw new Error();" + "  } catch (e) {" + "  }" + "}", getRhsText(toplevel));

    // Test for a method with unreachable statements inside a switch statement.
    input = ""
        + "function foo() {"
        + "  switch (0) {"
        + "    case 1:"
        + "      break;"
        + "    case 2:"
        + "      alert('unreachable');"
        + "      break;"
        + "  }"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass9 = new UnreachableCodeElimination(compiler, false);
    pass9.process(null, toplevel);
    checkEqual("function foo() {" + "  switch (0) {" + "    case 1:" + "      break;" + "  }" + "}", getRhsText(toplevel));

    // Test for a method with unreachable statements inside a for loop.
    input = ""
        + "function foo() {"
        + "  for (var i = 0; i < 10; i++) {"
        + "    if (true) {"
        + "      return;"
        + "    }"
        + "    alert('unreachable');"
        + "  }"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass10 = new UnreachableCodeElimination(compiler, false);
    pass10.process(null, toplevel);
    checkEqual("function foo() {" + "  for (var i = 0; i < 10; i++) {" + "    if (true) {" + "      return;" + "    }" + "  }" + "}", getRhsText(toplevel));

    // Test for a method with unreachable statements inside an if statement.
    input = ""
        + "function foo() {"
        + "  if (true) {"
        + "    alert('unreachable');"
        + "  } else {"
        + "    alert('else');"
        + "  }"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass11 = new UnreachableCodeElimination(compiler, false);
    pass11.process(null, toplevel);
    checkEqual("function foo() {" + "  if (true) {" + "  } else {" + "    alert('else');" + "  }" + "}", getRhsText(toplevel));

    // Test for a method with unreachable statements inside an else if statement.
    input = ""
        + "function foo() {"
        + "  if (false) {"
        + "    alert('if (false)');"
        + "  } else if (true) {"
        + "    alert('else if (true)');"
        + "  } else {"
        + "    alert('else');"
        + "  }"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass12 = new UnreachableCodeElimination(compiler, false);
    pass12.process(null, toplevel);
    checkEqual("function foo() {" + "  if (false) {" + "    alert('if (false)');" + "  } else if (true) {" + "    alert('else if (true)');" + "  }" + "}", getRhsText(toplevel));

    // Test for a method with unreachable statements inside an if statement with else if and else.
    input = ""
        + "function foo() {"
        + "  if (false) {"
        + "    alert('if (false)');"
        + "  } else if (true) {"
        + "    alert('else if (true)');"
        + "  } else {"
        + "    alert('else');"
        + "  }"
        + "}"
        + "function test() {"
        + "  foo();"
        + "}"
        + "test();";
    toplevel = parseTree(input);
    UnreachableCodeElimination pass13 = new UnreachableCodeElimination(compiler, false);
    pass13.process(null, toplevel);
    checkEqual("function foo() {" + "  if (false) {" + "    alert('if (false)');" + "  } else if (true) {" + "    alert('else if (true)');" + "  } else {" + "    alert('else');" + "  }" + "}", getRhsText(toplevel));
  }

  @Override
  protected Compiler newCompiler() throws Exception {
    return newCompilationTestHelper().newCompiler("none", null);
  }

  @Override
  protected NodeTraversal newNodeTraversal(Compiler compiler) {
    return new NodeTraversal(compiler, null);
  }

  @Override
  protected void runTests() throws Exception {
    runTest(VISIT, "UnreachableCodeEliminationTest.java", "testUnreachableCodeElimination");
  }

}