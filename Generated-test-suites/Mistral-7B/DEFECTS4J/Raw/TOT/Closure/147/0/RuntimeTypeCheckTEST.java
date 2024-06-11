import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.CompilerInput.*;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;

public class RuntimeTypeCheckTest {

  public static void main(String[] args) throws Exception {
    testTypeChecking(
        new Compiler() {
          @Override
          public CompilerOptions getOptions() {
            return new CompilerOptions();
          }
        },
        new String[]{},
        "test.js");
  }

  private static void testTypeChecking(Compiler compiler,
                                      String[] args,
                                      String inputFile) throws Exception {

    String input = getInputFromResource("/" + inputFile);
    CompilerInput inputInput = makeInput(inputFile, input);
    Node externs = Node.externalInput(inputInput);

    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node root = Node.parseCompilationUnit(input);
    runtimeTypeCheck.process(externs, root);

    Node js = getBoilerplateCode(compiler, null);
    Node expectedOutput = Node.parseCompilationUnit(
        CharStreams.toString(new InputStreamReader(
            RuntimeTypeCheckTest.class.getResourceAsStream(
                "expected_output.js"), Charsets.UTF_8)));

    Node actualOutput = Normalize.normalize(
        NodeUtil.getFunctionBody(
            NodeUtil.getFunctionByName(root, "checkType"), root));

    Node differ = compareTrees(expectedOutput, actualOutput);
    if (differ != null) {
      throw new RuntimeException("Unexpected output:\n" +
          NodeUtil.getSourceCode(differ));
    }

    // Test individual type checks
    Node checkTypeCall = NodeUtil.getFunctionByName(root, "checkType");
    testCheckValueType(compiler, checkTypeCall, "string");
    testCheckValueType(compiler, checkTypeCall, "number");
    testCheckValueType(compiler, checkTypeCall, "boolean");
    testCheckValueType(compiler, checkTypeCall, "null");
    testCheckValueType(compiler, checkTypeCall, "undefined");
    testCheckValueType(compiler, checkTypeCall, "void");
    testCheckClassType(compiler, checkTypeCall, "Foo");
    testCheckInterfaceType(compiler, checkTypeCall, "Bar");
    testCheckClassWithInterfaceType(compiler, checkTypeCall, "Baz");
    testCheckUnionType(compiler, checkTypeCall, "string|number");

    // Test error cases
    testCheckUnknownType(compiler, checkTypeCall);
    testCheckCheckerIsNull(compiler, checkTypeCall);
    testCheckCheckerDoesNotMatch(compiler, checkTypeCall);
    testCheckCheckerThrows(compiler, checkTypeCall);
  }

  private static void testCheckValueType(Compiler compiler,
                                        Node checkTypeCall,
                                        String valueType) throws Exception {
    String input = getInputFromResource("/test_value_type_" + valueType + ".js");
    CompilerInput inputInput = makeInput("test_value_type_" + valueType + ".js", input);
    Node root = Node.parseCompilationUnit(input);
    Node expectedOutput = Node.parseCompilationUnit(
        CharStreams.toString(new InputStreamReader(
            RuntimeTypeCheckTest.class.getResourceAsStream(
                "expected_output_value_type_" + valueType + ".js"),
            Charsets.UTF_8)));

    Node result = testCheckType(compiler, checkTypeCall, inputInput, root);
    Node differ = compareTrees(expectedOutput, result);
    if (differ != null) {
      throw new RuntimeException("Unexpected output for value type testing:\n" +
          NodeUtil.getSourceCode(differ));
    }
  }

  private static void testCheckClassType(Compiler compiler,
                                        Node checkTypeCall,
                                        String className) throws Exception {
    String input = getInputFromResource("/test_class_type_" + className + ".js");
    CompilerInput inputInput = makeInput("test_class_type_" + className + ".js", input);
    Node root = Node.parseCompilationUnit(input);
    Node expectedOutput = Node.parseCompilationUnit(
        CharStreams.toString(new InputStreamReader(
            RuntimeTypeCheckTest.class.getResourceAsStream(
                "expected_output_class_type_" + className + ".js"),
            Charsets.UTF_8)));

    Node result = testCheckType(compiler, checkTypeCall, inputInput, root);
    Node differ = compareTrees(expectedOutput, result);
    if (differ != null) {
      throw new RuntimeException("Unexpected output for class type testing:\n" +
          NodeUtil.getSourceCode(differ));
    }
  }

