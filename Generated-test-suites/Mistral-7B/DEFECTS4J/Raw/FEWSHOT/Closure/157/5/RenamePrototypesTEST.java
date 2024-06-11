package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.Node;

public class RenamePrototypesTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, false, null, null);

    Node root = createTestNode();
    Node externs = createExternsNode();
    renamer.process(externs, root);

    checkResult(renamer.getPropertyMap(), root);
  }

  @Test
  public void testProcessAggressiveRenaming() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, true, null, null);

    Node root = createTestNode();
    Node externs = createExternsNode();
    renamer.process(externs, root);

    checkResult(renamer.getPropertyMap(), root);
  }

  @Test
  public void testProcessNoRenaming() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, false, null, null);

    Node root = createTestNodeWithReservedNames();
    Node externs = createExternsNode();
    renamer.process(externs, root);

    checkResult(renamer.getPropertyMap(), root);
  }

  private Node createTestNode() {
    TokenStream ts = new TokenStream(new StringBuilder());
    Node root = ts.parseProgram();
    return root;
  }

  private Node createExternsNode() {
    TokenStream ts = new TokenStream(new StringBuilder());
    Node externs = ts.parseProgram();
    externs.setProperty("EXPORTED_NAME", new Object());
    return externs;
  }

  private Node createTestNodeWithReservedNames() {
    TokenStream ts = new TokenStream(new StringBuilder());
    Node root = ts.parseProgram();
    Node prototype = ts.parseExpression("prototype");
    Node prototypeProp = ts.parseGetProperty(prototype, "indexOf");
    Node objLit = ts.parseObjectLiteral();
    Node objLitKey = ts.parseStringLiteral("indexOf");
    Node objLitValue = ts.parseStringLiteral("get 'indexOf'");
    objLit.setProperty(objLitKey, objLitValue);
    Node prototypeAssignment = ts.parseAssignment(prototype, objLit);
    root.setFirstChild(prototypeAssignment);
    return root;
  }

  private void checkResult(VariableMap expectedMap, Node root) {
    Map<String, String> actualMap = renamer.getPropertyMap().asMap();
    assertEquals(expectedMap.size(), actualMap.size());
    for (Map.Entry<String, String> entry : expectedMap.asMap().entrySet()) {
      String actual = actualMap.get(entry.getKey());
      String expected = entry.getValue();
      assertEquals(expected, actual);
    }
    checkStringNodes(root, expectedMap);
  }

  private void checkStringNodes(Node root, Map<String, String> expectedMap) {
    Set<Node> stringNodes = new HashSet<Node>();
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.STRING) {
        String stringValue = currentNode.getString();
        String renamedName = expectedMap.get(stringValue);
        if (renamedName != null) {
          assertEquals(renamedName, stringValue);
        }
        stringNodes.add(currentNode);
      }
      currentNode = currentNode.getNext();
    }
    assertEquals(expectedMap.size(), stringNodes.size());
  }
}