import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.VAR_PREFIX;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler(null, null);
    safeNameIdSupplier = () -> "test_" + System.currentTimeMillis();
  }

  @Test
  public void testInlineObjectLiterals() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);
    Node root = Node.objectLiteral();
    Node externs = Node.objectLiteral();
    inlineObjectLiterals.process(externs, root);

    // Test object literal with no references.
    Var objectVar = new Scope.Var(NodeUtil.newNameNode("obj"), null, Node.SCOPE_GLOBAL, null);
    Node objectNode = Node.objectLiteral(NodeUtil.newNameNode("key"), NodeUtil.newStringLiteral("value"));
    Node objectAssignment = Node.assign(objectVar, objectNode);
    Node objectStatement = NodeUtil.newExprStatement(objectAssignment);
    Node rootWithObject = NodeUtil.newBlock(Lists.newArrayList(objectStatement), root);

    ReferenceCollection referenceInfo = new ReferenceCollection();
    new InliningBehavior(inlineObjectLiterals).afterExitScope(
        NodeUtil.newRootNodeTraversal(rootWithObject), referenceInfo);

    assertEquals(Sets.newHashSet(objectVar), referenceInfo.staleVars);
    assertEquals(0, referenceInfo.references.size());

    // Test object literal with single reference.
    Node objRef = referenceInfo.getReferences(objectVar).get(0);
    Node refNode = objRef.getNode();
    Node objRefNode = Node.getNameNode(VAR_PREFIX + objectVar.name + "_key");
    Node objRefValue = NodeUtil.newStringLiteral("value");

    Node objRefStatement = NodeUtil.newAssignStatement(objRefNode, objRefValue);
    Node rootWithRef = NodeUtil.newBlock(
        Lists.newArrayList(objRefStatement), rootWithObject);

    referenceInfo = new ReferenceCollection();
    new InliningBehavior(inlineObjectLiterals).afterExitScope(
        NodeUtil.newRootNodeTraversal(rootWithRef), referenceInfo);

    assertEquals(Sets.newHashSet(objectVar), referenceInfo.staleVars);
    assertEquals(1, referenceInfo.references.size());

    Reference ref = referenceInfo.references.get(0);
    assertEquals(objRef, ref);
    assertEquals(objRefNode, ref.getNode());

    // Test object literal with multiple references.
    Node objRef2 = new Reference(null, null, null, null, null, null, null);
    Node objRef2Node = NodeUtil.newNameNode("obj2");
    objRef2.setNode(NodeUtil.newAssignStatement(objRef2Node, objectNode.cloneTree()));
    referenceInfo.addReference(objectVar, objRef2);

    Node rootWithRefs = NodeUtil.newBlock(
        Lists.newArrayList(objRefStatement, objRef2Node), rootWithRef);

    referenceInfo = new ReferenceCollection();
    new InliningBehavior(inlineObjectLiterals).afterExitScope(
        NodeUtil.newRootNodeTraversal(rootWithRefs), referenceInfo);

    assertEquals(Sets.newHashSet(objectVar), referenceInfo.staleVars);
    assertEquals(2, referenceInfo.references.size());

    ref = referenceInfo.references.get(1);
    assertEquals(objRef2, ref);
    assertEquals(objRef2Node, ref.getNode());

    // Test object literal with self-referential assignment.
    Node selfRefObjectNode = Node.objectLiteral(
        Node.newNameNode("self"), Node.newObjectLiteral(Node.newNameNode("key"), Node.newNameNode("value")));
    Node selfRefObjectStatement = NodeUtil.newExprStatement(selfRefObjectNode);
    Node selfRefObject = NodeUtil.newAssignStatement(
        NodeUtil.newNameNode("self"), selfRefObjectNode);
    Node rootWithSelfRef = NodeUtil.newBlock(
        Lists.newArrayList(selfRefObjectStatement, selfRefObject), rootWithRefs);

    referenceInfo = new ReferenceCollection();
    new InliningBehavior(inlineObjectLiterals).afterExitScope(
        NodeUtil.newRootNodeTraversal(rootWithSelfRef), referenceInfo);

    assertEquals(Sets.newHashSet(objectVar), referenceInfo.staleVars);
    assertEquals(0, referenceInfo.references.size());
  }

  private static class InliningBehavior extends InlineObjectLiterals.InliningBehavior {

    public InliningBehavior(InlineObjectLiterals inlineObjectLiterals) {
      super();
      this.compiler = inlineObjectLiterals.compiler;
    }
  }
}