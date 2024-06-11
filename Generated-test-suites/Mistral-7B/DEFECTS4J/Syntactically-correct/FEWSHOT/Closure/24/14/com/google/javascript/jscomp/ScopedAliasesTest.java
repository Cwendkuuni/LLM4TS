package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Test empty case
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Node root = new Node("root");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test invalid call to goog.scope
    Node invalidCallToScope = new Node("invalidCallToScope")
        .setType(Token.CALL)
        .setProperty("callee", new Node("goog.scope").setType(Token.NAME))
        .setProperty("arguments", new Node("invalidArg").setType(Token.NUMBER));
    Node rootWithInvalidCallToScope = new Node("rootWithInvalidCallToScope")
        .setType(Token.SCRIPT)
        .addChild(invalidCallToScope);
    ScopedAliases scopedAliasesWithInvalidCallToScope = new ScopedAliases(null, null, null);
    scopedAliasesWithInvalidCallToScope.process(null, rootWithInvalidCallToScope);
    assertTrue(scopedAliasesWithInvalidCallToScope.hasErrors());

    // Test valid call to goog.scope with no aliases
    Node validCallToScope = new Node("validCallToScope")
        .setType(Token.CALL)
        .setProperty("callee", new Node("goog.scope").setType(Token.NAME))
        .setProperty("arguments", new Node("anonymousFunction")
            .setType(Token.FUNCTION)
            .setProperty("body", new Node("{}")));
    Node rootWithValidCallToScope = new Node("rootWithValidCallToScope")
        .setType(Token.SCRIPT)
        .addChild(validCallToScope);
    ScopedAliases scopedAliasesWithValidCallToScope = new ScopedAliases(null, null, null);
    scopedAliasesWithValidCallToScope.process(null, rootWithValidCallToScope);
    assertFalse(scopedAliasesWithValidCallToScope.hasErrors());

    // Test valid call to goog.scope with aliases
    Node validCallToScopeWithAliases = new Node("validCallToScopeWithAliases")
        .setType(Token.CALL)
        .setProperty("callee", new Node("goog.scope").setType(Token.NAME))
        .setProperty("arguments", new Node("anonymousFunction")
            .setType(Token.FUNCTION)
            .addChild(new Node("var aliasedVar = goog.dom"))
            .addChild(new Node("function() {"))
            .addChild(new Node("return aliasedVar.createElement"))
            .addChild(new Node("('DIV')"))
            .addChild(new Node("}"))
            .setProperty("body", new Node("{}")));
    Node rootWithValidCallToScopeWithAliases = new Node("rootWithValidCallToScopeWithAliases")
        .setType(Token.SCRIPT)
        .addChild(validCallToScopeWithAliases);
    ScopedAliases scopedAliasesWithValidCallToScopeWithAliases = new ScopedAliases(null, null, null);
    scopedAliasesWithValidCallToScopeWithAliases.process(null, rootWithValidCallToScopeWithAliases);
    assertFalse(scopedAliasesWithValidCallToScopeWithAliases.hasErrors());

    // Test valid call to goog.scope with redefined alias
    Node validCallToScopeWithRedefinedAlias = new Node("validCallToScopeWithRedefinedAlias")
        .setType(Token.CALL)
        .setProperty("callee", new Node("goog.scope").setType(Token.NAME))
        .setProperty("arguments", new Node("anonymousFunction")
            .setType(Token.FUNCTION)
            .addChild(new Node("var aliasedVar = goog.dom"))
            .addChild(new Node("function() {"))
            .addChild(new Node("var aliasedVar = goog.dom.TagName")) // redefined alias
            .addChild(new Node("return aliasedVar.DIV"))
            .addChild(new Node("}"))
            .setProperty("body", new Node("{}")))
        .setProperty("isDeclaration", true);
    Node rootWithValidCallToScopeWithRedefinedAlias = new Node("rootWithValidCallToScopeWithRedefinedAlias")
        .setType(Token.SCRIPT)
        .addChild(validCallToScopeWithRedefinedAlias);
    ScopedAliases scopedAliasesWithValidCallToScopeWithRedefinedAlias = new ScopedAliases(null, null, null);
    scopedAliasesWithValidCallToScopeWithRedefinedAlias.process(null, rootWithValidCallToScopeWithRedefinedAlias);
    assertTrue(scopedAliasesWithValidCallToScopeWithRedefinedAlias.hasErrors());

    // Test valid call to goog.scope with non-alias local variable
    Node validCallToScopeWithNonAliasLocalVariable = new Node("validCallToScopeWithNonAliasLocalVariable")
        .setType(Token.CALL)
        .setProperty("callee", new Node("goog.scope").setType(Token.NAME))
        .setProperty("arguments", new Node("anonymousFunction")
            .setType(Token.FUNCTION)
            .addChild(new Node("var nonAliasedVar = 1"))
            .addChild(new Node("function() {"))
            .addChild(new Node("return nonAliasedVar"))
            .addChild(new Node("}"))
            .setProperty("body", new Node("{}")))
        .setProperty("isDeclaration", true);
    Node rootWithValidCallToScopeWithNonAliasLocalVariable = new Node("rootWithValidCallToScopeWithNonAliasLocalVariable")
        .setType(Token.SCRIPT)
        .addChild(validCallToScopeWithNonAliasLocalVariable);
    ScopedAliases scopedAliasesWithValidCallToScopeWithNonAliasLocalVariable = new ScopedAliases(null, null, null);
    scopedAliasesWithValidCallToScopeWithNonAliasLocalVariable.process(null, rootWithValidCallToScopeWithNonAliasLocalVariable);
    assertTrue(scopedAliasesWithValidCallToScopeWithNonAliasLocalVariable.hasErrors());

    // Test valid call to goog.scope with alias redefinition and non-alias local variable
    Node validCallToScopeWithAliasRedefinitionAndNonAliasLocalVariable = new Node("validCallToScopeWithAliasRedefinitionAndNonAliasLocalVariable")
        .setType(Token.CALL)
        .setProperty("callee", new Node("goog.scope").setType(Token.NAME))
        .setProperty("arguments", new Node("anonymousFunction")
            .setType(Token.FUNCTION)
            .addChild(new Node("var aliasedVar = goog.dom"))
            .addChild(new Node("function() {"))
            .addChild(new Node("var nonAliasedVar = 1")) // non-alias local variable
            .addChild(new Node("return aliasedVar.createElement")) // alias usage
            .addChild(new Node("('DIV')"))
            .addChild(new Node("}"))
            .setProperty("body", new Node("{}")))
        .setProperty("isDeclaration", true);
    Node rootWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariable = new Node("rootWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariable")
        .setType(Token.SCRIPT)
        .addChild(validCallToScopeWithAliasRedefinitionAndNonAliasLocalVariable);
    ScopedAliases scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariable = new ScopedAliases(null, null, null);
    scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariable.process(null, rootWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariable);
    assertTrue(scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariable.hasErrors());

    // Test valid call to goog.scope with alias redefinition and alias usage
    Node validCallToScopeWithAliasRedefinitionAndAliasUsage = new Node("validCallToScopeWithAliasRedefinitionAndAliasUsage")
        .setType(Token.CALL)
        .setProperty("callee", new Node("goog.scope").setType(Token.NAME))
        .setProperty("arguments", new Node("anonymousFunction")
            .setType(Token.FUNCTION)
            .addChild(new Node("var aliasedVar = goog.dom"))
            .addChild(new Node("function() {"))
            .addChild(new Node("var aliasedVar2 = aliasedVar")) // alias redefinition
            .addChild(new Node("return aliasedVar2.createElement")) // alias usage
            .addChild(new Node("('DIV')"))
            .addChild(new Node("}"))
            .setProperty("body", new Node("{}")))
        .setProperty("isDeclaration", true);
    Node rootWithValidCallToScopeWithAliasRedefinitionAndAliasUsage = new Node("rootWithValidCallToScopeWithAliasRedefinitionAndAliasUsage")
        .setType(Token.SCRIPT)
        .addChild(validCallToScopeWithAliasRedefinitionAndAliasUsage);
    ScopedAliases scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndAliasUsage = new ScopedAliases(null, null, null);
    scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndAliasUsage.process(null, rootWithValidCallToScopeWithAliasRedefinitionAndAliasUsage);
    assertTrue(scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndAliasUsage.hasErrors());

    // Test valid call to goog.scope with alias redefinition and non-alias local variable and alias usage
    Node validCallToScopeWithAliasRedefinitionAndNonAliasLocalVariableAndAliasUsage = new Node("validCallToScopeWithAliasRedefinitionAndNonAliasLocalVariableAndAliasUsage")
        .setType(Token.CALL)
        .setProperty("callee", new Node("goog.scope").setType(Token.NAME))
        .setProperty("arguments", new Node("anonymousFunction")
            .setType(Token.FUNCTION)
            .addChild(new Node("var aliasedVar = goog.dom"))
            .addChild(new Node("function() {"))
            .addChild(new Node("var nonAliasedVar = 1")) // non-alias local variable
            .addChild(new Node("var aliasedVar2 = aliasedVar")) // alias redefinition
            .addChild(new Node("return aliasedVar2.createElement")) // alias usage
            .addChild(new Node("('DIV')"))
            .addChild(new Node("}"))
            .setProperty("body", new Node("{}")))
        .setProperty("isDeclaration", true);
    Node rootWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariableAndAliasUsage = new Node("rootWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariableAndAliasUsage")
        .setType(Token.SCRIPT)
        .addChild(validCallToScopeWithAliasRedefinitionAndNonAliasLocalVariableAndAliasUsage);
    ScopedAliases scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariableAndAliasUsage = new ScopedAliases(null, null, null);
    scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariableAndAliasUsage.process(null, rootWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariableAndAliasUsage);
    assertTrue(scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndNonAliasLocalVariableAndAliasUsage.hasErrors());

    // Test valid call to goog.scope with alias redefinition and alias usage and alias redefinition and alias usage
    Node validCallToScopeWithAliasRedefinitionAndAliasUsageAndAliasRedefinitionAndAliasUsage = new Node("validCallToScopeWithAliasRedefinitionAndAliasUsageAndAliasRedefinitionAndAliasUsage")
        .setType(Token.CALL)
        .setProperty("callee", new Node("goog.scope").setType(Token.NAME))
        .setProperty("arguments", new Node("anonymousFunction")
            .setType(Token.FUNCTION)
            .addChild(new Node("var aliasedVar = goog.dom"))
            .addChild(new Node("function() {"))
            .addChild(new Node("var aliasedVar2 = aliasedVar")) // alias redefinition
            .addChild(new Node("var aliasedVar3 = aliasedVar")) // alias redefinition
            .addChild(new Node("return aliasedVar3.createElement")) // alias usage
            .addChild(new Node("('DIV')"))
            .addChild(new Node("}"))
            .setProperty("body", new Node("{}")))
        .setProperty("isDeclaration", true);
    Node rootWithValidCallToScopeWithAliasRedefinitionAndAliasUsageAndAliasRedefinitionAndAliasUsage = new Node("rootWithValidCallToScopeWithAliasRedefinitionAndAliasUsageAndAliasRedefinitionAndAliasUsage")
        .setType(Token.SCRIPT)
        .addChild(validCallToScopeWithAliasRedefinitionAndAliasUsageAndAliasRedefinitionAndAliasUsage);
    ScopedAliases scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndAliasUsageAndAliasRedefinitionAndAliasUsage = new ScopedAliases(null, null, null);
    scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndAliasUsageAndAliasRedefinitionAndAliasUsage.process(null, rootWithValidCallToScopeWithAliasRedefinitionAndAliasUsageAndAliasRedefinitionAndAliasUsage);
    assertTrue(scopedAliasesWithValidCallToScopeWithAliasRedefinitionAndAliasUsageAndAliasRedefinitionAndAliasUsage.hasErrors());
  }
}