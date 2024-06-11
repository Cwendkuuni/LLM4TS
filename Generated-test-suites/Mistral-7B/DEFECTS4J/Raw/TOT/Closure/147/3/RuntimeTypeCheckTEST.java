import static com.google.javascript.jscomp.NodeUtil.getFunctionName;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeFactory;

import java.util.Arrays;

public class RuntimeTypeCheckTest {

  private static CompilerPass createCompilerPass() {
    return new RuntimeTypeCheck(new CompilerPass() {}, null);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testSimpleChecks() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = createCompilerPass();

    String sourceCode = "function Foo() {}";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");
    Node root = NodeFactory.createFromString(sourceCode);

    compiler.compile(Arrays.asList(input), root);
    runtimeTypeCheck.process(NodeUtil.getExterns(root), root);
    Node result = compiler.getProcessedSource();

    FunctionType fooType = (FunctionType) NodeUtil.getFunctionType(result, "Foo");

    Node fooInstance = NodeUtil.newQualifiedNameNode("Foo", -1, -1);
    Node fooChecker = addCheckers(runtimeTypeCheck, fooInstance);
    assertTrue(fooChecker.isTrue());

    Object obj = new Object();
    Node objChecker = addCheckers(runtimeTypeCheck, NodeUtil.newQualifiedNameNode(
        "Object", -1, -1));
    assertFalse(objChecker.isTrue());

    fooInstance.setNext(obj);
    Node rootForCheck = NodeUtil.getFunctionDeclaration(result, "test");
    runtimeTypeCheck.process(NodeUtil.getExterns(result), rootForCheck);
    result = compiler.getProcessedSource();

    Node fooInstanceAndObj = NodeUtil.getFunctionCall(
        result, "test", NodeFactory.newArrayLiteral(fooInstance, obj));

    Node checkTypeCall = NodeUtil.getFunctionCall(result, "checkType",
        fooInstanceAndObj, fooChecker);
    assertTrue(checkTypeCall.isTrue());

    Node functionTypeNode = NodeUtil.getFunctionType(result, "g");
    assertNull(functionTypeNode);
  }

  private Node addCheckers(CompilerPass pass, Node node) {
    Passes passes = new Passes(pass);
    pass.process(NodeUtil.getExterns(NodeUtil.getFunctionDeclaration(
        pass.getProcessedSource(), "test")), NodeUtil.getFunctionDeclaration(
        pass.getProcessedSource(), "test"));
    Node root = pass.getProcessedSource();
    passes.addCheckersToNode(node);
    return NodeUtil.getFunctionCall(root, "checkType", node,
        passes.getTypeChecker(node));
  }

  @org.junit.Test
  public void testUnionTypes() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = createCompilerPass();

    String sourceCode = "function Foo() {} function Bar() {} function FooOrBar() {}";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");
    Node root = NodeFactory.createFromString(sourceCode);

    compiler.compile(Arrays.asList(input), root);
    runtimeTypeCheck.process(NodeUtil.getExterns(root), root);

    Node fooOrBarInstance = NodeUtil.newQualifiedNameNode("FooOrBar", -1, -1);
    Node fooChecker = addCheckers(runtimeTypeCheck, fooOrBarInstance);

    Node foo = NodeUtil.newQualifiedNameNode("Foo", -1, -1);
    Node bar = NodeUtil.newQualifiedNameNode("Bar", -1, -1);

    Node fooCheckerResult = NodeUtil.getFunctionCall(
        runtimeTypeCheck.getProcessedSource(), "checkType",
        fooOrBarInstance, fooChecker);
    assertTrue(fooCheckerResult.isTrue());

