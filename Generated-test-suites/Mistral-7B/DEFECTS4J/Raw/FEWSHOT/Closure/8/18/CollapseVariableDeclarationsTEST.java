package com.google.javascript.jscomp.test;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.NodeBuilder;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  private static Node createNode(int type, String value) {
    return new NodeBuilder().nodeType(type).value(value).build();
  }

  private static Node createVarNode(String name) {
    return new NodeBuilder().nodeType(Token.VAR).addChild(createNode(Token.IDENT, name)).build();
  }

  private static Node createAssignNode(Node lhs, Node rhs) {
    return new NodeBuilder()
        .nodeType(Token.ASSIGNMENT)
        .addChild(lhs)
        .addChild(rhs)
        .build();
  }

  private static Node createIfNode() {
    return new NodeBuilder().nodeType(Token.IF).build();
  }

  @Test
  public void testCollapsingSimpleVariables() {
    CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(null);
    Scope scope = new Scope();

    Node root = createIfNode();

    // Create the first variable
    Node var1 = createVarNode("a");
    root.addChild(var1);
    Node assign1 = createAssignNode(var1, createNode(Token.NUM, "1"));
    var1.addChild(assign1);

    // Create the second variable
    Node var2 = createVarNode("b");
    root.addChild(var2);
    Node assign2 = createAssignNode(var2, createNode(Token.NUM, "2"));
    var2.addChild(assign2);

    cvd.process(null, root, null);

    assertEquals(3, root.getChildCount());

    Node collapsedVar = root.getChild(0);
    assertEquals("a", collapsedVar.getString());
    assertEquals(3, collapsedVar.getChildCount());
    assertEquals("1", collapsedVar.getChild(1).getString());
    assertEquals("b = 2", collapsedVar.getChild(2).getString());
  }

  @Test
  public void testCollapsingVariableWithAssign() {
    CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(null);
    Scope scope = new Scope();

    Node root = createIfNode();

    // Create the first variable
    Node var1 = createVarNode("a");
    root.addChild(var1);
    Node assign1 = createAssignNode(var1, createNode(Token.NUM, "1"));
    var1.addChild(assign1);

    // Create the second variable with an assign
    Node var2 = createVarNode("b");
    root.addChild(var2);
    Node assign2 = createAssignNode(var2, createNode(Token.NUM, "2"));
    var2.addChild(assign2);
    Node assign3 = createAssignNode(var1, createNode(Token.NUM, "3"));
    var1.addChild(assign3);

    cvd.process(null, root, null);

    assertEquals(3, root.getChildCount());

    Node collapsedVar = root.getChild(0);
    assertEquals("a", collapsedVar.getString());
    assertEquals(4, collapsedVar.getChildCount());
    assertEquals("1", collapsedVar.getChild(1).getString());
    assertEquals("b = 2", collapsedVar.getChild(2).getString());
    assertEquals("a = 3", collapsedVar.getChild(3).getString());
  }

  @Test
  public void testCollapsingVariablesInsideIfStatement() {
    CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(null);
    Scope scope = new Scope();

    Node root = createIfNode();

    // Create the first variable
    Node var1 = createVarNode("a");
    root.addChild(var1);
    Node assign1 = createAssignNode(var1, createNode(Token.NUM, "1"));
    var1.addChild(assign1);

    Node ifStmt = createIfNode();
    root.addChild(ifStmt);

    // Create the second variable
    Node var2 = createVarNode("b");
    ifStmt.addChild(var2);
    Node assign2 = createAssignNode(var2, createNode(Token.NUM, "2"));
    var2.addChild(assign2);

    cvd.process(null, root, null);

    assertEquals(5, root.getChildCount());

    Node collapsedVar = root.getChild(1);
    assertEquals("if (...) {", collapsedVar.getString());

    Node collapsedVarIf = collapsedVar.getChild(0);
    assertEquals("a", collapsedVarIf.getChild(0).getString());
    assertEquals("1", collapsedVarIf.getChild(1).getString());
    assertEquals("}", collapsedVarIf.getChild(2).getString());
    assertEquals("b = 2", collapsedVar.getChild(1).getChild(0).getString());
  }

  @Test
  public void testCollapsingVariablesWithBlacklistedVar() {
    CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(null);
    Scope scope = new Scope();

    Node root = new Node(Token.PROGRAM);

    // Create the first variable
    Node var1 = createVarNode("a");
    root.addChild(var1);
    Node assign1 = createAssignNode(var1, createNode(Token.NUM, "1"));
    var1.addChild(assign1);

    // Create the second variable
    Node var2 = createVarNode("b");
    root.addChild(var2);
    Node assign2 = createAssignNode(var2, createNode(Token.NUM, "2"));
    var2.addChild(assign2);

    // Create a third variable that is blacklisted
    Node var3 = createVarNode("c");
    Node assign3 = createAssignNode(var3, createNode(Token.NUM, "3"));
    var3.addChild(assign3);
    root.addChild(var3);

    cvd.process(null, root, null);

    assertEquals(4, root.getChildCount());

    Node collapsedVar = root.getChild(0);
    assertEquals("a", collapsedVar.getString());
    assertEquals(3, collapsedVar.getChildCount());
    assertEquals("1", collapsedVar.getChild(1).getString());
    assertEquals("b = 2", collapsedVar.getChild(2).getString());

    Node collapsedVarBlacklisted = root.getChild(3);
    assertEquals("c", collapsedVarBlacklisted.getString());
    assertNull(collapsedVarBlacklisted.getChild(0));
  }

  @Test
  public void testCollapsingVariablesInsideFunction() {
    CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(null);
    Scope scope = new Scope();

    Node functionNode = new NodeBuilder().nodeType(Token.FUNCTION).build();
    Node functionBody = new NodeBuilder().nodeType(Token.BLOCK).build();
    functionNode.addChild(functionBody);

    Node root = functionNode;

    // Create the first variable
    Node var1 = createVarNode("a");
    functionBody.addChild(var1);
    Node assign1 = createAssignNode(var1, createNode(Token.NUM, "1"));
    var1.addChild(assign1);

    // Create the second variable
    Node var2 = createVarNode("b");
    functionBody.addChild(var2);
    Node assign2 = createAssignNode(var2, createNode(Token.NUM, "2"));
    var2.addChild(assign2);

    cvd.process(null, root, null);

    assertEquals(1, root.getChildCount());
    assertEquals("function () {", root.getString());

    Node collapsedVar = root.getChild();
    assertEquals(3, collapsedVar.getChildCount());
    assertEquals("var a = 1, b = 2", collapsedVar.getString());
  }
}