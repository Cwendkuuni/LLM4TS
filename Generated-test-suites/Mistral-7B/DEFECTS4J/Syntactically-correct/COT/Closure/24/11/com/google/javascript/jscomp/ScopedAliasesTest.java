import static org.junit.Assert.*;

import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    // When
    Node root = new Node();
    Node externs = new Node();
    scopedAliases.process(externs, root);

    // Then
    // Basic functionality test
    assertTrue("ScopedAliases process method should not have any errors", scopedAliases.hasErrors());

    // Edge cases and exception handling test

    // Test invalid goog.scope method call
    Node invalidScopeCall = new Node(Token.CALL, null, null);
    invalidScopeCall.setFirstChild(new Node(Token.IDENT, "goog", null, null));
    invalidScopeCall.setLastChild(new Node(Token.STRING, "scope", null, null));
    Node parent = new Node(Token.ASSIGN, null, null);
    parent.setFirstChild(invalidScopeCall);
    Node child = new Node(Token.IDENT, "result", null, null);
    parent.setLastChild(child);

    Node rootWithInvalidScopeCall = new Node();
    rootWithInvalidScopeCall.appendChild(root);
    rootWithInvalidScopeCall.appendChild(externs);
    rootWithInvalidScopeCall.appendChild(parent);
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(externs, rootWithInvalidScopeCall);

    assertTrue("ScopedAliases should report an error for invalid goog.scope method call", scopedAliases.hasErrors());
    assertEquals("Expected JSC_GOOG_SCOPE_USED_IMPROPERLY error", 1, scopedAliases.getDiagnostics().size());

    // Test goog.scope method call with invalid parameter
    Node invalidScopeParameterCall = new Node(Token.CALL, null, null);
    invalidScopeParameterCall.setFirstChild(new Node(Token.IDENT, "goog", null, null));
    invalidScopeParameterCall.setLastChild(new Node(
        Token.CALL, null,
        new Node(Token.IDENT, "scope", null, null),
        new Node(Token.ARRAY_LITERAL, null, Collections.singletonList(new Node(Token.OBJECT_LITERAL, null, null)))
    ));
    Node parentWithInvalidScopeParameterCall = new Node(Token.ASSIGN, null, null);
    parentWithInvalidScopeParameterCall.setFirstChild(invalidScopeParameterCall);
    Node childWithInvalidScopeParameterCall = new Node(Token.IDENT, "result", null, null);
    parentWithInvalidScopeParameterCall.setLastChild(childWithInvalidScopeParameterCall);

    Node rootWithInvalidScopeParameterCall = new Node();
    rootWithInvalidScopeParameterCall.appendChild(root);
    rootWithInvalidScopeParameterCall.appendChild(externs);
    rootWithInvalidScopeParameterCall.appendChild(parentWithInvalidScopeParameterCall);
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(externs, rootWithInvalidScopeParameterCall);

    assertTrue("ScopedAliases should report an error for invalid goog.scope method call with parameter", scopedAliases.hasErrors());
    assertEquals("Expected JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS error", 1, scopedAliases.getDiagnostics().size());

    // Test goog.scope method call with this keyword
    Node scopeCallWithThis = new Node(Token.CALL, null, null);
    scopeCallWithThis.setFirstChild(new Node(Token.IDENT, "goog", null, null));
    scopeCallWithThis.setLastChild(new Node(Token.FUNCTION, null, null, null));
    scopeCallWithThis.getLastChild().setBody(
        new Node(Token.BLOCK, null, new Node(Token.RETURN, null, new Node(Token.THIS, null, null))));
    Node parentWithScopeCallWithThis = new Node(Token.ASSIGN, null, null);
    parentWithScopeCallWithThis.setFirstChild(scopeCallWithThis);
    Node childWithScopeCallWithThis = new Node(Token.IDENT, "result", null, null);
    parentWithScopeCallWithThis.setLastChild(childWithScopeCallWithThis);

    Node rootWithScopeCallWithThis = new Node();
    rootWithScopeCallWithThis.appendChild(root);
    rootWithScopeCallWithThis.appendChild(externs);
    rootWithScopeCallWithThis.appendChild(parentWithScopeCallWithThis);
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(externs, rootWithScopeCallWithThis);

    assertTrue("ScopedAliases should report an error for goog.scope method call with this keyword", scopedAliases.hasErrors());
    assertEquals("Expected JSC_GOOG_SCOPE_REFERENCES_THIS error", 1, scopedAliases.getDiagnostics().size());

    // Test goog.scope method call with return statement
    Node scopeCallWithReturn = new Node(Token.CALL, null, null);
    scopeCallWithReturn.setFirstChild(new Node(Token.IDENT, "goog", null, null));
    scopeCallWithReturn.setLastChild(new Node(Token.FUNCTION, null, null, null));
    scopeCallWithReturn.getLastChild().setBody(new Node(Token.RETURN, null, new Node(Token.IDENT, "value", null, null)));
    Node parentWithScopeCallWithReturn = new Node(Token.ASSIGN, null, null);
    parentWithScopeCallWithReturn.setFirstChild(scopeCallWithReturn);
    Node childWithScopeCallWithReturn = new Node(Token.IDENT, "result", null, null);
    parentWithScopeCallWithReturn.setLastChild(childWithScopeCallWithReturn);

    Node rootWithScopeCallWithReturn = new Node();
    rootWithScopeCallWithReturn.appendChild(root);
    rootWithScopeCallWithReturn.appendChild(externs);
    rootWithScopeCallWithReturn.appendChild(parentWithScopeCallWithReturn);
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(externs, rootWithScopeCallWithReturn);

    assertTrue("ScopedAliases should report an error for goog.scope method call with return statement", scopedAliases.hasErrors());
    assertEquals("Expected JSC_GOOG_SCOPE_USES_RETURN error", 1, scopedAliases.getDiagnostics().size());

    // Test goog.scope method call with throw statement
    Node scopeCallWithThrow = new Node(Token.CALL, null, null);
    scopeCallWithThrow.setFirstChild(new Node(Token.IDENT, "goog", null, null));
    scopeCallWithThrow.setLastChild(new Node(Token.FUNCTION, null, null, null));
    scopeCallWithThrow.getLastChild().setBody(new Node(Token.THROW, null, new Node(Token.IDENT, "error", null, null)));
    Node parentWithScopeCallWithThrow = new Node(Token.ASSIGN, null, null);
    parentWithScopeCallWithThrow.setFirstChild(scopeCallWithThrow);
    Node childWithScopeCallWithThrow = new Node(Token.IDENT, "result", null, null);
    parentWithScopeCallWithThrow.setLastChild(childWithScopeCallWithThrow);

    Node rootWithScopeCallWithThrow = new Node();
    rootWithScopeCallWithThrow.appendChild(root);
    rootWithScopeCallWithThrow.appendChild(externs);
    rootWithScopeCallWithThrow.appendChild(parentWithScopeCallWithThrow);
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(externs, rootWithScopeCallWithThrow);

    assertTrue("ScopedAliases should report an error for goog.scope method call with throw statement", scopedAliases.hasErrors());
    assertEquals("Expected JSC_GOOG_SCOPE_USES_THROW error", 1, scopedAliases.getDiagnostics().size());

    // Test alias redefinition
    Node aliasDefinition = new Node(Token.VAR, null, null, null);
    aliasDefinition.setName("alias");
    aliasDefinition.setInitialValue(new Node(Token.IDENT, "value", null, null));
    Node parentWithAliasDefinition = new Node(Token.BLOCK, null, null);
    parentWithAliasDefinition.appendChild(aliasDefinition);

    Node rootWithAliasDefinition = new Node();
    Node externsWithAliasDefinition = new Node();
    rootWithAliasDefinition.appendChild(root);
    rootWithAliasDefinition.appendChild(externsWithAliasDefinition);
    rootWithAliasDefinition.appendChild(parentWithAliasDefinition);
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(externsWithAliasDefinition, rootWithAliasDefinition);

    Node aliasDefinitionInScope = parentWithAliasDefinition.getFirstChild();
    Var aliasVar = scopedAliases.getScope().getVar("alias");
    Var aliasVarInScope = aliasVar.getScope().getVar("alias");
    Node aliasedNode = aliasVarInScope.getNode();

    Node aliasUsage = new Node(Token.IDENT, "alias", null, null);
    Node parentWithAliasUsage = new Node(Token.ASSIGN, null, null);
    parentWithAliasUsage.setFirstChild(aliasUsage);
    Node childWithAliasUsage = new Node(Token.IDENT, "result", null, null);
    parentWithAliasUsage.setLastChild(childWithAliasUsage);

    Node rootWithAliasUsage = new Node();
    Node externsWithAliasUsage = new Node();
    rootWithAliasUsage.appendChild(root);
    rootWithAliasUsage.appendChild(externsWithAliasUsage);
    rootWithAliasUsage.appendChild(parentWithAliasUsage);
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(externsWithAliasUsage, rootWithAliasUsage);

    Node aliasUsageInScope = parentWithAliasUsage.getFirstChild();
    assertTrue("Expected alias usage to be replaced with aliased node",
        aliasUsageInScope.getNodeValue().equals(aliasedNode.getNodeValue()));

    assertTrue("ScopedAliases should not report any errors for alias redefinition", scopedAliases.hasErrors());

    // Test alias redefinition with different aliases
    aliasDefinition.setName("anotherAlias");
    Node rootWithAliasDefinitionAndAnotherAlias = new Node();
    Node externsWithAliasDefinitionAndAnotherAlias = new Node();
    rootWithAliasDefinitionAndAnotherAlias.appendChild(root);
    rootWithAliasDefinitionAndAnotherAlias.appendChild(externsWithAliasDefinitionAndAnotherAlias);
    rootWithAliasDefinitionAndAnotherAlias.appendChild(parentWithAliasDefinition);
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(externsWithAliasDefinitionAndAnotherAlias, rootWithAliasDefinitionAndAnotherAlias);

    Var anotherAliasVarInScope = aliasVar.getScope().getVar("anotherAlias");
    Node anotherAliasedNode = anotherAliasVarInScope.getNode();
    aliasUsage = new Node(Token.IDENT, "anotherAlias", null, null);
    Node parentWithAnotherAliasUsage = new Node(Token.ASSIGN, null, null);
    parentWithAnotherAliasUsage.setFirstChild(aliasUsage);
    childWithAliasUsage = new Node(Token.IDENT, "result", null, null);
    parentWithAnotherAliasUsage.setLastChild(childWithAliasUsage);

    Node rootWithAnotherAliasUsage = new Node();
    Node externsWithAnotherAliasUsage = new Node();
    rootWithAnotherAliasUsage.appendChild(root);
    rootWithAnotherAliasUsage.appendChild(externsWithAnotherAliasUsage);
    rootWithAnotherAliasUsage.appendChild(parentWithAnotherAliasUsage);
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(externsWithAnotherAliasUsage, rootWithAnotherAliasUsage);

    assertTrue("Expected alias usage to be replaced with another aliased node",
        aliasUsageInScope.getNodeValue().equals(anotherAliasedNode.getNodeValue()));

    assertTrue("ScopedAliases should report an error for alias redefinition with different aliases", scopedAliases.hasErrors());
    assertEquals("Expected JSC_GOOG_SCOPE_ALIAS_REDEFINED error", 1, scopedAliases.getDiagnostics().size());

    // Test alias redefinition in the same scope
    aliasDefinition.setName("alias");
    Node rootWithAliasDefinitionAndSameAlias = new Node();
    Node externsWithAliasDefinitionAndSameAlias = new Node();
    rootWithAliasDefinitionAndSameAlias.appendChild(root);
    rootWithAliasDefinitionAndSameAlias.appendChild(externsWithAliasDefinitionAndSameAlias);
    rootWithAliasDefinitionAndSameAlias.appendChild(parentWithAliasDefinition);
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(externsWithAliasDefinitionAndSameAlias, rootWithAliasDefinitionAndSameAlias);

    assertTrue("ScopedAliases should report an error for alias redefinition in the same scope", scopedAliases.hasErrors());
    assertEquals("Expected JSC_GOOG_SCOPE_ALIAS_REDEFINED error", 1, scopedAliases.getDiagnostics().size());

    // Test alias assignment to a non-alias local variable
    aliasDefinition = new Node(Token.VAR, null, null, null);
    aliasDefinition.setName("nonAliasLocalVariable");
    Node parentWithAliasDefinitionToNonAliasLocalVariable = new Node(Token.BLOCK, null, null);
    parentWithAliasDefinitionToNonAliasLocalVariable.appendChild(aliasDefinition);

    rootWithAliasDefinitionToNonAliasLocalVariable = new Node();
    externsWithAliasDefinitionToNonAliasLocalVariable = new Node();
    rootWithAliasDefinitionToNonAliasLocalVariable.appendChild(root);
    rootWithAliasDefinitionToNonAliasLocalVariable.appendChild(externsWithAliasDefinitionToNonAliasLocalVariable);
    rootWithAliasDefinitionToNonAliasLocalVariable.appendChild(parentWithAliasDefinitionToNonAliasLocalVariable);
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(externsWithAliasDefinitionToNonAliasLocalVariable, rootWithAliasDefinitionToNonAliasLocalVariable);

    assertTrue("ScopedAliases should report an error for alias assignment to a non-alias local variable", scopedAliases.hasErrors());
    assertEquals("Expected JSC_GOOG_SCOPE_NON_ALIAS_LOCAL error", 1, scopedAliases.getDiagnostics().size());
  }
}