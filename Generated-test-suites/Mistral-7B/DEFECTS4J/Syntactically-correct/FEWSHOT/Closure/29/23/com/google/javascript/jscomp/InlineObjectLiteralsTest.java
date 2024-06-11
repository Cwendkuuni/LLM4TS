package org.apache.commons.cli.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Compiler;
import org.apache.commons.cli.CompilerPass;
import org.apache.commons.cli.ReferenceCollectingCallback.Behavior;
import org.apache.commons.cli.ReferenceCollectingCallback.Reference;
import org.apache.commons.cli.ReferenceCollectingCallback.ReferenceCollection;
import org.apache.commons.cli.ReferenceCollectingCallback.ReferenceMap;
import org.apache.commons.cli.Scope;
import org.apache.commons.cli.tests.testdata.InlineObjectLiteralsTestData;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope.Var;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler,
        () -> "tmp_" + System.currentTimeMillis());

    InlineObjectLiteralsTestData testData = new InlineObjectLiteralsTestData();

    Node sourceNode = testData.getSourceNode();
    ReferenceCollection referenceCollection = new ReferenceCollectingCallback(
        compiler, inlineObjectLiterals.new InliningBehavior()).process(testData.getExterns(), sourceNode);

    inlineObjectLiterals.process(testData.getExterns(), sourceNode);

    Scope globalScope = compiler.getGlobalScope();
    Var objectVar = globalScope.getVar("obj");

    // Check that the object variable has been replaced with individual variables
    for (Var var : globalScope.getVars()) {
      if (var.name.startsWith("JSCompiler_object_inline_")) {
        String key = var.name.substring(31);
        Reference reference = referenceCollection.getReferences(var)[0];
        Node node = reference.getNode();

        // Check that the variable has been assigned a value
        assertNotNull(node.getAssignedValue());

        // Check that the value matches the expected key
        Node valueNode = node.getFirstChild();
        assertTrue(valueNode.isStringLiteral());
        String value = valueNode.getString();
        assertTrue(value.equals(key));
      }
    }

    // Check that the original object literal has been removed
    Node rootNode = testData.getRootNode();
    Node objectLiteralNode = NodeUtil.findFirstChildWithName(rootNode, "obj");
    assertNull(objectLiteralNode);
  }
}

class InlineObjectLiteralsTestData {
  Node getExterns() {
    return Node.blankNode();
  }

  Node getSourceNode() {
    Node rootNode = Node.blankNode();
    rootNode.setLineNumber(1);

    Node objNode = Node.newObjectLiteral();
    objNode.setLineNumber(1);
    objNode.addChildToBack(NodeUtil.newProperty(NodeUtil.newStringLiteral("a"), NodeUtil.newStringLiteral("1")));
    objNode.addChildToBack(NodeUtil.newProperty(NodeUtil.newStringLiteral("b"), NodeUtil.newStringLiteral("2")));

    Node objAssignNode = Node.newAssign(Node.newName("obj"), objNode);
    objAssignNode.setLineNumber(1);

    rootNode.addChildToBack(objAssignNode);
    return rootNode;
  }

  Node getRootNode() {
    return Node.blankNode();
  }
}

class InlineObjectLiterals extends InlineObjectLiterals implements TestingCompilerPass {

  @Override
  public String getPassName() {
    return "InlineObjectLiterals";
  }
}

class InliningBehavior extends InliningBehavior {

  @Override
  public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
    super.afterExitScope(t, referenceMap);

    if (staleVars.isEmpty()) {
      return;
    }

    for (Var v : staleVars) {
      Node declNode = v.getNode();
      Node replacement = Node.newAssign(declNode, Node.newName(v.name));
      replacement.setLineNumber(declNode.getLineNumber());

      declNode.getParent().replaceChild(declNode, replacement);
      staleVars.remove(v);
    }
  }
}