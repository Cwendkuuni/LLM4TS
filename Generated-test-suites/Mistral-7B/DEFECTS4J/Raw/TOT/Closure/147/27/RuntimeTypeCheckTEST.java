import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("types", CompilerOptions.WarningLevel.ERROR);

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);

    SourceFile sourceFile = new SourceFile("test.js", null);
    Node root = NodeFactory.createProgram();
    root.setSource(sourceFile);

    String jsCode = "function Foo() {}";
    Node fooConstructor = NodeFactory.createFunction(
            "Foo", NodeFactory.createFunctionType(null, null, null), null, jsCode);
    root.addChildToBack(fooConstructor);

    Node fooInstance = NodeFactory.newThis();
    Node fooInstanceCall = NodeFactory.createCall(fooConstructor, null, null);
    fooInstanceCall.setThis(fooInstance);
    root.addChildToBack(fooInstanceCall);

    String logFunction = "function log(message) {" +
            "console.log('" + RuntimeTypeCheck.class.getName() + ":' + message);" +
            "}";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);
    runtimeTypeCheck.process(null, root);

    Node checkTypeFunction = NodeUtil.getFunctionByName(root, "checkType");
    Node checkTypeCall = NodeFactory.createCall(checkTypeFunction, fooInstance, null);
    Node result = NodeUtil.getFirstChild(NodeUtil.getFirstChild(checkTypeCall));

    assertEquals("Foo", result.toString());

    Node fooInstanceWrongType = NodeFactory.newNumberLiteral(123);
    checkTypeCall = NodeFactory.createCall(checkTypeFunction, fooInstanceWrongType, null);
    result = NodeUtil.getFirstChild(NodeUtil.getFirstChild(checkTypeCall));

    assertFalse("Expected a type error, but got: " + result, result.isUndefined());

    Node fooInstanceNull = NodeFactory.newNull();
    checkTypeCall = NodeFactory.createCall(checkTypeFunction, fooInstanceNull, null);
    result = NodeUtil.getFirstChild(NodeUtil.getFirstChild(checkTypeCall));

    assertFalse("Expected a type error, but got: " + result, result.isUndefined());

    Node fooInterface = NodeFactory.createFunction(
            "MyInterface", NodeFactory.createFunctionType(null, null, null), null, null);
    fooInterface.setIsInterface(true);
    root.addChildToBack(fooInterface);

    Node fooClassImplementingInterface = NodeFactory.newClass(
            "MyClass", NodeFactory.createFunctionType(null, null, null), null);
    Node fooClassConstructor = NodeFactory.createFunction(
            "MyClass", NodeFactory.createFunctionType(null, null, null), null, null);
    Node fooClassPrototype = NodeFactory.createObjectLiteral();
    Node fooClassPrototypeProperty = NodeFactory.newProperty(
            NodeFactory.newString("implements__MyInterface"), NodeFactory.newBooleanLiteral(true));
    fooClassPrototype.addChildToBack(fooClassPrototypeProperty);
    fooClassConstructor.setPrototype(fooClassPrototype);
    fooClassImplementingInterface.setPrototype(fooClassConstructor);
    root.addChildToBack(fooClassImplementingInterface);

    Node interfaceChecker = NodeUtil.getFunctionByName(root, "interfaceChecker");
    Node fooInstanceCheckInterface = NodeFactory.createCall(
            interfaceChecker, NodeFactory.newQualifiedNameNode("MyClass"), null);

    Node interfaceCheckResult = NodeUtil.getFirstChild(NodeUtil.getFirstChild(fooInstanceCheckInterface));
    assertTrue("Expected true, but got: " + interfaceCheckResult, interfaceCheckResult.isBoolean());
    assertTrue("Expected true, but got: " + interfaceCheckResult, interfaceCheckResult.getBooleanValue());

    Node classChecker = NodeUtil.getFunctionByName(root, "classChecker");
    Node fooInstanceCheckClass = NodeFactory.createCall(
            classChecker, NodeFactory.newQualifiedNameNode("MyClass"), null);

    Node classCheckResult = NodeUtil.getFirstChild(NodeUtil.getFirstChild(fooInstanceCheckClass));
    assertTrue("Expected true, but got: " + classCheckResult, classCheckResult.isBoolean());
    assertTrue("Expected true, but got: " + classCheckResult, classCheckResult.getBooleanValue());

    Node constructorChecker = NodeUtil.getFunctionByName(root, "constructorChecker");
    Node fooInstanceCheckConstructor = NodeFactory.createCall(
            constructorChecker, fooInstance, NodeFactory.newQualifiedNameNode("Foo"), null);

    Node constructorCheckResult = NodeUtil.getFirstChild(NodeUtil.getFirstChild(fooInstanceCheckConstructor));
    assertTrue("Expected true, but got: " + constructorCheckResult, constructorCheckResult.isBoolean());
    assertTrue("Expected true, but got: " + constructorCheckResult, constructorCheckResult.getBooleanValue());

    Node fooInstanceCheckInterfaceWrong = NodeFactory.newQualifiedNameNode("MyWrongInterface");
    Node fooInstanceCheckInterfaceWrongCall = NodeFactory.createCall(
            interfaceChecker, NodeFactory.newQualifiedNameNode("MyClass"), null);
    Node fooInstanceCheckInterfaceWrongResult = NodeUtil.getFirstChild(NodeUtil.getFirstChild(fooInstanceCheckInterfaceWrongCall));

    assertFalse("Expected false, but got: " + fooInstanceCheckInterfaceWrongResult, fooInstanceCheckInterfaceWrongResult.isBoolean());

    Node fooInstanceCheckClassWrong = NodeFactory.newQualifiedNameNode("MyWrongClass");
    Node fooInstanceCheckClassWrongCall = NodeFactory.createCall(
            classChecker, NodeFactory.newQualifiedNameNode("MyClass"), null);
    Node fooInstanceCheckClassWrongResult = NodeUtil.getFirstChild(NodeUtil.getFirstChild(fooInstanceCheckClassWrongCall));

    assertFalse("Expected false, but got: " + fooInstanceCheckClassWrongResult, fooInstanceCheckClassWrongResult.isBoolean());

    Node fooInstanceCheckConstructorWrong = NodeFactory.newQualifiedNameNode("MyOtherClass");
    Node fooInstanceCheckConstructorWrongCall = NodeFactory.createCall(
            constructorChecker, fooInstance, NodeFactory.newQualifiedNameNode("MyClass"), null);
    Node fooInstanceCheckConstructorWrongResult = NodeUtil.getFirstChild(NodeUtil.getFirstChild(fooInstanceCheckConstructorWrongCall));

    assertFalse("Expected false, but got: " + fooInstanceCheckConstructorWrongResult, fooInstanceCheckConstructorWrongResult.isBoolean());
  }
}