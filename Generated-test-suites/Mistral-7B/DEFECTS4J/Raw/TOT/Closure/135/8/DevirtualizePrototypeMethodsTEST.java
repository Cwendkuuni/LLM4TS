import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Externs;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.DefinitionSite;
import com.google.javascript.jscomp.SimpleDefinitionFinder.Definition;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  private static class TestCompiler implements CompilerPass {
    @Override
    public void process(Node externs, Node root) {
      // Empty implementation.
    }
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethods() throws Exception {
    // Create a simple test case.
    String sourceCode =
        "function Foo() {\n" +
        "  this.sum = 0;\n" +
        "}\n" +
        "Foo.prototype.accumulate = function(value) {\n" +
        "  this.sum += value;\n" +
        "  return this.sum;\n" +
        "}";

    // Set up the compiler.
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPass(new TestCompiler());
    compiler.addPass(new DevirtualizePrototypeMethods(null));

    // Compile the source code.
    Externs externs = new Externs();
    Node root = NodeUtil.parse(sourceCode, externs);
    compiler.compile(externs, root);

    // Check the compiled code.
    List<DefinitionSite> definitionSites =
        ((SimpleDefinitionFinder) compiler.getPassManager()
            .getPass(SimpleDefinitionFinder.class))
            .getDefinitionSites();
    Definition accumulateDefinition = null;
    for (DefinitionSite definitionSite : definitionSites) {
      Definition definition = definitionSite.definition;
      if (definition.getName().equals("accumulate")) {
        accumulateDefinition = definition;
        break;
      }
    }

    assertNotNull(accumulateDefinition);

    Node accumulateDefinitionNode = accumulateDefinition.getNode();
    Node accumulateNameNode = accumulateDefinitionNode.getLastChild();

    // Check the name of the rewritten method.
    assertNotNull(accumulateNameNode);
    String newMethodName = accumulateNameNode.getString().substring(16);
    assertEquals("JSCompiler_StaticMethods_accumulate", newMethodName);

    // Check the function type of the rewritten method.
    FunctionType functionType =
        (FunctionType) accumulateDefinitionNode.getJSType();
    List<JSType> parameterTypes = functionType.getParameters();
    assertEquals(1, parameterTypes.size());
    assertNotNull(parameterTypes.get(0).getName());
    assertEquals("self", parameterTypes.get(0).getName());

    // Check the body of the rewritten method.
    Node body = accumulateDefinitionNode.getFirstChild();
    String bodyCode = body.toSource();
    assertEquals(
        "function(self, value) {\n" +
        "  self.sum += value;\n" +
        "  return self.sum;\n" +
        "}",
        bodyCode);

    // Check the call site.
    Node callSite =
        NodeUtil.findFirstDescendantByPredicate(
            root, (Node node) -> NodeUtil.isCall(node));
    assertNotNull(callSite);

    // Check that the rewritten method is called instead of the original one.
    String callSiteCode = callSite.toSource();
    assertEquals(
        "Foo.prototype.accumulate(a, b)", callSiteCode);
    assertEquals(newMethodName, callSite.getFirstChild().getString());
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethodsWithExportedMethod()
      throws Exception {
    // Create a simple test case.
    String sourceCode =
        "function Foo() {\n" +
        "  this.sum = 0;\n" +
        "}\n" +
        "Foo.prototype.accumulate = function(value) {\n" +
        "  this.sum += value;\n" +
        "  return this.sum;\n" +
        "}\n" +
        "exports.accumulate = Foo.prototype.accumulate;";

    // Set up the compiler.
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPass(new TestCompiler());
    compiler.addPass(new DevirtualizePrototypeMethods(null));

    // Compile the source code.
    Externs externs = new Externs();
    Node root = NodeUtil.parse(sourceCode, externs);
    compiler.compile(externs, root);

    // Check that the method is not rewritten since it is exported.
    List<DefinitionSite> definitionSites =
        ((SimpleDefinitionFinder) compiler.getPassManager()
            .getPass(SimpleDefinitionFinder.class))
            .getDefinitionSites();
    Definition accumulateDefinition = null;
    for (DefinitionSite definitionSite : definitionSites) {
      Definition definition = definitionSite.definition;
      if (definition.getName().equals("accumulate")) {
        accumulateDefinition = definition;
        break;
      }
    }

    assertNotNull(accumulateDefinition);
    assertNull(accumulateDefinition.getNode().getFirstChild());
  }
}