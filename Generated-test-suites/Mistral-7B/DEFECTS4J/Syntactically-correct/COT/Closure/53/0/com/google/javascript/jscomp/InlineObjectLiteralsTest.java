import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    safeNameIdSupplier = mock(Supplier.class);
  }

  @Test
  public void testInlineSimpleObject() {
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = Node.newText("", null);
    Node root = Node.newText("{}", null);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(externs, root);

    InlineObjectLiterals.InliningBehavior inliningBehavior = inlineObjectLiterals.new InliningBehavior();
    Scope scope = mock(Scope.class);
    ReferenceCollection referenceCollection = mock(ReferenceCollection.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    when(referenceCollection.getReferences(ArgumentMatchers.any()))
        .thenReturn(Collections.singletonList(
            new Reference(new Node(Token.VAR, null, null, null, null), null, null, null, null, null)
        ));
    when(referenceCollection.isWellDefined()).thenReturn(true);

    inliningBehavior.afterExitScope(ArgumentMatchers.any(NodeTraversal.class), referenceCollection);

    assertEquals(5, scope.getVars().size());
  }

  @Test
  public void testInlineObjectWithFunctionProperty() {
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = Node.newText("", null);
    Node root = Node.newText("{foo: function(){}}", null);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(externs, root);

    InlineObjectLiterals.InliningBehavior inliningBehavior = inlineObjectLiterals.new InliningBehavior();
    Scope scope = mock(Scope.class);
    ReferenceCollection referenceCollection = mock(ReferenceCollection.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    when(referenceCollection.getReferences(ArgumentMatchers.any()))
        .thenReturn(Collections.singletonList(
            new Reference(new Node(Token.VAR, null, null, null, null), null, null, null, null, null)
        ));
    when(referenceCollection.getInitializingReference()).thenReturn(null);
    when(referenceCollection.isWellDefined()).thenReturn(true);

    inliningBehavior.afterExitScope(ArgumentMatchers.any(NodeTraversal.class), referenceCollection);

    // Ensure that the function property was not inlined
    assertEquals(6, scope.getVars().size());
  }

  @Test
  public void testInlineObjectWithSelfAssignedProperty() {
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = Node.newText("", null);
    Node root = Node.newText("{a: {b: this}}", null);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(externs, root);

    InlineObjectLiterals.InliningBehavior inliningBehavior = inlineObjectLiterals.new InliningBehavior();
    Scope scope = mock(Scope.class);
    ReferenceCollection referenceCollection = mock(ReferenceCollection.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    when(referenceCollection.getReferences(ArgumentMatchers.any()))
        .thenReturn(Collections.singletonList(
            new Reference(new Node(Token.VAR, null, null, null, null), null, null, null, null, null)
        ));
    when(referenceCollection.getInitializingReference()).thenReturn(null);
    when(referenceCollection.isWellDefined()).thenReturn(true);

    inliningBehavior.afterExitScope(ArgumentMatchers.any(NodeTraversal.class), referenceCollection);

    // Ensure that the self-assigned property was not inlined
    assertEquals(7, scope.getVars().size());
  }

  @Test
  public void testInlineObjectWithCycle() {
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = Node.newText("", null);
    Node root = Node.newText("{a: {b: this, c: a}}", null);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
    callback.process(externs, root);

    InlineObjectLiterals.InliningBehavior inliningBehavior = inlineObjectLiterals.new InliningBehavior();
    Scope scope = mock(Scope.class);
    ReferenceCollection referenceCollection = mock(ReferenceCollection.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    when(referenceCollection.getReferences(ArgumentMatchers.any()))
        .thenReturn(Collections.singletonList(
            new Reference(new Node(Token.VAR, null, null, null, null), null, null, null, null, null)
        ));
    when(referenceCollection.getInitializingReference()).thenReturn(null);
    when(referenceCollection.isWellDefined()).thenReturn(false);

    try {
      inliningBehavior.afterExitScope(ArgumentMatchers.any(NodeTraversal.class), referenceCollection);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }

    // Ensure that the variables were not modified
    assertEquals(1, scope.getVars().size());
  }
}