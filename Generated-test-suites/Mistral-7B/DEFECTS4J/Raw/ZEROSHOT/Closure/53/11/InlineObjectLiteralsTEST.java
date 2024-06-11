import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private static class InliningBehaviorSpy implements Behavior {
    private final Set<Var> staleVars = Sets.newHashSet();

    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
      for (Iterator<Var> it = t.getScope().getVars(); it.hasNext();) {
        Var v = it.next();
        processVar(v);
      }
    }

    private void processVar(Var v) {
      if (isVarInlineForbidden(v)) {
        return;
      }

      ReferenceCollection referenceInfo = referenceMap.getReferences(v);
      if (isInlinableObject(referenceInfo.references)) {
        staleVars.add(v);
        splitObject(v, referenceInfo);
      }
    }

    private boolean isVarInlineForbidden(Var var) {
      return false;
    }

    private boolean isInlinableObject(List<Reference> refs) {
      return false;
    }

    private void splitObject(Var v, ReferenceCollection referenceInfo) {
      // TODO: Implement the actual behavior of the method.
    }

    public Set<Var> getStaleVars() {
      return staleVars;
    }
  }

  @SuppressWarnings("unchecked")
  private static InlineObjectLiterals createInlineObjectLiteralsMock(
      AbstractCompiler compiler, Supplier<String> safeNameIdSupplier) {
    InliningBehaviorSpy inliningBehavior = mock(InliningBehaviorSpy.class);
    InlineObjectLiterals instance = new InlineObjectLiterals(compiler, safeNameIdSupplier) {
      @Override
      private class InliningBehavior extends InliningBehaviorSpy {
      }
    };
    instance.inliningBehavior = inliningBehavior;
    return instance;
  }

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    InlineObjectLiterals inlineObjectLiterals = createInlineObjectLiteralsMock(compiler, safeNameIdSupplier);
    ReferenceCollectingCallback referenceCollectingCallback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());

    Node rootNode = Node.newObjectLit(Collections.singletonMap("a", Node.newNumber(1)), Collections.singletonMap("b", Node.newNumber(2)));
    Scope scope = new Scope();
    Node externsNode = Node.newObjectLit(Collections.emptyMap());

    inlineObjectLiterals.process(externsNode, rootNode);

    ReferenceCollection referenceInfo = referenceCollectingCallback.getReferenceCollection();
    InliningBehaviorSpy inliningBehavior = inlineObjectLiterals.inliningBehavior;

    List<Var> expectedStaleVars = Arrays.asList(
        new Var("a", Node.newNumber(1), 0, null, null, null, null, null, null, null),
        new Var("b", Node.newNumber(2), 0, null, null, null, null, null, null, null)
    );

    assertEquals(expectedStaleVars, inliningBehavior.getStaleVars());

    // TODO: Add more test cases.
  }

}