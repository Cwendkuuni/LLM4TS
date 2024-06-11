package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node root = NodeFactory.createScript("");

    // Test traversing a function outside of a function or block context
    Node functionNode1 = NodeFactory.createFunction("func1", null, null);
    Node functionParent1 = NodeFactory.createName("window");
    Node functionGrandparent1 = NodeFactory.createScript("");
    Node functionGrandparentParent1 = NodeFactory.createName("global");

    Node functionNode2 = NodeFactory.createFunction("func2", null, null);
    Node functionParent2 = NodeFactory.createBlock("{}");
    Node functionParentParent2 = NodeFactory.createName("global");

    Node functionNode3 = NodeFactory.createFunction("func3", null, null);
    Node functionParent3 = NodeFactory.createName("this");
    Node functionParentParent3 = NodeFactory.createBlock("{}");

    Node functionNode4 = NodeFactory.createFunction("func4", null, null);
    Node functionParent4 = NodeFactory.createBlock("{function f() {}}");
    Node functionParentParent4 = NodeFactory.createName("global");

    Node functionNode5 = NodeFactory.createFunction("func5", null, null);
    Node functionParent5 = NodeFactory.createName("Object");
    Node functionParentParent5 = NodeFactory.createBlock("{}");

    Node functionNode6 = NodeFactory.createFunction("func6", null, null);
    Node functionParent6 = NodeFactory.createName("global");
    Node functionParentParent6 = NodeFactory.createName("global");

    Node functionNode7 = NodeFactory.createFunction("func7", null, null);
    Node functionParent7 = NodeFactory.createName("function");
    Node functionParentParent7 = NodeFactory.createName("global");

    when(compiler.getCurrentNode()).thenReturn(null);
    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(functionGrandparent1);
    when(functionGrandparent1.getPreviousSibling()).thenReturn(functionNode1);

    assertTrue(checker.shouldTraverse(traversal, functionNode1, functionGrandparent1));

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(functionParent1);
    when(functionParent1.getParent()).thenReturn(functionGrandparentParent1);
    when(functionGrandparentParent1.getPreviousSibling()).thenReturn(functionNode1);

    assertTrue(checker.shouldTraverse(traversal, functionNode1, functionGrandparentParent1));

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(functionParent2);
    when(functionParent2.getParent()).thenReturn(functionParentParent2);

    assertFalse(checker.shouldTraverse(traversal, functionNode2, functionParentParent2));

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(functionParent3);
    when(functionParent3.getParent()).thenReturn(functionParentParent3);

    assertFalse(checker.shouldTraverse(traversal, functionNode3, functionParentParent3));

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(functionParent4);
    when(functionParent4.getParent()).thenReturn(functionParentParent4);

    assertFalse(checker.shouldTraverse(traversal, functionNode4, functionParentParent4));

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(functionParent5);
    when(functionParent5.getParent()).thenReturn(functionParentParent5);

    assertFalse(checker.shouldTraverse(traversal, functionNode5, functionParentParent5));

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(functionParent6);
    when(functionParent6.getParent()).thenReturn(functionParentParent6);
    when(functionParentParent6.getPreviousSibling()).thenReturn(functionNode6);

    assertTrue(checker.shouldTraverse(traversal, functionNode6, functionParentParent6));

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(functionParent7);
    when(functionParent7.getParent()).thenReturn(functionParentParent7);

    assertFalse(checker.shouldTraverse(traversal, functionNode7, functionParentParent7));
  }

  @Test
  public void testShouldReportThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node root = NodeFactory.createScript("");

    Node thisNode1 = NodeFactory.createThis();
    Node parent1 = NodeFactory.createName("global");

    Node thisNode2 = NodeFactory.createThis();
    Node parent2 = NodeFactory.createGetProp(NodeFactory.createName("prototype"));

    Node thisNode3 = NodeFactory.createThis();
    Node parent3 = NodeFactory.createBlock("{}");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(parent1);

    assertTrue(checker.shouldReportThis(traversal, thisNode1, parent1));

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(parent2);

    assertTrue(checker.shouldReportThis(traversal, thisNode2, parent2));

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(parent3);

    assertFalse(checker.shouldReportThis(traversal, thisNode3, parent3));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = NodeFactory.createFunction("func", null, null);
    Node parent = NodeFactory.createName("global");

    JSDocInfo jsDoc1 = checker.getFunctionJsDocInfo(functionNode);
    assertNull(jsDoc1);

    when(functionNode.getParent()).thenReturn(parent);
    JSDocInfo jsDoc2 = checker.getFunctionJsDocInfo(functionNode);
    assertNull(jsDoc2);

    when(parent.getPreviousSibling()).thenReturn(NodeFactory.createName("var func"));
    JSDocInfo jsDoc3 = checker.getFunctionJsDocInfo(functionNode);
    assertNull(jsDoc3);

    when(parent.getFirstChild()).thenReturn(NodeFactory.createVar("var func"));
    JSDocInfo jsDoc4 = checker.getFunctionJsDocInfo(functionNode);
    assertNull(jsDoc4);

    when(parent.getFirstChild()).thenReturn(functionNode);
    JSDocInfo jsDoc5 = checker.getFunctionJsDocInfo(functionNode);
    assertNull(jsDoc5);

    String funcJsDoc = "/**\n" +
        " * Function documentation.\n" +
        " */";

    when(functionNode.getRawSource()).thenReturn(funcJsDoc);
    JSDocInfo jsDoc6 = checker.getFunctionJsDocInfo(functionNode);
    assertEquals("Function documentation.", jsDoc6.getSummary());
  }
}