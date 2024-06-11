import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TreeTraverser.AbstractNodeVisitor;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Mock private AbstractCompiler compiler;
  @Mock private NodeTraversal nodeTraversal;
  private CheckGlobalThis checker;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    Node nonGlobalContext = Node.newNode(Token.FUNCTION, null);
    assertFalse(checker.shouldTraverse(nodeTraversal, nonGlobalContext, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithAnnotations() {
    Node constructor = Node.newNode(Token.FUNCTION, null);
    JSDocInfo constructorJsDoc = Node.newNode(Token.COMMENT, "constructor");
    constructor.setJSDocInfo(constructorJsDoc);

    Node thisAnnotatedFunction = Node.newNode(Token.FUNCTION, null);
    JSDocInfo thisAnnotatedFunctionJsDoc = Node.newNode(Token.COMMENT, "@this");
    thisAnnotatedFunction.setJSDocInfo(thisAnnotatedFunctionJsDoc);

    Node overrideFunction = Node.newNode(Token.FUNCTION, null);
    JSDocInfo overrideFunctionJsDoc = Node.newNode(Token.COMMENT, "@override");
    overrideFunction.setJSDocInfo(overrideFunctionJsDoc);

    Node interfaceFunction = Node.newNode(Token.FUNCTION, null);
    JSDocInfo interfaceFunctionJsDoc = Node.newNode(Token.COMMENT, "@interface");
    interfaceFunction.setJSDocInfo(interfaceFunctionJsDoc);

    Node noAnnotationFunction = Node.newNode(Token.FUNCTION, null);

    Node[] functions =
        new Node[] {
          constructor,
          thisAnnotatedFunction,
          overrideFunction,
          interfaceFunction,
          noAnnotationFunction
        };

    for (Node function : functions) {
      AbstractNodeVisitor visitor = new AbstractNodeVisitor() {
        @Override
        public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
          return checker.shouldTraverse(t, n, parent);
        }
      };

      assertTrue(visitor.shouldTraverse(nodeTraversal, function, null));
      assertFalse(visitor.shouldTraverse(nodeTraversal, function, Node.newNode(Token.BLOCK, null)));
    }
  }

  @Test
  public void testShouldTraverseFunctionsWithNoLogicalPlaceForThisAnnotation() {
    Node functionWithNoLogicalPlaceForThisAnnotation =
        Node.newNode(Token.FUNCTION, null);

    Node objectLiteral = Node.newNode(Token.OBJECT, null);

    Node function = Node.newNode(Token.NAME, "function");
    function.setProperty("x", Node.newNode(Token.FUNCTION, null));
    objectLiteral.setProperty("x", function);

    Node assignment = Node.newNode(Token.ASSIGN, null);
    assignment.setLeft(objectLiteral);
    assignment.setRight(Node.newNode(Token.NAME, "functionWithNoLogicalPlaceForThisAnnotation"));

    Node script = Node.newNode(Token.SCRIPT, null);
    script.appendChild(assignment);

    AbstractNodeVisitor visitor = new AbstractNodeVisitor() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(t, n, parent);
      }
    };

    assertTrue(visitor.shouldTraverse(nodeTraversal, functionWithNoLogicalPlaceForThisAnnotation, null));
  }

  @Test(expected = AssertionError.class)
  public void testAssignmentToPrototypePropertyShouldNotTraverse() {
    Node prototypePropertyAssignment = Node.newNode(Token.ASSIGN, null);
    prototypePropertyAssignment.setLeft(Node.newNode(Token.GETPROP, null, Node.newNode(Token.NAME, "prototype")));
    prototypePropertyAssignment.setRight(Node.newNode(Token.NAME, "function"));

    Node script = Node.newNode(Token.SCRIPT, null);
    script.appendChild(prototypePropertyAssignment);

    AbstractNodeVisitor visitor = new AbstractNodeVisitor() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(t, n, parent);
      }
    };

    visitor.shouldTraverse(nodeTraversal, prototypePropertyAssignment, null);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignment = Node.newNode(Token.ASSIGN, null);
    Node leftSide = Node.newNode(Token.THIS, null);
    assignment.setLeft(leftSide);
    Node rightSide = Node.newNode(Token.NAME, "function");
    assignment.setRight(rightSide);

    Node script = Node.newNode(Token.SCRIPT, null);
    script.appendChild(assignment);

    AbstractNodeVisitor visitor = new AbstractNodeVisitor() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(t, n, parent);
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.THIS && checker.shouldReportThis(n, parent)) {
          compiler.report(t.makeError(n, WARNING, CheckGlobalThis.GLOBAL_THIS));
        }
      }
    };

    visitor.visit(nodeTraversal, assignment, script);

    assertTrue(compiler.getDiagnostics().isEmpty());

    checker.visit(nodeTraversal, assignment, script);

    assertTrue(compiler.getDiagnostics().size() == 1);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node propertyAccess = Node.newNode(Token.GET, null);
    Node leftSide = Node.newNode(Token.THIS, null);
    propertyAccess.setLeft(leftSide);
    Node propertyName = Node.newNode(Token.NAME, "property");
    propertyAccess.setProperties(propertyName);

    Node script = Node.newNode(Token.SCRIPT, null);
    script.appendChild(propertyAccess);

    AbstractNodeVisitor visitor = new AbstractNodeVisitor() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(t, n, parent);
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.THIS && checker.shouldReportThis(n, parent)) {
          compiler.report(t.makeError(n, WARNING, CheckGlobalThis.GLOBAL_THIS));
        }
      }
    };

    visitor.visit(nodeTraversal, propertyAccess, script);

    assertTrue(compiler.getDiagnostics().isEmpty());

    checker.visit(nodeTraversal, propertyAccess, script);

    assertTrue(compiler.getDiagnostics().size() == 1);
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignmentToVariableOrObject() {
    Node assignment = Node.newNode(Token.ASSIGN, null);
    Node leftSide = Node.newNode(Token.THIS, null);
    Node variable = Node.newNode(Token.NAME, "variable");
    assignment.setLeft(Node.newNode(Token.ASSIGN, null, leftSide, variable));
    Node rightSide = Node.newNode(Token.NAME, "function");
    assignment.setRight(rightSide);

    Node script = Node.newNode(Token.SCRIPT, null);
    script.appendChild(assignment);

    AbstractNodeVisitor visitor = new AbstractNodeVisitor() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(t, n, parent);
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.THIS && checker.shouldReportThis(n, parent)) {
          compiler.report(t.makeError(n, WARNING, CheckGlobalThis.GLOBAL_THIS));
        }
      }
    };

    visitor.visit(nodeTraversal, assignment, script);

    assertTrue(compiler.getDiagnostics().isEmpty());

    checker.visit(nodeTraversal, assignment, script);

    assertTrue(compiler.getDiagnostics().isEmpty());
  }
}