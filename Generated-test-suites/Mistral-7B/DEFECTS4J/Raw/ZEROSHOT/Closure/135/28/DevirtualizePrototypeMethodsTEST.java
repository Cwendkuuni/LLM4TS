import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilationTestHelper;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.TypeChecker;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private CompilerOptions options;
  private SimpleDefinitionFinder definitionFinder;

  @org.junit.Before
  public void setUp() throws Exception {
    compiler = mock(AbstractCompiler.class);
    passManager = new CompilerPassManager(compiler);
    options = new CompilerOptions();

    definitionFinder = new SimpleDefinitionFinder(compiler);
    passManager.addPass(new DevirtualizePrototypeMethods(compiler));
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethods() throws Exception {
    String input =
        "function A() { }\n" +
            "A.prototype.accumulate = function(a, b) { return a + b };\n" +
            "var c = new A();\n" +
            "var total = c.accumulate(2, 3);\n";

    TestSourceFile sourceFile =
        new TestSourceFile(input, "DevirtualizePrototypeMethodsTest.js");
    CompilationTestHelper helper = new CompilationTestHelper("Test", sourceFile);
    helper.setOptions(options);
    helper.setPassManager(passManager);

    Node externs = helper.parseExterns();
    Node root = helper.parseTopLevelCode();
    passManager.process(externs, root);

    // Check that there is no longer a prototype method definition.
    List<Definition> definitions = definitionFinder.getDefinitions();
    assertEquals(0, definitions.size());

    // Check that the method call has been rewritten as a static call.
    TypeChecker typeChecker = compiler.getTypeChecker();
    JSType actualType = typeChecker.checkType(root, TypeChecker.Type.TOP);
    FunctionType functionType = (FunctionType) actualType;
    List<JSType> arguments = functionType.getParameters();
    assertEquals("JSCompiler_StaticMethods_accumulate", arguments.get(0).getName());
    assertEquals(JSType.OBJECT, arguments.get(1).getType());
    assertEquals(JSType.NUMBER, arguments.get(2).getType());
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethodsWithVarArgs() throws Exception {
    String input =
        "function A() { }\n" +
            "A.prototype.accumulate = function() {\n" +
            "  var sum = 0;\n" +
            "  for (var i = 0, len = arguments.length; i < len; ++i) {\n" +
            "    sum += arguments[i];\n" +
            "  }\n" +
            "  return sum;\n" +
            "};\n" +
            "var c = new A();\n" +
            "var total = c.accumulate(2, 3, 4, 5);\n";

    TestSourceFile sourceFile =
        new TestSourceFile(input, "DevirtualizePrototypeMethodsTest.js");
    CompilationTestHelper helper = new CompilationTestHelper("Test", sourceFile);
    helper.setOptions(options);
    helper.setPassManager(passManager);

    Node externs = helper.parseExterns();
    Node root = helper.parseTopLevelCode();
    passManager.process(externs, root);

    // Check that there is no longer a prototype method definition.
    List<Definition> definitions = definitionFinder.getDefinitions();
    assertEquals(0, definitions.size());

    // Check that the method call has been rewritten as a static call.
    TypeChecker typeChecker = compiler.getTypeChecker();
    JSType actualType = typeChecker.checkType(root, TypeChecker.Type.TOP);
    FunctionType functionType = (FunctionType) actualType;
    List<JSType> arguments = functionType.getParameters();
    assertEquals("JSCompiler_StaticMethods_accumulate", arguments.get(0).getName());
    assertEquals(JSType.OBJECT, arguments.get(1).getType());
    assertEquals(JSType.ARRAY, arguments.get(2).getType());
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethodsWithDifferentName() throws Exception {
    String input =
        "function A() { }\n" +
            "A.prototype.accumulate = function(a, b) { return a + b };\n" +
            "function B() { }\n" +
            "B.prototype.accumulate = function(a, b) { return a + b };\n" +
            "var c = new A();\n" +
            "var d = new B();\n" +
            "var totalA = c.accumulate(2, 3);\n" +
            "var totalB = d.accumulate(4, 5);\n";

    TestSourceFile sourceFile =
        new TestSourceFile(input, "DevirtualizePrototypeMethodsTest.js");
    CompilationTestHelper helper = new CompilationTestHelper("Test", sourceFile);
    helper.setOptions(options);
    helper.setPassManager(passManager);

    Node externs = helper.parseExterns();
    Node root = helper.parseTopLevelCode();
    passManager.process(externs, root);

    // Check that there is no longer a prototype method definition for either A or B.
    List<Definition> definitions = definitionFinder.getDefinitions();
    assertEquals(0, definitions.size());

    // Check that method calls have been rewritten as static calls with different names.
    TypeChecker typeChecker = compiler.getTypeChecker();
    JSType actualType = typeChecker.checkType(root, TypeChecker.Type.TOP);
    FunctionType functionType = (FunctionType) actualType;
    List<JSType> arguments = functionType.getParameters();
    assertEquals("JSCompiler_StaticMethods_accumulate_A", arguments.get(0).getName());
    assertEquals(JSType.OBJECT, arguments.get(1).getType());
    assertEquals("JSCompiler_StaticMethods_accumulate_B", arguments.get(2).getName());
    assertEquals(JSType.OBJECT, arguments.get(3).getType());
  }
}