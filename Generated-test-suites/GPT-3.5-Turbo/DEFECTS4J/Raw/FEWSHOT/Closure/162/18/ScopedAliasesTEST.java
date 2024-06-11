package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ScopedAliasesTest {

    @Test
    public void testAliasDefinitionsInOrder() {
        // Arrange
        String expected = "goog.dom";
        ScopedAliases ali = new ScopedAliases();
        Node aliasDefinition = createAliasDefinition("dom", expected);
        ali.aliasDefinitionsInOrder.add(aliasDefinition);
        
        // Act
        ali.getAliasDefinitionsInOrder();
        
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testScopeCalls() {
        // Arrange
        ScopedAliases ali = new ScopedAliases();
        ali.scopeCalls.add(createScopeCall());
        
        // Act
        List<Node> result = ali.getScopeCalls();
        
        // Assert
        assertEquals(1, result.size());
    }

    @Test
    public void testAliasUsages_shouldReturnEmptyListWhenNoAliasUsages() {
        // Arrange
        ScopedAliases ali = new ScopedAliases();
        
        // Act
        List<AliasUsage> result = ali.getAliasUsages();
        
        // Assert
        assertEquals(0, result.size());
    }

    private Node createAliasDefinition(String alias, String qualifiedName) {
        // Create the alias definition
        Node aliasDefinition = new Node(Token.NAME, alias);
        Node initialValue = new Node();
        initialValue.setType(Token.NAME);
        initialValue.setQualifiedName(qualifiedName);
        aliasDefinition.addChildToBack(initialValue);

        return aliasDefinition;
    }

    private Node createScopeCall() {
        // Create the scope call
        Node scopeCall = new Node(Token.CALL);
        Node scopeFunction = new Node(Token.NAME, ScopedAliases.SCOPING_METHOD_NAME);
        Node anonymousFunction = createAnonymousFunction();
        scopeCall.addChildToBack(scopeFunction);
        scopeCall.addChildToBack(anonymousFunction);
        
        return scopeCall;
    }
    
    private Node createAnonymousFunction() {
        Node anonymousFunction = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME, "");
        Node parametersNode = new Node(Token.PARAM_LIST);
        anonymousFunction.addChildToBack(nameNode);
        anonymousFunction.addChildToBack(parametersNode);
        
        return anonymousFunction;
    }
}