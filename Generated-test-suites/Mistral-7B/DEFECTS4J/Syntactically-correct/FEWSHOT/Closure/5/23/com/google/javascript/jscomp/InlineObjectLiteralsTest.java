package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setup() {
    compiler = new AbstractCompiler() {};
    safeNameIdSupplier = () -> "test";
    inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  @Test
  public void testInlineObjectLiterals() {
    Node root = Node.root();

    inlineObjectLiterals.process(Node.NULL, root);

    // Verify that all object literals have been inlined and replaced
    // with individual variables.
    Iterator<Node> iterator = NodeUtil.depthFirstIteration(root, NodeUtil.MATCH_ANY);
    while (iterator.hasNext()) {
      Node node = iterator.next();
      if (node.isObjectLit()) {
        assertNull(node);
        continue;
      }

      if (node.isVar()) {
        String variableName = node.getString();
        Node variableValue = null;

        // Find the corresponding variable value
        for (Iterator<Node> childIterator = node.getChildNodes(); childIterator.hasNext();) {
          Node childNode = childIterator.next();
          if (childNode.isAssign()) {
            variableValue = childNode.getLastChild();
            break;
          }
        }

        Map<String, String> varmap = Maps.newLinkedHashMap();
        ReferenceCollection referenceInfo = new ReferenceCollectingCallback(
            compiler, inlineObjectLiterals.new InliningBehavior()).process(Node.NULL, root);

        // Check that the variable has been assigned to a valid object literal
        assertNotNull(variableValue);
        assert(inlineObjectLiterals.new InliningBehavior().isInlinableObject(
            referenceInfo.getReferencesForName(variableName)));

        // Verify that the object literal has been inlined and replaced
        // with individual variables.
        Map<String, Node> initvals = Maps.newHashMap();
        inlineObjectLiterals.new InliningBehavior().splitObject(
            new Var(null, null, null, null, null, null, null, null, null, null),
            null, null, referenceInfo);

        for (Map.Entry<String, Node> entry : varmap.entrySet()) {
          String key = entry.getKey();
          String expectedVarname = VAR_PREFIX + key + "_test";
          String actualVarname = variableValue.getFirstChild().getString();
          assertEquals(expectedVarname, actualVarname);

          // Check that the corresponding value has been assigned to the
          // generated variable.
          Node variable = NodeUtil.findNode(root, "JSCompiler_object_inline_" + key);
          assertNotNull(variable);
          Node value = variable.getLastChild();
          assertNotNull(value);
          assert(value.isName());
          assertEquals(entry.getValue(), value.getString());

          // Check that all uses of the original object literal have been
          // replaced with the generated variables.
          for (Reference ref : referenceInfo.getReferencesForName(key)) {
            Node use = ref.getNode();
            assertNotNull(use);
            Node newValue = null;

            if (use.isAssIGN()) {
              // Check that the assignment has been replaced with multiple
              // assignments to the generated variables.
              List<Node> newAssignments = Lists.newArrayList();
              for (Node child = use.getFirstChild(); child != null;
                   child = child.getNext()) {
                if (child.isAssign()) {
                  newAssignments.add(child);
                } else if (child.isName()) {
                  newValue = child;
                  break;
                }
              }
              for (Node newAssignment : newAssignments) {
                Node left = newAssignment.getFirstChild();
                Node right = newAssignment.getLastChild();
                assertEquals(variable, left);
                assertEquals(entry.getValue(), right.getString());
              }
            } else if (use.isGetProp()) {
              // Check that the GETPROP node has been replaced with a NAME
              // node for the generated variable.
              assertEquals(variable, use.getFirstChild());
            }
          }
        }
      }
    }
  }
}