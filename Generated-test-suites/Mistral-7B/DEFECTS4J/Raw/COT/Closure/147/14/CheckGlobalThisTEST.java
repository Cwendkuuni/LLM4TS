import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunction() {
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(null, CheckLevel.WARNING));
    Node functionNode = new Node(TOKEN_FUNCTION, new int[] {0});
    Node parent = new Node(BLOCK, new int[] {0});

    assertTrue(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(null, CheckLevel.WARNING));
    Node constructorFunctionNode = new Node(TOKEN_FUNCTION, new int[] {FUNCTION_CONSTRUCTOR});
    Node parent = new Node(BLOCK, new int[] {0});

    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, constructorFunctionNode, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(null, CheckLevel.WARNING));
    Node thisAnnotatedFunctionNode = new Node(TOKEN_FUNCTION, new int[] {FUNCTION_CONSTRUCTOR, THIS});
    Node parent = new Node(BLOCK, new int[] {0});

    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, thisAnnotatedFunctionNode, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(null, CheckLevel.WARNING));
    Node overrideAnnotatedFunctionNode = new Node(TOKEN_FUNCTION, new int[] {FUNCTION, OVERRIDE});
    Node parent = new Node(BLOCK, new int[] {0});

    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, overrideAnnotatedFunctionNode, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionOutsideBlock() {
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(null, CheckLevel.WARNING));
    Node functionNode = new Node(TOKEN_FUNCTION, new int[] {0});
    Node parent = new Node(TOKEN.SCRIPT, new int[] {0});

    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverseAssignmentLeftSide() {
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(null, CheckLevel.WARNING));
    Node assignParent = new Node(ASSIGN, new int[] {0});
    Node assignLeftSide = new Node(TOKEN_THIS, new int[] {0});
    Node assignRightSide = new Node(TOKEN.IDENT, new String[] {"a"});

    assertTrue(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, assignLeftSide, assignParent));
  }

  @Test
  public void testShouldTraverseAssignmentRightSide() {
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(null, CheckLevel.WARNING));
    Node assignParent = new Node(ASSIGN, new int[] {0});
    Node assignLeftSide = new Node(ASSIGN, new int[] {0});
    Node assignRightSide = new Node(TOKEN_THIS, new int[] {0});

    assertTrue(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, assignRightSide, assignParent));
  }

  @Test
  public void testShouldNotTraverseAssignmentLeftSideWithPrototypeAccess() {
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(null, CheckLevel.WARNING));
    Node assignParent = new Node(ASSIGN, new int[] {0});
    Node assignLeftSide = new Node(GETPROP, new int[] {0, PROPERTY_NAME, "prototype"});
    Node assignRightSide = new Node(TOKEN.IDENT, new String[] {"a"});

    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, assignLeftSide, assignParent));
  }

  @Test
  public void testShouldNotTraverseAssignmentLeftSideWithPrototypeAccessDeep() {
    NodeTraversal traversal = new NodeTraversal(new CheckGlobalThis(null, CheckLevel.WARNING));
    Node assignParent = new Node(ASSIGN, new int[] {0});
    Node assignLeftSide = new Node(GETPROP, new int[] {0, PROPERTY_NAME, "prototype"});
    Node getPropParent = assignLeftSide.getFirstChild();
    Node getPropLeftSide = getPropParent.getFirstChild();
    Node getPropRightSide = getPropParent.getLastChild();
    Node assignRightSide = new Node(TOKEN.IDENT, new String[] {"a"});

    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, assignLeftSide, assignParent));
  }

  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node code = new Node(SCRIPT, new int[] {0});
    Node function = new Node(FUNCTION, new int[] {0});
    Node globalThis = new Node(TOKEN_THIS, new int[] {0});
    Node assign = new Node(ASSIGN, new int[] {0});
    Node assignLeft = new Node(TOKEN_THIS, new int[] {0});
    Node assignRight = new Node(ASSIGN, new int[] {0});
    Node ifStatement = new Node(IF, new int[] {0});
    Node ifCondition = new Node(LOGICAL_OR, new int[] {0, 0});
    Node equalCondition1 = new Node(EQUAL, new int[] {0, 0});
    Node equalCondition2 = new Node(EQUAL, new int[] {0, 0});
    Node identifierA = new Node(TOKEN.IDENT, new String[] {"a"});
    Node identifierB = new Node(TOKEN.IDENT, new String[] {"b"});

    code.addChild(function);
    function.addChild(new Node(TOKEN.FUNCTION_DECL, new int[] {FUNCTION_DECLaration}, new String[] {"myFunction"}));
    function.addChild(globalThis);
    function.addChild(assign);
    assign.addChild(assignLeft);
    assign.addChild(assignRight);
    assignRight.addChild(globalThis);
    assignLeft.setParent(assign);

    code.addChild(new Node(TOKEN.IF, new int[] {0}));
    ifStatement.addChild(ifCondition);
    ifCondition.addChild(equalCondition1);
    equalCondition1.addChildren(new Node[]{globalThis, identifierA});
    ifCondition.addChild(equalCondition2);
    equalCondition2.addChildren(new Node[]{identifierA, identifierB});
    ifStatement.setParent(code);

    NodeTraversal traversal = new NodeTraversal(check);
    traversal.traverse(code);

    assertEquals(1, check.compiler.getDiagnostics().size());
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThisWithNoDiagnostic() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node code = new Node(SCRIPT, new int[] {0});
    Node function = new Node(FUNCTION, new int[] {0});
    Node functionAnnotation = new Node(FUNCTION_ANNOTATION, new int[] {0});
    Node functionAnnotationValue = new Node(TOKEN.STRING_LITERAL, new String[] {"@constructor"});
    Node globalThis = new Node(TOKEN_THIS, new int[] {0});
    Node assign = new Node(ASSIGN, new int[] {0});
    Node assignLeft = new Node(TOKEN_THIS, new int[] {0});
    Node assignRight = new Node(ASSIGN, new int[] {0});
    Node identifierA = new Node(TOKEN.IDENT, new String[] {"a"});

    function.addChild(new Node(TOKEN.FUNCTION_DECL, new int[] {FUNCTION_DECLaration}, new String[] {"myFunction"}));
    function.addChild(functionAnnotation);
    functionAnnotation.addChild(functionAnnotationValue);
    function.addChild(globalThis);
    function.addChild(assign);
    assign.addChild(assignLeft);
    assign.addChild(assignRight);
    assignRight.addChild(globalThis);
    assignLeft.setParent(assign);

    code.addChild(function);
    code.addChild(new Node(TOKEN.IF, new int[] {0}));
    Node ifCondition = new Node(LOGICAL_OR, new int[] {0, 0});
    Node equalCondition1 = new Node(EQUAL, new int[] {0, 0});
    Node equalCondition2 = new Node(EQUAL, new int[] {0, 0});
    Node identifierB = new Node(TOKEN.IDENT, new String[] {"b"});

    ifCondition.addChild(equalCondition1);
    equalCondition1.addChildren(new Node[]{globalThis, identifierA});
    ifCondition.addChild(equalCondition2);
    equalCondition2.addChildren(new Node[]{identifierA, identifierB});

    NodeTraversal traversal = new NodeTraversal(check);
    traversal.traverse(code);

    assertEquals(0, check.compiler.getDiagnostics().size());
  }
}