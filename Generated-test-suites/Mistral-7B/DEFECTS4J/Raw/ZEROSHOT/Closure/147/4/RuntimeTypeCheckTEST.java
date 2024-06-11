import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.newBinaryOp;
import static com.google.javascript.rhino.NodeFactory.newCallExpr;
import static com.google.javascript.rhino.NodeFactory.newIdentifier;
import static com.google.javascript.rhino.NodeFactory.newString;
import static com.google.javascript.rhino.NodeFactory.newThis;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerTest;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSTypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test class for {@code RuntimeTypeCheck} class.
 */
public class RuntimeTypeCheckTest {

  private static Node getTestData() throws IOException {
    String sourceCode = "function Foo() {}\n" +
        "function Bar() {\n" +
        "  this.x = 1;\n" +
        "}\n" +
        "Bar.prototype.bar = function() {\n" +
        "  return this.x;\n" +
        "}";

    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile(sourceCode);
    Node root = compiler.getRootNode();
    return root;
  }

  @SuppressWarnings("unchecked")
  private static void checkTypeInserted(FunctionType funcType, int paramIndex) {
    FunctionType funcTypeAfter = (FunctionType) ((Node) funcType.getParent())
        .getJSType();

    Node checkerCallNode = null;
    for (Node child : funcTypeAfter.getBody().getChildren()) {
      if (child.getType() == Token.CALL) {
        Node callee = child.getFirstChild();
        if (callee.getType() == Token.IDENT &&
            callee.toString().equals("checkType")) {
          checkerCallNode = child;
          break;
        }
      }
    }

    assertNotNull(checkerCallNode);

    Node checkerArgsNode = checkerCallNode.getLastChild();
    List<Node> checkerArgs = new ArrayList<>();
    while (checkerArgsNode.getType() == Token.ARRAYLIT) {
      checkerArgs.addAll(checkerArgsNode.getChildren());
      checkerArgsNode = checkerArgsNode.getPreviousSibling();
    }

    assertEquals(2, checkerArgs.size());

    Node checkerArg0 = checkerArgs.get(0);
    assertEquals(checkerArg0.getType(), Token.IDENT);
    assertEquals(checkerArg0.toString(), "expr");

    Node checkerArg1 = checkerArgs.get(1);
    List<Node> checkerArgs1Children = new ArrayList<>();
    if (checkerArg1.getType() == Token.CALL) {
      Node callee1 = checkerArg1.getFirstChild();
      if (callee1.getType() == Token.IDENT &&
          callee1.toString().equals("valueChecker")) {
        checkerArgs1Children.addAll(callee1.getChildren());
      }
    } else {
      checkerArgs1Children.add(checkerArg1);
    }

    assertEquals(1, checkerArgs1Children.size());

    Node checkerArg1Value = checkerArgs1Children.get(0);
    assertEquals(checkerArg1Value.getType(), Token.STRING);
    assertEquals(funcType.getParameter(paramIndex).getName(),
        checkerArg1Value.toString());
  }

  @Test
  public void testInsertCheckForPrimitiveTypes() throws Exception {
    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile("function foo(x) {}");
    Node root = compiler.getRootNode();
    FunctionType fooType = (FunctionType) root.getFirstChild();

    checkTypeInserted(fooType, 0);
  }

  @Test
  public void testInsertCheckForClassType() throws Exception {
    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile(getTestData().toString());
    Node root = compiler.getRootNode();
    FunctionType barType = (FunctionType) NodeUtil.getFunctionByName(root, "Bar")
        .getJSType();

    checkTypeInserted(barType, 0);
  }

  @Test
  public void testInsertCheckForInterfaceType() throws Exception {
    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile(getTestData().toString());
    Node root = compiler.getRootNode();
    FunctionType fooType = (FunctionType) root.getFirstChild();

    // Foo is not an interface, but Bar is.
    FunctionType barType = (FunctionType) NodeUtil.getFunctionByName(root, "Bar")
        .getJSType();
    Node barPrototype = NodeUtil.getFunctionByName(root, "Bar")
        .getLastChild();

    checkTypeInserted(barPrototype.getJSType(), 0);
  }

  @Test
  public void testInsertCheckForFunctionType() throws Exception {
    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile(getTestData().toString());
    Node root = compiler.getRootNode();
    FunctionType barType = (FunctionType) NodeUtil.getFunctionByName(root, "Bar")
        .getJSType();

    FunctionType barBarType = (FunctionType) NodeUtil.getFunctionByName(root,
        "Bar.bar").getJSType();

    checkTypeInserted(barBarType, 0);
  }

  @Test
  public void testInsertCheckForNullType() throws Exception {
    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile("function foo(x) {}");
    Node root = compiler.getRootNode();
    FunctionType fooType = (FunctionType) root.getFirstChild();

    Node nullChecker = newIdentifier("nullChecker");
    FunctionType nullCheckerType = new JSTypeFactory()
        .makeFunctionType(nullChecker, ImmutableSet.<JSType>of(),
            new JSTypeFactory().makeNullType());

    Node nullExpr = newIdentifier("nullExpr");
    Node nullCheckNode = newCallExpr(
        newIdentifier("checkType"),
        newArrayLiteral(newIdentifier("expr"), nullCheckerType, nullChecker,
            newIdentifier(nullExpr)));

    Node insertionPoint = NodeUtil.getFunctionByName(root, "foo")
        .getLastChild().getPreviousSibling();

    root.addChildAfter(nullCheckNode, insertionPoint);

    CompilerPassManager passManager = new CompilerPassManager(compiler)
        .addPass(new RuntimeTypeCheck(compiler, null));
    passManager.runPasses();

    FunctionType fooTypeAfter = (FunctionType) NodeUtil.getFunctionByName(root,
        "foo").getJSType();

    checkTypeInserted(fooTypeAfter, 0);
  }

