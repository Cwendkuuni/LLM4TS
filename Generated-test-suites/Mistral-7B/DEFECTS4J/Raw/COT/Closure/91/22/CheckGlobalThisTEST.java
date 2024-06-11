import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = Node.newFunction("func", new Node[0], new Node[0]);
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node functionNode = Node.newFunction("constructorFunc", new Node[0], null);
    JSDocInfo constructorJsDoc = new JSDocInfo(true, null, null, null, null, null, null, null, null);
    functionNode.setJSDocInfo(constructorJsDoc);

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunction("funcWithThis", new Node[0], null);
    JSDocInfo thisJsDoc = new JSDocInfo(null, null, "@this", null, null, null, null, null, null);
    functionNode.setJSDocInfo(thisJsDoc);

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoParent() {
    Node functionNode = Node.newFunction("funcNoParent", new Node[0], null);

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeProperty() {
    Node functionNode = Node.newFunction("funcPrototype", new Node[0], null);
    Node prototypeNode = Node.newGetProp("prototype", Node.newName("Object"));

    Node assignNode = Node.newAssign(Node.newName("p"), prototypeNode);
    Node parentNode = Node.newAssign(Node.newName("o"), assignNode);
    functionNode.setParent(parentNode);

    Node functionBody = Node.newBlock(new Node[0]);
    functionNode.setBody(functionBody);
    Node functionCall = Node.newCall(functionNode, new Node[0]);

    Node scriptNode = Node.newBlock(new Node[] { functionNode });
    Node rootNode = Node.newCall(Node.newName("top"), new Node[] { scriptNode });

    Node traversal = new NodeTraversal(rootNode, new Node[0], null, checker);
    traversal.traverse(NodeTraversal.BODY);

    Node funcPrototype = traversal.findNode(NodeUtil.getName(functionNode));
    Node prototypeProperty = traversal.findNode(prototypeNode.getString());

    Node thisNode = Node.newThis();
    Node propertyAccessNode = Node.newGetProp(NodeUtil.getName(prototypeProperty), thisNode);

    Node assignNode2 = Node.newAssign(propertyAccessNode, thisNode);

    Node visitor = new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        if (n.getType() == Token.ASSIGN) {
          Node lhs = n.getFirstChild();
          Node rhs = lhs.getNext();
          if (lhs == propertyAccessNode && rhs == thisNode) {
            fail("Should not have traversed the prototype property assignment.");
          }
        }
      }
    };

    NodeTraversal traversal2 = new NodeTraversal(rootNode, new Node[0], null, visitor);
    traversal2.traverse(NodeTraversal.BODY);

    assertTrue(checker.shouldNotTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldTraverseAssignmentToThis() {
    Node assignNode = Node.newAssign(Node.newThis(), Node.newName("x"));

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, assignNode, null));
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisInAssignment() {
    Node assignNode = Node.newAssign(Node.newName("x"), Node.newThis());

    Node scriptNode = Node.newBlock(new Node[] { assignNode });
    Node rootNode = Node.newCall(Node.newName("top"), new Node[] { scriptNode });

    Node traversal = new NodeTraversal(rootNode, new Node[0], null, checker);
    traversal.traverse(NodeTraversal.BODY);

    fail("Expected error not reported.");
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunction("funcNoAnnotation", new Node[0], null);

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithAssignmentToPrototype() {
    Node functionNode = Node.newFunction("funcAssignmentToPrototype", new Node[0], null);
    Node prototypeNode = Node.newGetProp("prototype", Node.newName("Object"));

    Node assignNode = Node.newAssign(prototypeNode, Node.newName("x"));
    Node parentNode = Node.newAssign(Node.newName("o"), assignNode);
    functionNode.setParent(parentNode);

    Node functionBody = Node.newBlock(new Node[0]);
    functionNode.setBody(functionBody);
    Node functionCall = Node.newCall(functionNode, new Node[0]);

    Node scriptNode = Node.newBlock(new Node[] { functionNode });
    Node rootNode = Node.newCall(Node.newName("top"), new Node[] { scriptNode });

    Node traversal = new NodeTraversal(rootNode, new Node[0], null, checker);
    traversal.traverse(NodeTraversal.BODY);

    Node funcAssignmentToPrototype = traversal.findNode(NodeUtil.getName(functionNode));

    Node thisNode = Node.newThis();
    Node propertyAccessNode = Node.newGetProp(NodeUtil.getName(prototypeNode), thisNode);

    Node assignNode2 = Node.newAssign(propertyAccessNode, thisNode);

    Node visitor = new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        if (n.getType() == Token.ASSIGN && n == assignNode2) {
          fail("Should not have traversed the prototype property assignment.");
        }
      }
    };

    NodeTraversal traversal2 = new NodeTraversal(rootNode, new Node[0], null, visitor);
    traversal2.traverse(NodeTraversal.BODY);

    assertTrue(checker.shouldNotTraverse(NodeTraversal.EMPTY, functionNode, null));
  }
}