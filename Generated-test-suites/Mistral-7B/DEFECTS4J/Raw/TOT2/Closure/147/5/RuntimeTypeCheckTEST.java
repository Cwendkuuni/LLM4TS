import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeTraversal;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() throws Exception {
    String testSource = "function Foo() {} class Bar extends Foo {} function test() {" +
        "class A {} function foo(a: A) {} function bar(x: number) {" +
        "  if (x instanceof Foo) {" +
        "    foo(new A());" +
        "  } else {" +
        "    foo(42);" +
        "  }}" +
        "} test();";

    Compiler compiler = new Compiler();
    Node rootNode = NodeUtil.parseJs(testSource);

    CompilerPass runtimeTypeCheckPass = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheckPass.process(null, rootNode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);
    nodeTraversal.traverse(new NodeAssertions(compiler));
  }

  private static class NodeAssertions
      extends NodeTraversal.AbstractPostOrderCallback {

    private final Compiler compiler;

    private NodeAssertions(Compiler compiler) {
      this.compiler = compiler;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (NodeUtil.isFunction(n)) {
        FunctionType funType = (FunctionType) n.getJSType();
        if (funType.getName().equals("test")) {
          assertFalse("Expected test function to throw an error",
              compiler.getDiagnostics().hasError());
          assertTrue("Expected test function to be called",
              compiler.getRunnableCode().contains("test()"));
          return;
        }
      }

      if (NodeUtil.isBlock(n)) {
        Node checkTypeCall = null;
        for (int i = 0; i < n.getChildCount(); i++) {
          Node child = n.getChild(i);
          if (NodeUtil.isCall(child) &&
              child.getFirstChild().getJSType().getName().equals("checkType")) {
            checkTypeCall = child;
            break;
          }
        }

        if (checkTypeCall != null) {
          Node[] arguments = checkTypeCall.getArguments();
          assertNull("Expected no argument for null checker",
              arguments[0]);
          JSType type = NodeUtil.getJSType(arguments[1]);
          if (type.isNullType()) {
            assertTrue("Expected null check to pass",
                checkTypeCall.getChild(0).getFirstChild().isNull());
          } else {
            String typeName = NodeUtil.getTypeName(type);
            switch(typeName) {
              case "Foo":
                assertInstanceOf("Expected Foo instance",
                    checkTypeCall.getChild(0).getFirstChild(), Foo.class);
                break;
              case "A":
                assertInstanceOf("Expected A instance",
                    checkTypeCall.getChild(0).getFirstChild(), A.class);
                break;
              case "number":
                assertEquals(42.0, checkTypeCall.getChild(0).getFirstChild()
                    .getDoubleValueExact());
                break;
              default:
                fail("Unexpected type: " + typeName);
            }
          }
        }
      }
    }
  }
}