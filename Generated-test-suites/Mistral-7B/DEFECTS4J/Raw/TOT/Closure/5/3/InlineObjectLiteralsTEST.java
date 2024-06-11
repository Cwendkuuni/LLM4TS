package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior.AFTER_EXIT_SCOPE;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Tests for {@link InlineObjectLiterals} CompilerPass.
 */
public class InlineObjectLiteralsTest {

  private static final String TEST_CODE = "(" +
      "function main() {" +
      "  var obj1 = {" +
      "    a: 1," +
      "    b: {\n" +
      "      c: 2," +
      "      d: 3\n" +
      "    }," +
      "    e: function() {}\n" +
      "  }";

  @SuppressWarnings("unchecked")
  private static final InlineObjectLiterals PASS = new InlineObjectLiterals(
      new BasicCompiler(),
      new ConstantNameIdSupplier()) {
    @Override
    public void process(Node externs, Node root) {
      super.process(externs, root);
    }
  };

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    compiler.addPass(PASS);
    Scope scope = compiler.parseAndScope(TEST_CODE);
    Node root = scope.getRootNode();

    // Perform the pass.
    compiler.run();

    // Verify that the inlined variables are present in the root node.
    Iterator<Node> iter = root.traverseTopological().getIterator();
    List<Node> variables = Lists.newArrayList();
    while (iter.hasNext()) {
      Node node = iter.next();
      if (node.isVar()) {
        variables.add(node);
      }
    }
    assertEquals(5, variables.size());

    // Verify that the variables have the correct names.
    Iterator<Node> variableIter = variables.iterator();
    Map<String, String> expectedVariableNames = Maps.newHashMap();
    expectedVariableNames.put("a", "JSCompiler_object_inline_a_0");
    expectedVariableNames.put("c", "JSCompiler_object_inline_c_1");
    expectedVariableNames.put("d", "JSCompiler_object_inline_d_2");
    String currentVariableName;
    while (variableIter.hasNext()) {
      Node variable = variableIter.next();
      currentVariableName = variable.getName();
      assertEquals(expectedVariableNames.get(currentVariableName),
          variable.getName());
    }

    // Verify that the object literals have been replaced with variable assignments.
    Node mainFunction = root.getFirstChild();
    Node body = mainFunction.getLastChild();
    Node statement = body.getFirstChild();
    Node varAssignment = statement.getLastChild();
    assertEquals(AFTER_EXIT_SCOPE, varAssignment.getParent().getBehavior());
    Node objectLiteral = varAssignment.getFirstChild();
    Node assignmentTarget = varAssignment.getLastChild();
    assertEquals(objectLiteral, assignmentTarget.getParent());
    assertEquals(1, objectLiteral.getNumChildren());
    Node objectProperty = objectLiteral.getFirstChild();
    assertEquals("a", objectProperty.getString());
    Node objectValue = objectProperty.getFirstChild();
    assertEquals(Node.NUMBER_LITERAL, objectValue.getNodeType());
    assertEquals(1, objectValue.getFirstChild().getInt());

    // Verify that the nested object literal has been replaced with variable assignments.
    Node nestedObjectLiteral = objectLiteral.getNextSibling();
    assertEquals(2, nestedObjectLiteral.getNumChildren());
    statement = nestedObjectLiteral.getNextSibling();
    varAssignment = statement.getLastChild();
    assertEquals(AFTER_EXIT_SCOPE, varAssignment.getParent().getBehavior());
    objectLiteral = varAssignment.getFirstChild();
    assignmentTarget = varAssignment.getLastChild();
    assertEquals(objectLiteral, assignmentTarget.getParent());
    assertEquals(1, objectLiteral.getNumChildren());
    objectProperty = objectLiteral.getFirstChild();
    assertEquals("c", objectProperty.getString());
    objectValue = objectProperty.getFirstChild();
    assertEquals(Node.NULL_LITERAL, objectValue.getNodeType());

    statement = statement.getNextSibling();
    varAssignment = statement.getLastChild();
    assertEquals(AFTER_EXIT_SCOPE, varAssignment.getParent().getBehavior());
    objectLiteral = varAssignment.getFirstChild();
    assignmentTarget = varAssignment.getLastChild();
    assertEquals(objectLiteral, assignmentTarget.getParent());
    assertEquals(1, objectLiteral.getNumChildren());
    objectProperty = objectLiteral.getFirstChild();
    assertEquals("d", objectProperty.getString());
    objectValue = objectProperty.getFirstChild();
    assertEquals(Node.NUMBER_LITERAL, objectValue.getNodeType());
    assertEquals(3, objectValue.getFirstChild().getInt());

    // Verify that the function expression has been replaced with a variable assignment.
    Node functionExpression = nestedObjectLiteral.getNextSibling();
    assertEquals(1, functionExpression.getNumChildren());
    statement = functionExpression.getNextSibling();
    varAssignment = statement.getLastChild();
    assertEquals(AFTER_EXIT_SCOPE, varAssignment.getParent().getBehavior());
    objectLiteral = varAssignment.getFirstChild();
    assignmentTarget = varAssignment.getLastChild();
    assertEquals(objectLiteral, assignmentTarget.getParent());
    assertEquals(2, objectLiteral.getNumChildren());
    assertEquals("e", objectLiteral.getChildAtIndex(0).getString());
    assertEquals(Node.FUNCTION_EXPRESSION,
        objectLiteral.getChildAtIndex(1).getNodeType());
  }
}