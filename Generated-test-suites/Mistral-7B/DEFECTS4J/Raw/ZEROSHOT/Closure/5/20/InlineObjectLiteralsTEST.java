import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String testCode = "function foo(x) {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: function() {\n" +
        "      return 2;\n" +
        "    },\n" +
        "    c: x,\n" +
        "    d: {\n" +
        "      e: 1,\n" +
        "      f: function() {\n" +
        "        return 3;\n" +
        "      },\n" +
        "      g: x\n" +
        "    },\n" +
        "    h: x\n" +
        "  };\n" +
        "  return obj.b();\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");

    Node ast = compiler.parseTree(testCode, null, null, null);
    PassManager pm = new PassManager(compiler);
    pm.addPasses(pass);
    pm.runPasses(ast, null, null);

    Node root = ast.getFirstChild();
    Scope globalScope = root.getFirstChild();

    // Check that variables have been inlined as expected.
    List<String> expectedVars = ImmutableList.of("JSCompiler_object_inline_a",
        "JSCompiler_object_inline_b", "JSCompiler_object_inline_c",
        "JSCompiler_object_inline_e", "JSCompiler_object_inline_f");
    for (Node child = root.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isStmt() && child.isExpressionStatement()) {
        Node expr = child.getFirstChild();
        if (expr.isCall()) {
          Node callee = expr.getFirstChild();
          if (callee.isName()) {
            String varName = callee.getString();
            assertTrue(expectedVars.contains(varName));
          }
        }
      }
    }

    // Check that the object structure has been transformed as expected.
    Node transformedObj = null;
    for (Node child = root.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isVar()) {
        String variableName = child.getFirstChild().getString();
        if (variableName.startsWith("JSCompiler_object_inline_")) {
          transformedObj = child.getChildAtIndex(1);
          break;
        }
      }
    }

    assertEquals(expectedSize(7), transformedObj.getChildCount());
    Node a = transformedObj.getChildAtIndex(0);
    assertEquals(Token.NUMBER_LITERAL, a.getNodeType());
    assertEquals(1, ((Number) a.getFirstChild()).intValue());

    Node b = transformedObj.getChildAtIndex(1);
    assertEquals(Token.FUNCTION_DECL, b.getNodeType());
    assertEquals("JSCompiler_object_inline_b", b.getFirstChild().getString());
    Node bBody = b.getFirstChild();
    assertEquals(Token.RETURN_STATEMENT, bBody.getNodeType());
    Node bReturn = bBody.getFirstChild();
    assertEquals(Token.NUMBER_LITERAL, bReturn.getNodeType());
    assertEquals(2, ((Number) bReturn.getFirstChild()).intValue());

    Node c = transformedObj.getChildAtIndex(2);
    assertEquals(Token.IDENT, c.getNodeType());
    assertEquals("c", c.getString());

    Node e = transformedObj.getChildAtIndex(3);
    assertEquals(Token.NUMBER_LITERAL, e.getNodeType());
    assertEquals(1, ((Number) e.getFirstChild()).intValue());

    Node f = transformedObj.getChildAtIndex(4);
    assertEquals(Token.FUNCTION_DECL, f.getNodeType());
    assertEquals("JSCompiler_object_inline_f", f.getFirstChild().getString());
    Node fBody = f.getFirstChild();
    assertEquals(Token.RETURN_STATEMENT, fBody.getNodeType());
    Node fReturn = fBody.getFirstChild();
    assertEquals(Token.NUMBER_LITERAL, fReturn.getNodeType());
    assertEquals(3, ((Number) fReturn.getFirstChild()).intValue());

    Node h = transformedObj.getChildAtIndex(5);
    assertEquals(Token.IDENT, h.getNodeType());
    assertEquals("h", h.getString());

    Node i = transformedObj.getChildAtIndex(6);
    assertEquals(Token.IDENT, i.getNodeType());
    assertEquals("c", i.getString());
  }

  private int expectedSize(int numProperties) {
    return 1 + // Object literal
        2 + // Object literal opening and closing brackets
        numProperties * // Properties and their values
            (2 + // Property name and colon
                1 + // Assign node
                1); // Property value
  }
}