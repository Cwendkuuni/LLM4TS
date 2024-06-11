package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Var;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Scope scope = new Scope(null);
    Node root = new Node(Token.BLOCK);
    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> null);

    inlineObjectLiterals.process(null, root);

    assertEquals(11, scope.getVars().size());

    Var objVar = null;
    for (Var var : scope.getVars()) {
      if (var.name.startsWith("JSCompiler_object_inline_")) {
        objVar = var;
        break;
      }
    }

    Reference objReference = null;
    for (Reference reference : inlineObjectLiterals.getBehavior().staleVars) {
      if (reference.getNode().isVar() && reference.getNode().getString().equals(objVar.name)) {
        objReference = reference;
        break;
      }
    }

    assertNotNull(objReference);

    ReferenceCollection referenceInfo = inlineObjectLiterals.getBehavior().referenceMap.getReferences(objVar);

    List<Reference> references = referenceInfo.references;

    Map<String, Node> initValues = Maps.newHashMap();
    inlineObjectLiterals.getBehavior().fillInitialValues(objReference, initValues);

    Map<String, String> varMap = inlineObjectLiterals.getBehavior().computeVarList(objVar, referenceInfo);

    for (Map.Entry<String, String> entry : varMap.entrySet()) {
      Node value = initValues.get(entry.getKey());
      String varName = entry.getValue();
      String newVarName = varName.substring(varName.lastIndexOf("_") + 1);

      assertEquals(value.getClass(), Node.class);
      assertEquals(Node.NAME, value.getNodeType());
      assertEquals(newVarName, value.getString());

      Node varNode = scope.getVar(newVarName);
      assertEquals(Var.VAR, varNode.getNodeType());
      assertEquals(varName, varNode.getName());
      assertEquals(Node.UNDEFINED, varNode.getFirstChild());
    }

    // Check that all references to the object have been replaced
    Node objectLiteral = objReference.getNode().getFirstChild();
    Node parent = objectLiteral.getParent();
    int index = parent.getIndexOfChild(objectLiteral);
    Node replacement = inlineObjectLiterals.getBehavior().referenceMap.getReferences(objVar).get(0).getNode();

    for (Reference ref : referenceInfo.references) {
      if (ref != objReference) {
        Node oldReference = ref.getNode();
        Node newReference = null;

        if (oldReference.isVar()) {
          newReference = scope.getVar(varMap.get(oldReference.getString()));
        } else if (oldReference.isGetProp()) {
          String propertyName = oldReference.getLastChild().getString();
          newReference = scope.getVar(varMap.get(propertyName));
        }

        if (oldReference.getClass() == Node.class) {
          assertEquals(newReference.getNodeType(), oldReference.getNodeType());
          assertEquals(newReference, oldReference.getFirstChild());
        } else if (oldReference.getClass() == Node.class && newReference != null) {
          assertEquals(newReference, oldReference);
        }

        // Check that the reference is no longer present in the original tree
        if (parent.getClass() == Node.class) {
          Node oldParent = parent;
          parent = parent.getParent();
          parent.removeChild(oldParent);
        }
      }
    }

    // Check that the object literal has been removed from the tree
    assertNull(parent.getChildAtIndex(index));
  }
}