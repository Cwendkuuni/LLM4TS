import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    

    // Test case for valid usage of goog.scope
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = Mockito.mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Mockito.mock(Node.class);

    scopedAliases.process(null, root);

    // No errors reported
    Mockito.verifyNoMoreInteractions(compiler);

    

    // Test case for improper usage of goog.scope
    Node rootWithImproperUsage = Mockito.mock(Node.class);
    Node improperUsage = Mockito.mock(Node.class);

    Node parentOfImproperUsage = Mockito.mock(Node.class);
    parentOfImproperUsage.setType(Token.SCRIPT);
    parentOfImproperUsage.addChild(improperUsage);
    rootWithImproperUsage.setParent(parentOfImproperUsage);
    rootWithImproperUsage.setType(Token.SCRIPT);

    scopedAliases.process(null, rootWithImproperUsage);

    // GOOG_SCOPE_USED_IMPROPERLY error reported
    Mockito.verify(compiler).report(Mockito.any());

    

    // Test case for goog.scope with bad parameters
    Node rootWithBadParameters = Mockito.mock(Node.class);
    Node badParameters = Mockito.mock(Node.class);

    Node parentOfBadParameters = Mockito.mock(Node.class);
    parentOfBadParameters.setType(Token.SCRIPT);
    parentOfBadParameters.addChild(new Node(Token.CALL, null,
        new Node(Token.NAME, null, "goog.scope"), badParameters));
    rootWithBadParameters.setParent(parentOfBadParameters);
    rootWithBadParameters.setType(Token.SCRIPT);

    scopedAliases.process(null, rootWithBadParameters);

    // GOOG_SCOPE_HAS_BAD_PARAMETERS error reported
    Mockito.verify(compiler).report(Mockito.any());

    

    // Test case for goog.scope referencing 'this'
    Node rootWithThis = Mockito.mock(Node.class);
    Node thisReference = Mockito.mock(Node.class);
    thisReference.setType(Token.THIS);

    Node parentOfThis = Mockito.mock(Node.class);
    parentOfThis.setType(Token.FUNCTION);
    parentOfThis.addChild(new Node(Token.CALL, null,
        new Node(Token.NAME, null, "goog.scope"), thisReference));
    rootWithThis.setParent(parentOfThis);
    rootWithThis.setType(Token.SCRIPT);

    scopedAliases.process(null, rootWithThis);

    // GOOG_SCOPE_REFERENCES_THIS error reported
    Mockito.verify(compiler).report(Mockito.any());

    

    // Test case for goog.scope using return
    Node rootWithReturn = Mockito.mock(Node.class);
    Node returnStatement = Mockito.mock(Node.class);
    returnStatement.setType(Token.RETURN);

    Node parentOfReturn = Mockito.mock(Node.class);
    parentOfReturn.setType(Token.FUNCTION);
    parentOfReturn.addChild(new Node(Token.CALL, null,
        new Node(Token.NAME, null, "goog.scope"), null));
    parentOfReturn.addChild(returnStatement);
    rootWithReturn.setParent(parentOfReturn);
    rootWithReturn.setType(Token.SCRIPT);

    scopedAliases.process(null, rootWithReturn);

    // GOOG_SCOPE_USES_RETURN error reported
    Mockito.verify(compiler).report(Mockito.any());

    

    // Test case for goog.scope using throw
    Node rootWithThrow = Mockito.mock(Node.class);
    Node throwStatement = Mockito.mock(Node.class);
    throwStatement.setType(Token.THROW);

    Node parentOfThrow = Mockito.mock(Node.class);
    parentOfThrow.setType(Token.FUNCTION);
    parentOfThrow.addChild(new Node(Token.CALL, null,
        new Node(Token.NAME, null, "goog.scope"), null));
    parentOfThrow.addChild(throwStatement);
    rootWithThrow.setParent(parentOfThrow);
    rootWithThrow.setType(Token.SCRIPT);

    scopedAliases.process(null, rootWithThrow);

    // GOOG_SCOPE_USES_THROW error reported
    Mockito.verify(compiler).report(Mockito.any());

    

    // Test case for alias redefinition
    AbstractCompiler compilerWithErrors = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTableWithErrors =
        Mockito.mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandlerWithErrors =
        Mockito.mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliasesWithErrors =
        new ScopedAliases(compilerWithErrors, preprocessorSymbolTableWithErrors,
            transformationHandlerWithErrors);

    Node rootWithAliasRedefinition = Mockito.mock(Node.class);
    Node aliasReference = Mockito.mock(Node.class);
    Node aliasDefinition = Mockito.mock(Node.class);

    Node parentOfAliasReference = Mockito.mock(Node.class);
    parentOfAliasReference.setType(Token.VAR);
    parentOfAliasReference.addChild(aliasReference);
    parentOfAliasReference.addChild(aliasDefinition);
    rootWithAliasRedefinition.setParent(parentOfAliasReference);
    rootWithAliasRedefinition.setType(Token.SCRIPT);

    // First alias definition
    scopedAliasesWithErrors.process(null, rootWithAliasRedefinition);

    // GOOG_SCOPE_ALIAS_REDEFINED error reported
    Mockito.verify(compilerWithErrors).report(Mockito.any());

    // Second alias definition
    rootWithAliasRedefinition = Mockito.mock(Node.class);
    aliasReference = Mockito.mock(Node.class);
    aliasDefinition = Mockito.mock(Node.class);

    Node parentOfSecondAliasReference = Mockito.mock(Node.class);
    parentOfSecondAliasReference.setType(Token.VAR);
    parentOfSecondAliasReference.addChild(aliasReference);
    parentOfSecondAliasReference.addChild(aliasDefinition);
    rootWithAliasRedefinition.setParent(parentOfSecondAliasReference);
    rootWithAliasRedefinition.setType(Token.SCRIPT);

    scopedAliasesWithErrors.process(null, rootWithAliasRedefinition);

    // No errors reported
    Mockito.verifyNoMoreInteractions(compilerWithErrors);

    

    // Test case for non-alias local variable
    AbstractCompiler compilerWithNonAliasLocal = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTableWithNonAliasLocal =
        Mockito.mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandlerWithNonAliasLocal =
        Mockito.mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliasesWithNonAliasLocal =
        new ScopedAliases(compilerWithNonAliasLocal, preprocessorSymbolTableWithNonAliasLocal,
            transformationHandlerWithNonAliasLocal);

    Node rootWithNonAliasLocal = Mockito.mock(Node.class);
    Node nonAliasLocal = Mockito.mock(Node.class);
    nonAliasLocal.setType(Token.NAME);
    nonAliasLocal.setString("nonAliasLocal");

    Node parentOfNonAliasLocal = Mockito.mock(Node.class);
    parentOfNonAliasLocal.setType(Token.VAR);
    parentOfNonAliasLocal.addChild(nonAliasLocal);
    rootWithNonAliasLocal.setParent(parentOfNonAliasLocal);
    rootWithNonAliasLocal.setType(Token.SCRIPT);

    scopedAliasesWithNonAliasLocal.process(null, rootWithNonAliasLocal);

    // GOOG_SCOPE_NON_ALIAS_LOCAL error reported
    Mockito.verify(compilerWithNonAliasLocal).report(Mockito.any());

    

    // Test case for alias usage
    AbstractCompiler compilerWithAliasUsage = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTableWithAliasUsage =
        Mockito.mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandlerWithAliasUsage =
        Mockito.mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliasesWithAliasUsage =
        new ScopedAliases(compilerWithAliasUsage, preprocessorSymbolTableWithAliasUsage,
            transformationHandlerWithAliasUsage);

    Node rootWithAliasUsage = Mockito.mock(Node.class);
    Node aliasReference = Mockito.mock(Node.class);
    Node aliasDefinition = Mockito.mock(Node.class);

    Node parentOfAliasReferenceWithAliasUsage = Mockito.mock(Node.class);
    parentOfAliasReferenceWithAliasUsage.setType(Token.VAR);
    parentOfAliasReferenceWithAliasUsage.addChild(aliasReference);
    parentOfAliasReferenceWithAliasUsage.addChild(aliasDefinition);

    Node aliasNodeWithAliasUsage = Mockito.mock(Node.class);
    aliasNodeWithAliasUsage.setType(Token.NAME);
    aliasNodeWithAliasUsage.setString(aliasDefinition.getQualifiedName());

    Node parentOfAliasNodeWithAliasUsage = Mockito.mock(Node.class);
    parentOfAliasNodeWithAliasUsage.setParent(parentOfAliasReferenceWithAliasUsage);
    parentOfAliasNodeWithAliasUsage.setType(Token.ASSIGN);
    parentOfAliasNodeWithAliasUsage.addChild(aliasNodeWithAliasUsage);
    rootWithAliasUsage.setParent(parentOfAliasReferenceWithAliasUsage);
    rootWithAliasUsage.setType(Token.SCRIPT);

    scopedAliasesWithAliasUsage.process(null, rootWithAliasUsage);

    // No errors reported
    Mockito.verifyNoMoreInteractions(compilerWithAliasUsage);

    

  }
}