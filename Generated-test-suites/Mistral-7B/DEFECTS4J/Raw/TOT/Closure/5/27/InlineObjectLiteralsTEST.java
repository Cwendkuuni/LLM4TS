import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String jsCode = "function f() {" +
        "var obj1 = {a: 1, b: 2, c: {d: 3}};" +
        "var obj2 = obj1; " +
        "obj1.c.d = 4; " +
        "};";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");

    CompilerResults result = compiler.compile(
        jsCode,
        null,
        options,
        pass);

    Node root = result.getOutput();
    Node function = NodeUtil.findFunction(root, "f");

    // Check that the object literals were inlined correctly
    for (int i = 0; i < function.getNumChildren(); i++) {
      Node child = function.getChildAtIndex(i);
      if (child.isVar()) {
        String varName = child.getName();
        if (varName.startsWith("JSCompiler_object_inline_")) {
          Node value = child.getNextSibling();
          Node object = value.getFirstChild();

          // Check that the object was split into individual variables
          for (int j = 0; j < object.getNumChildren(); j++) {
            Node key = object.getChildAtIndex(j);
            Node valueNode = key.getNextSibling();
            String varNameForKey = "JSCompiler_object_inline_" + key.getString();
            Node var = NodeUtil.findVar(function, varNameForKey);
            assertEquals(valueNode, var.getFirstChild());
          }
        }
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    String jsCode = "function f() {" +
        "var obj1 = {a: {b: obj1}};" +
        "};";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");

    CompilerResults result = compiler.compile(
        jsCode,
        null,
        options,
        pass);

    Node root = result.getOutput();
    Node function = NodeUtil.findFunction(root, "f");

    // Check that the object literals were not inlined due to self-reference
    for (int i = 0; i < function.getNumChildren(); i++) {
      Node child = function.getChildAtIndex(i);
      if (child.isVar()) {
        String varName = child.getName();
        if (varName.startsWith("JSCompiler_object_inline_")) {
          fail("Expected object not to be inlined due to self-reference.");
        }
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithDeletedProperty() {
    String jsCode = "function f() {" +
        "delete obj1.b;" +
        "var obj1 = {a: 1, b: 2, c: {d: 3}};" +
        "};";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");

    CompilerResults result = compiler.compile(
        jsCode,
        null,
        options,
        pass);

    Node root = result.getOutput();
    Node function = NodeUtil.findFunction(root, "f");

    // Check that the object literals were not inlined due to deleted property
    for (int i = 0; i < function.getNumChildren(); i++) {
      Node child = function.getChildAtIndex(i);
      if (child.isVar()) {
        String varName = child.getName();
        if (varName.startsWith("JSCompiler_object_inline_")) {
          fail("Expected object not to be inlined due to deleted property.");
        }
      }
    }
  }
}