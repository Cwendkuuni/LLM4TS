package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InlineObjectLiteralsTest {

  private static final String CLASS_NAME = "InlineObjectLiterals";
  private static final String TEST_DATA_FILE = "InlineObjectLiteralsTestData.js";

  @org.junit.Test
  public void testInlineObjectLiteralsPass() {
    // Set up mocks.
    AbstractCompiler compiler = mock(CompilerPass.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // Prepare test data.
    SourceFile testData = SourceFile.fromString(TEST_DATA_FILE);
    Node externs = testData.getTree();
    Node root = externs.getFirstChild();

    // Run the pass and verify the results.
    pass.process(externs, root);
    CompilerPass actualPass = compiler.getPreviousPass();
    assertThat(actualPass).isInstanceOf(InlineObjectLiterals.class);

    // Sample verification of the inlined object literals.
    List<Node> objectAssignments = NodeUtil.collectNodes(root, NodeUtil.MATCH_ASSIGN,
        node -> node.getParent().getType() == Token.VAR && node.getParent().getFirstChild()
            .getType() == Token.OBJECTLIT);

    Map<String, Node> expectedObjects = ImmutableMap.<String, Node>builder()
        .put("obj1", testData.getTree().getFirstChild().getFirstChild())
        .put("obj2", testData.getTree().getFirstChild().getLastChild().getLastChild())
        .build();

    for (Node objAssignment : objectAssignments) {
      String actualVarName = objAssignment.getParent().getFirstChild().getString();
      Node actualObject = objAssignment.getLastChild();
      Node expectedObject = expectedObjects.get(actualVarName);

      // Verify that the object literal has been inlined and the generated
      // variables have been assigned the correct values.
      List<Node> objectKeys = NodeUtil.collectNodes(expectedObject, NodeUtil.MATCH_GETPROP);
      List<Node> expectedAssignments = Lists.newArrayList();
      List<Node> actualAssignments = Lists.newArrayList();

      for (Node objKey : objectKeys) {
        String keyName = objKey.getString();
        String actualVarName = actualVarName + "_" + keyName;
        Node expectedVar = NodeUtil.findVariable(expectedObject, keyName);

        Node expectedAssignment = NodeUtil.newAssign(
            NodeUtil.newString(Token.NAME, actualVarName),
            NodeUtil.newString(Token.NAME, keyName + ":")
                .getFirstChild());

        Node actualVar = NodeUtil.findVariable(actualObject, keyName);
        Node actualAssignment = NodeUtil.newAssign(actualVar,
            NodeUtil.newString(Token.NAME, actualVarName + "_")
                .getFirstChild());

        expectedAssignments.add(expectedAssignment);
        actualAssignments.add(actualAssignment);
      }

      // Verify that the generated variables have been assigned the correct values.
      Node actualVariableNode = NodeUtil.findVariable(root, actualVarName);
      Node expectedVariableNode = NodeUtil.findVariable(expectedObject, actualVarName);

      Node actualValue = actualVariableNode.getNext();
      Node expectedValue = expectedVariableNode.getNext();

      assertThat(actualAssignments).hasSize(expectedAssignments.size());
      for (int i = 0; i < expectedAssignments.size(); i++) {
        Node actual = actualAssignments.get(i);
        Node expected = expectedAssignments.get(i);
        assertThat(actual).isEqualTo(expected);
      }

      assertThat(actualValue).isEqualTo(expectedValue);
    }
  }
}