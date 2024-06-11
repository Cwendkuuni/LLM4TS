import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.JSTypeRegistry;

public class DevirtualizePrototypeMethodsTest extends CompilerPassTestCase {

  private AbstractCompiler compiler;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    compiler = getCompiler();
  }

  @Override
  public CompilerPass getPass() {
    return new DevirtualizePrototypeMethods(compiler);
  }

  public void testDevirtualizePrototypeMethods() throws Exception {
    String input =
        "var a = {};\n" +
        "a.prototype.accumulate = function(value) {\n" +
        "  this.total += value;\n" +
        "  return this.total\n" +
        "};\n" +
        "var total = a.accumulate(2);";

    Node root = parseJs(input);

    compile(root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    List<Definition> definitions = defFinder.getDefinitionSites().stream()
        .map(DefinitionSite::getDefinition)
        .collect(ImmutableList.toImmutableList());

    assertEquals(1, definitions.size());

    Definition definition = definitions.get(0);
    assertNotNull("Definition should not be null", definition);

    Node definitionNode = definition.getNode();
    assertEquals("a.prototype.accumulate", definitionNode.getString());

    Node functionNode = definitionNode.getLastChild();
    assertEquals("accumulate", functionNode.getLastChild().getString());
    assertEquals(3, functionNode.getChildCount());

    Node body = functionNode.getLastChild();
    assertEquals(2, body.getChildCount());

    Node thisNode = body.getChild(0);
    assertEquals("this", thisNode.getString());

    Node totalNode = body.getChild(1);
    assertEquals("this.total", totalNode.getString());

    // Check that the rewritten method was created as a global function.
    Node rewrittenFunction = null;
    for (Node child : root.getChildren()) {
      if (child.getClass() == Node.class &&
          child.getNodeType() == TOKEN &&
          child.getString().startsWith("JSCompiler_StaticMethods_accumulate")) {
        rewrittenFunction = child;
        break;
      }
    }

    assertNotNull("Rewritten function should exist", rewrittenFunction);
    assertEquals(4, rewrittenFunction.getChildCount());

    Node rewrittenThisNode = rewrittenFunction.getChild(0);
    assertEquals("self", rewrittenThisNode.getString());

    Node rewrittenBody = rewrittenFunction.getLastChild();
    assertEquals(2, rewrittenBody.getChildCount());

    Node rewrittenTotalNode = rewrittenBody.getChild(1);
    assertEquals("self.total", rewrittenTotalNode.getString());

    Node rewrittenReturnNode = rewrittenBody.getChild(2);
    assertEquals("return self.total", rewrittenReturnNode.getString());

    // Check that the call site was rewritten.
    Node callSite = findCallSite(root, "a.accumulate");
    assertNotNull("Call site should exist", callSite);

    assertEquals(3, callSite.getChildCount());

    Node rewrittenCallSiteFunction = callSite.getChild(0);
    assertEquals("JSCompiler_StaticMethods_accumulate",
                 rewrittenCallSiteFunction.getString());

    Node rewrittenCallSiteThis = rewrittenCallSiteFunction.getLastChild();
    assertEquals("a", rewrittenCallSiteThis.getString());

    Node rewrittenCallSiteArguments =
        rewrittenCallSiteFunction.getChild(rewrittenCallSiteThis.getIndex());
    assertEquals("2", rewrittenCallSiteArguments.getString());
  }
}