    Node barCheckerResult = NodeUtil.getFunctionCall(
        runtimeTypeCheck.getProcessedSource(), "checkType",
        fooOrBarInstance, bar);
    assertTrue(barCheckerResult.isTrue());
  }

  @org.junit.Test
  public void testNullCheck() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = createCompilerPass();

    String sourceCode = "function Foo() {} Foo.prototype.bar = null;";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");
    Node root = NodeFactory.createFromString(sourceCode);

    compiler.compile(Arrays.asList(input), root);
    runtimeTypeCheck.process(NodeUtil.getExterns(root), root);

    Node fooInstance = NodeUtil.newQualifiedNameNode("Foo", -1, -1);
    Node nullChecker = NodeUtil.getFunctionCall(runtimeTypeCheck.getProcessedSource(),
        "jscomp.typecheck.checkType", fooInstance, NodeFactory.newNull());

    assertFalse(nullChecker.isTrue());
  }

  @org.junit.Test
  public void testArrayCheck() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = createCompilerPass();

    String sourceCode = "function Foo() {} function FooArray() { this.push.apply(this, arguments); }";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");
    Node root = NodeFactory.createFromString(sourceCode);

    compiler.compile(Arrays.asList(input), root);
    runtimeTypeCheck.process(NodeUtil.getExterns(root), root);

    Node fooInstance = NodeUtil.newQualifiedNameNode("Foo", -1, -1);
    Node fooArray = NodeUtil.newQualifiedNameNode("FooArray", -1, -1);

    Node fooChecker = addCheckers(runtimeTypeCheck, fooInstance);
    Node fooArrayChecker = addCheckers(runtimeTypeCheck, fooArray);

    Node fooInstanceAndArray = NodeUtil.newQualifiedNameNode("FooArray", -1, -1);
    Node fooCheckerResult = NodeUtil.getFunctionCall(
        runtimeTypeCheck.getProcessedSource(), "checkType",
        fooInstanceAndArray, fooChecker);
    assertTrue(fooCheckerResult.isTrue());

    Node arrayCheckerResult = NodeUtil.getFunctionCall(
        runtimeTypeCheck.getProcessedSource(), "checkType",
        fooInstanceAndArray, fooArrayChecker);
    assertTrue(arrayCheckerResult.isTrue());

    Node fooArrayWithString = Node.newArrayLiteral(fooInstance, "foo");
    Node fooArrayWithStringChecker = addCheckers(runtimeTypeCheck, fooArrayWithString);

    Node fooArrayWithStringCheckerResult = NodeUtil.getFunctionCall(
        runtimeTypeCheck.getProcessedSource(), "checkType",
        fooArrayWithString, fooArrayChecker);
    assertTrue(fooArrayWithStringCheckerResult.isTrue());

    Node fooArrayWithStringCheckerResult2 = NodeUtil.getFunctionCall(
        runtimeTypeCheck.getProcessedSource(), "checkType",
        fooArrayWithString, fooArrayWithStringChecker);
    assertTrue(fooArrayWithStringCheckerResult2.isTrue());

    Node fooArrayWithNumber = Node.newArrayLiteral(fooInstance, 1);
    Node fooArrayWithNumberChecker = addCheckers(runtimeTypeCheck, fooArrayWithNumber);

    Node fooArrayWithNumberCheckerResult = NodeUtil.getFunctionCall(
        runtimeTypeCheck.getProcessedSource(), "checkType",
        fooArrayWithNumber, fooArrayChecker);
    assertTrue(fooArrayWithNumberCheckerResult.isTrue());

    Node fooArrayWithNumberCheckerResult2 = NodeUtil.getFunctionCall(
        runtimeTypeCheck.getProcessedSource(), "checkType",
        fooArrayWithNumber, fooArrayWithNumberChecker);
    assertTrue(fooArrayWithNumberCheckerResult2.isTrue());
  }

  @org.junit.Test
  public void testInterfaceCheck() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = createCompilerPass();

    String sourceCode = "interface Foo { qux(); } function FooImpl() {} FooImpl.prototype.qux = function() {};";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");
    Node root = NodeFactory.createFromString(sourceCode);

    compiler.compile(Arrays.asList(input), root);
    runtimeTypeCheck.process(NodeUtil.getExterns(root), root);

    Node fooImplInstance = NodeUtil.newQualifiedNameNode("FooImpl", -1, -1);
    Node fooChecker = addCheckers(runtimeTypeCheck, fooImplInstance);

    Node fooCheckerResult = NodeUtil.getFunctionCall(runtimeTypeCheck.getProcessedSource(),
        "checkType", fooImplInstance, fooChecker);
    assertTrue(fooCheckerResult.isTrue());

    Node fooQuxChecker = addCheckers(runtimeTypeCheck, fooImplInstance);
    Node fooQuxCheckerResult = NodeUtil.getFunctionCall(
        runtimeTypeCheck.getProcessedSource(), "checkType",
        fooImplInstance, fooQuxChecker);
    assertTrue(fooQuxCheckerResult.isTrue());

    Node fooQuxCheckerResult2 = NodeUtil.getFunctionCall(
        runtimeTypeCheck.getProcessedSource(), "checkType",
        fooImplInstance, fooChecker);
    assertTrue(fooQuxCheckerResult2.isTrue());
  }

  @org.junit.Test
  public void testExternClassCheck() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = createCompilerPass();

    String sourceCode = "extern class Foo {}";
    CompilerInput input = new CompilerInput(sourceCode, "test.js");
    Node root = NodeFactory.createFromString(sourceCode);

    compiler.compile(Arrays.asList(input), root);
    runtimeTypeCheck.process(NodeUtil.getExterns(root), root);

    Node fooInstance = NodeUtil.newQualifiedNameNode("Foo", -1, -1);
    Node fooChecker = addCheckers(runtimeTypeCheck, fooInstance);

    Node fooCheckerResult = NodeUtil.getFunctionCall(runtimeTypeCheck.getProcessedSource(),
        "checkType", fooInstance, fooChecker);
    assertTrue(fooCheckerResult.isTrue());
  }
}