import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.common.collect.Iterators.*;

import com.google.common.base.Function;
import com.google.common.collect.LinkedHashMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.compiler.AbstractCompiler;
import com.google.javascript.rhino.compiler.CompilerInput;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;

import java.util.Iterator;
import java.util.Map;

/**
 * Test class for Scope.
 */
public class ScopeTest {

  private AbstractCompiler compiler;
  private Scope globalScope;
  private Scope localScope;

  @org.junit.Before
  public void setUp() {
    // Initialize the compiler for creating CompilerInput objects
    compiler = new AbstractCompiler();

    // Initialize the global scope
    Node rootNode = Node.newFunctionNode(FUNCTION, new Object[]{}, new Object[]{});
    globalScope = new Scope(rootNode, compiler);

    // Initialize the local scope
    Node localRootNode = Node.newBlockNode(BLOCK, new Object[]{}, new Object[]{});
    localScope = new Scope(localRootNode, globalScope, null);
  }

  /**
   * Test constructor: Scope(parent, rootNode).
   */
  @org.junit.Test
  public void testScopeConstructor() {
    Scope scope = new Scope(localScope, Node.newFunctionNode(FUNCTION, new Object[]{}, new Object[]{}));

    assertNotNull(scope);
    assertEquals(localScope.depth + 1, scope.depth);
  }

  /**
   * Test constructor: Scope(rootNode, thisType).
   */
  @org.junit.Test
  public void testScopeConstructorWithThisType() {
    ObjectType thisType = compiler.getTypeRegistry().getNativeObjectType(GLOBAL_THIS);
    Scope scope = new Scope(Node.newBlockNode(BLOCK, new Object[]{}, new Object[]{}), thisType);

    assertNotNull(scope);
    assertEquals(0, scope.depth);
  }

  /**
   * Test constructor: Scope(rootNode, thisType).
   */
  @org.junit.Test
  public void testScopeConstructorWithEmpty() {
    Scope scope = new Scope(Node.newBlockNode(BLOCK, new Object[]{}, new Object[]{}), null);

    assertNotNull(scope);
    assertEquals(0, scope.depth);
  }

  /**
   * Test method: getParent().
   */
  @org.junit.Test
  public void testGetParent() {
    assertNull(globalScope.getParent());
    assertEquals(globalScope, localScope.getParent());
  }

  /**
   * Test method: getRootNode().
   */
  @org.junit.Test
  public void testGetRootNode() {
    assertEquals(globalScope.rootNode, Node.newFunctionNode(FUNCTION, new Object[]{}, new Object[]{}).getFirstChild());
    assertEquals(localScope.rootNode, Node.newBlockNode(BLOCK, new Object[]{}, new Object[]{}).getFirstChild());

    Node childRootNode = Node.newFunctionNode(FUNCTION, new Object[]{}, new Object[]{});
    Scope childScope = new Scope(childRootNode, localScope);
    assertEquals(childScope.rootNode, childNode);
  }

  /**
   * Test method: getDepth().
   */
  @org.junit.Test
  public void testGetDepth() {
    assertEquals(0, globalScope.getDepth());
    assertEquals(1, localScope.getDepth());
  }

  /**
   * Test method: isBottom().
   */
  @org.junit.Test
  public void testIsBottom() {
    assertTrue(globalScope.isBottom());
    assertFalse(localScope.isBottom());
  }

  /**
   * Test method: declare().
   */
  @org.junit.Test
  public void testDeclare() {
    String name = "testVar";
    Node nameNode = Node.newNameNode(name);
    Var var = localScope.declare(name, nameNode, Integer.TYPE, new CompilerInput("TestInput.js"));

    assertEquals(name, var.getName());
    assertEquals(nameNode, var.getNameNode());
    assertNotNull(var.getType());
    assertEquals(Integer.TYPE, var.getType());
    assertEquals(localScope, var.getScope());
    assertTrue(localScope.getVars().contains(var));
  }