  @Test
  public void testInsertCheckForUnionType() throws Exception {
    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile(getTestData().toString());
    Node root = compiler.getRootNode();
    FunctionType barType = (FunctionType) NodeUtil.getFunctionByName(root, "Bar")
        .getJSType();

    // Union type: number | Foo
    JSType unionType = new JSTypeFactory().makeUnionType(
        new JSTypeFactory().makeNumberType(), new ObjectType(new JSTypeFactory()
            .makeReferenceType("Foo")));

    Node unionChecker = newIdentifier("unionChecker");
    FunctionType unionCheckerType = new JSTypeFactory()
        .makeFunctionType(unionChecker, ImmutableSet.<JSType>of(), unionType);

    Node unionExpr = newIdentifier("unionExpr");
    Node unionCheckNode = newCallExpr(
        newIdentifier("checkType"),
        newArrayLiteral(newIdentifier("expr"), unionCheckerType, unionChecker,
            newIdentifier(unionExpr)));

    Node insertionPoint = NodeUtil.getFunctionByName(root, "Bar")
        .getLastChild().getPreviousSibling();

    root.addChildAfter(unionCheckNode, insertionPoint);

    CompilerPassManager passManager = new CompilerPassManager(compiler)
        .addPass(new RuntimeTypeCheck(compiler, null));
    passManager.runPasses();

    FunctionType barTypeAfter = (FunctionType) NodeUtil.getFunctionByName(root,
        "Bar").getJSType();

    checkTypeInserted(barTypeAfter, 0);
  }

  @Test
  public void testLogFunction() throws Exception {
    String logFunctionSourceCode = "function log(warning, expr) {\n" +
        "  console.error(warning + ': ' + expr.toString());\n" +
        "}";

    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile(getTestData().toString());
    Node root = compiler.getRootNode();

    CompilerPassManager passManager = new CompilerPassManager(compiler)
        .addPass(new RuntimeTypeCheck(compiler, logFunctionSourceCode));
    passManager.runPasses();

    FunctionType fooType = (FunctionType) root.getFirstChild();
    FunctionType fooTypeAfter = (FunctionType) NodeUtil.getFunctionByName(root,
        "foo").getJSType();

    assertEquals(fooType, fooTypeAfter);

    Node fooFunction = NodeUtil.getFunctionByName(root, "foo");

    Node logCall = NodeUtil.getFirstChildOfType(fooFunction, Token.CALL);

    Node warning = logCall.getLastChild();
    Node expr = logCall.getLastChild().getPreviousSibling();

    assertEquals(newString("foo: function () {}"), warning);
    assertEquals(fooType.getParameter(0), expr.getType());
  }

  @Test
  public void testNoCheckForUndeclaredType() throws Exception {
    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile(getTestData().toString());
    Node root = compiler.getRootNode();
    FunctionType barType = (FunctionType) NodeUtil.getFunctionByName(root, "Bar")
        .getJSType();

    FunctionType barTypeAfter = (FunctionType) NodeUtil.getFunctionByName(root,
        "Bar").getJSType();

    assertEquals(barType, barTypeAfter);
  }

  @Test
  public void testNoCheckForUndefinedType() throws Exception {
    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile(getTestData().toString());
    Node root = compiler.getRootNode();
    FunctionType barType = (FunctionType) NodeUtil.getFunctionByName(root, "Bar")
        .getJSType();

    Node barPrototype = NodeUtil.getFunctionByName(root, "Bar")
        .getLastChild();

    barPrototype.setJSType(null);

    FunctionType barTypeAfter = (FunctionType) NodeUtil.getFunctionByName(root,
        "Bar").getJSType();

    assertEquals(barType, barTypeAfter);
  }

  @Test
  public void testNoCheckForExternClassType() throws Exception {
    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile("class Foo {}\n" +
            "/** @type {!Foo} */ var foo = new Foo();");
    Node root = compiler.getRootNode();
    FunctionType fooType = (FunctionType) NodeUtil.getFunctionByName(root,
        "Object").getParameter(0).getJSType();

    FunctionType fooTypeAfter = (FunctionType) NodeUtil.getFunctionByName(root,
        "Object").getParameter(0).getJSType();

    assertEquals(fooType, fooTypeAfter);
  }

  @Test
  public void testNoCheckForExternInterfaceType() throws Exception {
    AbstractCompiler compiler = new CompilerOptions().setSourceFile("test.js")
        .setPrintStack(false).compile("class Foo implements Bar {}");
    Node root = compiler.getRootNode();
    FunctionType fooType = (FunctionType) NodeUtil.getFunctionByName(root, "Foo")
        .getJSType();

    FunctionType fooTypeAfter = (FunctionType) NodeUtil.getFunctionByName(root,
        "Foo").getJSType();

    assertEquals(fooType, fooTypeAfter);
  }
}