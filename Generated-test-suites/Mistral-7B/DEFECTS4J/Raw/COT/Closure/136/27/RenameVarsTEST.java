package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenameVarsTest {

  private NodeFactory nodeFactory;
  private RenameVars renameVars;
  private NodeTraversal nodeTraversal;
  private Scope scope;
  private CompilerInput input;

  @Before
  public void setup() {
    nodeFactory = new NodeFactory();
    scope = mock(Scope.class);
    input = mock(CompilerInput.class);
    renameVars = new RenameVars(mock(AbstractCompiler.class), "", false, false, false, null, null, Collections.emptySet());
    nodeTraversal = new NodeTraversal(nodeFactory, scope, null, null);
  }

  @Test
  public void testProcessEmptyExternsAndSource() {
    Node externs = nodeFactory.createTreeFromCode("// empty");
    Node root = nodeFactory.createTreeFromCode("// empty");
    renameVars.process(externs, root);
    assertEquals(0, assignmentLog.length());
  }

  @Test
  public void testProcessExternsAndSourceWithGlobalVariables() {
    String[] oldNames = {"foo", "bar", "baz"};
    Node externs = nodeFactory.createTreeFromCode("var foo = 1;\nvar bar = 2;\nvar baz = 3;");
    Node root = nodeFactory.createTreeFromCode("function foo() {}");
    when(scope.getVar("foo")).thenReturn(new Scope.Var(nodeFactory.createName("foo"), input, null, null, null));
    when(scope.getVar("bar")).thenReturn(new Scope.Var(nodeFactory.createName("bar"), input, null, null, null));
    when(scope.getVar("baz")).thenReturn(new Scope.Var(nodeFactory.createName("baz"), input, null, null, null));

    renameVars.process(externs, root);

    ListMultimap<String, String> oldToNew = ArrayListMultimap.create();
    oldToNew.putAll(renameVars.getVariableMap().getVariableMap());

    String[] newNames = oldToNew.get("foo").toArray(new String[0]);
    assertEquals(1, newNames.length);
    assertEquals("_", newNames[0]);

    newNames = oldToNew.get("bar").toArray(new String[0]);
    assertEquals(1, newNames.length);
    assertEquals("_", newNames[0]);

    newNames = oldToNew.get("baz").toArray(new String[0]);
    assertEquals(1, newNames.length);
    assertEquals("_", newNames[0]);

    assertEquals(3, assignmentLog.length());
  }

  @Test
  public void testProcessExternsAndSourceWithLocalVariables() {
    Node externs = nodeFactory.createTreeFromCode("// empty");
    Node root = nodeFactory.createTreeFromCode(
        "function foo(x, y) {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = 3;\n" +
        "}\n");

    when(scope.getVar("a")).thenReturn(new Scope.Var(nodeFactory.createName("a"), input, null, null, null));
    when(scope.getVar("b")).thenReturn(new Scope.Var(nodeFactory.createName("b"), input, null, null, null));
    when(scope.getVar("c")).thenReturn(new Scope.Var(nodeFactory.createName("c"), input, null, null, null));

    renameVars.process(externs, root);

    ListMultimap<String, String> oldToNew = ArrayListMultimap.create();
    oldToNew.putAll(renameVars.getVariableMap().getVariableMap());

    int index = 0;
    String[] newNamesForA = oldToNew.get("a").toArray(new String[0]);
    assertEquals(1, newNamesForA.length);
    assertEquals("L0_", newNamesForA[0]);

    String[] newNamesForB = oldToNew.get("b").toArray(new String[0]);
    assertEquals(1, newNamesForB.length);
    assertEquals("L1_", newNamesForB[0]);

    String[] newNamesForC = oldToNew.get("c").toArray(new String[0]);
    assertEquals(1, newNamesForC.length);
    assertEquals("L2_", newNamesForC[0]);

    assertEquals(3, assignmentLog.length());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessWithNullExterns() {
    Node root = nodeFactory.createTreeFromCode("// empty");
    renameVars.process(null, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessWithNullRoot() {
    Node externs = nodeFactory.createTreeFromCode("// empty");
    renameVars.process(externs, null);
  }

  @Test
  public void testGetVariableMap() {
    String[] oldNames = {"foo", "bar", "baz"};
    Node externs = nodeFactory.createTreeFromCode("var foo = 1;\nvar bar = 2;\nvar baz = 3;");
    Node root = nodeFactory.createTreeFromCode("function foo() {}");
    when(scope.getVar("foo")).thenReturn(new Scope.Var(nodeFactory.createName("foo"), input, null, null, null));
    when(scope.getVar("bar")).thenReturn(new Scope.Var(nodeFactory.createName("bar"), input, null, null, null));
    when(scope.getVar("baz")).thenReturn(new Scope.Var(nodeFactory.createName("baz"), input, null, null, null));

    renameVars.process(externs, root);

    Map<String, String> expectedVariableMap = Maps.newHashMap();
    expectedVariableMap.put("foo", "_");
    expectedVariableMap.put("bar", "_");
    expectedVariableMap.put("baz", "_");

    assertEquals(expectedVariableMap, renameVars.getVariableMap().getVariableMap());
  }

  @Test
  public void testOkToRenameVar() {
    boolean result = renameVars.okToRenameVar("foo", false);
    assertEquals(true, result);

    result = renameVars.okToRenameVar("EXPORTED_FOO", false);
    assertEquals(false, result);

    result = renameVars.okToRenameVar("FOO", true);
    assertEquals(false, result);
  }
}