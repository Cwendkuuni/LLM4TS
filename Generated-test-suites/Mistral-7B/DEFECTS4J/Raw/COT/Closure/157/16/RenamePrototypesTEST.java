package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RenamePrototypesTest {

  @Mock
  private Compiler compiler;

  @Mock
  private CompilerInput input;

  @Mock
  private VariableMap prevUsedRenameMap;

  private RenamePrototypes renamePrototypes;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    renamePrototypes = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);
  }

  @Test
  public void testProcess() {
    Node root = mock(Node.class);
    Node externs = mock(Node.class);
    renamePrototypes.process(externs, root);

    verify(compiler, times(1)).addToDebugLog(ArgumentMatchers.stringThat(s -> s.contains("JS property assignments:")));
  }

  @Test
  public void testProcess_ExternedPropertiesAreNotRenamed() {
    String oldName = "prototype";
    Node externs = new Node(Token.GETPROP, null, new Node(Token.STRING, oldName));
    Node root = mock(Node.class);

    renamePrototypes.process(externs, root);

    verify(compiler, never()).addToDebugLog(ArgumentMatchers.stringContaining(oldName + " => "));
  }

  @Test
  public void testProcess_PrototypePropertiesAreRenamed() {
    String oldName = "getBar";
    String newName = "getFoo";

    Node getPropPrototype = new Node(Token.GETPROP, null, new Node(Token.STRING, oldName));
    Node prototype = new Node(Token.GETPROP, null, new Node(Token.STRING, "prototype"));
    Node prototypeGetProp = new Node(Token.GETPROP, prototype, getPropPrototype);

    Node root = mock(Node.class);

    renamePrototypes.process(null, root);

    Property getBarProperty = getProperty(oldName);
    assertNotNull(getBarProperty);
    assertEquals(newName, getBarProperty.newName);
  }

  @Test
  public void testProcess_ObjLitPropertiesAreRenamed() {
    String oldName = "getBar";
    String newName = "getFoo";

    Node objLit = new Node(Token.OBJECTLIT, null, null);
    Node key = new Node(Token.STRING, oldName);
    Node value = new Node(Token.GETPROP, null, new Node(Token.STRING, "prototype"));
    Node objLitSet = new Node(Token.SETPROP, objLit, key, value);

    Node root = mock(Node.class);

    renamePrototypes.process(null, root);

    Property getBarProperty = getProperty(oldName);
    assertNotNull(getBarProperty);
    assertEquals(newName, getBarProperty.newName);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_NullInput() {
    Node root = null;
    Node externs = mock(Node.class);

    renamePrototypes.process(externs, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_NullCompiler() {
    Node root = mock(Node.class);
    Node externs = mock(Node.class);
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, prevUsedRenameMap);

    renamePrototypes.process(externs, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_NullPrevUsedRenameMap() {
    Node root = mock(Node.class);
    Node externs = mock(Node.class);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    renamePrototypes.process(externs, root);
  }

  @Test
  public void testGetPropertyMap() {
    String oldName1 = "getBar";
    String oldName2 = "getFoo";
    String newName1 = "getRenamedBar";
    String newName2 = "getRenamedFoo";

    Property getBarProperty = new Property(oldName1);
    Property getFooProperty = new Property(oldName2);

    getBarProperty.newName = newName1;
    getFooProperty.newName = newName2;

    Map<String, Property> properties = new HashMap<>();
    properties.put(oldName1, getBarProperty);
    properties.put(oldName2, getFooProperty);

    Map<String, String> map = renamePrototypes.getPropertyMap();

    assertEquals(2, map.size());
    assertEquals(newName1, map.get(oldName1));
    assertEquals(newName2, map.get(oldName2));
  }

  private Property getProperty(String name) {
    return renamePrototypes.properties.get(name);
  }
}