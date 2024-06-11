import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = () -> "test_id";

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, safeNameIdSupplier);

    Node externs = Node.root();
    Node root = Node.objectLiteral();
    root.addChild(Node.property("a", Node.numberLiteral(1)));
    root.addChild(Node.property("b", Node.objectLiteral()));
    root.addChild(Node.property("b.c", Node.numberLiteral(2)));
    root.addChild(Node.property("c", Node.numberLiteral(3)));

    CompilerPassManager passManager = new CompilerPassManager(
        compiler, options, null);
    passManager.addPass(inlineObjectLiterals);
    passManager.process(externs, root);

    Scope scope = root.getFirstChild().asScope();
    NodeTraversal traverser = NodeTraversal.createTraverser(root);

    for (Var var : scope.getVars()) {
      if (var.name.equals("b")) {
        ReferenceCollection referenceInfo = new ReferenceCollectingCallback(
            compiler, inlineObjectLiterals.new InliningBehavior())
            .process(externs, root);

        // "b" should have been expanded into separate variables
        assertEquals(4, referenceInfo.references.size());
        // Check variables "b.c" and "c" are also inlined
        for (Reference ref : referenceInfo.references) {
          if (ref.getNode().isName() && ref.getNode().getString().equals("c")) {
            assertEquals("JSCompiler_object_inline_c_test_id",
                ref.getVariable().getName().getName());
          }
          if (ref.getNode().isGetProp() && ref.getNode().getString().equals("b.c")) {
            assertEquals("JSCompiler_object_inline_b_test_id",
                ref.getVariable().getName().getName());
          }
        }
        break;
      }
    }
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralsWithSelfReferentialAssignment() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = () -> "test_id";

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, safeNameIdSupplier);

    Node externs = Node.root();
    Node root = Node.objectLiteral();
    root.addChild(Node.property("a", Node.objectLiteral()));
    root.addChild(Node.property("a.b", Node.numberLiteral(1)));
    root.addChild(Node.property("a.b", Node.name("a")));

    CompilerPassManager passManager = new CompilerPassManager(
        compiler, options, null);
    passManager.addPass(inlineObjectLiterals);
    passManager.process(externs, root);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralsWithSelfReferentialKey() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = () -> "test_id";

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, safeNameIdSupplier);

    Node externs = Node.root();
    Node root = Node.objectLiteral();
    root.addChild(Node.property("a", Node.objectLiteral()));
    root.addChild(Node.property("a.a", Node.numberLiteral(1)));

    CompilerPassManager passManager = new CompilerPassManager(
        compiler, options, null);
    passManager.addPass(inlineObjectLiterals);
    passManager.process(externs, root);
  }
}