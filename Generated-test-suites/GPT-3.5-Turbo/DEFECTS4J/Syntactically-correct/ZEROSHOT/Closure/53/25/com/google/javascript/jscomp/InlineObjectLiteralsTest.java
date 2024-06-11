import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setUp() {
    inlineObjectLiterals = new InlineObjectLiterals(new AbstractCompiler(), new Supplier<String>() {
      @Override
      public String get() {
        return "12345";
      }
    });
  }

  @Test
  public void testProcess() {
    Node externs = null;
    Node root = null;
    inlineObjectLiterals.process(externs, root);
    // Add your assertions here
  }

  @Test
  public void testAfterExitScope() {
    NodeTraversal t = null;
    ReferenceMap referenceMap = null;
    inlineObjectLiterals.new InliningBehavior().afterExitScope(t, referenceMap);
    // Add your assertions here
  }

  @Test
  public void testBlacklistVarReferencesInTree() {
    Node root = null;
    Scope scope = null;
    inlineObjectLiterals.new InliningBehavior().blacklistVarReferencesInTree(root, scope);
    // Add your assertions here
  }

  @Test
  public void testIsVarInlineForbidden() {
    Var var = null;
    boolean result = inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(var);
    // Add your assertions here
  }

  @Test
  public void testIsInlinableObject() {
    List<Reference> refs = null;
    boolean result = inlineObjectLiterals.new InliningBehavior().isInlinableObject(refs);
    // Add your assertions here
  }

  @Test
  public void testComputeVarList() {
    Var v = null;
    ReferenceCollection referenceInfo = null;
    Map<String, String> result = inlineObjectLiterals.new InliningBehavior().computeVarList(v, referenceInfo);
    // Add your assertions here
  }

  @Test
  public void testFillInitialValues() {
    Reference init = null;
    Map<String, Node> initvals = null;
    inlineObjectLiterals.new InliningBehavior().fillInitialValues(init, initvals);
    // Add your assertions here
  }

  @Test
  public void testReplaceAssignmentExpression() {
    Var v = null;
    Reference ref = null;
    Map<String, String> varmap = null;
    inlineObjectLiterals.new InliningBehavior().replaceAssignmentExpression(v, ref, varmap);
    // Add your assertions here
  }

  @Test
  public void testSplitObject() {
    Var v = null;
    Reference declaration = null;
    Reference init = null;
    ReferenceCollection referenceInfo = null;
    inlineObjectLiterals.new InliningBehavior().splitObject(v, declaration, init, referenceInfo);
    // Add your assertions here
  }
}