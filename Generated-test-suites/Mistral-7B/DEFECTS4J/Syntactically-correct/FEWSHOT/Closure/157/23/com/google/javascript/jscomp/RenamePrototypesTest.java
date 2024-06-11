package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

@RunWith(PowerMockRunner.class)
public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() throws Exception {
    AbstractCompiler compiler = PowerMockito.mock(AbstractCompiler.class);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);

    CompilerInput input = new CompilerInput("test.js");
    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);

    renamePrototypes.process(externs, root);

    assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, compiler.getLifeCycleStage());

    // Test some simple cases
    Node oldName1 = new Node(Token.GETPROP, "oldName1", null);
    Node newName1 = new Node(Token.GETPROP, "newName1", null);
    oldName1.setParent(root);
    newName1.setParent(root);
    oldName1.setString("oldName1");
    newName1.setString("newName1");

    assertEquals("newName1", renamePrototypes.getPropertyMap().lookupNewName("oldName1"));
    assertEquals("oldName1", renamePrototypes.getPropertyMap().lookupOldName("newName1"));

    Node oldName2 = new Node(Token.GETPROP, "oldName2_prototype", null);
    Node newName2 = new Node(Token.GETPROP, "newName2_prototype", null);
    oldName2.setParent(root);
    newName2.setParent(root);
    oldName2.setString("oldName2_prototype");
    newName2.setString("newName2_prototype");

    assertEquals("newName2_prototype", renamePrototypes.getPropertyMap().lookupNewName("oldName2_prototype"));
    assertEquals("oldName2_prototype", renamePrototypes.getPropertyMap().lookupOldName("newName2_prototype"));

    // Test some cases with aggresive renaming enabled
    RenamePrototypes aggresiveRenamePrototypes = new RenamePrototypes(compiler, true, null, null);

    Node oldName3 = new Node(Token.GETPROP, "oldName3", null);
    Node newName3 = new Node(Token.GETPROP, "newName3", null);
    oldName3.setParent(root);
    newName3.setParent(root);
    oldName3.setString("oldName3");
    newName3.setString("newName3");

    assertEquals("newName3", aggresiveRenamePrototypes.getPropertyMap().lookupNewName("oldName3"));
    assertEquals("oldName3", aggresiveRenamePrototypes.getPropertyMap().lookupOldName("newName3"));

    Node oldName4 = new Node(Token.GETPROP, "_oldName4_prototype", null);
    Node newName4 = new Node(Token.GETPROP, "_newName4_prototype", null);
    oldName4.setParent(root);
    newName4.setParent(root);
    oldName4.setString("_oldName4_prototype");
    newName4.setString("_newName4_prototype");

    assertEquals("_newName4_prototype", aggresiveRenamePrototypes.getPropertyMap().lookupNewName("_oldName4_prototype"));
    assertEquals("_oldName4_prototype", aggresiveRenamePrototypes.getPropertyMap().lookupOldName("_newName4_prototype"));

    Node oldName5 = new Node(Token.GETPROP, "oldName5", null);
    Node newName5 = new Node(Token.GETPROP, "newName5", null);
    oldName5.setParent(root);
    newName5.setParent(root);
    oldName5.setString("oldName5");
    newName5.setString("newName5");

    // oldName5 should not be renamed as it contains only lowercase letters
    assertNull(aggresiveRenamePrototypes.getPropertyMap().lookupNewName("oldName5"));
    assertNotNull(oldName5.getString());

    // Test some cases with objLit properties
    Node objLit = new Node(Token.OBJECTLIT, null, null);
    oldName1.setParent(objLit);
    newName1.setParent(objLit);
    oldName1.setString("oldName1");
    newName1.setString("newName1");

    oldName2 = new Node(Token.GETPROP, "oldName2_objLit", null);
    newName2 = new Node(Token.GETPROP, "newName2_objLit", null);
    oldName2.setParent(objLit);
    newName2.setParent(objLit);
    oldName2.setString("oldName2_objLit");
    newName2.setString("newName2_objLit");

    assertEquals("newName1", aggresiveRenamePrototypes.getPropertyMap().lookupNewName("oldName1"));
    assertEquals("oldName1", aggresiveRenamePrototypes.getPropertyMap().lookupOldName("newName1"));

    assertEquals("newName2_objLit", aggresiveRenamePrototypes.getPropertyMap().lookupNewName("oldName2_objLit"));
    assertEquals("oldName2_objLit", aggresiveRenamePrototypes.getPropertyMap().lookupOldName("newName2_objLit"));

    Node oldName6 = new Node(Token.GETPROP, "oldName6_prototype", null);
    Node newName6 = new Node(Token.GETPROP, "newName6_prototype", null);
    oldName6.setParent(objLit);
    newName6.setParent(objLit);
    oldName6.setString("oldName6_prototype");
    newName6.setString("newName6_prototype");

    // oldName6 should not be renamed as it is an externed property
    assertNull(aggresiveRenamePrototypes.getPropertyMap().lookupNewName("oldName6_prototype"));
    assertNotNull(oldName6.getString());

    // Test some cases with prototype properties
    Node prototype = new Node(Token.GETPROP, "prototype", null);
    oldName3 = new Node(Token.GETPROP, "oldName3_prototype", null);
    newName3 = new Node(Token.GETPROP, "newName3_prototype", null);
    oldName3.setParent(prototype);
    newName3.setParent(prototype);
    oldName3.setString("oldName3_prototype");
    newName3.setString("newName3_prototype");

    assertEquals("newName3_prototype", aggresiveRenamePrototypes.getPropertyMap().lookupNewName("oldName3_prototype"));
    assertEquals("oldName3_prototype", aggresiveRenamePrototypes.getPropertyMap().lookupOldName("newName3_prototype"));

    Node prototype2 = new Node(Token.GETPROP, "prototype", null);
    oldName4 = new Node(Token.GETPROP, "_oldName4_prototype", null);
    newName4 = new Node(Token.GETPROP, "_newName4_prototype", null);
    oldName4.setParent(prototype2);
    newName4.setParent(prototype2);
    oldName4.setString("_oldName4_prototype");
    newName4.setString("_newName4_prototype");

    // _oldName4_prototype should be renamed as it is not an externed property
    assertEquals("_newName4_prototype", aggresiveRenamePrototypes.getPropertyMap().lookupNewName("_oldName4_prototype"));
    assertEquals("_oldName4_prototype", aggresiveRenamePrototypes.getPropertyMap().lookupOldName("_newName4_prototype"));

    Node oldName7 = new Node(Token.GETPROP, "oldName7_prototype", null);
    Node newName7 = new Node(Token.GETPROP, "newName7_prototype", null);
    oldName7.setParent(prototype2);
    newName7.setParent(prototype2);
    oldName7.setString("oldName7_prototype");
    newName7.setString("newName7_prototype");

    // oldName7 should not be renamed as it starts with an underscore
    assertNull(aggresiveRenamePrototypes.getPropertyMap().lookupNewName("oldName7_prototype"));
    assertNotNull(oldName7.getString());
  }
}