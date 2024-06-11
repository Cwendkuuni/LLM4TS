import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.DiagnosticListener;
import com.google.javascript.jscomp.ErrorCollector;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {
  private static AbstractCompiler compiler;
  private static ErrorCollector diagnostics;
  private static SourceFile sourceFile;
  private static AliasTransformationHandler transformationHandler;
  private static DiagnosticListener diagnosticListener;

  @BeforeClass
  public static void setUp() {
    compiler = new AbstractCompiler();
    diagnostics = new ErrorCollector();
    sourceFile = new SourceFile("Test.java", null);
    transformationHandler = new AliasTransformationHandler();
    diagnosticListener = new DiagnosticListener() {
      @Override
      public void handleDiagnostic(DiagnosticType type, SourcePosition position,
          String message, Object[] args) {
        fail("Unexpected diagnostic: " + type + " at " + position + ", " + message);
      }
    };
    compiler.addPass(new HotSwapCompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new ScopedAliases(compiler, null, transformationHandler).process(externs, root);
      }

      @Override
      public void hotSwapScript(Node root, Node originalRoot) {
        // No-op
      }
    });
  }

  @Test
  public void testSimpleAlias() {
    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();
    Node root = node.getFirstChild();

    new Traversal() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("DIV")) {
          assertEquals(
              "goog.dom.TagName.DIV",
              n.getParent().getLastChild().getQualifiedName());
        }
      }
    }.traverse(compiler, root, null);

    Node result = compiler.compile(node, sourceFile);
    assertNotNull(result);
  }

  @Test
  public void testBadAliasCall() {
    String code =
        "goog.scope(function() {" +
        "  goog.scope(function() {});" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();

    compiler.process(node, sourceFile, null, diagnostics, null);

    List<DiagnosticType> diagnosticsList = Lists.newArrayList(diagnostics.getAll());
    assertEquals(1, diagnosticsList.size());
    assertEquals(JSC_GOOG_SCOPE_USED_IMPROPERLY, diagnosticsList.get(0));
  }

  @Test
  public void testAliasRedefinition() {
    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var DIV = 'div';" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();

    compiler.process(node, sourceFile, null, diagnostics, null);

    List<DiagnosticType> diagnosticsList = Lists.newArrayList(diagnostics.getAll());
    assertEquals(1, diagnosticsList.size());
    assertEquals(JSC_GOOG_SCOPE_ALIAS_REDEFINED, diagnosticsList.get(0));
  }

  @Test
  public void testAliasNonLocalVariable() {
    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var localVar = 'local';" +
        "  var DIV = dom.TagName.DIV;" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();

    compiler.process(node, sourceFile, null, diagnostics, null);

    List<DiagnosticType> diagnosticsList = Lists.newArrayList(diagnostics.getAll());
    assertEquals(1, diagnosticsList.size());
    assertEquals(JSC_GOOG_SCOPE_NON_ALIAS_LOCAL, diagnosticsList.get(0));
  }

  @Test
  public void testAliasFunction() {
    String code =
        "goog.scope(function() {" +
        "  function createElement(tagName) {" +
        "    return goog.dom.createElement(tagName);" +
        "  }" +
        "  var DIV = createElement;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();

    new Traversal() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("DIV")) {
          assertEquals(
              "goog.dom.createElement",
              n.getParent().getLastChild().getQualifiedName());
        }
      }
    }.traverse(compiler, node, null);

    Node result = compiler.compile(node, sourceFile);
    assertNotNull(result);
  }

  @Test
  public void testAliasMethod() {
    String code =
        "goog.scope(function() {" +
        "  var dom = {};" +
        "  dom.createElement = function(tagName) {" +
        "    return goog.dom.createElement(tagName);" +
        "  }" +
        "  var DIV = dom.createElement;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();

    new Traversal() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("DIV")) {
          assertEquals(
              "goog.dom.createElement",
              n.getParent().getLastChild().getQualifiedName());
        }
      }
    }.traverse(compiler, node, null);

    Node result = compiler.compile(node, sourceFile);
    assertNotNull(result);
  }

  @Test
  public void testAliasType() {
    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "  var divTagName = goog.dom.TagName.DIV;" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();

    new Traversal() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("DIV")) {
          assertEquals(
              "goog.dom.TagName.DIV",
              n.getParent().getLastChild().getQualifiedName());
        } else if (n.getType() == Token.NAME && n.getString().equals("divTagName")) {
          assertEquals(
              "goog.dom.TagName.DIV",
              n.getParent().getLastChild().getQualifiedName());
        }
      }
    }.traverse(compiler, node, null);

    Node result = compiler.compile(node, sourceFile);
    assertNotNull(result);
  }

  @Test
  public void testAliasChain() {
    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  var divElement = goog.dom.createElement(DIV);" +
        "  var divStyle = divElement.style;" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();

    new Traversal() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("DIV")) {
          assertEquals(
              "goog.dom.TagName.DIV",
              n.getParent().getLastChild().getQualifiedName());
        } else if (n.getType() == Token.NAME && n.getString().equals("divElement")) {
          assertEquals(
              "goog.dom.createElement",
              n.getParent().getLastChild().getQualifiedName());
        } else if (n.getType() == Token.NAME && n.getString().equals("divStyle")) {
          assertEquals(
              "goog.dom.elements.DivElement.prototype.style",
              n.getParent().getLastChild().getQualifiedName());
        }
      }
    }.traverse(compiler, node, null);

    Node result = compiler.compile(node, sourceFile);
    assertNotNull(result);
  }

  @Test
  public void testAliasFunctionWithParameters() {
    String code =
        "goog.scope(function() {" +
        "  function createElement(tagName) {" +
        "    return goog.dom.createElement(tagName);" +
        "  }" +
        "  function appendToBody(node) {" +
        "    document.body.appendChild(node);" +
        "  }" +
        "  var DIV = createElement;" +
        "  var divElement = DIV();" +
        "  appendToBody(divElement);" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();

    new Traversal() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("DIV")) {
          assertEquals(
              "goog.dom.createElement",
              n.getParent().getLastChild().getQualifiedName());
        } else if (n.getType() == Token.NAME && n.getString().equals("divElement")) {
          Node createElementCall = n.getParent();
          assertEquals("goog.dom.createElement", createElementCall.getQualifiedName());
          assertEquals(1, createElementCall.getChildCount());
          assertEquals(Token.CALL, createElementCall.getChildAtIndex(0).getType());
          assertEquals(
              "goog.dom.createElement",
              createElementCall.getChildAtIndex(0).getLastChild().getQualifiedName());
        } else if (n.getType() == Token.NAME && n.getString().equals("appendToBody")) {
          assertEquals(
              "document.body.appendChild",
              n.getParent().getLastChild().getQualifiedName());
        }
      }
    }.traverse(compiler, node, null);

    Node result = compiler.compile(node, sourceFile);
    assertNotNull(result);
  }

  @Test
  public void testAliasFunctionWithLocalVariable() {
    String code =
        "goog.scope(function() {" +
        "  function createElement(tagName) {" +
        "    var localVar = 'local';" +
        "    return goog.dom.createElement(tagName);" +
        "  }" +
        "  var DIV = createElement;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();

    compiler.process(node, sourceFile, null, diagnostics, null);

    List<DiagnosticType> diagnosticsList = Lists.newArrayList(diagnostics.getAll());
    assertEquals(1, diagnosticsList.size());
    assertEquals(JSC_GOOG_SCOPE_NON_ALIAS_LOCAL, diagnosticsList.get(0));
  }

  @Test
  public void testAliasFunctionWithReturn() {
    String code =
        "goog.scope(function() {" +
        "  function createElement(tagName) {" +
        "    return goog.dom.createElement(tagName);" +
        "    return 42;" +
        "  }" +
        "  var DIV = createElement;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();

    compiler.process(node, sourceFile, null, diagnostics, null);

    List<DiagnosticType> diagnosticsList = Lists.newArrayList(diagnostics.getAll());
    assertEquals(1, diagnosticsList.size());
    assertEquals(JSC_GOOG_SCOPE_USES_RETURN, diagnosticsList.get(0));
  }

  @Test
  public void testAliasFunctionWithThrow() {
    String code =
        "goog.scope(function() {" +
        "  function createElement(tagName) {" +
        "    try {" +
        "      return goog.dom.createElement(tagName);" +
        "    } catch (e) {" +
        "      throw e;" +
        "    }" +
        "  }" +
        "  var DIV = createElement;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node node = compiler.parse(code, sourceFile).getTree();

    compiler.process(node, sourceFile, null, diagnostics, null);

    List<DiagnosticType> diagnosticsList = Lists.newArrayList(diagnostics.getAll());
    assertEquals(1, diagnosticsList.size());
    assertEquals(JSC_GOOG_SCOPE_USES_THROW, diagnosticsList.get(0));
  }

  @AfterClass
  public static void tearDown() {
    diagnostics.clear();
  }
}