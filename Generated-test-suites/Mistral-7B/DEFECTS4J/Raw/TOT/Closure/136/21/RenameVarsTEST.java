package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.RenameVars.Assignment.ORDER_OF_OCCURRENCE_COMPARATOR;
import static com.google.javascript.jscomp.RenameVars.Assignment.FREQUENCY_COMPARATOR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassCallback;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.Environment;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;

public class RenameVarsTest {
  private static final String TEST_CASE_1 = "test1.js";
  private static final String TEST_CASE_2 = "test2.js";
  private static final String TEST_CASE_3 = "test3.js";

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private ErrorReporter errorReporter;
  private CompilerPass renamePass;
  private CompilerPassCallback callback;
  private List<Node> inputNodes;
  private Node rootNode;

  @Before
  public void setup() {
    options = new CompilerOptions();
    options.setWarningsAsErrors(true);
    options.setShowErrors(true);
    errorReporter = new ErrorReporter() {};

    compiler = new AbstractCompiler(errorReporter, options);
    inputNodes = Arrays.asList(
        Node.load(getClass().getResource(TEST_CASE_1).openStream()),
        Node.load(getClass().getResource(TEST_CASE_2).openStream()),
        Node.load(getClass().getResource(TEST_CASE_3).openStream())
    );
    rootNode = inputNodes.get(0);

    callback = new CompilerPassCallback() {
      private int passCount = 0;

      @Override
      public void onPassEnd(CompilerPass pass) {
        ++passCount;
      }

      @Override
      public boolean shouldAbort() {
        return passCount >= 2;
      }

      @Override
      public void process(Node externs, Node root) {
        passCount = 1;
        pass.process(externs, root);
      }
    };

    renamePass = new RenameVars(compiler, null, false, false, false, null, null, null);
  }

  @Test
  public void testRenameVars() {
    VariableMap prevUsedRenameMap = Maps.newHashMap();
    VariableMap variableMap = null;

    NodeTraversal traversal = new NodeTraversal(rootNode, compiler, errorReporter, null);
    NodeTraversal.traverse(compiler, rootNode, callback);

    // Check that the renaming pass was executed twice
    assertTrue(callback.shouldAbort());

    // Process the first time
    renamePass.process(inputNodes.get(0), rootNode);
    variableMap = renamePass.getVariableMap();
    assertNotNull(variableMap);

    // Process the second time
    renamePass.process(inputNodes.get(1), rootNode);
    VariableMap variableMap2 = renamePass.getVariableMap();
    assertNotNull(variableMap2);

    // Check that both maps are equal
    assertEquals(variableMap, variableMap2);

    // Check that the new names are unique
    Set<String> newNames = new HashSet<String>();
    for (String oldName : variableMap.getOldNames().keySet()) {
      String newName = variableMap.getNewNames().get(oldName);
      assertNotNull(newName);
      assertTrue(newNames.add(newName));
    }

    // Check that there is exactly one mapping for each old name
    ListMultimap<String, String> oldToNewMap = ArrayListMultimap.create();
    for (String oldName : variableMap.getOldNames().keySet()) {
      String newName = variableMap.getNewNames().get(oldName);
      oldToNewMap.put(oldName, newName);
    }
    assertEquals(variableMap.getOldNames().size(), oldToNewMap.size());
    for (String oldName : variableMap.getOldNames().keySet()) {
      List<String> newNamesForOldName = oldToNewMap.get(oldName);
      assertEquals(1, newNamesForOldName.size());
    }

    // Check that the old names are not present in the new names
    for (String oldName : variableMap.getOldNames().keySet()) {
      assertFalse(variableMap.getNewNames().containsKey(oldName));
    }

    // Check that the new names follow the rules
    for (String newName : variableMap.getNewNames().values()) {
      assertTrue(newName.matches("(([a-zA-Z][a-zA-Z0-9_]*)|\\$[a-zA-Z][a-zA-Z0-9_]*|L\\d+)"));
    }

    // Check that the original names can be retrieved from the new names
    for (Map.Entry<String, String> entry : variableMap.getOldNames().entrySet()) {
      String oldName = entry.getKey();
      String newName = entry.getValue();
      String retrievedOldName = renamePass.getOldName(newName);
      assertEquals(oldName, retrievedOldName);
    }

    // Check that the old names can be retrieved even if the new names have been
    // obfuscated
    Set<String> obfuscatedNames = Sets.newHashSet("$a$", "$b$", "$c$");
    for (String oldName : variableMap.getOldNames().keySet()) {
      String newName = variableMap.getNewNames().get(oldName);
      String retrievedOldName = renamePass.getOldName(newName);
      assertEquals(oldName, retrievedOldName);
      assertTrue(obfuscatedNames.contains(newName));
    }

    // Check that the renaming does not affect the code semantics
    NodeTraversal.traverse(compiler, rootNode, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.ASSIGN) {
          String oldName = t.getScope().getVar(n.getFirstChild().getString()).getName();
          String newName = variableMap.getNewNames().get(oldName);
          n.setFirstChild(n.getFirstChild().getNext());
          n.setString(newName);
        }
      }
    });

    assertEquals(inputNodes.get(0).getText(), rootNode.getText());
    assertEquals(inputNodes.get(1).getText(), rootNode.getText());
  }
}