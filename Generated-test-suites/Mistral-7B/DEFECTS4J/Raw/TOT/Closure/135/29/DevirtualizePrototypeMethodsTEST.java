import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.JSType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerUnit;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.DefinitionSite;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;

/**
 * Test class for {@link DevirtualizePrototypeMethods}.
 */
public class DevirtualizePrototypeMethodsTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private JSTypeRegistry typeRegistry;
  private CompilerOptions options;

  @org.junit.Before
  public void setUp() {
    this.compiler = newAbstractCompiler();
    this.passManager = new CompilerPassManager(compiler);
    this.typeRegistry = compiler.getTypeRegistry();
    this.options = new CompilerOptions();
  }

  @org.junit.Test
  public void testRewritesSimplePrototypeMethod() {
    // Given
    String sourceCode = "var a = {}; a.prototype.accumulate = function(value) {" +
        " return this.total += value; }; var total = a.accumulate(2);";

    // When
    CompilerUnit unit = new CompilerUnit(sourceCode, "module.js", null);
    passManager.addPass(new DevirtualizePrototypeMethods(passManager));
    CompilerPass result = passManager.runPasses(unit, options);

    // Then
    Node root = unit.getRoot();
    Node totalNode = findChildByValue(root, "total");
    assertTrue(totalNode.isNumber());
    assertEquals(3, totalNode.getNumber().intValueExact());

    // Check the rewritten method
    Node rewrittenMethod = findMethodByName(root, "accumulate");
    assertNotNull(rewrittenMethod);
    assertEquals(2, rewrittenMethod.getChildCount());

    assertInstanceOf(Node.class, rewrittenMethod.getFirstChild());
    Node thisNode = (Node) rewrittenMethod.getFirstChild();
    assertEquals("this", thisNode.getString());

    assertInstanceOf(Node.class, rewrittenMethod.getLastChild());
    Node body = (Node) rewrittenMethod.getLastChild();
    Node totalVar = findChildByValue(body, "total");
    assertNotNull(totalVar);
    assertEquals(Node.VAR, totalVar.getType());

    List<Node> statements = body.getChildren();
    assertEquals(2, statements.size());

    Node assignmentNode = statements.get(0);
    assertEquals(Node.ASSIGN, assignmentNode.getType());
    Node left = assignmentNode.getFirstChild();
    assertEquals("total", left.getString());

    Node right = assignmentNode.getLastChild();
    Node valueNode = right.getFirstChild();
    Node addNode = right.getLastChild();

    assertEquals(Node.NAME, left.getType());
    assertEquals(Node.NAME, right.getType());
    assertEquals(Node.NUMBER, valueNode.getType());
    assertEquals(Node.PLUS, addNode.getType());

    Node lhs = valueNode.getFirstChild();
    Node rhs = valueNode.getLastChild();
    assertEquals("value", rhs.getString());

    Node accumulateName = rewrittenMethod.getFirstChild();
    assertEquals("accumulate", accumulateName.getString());

    Node functionTypeNode = findFunctionType(rewrittenMethod);
    Node functionType = (FunctionType) functionTypeNode.getJSType();
    assertEquals(1, functionType.getParameters().size());
    assertEquals("this", functionType.getParameters().get(0).getName());
    assertEquals(INT, functionType.getReturnType().getType());
  }

  /**
   * Helper method to find the first child with the given value.
   *
   * @param node the node to search in.
   * @param value the value to search for.
   * @return the Node with the given value or null if not found.
   */
  private Node findChildByValue(Node node, String value) {
    for (Node child : node.children()) {
      if (NodeUtil.isVar(child) && child.getString().equals(value)) {
        return child;
      } else if (NodeUtil.isGetProp(child)) {
        String propertyName = child.getLastChild().getString();
        if (propertyName.equals(value)) {
          return child.getFirstChild();
        }
      } else if (NodeUtil.isSetProp(child)) {
        String propertyName = child.getFirstChild().getString();
        if (propertyName.equals(value)) {
          return child.getLastChild();
        }
      }
    }
    return null;
  }

  /**
   * Helper method to find a method by name.
   *
   * @param node the root node.
   * @param methodName the name of the method to find.
   * @return the Node for the method with the given name or null if not found.
   */
  private Node findMethodByName(Node node, String methodName) {
    for (Node child : node.children()) {
      if (isPrototypeMethodDefinition(child)) {
        String method = child.getLastChild().getLastChild().getString();
        if (method.equals(methodName)) {
          return child;
        }
      } else if (child instanceof Node && NodeUtil.isFunction(child)) {
        String method = ((Node) child).getFirstChild().getString();
        if (method.equals(methodName)) {
          return child;
        }
      }
      Node result = findMethodByName(child, methodName);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  /**
   * Helper method to find the function type node of a given method.
   *
   * @param node the Node representing a method.
   * @return the first child of the method node that represents its
   * function type or null if the method does not have a function type.
   */
  private Node findFunctionType(Node node) {
    for (Node child : node.children()) {
      if (child instanceof Node && NodeUtil.isFunctionType(child)) {
        return child;
      }
    }
    return null;
  }

  /**
   * Helper method to create a new AbstractCompiler instance.
   *
   * @return a new AbstractCompiler instance.
   */
  private AbstractCompiler newAbstractCompiler() {
    return new AbstractCompiler(new SimpleDefinitionFinder(compiler),
        new CompilerContext(newSourceFile("")), options);
  }
}