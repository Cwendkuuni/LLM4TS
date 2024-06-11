import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

/**
 * Tests for {@link MethodCompilerPass} class.
 */
public class MethodCompilerPassTest {

  private static class TestMethodCompilerPass extends MethodCompilerPass {

    @Override
    SignatureStore getSignatureStore() {
      return new SignatureStore() {
        private Multimap<String, Node> signatures = HashMultimap.create();

        @Override
        public void reset() {
          signatures.clear();
        }

        @Override
        public void addSignature(String functionName, Node functionNode, String sourceFile) {
          signatures.put(functionName, functionNode);
        }

        @Override
        public void removeSignature(String functionName) {
          signatures.remove(functionName);
        }

        public Node getFunctionByName(String functionName) {
          return signatures.get(functionName).get(0);
        }
      };
    }

    @Override
    Callback getActingCallback() {
      // no-op
      return new Callback() {};
    }
  }

  @org.junit.Test
  public void testBasicFunctionDeclaration() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.newProgram();
    NodeTraversal traverser = new NodeTraversal(root);

    // function Foo() { ... }
    Node functionNode = Node.newFunction(Node.newIdentifier("Foo"), null, null);
    Node functionDeclaration = Node.newExpressionStatement(functionNode);
    Node functionDeclarationParent = Node.newAssign(Node.newIdentifier("Foo"), functionDeclaration);
    Node rootFunctionParent = Node.newExpressionStatement(functionDeclarationParent);
    Node rootFunction = Node.newProgram();
    rootFunction.addChild(rootFunctionParent);

    compilerPass.process(null, rootFunction);

