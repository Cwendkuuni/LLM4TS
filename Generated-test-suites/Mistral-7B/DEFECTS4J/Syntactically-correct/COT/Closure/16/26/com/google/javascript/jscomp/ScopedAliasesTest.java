import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createCallExpression;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createLiteral;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createProperty;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createVar;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {};
  }

  @Test
  public void testScopedAliases() {
    // Given: A valid JavaScrip file with a goog.scope block
    String javaScriptCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "});";

    // When: Compile the JavaScrip file with the ScopedAliases pass
    Node root = compiler.parse(javaScriptCode, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    // Then: Assert that there are no errors and aliases are applied correctly
    Traversal traversal = root.getUserData(ScopedAliases.TRAVERSAL_KEY);
    assertFalse(traversal.hasErrors());
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(2, aliasUsages.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node scopeCall = scopeCalls.get(0);
    Node aliasUsage1 = aliasUsages.get(0);
    Node aliasUsage2 = aliasUsages.get(1);

    // Check aliasDefinition
    assertEquals(createCallExpression(createName("goog", "scope"), null), scopeCall);
    assertEquals(createProperty(createIdentifier("dom"), createIdentifier("TagName"), createLiteral(1), null), aliasDefinition);

    // Check aliasUsage1
    Node aliasReference = aliasUsage1.getAliasReference();
    Node aliasDefinitionNode = aliasUsage1.getAliasDefinition();
    assertEquals(createIdentifier("dom"), aliasReference);
    assertEquals(aliasDefinitionNode, aliasDefinition);

    // Check aliasUsage2
    Node aliasReference2 = aliasUsage2.getAliasReference();
    Node aliasDefinitionNode2 = aliasUsage2.getAliasDefinition();
    String aliasName = aliasUsage2.getAliasName();
    assertEquals(createIdentifier("DIV"), aliasReference2);
    assertEquals(createIdentifier(aliasName), aliasDefinitionNode2);
  }

  @Test
  public void testScopedAliasesWithBadParameters() {
    // Given: A JavaScrip file with a goog.scope call with bad parameters
    String javaScriptCode =
        "goog.scope(function() {" +
            "goog.scope(function() {" +
            "});" +
            "});";

    // When: Compile the JavaScrip file with the ScopedAliases pass
    Node root = compiler.parse(javaScriptCode, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    // Then: Assert that there is an error due to bad parameters
    Traversal traversal = root.getUserData(ScopedAliases.TRAVERSAL_KEY);
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, error);
  }

  @Test
  public void testScopedAliasesWithShadowedLocal() {
    // Given: A JavaScrip file with a local variable shadowing a goog.scope alias
    String javaScriptCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "var DIV = 42;" +
            "dom.createElement(DIV);" +
            "});";

    // When: Compile the JavaScrip file with the ScopedAliases pass
    Node root = compiler.parse(javaScriptCode, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    // Then: Assert that there is an error due to shadowed local
    Traversal traversal = root.getUserData(ScopedAliases.TRAVERSAL_KEY);
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, error);
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    // Given: A JavaScrip file with a goog.scope block containing a throw statement
    String javaScriptCode =
        "goog.scope(function() {" +
            "throw new Error('test');" +
            "});";

    // When: Compile the JavaScrip file with the ScopedAliases pass
    Node root = compiler.parse(javaScriptCode, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    // Then: Assert that there is an error due to throw statement
    Traversal traversal = root.getUserData(ScopedAliases.TRAVERSAL_KEY);
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_USES_THROW, error);
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    // Given: A JavaScrip file with a goog.scope block containing a return statement
    String javaScriptCode =
        "goog.scope(function() {" +
            "return 42;" +
            "});";

    // When: Compile the JavaScrip file with the ScopedAliases pass
    Node root = compiler.getSourceNodeFactory().createScriptletNode(javaScriptCode, new SourcePosition(0, 0));
    CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.setAliasTransformation(AliasTransformation.STRICT);
    compilerOptions.setAliasTransformationHandler(transformationHandler);
    Compiler compilerInstance = new Compiler();
    Node parsedNode = compilerInstance.parse(root, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compilerInstance, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(parsedNode, null);

    // Then: Assert that there is no error and the alias is applied correctly
    Traversal traversal = parsedNode.getUserData(ScopedAliases.TRAVERSAL_KEY);
    assertFalse(traversal.hasErrors());
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(1, aliasUsages.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node scopeCall = scopeCalls.get(0);
    Node aliasUsage = aliasUsages.get(0);

    // Check aliasDefinition
    assertEquals(createCallExpression(createName("goog", "scope"), null), scopeCall);
    assertEquals(createIdentifier("return"), aliasDefinition);

    // Check aliasUsage
    Node aliasReference = aliasUsage.getAliasReference();
    Node aliasDefinitionNode = aliasUsage.getAliasDefinition();
    assertEquals(createIdentifier("return"), aliasReference);
    assertEquals(aliasDefinitionNode, aliasDefinition);
  }

  @Test
  public void testScopedAliasesWithThisKeyword() {
    // Given: A JavaScrip file with a goog.scope block containing 'this' keyword
    String javaScriptCode =
        "goog.scope(function() {" +
            "this.test = 42;" +
            "});";

    // When: Compile the JavaScrip file with the ScopedAliases pass
    Node root = compiler.parse(javaScriptCode, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    // Then: Assert that there is an error due to 'this' keyword
    Traversal traversal = root.getUserData(ScopedAliases.TRAVERSAL_KEY);
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, error);
  }
}