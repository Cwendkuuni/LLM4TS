import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.rhino.Node.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    // Given
    AbstractCompiler compiler = new Compiler();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    Node root = new Node(Token.PROGRAM, Lists.newArrayList(), null);

    // When
    collapseVariableDeclarations.process(null, root);

    // Then
    Node varA = root.getFirstChild();
    Node varB = varA.getNext();
    Node varC = varB.getNext();

    assertNotNull(varA);
    assertNotNull(varB);
    assertNotNull(varC);
    assertEquals("var", varA.getTokenType().toString());
    assertEquals("a", varA.getString());
    assertEquals("var", varB.getTokenType().toString());
    assertEquals("b", varB.getString());
    assertEquals("var", varC.getTokenType().toString());
    assertEquals("c", varC.getString());

    Node collapseNode = varB;
    Node nextNode = collapseNode.getNext();

    while (nextNode != null && nextNode.isVar()) {
      collapseNode = nextNode;
      nextNode = collapseNode.getNext();
    }

    assertNull(nextNode);

    assertNotNull(collapseNode.getFirstChild());
    Node collapsedAssignments = collapseNode.getFirstChild();

    Node assignmentA = collapsedAssignments.getFirstChild();
    Node assignmentB = assignmentA.getNext();

    assertNotNull(assignmentA);
    assertNotNull(assignmentB);
    assertEquals("=", assignmentA.getTokenType().toString());
    assertEquals("true", assignmentA.getLastChild().getString());
    assertEquals("=", assignmentB.getTokenType().toString());
    assertEquals("true", assignmentB.getLastChild().getString());

    Node collapsedVariable = collapseNode;
    assertEquals(varA, collapsedVariable);

    Node nextCollapsedVariable = collapsedVariable.getNext();
    assertEquals(varC, nextCollapsedVariable);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsWithInvalidInput() {
    // Given
    AbstractCompiler compiler = new Compiler();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    Node root = new Node(Token.PROGRAM, Lists.newArrayList(), null);

    // When
    collapseVariableDeclarations.process(null, root);

    // Then
    Node varA = root.getFirstChild();
    Node varB = varA.getNext();
    Node varC = varB.getNext();

    // Assertion error should be thrown here because varD is not a child of the given root
    Node varD = varC.getNext();

    // This assertion will fail because varD is not a child of the root
    assertNotNull(varD);
  }

  @Test
  public void testCollapseVariableDeclarationsWithExceptionHandling() {
    // Given
    AbstractCompiler compiler = new Compiler();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    Node root = new Node(Token.PROGRAM, Lists.newArrayList(), null);

    // When
    try {
      collapseVariableDeclarations.process(null, root);
      fail("Expected an exception to be thrown.");
    } catch (RuntimeException e) {
      // Then
      assertEquals("Unexpected exception thrown while collapsing variable declarations.", e.getMessage());
    }

    // Given
    Node rootWithException = new Node(Token.PROGRAM, Lists.newArrayList(), null);
    rootWithException.setJSDocInfo(new JSDocInfo());
    rootWithException.getJSDocInfo().addSuppression("Unexpected exception");

    // When
    collapseVariableDeclarations.process(null, rootWithException);

    // Then
    Node varA = rootWithException.getFirstChild();
    Node varB = varA.getNext();
    Node varC = varB.getNext();

    assertNotNull(varA);
    assertNotNull(varB);
    assertNotNull(varC);

    Node collapseNodeA = varA;
    Node nextNodeA = collapseNodeA.getNext();

    while (nextNodeA != null && nextNodeA.isVar()) {
      collapseNodeA = nextNodeA;
      nextNodeA = collapseNodeA.getNext();
    }

    assertNull(nextNodeA);

    Node collapseNodeB = varB;
    Node nextNodeB = collapseNodeB.getNext();

    while (nextNodeB != null && nextNodeB.isVar()) {
      collapseNodeB = nextNodeB;
      nextNodeB = collapseNodeB.getNext();
    }

    assertNull(nextNodeB);

    Node collapsedVariableA = collapseNodeA;
    assertEquals(varA, collapsedVariableA);

    Node collapsedVariableB = collapseNodeB;
    assertEquals(varB, collapsedVariableB);

    Node nextCollapsedVariable = collapsedVariableB.getNext();
    assertEquals(varC, nextCollapsedVariable);
  }

  @Test
  public void testCollapseVariableAssignments() {
    // Given
    AbstractCompiler compiler = new Compiler();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    Node root = new Node(Token.PROGRAM, Lists.newArrayList(), null);

    // When
    collapseVariableDeclarations.process(null, root);

    // Then
    Node varA = root.getFirstChild();
    Node varB = varA.getNext();
    Node assignA = varA.getLastChild();
    Node assignB = varB.getFirstChild();

    assertNotNull(varA);
    assertNotNull(varB);
    assertNotNull(assignA);
    assertNotNull(assignB);

    assertEquals("var", varA.getTokenType().toString());
    assertEquals("a", varA.getString());
    assertEquals("=", assignA.getTokenType().toString());
    assertEquals("true", assignA.getPrevious().getString());
    assertEquals("var", varB.getTokenType().toString());
    assertEquals("b", varB.getString());
    assertEquals("=", assignB.getTokenType().toString());
    assertEquals("true", assignB.getPrevious().getString());

    Node collapseNodeA = varA;
    Node nextNodeA = collapseNodeA.getNext();

    while (nextNodeA != null && nextNodeA.isVar()) {
      collapseNodeA = nextNodeA;
      nextNodeA = collapseNodeA.getNext();
    }

    assertNull(nextNodeA);

    Node collapseNodeB = varB;
    Node nextNodeB = collapseNodeB.getNext();

    while (nextNodeB != null && nextNodeB.isVar()) {
      collapseNodeB = nextNodeB;
      nextNodeB = collapseNodeB.getNext();
    }

    assertNull(nextNodeB);

    Node collapsedVariableA = collapseNodeA;
    assertEquals(varA, collapsedVariableA);

    Node collapsedVariableB = collapseNodeB;
    assertEquals(varB, collapsedVariableB);

    Node nextCollapsedVariable = collapsedVariableB.getNext();
    assertEquals(null, nextCollapsedVariable);

    Node collapsedAssignments = collapsedVariableB;
    Node collapsedAssignmentA = collapsedAssignments.getFirstChild();
    Node collapsedAssignmentB = collapsedAssignmentA.getNext();

    assertNotNull(collapsedAssignments);
    assertNotNull(collapsedAssignmentA);
    assertNotNull(collapsedAssignmentB);
    assertEquals("=", collapsedAssignmentA.getTokenType().toString());
    assertEquals("true", collapsedAssignmentA.getLastChild().getString());
    assertEquals("=", collapsedAssignmentB.getTokenType().toString());
    assertEquals("true", collapsedAssignmentB.getLastChild().getString());

    Node collapsedVariable = collapsedVariableB;
    assertEquals(varB, collapsedVariable);

    Node nextCollapsedVariableA = collapsedVariable.getPrevious();
    assertEquals(varA, nextCollapsedVariableA);

    Node nextCollapsedVariableB = nextCollapsedVariableA.getNext();
    assertEquals(null, nextCollapsedVariableB);

    Node collapsibleAssignmentC = new Node(Token.ASSIGN, Lists.newArrayList(), varB);
    varB.addChildToBack(collapsibleAssignmentC);

    collapseVariableDeclarations.process(null, root);

    // Then
    Node collapsedAssignmentC = collapsedVariableB.getLastChild();

    assertNotNull(collapsedAssignmentC);
    assertEquals("=", collapsedAssignmentC.getTokenType().toString());
    assertEquals("true", collapsedAssignmentC.getPrevious().getString());
  }
}