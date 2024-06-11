package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.testing.Libraries;
import com.google.javascript.jscomp.testing.ProcessingTest;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  private static final String SOURCE =
      "/**\n" +
          " * Copyright 2010 The Closure Compiler Authors.\n" +
          " *\n" +
          " * @preserve\n" +
          " *\n" +
          " * Process aliases in goog.scope blocks.\n" +
          " *\n" +
          " * goog.scope(function() {\n" +
          " *   var dom = goog.dom;\n" +
          " *   var DIV = dom.TagName.DIV;\n" +
          " *\n" +
          " *   dom.createElement(DIV);\n" +
          " * });\n" +
          " */\n" +
          "\n" +
          "class ScopedAliasesTest {\n" +
          "\n" +
          "  @Test\n" +
          "  public void testScopedAliases() {\n" +
          "    AbstractCompiler compiler = mock(AbstractCompiler.class);\n" +
          "    PreprocessorSymbolTable preprocessorSymbolTable = mock(" +
          "        PreprocessorSymbolTable.class);\n" +
          "    AliasTransformationHandler transformationHandler = mock(" +
          "        AliasTransformationHandler.class);\n" +
          "\n" +
          "    ScopedAliases scopedAliases = new ScopedAliases(\n" +
          "        compiler,\n" +
          "        preprocessorSymbolTable,\n" +
          "        transformationHandler);\n" +
          "\n" +
          "    Node rootNode = Libraries.loadTestFile(\n" +
          "        getClass().getResource(\"/testdata/scoped_aliases/ScopedAliases.js\")\n" +
          "            .getFileObject().getPath());\n" +
          "\n" +
          "    scopedAliases.process(null, rootNode);\n" +
          "\n" +
          "    // Check the errors were reported.\n" +
          "    ArgumentCaptor<DiagnosticPosition> errorCaptor = ArgumentCaptor.forClass(DiagnosticPosition.class);\n" +
          "    compiler.expectErrorCount(2);\n" +
          "    compiler.expectErrorMessageInFile(\n" +
          "        errorCaptor.capture(),\n" +
          "        \"JSC_GOOG_SCOPE_USED_IMPROPERLY\",\n" +
          "        \"/testdata/scoped_aliases/ScopedAliases.js\",\n" +
          "        \"goog.scope must be alone in a single statement.\");\n" +
          "    compiler.expectErrorMessageInFile(\n" +
          "        errorCaptor.capture(),\n" +
          "        \"JSC_GOOG_SCOPE_REFERENCES_THIS\",\n" +
          "        \"/testdata/scoped_aliases/ScopedAliases.js\",\n" +
          "        \"The body of a goog.scope function cannot reference 'this'.\");\n" +
          "\n" +
          "    // Check the alias definitions were extracted correctly.\n" +
          "    List<Node> aliasDefinitions = Lists.newArrayList(\n" +
          "        scopedAliases.getAliasDefinitionsInOrder().get(0),\n" +
          "        scopedAliases.getAliasDefinitionsInOrder().get(1)\n" +
          "    );\n" +
          "    List<Node> expectedAliasDefinitions = Lists.newArrayList(\n" +
          "        Node.newName(\"dom\", null, new SourcePosition(1, 0)),\n" +
          "        Node.newName(\"DIV\", null, new SourcePosition(1, 7))\n" +
          "    );\n" +
          "    assertThat(aliasDefinitions).isEqualTo(expectedAliasDefinitions);\n" +
          "\n" +
          "    // Check the alias usages were replaced correctly.\n" +
          "    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();\n" +
          "    List<Node> expectedAliasUsages = Lists.newArrayList(\n" +
          "        Node.newName(\"dom\", null, new SourcePosition(2, 11)),\n" +
          "        Node.newQualifiedName(\"goog.dom\", null, new SourcePosition(2, 11))\n" +
          "    );\n" +
          "    assertThat(aliasUsages).size().isEqualTo(expectedAliasUsages.size());\n" +
          "    for (int i = 0; i < aliasUsages.size(); i++) {\n" +
          "      Node aliasUsage = aliasUsages.get(i).getNode();\n" +
          "      Node expectedAliasUsage = expectedAliasUsages.get(i);\n" +
          "      assertThat(aliasUsage).isEqualTo(expectedAliasUsage);\n" +
          "    }\n" +
          "\n" +
          "    // Check the scope calls were removed correctly.\n" +
          "    List<Node> scopeCalls = scopedAliases.getScopeCalls();\n" +
          "    List<Node> expectedScopeCalls = Lists.newArrayList(\n" +
          "        Node.newCall(Node.newName(\"goog.scope\", null), null,\n" +
          "            new Node[]{Node.newBlockStatement(null)}),\n" +
          "        Node.newBlockStatement(null)\n" +
          "    );\n" +
          "    assertThat(scopeCalls).size().isEqualTo(expectedScopeCalls.size());\n" +
          "    for (int i = 0; i < scopeCalls.size(); i++) {\n" +
          "      Node scopeCall = scopeCalls.get(i);\n" +
          "      Node expectedScopeCall = expectedScopeCalls.get(i);\n" +
          "      assertThat(scopeCall).isEqualTo(expectedScopeCall);\n" +
          "    }\n" +
          "\n" +
          "    // Check the alias transformations were logged correctly.\n" +
          "    List<AliasTransformation> transformations =\n" +
          "        transformationHandler.getLoggedTransformations();\n" +
          "    List<AliasTransformation> expectedTransformations =\n" +
          "        Lists.newArrayList(\n" +
          "            new AliasTransformation(\n" +
          "                \"dom\",\n" +
          "                \"goog.dom\"\n" +
          "            ),\n" +
          "            new AliasTransformation(\n" +
          "                \"DIV\",\n" +
          "                \"goog.dom.TagName.DIV\"\n" +
          "            )\n" +
          "        );\n" +
          "    assertThat(transformations).size().isEqualTo(expectedTransformations.size());\n" +
          "    for (int i = 0; i < transformations.size(); i++) {\n" +
          "      AliasTransformation transformation = transformations.get(i);\n" +
          "      AliasTransformation expectedTransformation = expectedTransformations.get(i);\n" +
          "      assertThat(transformation).isEqualTo(expectedTransformation);\n" +
          "    }\n" +
          "\n" +
          "  }\n" +
          "}\n";

  @Test
  public void testScopedAliases() throws Exception {
    ProcessingTest.runTest(SOURCE);
  }

  private static interface AliasUsage {
    Node getNode();
  }

  private static class AliasedNode implements AliasUsage {
    private final Node node;

    AliasedNode(Node node) {
      this.node = node;
    }

    @Override
    public Node getNode() {
      return node;
    }
  }

  private static class AliasedTypeNode implements AliasUsage {
    private final Node node;

    AliasedTypeNode(Node node) {
      this.node = node;
    }

    @Override
    public Node getNode() {
      return node;
    }
  }
}