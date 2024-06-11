import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.List;

public class ScopedAliasesTest {

  private static Compiler compiler;
  private static CompilerOptions options;
  private static AliasTransformationHandler transformationHandler;

  @BeforeClass
  public static void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions(null);
    transformationHandler = new AliasTransformationHandler() {};
    options.setAliasTransformationHandler(transformationHandler);
  }

  @Test
  public void testSimpleAlias() {
    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "})";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    Node rootNode = compiler.parseTree(sourceFile, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCallNode = scopeCalls.get(0);
    Node scopeFunctionNode = scopeCallNode.getLastChild();

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());

    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      AliasedNode aliasedNode = (AliasedNode) aliasUsage;
      Node aliasDefinition = aliasedNode.getAliasDefinition();
      assertEquals("dom", aliasDefinition.getQualifiedName());
      assertEquals("goog.dom", aliasedNode.getAliasReference().getString());
    } else if (aliasUsage instanceof AliasedTypeNode) {
      fail("Unexpected alias usage type.");
    }

    Node domNode = scopeFunctionNode.getFirstChild().getLastChild();
    assertEquals("DIV", domNode.getLastChild().getString());
  }

  @Test
  public void testRedefinedAlias() {
    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "})();" +
            "dom.createElement(DIV);" +
            "})";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    Node rootNode = compiler.parseTree(sourceFile, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(2, scopeCalls.size());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());

    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      AliasedNode aliasedNode = (AliasedNode) aliasUsage;
      Node aliasDefinition = aliasedNode.getAliasDefinition();
      assertEquals("dom", aliasDefinition.getQualifiedName());
      assertEquals("goog.dom", aliasedNode.getAliasReference().getString());
    } else if (aliasUsage instanceof AliasedTypeNode) {
      fail("Unexpected alias usage type.");
    }

    try {
      compiler.compile(rootNode, options);
      fail("Expected compilation error due to alias redefinition.");
    } catch (IOException e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("The alias DIV is assigned a value more than once."));
    }
  }

  @Test
  public void testInvalidScopeCall() {
    String sourceCode =
        "goog.scope(function() {" +
            "goog.scope(function() {})" +
            "})";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    Node rootNode = compiler.parseTree(sourceFile, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(0, scopeCalls.size());

    try {
      compiler.compile(rootNode, options);
      fail("Expected compilation error due to invalid goog.scope call.");
    } catch (IOException e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("The call to goog.scope must take only a single statement."));
    }
  }

  @Test
  public void testScopeCallWithParameters() {
    String sourceCode =
        "goog.scope(function(arg) {" +
            "goog.scope(function() {" +
            "  arg;" +
            "})();" +
            "})";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    Node rootNode = compiler.parseTree(sourceFile, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCallNode = scopeCalls.get(0);
    Node scopeFunctionNode = scopeCallNode.getLastChild();

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    try {
      compiler.compile(rootNode, options);
      fail("Expected compilation error due to scope call with parameters.");
    } catch (IOException e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("The call to goog.scope must take only a single statement."));
    }
  }

  @Test
  public void testScopeCallWithReturnStatement() {
    String sourceCode =
        "goog.scope(function() {" +
            "goog.scope(function() {" +
            "  return;" +
            "})();" +
            "})";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    Node rootNode = compiler.parseTree(sourceFile, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCallNode = scopeCalls.get(0);
    Node scopeFunctionNode = scopeCallNode.getLastChild();

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    try {
      compiler.compile(rootNode, options);
      fail("Expected compilation error due to scope call with return statement.");
    } catch (IOException e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("The body of a goog.scope function cannot use 'return'."));
    }
  }

  @Test
  public void testScopeCallWithThrowStatement() {
    String sourceCode =
        "goog.scope(function() {" +
            "try {" +
            "  goog.scope(function() {" +
            "    throw new Error('Error');" +
            "  })();" +
            "} catch (e) {" +
            "}" +
            "})";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    Node rootNode = compiler.parseTree(sourceFile, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCallNode = scopeCalls.get(0);
    Node scopeFunctionNode = scopeCallNode.getLastChild();

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    try {
      compiler.compile(rootNode, options);
      fail("Expected compilation error due to scope call with throw statement.");
    } catch (IOException e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("The body of a goog.scope function cannot use 'throw'."));
    }
  }

  @Test
  public void testAliasInFunction() {
    String sourceCode =
        "function foo() {" +
        "  goog.scope(function() {" +
        "    var x = goog.x;" +
        "    x = 42;" +
        "  })();" +
        "  return x;" +
        "}" +
        "goog.x = 0";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    Node rootNode = compiler.parseTree(sourceFile, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCallNode = scopeCalls.get(0);
    Node scopeFunctionNode = scopeCallNode.getLastChild();

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());

    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      AliasedNode aliasedNode = (AliasedNode) aliasUsage;
      Node aliasDefinition = aliasedNode.getAliasDefinition();
      assertEquals("x", aliasDefinition.getQualifiedName());
      assertEquals("goog.x", aliasedNode.getAliasReference().getString());
    } else if (aliasUsage instanceof AliasedTypeNode) {
      fail("Unexpected alias usage type.");
    }

    Node xNode = scopeFunctionNode.getFirstChild().getLastChild();
    assertEquals(42, xNode.getNumber().intValue());

    Node returnNode = scopeFunctionNode.getLastChild();
    assertEquals(42, returnNode.getNumber().intValue());

    assertEquals(42, rootNode.getChildAtIndex(1).getNumber().intValue());
  }

  @AfterClass
  public static void tearDown() {
    compiler = null;
    options = null;
    transformationHandler = null;
  }
}