  private static void testCheckInterfaceType(Compiler compiler,
                                            Node checkTypeCall,
                                            String interfaceName) throws Exception {
    String input = getInputFromResource("/test_interface_type_" + interfaceName + ".js");
    CompilerInput inputInput = makeInput("test_interface_type_" + interfaceName +".js", input);
    Node root = Node.parseCompilationUnit(input);
    Node expectedOutput = Node.parseCompilationUnit(
        CharStreams.toString(new InputStreamReader(
            RuntimeTypeCheckTest.class.getResourceAsStream(
                "expected_output_interface_type_" + interfaceName + ".js"),
            Charsets.UTF_8)));

    Node result = testCheckType(compiler, checkTypeCall, inputInput, root);
    Node differ = compareTrees(expectedOutput, result);
    if (differ != null) {
      throw new RuntimeException("Unexpected output for interface type testing:\n" +
          NodeUtil.getSourceCode(differ));
    }
  }

  private static void testCheckClassWithInterfaceType(Compiler compiler,
                                                    Node checkTypeCall,
                                                    String className) throws Exception {
    String input = getInputFromResource("/test_class_with_interface_type_" + className + ".js");
    CompilerInput inputInput = makeInput("test_class_with_interface_type_" + className + ".js", input);
    Node root = Node.parseCompilationUnit(input);
    Node expectedOutput = Node.parseCompilationUnit(
        CharStreams.toString(new InputStreamReader(
            RuntimeTypeCheckTest.class.getResourceAsStream(
                "expected_output_class_with_interface_type_" + className + ".js"),
            Charsets.UTF_8)));

    Node result = testCheckType(compiler, checkTypeCall, inputInput, root);
    Node differ = compareTrees(expectedOutput, result);
    if (differ != null) {
      throw new RuntimeException("Unexpected output for class with interface type testing:\n" +
          NodeUtil.getSourceCode(differ));
    }
  }

  private static void testCheckUnionType(Compiler compiler,
                                        Node checkTypeCall,
                                        String unionType) throws Exception {
    String input = getInputFromResource("/test_union_type_" + unionType + ".js");
    CompilerInput inputInput = makeInput("test_union_type_" + unionType + ".js", input);
    Node root = Node.parseCompilationUnit(input);
    Node expectedOutput = Node.parseCompilationUnit(
        CharStreams.toString(new InputStreamReader(
            RuntimeTypeCheckTest.class.getResourceAsStream(
                "expected_output_union_type_" + unionType + ".js"),
            Charsets.UTF_8)));

    Node result = testCheckType(compiler, checkTypeCall, inputInput, root);
    Node differ = compareTrees(expectedOutput, result);
    if (differ != null) {
      throw new RuntimeException("Unexpected output for union type testing:\n" +
          NodeUtil.getSourceCode(differ));
    }
  }

  private static Node testCheckType(Compiler compiler,
                                   Node checkTypeCall,
                                   CompilerInput inputInput,
                                   Node root) throws Exception {
    compiler.compile(ImmutableList.of(inputInput), root);
    Node output = NodeUtil.getFunctionBody(compiler.getOutput(), root);
    return output;
  }

  private static void testCheckUnknownType(Compiler compiler,
                                          Node checkTypeCall) {
    CompilerInput inputInput = makeInput("test_unknown_type.js", "");
    Node root = Node.parseCompilationUnit("function test() {}");
    testCheckType(compiler, checkTypeCall, inputInput, root);
  }

  private static void testCheckCheckerIsNull(Compiler compiler,
                                            Node checkTypeCall) {
    CompilerInput inputInput = makeInput("test_check_null.js", "");
    Node root = Node.parseCompilationUnit("function test() {}");
    Node checker = Node.newNull();
    Node checkTypeCallNode = new Node(Token.CALL, checkTypeCall, checker);
    Node result = testCheckType(compiler, checkTypeCallNode, inputInput, root);
  }

  private static void testCheckCheckerDoesNotMatch(Compiler compiler,
                                                Node checkTypeCall) {
    CompilerInput inputInput = makeInput("test_check_does_not_match.js", "");
    Node root = Node.parseCompilationUnit("function test() {}");
    Node checker = Node.newString("Foo");
    Node checkTypeCallNode = new Node(Token.CALL, checkTypeCall, checker);
    Node result = testCheckType(compiler, checkTypeCallNode, inputInput, root);
  }

  private static void testCheckCheckerThrows(Compiler compiler,
                                          Node checkTypeCall) {
    CompilerInput inputInput = makeInput("test_check_throws.js", "");
    Node root = Node.parseCompilationUnit("function test() {" +
        "  checkType(function() {}, function() { throw new Error(); });" +
        "}");
    Node checkTypeCallNode = NodeUtil.getFunctionByName(root, "checkType");
    Node result = testCheckType(compiler, checkTypeCallNode, inputInput, root);

    Node thrownError = NodeUtil.getFirstChildOfType(
        result, Token.THROW);
    if (thrownError == null) {
      throw new RuntimeException("Expected an error to be thrown");
    }
  }

  private static String getInputFromResource(String path) {
    return CharStreams.toString(RuntimeTypeCheckTest.class.getResourceAsStream(path));
  }

  private static CompilerInput makeInput(String name, String code) {
    return new CompilerInput(name, code, null, null, null);
  }
}