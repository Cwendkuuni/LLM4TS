import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {
  private static final String RUNTIME_TYPE_CHECK_JS =
      "js/runtime_type_check.js";

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() throws IOException {
    compiler = new Compiler();
    nodeFactory = NodeFactory.getFactory();
  }

  @After
  public void tearDown() {
    compiler.dispose();
  }

  @Test
  public void testProcess_NoLogFunction() {
    CompilerInput input = new CompilerInput("test.js", "test");
    Node root = Node.newText("function test() {}");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, root);

    Node output = compiler.getCompiledResult();
    Node expectedOutput = NodeUtil.getCompiledCode(
        "test_runtime_type_check_output.js");

    NodeUtil.assertNodeEquals(output, expectedOutput);
  }

  @Test
  public void testProcess_WithLogFunction() {
    CompilerInput input = new CompilerInput("test.js", "test");
    Node root = Node.newText("function test() {}");

    String logFunction =
        "function logWarning(warning, expr) {" +
            "console.warn('Runtime type warning:', warning, expr);" +
            "}";

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);
    runtimeTypeCheck.process(input, root);

    Node output = compiler.getCompiledResult();
    Node expectedOutput = NodeUtil.getCompiledCode(
        "test_runtime_type_check_output_with_log.js");

    NodeUtil.assertNodeEquals(output, expectedOutput);
  }

  @Test
  public void testAddMarkers_SimpleFunction() {
    CompilerInput input = new CompilerInput("test.js", "test");
    Node root = Node.newText("function test() {}");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, root);
    Node output = compiler.getCompiledResult();

    Node functionNode = NodeUtil.getFunctionNode(output, "test");
    Node markerNodes = NodeUtil.getAllChildrenOfType(functionNode, Token.GETPROP);

    assertEquals(2, markerNodes.getNumChildren());

    List<String> markerNames = Arrays.asList(
        "instance_of__test", "implements__IFunction");

    for (int i = 0; i < markerNodes.getNumChildren(); i++) {
      Node markerNode = markerNodes.getChild(i);
      String markerName = markerNode.getFirstChild().getFirstChild()
          .getString();
      assertEquals(markerNames.get(i), markerName);
    }
  }

  @Test
  public void testAddChecks_SimpleFunction() {
    CompilerInput input = new CompilerInput("test.js", "test");
    Node root = Node.newText("function test() {}");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, root);
    Node output = compiler.getCompiledResult();

    Node functionNode = NodeUtil.getFunctionNode(output, "test");
    Node checkNodes = NodeUtil.getAllChildrenOfType(functionNode, Token.CALL);

    assertEquals(2, checkNodes.getNumChildren());

    List<String> checkerNames = Arrays.asList(
        "checkType", "valueChecker('function')");

    for (int i = 0; i < checkNodes.getNumChildren(); i++) {
      Node checkNode = checkNodes.getChild(i);
      String checkerName = checkNode.getFirstChild().getFirstChild()
          .getString();
      assertEquals(checkerNames.get(i), checkerName);
    }
  }

  @Test
  public void testAddChecks_FunctionWithParameters() {
    CompilerInput input = new CompilerInput("test.js", "test");
    Node root = Node.newText("function test(a, b) {}");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, root);
    Node output = compiler.getCompiledResult();

    Node functionNode = NodeUtil.getFunctionNode(output, "test");
    Node checkNodes = NodeUtil.getAllChildrenOfType(functionNode, Token.CALL);

    assertEquals(4, checkNodes.getNumChildren());

    List<String> checkerNames = Arrays.asList(
        "checkType",
        "valueChecker('number')",
        "valueChecker('number')",
        "valueChecker('number')");

    for (int i = 0; i < checkNodes.getNumChildren(); i++) {
      Node checkNode = checkNodes.getChild(i);
      String checkerName = checkNode.getFirstChild().getFirstChild()
          .getString();
      assertEquals(checkerNames.get(i), checkerName);
    }
  }

  @Test
  public void testAddChecks_FunctionWithReturnType() {
    CompilerInput input = new CompilerInput("test.js", "test");
    Node root = Node.newText("function test() {}");

    ObjectType returnType = new ObjectType("String");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, root);
    Node output = compiler.getCompiledResult();

    Node functionNode = NodeUtil.getFunctionNode(output, "test");
    Node checkNodes = NodeUtil.getAllChildrenOfType(functionNode, Token.CALL);

    assertEquals(3, checkNodes.getNumChildren());

    List<String> checkerNames = Arrays.asList(
        "checkType",
        "valueChecker('function')",
        "valueChecker('String')");

    for (int i = 0; i < checkNodes.getNumChildren(); i++) {
      Node checkNode = checkNodes.getChild(i);
      String checkerName = checkNode.getFirstChild().getFirstChild()
          .getString();
      assertEquals(checkerNames.get(i), checkerName);
    }
  }

  @Test
  public void testAddChecks_FunctionWithInterfaceImplementation() {
    CompilerInput input = new CompilerInput("test.js", "test");
    Node root = Node.newText("function test() implements IInterface {}");

    ObjectType interfaceType = new ObjectType("IInterface");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, root);
    Node output = compiler.getCompiledResult();

    Node functionNode = NodeUtil.getFunctionNode(output, "test");
    Node checkNodes = NodeUtil.getAllChildrenOfType(functionNode, Token.CALL);

    assertEquals(4, checkNodes.getNumChildren());

    List<String> checkerNames = Arrays.asList(
        "checkType",
        "valueChecker('function')",
        "interfaceChecker('IInterface')",
        "interfaceChecker('IInterface')");

    for (int i = 0; i < checkNodes.getNumChildren(); i++) {
      Node checkNode = checkNodes.getChild(i);
      String checkerName = checkNode.getFirstChild().getFirstChild()
          .getString();
      assertEquals(checkerNames.get(i), checkerName);
    }
  }

  @Test
  public void testAddChecks_FunctionWithUnionTypeParameter() {
    CompilerInput input = new CompilerInput("test.js", "test");
    Node root = Node.newText(
        "function test(a) {" +
        "  if (a == 1) {" +
        "    return 'one';" +
        "  } else if (a == 2) {" +
        "    return 'two';" +
        "  }}" +
        ");");

    UnionType unionType = new UnionType(Arrays.asList(
        new ObjectType("Number"), new ObjectType("String")));

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, root);
    Node output = compiler.getCompiledResult();

    Node functionNode = NodeUtil.getFunctionNode(output, "test");
    Node checkNodes = NodeUtil.getAllChildrenOfType(functionNode, Token.CALL);

    assertEquals(5, checkNodes.getNumChildren());

    List<String> checkerNames = Arrays.asList(
        "checkType",
        "valueChecker('number')",
        "valueChecker('number')",
        "valueChecker('string')",
        "valueChecker('string')");

    for (int i = 0; i < checkNodes.getNumChildren(); i++) {
      Node checkNode = checkNodes.getChild(i);
      String checkerName = checkNode.getFirstChild().getFirstChild()
          .getString();
      assertEquals(checkerNames.get(i), checkerName);
    }
  }

  @Test
  public void testAddChecks_FunctionWithExternClassImplementation() {
    CompilerInput input = new CompilerInput("test.js", "test");
    Node root = Node.newText("function test() {}");

    ObjectType externClassType = new ObjectType("extern.ExternClass");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, root);
    Node output = compiler.getCompiledResult();

    Node functionNode = NodeUtil.getFunctionNode(output, "test");
    Node checkNodes = NodeUtil.getAllChildrenOfType(functionNode, Token.CALL);

    assertEquals(3, checkNodes.getNumChildren());

    List<String> checkerNames = Arrays.asList(
        "checkType",
        "externClassChecker('extern.ExternClass')",
        "externClassChecker('extern.ExternClass')");

    for (int i = 0; i < checkNodes.getNumChildren(); i++) {
      Node checkNode = checkNodes.getChild(i);
      String checkerName = checkNode.getFirstChild().getFirstChild()
          .getString();
      assertEquals(checkerNames.get(i), checkerName);
    }
  }

  @Test
  public void testAddChecks_FunctionWithExternInterfaceImplementation() {
    CompilerInput input = new CompilerInput("test.js", "test");
    Node root = Node.newText("function test() implements IExternInterface {}");

    ObjectType externInterfaceType = new ObjectType("IExternInterface");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, root);
    Node output = compiler.getCompiledResult();

    Node functionNode = NodeUtil.getFunctionNode(output, "test");
    Node checkNodes = NodeUtil.getAllChildrenOfType(functionNode, Token.CALL);

    assertEquals(4, checkNodes.getNumChildren());

    List<String> checkerNames = Arrays.asList(
        "checkType",
        "valueChecker('function')",
        "interfaceChecker('IExternInterface')",
        "interfaceChecker('IExternInterface')");

    for (int i = 0; i < checkNodes.getNumChildren(); i++) {
      Node checkNode = checkNodes.getChild(i);
      String checkerName = checkNode.getFirstChild().getFirstChild()
          .getString();
      assertEquals(checkerNames.get(i), checkerName);
    }
  }

  @Test
  public void testProcess_ThrowsException() {
    CompilerInput input = new CompilerInput("test.js", "test");
    Node root = Node.newText("throw new Error('Test error');");

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    try {
      runtimeTypeCheck.process(input, root);
      fail("Expected exception to be thrown.");
    } catch (CompilationErrorException e) {
      // Expected
    }
  }
}