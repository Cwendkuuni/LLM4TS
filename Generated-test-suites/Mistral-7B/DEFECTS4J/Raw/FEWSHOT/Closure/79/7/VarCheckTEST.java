package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {
  private VarCheck varCheck;
  private Node synthesizedExternsRoot;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    varCheck = new VarCheck(compiler);
    synthesizedExternsRoot = Node.nullLiteral();
  }

  @Test
  public void testUtilStripLeadingAndTrailingQuotes() {
    assertEquals("foo", varCheck.stripLeadingAndTrailingQuotes("foo"));
    assertEquals("foo", varCheck.stripLeadingAndTrailingQuotes("foo"));
    assertEquals("foo", varCheck.stripLeadingAndTrailingQuotes("foo"));
  }

  @Test
  public void testUtilStripLeadingHyphens() {
    assertEquals("f", varCheck.stripLeadingHyphens("-f"));
    assertEquals("foo", varCheck.stripLeadingHyphens("--foo"));
    assertEquals("-foo", varCheck.stripLeadingHyphens("---foo"));
  }

  @Test
  public void testVarCheckProcess_NoErrors() {
    Node externs = Node.nullLiteral();
    Node root = Node.nullLiteral();

    varCheck.process(externs, root);

    assertNull(varCheck.varsToDeclareInExterns);
    assertNull(varCheck.synthesizedExternsInput);
    assertNull(varCheck.synthesizedExternsRoot);
  }

  @Test
  public void testVarCheckProcess_UndefinedVarError() {
    String varName = "foo";

    Node externs = Node.newObject("Object");
    Node root = Node.newObject("Object").addChild(
        new Node(Token.VAR, Node.newName(Token.NAME, varName)));

    varCheck.varsToDeclareInExterns = Sets.newHashSet(varName);
    varCheck.process(externs, root);

    assertFalse(varCheck.varsToDeclareInExterns.isEmpty());
    assertSame(externs, varCheck.synthesizedExternsInput.getAstRoot(varCheck.compiler));
    assertSame(VarCheck.createSynthesizedExternVar(varName), synthesizedExternsRoot);
  }

  @Test
  public void testVarCheckProcess_UndefinedVarWarning() {
    String varName = "foo";

    Node externs = Node.newObject("Object");
    Node root = Node.newObject("Object").addChild(
        new Node(Token.VAR, Node.newName(Token.NAME, varName)));

    varCheck = new VarCheck(varCheck.compiler, true);
    varCheck.varsToDeclareInExterns = Sets.newHashSet(varName);
    varCheck.process(externs, root);

    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertNull(varCheck.synthesizedExternsInput);
    assertNull(varCheck.synthesizedExternsRoot);
  }

  @Test
  public void testVarCheckProcess_ViolatedModuleDepError() {
    String moduleAName = "A";
    String moduleBName = "B";
    String varName = "foo";

    JSModule moduleA = new JSModule(moduleAName);
    JSModule moduleB = new JSModule(moduleBName);
    Node externsA = Node.newObject("Object");
    Node rootA = Node.newObject("Object");
    Node externsB = Node.newObject("Object");
    Node rootB = Node.newObject("Object");

    Node varA = Node.newName(Token.NAME, varName);
    Node varB = Node.newName(Token.NAME, varName);

    externsA.addChild(varA);
    rootA.addChild(new Node(Token.VAR, varA));

    externsB.addChild(varB);
    rootB.addChild(new Node(Token.VAR, varB));

    Node moduleADepOnB = new Node(Token.GETPROPSYM, Node.newName(Token.NAME, "dependsOn"), Node.newArray(Node.newName(Token.NAME, moduleBName)));
    moduleA.setUserData(moduleADepOnB);

    Node moduleBDepOnA = new Node(Token.GETPROPSYM, Node.newName(Token.NAME, "dependsOn"), Node.newArray(Node.newName(Token.NAME, moduleAName)));
    moduleB.setUserData(moduleBDepOnA);

    Node moduleGraph = new Node(Token.OBJECT, Node.newObject("ModuleGraph"));
    moduleGraph.addChild(new Node(Token.GETPROPSYM, Node.newName(Token.NAME, "dependsOn"), Node.newObject(
        Node.newArray(
          Node.newObject(
            Node.newName(Token.NAME, moduleAName),
            Node.newObject(Node.newName(Token.NAME, moduleBName))),
          Node.newObject(
            Node.newName(Token.NAME, moduleBName),
            Node.newObject(Node.newName(Token.NAME, moduleAName)))))));

    varCheck.compiler.setModuleGraph(moduleGraph);

    varCheck.varsToDeclareInExterns = Sets.newHashSet(varName);
    varCheck.process(externsA, rootA);
    varCheck.process(externsB, rootB);

    Node varAInB = NodeTraversal.findNode(rootB, varB);
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertNull(varCheck.synthesizedExternsInput);
    assertNull(varCheck.synthesizedExternsRoot);
    assertNotNull(varAInB);
    assertEquals(VarCheck.VIOLATED_MODULE_DEP_ERROR, varAInB.getFirstChild().getType());
  }

  @Test
  public void testVarCheckProcess_MissingModuleDepWarning() {
    String moduleAName = "A";
    String moduleBName = "B";
    String varName = "foo";

    JSModule moduleA = new JSModule(moduleAName);
    JSModule moduleB = new JSModule(moduleBName);
    Node externsA = Node.newObject("Object");
    Node rootA = Node.newObject("Object");
    Node externsB = Node.newObject("Object");
    Node rootB = Node.newObject("Object");

    Node varA = Node.newName(Token.NAME, varName);
    Node varB = Node.newName(Token.NAME, varName);

    externsA.addChild(varA);
    rootA.addChild(new Node(Token.VAR, varA));

    externsB.addChild(varB);
    rootB.addChild(new Node(Token.VAR, varB));

    Node moduleADepOnB = new Node(Token.GETPROPSYM, Node.newName(Token.NAME, "dependsOn"), Node.newArray(Node.newName(Token.NAME, moduleBName)));
    moduleA.setUserData(moduleADepOnB);

    Node moduleBDepOnA = new Node(Token.GETPROPSYM, Node.newName(Token.NAME, "dependsOn"), Node.newArray());
    moduleB.setUserData(moduleBDepOnA);

    varCheck.varsToDeclareInExterns = Sets.newHashSet(varName);
    varCheck.process(externsA, rootA);
    varCheck.process(externsB, rootB);

    Node varAInB = NodeTraversal.findNode(rootB, varB);
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertNull(varCheck.synthesizedExternsInput);
    assertNull(varCheck.synthesizedExternsRoot);
    assertNotNull(varAInB);
    assertEquals(VarCheck.MISSING_MODULE_DEP_ERROR, varAInB.getFirstChild().getType());
  }

  @Test
  public void testVarCheckProcess_StrictModuleDepError() {
    String moduleAName = "A";
    String moduleBName = "B";
    String varName = "foo";

    JSModule moduleA = new JSModule(moduleAName);
    JSModule moduleB = new JSModule(moduleBName);
    Node externsA = Node.newObject("Object");
    Node rootA = Node.newObject("Object");
    Node externsB = Node.newObject("Object");
    Node rootB = Node.newObject("Object");

    Node varA = Node.newName(Token.NAME, varName);
    Node varB = Node.newName(Token.NAME, varName);

    externsA.addChild(varA);
    rootA.addChild(new Node(Token.VAR, varA));

    externsB.addChild(varB);
    rootB.addChild(new Node(Token.VAR, varB));

    Node moduleADepOnB = new Node(Token.GETPROPSYM, Node.newName(Token.NAME, "dependsOn"), Node.newArray(Node.newName(Token.NAME, moduleBName)));
    moduleA.setUserData(moduleADepOnB);

    Node moduleBDepOnA = new Node(Token.GETPROPSYM, Node.newName(Token.NAME, "dependsOn"), Node.newArray());
    moduleB.setUserData(moduleBDepOnA);

    varCheck = new VarCheck(varCheck.compiler, true, true);
    varCheck.varsToDeclareInExterns = Sets.newHashSet(varName);
    varCheck.process(externsA, rootA);
    varCheck.process(externsB, rootB);

    Node varAInB = NodeTraversal.findNode(rootB, varB);
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertNull(varCheck.synthesizedExternsInput);
    assertNull(varCheck.synthesizedExternsRoot);
    assertNotNull(varAInB);
    assertEquals(VarCheck.STRICT_MODULE_DEP_ERROR, varAInB.getFirstChild().getType());
  }
}