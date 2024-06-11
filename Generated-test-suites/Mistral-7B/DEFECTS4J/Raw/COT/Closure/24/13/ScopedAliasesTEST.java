package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_NoGoogScopeCalls() {
    Node root = Node.newText("class MyClass {}\n");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testProcess_EmptyGoogScopeCall() {
    Node root = Node.newText("goog.scope(function() {})");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testProcess_InvalidGoogScopeCall() {
    Node root = Node.newText("goog.scope(function() { return 1; })");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testProcess_MultipleAliases() {
    Node root = Node.newText(
        "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  var str = '';\n" +
            "  dom.createElement(DIV).setTextContent(str);\n" +
            "  var g = goog;\n" +
            "});");

    when(preprocessorSymbolTable.getVar(eq("dom"))).thenReturn(new Var(null, null, null));
    when(preprocessorSymbolTable.getVar(eq("DIV"))).thenReturn(new Var(null, null, null));

    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    Collection<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    assertEquals(3, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(3, aliasUsages.size());

    Node domAliasDefinition = aliasDefinitionsInOrder.get(0);
    Node domAliasUsage = aliasUsages.get(0);
    assertTrue(domAliasDefinition.isVar());
    assertEquals(domAliasUsage.aliasReference, domAliasDefinition);

    Node DIVAliasDefinition = aliasDefinitionsInOrder.get(1);
    Node DIVAliasUsage = aliasUsages.get(1);
    assertTrue(DIVAliasDefinition.isVar());
    assertEquals(DIVAliasUsage.aliasReference, DIVAliasDefinition);

    Node googAliasDefinition = aliasDefinitionsInOrder.get(2);
    Node googAliasUsage = aliasUsages.get(2);
    assertTrue(googAliasDefinition.isVar());
    assertEquals(googAliasUsage.aliasReference, googAliasDefinition);

    assertEquals(root.getChildAtIndex(0), scopeCalls.get(0));
  }

  @Test
  public void testProcess_AliasRedefined() {
    Node root = Node.newText(
        "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  var dom = goog.dom;\n" +
            "});");

    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals(Token.NAME, aliasUsages.get(0).aliasReference.getType());
    assertEquals("dom", aliasUsages.get(0).aliasReference.getString());

    List<String> errors = Lists.newArrayList(scopedAliases.getDiagnostics());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", errors.get(0));
  }
}