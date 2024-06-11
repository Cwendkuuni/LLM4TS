import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compiler;
  private NodeTraversal traversal;

  @Mock
  private AbstractCompiler.Context context;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = new AbstractCompiler(context);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = new NodeTraversal(compiler, checker);
  }

  @Test
  public void testShouldTraverseFunctionsWithoutAnnotationOrPrototype() {
    Node functionNode = Node.newFunction("functionName", new Node[0]);
    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithAnnotation() {
    Node constructorNode = Node.newFunction("constructorName", new Node[0]);
    constructorNode.setJSDocInfo(JSDocInfo.newFromComment("constructor"));

    Node thisAnnotatedFunctionNode = Node.newFunction("thisAnnotatedFunctionName", new Node[0]);
    thisAnnotatedFunctionNode.setJSDocInfo(JSDocInfo.newFromComment("@this"));

    assertFalse(checker.shouldTraverse(traversal, constructorNode, null));
    assertFalse(checker.shouldTraverse(traversal, thisAnnotatedFunctionNode, null));
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node globalThisNode = Node.newIdent("this");
    Node parentNode = Node.newAssign(new Node[0], globalThisNode);

    Node traverseNode = Node.newExpr(new Node[0], Token.THIS, null);
    checker.visit(traversal, traversal.traverse(parentNode, traverseNode), parentNode);

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testShouldNotReportThisWithPropertyAccess() {
    Node parentNode = Node.newAssign(new Node[0], Node.newIdent("parent"));
    Node thisNode = Node.newIdent("this");
    Node propertyAccessNode = Node.newGetProp(parentNode, "property");
    Node traverseNode = Node.newGetProp(thisNode, "property");

    checker.visit(traversal, traversal.traverse(parentNode, propertyAccessNode), parentNode);
    checker.visit(traversal, traversal.traverse(propertyAccessNode, traverseNode), propertyAccessNode);

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testShouldNotReportThisWithPrototypeAccess() {
    Node parentNode = Node.newAssign(new Node[0], Node.newIdent("parent"));
    Node prototypeAccessNode = Node.newGetProp(parentNode, "prototype");
    Node thisNode = Node.newIdent("this");
    Node prototypePropertyAccessNode = Node.newGetProp(prototypeAccessNode, "property");
    Node traverseNode = Node.newGetProp(thisNode, "property");

    checker.visit(traversal, traversal.traverse(parentNode, prototypeAccessNode), parentNode);
    checker.visit(traversal, traversal.traverse(prototypeAccessNode, prototypePropertyAccessNode), prototypeAccessNode);
    checker.visit(traversal, traversal.traverse(prototypePropertyAccessNode, traverseNode), prototypePropertyAccessNode);

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testShouldReportThisWithAssignment() {
    Node parentNode = Node.newAssign(new Node[0], Node.newIdent("parent"));
    Node thisNode = Node.newIdent("this");
    Node assignNode = Node.newAssign(new Node[0], thisNode);

    Node traverseNode = Node.newIdent("this");
    checker.visit(traversal, traversal.traverse(parentNode, assignNode), parentNode);
    checker.visit(traversal, traversal.traverse(assignNode, traverseNode), assignNode);

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testShouldReportThisWithAssignmentWithLhsChild() {
    Node parentNode = Node.newAssign(new Node[0], Node.newIdent("parent"));
    Node lhsNode = Node.newIdent("lhs");
    Node rhsNode = Node.newIdent("rhs");
    Node assignNode = Node.newAssign(new Node[0], lhsNode);

    Node thisNode = Node.newIdent("this");
    Node traverseNode = Node.newIdent("this");

    checker.visit(traversal, traversal.traverse(parentNode, assignNode), parentNode);
    checker.visit(traversal, traversal.traverse(assignNode, lhsNode), assignNode);
    checker.visit(traversal, traversal.traverse(lhsNode, thisNode), lhsNode);
    checker.visit(traversal, traversal.traverse(assignNode, traverseNode), assignNode);

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testShouldTraverseAssignmentWithPrototypePropertyAccess() {
    Node parentNode = Node.newAssign(new Node[0], Node.newIdent("parent"));
    Node lhsNode = Node.newIdent("lhs");
    Node rhsNode = Node.newIdent("rhs");
    Node assignNode = Node.newAssign(new Node[0], lhsNode);

    Node prototypeNode = Node.newGetProp(parentNode, "prototype");
    Node prototypePropertyNode = Node.newGetProp(prototypeNode, "property");

    Node thisNode = Node.newIdent("this");
    Node traverseNode = Node.newGetProp(thisNode, "property");

    checker.visit(traversal, traversal.traverse(parentNode, assignNode), parentNode);
    checker.visit(traversal, traversal.traverse(assignNode, lhsNode), assignNode);
    checker.visit(traversal, traversal.traverse(lhsNode, thisNode), lhsNode);
    checker.visit(traversal, traversal.traverse(prototypeNode, prototypePropertyNode), prototypeNode);
    checker.visit(traversal, traversal.traverse(prototypePropertyNode, traverseNode), prototypePropertyNode);

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testShouldReportThisWithPrototypePropertyAccessWithLhsChild() {
    Node parentNode = Node.newAssign(new Node[0], Node.newIdent("parent"));
    Node lhsNode = Node.newIdent("lhs");
    Node rhsNode = Node.newIdent("rhs");
    Node assignNode = Node.newAssign(new Node[0], lhsNode);

    Node prototypeNode = Node.newGetProp(parentNode, "prototype");
    Node prototypePropertyNode = Node.newGetProp(prototypeNode, "property");

    Node thisNode = Node.newIdent("this");
    Node assignLhsChildNode = Node.newIdent("assignLhsChild");
    Node traverseNode = Node.newGetProp(assignLhsChildNode, "property");

    checker.visit(traversal, traversal.traverse(parentNode, assignNode), parentNode);
    checker.visit(traversal, traversal.traverse(assignNode, lhsNode), assignNode);
    checker.visit(traversal, traversal.traverse(lhsNode, thisNode), lhsNode);
    checker.visit(traversal, traversal.traverse(prototypeNode, prototypePropertyNode), prototypeNode);
    checker.visit(traversal, traversal.traverse(prototypePropertyNode, assignLhsChildNode), prototypePropertyNode);
    checker.visit(traversal, traversal.traverse(assignLhsChildNode, traverseNode), assignLhsChildNode);

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testShouldReportThisWithPrototypePropertyAccessAndLhsChildWithAncestorAssigment() {
    Node parentNode = Node.newAssign(new Node[0], Node.newIdent("parent"));
    Node lhsNode1 = Node.newIdent("lhs1");
    Node rhsNode1 = Node.newIdent("rhs1");
    Node assignNode1 = Node.newAssign(new Node[0], lhsNode1);

    Node prototypeNode = Node.newGetProp(parentNode, "prototype");
    Node prototypePropertyNode = Node.newGetProp(prototypeNode, "property");

    Node thisNode = Node.newIdent("this");
    Node assignLhsChildNode = Node.newIdent("assignLhsChild");
    Node traverseNode = Node.newGetProp(assignLhsChildNode, "property");

    Node lhsNode2 = Node.newIdent("lhs2");
    Node rhsNode2 = Node.newIdent("rhs2");
    Node assignNode2 = Node.newAssign(new Node[0], lhsNode2);

    checker.visit(traversal, traversal.traverse(parentNode, assignNode1), parentNode);
    checker.visit(traversal, traversal.traverse(assignNode1, lhsNode1), assignNode1);
    checker.visit(traversal, traversal.traverse(lhsNode1, thisNode), lhsNode1);
    checker.visit(traversal, traversal.traverse(prototypeNode, prototypePropertyNode), prototypeNode);
    checker.visit(traversal, traversal.traverse(prototypePropertyNode, assignLhsChildNode), prototypePropertyNode);
    checker.visit(traversal, traversal.traverse(assignLhsChildNode, traverseNode), assignLhsChildNode);

    checker.visit(traversal, traversal.traverse(parentNode, assignNode2), parentNode);
    checker.visit(traversal, traversal.traverse(assignNode2, lhsNode2), assignNode2);
    checker.visit(traversal, traversal.traverse(lhsNode2, assignLhsChildNode), lhsNode2);

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testShouldNotReportThisWithPrototypePropertyAccessAndLhsChildWithoutAncestorAssigment() {
    Node parentNode = Node.newAssign(new Node[0], Node.newIdent("parent"));
    Node lhsNode1 = Node.newIdent("lhs1");
    Node rhsNode1 = Node.newIdent("rhs1");
    Node assignNode1 = Node.newAssign(new Node[0], lhsNode1);

    Node prototypeNode = Node.newGetProp(parentNode, "prototype");
    Node prototypePropertyNode = Node.newGetProp(prototypeNode, "property");

    Node thisNode = Node.newIdent("this");
    Node assignLhsChildNode = Node.newIdent("assignLhsChild");
    Node traverseNode = Node.newGetProp(assignLhsChildNode, "property");

    Node lhsNode2 = Node.newIdent("lhs2");
    Node rhsNode2 = Node.newIdent("rhs2");
    Node assignNode2 = Node.newAssign(new Node[0], lhsNode2);

    checker.visit(traversal, traversal.traverse(parentNode, assignNode1), parentNode);
    checker.visit(traversal, traversal.traverse(assignNode1, lhsNode1), assignNode1);
    checker.visit(traversal, traversal.traverse(lhsNode1, thisNode), lhsNode1);
    checker.visit(traversal, traversal.traverse(prototypeNode, prototypePropertyNode), prototypeNode);
    checker.visit(traversal, traversal.traverse(prototypePropertyNode, assignLhsChildNode), prototypePropertyNode);
    checker.visit(traversal, traversal.traverse(assignLhsChildNode, traverseNode), assignLhsChildNode);

    Node lhsNode3 = Node.newIdent("lhs3");
    Node rhsNode3 = Node.newIdent("rhs3");
    Node assignNode3 = Node.newAssign(new Node[0], lhsNode3);

    checker.visit(traversal, traversal.traverse(parentNode, assignNode3), parentNode);
    checker.visit(traversal, traversal.traverse(assignNode3, lhsNode3), assignNode3);

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));
  }
}