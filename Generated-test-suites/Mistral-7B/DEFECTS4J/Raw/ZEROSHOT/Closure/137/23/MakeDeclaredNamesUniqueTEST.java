package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.TokenStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

  private static class TestRenamer implements MakeDeclaredNamesUnique.Renamer {

    private final Map<String, Integer> nameCount;
    private final String uniqueIdPrefix;

    TestRenamer(String uniqueIdPrefix) {
      this.nameCount = ImmutableMap.of();
      this.uniqueIdPrefix = uniqueIdPrefix;
    }

    @Override
    public void addDeclaredName(String name) {
      Integer count = nameCount.get(name);
      if (count == null) {
        nameCount.put(name, 1);
      } else {
        nameCount.put(name, count + 1);
      }
    }

    @Override
    public String getReplacementName(String oldName) {
      Integer count = nameCount.get(oldName);
      if (count != null) {
        String id = Integer.toString(count);
        return oldName + "_" + id;
      }
      return oldName;
    }

    @Override
    public boolean stripConstIfReplaced() {
      return false;
    }

    @Override
    public Renamer forChildScope() {
      return new TestRenamer(uniqueIdPrefix + "_");
    }
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique() {
    TokenStream ts = new TokenStream("function test() { var a = 1; var a = 2; }", null);
    Node root = ts.parseProgram();

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = NodeTraversal.traverse(null, root, makeDeclaredNamesUnique);

    TestRenamer renamer = new TestRenamer("_test_");
    MakeDeclaredNamesUnique makeDeclaredNamesUniqueRenamed = new MakeDeclaredNamesUnique(renamer);
    NodeTraversal traverserRenamed = NodeTraversal.traverse(null, root, makeDeclaredNamesUniqueRenamed);

    traverser.traverse(Node.VISIT_ALL_CHILDREN, null, null);
    List<String> names = extractVarNames(root);
    List<String> renamedNames = extractVarNames(root);
    List<String> renamedNamesTraverser = extractVarNames(root, traverserRenamed);

    assertEquals(names, renamedNames);
    assertEquals(names, renamedNamesTraverser);

    // Test that the constants are not renamed
    List<Node> constants = extractConstNames(root);
    for (Node constant : constants) {
      assertEquals(constant.getString(), getReplacementName(constant.getString(), renamer));
      assertNull(getReplacementName(constant.getString(), makeDeclaredNamesUniqueRenamed));
    }
  }

  private static List<String> extractVarNames(Node node) {
    List<String> names = Lists.newArrayList();
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (NodeUtil.isVarDeclaration(child)) {
        names.add(child.getFirstChild().getString());
      }
      names.addAll(extractVarNames(child));
    }
    return names;
  }

  private static List<String> extractVarNames(Node node, NodeTraversal traverser) {
    List<String> names = Lists.newArrayList();
    traverser.traverse(Node.VISIT_ALL_CHILDREN, new NodeVisitor<Node>() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (NodeUtil.isVarDeclaration(n)) {
          names.add(n.getFirstChild().getString());
        }
      }
    }, null);
    return names;
  }

  private static List<Node> extractConstNames(Node node) {
    List<Node> constants = Lists.newArrayList();
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (NodeUtil.isConstDeclaration(child)) {
        constants.add(child);
      }
      constants.addAll(extractConstNames(child));
    }
    return constants;
  }

  private static String getReplacementName(String oldName, Renamer renamer) {
    return renamer.getReplacementName(oldName);
  }
}