  /**
   * Test method: undeclare().
   */
  @org.junit.Test
  public void testUndeclare() {
    String name = "testVar";
    Var var = localScope.declare(name, Node.newNameNode(name), Integer.TYPE, new CompilerInput("TestInput.js"));

    localScope.undeclare(var);

    assertFalse(localScope.getVars().contains(var));
  }

  /**
   * Test method: getSlot().
   */
  @org.junit.Test
  public void testGetSlot() {
    String name = "testVar";
    Var var = localScope.declare(name, Node.newNameNode(name), Integer.TYPE, new CompilerInput("TestInput.js"));

    StaticSlot<JSType> slot = localScope.getSlot(name);
    assertEquals(var, slot);
  }

  /**
   * Test method: getOwnSlot().
   */
  @org.junit.Test
  public void testGetOwnSlot() {
    String name = "testVar";
    Var var = localScope.declare(name, Node.newNameNode(name), Integer.TYPE, new CompilerInput("TestInput.js"));

    StaticSlot<JSType> slot = localScope.getOwnSlot(name);
    assertEquals(var, slot);
  }

  /**
   * Test method: getVar().
   */
  @org.junit.Test
  public void testGetVar() {
    String name1 = "testVar1";
    String name2 = "testVar2";
    Var var1 = localScope.declare(name1, Node.newNameNode(name1), Integer.TYPE, new CompilerInput("TestInput1.js"));
    Var var2 = localScope.declare(name2, Node.newNameNode(name2), String.TYPE, new CompilerInput("TestInput2.js"));

    Var result1 = localScope.getVar(name1);
    Var result2 = localScope.getVar(name2);

    assertEquals(var1, result1);
    assertEquals(var2, result2);
  }

  /**
   * Test method: isDeclared().
   */
  @org.junit.Test
  public void testIsDeclared() {
    localScope.declare("testVar1", Node.newNameNode("testVar1"), Integer.TYPE, new CompilerInput("TestInput1.js"));
    localScope.declare("testVar2", Node.newNameNode("testVar2"), String.TYPE, new CompilerInput("TestInput2.js"));

    assertTrue(localScope.isDeclared("testVar1"));
    assertTrue(localScope.isDeclared("testVar2"));
    assertFalse(localScope.isDeclared("nonExistentVar"));
  }

  /**
   * Test method: getVars().
   */
  @org.junit.Test
  public void testGetVars() {
    String name1 = "testVar1";
    String name2 = "testVar2";
    Var var1 = localScope.declare(name1, Node.newNameNode(name1), Integer.TYPE, new CompilerInput("TestInput1.js"));
    Var var2 = localScope.declare(name2, Node.newNameNode(name2), String.TYPE, new CompilerInput("TestInput2.js"));

    Iterator<Var> vars = localScope.getVars();

    Var result1 = vars.next();
    Var result2 = vars.next();

    assertEquals(var1, result1);
    assertEquals(var2, result2);
  }

  /**
   * Test method: getVarCount().
   */
  @org.junit.Test
  public void testGetVarCount() {
    String name1 = "testVar1";
    String name2 = "testVar2";
    Var var1 = localScope.declare(name1, Node.newNameNode(name1), Integer.TYPE, new CompilerInput("TestInput1.js"));
    Var var2 = localScope.declare(name2, Node.newNameNode(name2), String.TYPE, new CompilerInput("TestInput2.js"));

    assertEquals(2, localScope.getVarCount());
  }

  /**
   * Test method: isGlobal().
   */
  @org.junit.Test
  public void testIsGlobal() {
    assertTrue(globalScope.isGlobal());
    assertFalse(localScope.isGlobal());
  }

  /**
   * Test method: isLocal().
   */
  @org.junit.Test
  public void testIsLocal() {
    assertFalse(globalScope.isLocal());
    assertTrue(localScope.isLocal());
  }
}