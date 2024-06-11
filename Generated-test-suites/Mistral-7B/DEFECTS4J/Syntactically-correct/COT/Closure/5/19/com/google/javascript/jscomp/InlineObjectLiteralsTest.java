import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeUtil;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {
  private Compiler compiler;
  private InlineObjectLiterals inlineObjectLiterals;
  private ReferenceCollectingCallback callback;

  @Before
  public void setup() {
    compiler = new Compiler();
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");
    callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior());
  }

  @Test
  public void testProcess() {
    Node externs = Node.externalSource("", "window");
    Node root = Node.externalSource("", "function() {}");
    inlineObjectLiterals.process(externs, root);
    assertTrue(callback.hasCalledProcess());
  }

  @Test
  public void testProcessWithEmptyNodeTree() {
    Node externs = Node.empty();
    Node root = Node.empty();
    inlineObjectLiterals.process(externs, root);
    assertTrue(callback.hasCalledProcess());
  }

  @Test
  public void testProcessWithInvalidNodeTree() {
    Node externs = Node.externalSource("", "function() { throw new Error(); }");
    Node root = Node.externalSource("", "function() { return 1; }");
    inlineObjectLiterals.process(externs, root);
    assertFalse(callback.hasCalledProcess());
  }

  @Test
  public void testIsVarInlineForbiddenForVarInExterns() {
    Scope scope = new Scope(Node.newScopeNode());
    Var var = new Var(Token.NAME, "a", 0, Node.newScopeNode(), null, null, null);
    scope.addVar(var);
    Node root = Node.newExprStatement(
        Node.isVarAssign(
            Node.newName("externs"),
            Node.newName("a")
        )
    );
    ReferenceCollection referenceInfo = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(
        new NodeTraversal(compiler, root), referenceInfo
    );
    assertTrue(inlineObjectLiterals.isVarInlineForbidden(var));
  }

  @Test
  public void testIsVarInlineForbiddenForExportedVar() {
    Scope scope = new Scope(Node.newScopeNode());
    Var var = new Var(Token.NAME, "a", 0, Node.newScopeNode(), null, null, null);
    scope.addVar(var);
    Node root = Node.newExprStatement(Node.newAssign(
        Node.newName("exports"),
        Node.newName("a")
    ));
    ReferenceCollection referenceInfo = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(
        new NodeTraversal(compiler, root), referenceInfo
    );
    assertTrue(inlineObjectLiterals.isVarInlineForbidden(var));
  }

  @Test
  public void testIsVarInlineForbiddenForRenamePropertyFunctionName() {
    Scope scope = new Scope(Node.newScopeNode());
    Var var = new Var(Token.NAME, "a", 0, Node.newScopeNode(), null, null, null);
    scope.addVar(var);
    Node root = Node.newExprStatement(Node.newAssign(
        Node.newName("a"),
        Node.newName(RenameProperties.RENAME_PROPERTY_FUNCTION_NAME)
    ));
    ReferenceCollection referenceInfo = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(
        new NodeTraversal(compiler, root), referenceInfo
    );
    assertTrue(inlineObjectLiterals.isVarInlineForbidden(var));
  }

  @Test
  public void testIsVarInlineForbiddenForStaleVar() {
    Scope scope = new Scope(Node.newScopeNode());
    Var var = new Var(Token.NAME, "a", 0, Node.newScopeNode(), null, null, null);
    scope.addVar(var);
    Node root = Node.newExprStatement(Node.newAssign(
        Node.newName("a"),
        Node.newObjectLit()
    ));
    ReferenceCollection referenceInfo = new ReferenceCollection();
    inlineObjectLiterals.new InliningBehavior().afterExitScope(
        new NodeTraversal(compiler, root), referenceInfo
    );
    staleVars.add(var);
    assertTrue(inlineObjectLiterals.isVarInlineForbidden(var));
  }

  @Test
  public void testIsInlinableObjectWithNoDirectReferences() {
    List<Reference> refs = Lists.newArrayList();
    ReferenceCollection referenceInfo = new ReferenceCollection(refs);
    assertFalse(inlineObjectLiterals.isInlinableObject(referenceInfo));
  }

  @Test
  public void testIsInlinableObjectWithOneDirectReference() {
    List<Reference> refs = Lists.newArrayList(
        new Reference(new Node(Token.GET_PROP), Node.newName("x"), new Node(), 0, null)
    );
    ReferenceCollection referenceInfo = new ReferenceCollection(refs);
    assertFalse(inlineObjectLiterals.isInlinableObject(referenceInfo));
  }

  @Test
  public void testIsInlinableObjectWithMultipleDirectReferences() {
    List<Reference> refs = Lists.newArrayList(
        new Reference(new Node(Token.GET_PROP), Node.newName("x"), new Node(), 0, null),
        new Reference(new Node(Token.GET_PROP), Node.newName("y"), new Node(), 0, null)
    );
    ReferenceCollection referenceInfo = new ReferenceCollection(refs);
    assertFalse(inlineObjectLiterals.isInlinableObject(referenceInfo));
  }

  @Test
  public void testIsInlinableObjectWithOneSelfReference() {
    List<Reference> refs = Lists.newArrayList(
        new Reference(new Node(Token.ASSIGN), Node.newName("x"), Node.newObjectLit(), 0, null)
    );
    ReferenceCollection referenceInfo = new ReferenceCollection(refs);
    Node object = refs.get(0).getAssignedValue();
    assertFalse(inlineObjectLiterals.isInlinableObject(referenceInfo));
    assertFalse(inlineObjectLiterals.isVarInlineForbidden(object.getFirstChild()));
  }

  @Test
  public void testIsInlinableObjectWithMultipleSelfReferences() {
    List<Reference> refs = Lists.newArrayList(
        new Reference(new Node(Token.ASSIGN), Node.newName("x"), Node.newObjectLit(), 0, null),
        new Reference(new Node(Token.ASSIGN), Node.newName("y"), Node.newObjectLit(), 0, null)
    );
    ReferenceCollection referenceInfo = new ReferenceCollection(refs);
    Node object = refs.get(0).getAssignedValue();
    assertFalse(inlineObjectLiterals.isInlinableObject(referenceInfo));
    Node x = object.getFirstChild();
    Node y = object.getFirstChild().getNext();
    assertTrue(inlineObjectLiterals.isVarInlineForbidden(x));
    assertTrue(inlineObjectLiterals.isVarInlineForbidden(y));
  }

  @Test
  public void testIsInlinableObjectWithOneImmediateChildReference() {
    List<Reference> refs = Lists.newArrayList(
        new Reference(new Node(Token.ASSIGN), Node.newName("x"), Node.newObjectLit(), 0, null),
        new Reference(new Node(Token.GET_PROP), Node.newName("y"), Node.newName("x"), 0, null)
    );
    ReferenceCollection referenceInfo = new ReferenceCollection(refs);
    Node object = refs.get(0).getAssignedValue();
    assertFalse(inlineObjectLiterals.isInlinableObject(referenceInfo));
    Node x = object.getFirstChild();
    Node y = x.getFirstChild();
    assertFalse(inlineObjectLiterals.isVarInlineForbidden(x));
    assertTrue(inlineObjectLiterals.isVarInlineForbidden(y));
  }

  @Test
  public void testIsInlinableObjectWithMultipleImmediateChildReferences() {
    List<Reference> refs = Lists.newArrayList(
        new Reference(new Node(Token.ASSIGN), Node.newName("x"), Node.newObjectLit(), 0, null),
        new Reference(new Node(Token.GET_PROP), Node.newName("y"), Node.newName("x"), 0, null),
        new Reference(new Node(Token.GET_PROP), Node.newName("z"), Node.newName("x"), 0, null)
    );
    ReferenceCollection referenceInfo = new ReferenceCollection(refs);
    Node object = refs.get(0).getAssignedValue();
    assertFalse(inlineObjectLiterals.isInlinableObject(referenceInfo));
    Node x = object.getFirstChild();
    Node y = x.getFirstChild();
    Node z = y.getNext();
    assertFalse(inlineObjectLiterals.isVarInlineForbidden(x));
    assertTrue(inlineObjectLiterals.isVarInlineForbidden(y));
    assertTrue(inlineObjectLiterals.isVarInlineForbidden(z));
  }

  @Test
  public void testIsInlinableObjectWithMultipleDeepChildReferences() {
    List<Reference> refs = Lists.newArrayList(
        new Reference(new Node(Token.ASSIGN), Node.newName("x"), Node.newObjectLit(), 0, null),
        new Reference(new Node(Token.GET_PROP), Node.newName("y"), Node.newName("x"), 0, null),
        new Reference(new Node(Token.GET_PROP), Node.newName("z"), Node.newName("y"), 0, null)
    );
    ReferenceCollection referenceInfo = new ReferenceCollection(refs);
    Node object = refs.get(0).getAssignedValue();
    assertFalse(inlineObjectLiterals.isInlinableObject(referenceInfo));
    Node x = object.getFirstChild();
    Node y = x.getFirstChild();
    Node z = y.getNext();
    assertFalse(inlineObjectLiterals.isVarInlineForbidden(x));
    assertTrue(inlineObjectLiterals.isVarInlineForbidden(y));
    assertTrue(inlineObjectLiterals.isVarInlineForbidden(z));
  }

  @Test
  public void testComputeVarList() {
    List<Reference> refs = Lists.newArrayList(
        new Reference(new Node(Token.ASSIGN), Node.newName("a"), Node.newObjectLit(), 0, null),
        new Reference(new Node(Token.ASSIGN), Node.newName("b"), Node.newObjectLit(), 0, null)
    );
    ReferenceCollection referenceInfo = new ReferenceCollection(refs);
    Map<String, String> varmap = inlineObjectLiterals.computeVarList(new Var(Token.NAME, "c", 0, null, null, null, null), referenceInfo);
    assertEquals(2, varmap.size());
    assertEquals("JSCompiler_object_inline_a_0", varmap.get("a"));
    assertEquals("JSCompiler_object_inline_b_0", varmap.get("b"));
  }

  @Test
  public void testFillInitialValues() {
    List<Reference> refs = Lists.newArrayList(
        new Reference(new Node(Token.ASSIGN), Node.newName("a"), Node.newObjectLit(), 0, null)
    );
    Map<String, Node> initvals = Maps.newHashMap();
    inlineObjectLiterals.fillInitialValues(refs.get(0), initvals);
    assertEquals("JSCompiler_object_inline_a_1", initvals.get("a").getString());
  }

  @Test
  public void testReplaceAssignmentExpression() {
    List<Reference> refs = Lists.newArrayList(
        new Reference(new Node(Token.ASSIGN), Node.newName("a"), Node.newObjectLit(), 0, null)
    );
    Map<String, String> varmap = inlineObjectLiterals.computeVarList(new Var(Token.NAME, "a", 0, null, null, null, null), refs);
    ReferenceCollection referenceInfo = new ReferenceCollection(refs);
    Node object = refs.get(0).getAssignedValue();
    Node root = Node.newExprStatement(Node.newAssign(Node.newName("a"), Node.newObjectLit()));
    inlineObjectLiterals.splitObject(new Var(Token.NAME, "a", 0, null, null, null, null), refs.get(0), referenceInfo);
    Node newRoot = (Node) root.cloneTree();
    inlineObjectLiterals.replaceAssignmentExpression(new Var(Token.NAME, "a", 0, null, null, null, null), refs.get(0), varmap);
    assertEquals(3, newRoot.getFirstChild().getNumChildren());
  }
}