import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {

  private AbstractCompiler compiler;
  private RenamePrototypes renamePrototypes;
  private VariableMap prevUsedRenameMap;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    prevUsedRenameMap = mock(VariableMap.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);

    renamePrototypes = new RenamePrototypes(compiler, false, null, prevUsedRenameMap);
  }

  @Test
  public void testRenamePrototypeProperty() {
    CompilerInput input = getTestInput("TestPrototype.js");
    Node tree = input.getRootNode();

    renamePrototypes.process(input.getExterns(), tree);

    Node prototype = tree.getFirstChild();
    Property prototypeProp = getProperty("foo", prototype);
    assertEquals("_foo", prototypeProp.newName);
    assertEquals(1, prototypeProp.prototypeCount);
    assertEquals(0, prototypeProp.objLitCount);
    assertEquals(0, prototypeProp.refCount);

    Node objLit = tree.getLastChild();
    Property objLitProp = getProperty("foo", objLit);
    assertNull(objLitProp.newName);
    assertEquals(0, objLitProp.prototypeCount);
    assertEquals(1, objLitProp.objLitCount);
    assertEquals(0, objLitProp.refCount);
  }

  @Test
  public void testDontRenamePrototypePropertyWithLeadingUnderscore() {
    CompilerInput input = getTestInput("TestPrototypeWithUnderscore.js");
    Node tree = input.getRootNode();

    renamePrototypes.process(input.getExterns(), tree);

    Node prototype = tree.getFirstChild();
    Property prototypeProp = getProperty("_foo", prototype);
    assertNull(prototypeProp.newName);
    assertEquals(1, prototypeProp.prototypeCount);
    assertEquals(0, prototypeProp.objLitCount);
    assertEquals(0, prototypeProp.refCount);
  }

  @Test
  public void testDontRenamePrototypePropertyWithUpperCaseLetter() {
    CompilerInput input = getTestInput("TestPrototypeUpperCase.js");
    Node tree = input.getRootNode();

    renamePrototypes.process(input.getExterns(), tree);

    Node prototype = tree.getFirstChild();
    Property prototypeProp = getProperty("Foo", prototype);
    assertNull(prototypeProp.newName);
    assertEquals(1, prototypeProp.prototypeCount);
    assertEquals(0, prototypeProp.objLitCount);
    assertEquals(0, prototypeProp.refCount);
  }

  @Test
  public void testRenameObjLitProperty() {
    CompilerInput input = getTestInput("TestObjLit.js");
    Node tree = input.getRootNode();

    renamePrototypes.process(input.getExterns(), tree);

    Node objLit = tree.getLastChild();
    Property objLitProp = getProperty("foo", objLit.getLastChild());
    assertEquals("_foo", objLitProp.newName);
    assertEquals(0, objLitProp.prototypeCount);
    assertEquals(1, objLitProp.objLitCount);
    assertEquals(0, objLitProp.refCount);
  }

  @Test
  public void testDontRenameObjLitPropertyWithLeadingUnderscore() {
    CompilerInput input = getTestInput("TestObjLitWithUnderscore.js");
    Node tree = input.getRootNode();

    renamePrototypes.process(input.getExterns(), tree);

    Node objLit = tree.getLastChild();
    Property objLitProp = getProperty("_foo", objLit.getLastChild());
    assertNull(objLitProp.newName);
    assertEquals(0, objLitProp.prototypeCount);
    assertEquals(1, objLitProp.objLitCount);
    assertEquals(0, objLitProp.refCount);
  }

  @Test
  public void testDontRenameObjLitPropertyWithUpperCaseLetter() {
    CompilerInput input = getTestInput("TestObjLitUpperCase.js");
    Node tree = input.getRootNode();

    renamePrototypes.process(input.getExterns(), tree);

    Node objLit = tree.getLastChild();
    Property objLitProp = getProperty("Foo", objLit.getLastChild());
    assertNull(objLitProp.newName);
    assertEquals(0, objLitProp.prototypeCount);
    assertEquals(1, objLitProp.objLitCount);
    assertEquals(0, objLitProp.refCount);
  }

  @Test
  public void testRenamePrototypeAndObjLitProperties() {
    CompilerInput input = getTestInput("TestPrototypeAndObjLit.js");
    Node tree = input.getRootNode();

    renamePrototypes.process(input.getExterns(), tree);

    Node prototype = tree.getFirstChild();
    Property prototypeProp = getProperty("foo", prototype);
    assertEquals("_foo", prototypeProp.newName);
    assertEquals(1, prototypeProp.prototypeCount);
    assertEquals(0, prototypeProp.objLitCount);
    assertEquals(0, prototypeProp.refCount);

    Node objLit = tree.getLastChild();
    Property objLitProp = getProperty("bar", objLit.getLastChild());
    assertEquals("_bar", objLitProp.newName);
    assertEquals(0, objLitProp.prototypeCount);
    assertEquals(1, objLitProp.objLitCount);
    assertEquals(0, objLitProp.refCount);
  }

  @Test
  public void testAggressiveRenaming() {
    renamePrototypes = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);

    CompilerInput input = getTestInput("TestPrototype.js");
    Node tree = input.getRootNode();

    renamePrototypes.process(input.getExterns(), tree);

    Node prototype = tree.getFirstChild();
    Property prototypeProp = getProperty("foo", prototype);
    assertEquals("_foo_", prototypeProp.newName);
    assertEquals(1, prototypeProp.prototypeCount);
    assertEquals(0, prototypeProp.objLitCount);
    assertEquals(0, prototypeProp.refCount);
  }

  @Test
  public void testDontRenameBuiltInProperty() {
    CompilerInput input = getTestInput("TestBuiltInProperty.js");
    Node tree = input.getRootNode();

    renamePrototypes.process(input.getExterns(), tree);

    Node prototype = tree.getFirstChild();
    Property prototypeProp = getProperty("indexOf", prototype);
    assertNull(prototypeProp.newName);
    assertEquals(1, prototypeProp.prototypeCount);
    assertEquals(0, prototypeProp.objLitCount);
    assertEquals(0, prototypeProp.refCount);
  }

  @Test
  public void testDontRenamePrototypePropertyWithReservedCharacter() {
    renamePrototypes = new RenamePrototypes(compiler, false, new char[]{'?'}, prevUsedRenameMap);

    CompilerInput input = getTestInput("TestPrototypeWithReservedCharacter.js");
    Node tree = input.getRootNode();

    renamePrototypes.process(input.getExterns(), tree);

    Node prototype = tree.getFirstChild();
    Property prototypeProp = getProperty("?foo", prototype);
    assertNull(prototypeProp.newName);
    assertEquals(1, prototypeProp.prototypeCount);
    assertEquals(0, prototypeProp.objLitCount);
    assertEquals(0, prototypeProp.refCount);
  }

  @Test
  public void testRenamePrototypePropertyWithReservedCharacter() {
    renamePrototypes = new RenamePrototypes(compiler, true, new char[]{'?'}, prevUsedRenameMap);

    CompilerInput input = getTestInput("TestPrototypeWithReservedCharacter.js");
    Node tree = input.getRootNode();

    renamePrototypes.process(input.getExterns(), tree);

    Node prototype = tree.getFirstChild();
    Property prototypeProp = getProperty("?foo", prototype);
    assertEquals("?_foo", prototypeProp.newName);
    assertEquals(1, prototypeProp.prototypeCount);
    assertEquals(0, prototypeProp.objLitCount);
    assertEquals(0, prototypeProp.refCount);
  }

  private CompilerInput getTestInput(String fileName) {
    return new CompilerInput(fileName, new TokenStream(getClass()));
  }

  private Property getProperty(String name, Node node) {
    return renamePrototypes.properties.get(name);
  }
}