    Node functionInSignatures = compilerPass.getSignatureStore().getFunctionByName("Foo");
    assertSame(functionNode, functionInSignatures);
  }

  @org.junit.Test
  public void testFunctionDeclarationWithArguments() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.newProgram();
    NodeTraversal traverser = new NodeTraversal(root);

    // function Foo(a, b) { ... }
    Node functionNode = Node.newFunction(
        Node.newIdentifier("Foo"),
        Node.newList(
            Node.newIdentifier("a"),
            Node.newIdentifier("b")
        ), null);
    Node functionDeclaration = Node.newExpressionStatement(functionNode);
    Node functionDeclarationParent = Node.newAssign(Node.newIdentifier("Foo"), functionDeclaration);
    Node rootFunctionParent = Node.newExpressionStatement(functionDeclarationParent);
    Node rootFunction = Node.newProgram();
    rootFunction.addChild(rootFunctionParent);

    compilerPass.process(null, rootFunction);

    Node functionInSignatures = compilerPass.getSignatureStore().getFunctionByName("Foo");
    assertSame(functionNode, functionInSignatures);

    List<Node> arguments = functionNode.getParameters();
    assertEquals(2, arguments.size());
    assertEquals(Node.newIdentifier("a"), arguments.get(0));
    assertEquals(Node.newIdentifier("b"), arguments.get(1));
  }

  @org.junit.Test
  public void testFunctionDeclarationWithoutName() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.newProgram();
    NodeTraversal traverser = new NodeTraversal(root);

    // function() { ... }
    Node functionNode = Node.newFunction(null, null, null);
    Node functionDeclaration = Node.newExpressionStatement(functionNode);
    Node functionDeclarationParent = Node.newAssign(Node.newIdentifier("anon"), functionDeclaration);
    Node rootFunctionParent = Node.newExpressionStatement(functionDeclarationParent);
    Node rootFunction = Node.newProgram();
    rootFunction.addChild(rootFunctionParent);

    compilerPass.process(null, rootFunction);

    assertFalse(compilerPass.externMethods.contains("anon"));
    assertFalse(compilerPass.methodDefinitions.containsKey("anon"));
  }

  @org.junit.Test
  public void testFunctionDeclarationInExterns() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.newProgram();
    NodeTraversal traverser = new NodeTraversal(root);

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty(Node.newIdentifier("Foo"), Node.newFunction(
        Node.newIdentifier("Foo"), null, null
    )));
    Node rootWithExterns = Node.newProgram();
    rootWithExterns.addChild(Node.newAssign(Node.newIdentifier("externs"), externs));
    rootWithExterns.addChild(Node.newAssign(Node.newIdentifier("Foo"), Node.newIdentifier("Foo")));

    compilerPass.process(Node.newIdentifier("externs"), rootWithExterns);

    Node functionInSignatures = compilerPass.getSignatureStore().getFunctionByName("Foo");
    assertSame(functionNode, functionInSignatures);
  }

  @org.junit.Test
  public void testMethodWithSameNameButDifferentParents() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.newProgram();
    NodeTraversal traverser = new NodeTraversal(root);

    // function Foo() { ... }
    Node functionNode1 = Node.newFunction(Node.newIdentifier("Foo"), null, null);
    Node functionDeclaration1 = Node.newExpressionStatement(functionNode1);
    Node functionDeclarationParent1 = Node.newAssign(Node.newIdentifier("Foo1"), functionDeclaration1);
    Node rootFunctionParent1 = Node.newExpressionStatement(functionDeclarationParent1);
    Node rootFunction1 = Node.newProgram();
    rootFunction1.addChild(rootFunctionParent1);

    // function Foo() { ... }
    Node functionNode2 = Node.newFunction(Node.newIdentifier("Foo"), null, null);
    Node functionDeclaration2 = Node.newExpressionStatement(functionNode2);
    Node functionDeclarationParent2 = Node.newAssign(Node.newIdentifier("Foo2"), functionDeclaration2);
    Node rootFunctionParent2 = Node.newExpressionStatement(functionDeclarationParent2);
    Node rootFunction2 = Node.newProgram();
    rootFunction2.addChild(rootFunctionParent2);

    compilerPass.process(null, Node.newProgram(rootFunction1, rootFunction2));

    Node functionInSignatures1 = compilerPass.getSignatureStore().getFunctionByName("Foo");
    assertSame(functionNode1, functionInSignatures1);

    Node functionInSignatures2 = compilerPass.getSignatureStore().getFunctionByName("Foo");
    assertSame(functionNode2, functionInSignatures2);

    assertTrue(compilerPass.methodDefinitions.containsKey("Foo1"));
    assertTrue(compilerPass.methodDefinitions.containsKey("Foo2"));
  }

  @org.junit.Test
  public void testMethodWithPrototype() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.newProgram();
    NodeTraversal traverser = new NodeTraversal(root);

    // function Foo() { this.getBar = function() { ... } }
    Node functionNode = Node.newFunction(Node.newIdentifier("Foo"), null, null);
    Node setPrototype = Node.newAssign(functionNode, Node.newProperty(Node.newThis(), "prototype"));
    Node prototype = Node.newObjectLiteral();
    prototype.addChild(Node.newProperty(Node.newIdentifier("getBar"), Node.newFunction(
        null, Node.newList(Node.newThis()), null
    )));
    setPrototype.setNext(prototype);
    Node functionDeclaration = Node.newExpressionStatement(functionNode);
    Node functionDeclarationParent = Node.newAssign(Node.newIdentifier("Foo"), functionDeclaration);
    Node rootFunctionParent = Node.newExpressionStatement(functionDeclarationParent);
    Node rootFunction = Node.newProgram();
    rootFunction.addChild(rootFunctionParent);

    compilerPass.process(null, rootFunction);

    Node functionInSignatures = compilerPass.getSignatureStore().getFunctionByName("getBar");
    assertSame(functionNode.getChild(1), functionInSignatures);

    Node methodDefinition = compilerPass.methodDefinitions.get("Foo").getValue();
    Node prototypeNode = methodDefinition.getFirstChild();
    Node prototypeProperty = prototypeNode.getFirstChild();
    assertEquals(Node.newIdentifier("getBar"), prototypeProperty.getLastChild());
  }

  @org.junit.Test
  public void testPropertyWithSameNameButDifferentParents() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.newProgram();
    NodeTraversal traverser = new NodeTraversal(root);

    // property a { get Foo() { ... } }
    Node propertyNode1 = Node.newProperty(Node.newIdentifier("a"), Node.newObjectLiteral());
    Node setGetter1 = Node.newAssign(propertyNode1, Node.newProperty(Node.newIdentifier("get"), Node.newIdentifier("Foo")));
    Node getter1 = Node.newFunction(null, Node.newList(Node.newThis()), null);
    setGetter1.setNext(getter1);
    Node objectLiteral1 = Node.newObjectLiteral();
    setGetter1.setNext(objectLiteral1);
    Node functionDeclaration1 = Node.newExpressionStatement(Node.newAssign(Node.newIdentifier("Foo1"), propertyNode1));
    Node functionDeclarationParent1 = Node.newExpressionStatement(functionDeclaration1);
    Node rootFunctionParent1 = Node.newExpressionStatement(functionDeclarationParent1);
    Node rootFunction1 = Node.newProgram();
    rootFunction1.addChild(rootFunctionParent1);

    // property a { get Foo() { ... } }
    Node propertyNode2 = Node.newProperty(Node.newIdentifier("a"), Node.newObjectLiteral());
    Node setGetter2 = Node.newAssign(propertyNode2, Node.newProperty(Node.newIdentifier("get"), Node.newIdentifier("Foo")));
    Node getter2 = Node.newFunction(null, Node.newList(Node.newThis()), null);
    setGetter2.setNext(getter2);
    Node objectLiteral2 = Node.newObjectLiteral();
    setGetter2.setNext(objectLiteral2);
    Node functionDeclaration2 = Node.newExpressionStatement(Node.newAssign(Node.newIdentifier("Foo2"), propertyNode2));
    Node functionDeclarationParent2 = Node.newExpressionStatement(functionDeclaration2);
    Node rootFunctionParent2 = Node.newExpressionStatement(functionDeclarationParent2);
    Node rootFunction2 = Node.newProgram();
    rootFunction2.addChild(rootFunctionParent2);

    compilerPass.process(null, Node.newProgram(rootFunction1, rootFunction2));

    Node propertyInSignatures1 = compilerPass.getSignatureStore().getFunctionByName("Foo");
    assertSame(getter1, propertyInSignatures1);

    Node propertyInSignatures2 = compilerPass.getSignatureStore().getFunctionByName("Foo");
    assertSame(getter2, propertyInSignatures2);

    assertTrue(compilerPass.methodDefinitions.containsKey("Foo1"));
    assertTrue(compilerPass.methodDefinitions.containsKey("Foo2"));
  }

  @org.junit.Test
  public void testMethodWithoutSignatureInExterns() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.newProgram();
    NodeTraversal traverser = new NodeTraversal(root);

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty(Node.newIdentifier("Foo"), Node.newFunction(
        Node.newIdentifier("Foo"), null, null
    )));
    Node rootWithExterns = Node.newProgram();
    rootWithExterns.addChild(Node.newAssign(Node.newIdentifier("externs"), externs));
    Node functionDeclaration = Node.newExpressionStatement(Node.newAssign(Node.newIdentifier("Foo"), Node.newIdentifier("Foo")));
    Node rootFunctionParent = Node.newExpressionStatement(functionDeclaration);
    Node rootFunction = Node.newProgram();
    rootFunction.addChild(rootFunctionParent);

    compilerPass.process(Node.newIdentifier("externs"), rootWithExterns);

    assertFalse(compilerPass.methodDefinitions.containsKey("Foo"));
  }

  @org.junit.Test
  public void testMethodWithoutSignatureInCode() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.newProgram();
    NodeTraversal traverser = new NodeTraversal(root);

    Node functionDeclaration = Node.newExpressionStatement(Node.newAssign(Node.newIdentifier("Foo"), Node.newIdentifier("Foo")));
    Node rootFunctionParent = Node.newExpressionStatement(functionDeclaration);
    Node rootFunction = Node.newProgram();
    rootFunction.addChild(rootFunctionParent);

    compilerPass.process(null, rootFunction);

    assertFalse(compilerPass.methodDefinitions.containsKey("Foo"));
  }

  @org.junit.Test
  public void testNonMethodProperty() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.newProgram();
    NodeTraversal traverser = new NodeTraversal(root);

    Node propertyDeclaration = Node.newExpressionStatement(Node.newAssign(Node.newIdentifier("a"), Node.newString("foo")));
    Node rootFunctionParent = Node.newExpressionStatement(propertyDeclaration);
    Node rootFunction = Node.newProgram();
    rootFunction.addChild(rootFunctionParent);

    compilerPass.process(null, rootFunction);

    assertTrue(compilerPass.nonMethodProperties.contains("a"));
    assertFalse(compilerPass.methodDefinitions.containsKey("a"));
  }
}