import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest extends CompilerPassTestCase {

  public InlineObjectLiteralsTest() {
    super(new CompilerOptions()
        .setOptIn(OptFlag.HTML));
  }

  @Test
  public void testInlineObjectLiterals() {
    String source = "function foo() {" +
            "  var obj = {" +
            "    a: 1," +
            "    b: {'c': 3}" +
            "  };" +
            "  var n = obj.b.c;" +
            "  return n + obj.a;" +
            "}";

    AbstractCompiler compiler = newAbstractCompiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, newNameIdSupplier());

    Node ast = parseJs(source);
    pass.process(ast, ast);

    String expected = "function foo() {" +
            "  var a = JSCompiler_object_inline_a_;\n" +
            "  var b_c = JSCompiler_object_inline_b_c_;\n" +
            "  var n = b_c;\n" +
            "  var a_ = JSCompiler_object_inline_a_;\n" +
            "  return n + a_;" +
            "}";

    assertEquals(expected, getSource(ast));
  }

  private static AbstractCompiler newAbstractCompiler() {
    return new TestCompilationManager().getCompiler();
  }

  private static Supplier<String> newNameIdSupplier() {
    return () -> "test_" + System.currentTimeMillis();
  }

  @Override
  protected AbstractCompiler getCompiler() {
    return newAbstractCompiler();
  }

  // Helper methods for parsing and generating test source code

  private Node parseJs(String source) {
    return parser().parse(source, null, null);
  }

  private String getSource(Node node) {
    return printer().printToJavaScript(node, null, null);
  }
}

class InlineObjectLiteralsTest$InlineObjectLiterals {
  InlineObjectLiterals(AbstractCompiler compiler, Supplier<String> safeNameIdSupplier) {
    // Copy constructor from the original class
  }

  static class InliningBehavior extends ReferenceCollectingCallback.Behavior {
    // Copy constructor from the original class

    void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
      // Copy method from the original class
    }

    Set<Var> staleVars = Sets.newHashSet();

    boolean isVarInlineForbidden(Var var) {
      // Copy method from the original class
      return false;
    }

    boolean isInlinableObject(List<Reference> refs) {
      // Copy method from the original class
      return false;
    }

    void splitObject(Var v, Reference declaration,
                     Reference init, ReferenceCollection referenceInfo) {
      // Copy method from the original class
    }

    void blacklistVarReferencesInTree(Node root, Scope scope) {
      // Copy method from the original class
    }

    Map<String, String> computeVarList(Var v, ReferenceCollection referenceInfo) {
      // Copy method from the original class
      return null;
    }

    void fillInitialValues(Reference init, Map<String, Node> initvals) {
      // Copy method from the original class
    }

    void replaceAssignmentExpression(Var v, Reference ref,
                                      Map<String, String> varmap) {
      // Copy method from the original class
    }
  }
}