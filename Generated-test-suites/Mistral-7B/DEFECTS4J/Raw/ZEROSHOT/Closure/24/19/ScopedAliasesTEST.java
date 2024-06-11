package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.GOOG_SCOPE;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler.logAliasTransformation;
import static com.google.javascript.jscomp.CompilerOptions.TestCompilerOptions.defaultOptions;
import static com.google.javascript.jscomp.NodeUtil.isLValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp. HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Test for {@link ScopedAliases} class
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() throws Exception {
    CompilerOptions options = defaultOptions();
    options.setAliasTransformation(GOOG_SCOPE, new LoggingAliasTransformationHandler());
    AbstractCompiler compiler = new TestCompiler(options);

    String sourceCode = "package test;\n" +
        "\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "\n" +
        "  dom.createElement(DIV);\n" +
        "});\n";

    Node root = new Node(Token.PROGRAM, sourceCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, logAliasTransformation);
    scopedAliases.process(null, root);

    Node result = compiler.getOutput();
    assertNotNull(result);
    assertEquals("package test;\n" +
        "\n" +
        "goog.dom.createElement(goog.dom.TagName.DIV);\n", result.toSource());

    Collection<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.iterator().next();
    assertEquals("DomConstant.TagName.DIV", aliasDefinition.toSource());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.toSource());

    Scope scope = compiler.getCurrentScope();
    assertNotNull(scope);
    List<Var> variables = Lists.newArrayList(scope.getVarIterable());
    Var domVar = null;
    for (Var var : variables) {
      if ("dom".equals(var.getName())) {
        domVar = var;
      }
    }
    assertNotNull(domVar);
    assertEquals(aliasDefinition, domVar.getNode());

    JSDocInfo docInfo = result.getFirstChild().getFirstChild().getJSDocInfo();
    assertNotNull(docInfo);
    List<Node> typeNodes = docInfo.getTypeNodes();
    assertEquals(1, typeNodes.size());
    Node typeNode = typeNodes.get(0);
    assertEquals("DomConstant.TagName", typeNode.toSource());
  }

  private static class LoggingAliasTransformationHandler
          implements AliasTransformationHandler {

    private int currentAliasId = 0;

    @Override
    public AliasTransformation logAliasTransformation(
        String sourceFileName, SourcePosition sourcePosition) {
      return new AliasTransformation() {
        @Override
        public String getAliasName() {
          String name = "alias" + currentAliasId++;
          return name;
        }

        @Override
        public String getAliasValue() {
          return "goog." + getQualifiedName();
        }

        @Override
        public String getQualifiedName() {
          throw new UnsupportedOperationException();
        }
      };
    }
  }
}