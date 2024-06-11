package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RenamePrototypesTest {

  private AbstractCompiler compiler;
  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public VariableMap getOrCreateVariableMap() {
        return new VariableMap();
      }

      @Override
      public VariableMap getVariableMap() {
        return getOrCreateVariableMap();
      }

      @Override
      public LifeCycleStage getLifeCycleStage() {
        return LifeCycleStage.NORMALIZED;
      }

      @Override
      public void reportCodeChange() {
        // Do nothing
      }
    };

    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
  }

  @Test
  public void testProcess() {
    CompilerInput input = new CompilerInput("test.js", "source code");
    Node root = new Node(Token.PROGRAM, null, 0);

    renamePrototypes.process(null, root);

    // No properties to rename
    assertNull(renamePrototypes.getPropertyMap());
  }

  @Test
  public void testProcessWithPropertiesToRename() {
    CompilerInput input = new CompilerInput("test.js", "source code");

    Node root = new Node(Token.PROGRAM, null, 0);
    Node prototype = new Node(Token.GETPROP, null, 0);
    prototype.setString("prototype");
    Node prototypeMemberAccess = new Node(Token.GETPROP, null, 0);
    prototypeMemberAccess.setString("getName");
    prototype.addChild(prototypeMemberAccess);
    root.addChild(prototype);

    Node objectLit = new Node(Token.OBJECTLIT, null, 0);
    Node objectLitPropertyName = new Node(Token.STRING, "getName", 0);
    Node objectLitPropertyValue = new Node(Token.FUNCTION, null, 0);
    objectLit.addChild(objectLitPropertyName);
    objectLit.addChild(objectLitPropertyValue);
    root.addChild(objectLit);

    renamePrototypes.process(null, root);

    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    assertEquals(1, propertyMap.size());

    Map.Entry<String, String> entry = propertyMap.entrySet().iterator().next();
    assertEquals("getName", entry.getKey());
    assertNull(entry.getValue());
  }

  @Test
  public void testProcessWithPropertiesToRenameAndNewNames() {
    CompilerInput input = new CompilerInput("test.js", "source code");

    Node root = new Node(Token.PROGRAM, null, 0);
    Node prototype = new Node(Token.GETPROP, null, 0);
    prototype.setString("prototype");
    Node prototypeMemberAccess = new Node(Token.GETPROP, null, 0);
    prototypeMemberAccess.setString("getName");
    prototype.addChild(prototypeMemberAccess);
    root.addChild(prototype);

    Node objectLit = new Node(Token.OBJECTLIT, null, 0);
    Node objectLitPropertyName = new Node(Token.STRING, "getName", 0);
    Node objectLitPropertyValue = new Node(Token.FUNCTION, null, 0);
    objectLit.addChild(objectLitPropertyName);
    objectLit.addChild(objectLitPropertyValue);
    root.addChild(objectLit);

    Node newObjectLit = new Node(Token.OBJECTLIT, null, 0);
    Node newObjectLitPropertyName = new Node(Token.STRING, "newName", 0);
    Node newObjectLitPropertyValue = new Node(Token.FUNCTION, null, 0);
    newObjectLit.addChild(newObjectLitPropertyName);
    newObjectLit.addChild(newObjectLitPropertyValue);
    root.addChild(newObjectLit);

    renamePrototypes.process(null, root);

    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    assertEquals(1, propertyMap.size());

    Map.Entry<String, String> entry = propertyMap.entrySet().iterator().next();
    assertEquals("getName", entry.getKey());
    assertEquals("newName", entry.getValue());
  }

  @Test
  public void testProcessWithPropertiesToRenameAndSameNames() {
    CompilerInput input = new CompilerInput("test.js", "source code");

    Node root = new Node(Token.PROGRAM, null, 0);
    Node prototype = new Node(Token.GETPROP, null, 0);
    prototype.setString("prototype");
    Node prototypeMemberAccess = new Node(Token.GETPROP, null, 0);
    prototypeMemberAccess.setString("getName");
    prototype.addChild(prototypeMemberAccess);
    root.addChild(prototype);

    Node objectLit = new Node(Token.OBJECTLIT, null, 0);
    Node objectLitPropertyName = new Node(Token.STRING, "getName", 0);
    Node objectLitPropertyValue = new Node(Token.FUNCTION, null, 0);
    objectLit.addChild(objectLitPropertyName);
    objectLit.addChild(objectLitPropertyValue);
    root.addChild(objectLit);

    Node newObjectLit = new Node(Token.OBJECTLIT, null, 0);
    Node newObjectLitPropertyName = new Node(Token.STRING, "getName", 0);
    Node newObjectLitPropertyValue = new Node(Token.FUNCTION, null, 0);
    newObjectLit.addChild(newObjectLitPropertyName);
    newObjectLit.addChild(newObjectLitPropertyValue);
    root.addChild(newObjectLit);

    renamePrototypes.process(null, root);

    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    assertEquals(1, propertyMap.size());

    Map.Entry<String, String> entry = propertyMap.entrySet().iterator().next();
    assertEquals("getName", entry.getKey());
    assertSame("getName", entry.getKey()); // Check that the key is not renamed
  }

  @Test
  public void testProcessWithPropertyNameInPrototypeAndObjectLit() {
    CompilerInput input = new CompilerInput("test.js", "source code");

    Node root = new Node(Token.PROGRAM, null, 0);
    Node prototype = new Node(Token.GETPROP, null, 0);
    prototype.setString("prototype");
    Node prototypeMemberAccess = new Node(Token.GETPROP, null, 0);
    prototypeMemberAccess.setString("getName");
    prototype.addChild(prototypeMemberAccess);
    root.addChild(prototype);

    Node objectLit = new Node(Token.OBJECTLIT, null, 0);
    Node objectLitPropertyName = new Node(Token.STRING, "getName", 0);
    Node objectLitPropertyValue = new Node(Token.FUNCTION, null, 0);
    objectLit.addChild(objectLitPropertyName);
    objectLit.addChild(objectLitPropertyValue);
    root.addChild(objectLit);

    renamePrototypes.process(null, root);

    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    assertEquals(1, propertyMap.size());

    Map.Entry<String, String> entry = propertyMap.entrySet().iterator().next();
    String oldName = entry.getKey();
    String newName = entry.getValue();

    Node prototypeAccess = new Node(Token.GETPROP, null, 0);
    prototypeAccess.setString("prototype");
    Node prototypeMemberAccess = new Node(Token.GETPROP, null, 0);
    prototypeMemberAccess.setString(oldName);
    prototypeAccess.addChild(prototypeMemberAccess);

    Node objectLitAccess = new Node(Token.GET, null, 0);
    objectLitAccess.addChild(objectLitPropertyName);
    Node objectLitAccessResult = new Node(Token.GETPROP, null, 0);
    objectLitAccessResult.setString("result");
    objectLitAccess.addChild(objectLitAccessResult);

    root.addChild(prototypeAccess);
    root.addChild(objectLitAccess);

    renamePrototypes.process(null, root);

    propertyMap = renamePrototypes.getPropertyMap();
    assertEquals(1, propertyMap.size());

    entry = propertyMap.entrySet().iterator().next();
    assertEquals(newName, entry.getValue());

    Node prototypeAccessResult = new Node(Token.GETPROP, null, 0);
    prototypeAccessResult.setString(newName);
    prototypeAccess.replaceChild(prototypeMemberAccess, prototypeAccessResult);

    Node objectLitAccessResult = new Node(Token.GETPROP, null, 0);
    objectLitAccessResult.setString(newName);
    objectLitAccess.replaceChild(objectLitPropertyName, objectLitAccessResult);

    assertEquals(newName, prototypeAccess.getString());
    assertEquals(newName, objectLitAccess.getString());
  }
}