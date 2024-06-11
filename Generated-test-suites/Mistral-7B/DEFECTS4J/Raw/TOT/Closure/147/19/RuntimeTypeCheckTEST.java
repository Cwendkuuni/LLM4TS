import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() {
    String sourceCode = "function Foo() {}";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setClassNames(Sets.newHashSet("Foo"));
    Node root = NodeUtil.parse(sourceCode, null, options);

    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);

    // Check that Foo instance has the expected markers
    Node fooInstance = NodeUtil.newQualifiedNameNode("Foo", -1, -1);
    Node prototype = NodeUtil.getGetProperty(fooInstance, "prototype");
    Node checkInstanceOfFoo = NodeUtil.getGetProperty(prototype, "instance_of__Foo");
    Node checkImplementsObject = NodeUtil.getGetProperty(prototype, "implements__Object");

    assertThat(checkInstanceOfFoo).isNotNull();
    assertThat(checkImplementsObject).isNotNull();

    // Check that a function with a string parameter has the expected check
    Node functionWithStringParam = NodeUtil.newFunctionNode(
        "FunctionWithStringParam", null, ImmutableList.of(NodeUtil.newIdentifierNode("str")),
        Node.newBlock(new Node(Token.RETURN, NodeUtil.newIdentifierNode("str"))));
    Node rootWithFunction = new Node(Token.BLOCK, ImmutableList.of(root, functionWithStringParam));
    Node functionNode = NodeUtil.getFunctionDeclaration(functionWithStringParam);

    runtimeTypeCheck.process(null, rootWithFunction);

    Node stringChecker = NodeUtil.getFunctionDeclaration(functionNode).getBody().getLastChild();
    assertThat(stringChecker.getLastChild()).isInstanceOf(Node.CALL);
    Node callNode = (Node) stringChecker.getLastChild();
    assertThat(callNode.getFirstChild()).isInstanceOf(jsTypeCheckerNodeFor("valueChecker", "string"));

    // Check that a function with an array parameter has checks for all elements
    Node functionWithArrayParam = NodeUtil.newFunctionNode(
        "FunctionWithArrayParam", null, ImmutableList.of(NodeUtil.newIdentifierNode("arr")),
        Node.newBlock(new Node(Token.RETURN, NodeUtil.newIdentifierNode("arr"))));
    Node rootWithArrayFunction = new Node(Token.BLOCK, ImmutableList.of(root, functionWithArrayParam));
    Node functionNodeWithArrayParam = NodeUtil.getFunctionDeclaration(functionWithArrayParam);

    runtimeTypeCheck.process(null, rootWithArrayFunction);

    Node arrayChecker = NodeUtil.getFunctionDeclaration(functionNodeWithArrayParam).getBody();
    Node arrayExpression = arrayChecker.getChildren().get(0);
    for (Node element : arrayExpression.getChildren()) {
      Node checkNode = (Node) element;
      Node checker = checkNode.getFirstChild();
      assertThat(checker).isInstanceOf(Node.CALL);
      Node callNode = (Node) checker;
      assertThat(callNode.getFirstChild()).isInstanceOf(jsTypeCheckerNodeFor("valueChecker", "number"));
    }

    // Check that a function with a nullable parameter has a checker for null
    Node functionWithNullableParam = NodeUtil.newFunctionNode(
        "FunctionWithNullableParam", null, ImmutableList.of(NodeUtil.newIdentifierNode("num")),
        Node.newBlock(new Node(Token.RETURN, NodeUtil.newIdentifierNode("num"))));
    Node rootWithNullableFunction = new Node(Token.BLOCK, ImmutableList.of(root, functionWithNullableParam));
    Node functionNodeWithNullableParam = NodeUtil.getFunctionDeclaration(functionWithNullableParam);

    runtimeTypeCheck.process(null, rootWithNullableFunction);

    Node nullChecker = NodeUtil.getFunctionDeclaration(functionNodeWithNullableParam).getBody().getLastChild();
    Node nullCheckNode = (Node) nullChecker.getLastChild();
    assertThat(nullCheckNode).isInstanceOf(Node.CALL);
    Node nullCallNode = (Node) nullCheckNode;
    assertThat(nullCallNode.getFirstChild()).isInstanceOf(jsTypeCheckerNodeFor("nullChecker", null));

    // Check that a constructor call has checks for its constructor and arguments
    Node constructorCallNode = NodeUtil.newQualifiedNameNode("Foo", -1, -1);
    Node constructorCall = new Node(Token.CALL, constructorCallNode);
    Node rootWithConstructorCall = new Node(Token.BLOCK, ImmutableList.of(root, constructorCall));

    runtimeTypeCheck.process(null, rootWithConstructorCall);

    Node constructorChecker = NodeUtil.getFunctionDeclaration(functionNode).getBody().getLastChild();
    Node constructorCheckNode = (Node) constructorChecker.getLastChild();
    Node constructorCallCheckNode = (Node) constructorCheckNode.getLastChild();
    Node constructorNameNode = constructorCallCheckNode.getFirstChild();
    Node constructorNode = NodeUtil.getFunctionDeclaration(constructorNameNode);
    Node constructorType = constructorNode.getJSType();

    Node argumentsChecker = constructorChecker.getPreviousSibling();
    Node argumentsCheckNode = (Node) argumentsChecker.getLastChild();
    Node argumentsListNode = argumentsCheckNode.getFirstChild();
    Node argumentsList = argumentsListNode.getLastChild();

    Node firstArgument = argumentsList.getChildren().get(0);
    Node firstArgumentChecker = (Node) firstArgument;
    Node firstArgumentTypeNode = firstArgumentChecker.getFirstChild();
    Node firstArgumentType = firstArgumentTypeNode.getLastChild();
    Node firstArgumentTypeCheckNode = (Node) firstArgumentChecker.getLastChild();
    Node firstArgumentCheckNode = (Node) firstArgumentCheckNode.getLastChild();

    Node secondArgument = argumentsList.getChildren().get(1);
    Node secondArgumentChecker = (Node) secondArgument;
    Node secondArgumentTypeNode = secondArgumentChecker.getFirstChild();
    Node secondArgumentType = secondArgumentTypeNode.getLastChild();
    Node secondArgumentTypeCheckNode = (Node) secondArgumentChecker.getLastChild();
    Node secondArgumentCheckNode = (Node) secondArgumentCheckNode.getLastChild();

    Node constructorCheckerType = constructorChecker.getJSType();
    Node constructorCheckerFunctionType = (FunctionType) constructorCheckerType;
    Node constructorCheckerFunctionParams = constructorCheckerFunctionType.getParameters();

    Node constructorCheckerFunctionParam0 = constructorCheckerFunctionParams.get(0);
    Node constructorCheckerFunctionParam0Type = constructorCheckerFunctionParam0.getJSType();
    Node constructorCheckerFunctionParam0Name = constructorCheckerFunctionParam0Type.getName();

    Node constructorCheckerFunctionParam1 = constructorCheckerFunctionParams.get(1);
    Node constructorCheckerFunctionParam1Type = constructorCheckerFunctionParam1.getJSType();
    Node constructorCheckerFunctionParam1Name = constructorCheckerFunctionParam1Type.getName();

    assertThat(constructorCheckerType).isInstanceOf(FunctionType.class);
    assertThat(constructorCheckerType.getSourceName()).isEqualTo("jscomp.RuntimeTypeCheck.checkConstructor");
    assertThat(constructorType).isInstanceOf(ObjectType.class);
    assertThat(constructorType.getReferenceName()).isEqualTo("Foo");
    assertThat(constructorCheckerFunctionParams.size()).isEqualTo(2);
    assertThat(constructorCheckerFunctionParam0Name).isEqualTo("ctor");
    assertThat(constructorCheckerFunctionParam1Name).isEqualTo("args");
    assertThat(constructorCheckerFunctionParam0Type.getReferenceName()).isEqualTo("Function");
    assertThat(firstArgumentType).isInstanceOf(ObjectType.class);
    assertThat(firstArgumentType.getReferenceName()).isEqualTo("Object");
    assertThat(secondArgumentType).isInstanceOf(UnionType.class);
    assertThat(secondArgumentType.getAlternates().size()).isEqualTo(2);
    Node secondArgumentTypeAlternate0 = secondArgumentType.getAlternates().get(0);
    Node secondArgumentTypeAlternate0Type = secondArgumentTypeAlternate0.getJSType();
    Node secondArgumentTypeAlternate0TypeName = secondArgumentTypeAlternate0Type.getName();
    Node secondArgumentTypeAlternate1 = secondArgumentType.getAlternates().get(1);
    Node secondArgumentTypeAlternate1Type = secondArgumentTypeAlternate1.getJSType();
    Node secondArgumentTypeAlternate1TypeName = secondArgumentTypeAlternate1Type.getName();
    assertThat(secondArgumentTypeAlternate0TypeName).isEqualTo("number");
    assertThat(secondArgumentTypeAlternate1TypeName).isEqualTo("string");
    assertThat(firstArgumentCheckNode.getLastChild().getLastChild()).isInstanceOf(Node.TRUE);
    assertThat(secondArgumentCheckNode.getLastChild().getLastChild()).isInstanceOf(Node.TRUE);
    assertThat(constructorCallCheckNode.getLastChild().getLastChild()).isInstanceOf(Node.TRUE);
  }

  private Node jsTypeCheckerNodeFor(String checkerName, Object checkerArgument) {
    return NodeUtil.getQualifiedNameNode(
        "jscomp.typecheck." + checkerName, -1, -1)
        .append(new Node(Token.CALL, null, Node.newArray(ImmutableList.of(NodeUtil.newLiteralNode(checkerArgument)))));
  }
}