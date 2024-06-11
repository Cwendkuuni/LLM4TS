import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class InlineObjectLiteralsTest {

  private InlineObjectLiterals testee;
  private AbstractCompiler compilerMock;
  private Supplier<String> safeNameIdSupplierMock;
  private Node root;
  private Node externs;

  @Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    safeNameIdSupplierMock = mock(Supplier.class);

    testee = new InlineObjectLiterals(compilerMock, safeNameIdSupplierMock);

    root = Node.objectLiteral();
    externs = Node.objectLiteral();
  }

  @Test
  public void testProcess_ObjectLiteralAssignment_InlineSimpleObject() {
    // Given
    Scope scope = new Scope(null);
    Var variable = new Var(Node.newString("x"), Node.NEW, null, null, null, scope);
    scope.addVar(variable);

    Node assignment = Node.newAssign(variable, Node.objectLiteral());
    Node parent = Node.newAssign(Node.Identifier("y"), assignment);

    root.addChild(parent);

    // When
    testee.process(externs, root);

    // Then
    Iterator<Var> variableIterator = scope.getVars().iterator();
    Var newVariable = null;
    while (variableIterator.hasNext()) {
      Var currentVariable = variableIterator.next();
      if (currentVariable.name.equals("x")) {
        newVariable = currentVariable;
        break;
      }
    }
    assertNotNull(newVariable);

    assertEquals(2, newVariable.getNumChildren());

    Node firstChild = newVariable.getFirstChild();
    assertEquals(Token.VAR, firstChild.getType());
    assertEquals("x", firstChild.getString());

    Node secondChild = newVariable.getLastChild();
    assertEquals(Token.OBJECTLIT, secondChild.getType());

    Node variableObject = secondChild.getFirstChild();
    assertEquals(2, variableObject.getNumChildren());

    Node keyOne = variableObject.getChildAtIndex(0);
    assertEquals(Token.STRING_LITERAL, keyOne.getType());
    assertEquals("a", keyOne.getString());

    Node valueOne = variableObject.getChildAtIndex(1);
    assertEquals(Token.NUM, valueOne.getType());
    assertEquals(1, valueOne.getInt());

    Node keyTwo = Node.newAssign(Node.Identifier("b"),
        Node.newString(Token.STRING_LITERAL, "b"));
    Node valueTwo = Node.newAssign(Node.Identifier("c"),
        Node.newNumber(Token.INT_LITERAL, 2));

    Node newAssignment = Node.newAssign(Node.Identifier("x"), Node.newObjectLiteral(keyTwo, valueTwo));

    Node parentNode = Node.newAssign(Node.Identifier("y"), newAssignment);
    Node parentReplaceNode = Node.newAssign(Node.Identifier("y"), Node.Identifier("x"));

    Node expectedNode = Node.newAssign(Node.Identifier("x"), Node.newObjectLiteral());
    Node actualNode = newVariable.getParent();

    // Check that the original node was replaced by the new one
    assertEquals(expectedNode, actualNode);

    // Check that the parent node was also replaced
    assertEquals(parentNode, root.getChildAtIndex(1));

    Node parentGrandparent = root.getChildAtIndex(0);
    Node parentGrandparentReplaceNode = Node.newAssign(Node.Identifier("y"),
        parentReplaceNode);
    parentGrandparent.replaceChild(parentGrandparent.getChildAtIndex(1),
        parentGrandparentReplaceNode);

    assertEquals(expectedNode, parentGrandparent.getChildAtIndex(1));
  }

  @Test
  public void testProcess_ObjectLiteralAssignment_InlineComplexObject() {
    // Given
    Scope scope = new Scope(null);
    Var variable = new Var(Node.newString("x"), Node.NEW, null, null, null, scope);
    scope.addVar(variable);

    Node assignment = Node.newAssign(variable, Node.objectLiteral());
    Node parent = Node.newAssign(Node.Identifier("y"), assignment);

    root.addChild(parent);

    Node objectLiteral = Node.objectLiteral();
    Node keyOne = Node.newString("a");
    Node valueOne = Node.newNumber(1);
    Node keyTwo = Node.newString("b");
    Node valueTwo = Node.newObjectLiteral();
    Node keyThree = Node.newString("c");
    Node valueThree = Node.newNumber(2);
    Node keyFour = Node.newString("d");
    Node valueFour = Node.newNumber(3);

    valueTwo.addChild(Node.newAssign(Node.Identifier("e"), Node.newString("e")));
    valueTwo.addChild(Node.newAssign(Node.Identifier("f"), Node.newNumber(4)));

    objectLiteral.addChild(Node.newAssign(keyOne, valueOne));
    objectLiteral.addChild(Node.newAssign(keyTwo, valueTwo));
    objectLiteral.addChild(Node.newAssign(keyThree, valueThree));

    Node newAssignment = Node.newAssign(Node.Identifier("x"), objectLiteral);

    Node parentReplaceNode = Node.newAssign(Node.Identifier("y"), Node.Identifier("x"));

    // When
    testee.process(externs, root);

    // Then
    Iterator<Var> variableIterator = scope.getVars().iterator();
    Var newVariable = null;
    while (variableIterator.hasNext()) {
      Var currentVariable = variableIterator.next();
      if (currentVariable.name.equals("x")) {
        newVariable = currentVariable;
        break;
      }
    }
    assertNotNull(newVariable);

    assertEquals(2, newVariable.getNumChildren());

    Node firstChild = newVariable.getFirstChild();
    assertEquals(Token.VAR, firstChild.getType());
    assertEquals("x", firstChild.getString());

    Node secondChild = newVariable.getLastChild();
    assertEquals(Token.OBJECTLIT, secondChild.getType());

    Node variableObject = secondChild.getFirstChild();
    assertEquals(8, variableObject.getNumChildren());

    Node keyOneNode = variableObject.getChildAtIndex(0);
    Node valueOneNode = variableObject.getChildAtIndex(1);

    Node keyTwoNode = Node.newAssign(Node.Identifier("a"), Node.Identifier("a1"));
    Node a1 = Node.newObjectLiteral();
    Node keyThreeNode = Node.newAssign(Node.Identifier("b"), Node.Identifier("b1"));
    Node b1 = Node.newObjectLiteral();
    Node keyFourNode = Node.newAssign(Node.Identifier("c"), Node.Identifier("c1"));
    Node c1 = Node.newObjectLiteral();
    Node keyFiveNode = Node.newAssign(Node.Identifier("e"), Node.Identifier("e1"));
    Node e1 = Node.newString("e1");
    Node keySixNode = Node.newAssign(Node.Identifier("f"), Node.newNumber(4));

    Node keyOne = Node.newAssign(Node.Identifier("a"), Node.newString("a"));
    Node valueOne = Node.newAssign(Node.Identifier("a1"), valueOneNode);
    Node keyTwo = Node.newAssign(Node.Identifier("b"), Node.newString("b"));
    Node valueTwo = Node.newAssign(Node.Identifier("b1"), Node.newObjectLiteral(keyFiveNode, keySixNode));

    Node keyThree = Node.newAssign(Node.Identifier("c"), Node.newString("c"));
    Node valueThree = Node.newAssign(Node.Identifier("c1"), Node.newString("c1"));

    Node keyFour = Node.newAssign(Node.Identifier("d"), Node.newString("d"));
    Node valueFour = Node.newAssign(Node.Identifier("d1"), Node.newNumber(3));

    a1.addChild(keyOne);
    a1.addChild(valueOne);
    a1.addChild(keyTwo);
    a1.addChild(valueTwo);

    b1.addChild(keyThree);
    b1.addChild(valueThree);
    b1.addChild(keyFour);
    b1.addChild(valueFour);

    objectLiteral.addChild(keyOneNode);
    objectLiteral.addChild(valueOneNode);
    objectLiteral.addChild(keyTwoNode);
    objectLiteral.addChild(valueTwo);
    objectLiteral.addChild(keyThree);
    objectLiteral.addChild(valueThree);
    objectLiteral.addChild(keyFour);
    objectLiteral.addChild(valueFour);

    Node newAssignmentNode = Node.newAssign(Node.Identifier("x"), objectLiteral);

    Node parentReplaceNodeFinal = Node.newAssign(Node.Identifier("y"), Node.Identifier("x"));

    Node expectedNode = Node.newAssign(Node.Identifier("x"), Node.newObjectLiteral());
    Node actualNode = newVariable.getParent();

    // Check that the original node was replaced by the new one
    assertEquals(expectedNode, actualNode);

    // Check that the parent node was also replaced
    assertEquals(parentNode, root.getChildAtIndex(1));

    Node parentGrandparent = root.getChildAtIndex(0);
    Node parentGrandparentReplaceNode = Node.newAssign(Node.Identifier("y"),
        parentReplaceNodeFinal);
    parentGrandparent.replaceChild(parentGrandparent.getChildAtIndex(1),
        parentGrandparentReplaceNode);

    assertEquals(expectedNode, parentGrandparent.getChildAtIndex(1));
  }

  @Test
  public void testProcess_ObjectLiteralAssignment_DontInlineSelfReferentialObject() {
    // Given
    Scope scope = new Scope(null);
    Var variable = new Var(Node.newString("x"), Node.NEW, null, null, null, scope);
    scope.addVar(variable);

    Node assignment = Node.newAssign(variable, Node.objectLiteral());
    Node selfReferentialObject = Node.objectLiteral();
    selfReferentialObject.addChild(Node.newAssign(Node.Identifier("a"), Node.Identifier("x")));

    Node parent = Node.newAssign(Node.Identifier("y"), assignment);
    root.addChild(parent);

    // When
    testee.process(externs, root);

    // Then
    Iterator<Var> variableIterator = scope.getVars().iterator();
    Var newVariable = null;
    while (variableIterator.hasNext()) {
      Var currentVariable = variableIterator.next();
      if (currentVariable.name.equals("x")) {
        newVariable = currentVariable;
        break;
      }
    }
    assertNotNull(newVariable);

    assertEquals(2, newVariable.getNumChildren());

    Node firstChild = newVariable.getFirstChild();
    assertEquals(Token.VAR, firstChild.getType());
    assertEquals("x", firstChild.getString());

    Node secondChild = newVariable.getLastChild();
    assertEquals(Token.OBJECTLIT, secondChild.getType());

    Node variableObject = secondChild.getFirstChild();
    assertEquals(1, variableObject.getNumChildren());

    Node a = variableObject.getChildAtIndex(0);
    assertEquals(Token.ASSIGN, a.getType());
    Node aParent = a.getParent();
    assertEquals(Token.GETPROP, aParent.getType());
    assertEquals("x", aParent.getFirstChild().getString());

    // Check that the original node wasn't replaced
    Node actualNode = newVariable.getParent();
    Node expectedNode = Node.newAssign(Node.Identifier("x"), Node.Identifier("x"));
    assertNotNull(actualNode);
    assertEquals(expectedNode, actualNode);
  }
}