import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.IR;

import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new Compiler();
    Supplier<String> safeNameIdSupplier = () -> "test_";

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    Node rootNode = Node.objectLiteral();

    inlineObjectLiterals.process(null, rootNode);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, null);
    callback.process(null, rootNode);

    List<Reference> references = callback.getReferences(null);
    for (Reference reference : references) {
      Node node = reference.getNode();
      assertEquals("Object literals should be inlined",
          IR.isObjectLit(node), IR.isObjectLit(reference.getAssignedValue()));
    }
  }
}