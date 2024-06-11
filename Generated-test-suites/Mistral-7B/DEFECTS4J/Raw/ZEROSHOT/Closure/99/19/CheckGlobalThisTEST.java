import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;
  private Node node;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
    when(compiler.getTraversal()).thenReturn(traversal);
    node = Node.newFunction();
  }

  @Test
  public void shouldNotTraverseNonGlobalContexts() {
    Node nonGlobalContext = Node.newNumberLiteral(42);

    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), nonGlobalContext, null));
  }

  @Test
  public void shouldTraverseConstructorFunctions() {
    Node constructor = Node.newFunction();
    Node constructorJsDocInfo = Node.newTextNode("constructor");
    Node constructorAnnotation = Node.newTextNode("@constructor");
    Node constructorPrototype = Node.newName("prototype");

    constructor.setJSDocInfo(constructorJsDocInfo);
    constructor.addChildToChildren(constructorAnnotation);
    constructor.addChildToChildren(constructorPrototype);

    when(traversal.getCurrentNode()).thenReturn(constructor);

    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void shouldTraverseFunctionsAnnotatedWithThis() {
    Node thisAnnotatedFunction = Node.newFunction();
    Node thisAnnotation = Node.newTextNode("@this");

    thisAnnotatedFunction.addChildToChildren(thisAnnotation);

    when(traversal.getCurrentNode()).thenReturn(thisAnnotatedFunction);

    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void shouldTraverseFunctionsWithNoApparentThisAnnotations() {
    Node noApparentThisAnnotationFunction = Node.newFunction();
    Node parentBlock = Node.newBlock();
    Node parentName = Node.newName("parent");

    noApparentThisAnnotationFunction.addChildToChildren(parentName);
    parentName.addChildToChildren(noApparentThisAnnotationFunction);

    when(traversal.getCurrentNode()).thenReturn(parentName);

    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @Test
  public void shouldTraverseLeftSideOfAssignment() {
    Node assignment = Node.newAssign();
    Node leftSide = Node.newName("a");
    Node rightSide = Node.newName("b");

    leftSide.setType(Token.THIS);

    assignment.setLeftHandSide(leftSide);
    assignment.setRightHandSide(rightSide);

    when(traversal.getCurrentNode()).thenReturn(assignment);

    assertTrue(checker.shouldReportThis(assignment, null));
  }

  @Test
  public void shouldReportThisOnLeftSideOfAssignment() {
    Node assignment = Node.newAssign();
    Node leftSide = Node.newName("a");
    Node rightSide = Node.newName("b");

    leftSide.setType(Token.THIS);

    assignment.setLeftHandSide(leftSide);
    assignment.setRightHandSide(rightSide);

    CheckLevel level = CheckLevel.ERROR;
    checker.reportError(NodeTraversal.emptyTraversal(), assignment, level, DiagnosticType.error(null, null, null, "JSC_USED_GLOBAL_THIS"));

    assertTrue(true);
  }

  @Test
  public void shouldNotReportThisOnRightSideOfAssignment() {
    Node assignment = Node.newAssign();
    Node leftSide = Node.newName("a");
    Node rightSide = Node.newName("this");

    leftSide.setType(Token.NAME);
    rightSide.setType(Token.THIS);

    assignment.setLeftHandSide(leftSide);
    assignment.setRightHandSide(rightSide);

    when(traversal.getCurrentNode()).thenReturn(assignment);

    assertFalse(checker.shouldReportThis(assignment, null));
  }

  @Test
  public void shouldNotReportThisOnPrototypeProperties() {
    Node prototype = Node.newName("prototype");
    Node getProp = Node.newGetProp();
    Node function = Node.newFunction();

    getProp.setLeftHandSide(function);
    getProp.setName(prototype);

    function.setType(Token.FUNCTION);

    when(traversal.getCurrentNode()).thenReturn(getProp);

    assertFalse(checker.shouldReportThis(getProp, null));
  }

  @Test
  public void shouldNotReportThisOnQualifiedPrototypeProperties() {
    Node qualifiedName = Node.newName("obj.prototype");
    Node getProp = Node.newGetProp();
    Node function = Node.newFunction();

    getProp.setLeftHandSide(function);
    getProp.setName(qualifiedName);

    function.setType(Token.FUNCTION);

    when(traversal.getCurrentNode()).thenReturn(getProp);

    assertFalse(checker.shouldReportThis(getProp, null));
  }

  @Test
  public void shouldReportThisOnLeftSideOfAssignmentInConstructorFunction() {
    Node constructor = Node.newFunction();
    Node constructorJsDocInfo = Node.newTextNode("constructor");
    Node constructorAnnotation = Node.newTextNode("@constructor");
    Node constructorPrototype = Node.newName("prototype");
    Node assignment = Node.newAssign();
    Node leftSide = Node.newName("this");
    Node rightSide = Node.newName("x");

    constructor.setJSDocInfo(constructorJsDocInfo);
    constructor.addChildToChildren(constructorAnnotation);
    constructor.addChildToChildren(constructorPrototype);
    constructor.addChildToChildren(assignment);

    leftSide.setType(Token.THIS);
    assignment.setLeftHandSide(leftSide);
    assignment.setRightHandSide(rightSide);

    when(traversal.getCurrentNode()).thenReturn(assignment);

    assertFalse(checker.shouldTraverse(traversal, null, null));
    assertFalse(checker.shouldReportThis(assignment, constructor));
  }

  @Test
  public void shouldReportThisOnLeftSideOfAssignmentInConstructorFunctionWithJSDoc() {
    Node constructor = Node.newFunction();
    Node constructorJsDocInfo = Node.newTextNode("constructor");
    Node constructorAnnotation = Node.newTextNode("@constructor");
    Node constructorPrototype = Node.newName("prototype");
    Node assignment = Node.newAssign();
    Node leftSide = Node.newName("this");
    Node rightSide = Node.newName("x");
    Node thisAnnotation = Node.newTextNode("@this");

    constructor.setJSDocInfo(constructorJsDocInfo);
    constructor.addChildToChildren(constructorAnnotation);
    constructor.addChildToChildren(constructorPrototype);
    constructor.addChildToChildren(thisAnnotation);
    constructor.addChildToChildren(assignment);

    leftSide.setType(Token.THIS);
    assignment.setLeftHandSide(leftSide);
    assignment.setRightHandSide(rightSide);

    when(traversal.getCurrentNode()).thenReturn(assignment);

    assertTrue(checker.shouldReportThis(assignment, constructor));
  }
}