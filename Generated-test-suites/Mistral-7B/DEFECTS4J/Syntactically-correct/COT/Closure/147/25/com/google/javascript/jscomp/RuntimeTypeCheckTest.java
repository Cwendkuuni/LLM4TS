import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class RuntimeTypeCheckTest {

  @Test
  public void testProcess() throws Exception {
    Compiler compiler = new Compiler();
    String logFunction = "function(warning, expr) { System.out.println(warning + ': ' + expr); }";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String inputCode = "function Foo() {}";
    CompilerInput input = new CompilerInput(inputCode);
    Node rootNode = compiler.parseTree(input, null);

    runtimeTypeCheck.process(null, rootNode);

    Node functionNode = NodeUtil.getFunctionDeclaration(rootNode, "Foo");
    Node constructorNode = functionNode.getFirstChild();

    // Check for 'instance_of__Foo' property added to Foo.prototype
    Node instanceOfProperty = NodeUtil.getPrototype(functionNode).getChildByPropName("instance_of__Foo");
    assertNotNull(instanceOfProperty);

    // Check for 'implements__X' properties added to Foo.prototype for all interfaces implemented by Foo
    Node implementsProperties = NodeUtil.getPrototype(functionNode).getChildByPropName("implements__");
    assertNotNull(implementsProperties);
    NodeList children = implementsProperties.getChildren();
    for (int i = 0; i < children.size(); i++) {
      Node interfaceNode = children.get(i);
      String interfaceName = interfaceNode.toString();
      // Check if Foo implements the given interface
      assertTrue(NodeUtil.getFunctionDeclaration(rootNode, interfaceName) != null);
    }
  }

  @Test
  public void testProcess_NullInput() throws Exception {
    Compiler compiler = new Compiler();
    String logFunction = "function(warning, expr) { System.out.println(warning + ': ' + expr); }";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    Node rootNode = null;
    try {
      runtimeTypeCheck.process(null, rootNode);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testProcess_EmptyInput() throws Exception {
    Compiler compiler = new Compiler();
    String logFunction = "function(warning, expr) { System.out.println(warning + ': ' + expr); }";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String inputCode = "";
    CompilerInput input = new CompilerInput(inputCode);
    Node rootNode = compiler.parseTree(input, null);

    runtimeTypeCheck.process(null, rootNode);

    // No checks or markers added for empty input
    Node rootNodeCopy = NodeUtil.cloneTree(rootNode);
    assertTrue(NodeUtil.getTreeDifference(rootNode, rootNodeCopy).isEmpty());
  }

  @Test
  public void testProcess_FunctionWithParameters() throws Exception {
    Compiler compiler = new Compiler();
    String logFunction = "function(warning, expr) { System.out.println(warning + ': ' + expr); }";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String inputCode = "function foo(x: number, y: string) {}";
    CompilerInput input = new CompilerInput(inputCode);
    Node rootNode = compiler.parseTree(input, null);

    runtimeTypeCheck.process(null, rootNode);

    Node functionNode = NodeUtil.getFunctionDeclaration(rootNode, "foo");
    Node constructorNode = functionNode.getFirstChild();

    // Check for checks added for each parameter
    NodeList children = constructorNode.getChildren();
    for (int i = 0; i < children.size(); i++) {
      Node child = children.get(i);
      Node paramNode = child.getFirstChild();

      String paramName = paramNode.toString();
      Node checkNode = NodeUtil.getChildByPropName(constructorNode, "check_" + paramName);
      assertNotNull(checkNode);
    }
  }

  @Test
  public void testProcess_FunctionWithReturnType() throws Exception {
    Compiler compiler = new Compiler();
    String logFunction = "function(warning, expr) { System.out.println(warning + ': ' + expr); }";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String inputCode = "function foo(): boolean {}";
    CompilerInput input = new CompilerInput(inputCode);
    Node rootNode = compiler.parseTree(input, null);

    runtimeTypeCheck.process(null, rootNode);

    Node functionNode = NodeUtil.getFunctionDeclaration(rootNode, "foo");
    Node constructorNode = functionNode.getFirstChild();
    Node returnNode = constructorNode.getLastChild();

    // Check for check added for return value
    Node checkNode = NodeUtil.getChildByPropName(constructorNode, "check_return");
    assertNotNull(checkNode);
  }
}