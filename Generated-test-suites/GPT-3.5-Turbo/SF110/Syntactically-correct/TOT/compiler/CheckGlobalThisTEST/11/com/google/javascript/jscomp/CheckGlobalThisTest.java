import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForConstructorFunctions() {
    Node functionNode = NodeUtil.createFunctionNode();
    JSDocInfo jsDocInfo = new JSDocInfoBuilder().recordConstructor().build();
    functionNode.setJSDocInfo(jsDocInfo);
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, null));
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForInterfaceFunctions() {
    Node functionNode = NodeUtil.createFunctionNode();
    JSDocInfo jsDocInfo = new JSDocInfoBuilder().recordInterface().build();
    functionNode.setJSDocInfo(jsDocInfo);
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, null));
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionsAnnotatedWithThis() {
    Node functionNode = NodeUtil.createFunctionNode();
    JSDocInfo jsDocInfo = new JSDocInfoBuilder().recordThis().build();
    functionNode.setJSDocInfo(jsDocInfo);
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, null));
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionsAnnotatedWithOverride() {
    Node functionNode = NodeUtil.createFunctionNode();
    JSDocInfo jsDocInfo = new JSDocInfoBuilder().recordOverride().build();
    functionNode.setJSDocInfo(jsDocInfo);
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, null));
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionsInNonGlobalContexts() {
    Node functionNode = NodeUtil.createFunctionNode();
    Node parent = NodeUtil.createBlockNode();
    parent.addChildToFront(functionNode);
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionsLentToPrototype() {
    Node functionNode = NodeUtil.createFunctionNode();
    Node grandparent = NodeUtil.createObjectLitKeyNode();
    Node parent = NodeUtil.createGetPropNode(grandparent);
    parent.getLastChild().setString("prototype");
    parent.addChildToFront(functionNode);
    JSDocInfo jsDocInfo = new JSDocInfoBuilder().build();
    grandparent.setJSDocInfo(jsDocInfo);
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_shouldReturnTrueForLeftSideOfAssignment() {
    Node assignmentNode = NodeUtil.createAssignmentNode();
    Node left = NodeUtil.createNameNode();
    assignmentNode.addChildToBack(left);
    assertTrue(checkGlobalThis.shouldTraverse(null, left, assignmentNode));
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForAssignmentToPrototypeProperty() {
    Node assignmentNode = NodeUtil.createAssignmentNode();
    Node left = NodeUtil.createGetPropNode();
    left.getLastChild().setString("prototype");
    assignmentNode.addChildToBack(left);
    assertFalse(checkGlobalThis.shouldTraverse(null, left, assignmentNode));
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForAssignmentToNestedPrototypeProperty() {
    Node assignmentNode = NodeUtil.createAssignmentNode();
    Node left = NodeUtil.createGetPropNode();
    Node grandparent = NodeUtil.createObjectLitKeyNode();
    grandparent.getLastChild().setString("prototype");
    Node parent = NodeUtil.createGetPropNode();
    parent.addChildToFront(left);
    parent.getLastChild().setString("property");
    grandparent.addChildToFront(parent);
    assignmentNode.addChildToBack(grandparent);
    assertFalse(checkGlobalThis.shouldTraverse(null, left, assignmentNode));
  }

  @Test
  public void testShouldTraverse_shouldReturnTrueForRightSideOfAssignment() {
    Node assignmentNode = NodeUtil.createAssignmentNode();
    Node left = NodeUtil.createGetPropNode();
    Node right = NodeUtil.createNameNode();
    assignmentNode.addChildToBack(left);
    assignmentNode.addChildToBack(right);
    assertTrue(checkGlobalThis.shouldTraverse(null, right, assignmentNode));
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForRightSideOfAssignmentToPrototypeProperty() {
    Node assignmentNode = NodeUtil.createAssignmentNode();
    Node left = NodeUtil.createGetPropNode();
    left.getLastChild().setString("prototype");
    Node right = NodeUtil.createNameNode();
    assignmentNode.addChildToBack(left);
    assignmentNode.addChildToBack(right);
    assertFalse(checkGlobalThis.shouldTraverse(null, right, assignmentNode));
  }

  @Test
  public void testVisit_shouldReportGlobalThisOnLeftSideOfAssignment() {
    Node thisNode = NodeUtil.createThisNode();
    checkGlobalThis.assignLhsChild = thisNode;
    DiagnosticType diagnosticType = CheckGlobalThis.GLOBAL_THIS;
    assertTrue(checkGlobalThis.shouldReportThis(thisNode));

    DiagnosticReporter reporter = new DiagnosticReporter();
    checkGlobalThis.visit(null, thisNode, null);
    assertEquals(1, reporter.getErrorCount());
    assertEquals(diagnosticType, reporter.getLastError().getType());
  }

  @Test
  public void testVisit_shouldNotReportGlobalThisOnLeftSideOfAssignment() {
    Node thisNode = NodeUtil.createThisNode();
    checkGlobalThis.assignLhsChild = null;
    assertFalse(checkGlobalThis.shouldReportThis(thisNode));

    DiagnosticReporter reporter = new DiagnosticReporter();
    checkGlobalThis.visit(null, thisNode, null);
    assertEquals(0, reporter.getErrorCount());
  }

  @Test
  public void testVisit_shouldNotReportGlobalThisWhenCurrentNodeIsNotLeftSideOfAssignment() {
    Node assignmentNode = NodeUtil.createAssignmentNode();
    Node left = NodeUtil.createGetPropNode();
    Node right = NodeUtil.createNameNode();
    assignmentNode.addChildToBack(left);
    assignmentNode.addChildToBack(right);

    Node thisNode = NodeUtil.createThisNode();
    checkGlobalThis.assignLhsChild = left;
    assertFalse(checkGlobalThis.shouldReportThis(thisNode));

    DiagnosticReporter reporter = new DiagnosticReporter();
    checkGlobalThis.visit(null, thisNode, assignmentNode);
    assertEquals(0, reporter.getErrorCount());
  }

  private class DiagnosticReporter implements DiagnosticReporter {
    private int errorCount = 0;
    private JSError lastError;

    @Override
    public void report(JSError error) {
      errorCount++;
      lastError = error;
    }

    public int getErrorCount() {
      return errorCount;
    }

    public JSError getLastError() {
      return lastError;
    }
  }
}