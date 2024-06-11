import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.getTestCompiler;
import static com.google.javascript.jscomp.CompilerOptions.Option.CHECK_TYPES;
import static com.google.javascript.jscomp.CompilerOptions.Option.CROSS_MODULE_METHOD_MOTION;
import static com.google.javascript.jscomp.CompilerOptions.Option.WARNINGS;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.NodeTraversingCompiler;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CrossModuleMethodMotionTest {

  @org.junit.Test
  public void testCrossModuleMethodMotion() {
    // Set up the test compiler
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setOption(CHECK_TYPES, false);
    options.setOption(WARNINGS, false);
    options.setOption(CROSS_MODULE_METHOD_MOTION, true);

    Compiler compiler = getTestCompiler(options);
    Node rootNode = compiler.parseJavaScript("", "", null);

    // Create a dummy class with prototype methods that can be moved
    Node classNode = compiler.parseJavaScript(
        "class Dummy {\n" +
        "  constructor() {\n" +
        "  }\n" +
        "  function staticMethod() {\n" +
        "  }\n" +
        "  function prototypeMethod() {\n" +
        "  }\n" +
        "}",
        "Dummy",
        rootNode);

    // Create a module that references the prototype method
    Node moduleNode = compiler.parseJavaScript(
        "class Dependent {\n" +
        "  constructor() {\n" +
        "    Dummy.prototype.prototypeMethod.call(this);\n" +
        "  }\n" +
        "}",
        "Dependent",
        rootNode);

    // Add dependencies between the modules
    compiler.getModuleGraph().addDependency(
        compiler.getModule(classNode.getSourceName()),
        compiler.getModule(moduleNode.getSourceName()));

    // Run the compiler pass
    NodeTraversingCompiler traversingCompiler = new NodeTraversingCompiler(compiler);
    traversingCompiler.setPassManager(new CrossModuleMethodMotion(
        traversingCompiler, new CrossModuleMethodMotion.IdGenerator(), true));
    traversingCompiler.process(rootNode, rootNode);

    // Check that the prototype method was moved to the dependent module
    Node prototypeMethodCall = null;
    for (Node child = moduleNode; child != null; child = child.getNext()) {
      if (child.isCall()) {
        Node callee = child.getFirstChild();
        if (callee.isGetprop() && callee.getPropertyName().getText().equals("prototype")) {
          Node prototype = callee.getNext();
          if (prototype.isGetprop()) {
            Node methodName = prototype.getFirstChild();
            if (methodName.getText().equals("prototypeMethod")) {
              prototypeMethodCall = child;
              break;
            }
          }
        }
      }
    }
    assertNotNull(prototypeMethodCall);

    // Check that the stub method was added to the original class
    Node stubMethodCall = null;
    for (Node child = classNode; child != null; child = child.getNext()) {
      if (child.isCall() && child.getFirstChild().getText().equals(CrossModuleMethodMotion.STUB_METHOD_NAME)) {
        stubMethodCall = child;
        break;
      }
    }
    assertNotNull(stubMethodCall);

    // Check that the unstub method was added to the dependent module
    Node unstubMethodCall = null;
    for (Node child = moduleNode; child != null; child = child.getNext()) {
      if (child.isCall() && child.getFirstChild().getText().equals(CrossModuleMethodMotion.UNSTUB_METHOD_NAME)) {
        unstubMethodCall = child;
        break;
      }
    }
    assertNotNull(unstubMethodCall);

    // Check that the prototype method was replaced with the stub method in the original class
    Node prototypeMethod = null;
    for (Node child = classNode; child != null; child = child.getNext()) {
      if (child.isGetprop() && child.getPropertyName().getText().equals("prototype")) {
        Node prototype = child.getNext();
        if (prototype.isGetprop()) {
          Node methodName = prototype.getFirstChild();
          if (methodName.getText().equals("prototypeMethod")) {
            prototypeMethod = child;
            break;
          }
        }
      }
    }
    assertNotNull(prototypeMethod);
    assertEquals(prototypeMethodCall, prototypeMethod.getNext());
    assertEquals(stubMethodCall, prototypeMethod.getNext().getNext());

    // Check that no errors were reported
    List<JSError> errors = Arrays.asList(compiler.getDiagnostics());
    assertTrue(errors.isEmpty());
  }
}