import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testUnreachableCodeElimination() {
    String jsCode = "function foo() {" +
                    "  if (true) {" +
                    "    return;" +
                    "  }" +
                    "  alert('Unreachable');" +
                    "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parse(jsCode);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function foo() {" +
                 "  if (true) {" +
                 "    return;" +
                 "  }" +
                 "}", root.toString());

    jsCode = "function bar() {" +
            "  if (false) {" +
            "    return;" +
            "  }" +
            "  alert('Unreachable');" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function bar() {" +
                 "  if (false) {" +
                 "  }" +
                 "}", root.toString());

    jsCode = "function baz() {" +
            "  if (Math.random() > 0.5) {" +
            "    return;" +
            "  }" +
            "  alert('Unreachable');" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function baz() {" +
                 "  if (Math.random() > 0.5) {" +
                 "    return;" +
                 "  }" +
                 "}", root.toString());

    jsCode = "function qux() {" +
            "  alert('Unreachable');" +
            "  return;" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function qux() {" +
                 "}", root.toString());

    jsCode = "function quux() {" +
            "  if (true) {" +
            "    return;" +
            "  } else {" +
            "    alert('Unreachable');" +
            "  }" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function quux() {" +
                 "  if (true) {" +
                 "    return;" +
                 "  }" +
                 "}", root.toString());

    jsCode = "function coro() {" +
            "  function* generator() {" +
            "    co(this);" +
            "    function co(context) {" +
            "      if (context.next()) {" +
            "        return context.value();" +
            "      } else {" +
            "        return;" +
            "      }" +
            "    }" +
            "    co(generator.call(this));" +
            "  }" +
            "  generator();" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function coro() {" +
                 "  function* generator() {" +
                 "    co(this);" +
                 "  }" +
                 "  generator();" +
                 "}", root.toString());

    jsCode = "function quuux() {" +
            "  if (true) {" +
            "    return;" +
            "  } else {" +
            "    return;" +
            "  }" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function quuux() {" +
                 "  return;" +
                 "}", root.toString());

    jsCode = "function f() {" +
            "  try {" +
            "    return;" +
            "  } finally {" +
            "    alert('Unreachable');" +
            "  }" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function f() {" +
                 "  try {" +
                 "  } finally {" +
                 "  }", root.toString());

    jsCode = "function g() {" +
            "  try {" +
            "    return;" +
            "  } catch (e) {" +
            "    alert('Unreachable');" +
            "  }" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function g() {" +
                 "  try {" +
                 "  } catch (e) {}", root.toString());

    jsCode = "function h() {" +
            "  try {" +
            "    return;" +
            "  } finally {" +
            "    return;" +
            "  }" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function h() {" +
                 "  return;" +
                 "}", root.toString());

    jsCode = "function i() {" +
            "  try {" +
            "    return;" +
            "  } catch (e) {" +
            "    return;" +
            "  }" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function i() {" +
                 "  return;" +
                 "}", root.toString());

    jsCode = "function j() {" +
            "  try {" +
            "    return;" +
            "  } finally {" +
            "    return 42;" +
            "  }" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    assertEquals("function j() {" +
                 "  return 42;", root.toString());

    jsCode = "function k() {" +
            "  try {" +
            "    return;" +
            "  } finally {" +
            "    throw new Error('Error');" +
            "  }" +
            "}";

    root = AbstractSyntaxTree.parse(jsCode);
    traversal = new NodeTraversal(compiler, root);
    pass.process(null, root);

    try {
      pass.process(null, root);
      fail("Expected an exception: " + UnreachableCodeElimination.class.getName());
    } catch (RuntimeException e) {
      // Expected
    }

    assertEquals("function k() {}", root.toString());
  }
}