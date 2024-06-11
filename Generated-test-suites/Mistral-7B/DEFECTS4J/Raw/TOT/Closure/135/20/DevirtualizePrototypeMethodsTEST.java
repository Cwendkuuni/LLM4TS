import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions.WarningLevel;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  public static void main(String[] args) {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WarningLevel.VERBOSE);
    options.setCheckLevel(CheckLevel.ERROR);

    String inputCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function(a, b) {\n" +
        "  this.qux = a + b;\n" +
        "  return this.qux;\n" +
        "};\n" +
        "var foo = new Foo();\n" +
        "assertThis(foo.bar, foo, 1, 2);\n";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new DevirtualizePrototypeMethods(compiler));
    CompilerResult result = passManager.runCompilation(inputCode, null, options);

    assertEquals(0, result.getDiagnostics().size());

    String outputCode = result.toSource();
    Node rootNode = NodeFactory.compile(outputCode);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, rootNode);

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() > 0);

    DefinitionSite definitionSite = definitionSites.get(0);
    assertSame(definitionSite.definition.getRValue(), rootNode.getChildAt(3));
    assertSame(definitionSite.definition.getLValue(), rootNode.getChildAt(5));
    assertSame(definitionSite.definition.getLValue().getLastChild(),
               rootNode.getChildAt(6));
    assertSame(definitionSite.definition.getLValue().getLastChild().getString(),
               "Foo.prototype.bar");

    Node barFunction = definitionSite.definition.getRValue();

    List<JSType> argumentTypes = Lists.newArrayList();
    argumentTypes.add(JSType.UNDEFINED);
    argumentTypes.add(JSType.NUMBER);
    JSType returnType = JSType.NUMBER;
    JSType functionType = new ObjectType(NodeFactory.createName("Function"),
                                         argumentTypes, returnType);
    JSType newFunctionType = (FunctionType) barFunction.getJSType();
    // The first argument type should be the this type.
    JSType thisType = newFunctionType.getTypeOfThis();
    argumentTypes.add(0, thisType);
    // The type of this should be NO_TYPE since the function is now static.
    assertSame(JSType.NO_TYPE, newFunctionType.getTypeOfThis());
    assertSame(functionType,
               compiler.getTypeRegistry().createFunctionType(
                   thisType, returnType, argumentTypes));

    Node callSite = rootNode.getChildAt(8);
    assertEquals(callSite.getLastChild().getInt(), 3);
    assertEquals(callSite.getLastChild().getLastChild().getInt(), 4);
    assertSame(callSite.getLastChild().getLastChild().getLastChild(), foo);

    String rewrittenMethodName = definitionSite.definition.getLValue()
        .getLastChild().getString();
    assertEquals("JSCompiler_StaticMethods_bar", rewrittenMethodName);

    Node rewrittenFunction = rootNode.getChildAt(2);
    assertSame(rewrittenFunction.getLastChild().getString(),
               rewrittenMethodName);

    Node rewrittenFunctionCall = rootNode.getChildAt(10);
    assertSame(rewrittenFunctionCall.getFirstChild().getString(),
               rewrittenMethodName);

    Node thisReference = rewrittenFunction.getFirstChild();
    assertSame(thisReference.getType(), thisType);

    Node argumentA = rewrittenFunction.getChildAt(1);
    assertSame(argumentA.getType(), JSType.NUMBER);

    Node argumentB = rewrittenFunction.getChildAt(2);
    assertSame(argumentB.getType(), JSType.NUMBER);

    Node resultNode = rewrittenFunction.getLastChild();
    assertSame(resultNode.getType(), returnType);

    Node quxProperty = foo.getChildAt(0).getChildAt(0);
    assertSame(quxProperty.getType(), JSType.NUMBER);
    assertEquals(quxProperty.getInt(), 3);
  }
}