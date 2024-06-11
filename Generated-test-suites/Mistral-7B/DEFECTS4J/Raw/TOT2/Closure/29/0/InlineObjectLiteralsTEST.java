import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());

    // Test case for a simple object literal assignment
    String sourceCode = "function foo() { var obj = {a: 1, b: 2}; }";
    Node originNode = NodePrinter.print(NodePrinter.print(compiler.parse(sourceCode, null, null), null).getFirstChild());
    callback.process(Node.SCOPE_GLOBAL, originNode);

    // Basic functionality test
    Node expectedResult = NodePrinter.print(
        IR.assign(
            IR.name("obj"),
            IR.objectLiteral(
                IR.prop("a", IR.number(1)),
                IR.prop("b", IR.number(2))
            )
        )
    );
    Node actualResult = compiler.getSourceNode();
    assertEquals("Unexpected inlineObjectLiterals processing", expectedResult, actualResult);

    // Edge case: Object literal with multiple keys and values
    sourceCode = "function foo() { var obj = {a: 1, b: 'two', c: {d: 3, e: 'four'}}; }";
    originNode = NodePrinter.print(NodePrinter.print(compiler.parse(sourceCode, null, null), null).getFirstChild());
    callback.process(Node.SCOPE_GLOBAL, originNode);

    expectedResult = NodePrinter.print(
        IR.assign(
            IR.name("obj"),
            IR.objectLiteral(
                IR.prop("a", IR.number(1)),
                IR.prop("b", IR.string("two")),
                IR.prop("c", IR.objectLiteral(
                    IR.prop("d", IR.number(3)),
                    IR.prop("e", IR.string("four"))
                ))
            )
        )
    );
    actualResult = compiler.getSourceNode();
    assertEquals("Unexpected inlineObjectLiterals processing for complex object literal", expectedResult, actualResult);

    // Error scenario: Object literal with reference to itself
    sourceCode = "function foo() { var obj = {a: 1, b: obj}; }";
    originNode = NodePrinter.print(NodePrinter.print(compiler.parse(sourceCode, null, null), null).getFirstChild());
    callback.process(Node.SCOPE_GLOBAL, originNode);

    // The InlineObjectLiterals pass should not handle self-referential object literals
    Node actualError = compiler.getSourceNode();
    String expectedErrorMessage = "Error: Self-referential object literal detected.";
    assertEquals("Unexpected inlineObjectLiterals processing for self-referential object literal", expectedErrorMessage, actualError.getClass().getName());
  }

  private static class InliningBehavior implements ReferenceCollectingCallback.Behavior {

    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
      InlineObjectLiterals pass = new InlineObjectLiterals(compiler, new SafeNameIdSupplier());
      pass.process(Node.SCOPE_GLOBAL, t.getScope().getRootNode());
    }
  }

  private static class SafeNameIdSupplier implements Supplier<String> {
    private int idCounter = 0;

    @Override
    public String get() {
      return "safeNameId_" + idCounter++;
    }
  }
}