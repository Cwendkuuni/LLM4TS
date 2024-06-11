import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;
  private NodeFactory nodeFactory;
  private SourceFile sourceFile;
  private CompilerInput compilerInput;
  private CompilerPassManager passManager;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    sourceFile = mock(SourceFile.class);
    compilerInput = mock(CompilerInput.class);
    when(compilerInput.getAstRoot(ArgumentMatchers.any())).thenReturn(nodeFactory.createRootNode());
    passManager = mock(CompilerPassManager.class);
    when(compiler.getPassManager()).thenReturn(passManager);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @Test
  public void testBasicFunctionality() {
    Node root = nodeFactory.createRootNode();
    checkSideEffects.process(compilerInput, root);
    // Add assertions for the basic functionality of each method here.
  }

  @Test
  public void testProtectSideEffects() {
    Node root = nodeFactory.createRootNode();
    Node problemNode = nodeFactory.createName("problemNode");
    root.addChildToBack(problemNode);

    when(compiler.getSynthesizedExternsInput()).thenReturn(compilerInput);

    checkSideEffects.protectSideEffects();
    assertTrue(checkSideEffects.problemNodes.isEmpty());
    // Check that the problemNode has been replaced by a call to PROTECTOR_FN
    Node protectorFn = nodeFactory.createName("PROTECTOR_FN");
    Node call = nodeFactory.createCall(protectorFn);
    Node replacement = nodeFactory.createCall(protectorFn);
    Node expectedProtectorFnCall = nodeFactory.createCall(protectorFn, problemNode);
    // Add assertions to check that the problemNode has been replaced by the call to PROTECTOR_FN
  }

  @Test
  public void testStripProtection() {
    Node root = nodeFactory.createRootNode();
    Node protectorFnCall = nodeFactory.createCall(
        nodeFactory.createName("PROTECTOR_FN"), nodeFactory.createName("argument"));
    root.addChildToBack(protectorFnCall);

    StripProtection stripProtection = new StripProtection(compiler);

    NodeTraversal.traverse(compiler, root, stripProtection);

    // Check that the protectorFnCall has been removed
    assertTrue(root.getChildCount() == 0);
  }

  @Test(expected = AssertionError.class)
  public void testUselessCodeError() {
    Node root = nodeFactory.createRootNode();
    Node problemNode = nodeFactory.createName("problemNode");
    root.addChildToBack(problemNode);

    checkSideEffects.level = CheckLevel.ERROR;
    checkSideEffects.process(compilerInput, root);
    // Add assertions for the USLESS_CODE_ERROR here
  }
}