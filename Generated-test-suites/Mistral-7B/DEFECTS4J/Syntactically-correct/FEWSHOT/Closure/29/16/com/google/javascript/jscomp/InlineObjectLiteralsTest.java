package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Given a simple object literal
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: {'d': 3}" +
        "  };" +
        "  return obj.c.d;" +
        "}";

    // When we process the code with InlineObjectLiterals
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    InliningBehavior inliningBehavior = new InliningBehavior();
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, inliningBehavior.safeNameIdSupplier);
    inlineObjectLiterals.process(null, compiler.parse(sourceCode, null, null));
    Node root = compiler.getOutput();

    // Then the object literal should be inlined into individual variables
    Node obj = root.lookupVar("obj");
    Iterator<Var> vars = obj.scope.getVars().iterator();
    List<Node> variables = Lists.newArrayList();

    while (vars.hasNext()) {
      Var var = vars.next();
      if (var.isGlobal()) {
        continue;
      }
      variables.add(var.node);
    }

    // And the object's keys should be assigned to these variables
    for (Node variable : variables) {
      Node value = variable.getFirstChild();
      if (value.isObjectLit()) {
        assertNotNull(value.getFirstChild());
        assertEquals(value.getFirstChild().getString(), "a");
        Node valueOfA = value.getFirstChild().getNext();
        assertEquals(valueOfA.getNodeType(), Token.TOKEN_NUMBER);
        assertEquals(valueOfA.getFirstChild().getInt(), 1);

        Node nextKeyNode;
        while ((nextKeyNode = value.getNext()).getNodeType() != Token.TOKEN_COMMA) {
          if (nextKeyNode.isObjectLit()) {
            assertNotNull(nextKeyNode.getFirstChild());
            assertEquals(nextKeyNode.getFirstChild().getString(), "c");
            Node cNode = nextKeyNode.getFirstChild().getNext();
            assertNotNull(cNode);
            assertEquals(cNode.getNodeType(), Token.TOKEN_OBJ_OPEN_BRACKET);

            Node cObject = cNode.getNext();
            assertNotNull(cObject);
            assertEquals(cObject.getNodeType(), Token.TOKEN_OBJ_CLOSE_BRACKET);
            Node dNode = cObject.getNext();
            assertNotNull(dNode);
            assertEquals(dNode.getNodeType(), Token.TOKEN_NUMBER);
            assertEquals(dNode.getFirstChild().getInt(), 3);
          }
        }
      }
    }
  }
}

class InliningBehavior {
  // Reuse the same implementation as in the original InlineObjectLiterals class
  // for simplicity
  // ...
}