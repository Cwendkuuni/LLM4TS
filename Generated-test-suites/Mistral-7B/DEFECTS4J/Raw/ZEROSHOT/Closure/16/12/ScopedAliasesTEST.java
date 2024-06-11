import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;

import javax.annotation.Nullable;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_ALIASES);
    options.setAliasTransformationHandler(new AliasTransformationHandler() {
      @Nullable
      @Override
      public AliasTransformation getAliasTransformation(
          String name, String sourceFileName, SourcePosition position) {
        return new AliasTransformation(name, "goog." + name);
      }
    });

    Compiler compiler = new Compiler();
    compiler.setOptions(options);
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    Node root = ParserHelper.parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasDefinition.getString());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    Node aliasReference = aliasUsage.getAliasReference();
    Node aliasDefinition = aliasUsage.getAliasDefinition();
    assertEquals("DOM", aliasReference.getString());
    assertEquals("goog.dom", aliasDefinition.getString());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    Node anonymousFnNode = scopeCall.getLastChild().getLastChild();
    assertEquals("function() {", anonymousFnNode.getString());
  }
}

class ParserHelper {
  public static Node parse(String code) {
    RhinoCompilationUnit unit = new RhinoCompilationUnit();
    unit.parse(code, new SourceFile("test.js", null, null));
    return unit.getRootNode();
  }
}