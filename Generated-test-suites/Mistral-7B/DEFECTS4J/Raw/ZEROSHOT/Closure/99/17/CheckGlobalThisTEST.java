import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @SuppressWarnings("unchecked")
  private static Callback createCheckGlobalThis(AbstractCompiler compiler, CheckLevel level) {
    return new CheckGlobalThis(compiler, level);
  }

  @org.junit.Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = createCheckGlobalThis(compiler, WARNING);

    Node functionPrototype = Node.createCall(Token.FUNCTION, "Function", new Node[0]);
    Node function = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node constructor = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("MyConstructor")});
    Node getThis = Node.createGetProp(Node.createName("this"), new Node[0]);
    Node setThis = Node.createSetProp(getThis, Node.createString("myProperty"), Node.createNull());

    Node functionWithJsDoc = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node functionJsDoc = Node.createJSDocInfo(functionWithJsDoc, "myFunction", new Node[]{Node.createTag("constructor")});

    Node constructorWithJsDoc = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("MyConstructor")});
    Node constructorJsDoc = Node.createJSDocInfo(constructorWithJsDoc, "MyConstructor", new Node[0]);

    Node getThisInFunctionPrototype = Node.createGetProp(Node.createName("this"), new Node[]{functionPrototype});
    Node getThisInFunction = Node.createGetProp(Node.createName("this"), new Node[]{function});
    Node getThisInConstructorPrototype = Node.createGetProp(Node.createName("this"), new Node[]{constructor});
    Node getThisInConstructor = Node.createGetProp(Node.createName("this"), new Node[]{constructorWithJsDoc});

    Node functionWithAssign = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node functionAssign = Node.createBinaryOp(Token.ASSIGN, Node.createName("a"), getThisInFunction);
    Node functionWithAssignJsDoc = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node functionAssignJsDoc = Node.createSetProp(functionAssign, Node.createName("a"), Node.createName("myFunction"));

    Node functionWithPrototype = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node functionPrototypeProperty = Node.createGetProp(getThisInFunctionPrototype, Node.createString("prototype"));
    Node functionPrototypePropertySetter = Node.createSetProp(functionPrototypeProperty, Node.createString("myProperty"), Node.createNull());

    Node functionWithPrototypeChain = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node functionPrototypeChainProperty = Node.createGetProp(getThisInFunctionPrototype, Node.createString("prototype"));
    Node functionPrototypeChainPropertySetter = Node.createSetProp(functionPrototypeChainProperty, Node.createString("myPrototype"), Node.createName("MyPrototype"));

    Node functionInObject = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node object = Node.createObjectLiteral();
    Node objectProperty = Node.createSetProp(object, Node.createString("myFunction"), functionInObject);
    Node getThisInObject = Node.createGetProp(Node.createGetProp(Node.createName("this"), new Node[]{object}), Node.createString("myFunction"));

    Node functionInBlock = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node block = Node.createBlock(new Node[]{Node.createCall(Token.ASSIGN, null, Node.createName("a"), Node.createNull())});
    Node getThisInBlock = Node.createGetProp(Node.createName("this"), new Node[]{block});

    Node functionInScript = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node script = Node.createBlock(new Node[]{functionInScript});
    Node getThisInScript = Node.createGetProp(Node.createName("this"), new Node[]{script});

    Node[] testCases = new Node[]{
            functionPrototype,
            function,
            constructor,
            constructorWithJsDoc,
            getThis,
            setThis,
            functionWithAssign,
            functionAssignJsDoc,
            functionPrototypeProperty,
            functionPrototypePropertySetter,
            functionPrototypeChainProperty,
            functionPrototypeChainPropertySetter,
            functionInObject,
            getThisInObject,
            functionInBlock,
            getThisInBlock,
            functionInScript,
            getThisInScript};

    for (Node testCase : testCases) {
      Node parent = Node.createNull();
      if (testCase instanceof Node && testCase.getNext() != null) {
        parent = testCase.getPrevious();
      }

      boolean expected = false;
      if (testCase instanceof Node && testCase.getType() == Token.FUNCTION) {
        JSDocInfo jsDocInfo = NodeUtil.getFunctionJsDocInfo((Node) testCase);
        if (jsDocInfo != null && (jsDocInfo.isConstructor() || jsDocInfo.hasThisType() || jsDocInfo.isOverride())) {
          expected = false;
        } else {
          expected = parent != null && (parent.getType() == Token.BLOCK || parent.getType() == Token.SCRIPT || parent.getType() == Token.NAME || parent.getType() == Token.ASSIGN);
        }
      } else if (testCase instanceof Node && testCase.getType() == Token.ASSIGN) {
        Node leftSide = testCase.getFirstChild();
        Node rightSide = leftSide.getNext();
        expected = leftSide == getThis && (rightSide == null || !NodeUtil.isGet(rightSide));
      } else {
        expected = testCase == getThis;
      }

      CheckGlobalThis checker = createCheckGlobalThis(compiler, WARNING);
      assertTrue(checker.shouldTraverse(NodeTraversal.create(compiler), testCase, parent));
      assertEquals(expected, checker.shouldReportThis(testCase, parent));
    }
  }

  @org.junit.Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = createCheckGlobalThis(compiler, WARNING);

    Node functionPrototype = Node.createCall(Token.FUNCTION, "Function", new Node[0]);
    Node function = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node constructor = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("MyConstructor")});
    Node getThis = Node.createGetProp(Node.createName("this"), new Node[0]);
    Node setThis = Node.createSetProp(getThis, Node.createString("myProperty"), Node.createNull());

    Node functionWithJsDoc = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node functionJsDoc = Node.createJSDocInfo(functionWithJsDoc, "myFunction", new Node[]{Node.createTag("constructor")});

    Node constructorWithJsDoc = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("MyConstructor")});
    Node constructorJsDoc = Node.createJSDocInfo(constructorWithJsDoc, "MyConstructor", new Node[0]);

    Node functionWithAssign = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node functionAssign = Node.createBinaryOp(Token.ASSIGN, Node.createName("a"), getThis);
    Node functionWithAssignJsDoc = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node functionAssignJsDoc = Node.createSetProp(functionAssign, Node.createName("a"), Node.createName("myFunction"));

    Node functionWithPrototype = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node functionPrototypeProperty = Node.createGetProp(getThisInFunctionPrototype, Node.createString("prototype"));
    Node functionPrototypePropertySetter = Node.createSetProp(functionPrototypeProperty, Node.createString("myProperty"), Node.createNull());

    Node functionWithPrototypeChain = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node functionPrototypeChainProperty = Node.createGetProp(getThisInFunctionPrototype, Node.createString("prototype"));
    Node functionPrototypeChainPropertySetter = Node.createSetProp(functionPrototypeChainProperty, Node.createString("myPrototype"), Node.createName("MyPrototype"));

    Node functionInObject = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node object = Node.createObjectLiteral();
    Node objectProperty = Node.createSetProp(object, Node.createString("myFunction"), functionInObject);
    Node getThisInObject = Node.createGetProp(Node.createGetProp(Node.createName("this"), new Node[]{object}), Node.createString("myFunction"));

    Node functionInBlock = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node block = Node.createBlock(new Node[]{Node.createCall(Token.ASSIGN, null, Node.createName("a"), Node.createNull())});
    Node getThisInBlock = Node.createGetProp(Node.createName("this"), new Node[]{block});

    Node functionInScript = Node.createCall(Token.FUNCTION, "Function", new Node[]{Node.createName("myFunction")});
    Node script = Node.createBlock(new Node[]{functionInScript});
    Node getThisInScript = Node.createGetProp(Node.createName("this"), new Node[]{script});

    Node[] nodes = new Node[]{
            functionPrototype,
            function,
            constructor,
            constructorWithJsDoc,
            getThis,
            setThis,
            functionWithAssign,
            functionAssignJsDoc,
            functionPrototypeProperty,
            functionPrototypePropertySetter,
            functionPrototypeChainProperty,
            functionPrototypeChainPropertySetter,
            functionInObject,
            getThisInObject,
            functionInBlock,
            getThisInBlock,
            functionInScript,
            getThisInScript};

    Node[] expectedNodes = new Node[]{
            functionPrototype,
            function,
            constructor,
            constructorWithJsDoc,
            getThis,
            setThis,
            functionWithAssign,
            functionAssign,
            functionPrototypeProperty,
            functionPrototypePropertySetter,
            functionPrototypeChainProperty,
            functionPrototypeChainPropertySetter,
            functionInObject,
            getThisInObject,
            functionInBlock,
            getThisInBlock,
            functionInScript,
            getThisInScript};

    for (int i = 0; i < nodes.length; ++i) {
      Node currentNode = nodes[i];

      CheckGlobalThis checker = createCheckGlobalThis(compiler, WARNING);

      NodeTraversal traversal = NodeTraversal.create(compiler);
      Node parent = null;
      if (currentNode != null && currentNode.getNext() != null) {
        parent = currentNode.getPrevious();
      }

      checker.visit(traversal, currentNode, parent);

      assertEquals(currentNode, checker.assignLhsChild);
    }

    for (int i = 0; i < nodes.length; ++i) {
      Node currentNode = nodes[i];
      if (currentNode instanceof Node && currentNode.getType() == Token.THIS) {
        CheckGlobalThis checker = createCheckGlobalThis(compiler, WARNING);

        NodeTraversal traversal = NodeTraversal.create(compiler);
        Node parent = null;
        if (currentNode != null && currentNode.getParent() != null) {
          parent = currentNode.getParent();
        }

        assertTrue(checker.shouldTraverse(traversal, currentNode, parent));
        if (shouldReportThis(currentNode, parent)) {
          assertTrue(checker.shouldReportThis(currentNode, parent));
          checker.visit(traversal, currentNode, parent);
          assertEquals(DiagnosticType.WARNING, checker.compiler.getLastDiagnostic().getType());
        } else {
          assertFalse(checker.shouldReportThis(currentNode, parent));
          assertNull(checker.compiler.getLastDiagnostic());
        }
      }
    }
  }

  private static boolean shouldReportThis(Node node, Node parent) {
    if (node instanceof Node && node.getType() == Token.THIS) {
      if (parent != null && NodeUtil.isGet(parent)) {
        return true;
      }
      return node.getParent() != null;
    }
    return false;
  }
}