package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RenameVarsTest {

  private AbstractCompiler compiler;
  private RenameVars renameVars;
  private VariableMap prevUsedRenameMap;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    prevUsedRenameMap = mock(VariableMap.class);

    renameVars = new RenameVars(compiler, "", false, false, false, prevUsedRenameMap,
        null, null);
  }

  @Test
  public void testEmptyClass() {
    renameVars.process(null, null);
    assertEquals("", renameVars.getVariableMap().toString());
  }

  @Test
  public void testSimpleGlobalVariableRenaming() {
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);

    when(externs.getString()).thenReturn("var x = 1;");
    when(root.getString()).thenReturn("function f() { var y = 2; }");

    renameVars.process(externs, root);

    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("{x=x, y=y}", variableMap.toString());
  }

  @Test
  public void testGlobalVariableRenamingWithExterns() {
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);

    when(externs.getString()).thenReturn("var x = 1;");
    when(root.getString()).thenReturn("function f() { var y = x; }");

    renameVars.process(externs, root);

    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("{x=x, y=x}", variableMap.toString());
  }

  @Test
  public void testLocalVariableRenaming() {
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);

    when(externs.getString()).thenReturn("var x = 1;");
    when(root.getString()).thenReturn("function f() { var x = 2; var y = x; }");

    renameVars.process(externs, root);

    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("{x=x, y=L0}", variableMap.toString());
  }

  @Test
  public void testPreserveAnonymousFunctionNames() {
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);

    when(externs.getString()).thenReturn("var x = 1;");
    when(root.getString()).thenReturn("function foo() { return function bar() {} }");

    renameVars.process(externs, root);

    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("{x=x, bar=anonymous_function_0}", variableMap.toString());
  }

  @Test(expected = RuntimeException.class)
  public void testExceptionWhenRenamingBlindlyDuplicateVariableName() {
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);

    when(externs.getString()).thenReturn("var x = 1;");
    when(root.getString()).thenReturn("function f() { var x = 2; }");

    renameVars = new RenameVars(compiler, "", false, false, false, prevUsedRenameMap,
            null, null);
    renameVars.process(externs, root);
  }

  @Test
  public void testEdgeCaseWithLongVariableNames() {
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);

    when(externs.getString()).thenReturn("");
    when(root.getString()).thenReturn("function f() {"
        + "var zpgaqzrgsdfzxvcxzvczxdfghjklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
        + "=1; var zpgaqzrgsdfzxvcxzvczxdfghjklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
        + "=2; }");

    renameVars.process(externs, root);

    VariableMap variableMap = renameVars.getVariableMap();
    List<Map.Entry<String, String>> entries = variableMap.entrySet();

    Map.Entry<String, String> firstEntry = entries.get(0);
    assertEquals("zpgaqzrgsdfzxvcxzvczxdfghjklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz",
        firstEntry.getKey());
    assertEquals("zpgaqzrgsdfzxvcxzvczxdfghjklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz0",
        firstEntry.getValue());

    Map.Entry<String, String> secondEntry = entries.get(1);
    assertEquals("zpgaqzrgsdfzxvcxzvczxdfghjklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz",
        secondEntry.getKey());
    assertEquals("zpgaqzrgsdfzxvcxzvczxdfghjklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz1",
        secondEntry.getValue());
  }
}