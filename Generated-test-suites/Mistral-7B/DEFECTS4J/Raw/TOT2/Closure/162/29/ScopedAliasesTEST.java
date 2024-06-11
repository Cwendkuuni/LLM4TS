import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  private Compiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setup() {
    compiler = new Compiler();
    compiler.setOption(AliasTransformation.ENABLED, true);
    sourceFile = new SourceFile("Test.js", "test(){}");
  }

  @Test
  public void testScopedAliases() {
    // Test happy path
    compiler.process(sourceFile, null);
    Traversal traversal = compiler.getTraversal();
    assertEquals(0, traversal.getErrors().size());
    assertEquals(0, traversal.getWarnings().size());
    assertEquals(0, traversal.getAliasUsages().size());
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());

    // Test invalid goog.scope call
    sourceFile = new SourceFile("TestInvalid.js", "goog.scope(){}");
    compiler.process(sourceFile, null);
    traversal = compiler.getTraversal();
    assertEquals(1, traversal.getErrors().size());
    assertTrue(traversal.getErrors().get(0).getMessage().startsWith(
        GOOG_SCOPE_USED_IMPROPERLY.getMessage()));

    // Test goog.scope with multiple parameters
    sourceFile = new SourceFile("TestMultipleParameters.js", "goog.scope(1, 2, function() {})");
    compiler.process(sourceFile, null);
    traversal = compiler.getTraversal();
    assertEquals(1, traversal.getErrors().size());
    assertTrue(traversal.getErrors().get(0).getMessage().startsWith(
        GOOG_SCOPE_HAS_BAD_PARAMETERS.getMessage()));

    // Test goog.scope with non-anonymous function
    sourceFile = new SourceFile("TestNonAnonymous.js", "function nonAnonymousFunction() {}, goog.scope(nonAnonymousFunction)");
    compiler.process(sourceFile, null);
    traversal = compiler.getTraversal();
    assertEquals(1, traversal.getErrors().size());
    assertTrue(traversal.getErrors().get(0).getMessage().startsWith(
        GOOG_SCOPE_HAS_BAD_PARAMETERS.getMessage()));

    // Test goog.scope with return statement
    sourceFile = new SourceFile("TestReturn.js", "goog.scope(function() { return 1; })");
    compiler.process(sourceFile, null);
    traversal = compiler.getTraversal();
    assertEquals(1, traversal.getErrors().size());
    assertTrue(traversal.getErrors().get(0).getMessage().startsWith(
        GOOG_SCOPE_USES_RETURN.getMessage()));

    // Test goog.scope with throw statement
    sourceFile = new SourceFile("TestThrow.js", "goog.scope(function() { throw new Error('test'); })");
    compiler.process(sourceFile, null);
    traversal = compiler.getTraversal();
    assertEquals(1, traversal.getErrors().size());
    assertTrue(traversal.getErrors().get(0).getMessage().startsWith(
        GOOG_SCOPE_USES_THROW.getMessage()));

    // Test goog.scope with alias redefined
    sourceFile = new SourceFile("TestAliasRedefined.js", "goog.scope(function() { var g = goog.dom; var g = 'test'; })");
    compiler.process(sourceFile, null);
    traversal = compiler.getTraversal();
    assertEquals(1, traversal.getErrors().size());
    assertTrue(traversal.getErrors().get(0).getMessage().startsWith(
        GOOG_SCOPE_ALIAS_REDEFINED.getMessage()));

    // Test goog.scope with non-alias local
    sourceFile = new SourceFile("TestNonAliasLocal.js", "goog.scope(function() { var g = goog.dom; var nonAlias = g; })");
    compiler.process(sourceFile, null);
    traversal = compiler.getTraversal();
    assertEquals(1, traversal.getErrors().size());
    assertTrue(traversal.getErrors().get(0).getMessage().startsWith(
        GOOG_SCOPE_NON_ALIAS_LOCAL.getMessage()));

    // Test aliases
    sourceFile = new SourceFile(
        "TestAliases.js",
        "goog.scope(function() {" +
            "var g = goog;" +
            "var dom = g.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "g.scope(function() {" +
            "alias1 = 'test1';" +
            "alias2 = 'test2';" +
            "alias3 = alias2;" +
            "});" +
            "dom.createElement(DIV);" +
            "});");
    compiler.process(sourceFile, null);
    traversal = compiler.getTraversal();
    assertEquals(0, traversal.getErrors().size());
    assertEquals(0, traversal.getWarnings().size());
    ImmutableList<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(3, aliasDefinitions.size());
    ImmutableList<Node> aliasUsages = traversal.getAliasUsages();
    assertEquals(3, aliasUsages.size());
    Node alias1Definition = aliasDefinitions.get(0);
    Node alias1Usage = aliasUsages.get(0);
    assertEquals(alias1Definition, alias1Usage.getAliasDefinition());
    Node alias2Definition = aliasDefinitions.get(1);
    Node alias2Usage = aliasUsages.get(1);
    assertEquals(alias2Definition, alias2Usage.getAliasDefinition());
    Node alias3Definition = aliasDefinitions.get(2);
    Node alias3Usage = aliasUsages.get(2);
    assertEquals(alias2Definition, alias3Definition);
    assertEquals(alias2Definition, alias3Usage.getAliasDefinition());

    // Test alias types
    sourceFile = new SourceFile(
        "TestAliasTypes.js",
        "goog.scope(function() {" +
            "var g = goog;" +
            "var dom = g.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "var element = goog.dom.createElement(DIV);" +
            "g.scope(function() {" +
            "alias1 = 'test1';" +
            "alias2 = { type: 'Object', alias: 'goog.dom.createElement' };" +
            "alias3 = alias2.alias;" +
            "});" +
            "assertEquals('DIV', typeof alias1);" +
            "assertEquals('function', typeof alias3);" +
            "alias3();" +
            "});");
    compiler.process(sourceFile, null);
    traversal = compiler.getTraversal();
    assertEquals(0, traversal.getErrors().size());
    assertEquals(0, traversal.getWarnings().size());
    ImmutableList<Node> aliasDefinitionsTypes = traversal.getAliasDefinitionsInOrder();
    assertEquals(3, aliasDefinitionsTypes.size());
    ImmutableList<Node> aliasUsagesTypes = traversal.getAliasUsages();
    assertEquals(3, aliasUsagesTypes.size());
    Node alias1DefinitionType = aliasDefinitionsTypes.get(0);
    Node alias1UsageType = aliasUsagesTypes.get(0);
    assertEquals(alias1DefinitionType, alias1UsageType.getAliasDefinition());
    Node alias2DefinitionType = aliasDefinitionsTypes.get(1);
    Node alias2UsageType = aliasUsagesTypes.get(1);
    assertEquals(alias2DefinitionType, alias2UsageType.getAliasDefinition());
    Node alias3DefinitionType = aliasDefinitionsTypes.get(2);
    Node alias3UsageType = aliasUsagesTypes.get(2);
    assertEquals(alias2DefinitionType, alias3DefinitionType);
    assertEquals(alias2DefinitionType, alias3UsageType.getAliasDefinition());

    // Test alias used in type definition
    sourceFile = new SourceFile(
        "TestAliasUsedInType.js",
        "goog.scope(function() {" +
            "goog.scope(function() {" +
            "alias1 = 'test1';" +
            "});" +
            "function MyType() {" +
            "var myAlias = alias1;" +
            "}" +
            "var myType = new MyType();" +
            "});");
    compiler.process(sourceFile, null);
    traversal = compiler.getTraversal();
    assertEquals(0, traversal.getErrors().size());
    assertEquals(0, traversal.getWarnings().size());

    // Test alias used as function parameter
    sourceFile = new SourceFile(
        "TestAliasAsFunctionParameter.js",
        "goog.scope(function() {" +
            "function myFunction(alias) {" +
            "}" +
            "goog.scope(function() {" +
            "alias = 'test';" +
            "myFunction(alias);" +
            "});" +
            "});");
    compiler.process(sourceFile, null);
    traversal = compiler.getTraversal();
    assertEquals(0, traversal.getErrors().size());
    assertEquals(0, traversal.getWarnings().size());
  }

  @Test
  public void testHotSwap() {
    // TODO: Implement hot swapping test.
  }
}