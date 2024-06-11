package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.Token;

public class RenameVarsTest {

  private static final String PREFIX = "test_";
  private static final Set<String> RESERVED_NAMES = Sets.newHashSet("function", "class");
  private static final char[] RESERVED_CHARS = new char[0];
  private RenameVars renameVars;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    compiler = new NoOpCompiler();
    renameVars = new RenameVars(compiler, PREFIX, false, false, false, null, RESERVED_CHARS, RESERVED_NAMES);
  }

  @Test
  public void testRenameVars() {
    Node root = NodeBuilder.newNode("function foo() {}", Token.FUNC_DECL);
    NodeTraversal.traverse(compiler, root, new RenameVars.ProcessVars(false));

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("foo", "test_0");

    VariableMap variableMap = renameVars.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsExterns() {
    Node externs = NodeBuilder.newNode("var a = 1, b = 2", Token.VAR_DECL);
    Node root = NodeBuilder.newNode("function foo() {}", Token.FUNC_DECL);

    renameVars.process(externs, root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("a", "test_1_0");
    expectedRenameMap.put("b", "test_1_1");

    VariableMap variableMap = renameVars.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsLocalVariables() {
    Node root = NodeBuilder.newNode("function foo() { var a = 1, b = 2; }", Token.FUNCTION);

    renameVars.process(NodeBuilder.newNode("{}", Token.BLOCK), root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("a", "test_2_0");
    expectedRenameMap.put("b", "test_2_1");

    VariableMap variableMap = renameVars.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsLocalVariablesWithPrefix() {
    String prefix = "test_local_";
    renameVars = new RenameVars(compiler, prefix, false, false, false, null, RESERVED_CHARS, RESERVED_NAMES);

    Node root = NodeBuilder.newNode("function foo() { var a = 1, b = 2; }", Token.FUNCTION);

    renameVars.process(NodeBuilder.newNode("{}", Token.BLOCK), root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("a", "test_local_0");
    expectedRenameMap.put("b", "test_local_1");

    VariableMap variableMap = renameVars.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsLocalVariablesWithCollision() {
    Node root = NodeBuilder.newNode("function foo() { var a = 1, a = 2; }", Token.FUNCTION);

    renameVars.process(NodeBuilder.newNode("{}", Token.BLOCK), root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("a", "test_2_0");

    VariableMap variableMap = renameVars.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsLocalVariablesWithCollisionAndPrefix() {
    String prefix = "test_local_";
    renameVars = new RenameVars(compiler, prefix, false, false, false, null, RESERVED_CHARS, RESERVED_NAMES);

    Node root = NodeBuilder.newNode("function foo() { var a = 1, a = 2; }", Token.FUNCTION);

    renameVars.process(NodeBuilder.newNode("{}", Token.BLOCK), root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("a", "test_local_1");

    VariableMap variableMap = renameVars.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsPreserveAnonymousFunctionNames() {
    renameVars = new RenameVars(compiler, null, false, true, false, null, RESERVED_CHARS, RESERVED_NAMES);

    Node root = NodeBuilder.newNode("function foo() { function bar() {} }", Token.FUNCTION);

    renameVars.process(NodeBuilder.newNode("{}", Token.BLOCK), root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("bar", "bar");

    VariableMap variableMap = renameVars.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsGeneratePseudoNames() {
    renameVars = new RenameVars(compiler, null, false, false, true, null, RESERVED_CHARS, RESERVED_NAMES);

    Node root = NodeBuilder.newNode("function foo() { var a = 1; }", Token.FUNCTION);

    renameVars.process(NodeBuilder.newNode("{}", Token.BLOCK), root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("a", "$a$$");

    VariableMap variableMap = renameVars.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsGeneratePseudoNamesWithCollision() {
    renameVars = new RenameVars(compiler, null, false, false, true, null, RESERVED_CHARS, RESERVED_NAMES);

    Node root = NodeBuilder.newNode("function foo() { var a = 1, a = 2; }", Token.FUNCTION);

    renameVars.process(NodeBuilder.newNode("{}", Token.BLOCK), root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("a", "$a_1$$");

    VariableMap variableMap = renameVars.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsReuseNames() {
    Node externs = NodeBuilder.newNode("var a = 1, a = 2, b = 3", Token.VAR_DECL);
    Node root = NodeBuilder.newNode("function foo() { var a = 4, b = 5; }", Token.FUNCTION);

    RenameVars renameVars1 = new RenameVars(compiler, null, false, false, false, null, RESERVED_CHARS, RESERVED_NAMES);
    renameVars1.process(externs, root);

    Node externs2 = NodeBuilder.newNode("var a = 6, a = 7", Token.VAR_DECL);
    Node root2 = NodeBuilder.newNode("function foo2() { var a = 8, b = 9; }", Token.FUNCTION);

    RenameVars renameVars2 = new RenameVars(compiler, null, false, false, false, renameVars1.getVariableMap(), RESERVED_CHARS, RESERVED_NAMES);
    renameVars2.process(externs2, root2);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("a", "test_8_0");
    expectedRenameMap.put("b", "test_8_1");

    VariableMap variableMap = renameVars2.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsReuseNamesWithPrefix() {
    String prefix = "test_";

    Node externs = NodeBuilder.newNode("var a = 1, a = 2, b = 3", Token.VAR_DECL);
    Node root = NodeBuilder.newNode("function foo() { var a = 4, b = 5; }", Token.FUNCTION);

    RenameVars renameVars1 = new RenameVars(compiler, prefix, false, false, false, null, RESERVED_CHARS, RESERVED_NAMES);
    renameVars1.process(externs, root);

    Node externs2 = NodeBuilder.newNode("var a = 6, a = 7", Token.VAR_DECL);
    Node root2 = NodeBuilder.newNode("function foo2() { var a = 8, b = 9; }", Token.FUNCTION);

    RenameVars renameVars2 = new RenameVars(compiler, prefix, false, false, false, renameVars1.getVariableMap(), RESERVED_CHARS, RESERVED_NAMES);
    renameVars2.process(externs2, root2);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("a", "test_9_0");
    expectedRenameMap.put("b", "test_9_1");

    VariableMap variableMap = renameVars2.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsReuseNamesWithCollision() {
    Node externs = NodeBuilder.newNode("var a = 1, a = 2, b = 3", Token.VAR_DECL);
    Node root = NodeBuilder.newNode("function foo() { var a = 4, a = 5, b = 6; }", Token.FUNCTION);

    RenameVars renameVars1 = new RenameVars(compiler, null, false, false, false, null, RESERVED_CHARS, RESERVED_NAMES);
    renameVars1.process(externs, root);

    Node externs2 = NodeBuilder.newNode("var a = 7, a = 8, b = 9", Token.VAR_DECL);
    Node root2 = NodeBuilder.newNode("function foo2() { var a = 10, b = 11; }", Token.FUNCTION);

    RenameVars renameVars2 = new RenameVars(compiler, null, false, false, false, renameVars1.getVariableMap(), RESERVED_CHARS, RESERVED_NAMES);
    renameVars2.process(externs2, root2);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("a", "test_10_0");
    expectedRenameMap.put("b", "test_10_1");

    VariableMap variableMap = renameVars2.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenameVarsReuseNamesWithCollisionAndPrefix() {
    String prefix = "test_";

    Node externs = NodeBuilder.newNode("var a = 1, a = 2, b = 3", Token.VAR_DECL);
    Node root = NodeBuilder.newNode("function foo() { var a = 4, a = 5, b = 6; }", Token.FUNCTION);

    RenameVars renameVars1 = new RenameVars(compiler, prefix, false, false, false, null, RESERVED_CHARS, RESERVED_NAMES);
    renameVars1.process(externs, root);

    Node externs2 = NodeBuilder.newNode("var a = 7, a = 8, b = 9", Token.VAR_DECL);
    Node root2 = NodeBuilder.newNode("function foo2() { var a = 10, b = 11; }", Token.FUNCTION);

    RenameVars renameVars2 = new RenameVars(compiler, prefix, false, false, false, renameVars1.getVariableMap(), RESERVED_CHARS, RESERVED_NAMES);
    renameVars2.process(externs2, root2);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("a", "test_11_0");
    expectedRenameMap.put("b", "test_11_1");

    VariableMap variableMap = renameVars2.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  private static NodeBuilder newNode(String code, int type) {
    return new NodeBuilder(code, type);
  }

  private static NodeBuilder NodeBuilder
      (String code, int type) {
    return new NodeBuilder(code, type);
  }

  private static class NodeBuilder {
    private final String code;
    private final int type;

    private NodeBuilder(String code, int type) {
      this.code = code;
      this.type = type;
    }

    Node build() {
      return Node.newNode(code, type);
    }
  }

  private static class NoOpCompiler implements AbstractCompiler {
    @Override
    public void process(Node externs, Node root) {}

    @Override
    public void addToDebugLog(String msg) {}

    @Override
    public void reportCodeChange() {}

    @Override
    public void reportError(int line, int offset, String message, Throwable error) {
      throw new RuntimeException(message);
    }
  }
}