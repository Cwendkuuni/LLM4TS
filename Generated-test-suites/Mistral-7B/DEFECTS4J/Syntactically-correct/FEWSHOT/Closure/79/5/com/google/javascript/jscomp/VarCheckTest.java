package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

public class VarCheckTest {

  @Test
  public void testVisit() {
    AbstractCompiler compiler = new Compiler();
    VarCheck varCheck = new VarCheck(compiler);
    Node rootNode = Node.newObject("root");
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(rootNode), varCheck);

    // Test empty var check.
    Node varNode1 = Node.newString(Token.NAME, "var1");
    Node parentNode1 = Node.newName("root");
    Node varDeclNode1 = Node.newVar(Token.VAR, varNode1, Node.newName("undefined"));
    assertNull(varCheck.varsToDeclareInExterns.contains("var1"));
    varCheck.visit(NodeTraversal.create(compiler, rootNode), varDeclNode1, parentNode1);

    // Test undefined var check.
    Node varNode2 = Node.newString(Token.NAME, "var2");
    Node parentNode2 = Node.newName("root");
    Node varDeclNode2 = Node.newVar(Token.VAR, varNode2, Node.newName("undefined"));
    assertTrue(varCheck.varsToDeclareInExterns.contains("var2"));
    varCheck.visit(NodeTraversal.create(compiler, rootNode), varDeclNode2, parentNode2);
    assertFalse(varCheck.varsToDeclareInExterns.contains("var2"));
    assertNull(varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());

    // Test var check with module dependency.
    Node moduleDeclNode = Node.newName("module1");
    Node moduleDepNode = Node.newMember(Token.GETPROP, Node.newName("module1"), Node.newName("dependsOn"));
    Node moduleDepValueNode = Node.newArray(Node.newString(Token.STRING, "module2"));
    Node moduleNode1 = Node.newObject(moduleDeclNode, moduleDepNode, moduleDepValueNode);
    Node varNode3 = Node.newString(Token.NAME, "var3");
    Node parentNode3 = Node.newName("module1");
    Node varDeclNode3 = Node.newVar(Token.VAR, varNode3, Node.newName("undefined"));
    assertTrue(varCheck.varsToDeclareInExterns.contains("var3"));
    varCheck.visit(NodeTraversal.create(compiler, moduleNode1), varDeclNode3, parentNode3);
    assertFalse(varCheck.varsToDeclareInExterns.contains("var3"));
    assertSame(moduleNode1, varCheck.getSynthesizedExternsRoot());
    Set<String> varsDeclaredInExterns = Sets.newHashSet();
    varsDeclaredInExterns.add("var3");
    varCheck.createSynthesizedExternVar("var3");
    assertTrue(varsDeclaredInExterns.containsAll(varCheck.varsToDeclareInExterns));
    varCheck.varsToDeclareInExterns.clear();
  }
}