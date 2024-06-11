import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

public class ScopedAliasesTest {

  @Test
  public void testSimpleAlias() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = createSourceFile("testSimpleAlias.js", "goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "});");

    Node root = compiler.parseTree(sourceFile, null);
    scopedAliases.process(null, root);

    Node expectedNode = createNode("testSimpleAlias.js", "goog.x");
    Node actualNode = findNode(root, "goog.x");
    assertEquals(expectedNode, actualNode);
  }

  @Test
  public void testAliasWithQualifiedName() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = createSourceFile("testAliasWithQualifiedName.js", "goog.scope(function() {\n" +
        "  var goog = {\n" +
        "    dom: function() {\n" +
        "      return {'createElement': function(tagName) {\n" +
        "        return document.createElement(tagName);\n" +
        "      }};\n" +
        "    },\n" +
        "    x: x => x,\n" +
        "  };\n" +
        "  var div = goog.dom.createElement('DIV');\n" +
        "});");

    Node root = compiler.parseTree(sourceFile, null);
    scopedAliases.process(null, root);

    Node expectedNode = createNode("testAliasWithQualifiedName.js", "goog.dom.createElement");
    Node actualNode = findNode(root, "goog.dom.createElement");
    assertEquals(expectedNode, actualNode);
  }

  @Test
  public void testAliasWithError() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = createSourceFile("testAliasWithError.js", "goog.scope(function() {\n" +
        "  var goog = {\n" +
        "    x: function() {\n" +
        "      return this;\n" +
        "    },\n" +
        "  };\n" +
        "});");

    Node root = compiler.parseTree(sourceFile, null);
    scopedAliases.process(null, root);

    try {
      scopedAliases.hotSwapScript(root, null);
      fail("Expected error: JSC_GOOG_SCOPE_REFERENCES_THIS");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testAliasWithReturn() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = createSourceFile("testAliasWithReturn.js", "goog.scope(function() {\n" +
        "  return goog.x;\n" +
        "});");

    Node root = compiler.parseTree(sourceFile, null);
    scopedAliases.process(null, root);

    try {
      scopedAliases.hotSwapScript(root, null);
      fail("Expected error: JSC_GOOG_SCOPE_USES_RETURN");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testAliasWithThrow() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = createSourceFile("testAliasWithThrow.js", "goog.scope(function() {\n" +
        "  throw new Error('Test error');\n" +
        "});");

    Node root = compiler.parseTree(sourceFile, null);
    scopedAliases.process(null, root);

    try {
      scopedAliases.hotSwapScript(root, null);
      fail("Expected error: JSC_GOOG_SCOPE_USES_THROW");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testAliasRedefined() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = createSourceFile("testAliasRedefined.js", "goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  var x = 1;\n" +
        "});");

    Node root = compiler.parseTree(sourceFile, null);
    scopedAliases.process(null, root);

    try {
      scopedAliases.hotSwapScript(root, null);
      fail("Expected error: JSC_GOOG_SCOPE_ALIAS_REDEFINED");
    } catch (IllegalArgumentException e) {
      String message = e.getMessage();
      assertTrue(message.startsWith("The alias x is assigned a value more than once."));
    }
  }

  @Test
  public void testAliasWithNonAliasLocal() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = createSourceFile("testAliasWithNonAliasLocal.js", "goog.scope(function() {\n" +
        "  var x = 1;\n" +
        "});");

    Node root = compiler.parseTree(sourceFile, null);
    scopedAliases.process(null, root);

    try {
      scopedAliases.hotSwapScript(root, null);
      fail("Expected error: JSC_GOOG_SCOPE_NON_ALIAS_LOCAL");
    } catch (IllegalArgumentException e) {
      String message = e.getMessage();
      assertTrue(message.startsWith("The local variable x is in a goog.scope and is not an alias."));
    }
  }

  private Node findNode(Node node, String name) {
    if (node.isName()) {
      if (node.getString().equals(name)) {
        return node;
      }
    }

    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      Node found = findNode(child, name);
      if (found != null) {
        return found;
      }
    }

    return null;
  }

  private Node createNode(String fileName, String value) {
    Node node = new Node_();
    node.setType(Token.STRING);
    node.setString(value);
    node.setFileName(fileName);
    return node;
  }

  private SourceFile createSourceFile(String fileName, String code) {
    return new SourceFile(fileName, code, null, null);
  }
}