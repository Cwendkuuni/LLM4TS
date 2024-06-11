import static org.junit.Assert.*;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior.AFTER_EXIT_SCOPE;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  private InlineObjectLiterals inlineObjectLiterals;
  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    compiler = new NoOpCompiler();
    safeNameIdSupplier = () -> "test_id_" + System.currentTimeMillis();

    inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  @Test
  public void testInlineObjectLiterals() {
    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    inlineObjectLiterals.process(externs, root);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, AFTER_EXIT_SCOPE);
    callback.process(externs, root);

    ReferenceCollection referenceCollection = callback.getReferenceCollection();

    Map<Var, List<Reference>> referencesByVar = newHashMap();
    for (Var var : referenceCollection.getScope().getVars()) {
      referencesByVar.put(var, Lists.newArrayList());
    }

    for (Reference reference : referenceCollection.references) {
      referencesByVar.get(reference.getVariable()).add(reference);
    }

    for (Map.Entry<Var, List<Reference>> entry : referencesByVar.entrySet()) {
      Var var = entry.getKey();
      List<Reference> references = entry.getValue();
      assertEquals(1, references.size());

      Reference reference = references.get(0);

      Node referenceNode = reference.getNode();
      Node parentNode = referenceNode.getParent();

      if (parentNode.getType() == Token.VAR) {
        Node varNode = parentNode.getFirstChild();
        assertEquals(var, varNode);
      } else {
        Node getPropNode = parentNode;
        String propName = getPropNode.getLastChild().getString();

        Map<String, String> varmap = inlineObjectLiterals.new InliningBehavior().computeVarList(var, referenceCollection);
        String newVarName = varmap.get(propName);
        Node newVarNode = Node.newVarNode(newVarName, null);

        assertEquals(getNodeType(parentNode), getNodeType(newVarNode));
        assertEquals(parentNode, newVarNode.getParent());

        assertEquals(getNodeType(referenceNode), getNodeType(newVarNode.getFirstChild()));
        assertEquals(referenceNode, newVarNode.getFirstChild());
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithDeepObjects() {
    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    inlineObjectLiterals.process(externs, root);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, AFTER_EXIT_SCOPE);
    callback.process(externs, root);

    ReferenceCollection referenceCollection = callback.getReferenceCollection();

    Map<Var, List<Reference>> referencesByVar = newHashMap();
    for (Var var : referenceCollection.getScope().getVars()) {
      referencesByVar.put(var, Lists.newArrayList());
    }

    for (Reference reference : referenceCollection.references) {
      referencesByVar.get(reference.getVariable()).add(reference);
    }

    for (Map.Entry<Var, List<Reference>> entry : referencesByVar.entrySet()) {
      Var var = entry.getKey();
      List<Reference> references = entry.getValue();
      assertEquals(1, references.size());

      Reference reference = references.get(0);

      Node referenceNode = reference.getNode();
      Node parentNode = referenceNode.getParent();

      if (parentNode.getType() == Token.VAR) {
        Node varNode = parentNode.getFirstChild();
        assertEquals(var, varNode);
      } else {
        Node getPropNode = parentNode;
        String propName = getPropNode.getLastChild().getString();

        Map<String, String> varmap = inlineObjectLiterals.new InliningBehavior().computeVarList(var, referenceCollection);
        String newVarName = varmap.get(propName);
        Node newVarNode = Node.newVarNode(newVarName, null);

        assertEquals(getNodeType(parentNode), getNodeType(newVarNode));
        assertEquals(parentNode, newVarNode.getParent());

        Node objNode = referenceNode.getAssignedValue();

        Map<String, String> objVarmap = inlineObjectLiterals.new InliningBehavior().computeVarList(var, referenceCollection);
        for (Node child = objNode.getFirstChild(); child != null; child = child.getNext()) {
          if (child.getType() == Token.OBJECTLIT) {
            Map<String, String> nestedVarmap = inlineObjectLiterals.new InliningBehavior().computeVarList(var, referenceCollection);
            String nestedNewVarName = nestedVarmap.get(child.getFirstChild().getString());
            Node nestedNewVarNode = Node.newVarNode(nestedNewVarName, null);

            Node nestedObjNode = child.getFirstChild().getNext();
            assertEquals(getNodeType(child), getNodeType(nestedNewVarNode));
            assertEquals(child, nestedNewVarNode);

            Node nestedRefNode = Node.newGetPropNode(null, nestedNewVarName, null);
            Node nestedRefParentNode = nestedRefNode.getParent();

            Reference nestedRef = new Reference(nestedRefNode, null, null, referenceCollection.getScope(), null);
            inlineObjectLiterals.new InliningBehavior().splitObject(var, reference, nestedRef, referenceCollection);

            assertEquals(getNodeType(nestedRefParentNode), getNodeType(nestedNewVarNode.getParent()));
            assertEquals(nestedRefParentNode, nestedNewVarNode.getParent());

            assertEquals(getNodeType(nestedRefNode), getNodeType(nestedNewVarNode.getFirstChild()));
            assertEquals(nestedRefNode, nestedNewVarNode.getFirstChild());
          }
        }
      }
    }
  }

  private int getNodeType(Node node) {
    return node == null ? Token.NULL : node.getType();
  }
}