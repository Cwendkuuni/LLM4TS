package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Symbol;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Property;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
  private Compiler compiler;
  private IdGenerator idGenerator;
  private CrossModuleMethodMotion pass;
  private JSModule moduleA;
  private JSModule moduleB;

  @Before
  public void setUp() {
    compiler = new Compiler();
    idGenerator = new IdGenerator();
    pass = new CrossModuleMethodMotion(compiler, idGenerator, false);

    moduleA = new JSModule("moduleA");
    moduleB = new JSModule("moduleB");
    compiler.addTopLevelModule(moduleA);
    compiler.addTopLevelModule(moduleB);
  }

  @Test
  public void testMoveMethod() {
    // Create a property on the prototype of moduleA.
    Node prototypeA = Node.newObject("prototype");
    Node propertyA = Node.newProperty("methodA", Node.newFunction(null, null));
    prototypeA.addChild(propertyA);

    // Add a reference to the property in moduleA.
    Node moduleARef = Node.newObject(moduleA.getName());
    moduleARef.addChild(prototypeA);

    // Add a reference to the property in moduleB.
    Node moduleBRef = Node.newObject(moduleB.getName());
    moduleBRef.addChild(prototypeA);

    // Set the deepest common module of the property to moduleA.
    NameInfo nameInfo = new NameInfo(
        Node.newIdentifier("methodA"),
        moduleARef,
        Collections.singletonList(prototypeA),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
    nameInfo.setDeepestCommonModuleRef(moduleA);

    // Pass the test data to the pass.
    pass.process(Node.NULL, Node.NULL);

    // Check that the method was moved to moduleB and stubbed in moduleA.
    List<Symbol> declarations = nameInfo.getDeclarations();
    Iterator<Symbol> iterator = declarations.iterator();
    Symbol symbol = iterator.next();
    assertSame(symbol, nameInfo.getDeepestDeclaration());
    Property property = (Property) symbol;
    Node methodB = property.getValue();
    assertTrue(methodB.isFreeCall());
    assertSame(UNSTUB_METHOD_NAME, methodB.getChild(0).getName());
    assertSame(STUB_METHOD_NAME, methodB.getChild(1).getName());

    Node methodA = nodeForName("moduleA.prototype.methodA");
    assertSame(methodB, methodA);
  }

  @Test
  public void testMoveMethodNoStubbing() {
    // Create a property on the prototype of moduleA.
    Node prototypeA = Node.newObject("prototype");
    Node propertyA = Node.newProperty("methodA", Node.newFunction(null, null));
    prototypeA.addChild(propertyA);

    // Add a reference to the property in moduleA.
    Node moduleARef = Node.newObject(moduleA.getName());
    moduleARef.addChild(prototypeA);

    // Add a reference to the property in moduleB.
    Node moduleBRef = Node.newObject(moduleB.getName());
    moduleBRef.addChild(prototypeA);

    // Set the deepest common module of the property to moduleA.
    NameInfo nameInfo = new NameInfo(
        Node.newIdentifier("methodA"),
        moduleARef,
        Collections.singletonList(prototypeA),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
    nameInfo.setDeepestCommonModuleRef(moduleA);

    // Pass the test data to the pass without stubbing.
    pass.process(Node.NULL, Node.NULL);

    // Check that the method was not moved or stubbed.
    List<Symbol> declarations = nameInfo.getDeclarations();
    Iterator<Symbol> iterator = declarations.iterator();
    Symbol symbol = iterator.next();
    Property property = (Property) symbol;
    Node methodA = property.getValue();
    assertSame(methodA, nodeForName("moduleA.prototype.methodA"));
  }

  @Test
  public void testMoveMethodCycles() {
    // Create circular references between moduleA and moduleB.
    JSModule circularA = new JSModule("circularA");
    JSModule circularB = new JSModule("circularB");
    compiler.addTopLevelModule(circularA);
    compiler.addTopLevelModule(circularB);

    Node circularARef = Node.newObject(circularA.getName());
    Node circularBRef = Node.newObject(circularB.getName());
    Node circularAPrototype = Node.newObject("prototype");
    Node circularBPrototype = Node.newObject("prototype");

    circularARef.addChild(circularAPrototype);
    circularBRef.addChild(circularBPrototype);

    Node circularAProperty = Node.newProperty("property", Node.newFunction(null, null));
    circularAPrototype.addChild(circularAProperty);

    circularBPrototype.addChild(circularAPrototype);

    circularBRef.addChild(circularARef);

    // Set the deepest common module of the property to circularA.
    NameInfo nameInfo = new NameInfo(
        Node.newIdentifier("property"),
        circularARef,
        Collections.singletonList(circularAPrototype),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
    nameInfo.setDeepestCommonModuleRef(circularA);

    // Pass the test data to the pass.
    pass.process(Node.NULL, Node.NULL);

    // Check that no method was moved or stubbed due to circular references.
    List<Symbol> declarations = nameInfo.getDeclarations();
    Iterator<Symbol> iterator = declarations.iterator();
    Symbol symbol = iterator.next();
    Property property = (Property) symbol;
    Node method = property.getValue();
    assertSame(method, nodeForName("circularA.prototype.property"));
  }

  @Test
  public void testMoveMethodNullCommonModule() {
    // Create a property on the prototype of moduleA.
    Node prototypeA = Node.newObject("prototype");
    Node propertyA = Node.newProperty("methodA", Node.newFunction(null, null));
    prototypeA.addChild(propertyA);

    // Add a reference to the property in moduleA.
    Node moduleARef = Node.newObject(moduleA.getName());
    moduleARef.addChild(prototypeA);

    // Set the deepest common module of the property to null.
    NameInfo nameInfo = new NameInfo(
        Node.newIdentifier("methodA"),
        moduleARef,
        Collections.singletonList(prototypeA),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
    nameInfo.setDeepestCommonModuleRef(null);

    // Pass the test data to the pass.
    pass.process(Node.NULL, Node.NULL);

    // Check that no method was moved or stubbed due to null deepest common module.
    List<Symbol> declarations = nameInfo.getDeclarations();
    Iterator<Symbol> iterator = declarations.iterator();
    Symbol symbol = iterator.next();
    Property property = (Property) symbol;
    Node method = property.getValue();
    assertSame(method, nodeForName("moduleA.prototype.methodA"));
  }

  @Test(expected = NullPointerException.class)
  public void testMoveMethodNullNameInfo() {
    // Pass a null NameInfo to the pass.
    pass.process(Node.NULL, Node.NULL);
  }

  @Test(expected = NullPointerException.class)
  public void testMoveMethodNullModuleGraph() {
    compiler.setModuleGraph(null);
    pass.process(Node.NULL, Node.NULL);
  }

  @Test(expected = NullPointerException.class)
  public void testMoveMethodNullCompiler() {
    compiler.setCompiler(null);
    pass.process(Node.NULL, Node.NULL);
  }

  @Test(expected = NullPointerException.class)
  public void testMoveMethodNullIdGenerator() {
    compiler.setIdGenerator(null);
    pass.process(Node.NULL, Node.NULL);
  }

  @Test(expected = NullPointerException.class)
  public void testMoveMethodNullAnalyzer() {
    AnalyzePrototypeProperties analyzer = new AnalyzePrototypeProperties(
        compiler, null, false, false);
    pass.setAnalyzer(analyzer);
    pass.process(Node.NULL, Node.NULL);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveMethodCanModifyExternsFalse() {
    // Create a pass that cannot modify externs.
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, idGenerator, false);

    // Set up a test case where a method needs to be moved across modules
    // but cannot be moved because canModifyExterns is false.
    JSModule moduleA = new JSModule("moduleA");
    JSModule moduleB = new JSModule("moduleB");
    compiler.addTopLevelModule(moduleA);
    compiler.addTopLevelModule(moduleB);

    Node moduleARef = Node.newObject(moduleA.getName());
    Node moduleBRef = Node.newObject(moduleB.getName());

    Node prototypeA = Node.newObject("prototype");
    Node propertyA = Node.newProperty("methodA", Node.newFunction(null, null));
    prototypeA.addChild(propertyA);
    moduleARef.addChild(prototypeA);

    Node prototypeB = Node.newObject("prototype");
    Node propertyB = Node.newProperty("methodA", Node.newFunction(null, null));
    prototypeB.addChild(propertyB);
    moduleBRef.addChild(prototypeB);

    NameInfo nameInfo = new NameInfo(
        Node.newIdentifier("methodA"),
        moduleARef,
        Collections.singletonList(prototypeA),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
    nameInfo.setDeepestCommonModuleRef(moduleB);

    // Pass the test data to the pass.
    pass.process(Node.NULL, Node.NULL);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveMethodCanModifyExternsTrueButExternsMissing() {
    // Create a pass that can modify externs but the externs file is missing.
    compiler.setExternsFile(null);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, idGenerator, true);

    // Set up a test case where a method needs to be moved across modules
    // but the externs file is missing and cannot be created.
    JSModule moduleA = new JSModule("moduleA");
    JSModule moduleB = new JSModule("moduleB");
    compiler.addTopLevelModule(moduleA);
    compiler.addTopLevelModule(moduleB);

    Node moduleARef = Node.newObject(moduleA.getName());
    Node moduleBRef = Node.newObject(moduleB.getName());

    Node prototypeA = Node.newObject("prototype");
    Node propertyA = Node.newProperty("methodA", Node.newFunction(null, null));
    prototypeA.addChild(propertyA);
    moduleARef.addChild(prototypeA);

    Node prototypeB = Node.newObject("prototype");
    Node propertyB = Node.newProperty("methodA", Node.newFunction(null, null));
    prototypeB.addChild(propertyB);
    moduleBRef.addChild(prototypeB);

    NameInfo nameInfo = new NameInfo(
        Node.newIdentifier("methodA"),
        moduleARef,
        Collections.singletonList(prototypeA),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
    nameInfo.setDeepestCommonModuleRef(moduleB);

    // Pass the test data to the pass.
    pass.process(Node.NULL, Node.NULL);
  }

  @Test
  public void testGenerateIds() {
    // Test that the IdGenerator generates unique ids.
    for (int i = 0; i < 10; i++) {
      int id = idGenerator.newId();
      List<Integer> ids = new LinkedList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
      ids.remove((Integer) id);
      assertTrue(ids.isEmpty());
    }
  }

  @Test
  public void testGenerateIdsMultipleThreads() {
    // Test that the IdGenerator generates unique ids when used by multiple threads.
    Map<Thread, Integer> threadIds = new HashMap<>();

    for (int i = 0; i < 10; i++) {
      Thread thread = new Thread(() -> {
        for (int j = 0; j < 10; j++) {
          int id = idGenerator.newId();
          threadIds.put(Thread.currentThread(), id);
        }
      });
      thread.start();
      try {
        thread.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    for (Integer id : threadIds.values()) {
      List<Integer> ids = new LinkedList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
      assertTrue(ids.remove(id));
    }
    assertTrue(ids.isEmpty());
  }

  private Node nodeForName(String name) {
    List<Node> nodes = compiler.getTopLevelNodes();
    for (Node node : nodes) {
      if (node.isObject() && node.getFirstChild().isIdentifier()
          && node.getFirstChild().getString().equals(name)) {
        return node;
      }
    }
    return null;
  }

  @After
  public void tearDown() {
    compiler = null;
  }
}