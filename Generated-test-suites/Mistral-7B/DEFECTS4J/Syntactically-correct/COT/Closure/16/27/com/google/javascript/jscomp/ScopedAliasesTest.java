import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopeCall;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.JSDocInfo;

/**
 * Test class for ScopedAliases.
 */
public class ScopedAliasesTest {

  @Test
  public void testProcess() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String fileName, SourcePosition position) {
        return new AliasTransformation() {};
      }
    });

    Compiler compiler = new Compiler();
    compiler.setPrintDiagnostics(true);

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    Node root = parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, options.getAliasTransformationHandler());
    scopedAliases.process(null, root);

    List<JSDocInfo> jsDocInfos = getJSDocInfos(root);
    assertEquals(0, jsDocInfos.size());

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<ScopeCall> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());

    assertEquals(1, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getString());

    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      AliasedNode aliasedNode = (AliasedNode) aliasUsage;
      assertEquals("goog.dom", aliasedNode.aliasReference.getString());
      assertEquals("dom", aliasedNode.aliasDefinition.getString());
    } else if (aliasUsage instanceof AliasedTypeNode) {
      fail("Unexpected alias usage type.");
    }
  }

  @Test
  public void testProcessWithErrors() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String fileName, SourcePosition position) {
        return new AliasTransformation() {};
      }
    });

    Compiler compiler = new Compiler();
    compiler.setPrintDiagnostics(true);

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "goog.scope(function() {" +
        "  dom.createElement(DIV);" +
        "});" +
        "});";

    Node root = parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, options.getAliasTransformationHandler());
    scopedAliases.process(null, root);

    List<JSDocInfo> jsDocInfos = getJSDocInfos(root);
    assertEquals(3, jsDocInfos.size());

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<ScopeCall> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());

    assertEquals(2, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getString());
    assertEquals("goog.scope", scopeCalls.get(1).getFirstChild().getString());

    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      AliasedNode aliasedNode = (AliasedNode) aliasUsage;
      assertEquals("goog.dom", aliasedNode.aliasReference.getString());
      assertEquals("dom", aliasedNode.aliasDefinition.getString());
    } else if (aliasUsage instanceof AliasedTypeNode) {
      fail("Unexpected alias usage type.");
    }

    List<DiagnosticType> diagnostics = scopedAliases.getTraversal().getDiagnostics();
    assertEquals(2, diagnostics.size());
    assertEquals(JSC_GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));
    assertEquals(JSC_GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(1));
  }

  @Test
  public void testProcessWithNamespaceShadow() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String fileName, SourcePosition position) {
        return new AliasTransformation() {};
      }
    });

    Compiler compiler = new Compiler();
    compiler.setPrintDiagnostics(true);

    String sourceCode = "goog.scope(function() {" +
        "var goog = {" +
        "  dom: function() {" +
        "    return {'TagName': {'DIV': 1}};" +
        "  }}" +
        "};" +
        "var DIV = goog.dom.TagName.DIV;" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  dom.createElement(DIV);" +
        "});" +
        "});";

    Node root = parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, options.getAliasTransformationHandler());
    scopedAliases.process(null, root);

    List<JSDocInfo> jsDocInfos = getJSDocInfos(root);
    assertEquals(2, jsDocInfos.size());

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<ScopeCall> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());

    assertEquals(2, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getString());
    assertEquals("goog.scope", scopeCalls.get(1).getFirstChild().getString());

    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      AliasedNode aliasedNode = (AliasedNode) aliasUsage;
      assertEquals("goog.dom", aliasedNode.aliasReference.getString());
      assertEquals("dom", aliasedNode.aliasDefinition.getString());
    } else if (aliasUsage instanceof AliasedTypeNode) {
      AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
      assertEquals("goog.dom", aliasedTypeNode.typeReference.getParent().getString());
      assertEquals("dom", aliasedTypeNode.aliasName);
    }

    List<DiagnosticType> diagnostics = scopedAliases.getTraversal().getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  private List<JSDocInfo> getJSDocInfos(Node root) {
    NodeTraversal traversal = new NodeTraversal(root, null);
    traversal.setScopedCallback(new NodeTraversal.ScopedCallback() {
      @Override
      public void enterScope(NodeTraversal t) {
        Node node = t.getCurrentNode();
        if (node.isFunction() && node.getParent() != null) {
          JSDocInfo jsDocInfo = node.getJSDocInfo();
          if (jsDocInfo != null) {
            List<Node> jsDocInfoNodes = jsDocInfo.getNodes();
            for (Node jsDocInfoNode : jsDocInfoNodes) {
              if (jsDocInfoNode.isTag()) {
                if (jsDocInfoNode.getFirstChild().getString().equals("@interface")) {
                  return Lists.newArrayList(jsDocInfo);
                }
              }
            }
          }
        }
      }
    });

    NodeTraversal.traverse(null, root, traversal);
    return traversal.getResults();
  }

  private static Node parse(String sourceCode) {
    RhinoCompilerOptions options = new RhinoCompilerOptions();
    RhinoCompiler rhinoCompiler = new RhinoCompiler();
    Node root = rhinoCompiler.compile(sourceCode, options);
    return root;
  }
}