package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class RenamePrototypesTest {

  private final CompilerInput input;
  private final Node root;
  private final RenamePrototypes renamePrototypes;
  private final boolean aggressiveRenaming;

  public RenamePrototypesTest(boolean aggressiveRenaming, String inputText) {
    this.aggressiveRenaming = aggressiveRenaming;
    this.input = new CompilerInput(inputText);
    this.root = TokenStream.parseMinifiedJavaScript(this.input).getRootNode();
    this.renamePrototypes = new RenamePrototypes(null, aggressiveRenaming, null, null);
  }

  @Parameterized.Parameters
  public static List<Object[]> data() {
    return Arrays.asList(new Object[][]{
        new Object[]{false, "foo.prototype.bar = function(){}"},
        new Object[]{false, "foo.prototype.getBar = function(){}"},
        new Object[]{false, "foo.prototype = { getBar : function(){} }"},
        new Object[]{false, "foo.prototype.prototype.getBar = function(){}"},
        new Object[]{false, "foo.prototype.prototype = { getBar : function(){} }"},
        new Object[]{false, "var o = { getBar : function(){} }"},
        new Object[]{false, "var o = { getBar : function(){} }; foo.prototype = o"},
        new Object[]{false, "var o = { getBar : function(){} }; foo.prototype.getBar = o.getBar"},
        new Object[]{true, "foo.prototype.bar = function(){}"},
        new Object[]{true, "foo.prototype.getBar = function(){}"},
        new Object[]{true, "foo.prototype = { getBar : function(){} }"},
        new Object[]{true, "foo.prototype.prototype.getBar = function(){}"},
        new Object[]{true, "foo.prototype.prototype = { getBar : function(){} }"},
        new Object[]{true, "var o = { getBar : function(){} }"},
        new Object[]{true, "var o = { getBar : function(){} }; foo.prototype = o"},
        new Object[]{true, "var o = { getBar : function(){} }; foo.prototype.getBar = o.getBar"},
    });
  }

  @Test
  public void testProcess() {
    renamePrototypes.process(this.input.getExterns(), this.root);
    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    assertEquals(4, propertyMap.size());

    String oldName = "";
    String newName = "";

    for (Map.Entry<String, String> entry : propertyMap.getVariables().entrySet()) {
      oldName = entry.getKey();
      newName = entry.getValue();

      if (oldName.charAt(0) == '_') {
        continue;
      }

      Node node = this.root.findFirstChildOfType(Token.GETPROP, Token.GETELEM);
      while (node != null) {
        String name = node.getString();
        if (name.equals(oldName)) {
          assertEquals(newName, node.getFirstChild().getString());
        }
        node = node.getNextSiblingOfType(Token.GETPROP, Token.GETELEM);
      }

      node = this.root.findFirstChildOfType(Token.OBJECTLIT);
      while (node != null) {
        for (Node child = node.getFirstChild();
             child != null; child = child.getNext()) {
          if (child.getType() == Token.NUMBER) {
            continue;
          }

          String name2 = child.getString();
          if (name2.equals(oldName)) {
            assertEquals(newName, child.getString());
          }
        }

        node = node.getNextSibling();
      }

    }
  }
}