package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    String sourceCode = "package test;\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});\n";

    Node root = AbstractCompiler.parseTree(sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.SIMPLE);
    options.setAliasTransformationHandler(new TestAliasTransformationHandler());

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setOptions(options);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // Check that the alias definitions have been applied and the originals have been removed.
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    Node domDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", domDefinition.getQualifiedName());
    Node tagNameDefinition = aliasDefinitionsInOrder.get(1);
    assertEquals("goog.dom.TagName", tagNameDefinition.getQualifiedName());

    Node domReference = root.findChildWithName("dom");
    assertNull(domReference);

    // Check that the alias usage has been replaced with the alias definition.
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      AliasedNode aliasNode = (AliasedNode) aliasUsage;
      Node aliasDefinition = aliasNode.getAliasDefinition();
      Node aliasReference = aliasNode.getAliasReference();
      assertEquals("dom", aliasReference.getString());
      assertEquals("goog.dom", aliasDefinition.getQualifiedName());
    }

    // Check that the scoped function has been removed.
    Node scopeCall = root.findChildWithName("goog.scope");
    assertNull(scopeCall);
  }

  private static class TestAliasTransformationHandler implements AliasTransformationHandler {
    @Override
    public AliasTransformation logAliasTransformation(String filePath,
        SourcePosition position) {
      return new AliasTransformation("test", null);
    }
  }
}