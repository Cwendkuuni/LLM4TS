package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

@RunWith(MockitoJUnitRunner.class)
public class RenamePrototypesTest {

  // Mocks
  private AbstractCompiler compilerMock;
  private CompilerPassManager passManagerMock;
  private CompilerInput inputMock;

  // Test objects
  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    compilerMock = Mockito.mock(AbstractCompiler.class);
    passManagerMock = Mockito.mock(CompilerPassManager.class);
    inputMock = Mockito.mock(CompilerInput.class);

    renamePrototypes = new RenamePrototypes(compilerMock, true, null, null);
    Mockito.when(compilerMock.getCodingConvention()).thenReturn(new CodingConvention());
    Mockito.when(compilerMock.getLifeCycleStage()).thenReturn(AbstractCompiler.LifeCycleStage.NORMALIZED);
    Mockito.when(passManagerMock.process(inputMock)).thenReturn(renamePrototypes);
  }

  @Test
  public void testProcess_Externals_Are_Not_Renamed() {
    String input = "function Foo() {} Foo.prototype.extern = 1;";

    Node root = new Node(Token.PROGRAM, null, null);
    Node externs = new Node(Token.BLOCK, null, null);
    root.setFirstChild(externs);

    CompilerInput compilerInput = new CompilerInput(input, "test.js", 0);
    NodeTraversal traverser = new NodeTraversal(compilerMock, null, null, null, null);

    traverser.traverse(passManagerMock, root, null);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = new HashMap<>();
    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void testProcess_PrivateProperties_Are_Renamed() {
    String input = "function Foo() { var _foo = 1; }";

    Node root = new Node(Token.PROGRAM, null, null);
    Node externs = new Node(Token.BLOCK, null, null);
    root.setFirstChild(externs);

    CompilerInput compilerInput = new CompilerInput(input, "test.js", 0);
    NodeTraversal traverser = new NodeTraversal(compilerMock, null, null, null, null);

    traverser.traverse(passManagerMock, root, null);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("_foo", "_foo_");
    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void testProcess_AggressiveRenaming_Is_Enabled() {
    String input = "function Foo() {} Foo.prototype.bar = function() {}";

    Node root = new Node(Token.PROGRAM, null, null);
    Node externs = new Node(Token.BLOCK, null, null);
    root.setFirstChild(externs);

    CompilerInput compilerInput = new CompilerInput(input, "test.js", 0);
    NodeTraversal traverser = new NodeTraversal(compilerMock, null, null, null, null);

    traverser.traverse(passManagerMock, root, null);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("bar", "bar_");
    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void testProcess_Only_Prototype_Properties_Are_Renamed() {
    String input = "function Foo() {} Foo.prototype.bar = function() {}; Foo.bar = 1;";

    Node root = new Node(Token.PROGRAM, null, null);
    Node externs = new Node(Token.BLOCK, null, null);
    root.setFirstChild(externs);

    CompilerInput compilerInput = new CompilerInput(input, "test.js", 0);
    NodeTraversal traverser = new NodeTraversal(compilerMock, null, null, null, null);

    traverser.traverse(passManagerMock, root, null);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("bar", "bar_");
    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void testProcess_Only_ObjLit_Properties_Are_Renamed() {
    String input = "var obj = { bar : 1 };";

    Node root = new Node(Token.PROGRAM, null, null);
    Node externs = new Node(Token.BLOCK, null, null);
    root.setFirstChild(externs);

    CompilerInput compilerInput = new CompilerInput(input, "test.js", 0);
    NodeTraversal traverser = new NodeTraversal(compilerMock, null, null, null, null);

    traverser.traverse(passManagerMock, root, null);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = new HashMap<>();
    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void testProcess_Prototype_And_ObjLit_Properties_Are_Renamed() {
    String input = "function Foo() {} Foo.prototype.bar = function() {}; var obj = { bar : 1 };";

    Node root = new Node(Token.PROGRAM, null, null);
    Node externs = new Node(Token.BLOCK, null, null);
    root.setFirstChild(externs);

    CompilerInput compilerInput = new CompilerInput(input, "test.js", 0);
    NodeTraversal traverser = new NodeTraversal(compilerMock, null, null, null, null);

    traverser.traverse(passManagerMock, root, null);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("bar", "bar_");
    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void testProcess_Custom_Names_Are_Renamed() {
    String input = "function Foo() {} Foo.prototype._myCustomProperty = function() {};";

    Node root = new Node(Token.PROGRAM, null, null);
    Node externs = new Node(Token.BLOCK, null, null);
    root.setFirstChild(externs);

    CompilerInput compilerInput = new CompilerInput(input, "test.js", 0);
    NodeTraversal traverser = new NodeTraversal(compilerMock, null, null, null, null);

    traverser.traverse(passManagerMock, root, null);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("_myCustomProperty", "_myCustomProperty_");
    assertEquals(expectedMap, actualMap);
  }

  @Test
  public void testProcess_EdgeCase_Prototype_And_ObjLit_Properties_With_Same_Name() {
    String input = "function Foo() {} Foo.prototype.bar = function() {}; var obj = { bar : 1 };";

    Node root = new Node(Token.PROGRAM, null, null);
    Node externs = new Node(Token.BLOCK, null, null);
    root.setFirstChild(externs);

    CompilerInput compilerInput = new CompilerInput(input, "test.js", 0);
    NodeTraversal traverser = new NodeTraversal(compilerMock, null, null, null, null);

    renamePrototypes = new RenamePrototypes(compilerMock, false, null, null);

    traverser.traverse(passManagerMock, root, null);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = new HashMap<>();
    assertEquals(expectedMap, actualMap);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_Throws_Exception_When_AggrressiveRenaming_Is_Disabled() {
    String input = "function Foo() {} Foo.prototype.bar = function() {};";

    Node root = new Node(Token.PROGRAM, null, null);
    Node externs = new Node(Token.BLOCK, null, null);
    root.setFirstChild(externs);

    CompilerInput compilerInput = new CompilerInput(input, "test.js", 0);
    NodeTraversal traverser = new NodeTraversal(compilerMock, null, null, null, null);

    renamePrototypes = new RenamePrototypes(compilerMock, false, null, null);

    traverser.traverse(passManagerMock, root, null);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_Throws_Exception_When_AggrressiveRenaming_Is_True_But_No_Renaming_Needed() {
    String input = "function Foo() {} Foo.prototype.bar = function() {}; Foo.bar = 1;";

    Node root = new Node(Token.PROGRAM, null, null);
    Node externs = new Node(Token.BLOCK, null, null);
    root.setFirstChild(externs);

    CompilerInput compilerInput = new CompilerInput(input, "test.js", 0);
    NodeTraversal traverser = new NodeTraversal(compilerMock, null, null, null, null);

    renamePrototypes = new RenamePrototypes(compilerMock, true, null, null);

    traverser.traverse(passManagerMock, root, null);
  }

  @Test
  public void testProcess_Reserved_Names_Are_Not_Renamed() {
    String input = "function Foo() {} Foo.prototype.indexOf = function() {};";

    Node root = new Node(Token.PROGRAM, null, null);
    Node externs = new Node(Token.BLOCK, null, null);
    root.setFirstChild(externs);

    CompilerInput compilerInput = new CompilerInput(input, "test.js", 0);
    NodeTraversal traverser = new NodeTraversal(compilerMock, null, null, null, null);

    renamePrototypes = new RenamePrototypes(compilerMock, true, null, null);

    traverser.traverse(passManagerMock, root, null);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = new HashMap<>();
    assertEquals(expectedMap, actualMap);
  }
}

class CodingConvention {
  public boolean isExported(String name) {
    return false;
  }

  public boolean isPrivate(String name) {
    return name.startsWith("_");
  }
}