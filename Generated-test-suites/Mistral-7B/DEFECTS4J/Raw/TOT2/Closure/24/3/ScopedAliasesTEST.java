import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.test.TestSourceFile;
import com.google.javascript.jscomp.test.TestUtil;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ScopedAliasesTest {

  private TestCompiler compiler;
  private PreprocessorSymbolTable symbolTable;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    symbolTable = new PreprocessorSymbolTable();
    scopedAliases = new ScopedAliases(compiler, symbolTable, null);
  }

  @Test
  public void testBasicFunctionCall() {
    String input = "goog.scope(function() {" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";
    TestSourceFile sourceFile = new TestSourceFile("Test.java", input);
    Node rootNode = compiler.compileSingleFile(sourceFile);
    scopedAliases.process(null, rootNode);

    List<Node> nodes = TestUtil.getAllNodes(rootNode);
    Node googScopeCall = TestUtil.findSingleNode(nodes, n -> n.isCall() &&
        n.getFirstChild().getString().equals("goog.scope"));
    Node googScopeBlock = googScopeCall.getLastChild();

    // Check that goog.scope was replaced with a single statement.
    assertEquals(1, googScopeBlock.getChildCount());

    // Check that goog.dom.createElement was replaced with goog.dom.createElement.
    Node createElementCall = TestUtil.findSingleNode(nodes, n -> n.isCall() &&
        n.getFirstChild().isQualifiedName() &&
        n.getFirstChild().getQualifiedName().getIdentifier().equals("createElement"));
    assertEquals("goog.dom.createElement", createElementCall.getFirstChild().getString());
  }

  @Test
  public void testFunctionWithAliasParameter() {
    String input = "function processElement(googElement) {" +
        "goog.scope(function() {" +
        "  googElement.setAttribute('class', 'test');" +
        "});" +
        "}";
    TestSourceFile sourceFile = new TestSourceFile("Test.java", input);
    Node rootNode = compiler.compileSingleFile(sourceFile);
    scopedAliases.process(null, rootNode);

    List<Node> nodes = TestUtil.getAllNodes(rootNode);
    Node processElementCall = TestUtil.findSingleNode(nodes, n -> n.isCall() &&
        n.getFirstChild().getString().equals("processElement"));
    Node googScopeCall = TestUtil.findSingleNode(nodes, n -> n.isCall() &&
        n.getFirstChild().getString().equals("goog.scope"));

    // Check that goog.scope was replaced with a single statement.
    assertEquals(1, googScopeCall.getLastChild().getChildCount());

    // Check that processElement was not transformed.
    assertEquals("processElement", processElementCall.getFirstChild().getString());

    // Check that googElement was replaced with goog.dom.Element.
    Node googElement = TestUtil.findSingleNode(nodes, n -> n.isGetProp() &&
        n.getFirstChild().getFirstChild().getString().equals("googElement"));
    assertEquals("goog.dom.Element", googElement.getQualifiedName().getIdentifier());
  }

  @Test
  public void testFunctionWithAliasReturn() {
    String input = "goog.scope(function() {" +
        "function processElement() {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "}" +
        "return processElement();" +
        "});";
    TestSourceFile sourceFile = new TestSourceFile("Test.java", input);
    Node rootNode = compiler.compileSingleFile(sourceFile);
    scopedAliases.process(null, rootNode);

    List<Node> nodes = TestUtil.getAllNodes(rootNode);
    Node googScopeCall = TestUtil.findSingleNode(nodes, n -> n.isCall() &&
        n.getFirstChild().getString().equals("goog.scope"));
    Node googScopeBlock = googScopeCall.getLastChild();

    // Check that goog.scope was replaced with a single statement.
    assertEquals(1, googScopeBlock.getChildCount());

    // Check that return statement was replaced with goog.dom.createElement.
    Node returnStatement = TestUtil.findSingleNode(nodes, n -> n.isReturn());
    Node returnValue = returnStatement.getLastChild();
    Node createElementCall = TestUtil.findSingleNode(nodes, n -> n.isCall() &&
        n.getFirstChild().isQualifiedName() &&
        n.getFirstChild().getQualifiedName().getIdentifier().equals("createElement"));
    assertEquals(returnValue, createElementCall);
  }

  @Test(expected = AssertionError.class)
  public void testFunctionWithMultipleStatements() {
    String input = "goog.scope(function() {" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";
    TestSourceFile sourceFile = new TestSourceFile("Test.java", input);
    Node rootNode = compiler.compileSingleFile(sourceFile);
    scopedAliases.process(null, rootNode);

    List<Node> nodes = TestUtil.getAllNodes(rootNode);
    Node googScopeCall = TestUtil.findSingleNode(nodes, n -> n.isCall() &&
        n.getFirstChild().getString().equals("goog.scope"));

    // Check that goog.scope was replaced with an error.
    assertNotNull(googScopeCall.getError());
  }

  @Test(expected = AssertionError.class)
  public void testFunctionWithThis() {
    String input = "goog.scope(function() {" +
        "this.foo = 42;" +
        "});";
    TestSourceFile sourceFile = new TestSourceFile("Test.java", input);
    Node rootNode = compiler.compileSingleFile(sourceFile);
    scopedAliases.process(null, rootNode);

    List<Node> nodes = TestUtil.getAllNodes(rootNode);
    Node googScopeCall = TestUtil.findSingleNode(nodes, n -> n.isCall() &&
        n.getFirstChild().getString().equals("goog.scope"));

    // Check that goog.scope was replaced with an error.
    assertNotNull(googScopeCall.getError());
  }

  @Test(expected = AssertionError.class)
  public void testFunctionWithThrow() {
    String input = "goog.scope(function() {" +
        "throw new Error('Test');" +
        "});";
    TestSourceFile sourceFile = new TestSourceFile("Test.java", input);
    Node rootNode = compiler.compileSingleFile(sourceFile);
    scopedAliases.process(null, rootNode);

    List<Node> nodes = TestUtil.getAllNodes(rootNode);
    Node googScopeCall = TestUtil.findSingleNode(nodes, n -> n.isCall() &&
        n.getFirstChild().getString().equals("goog.scope"));

    // Check that goog.scope was replaced with an error.
    assertNotNull(googScopeCall.getError());
  }

  @Test(expected = AssertionError.class)
  public void testFunctionWithLocalVariable() {
    String input = "goog.scope(function() {" +
        "var foo = 42;" +
        "});";
    TestSourceFile sourceFile = new TestSourceFile("Test.java", input);
    Node rootNode = compiler.compileSingleFile(sourceFile);
    scopedAliases.process(null, rootNode);

    List<Node> nodes = TestUtil.getAllNodes(rootNode);
    Node googScopeCall = TestUtil.findSingleNode(nodes, n -> n.isCall() &&
        n.getFirstChild().getString().equals("goog.scope"));

    // Check that goog.scope was replaced with an error.
    assertNotNull(googScopeCall.getError());
  }
}