import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Compiler.parseTreeFromFile;
import static com.google.javascript.jscomp.Compiler.processSource;
import static com.google.javascript.jscomp.Compiler.toSource;
import static com.google.javascript.rhino.Node.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.GraphUtil;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeUtil;

/**
 * Tests for CrossModuleMethodMotion class.
 */
@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private ErrorReporter errorReporter;

  @Before
  public void setup() {
    compiler = new Compiler();
    errorReporter = compiler.getErrorReporter();
  }

  @After
  public void teardown() throws IOException {
    compiler.dispose();
  }

  /**
   * Test basic functionality of CrossModuleMethodMotion.
   */
  @Test
  public void testBasicFunctionality() throws IOException {
    CompilerOptions options = new CompilerOptions();
    SourceFile rootFile = new SourceFile("test.js", "test code", null);
    String source = "function A() {}\n" +
        "A.prototype.foo = function() {}\n";
    rootFile.setSource(source, null);
    Node rootNode = parseTreeFromFile(Arrays.asList(rootFile), options);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler,
        new CrossModuleMethodMotion.IdGenerator(), true);

    Node result = processSource(compiler, rootNode, null, null);

    assertEquals(source, toSource(result));

    JSModuleGraph moduleGraph = compiler.getModuleGraph();
    assertTrue(moduleGraph.getModuleCount() > 1);
    Node foo = findMethod(result, "A", "foo");
    assertNotNull(foo);
    JSModule deepestCommonModuleRef = findModule(moduleGraph, "test");
    assertEquals("A", deepestCommonModuleRef.getName());

    pass.process(null, rootNode);
    result = compiler.getResult();
    assertEquals(source, toSource(result));

   deepestCommonModuleRef = findModule(moduleGraph, "test");
    foo = findMethod(result, "A", "foo");
    assertNotNull(foo);
    assertFalse(foo.isFunction());
    Node stubIdNode = findNodeWithFunctionDeclaration(result, STUB_METHOD_NAME);
    Node unstubIdNode = findNodeWithFunctionDeclaration(result, UNSTUB_METHOD_NAME);
    Node unstubBodyNode = findNodeWithFunctionBody(unstubIdNode);
    Node unstubCallNode = findMethodCall(foo, STUB_METHOD_NAME, stubIdNode);
    Node unstubAssignNode = findAssign(result, unstubCallNode, foo);
    assertNotNull(unstubAssignNode);
    Node unstubBody = unstubBodyNode.getBody();
    Node body = unstubBody.getFirstChild();
    Node unstubFunction = body.getFirstChild();
    Node unstubBodyAssign = body.getLastChild();
    Node proto = findNodeWithPropertyDeclaration(result, "A.prototype", "foo");
    Node assign = findAssign(result, unstubBodyAssign, proto);
    Node assignRight = assign.getRightHandSide();
    Node unstubFunctionCall = findMethodCall(assignRight, UNSTUB_METHOD_NAME,
        unstubIdNode);
    Node unstubFunctionBody = unstubFunction.getBody();
    Node functionBody = unstubFunctionBody.getFirstChild();
    Node functionBodyAssign = functionBody.getLastChild();
    Node functionReturn = functionBodyAssign.getFirstChild();
    Node functionReturnAssign = functionReturn.getFirstChild();
    Node functionReturnValue = functionReturnAssign.getRightHandSide();
    Node prototype = functionReturnValue.getParent();
    Node property = prototype.getFirstChild();
    Node propertyName = property.getFirstChild();
    String propertyNameValue = propertyName.getText();
    Node propertyValue = property.getLastChild();
    Node propertyValueIdentifier = propertyValue.getFirstChild();
    String propertyValueIdentifierValue = propertyValueIdentifier.getText();
    assertEquals("foo", propertyNameValue);
    assertEquals("A.prototype.foo", propertyValueIdentifierValue);
  }

  /**
   * Test null deepest common module error.
   */
  @Test
  public void testNullDeepestCommonModuleError() throws IOException {
    CompilerOptions options = new CompilerOptions();
    SourceFile rootFile = new SourceFile("test.js", "test code", null);
    String source = "function A() {}\n";
    rootFile.setSource(source, null);
    Node rootNode = parseTreeFromFile(Arrays.asList(rootFile), options);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler,
        new CrossModuleMethodMotion.IdGenerator(), true);

    Node result = processSource(compiler, rootNode, null, null);

    JSModule deepestCommonModuleRef = null;
    pass.process(rootNode, null);

    result = compiler.getResult();
    assertEquals(source, toSource(result));
    compiler.reportDiagnostics();
    List<JSError> errors = errorReporter.getAllErrors();
    assertTrue(errors.size() > 0);
    JSError error = errors.get(0);
    assertEquals(DiagnosticType.ERROR, error.getType());
    assertEquals("JSC_INTERNAL_ERROR_MODULE_DEPEND", error.getCode());
    assertEquals("null deepest common module", error.getMessage());
  }

  /**
   * Finds a method node with the given name in the given node.
   * @param node The node to search for a method node in.
   * @param className The name of the class owning the method.
   * @param methodName The name of the method.
   * @return The method node, or null if not found.
   */
  private Node findMethod(Node node, String className, String methodName) {
    Node prototype = findNodeWithPropertyDeclaration(node, className + ".prototype", methodName);
    if (prototype != null) {
      return findMethod(prototype, methodName);
    }
    Iterator<Node> children = node.getChildren().iterator();
    while (children.hasNext()) {
      Node child = children.next();
      if (child.isFunction()) {
        Node name = child.getFirstChild();
        if (name != null && name.getText().equals(methodName)) {
          if (className.equals(child.getParent().getFirstChild().getText())) {
            return child;
          }
          if (child.getParent().isPropertyAccess()) {
            Node prototypeNode = child.getParent().getFirstChild();
            if (prototypeNode.getText().equals(className)) {
              return child;
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * Finds a module node with the given name in the module graph.
   * @param moduleGraph The module graph to search for a module node in.
   * @param name The name of the module.
   * @return The module node, or null if not found.
   */
  private JSModule findModule(JSModuleGraph moduleGraph, String name) {
    for (JSModule module : moduleGraph) {
      if (module.getName().equals(name)) {
        return module;
      }
    }
    return null;
  }

  /**
   * Finds a node with a function declaration with the given name.
   * @param node The node to search for a function declaration node in.
   * @param functionName The name of the function.
   * @return The function declaration node, or null if not found.
   */
  private Node findNodeWithFunctionDeclaration(Node node, String functionName) {
    Iterator<Node> children = node.getChildren().iterator();
    while (children.hasNext()) {
      Node child = children.next();
      if (child.isFunctionDeclaration()) {
        Node functionNameNode = child.getFirstChild();
        if (functionNameNode != null && functionNameNode.getText().equals(functionName)) {
          return child;
        }
      }
      if (child.isBlock()) {
        Node functionNode = findNodeWithFunctionDeclaration(child, functionName);
        if (functionNode != null) {
          return functionNode;
        }
      }
    }
    return null;
  }

  /**
   * Finds a node with a function body with the given function declaration node.
   * @param functionDeclaration The function declaration node.
   * @return The function body node, or null if not found.
   */
  private Node findNodeWithFunctionBody(Node functionDeclaration) {
    Node body = functionDeclaration.getBody();
    if (body != null && body.isBlock()) {
      return body;
    }
    return null;
  }

  /**
   * Finds a method call node with the given method name and method identifier node.
   * @param node The node to search for a method call node in.
   * @param methodName The name of the method.
   * @param methodIdentifierNode The method identifier node.
   * @return The method call node, or null if not found.
   */
  private Node findMethodCall(Node node, String methodName, Node methodIdentifierNode) {
    Iterator<Node> children = node.getChildren().iterator();
    while (children.hasNext()) {
      Node child = children.next();
      if (child.isPropertyAccess()) {
        Node methodIdentifier = child.getFirstChild();
        if (methodIdentifier != null && methodIdentifier.getText().equals(methodName)) {
          Node methodCall = child.getLastChild();
          Node methodIdentifierNode2 = methodCall.getFirstChild();
          if (methodIdentifierNode2 != null && methodIdentifierNode2.isIdentifier()) {
            if (methodIdentifierNode2.getText().equals(methodIdentifierNode.getText())) {
              return methodCall;
            }
          }
        }
      }
      if (child.isCall()) {
        Node callee = child.getFirstChild();
        if (callee.isIdent()) {
          Node methodIdentifierNode2 = callee.getFirstChild();
          if (methodIdentifierNode2 != null && methodIdentifierNode2.isIdentifier()
              && methodIdentifierNode2.getText().equals(methodIdentifierNode.getText())) {
            return child;
          }
        }
        Node callee2 = findMethodCall(callee, methodName, methodIdentifierNode);
        if (callee2 != null) {
          return callee2;
        }
      }
    }
    return null;
  }

  /**
   * Finds an assign node with the given left hand side and right hand side nodes.
   * @param node The node to search for an assign node in.
   * @param leftHandSideNode The left hand side node.
   * @param rightHandSideNode The right hand side node.
   * @return The assign node, or null if not found.
   */
  private Node findAssign(Node node, Node leftHandSideNode, Node rightHandSideNode) {
    Iterator<Node> children = node.getChildren().iterator();
    while (children.hasNext()) {
      Node child = children.next();
      if (child.isAssign()) {
        Node leftHandSide = child.getLeftHandSide();
        Node rightHandSide = child.getRightHandSide();
        if (leftHandSide.equals(leftHandSideNode) && rightHandSide.equals(rightHandSideNode)) {
          return child;
        }
      }
    }
    return null;
  }
}