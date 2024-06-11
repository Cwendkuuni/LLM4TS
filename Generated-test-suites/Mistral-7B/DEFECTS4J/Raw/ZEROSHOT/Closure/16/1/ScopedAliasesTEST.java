import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  private Compiler compiler;
  private CompilerOptions options;
  private AbstractCompiler abstractCompiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);
    abstractCompiler = Mockito.mock(AbstractCompiler.class);
    preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    transformationHandler = new AliasTransformationHandler() {};

    compiler = new Compiler(options, abstractCompiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testSimpleAlias() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.dom.createElement(DIV); " +
        "});";

    Node rootNode = Node.parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node aliasUsage = aliasUsages.get(0);
    Node scopeCall = scopeCalls.get(0);

    assertEquals("dom", aliasDefinition.getString());
    assertEquals("goog.dom", aliasUsage.aliasDefinition.getString());
    assertEquals("goog.dom.TagName.DIV", scopeCall.getFirstChild().getString());
  }

  @Test
  public void testNestedAliases() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var goog = {}; " +
        "  goog.foo = goog.dom; " +
        "  goog.scope(function() {" +
        "    var DIV = goog.foo.TagName.DIV; " +
        "    goog.dom.createElement(DIV); " +
        "  }); " +
        "});";

    Node rootNode = Node.parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node aliasUsage = aliasUsages.get(0);
    Node scopeCall = scopeCalls.get(0);

    assertEquals("dom", aliasDefinition.getString());
    assertEquals("goog.dom", aliasUsage.aliasDefinition.getString());
    assertEquals("goog.foo.TagName.DIV", scopeCall.getFirstChild().getString());
  }

  @Test(expected = AssertionError.class)
  public void testMultipleAliasesForSameName() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var dom = goog.dom; " +
        "});";

    Node rootNode = Node.parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(0, scopeCalls.size());

    fail("Expected an error for multiple aliases with the same name.");
  }

  @Test
  public void testAliasWithTypeNode() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = goog.dom.TagName.DIV; " +
        "  var MyClass = function() {}; " +
        "  MyClass.prototype.tagName = DIV; " +
        "  var myInstance = new MyClass(); " +
        "});";

    Node rootNode = Node.parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(2, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    Node aliasDefinition1 = aliasDefinitionsInOrder.get(0);
    Node aliasUsage1 = aliasUsages.get(0);
    Node aliasDefinition2 = aliasDefinitionsInOrder.get(1);
    Node aliasUsage2 = aliasUsages.get(1);
    Node scopeCall = scopeCalls.get(0);

    assertEquals("dom", aliasDefinition1.getString());
    assertEquals("goog.dom", aliasUsage1.aliasDefinition.getString());
    assertEquals("goog.dom.TagName.DIV", scopeCall.getFirstChild().getString());

    assertEquals("DIV", aliasDefinition2.getString());
    assertEquals("goog.dom.TagName.DIV", aliasUsage2.aliasDefinition.getString());
    assertEquals("myInstance.tagName", aliasUsage2.aliasReference.getString());
  }
}