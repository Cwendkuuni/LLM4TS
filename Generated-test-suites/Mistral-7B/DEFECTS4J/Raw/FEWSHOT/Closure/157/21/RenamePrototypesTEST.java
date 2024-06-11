package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Test class for the RenamePrototypes class.
 */
public class RenamePrototypesTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new TestCompiler();
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);

    CompilerInput input = new CompilerInput("test.js");
    Node root = TokenStream.parseMinifiedJs(input.getText()).getRootNode();

    renamePrototypes.process(input.getExterns(), root);

    // Check that the properties were renamed.
    Map<String, String> propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("newName1", propertyMap.get("oldName1"));
    assertEquals("newName2", propertyMap.get("oldName2"));

    // Check that the string nodes were updated.
    Node oldName1Node = input.getInput().getFirstChild();
    Node newName1Node = oldName1Node.getNext();
    assertEquals("newName1", newName1Node.getString());

    Node oldName2Node = input.getInput().getNextSibling().getFirstChild();
    Node newName2Node = oldName2Node.getNext();
    assertEquals("newName2", newName2Node.getString());
  }

  @Test
  public void testProcessWithAggressiveRenaming() {
    AbstractCompiler compiler = new TestCompiler();
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = new CompilerInput("test.js");
    Node root = TokenStream.parseMinifiedJs(input.getText()).getRootNode();

    renamePrototypes.process(input.getExterns(), root);

    // Check that the properties were renamed aggressively.
    Map<String, String> propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("newName1_aggressive", propertyMap.get("oldName1"));
    assertEquals("newName2_aggressive", propertyMap.get("oldName2"));
  }

  @Test
  public void testProcessWithReservedNames() {
    AbstractCompiler compiler = new TestCompiler();
    char[] reservedCharacters = {'_'};
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, reservedCharacters, null);

    CompilerInput input = new CompilerInput("test.js");
    Node root = TokenStream.parseMinifiedJs(input.getText()).getRootNode();

    renamePrototypes.process(input.getExterns(), root);

    // Check that the reserved names were not renamed.
    Map<String, String> propertyMap = renamePrototypes.getPropertyMap();
    assertNull(propertyMap.get("indexOf"));
    assertNull(propertyMap.get("lastIndexOf"));
    assertNull(propertyMap.get("toString"));
    assertNull(propertyMap.get("valueOf"));
  }

  @Test
  public void testProcessWithPrevUsedRenameMap() {
    AbstractCompiler compiler = new TestCompiler();
    char[] reservedCharacters = {'_'};
    VariableMap prevUsedRenameMap = new VariableMap();
    prevUsedRenameMap.put("oldName1", "newName1");
    prevUsedRenameMap.put("oldName2", "newName2");

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, reservedCharacters, prevUsedRenameMap);

    CompilerInput input = new CompilerInput("test.js");
    Node root = TokenStream.parseMinifiedJs(input.getText()).getRootNode();

    renamePrototypes.process(input.getExterns(), root);

    // Check that the properties were renamed using the previous names.
    Map<String, String> propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("newName1", propertyMap.get("oldName1"));
    assertEquals("newName2", propertyMap.get("oldName2"));
  }

  private static class TestCompiler extends AbstractCompiler {
    @Override
    public LifeCycleStage getLifeCycleStage() {
      return LifeCycleStage.PARSED;
    }
  }
}