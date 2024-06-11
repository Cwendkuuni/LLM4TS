package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private InlineObjectLiterals inlineObjectLiterals;
  private ReferenceCollectingCallback referenceCollectingCallback;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    referenceCollectingCallback = mock(ReferenceCollectingCallback.class);
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "test_id");
  }

  @Test
  public void testProcess() {
    Node externs = Node.object();
    Node root = Node.object();
    inlineObjectLiterals.process(externs, root);
    verify(compiler).reportCodeChange();
  }

  @Test
  public void testAfterExitScope() {
    Scope scope = mock(Scope.class);
    ReferenceMap referenceMap = mock(ReferenceMap.class);
    inlineObjectLiterals.new InliningBehavior().afterExitScope(
        NodeTraversalMock.create(), referenceMap);
  }

  @Test
  public void testIsVarInlineForbidden() {
    Var var = mock(Var.class);
    boolean result = inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(var);
    verify(var).isGlobal();
    verify(var).isExtern();
    verify(compiler).getCodingConvention();
    verify(compiler).getCodingConvention().isExported(var.name);
    verify(compiler).getCodingConvention().isExported(RenameProperties.RENAME_PROPERTY_FUNCTION_NAME);
    verify(referenceMap).getReferences(var);
    verify(staleVars).contains(var);
  }

  @Test
  public void testIsInlinableObject() {
    List<Reference> refs = Lists.newArrayList();
    Reference reference = mock(Reference.class);
    refs.add(reference);

    Node object = Node.object(Node.newProperty("a", Node.newString("1")));
    Node objectRef = Node.newObjectAccess(object, "a");
    Node objectInit = Node.newObjectInitializer(null);

    reference.setNode(objectInit);
    reference.setParent(objectRef);
    reference.setGrandparent(Node.newAssign(Node.newVar("x"), objectRef));

    Node objectLiteral = Node.newObjectLiterals(Node.object(), null);

    boolean result = inlineObjectLiterals.new InliningBehavior().isInlinableObject(refs);
    verify(objectLiteral).getType();
  }

  @Test(expected = AssertionError.class)
  public void testIsInlinableObject_selfReference() {
    List<Reference> refs = Lists.newArrayList();
    Reference reference = mock(Reference.class);
    refs.add(reference);

    Node object = Node.object(Node.newProperty("a", Node.newObjectAccess(
        Node.newObjectLiterals(Node.object(), null), "a")));

    reference.setNode(Node.newGetProp(Node.newVar("x"), "a"));
    reference.setParent(Node.newAssign(Node.newVar("x"), object));

    inlineObjectLiterals.new InliningBehavior().isInlinableObject(refs);
  }

  @Test
  public void testSplitObject() {
    Var var = mock(Var.class);
    Reference declaration = mock(Reference.class);
    Reference init = mock(Reference.class);
    ReferenceCollection referenceInfo = mock(ReferenceCollection.class);

    inlineObjectLiterals.new InliningBehavior().splitObject(var, declaration, init, referenceInfo);

    verify(declaration).setAssignedValue(null);
    verify(init).setAssignedValue(null);
    verify(referenceInfo).getReferences(var);
    verify(referenceInfo).isWellDefined();
  }
}

class NodeTraversalMock {
  static NodeTraversal create() {
    return mock(NodeTraversal.class);
  }
}