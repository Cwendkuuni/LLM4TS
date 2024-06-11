import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private Scope scope;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    safeNameIdSupplier = mock(Supplier.class);
    scope = mock(Scope.class);
    inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  @Test
  public void testProcess_InlineObjectLiterals() {
    Node externs = Node.newText("", null);
    Node root = Node.newText("", null);
    inlineObjectLiterals.process(externs, root);
    // Add assertions for the processed code here.
  }

  @Test
  public void testIsVarInlineForbidden_GlobalVariables() {
    Var v = new Var("global", null, null, null, null, null, null, false, false,
        null, false, null, null);
    boolean result = inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v);
    assertEquals(true, result);
  }

  @Test
  public void testIsVarInlineForbidden_ExternVariables() {
    Var v = new Var("extern", null, null, null, null, null, null, false, false,
        null, false, null, null);
    boolean result = inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v);
    assertEquals(true, result);
  }

  @Test
  public void testIsVarInlineForbidden_ExportedVariables() {
    Var v = new Var("exported", null, null, null, null, null, null, false, false,
        null, false, null, null);
    boolean result = inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v);
    assertEquals(true, result);
  }

  @Test
  public void testIsVarInlineForbidden_SelfReferentialAssignment() {
    Var v = new Var("v", null, null, null, null, null, null, false, false,
        null, false, null, null);
    Node objectLiteral = Node.newObjectLiteral();
    objectLiteral.setFirstChild(Node.newProperty("a", Node.newString("1")));
    objectLiteral.setNext(Node.newProperty("b", Node.newObjectLiteral()));
    objectLiteral.getLastChild().setFirstChild(Node.newProperty("c", Node.newString("3")));
    Reference ref = new Reference(null, null, null, null, null, null, null, null,
        null, null, null, null, Node.newString("b.c"), null, null, null, null, null,
        null, null, null, null, null, null, null);
    boolean result = inlineObjectLiterals.new InliningBehavior().isInlinableObject(
        Lists.newArrayList(ref));
    assertEquals(false, result);
  }

  @Test
  public void testReplaceAssignmentExpression() {
    Var v = new Var("v", null, null, null, null, null, null, false, false,
        null, false, null, null);
    Reference declaration = new Reference(null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null);
    Reference init = new Reference(null, Node.newString("{a: 1, b: {c: 2}}"),
        null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null);
    Map<String, String> varmap = Maps.newLinkedHashMap();
    Map<String, Node> initvals = Maps.newHashMap();
    inlineObjectLiterals.new InliningBehavior().fillInitialValues(init, initvals);
    inlineObjectLiterals.new InliningBehavior().splitObject(v, declaration, init,
        null);
    List<Node> nodes = Lists.newArrayList();
    nodes.add(Node.newAssign(Node.newString(
        "v_a_" + safeNameIdSupplier.get(), null), Node.newString("1"), null));
    nodes.add(Node.newAssign(Node.newString(
        "v_b_c_" + safeNameIdSupplier.get(), null), Node.newString("2"), null));
    nodes.add(Node.newAssign(Node.newString("true"), null, null));
    Node replacement = inlineObjectLiterals.new InliningBehavior().replaceAssignmentExpression(v,
        init, varmap);
    assertEquals(nodes, NodeUtil.traverseAndGetChildren(replacement));
  }
}