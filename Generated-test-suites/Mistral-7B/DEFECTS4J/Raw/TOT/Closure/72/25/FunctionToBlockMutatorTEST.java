import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.NodeTraversal.*;

import com.google.common.base.Function;
import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionArgumentInjector;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.jscomp.FunctionToBlockMutator.MakeDeclaredNamesUnique.InlineRenamer;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tests for {@link FunctionToBlockMutator} class.
 */
public class FunctionToBlockMutatorTest {

  AbstractCompiler compiler;
  FunctionToBlockMutator functionToBlockMutator;
  FunctionArgumentInjector functionArgumentInjector;
  MakeDeclaredNamesUnique makeDeclaredNamesUnique;

  @Before
  public void setUp() {
    compiler = new Compiler();
    functionToBlockMutator = new FunctionToBlockMutator(compiler, new Supplier<String>() {
      int i = 0;

      @Override
      public String get() {
        return "test_" + ++i;
      }
    });
    functionArgumentInjector = new FunctionArgumentInjector(compiler);
    makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
  }

  @Test
  public void testMutateFunctionWithNoArgumentsAndNoReturnValue() {
    String fnName = "foo";
    Node fnNode = makeFunctionNode(fnName);
    Node callNode = makeCallNode(fnName);
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, null, false, false);

    assertEquals("should be injectable block",
        makeFunctionNode(fnName, null, null, resultNode),
        resultNode);
  }

  @Test
  public void testMutateFunctionWithNoArgumentsAndReturnValue() {
    String fnName = "foo";
    Node fnNode = makeFunctionNode(fnName, null, 0, null);
    Node callNode = makeCallNode(fnName, null);
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", true, false);

    assertEquals("should be injectable block with result assignment",
        makeFunctionNode(fnName, null, 0, NodeUtil.newAssignStatementNode("result", resultNode)),
        resultNode);
  }

  @Test
  public void testMutateFunctionWithArgumentsAndNoReturnValue() {
    String fnName = "foo";
    Node fnNode = makeFunctionNode(fnName, Arrays.asList("arg1", "arg2"), 0, null);
    Node callNode = makeCallNode(fnName, Arrays.asList("arg1", "arg2"), null);
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, null, false, false);

    assertEquals("should be injectable block without result assignment",
        makeFunctionNode(fnName, Arrays.asList("arg1", "arg2"), null, resultNode),
        resultNode);
  }

  @Test
  public void testMutateFunctionWithArgumentsAndReturnValue() {
    String fnName = "foo";
    Node fnNode = makeFunctionNode(fnName, Arrays.asList("arg1", "arg2"), 0, null);
    Node callNode = makeCallNode(fnName, Arrays.asList("arg1", "arg2"), null);
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", true, false);

    assertEquals("should be injectable block with result assignment",
        makeFunctionNode(fnName, Arrays.asList("arg1", "arg2"), 0,
            NodeUtil.newAssignStatementNode("result", resultNode)),
        resultNode);
  }

  @Test
  public void testMutateFunctionWithArgumentsAndCallInLoop() {
    String fnName = "foo";
    Node fnNode = makeFunctionNode(fnName, Arrays.asList("arg1", "arg2"), 0, null);
    Node callNode = makeCallNode(fnName, Arrays.asList("arg1", "arg2"), null);
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", true, true);

    assertEquals("should be injectable block with initialized variables",
        makeFunctionNode(fnName, Arrays.asList("arg1", "arg2"), 0, resultNode),
        resultNode);
  }

  @Test
  public void testMutateFunctionWithArgumentsAndModifiedParameters() {
    String fnName = "foo";
    Node fnNode = makeFunctionNode(fnName, Arrays.asList("arg1", "arg2"), 0, null);
    Node callNode = makeCallNode(fnName, Arrays.asList("arg1", "arg2"), null);
    Set<String> namesToAlias = Sets.newHashSet("arg1");
    LinkedHashMap<String, Node> args =
        FunctionArgumentInjector.getFunctionCallParameterMap(fnNode, callNode, new LabelNameSupplier(new Supplier<String>() {
          int i = 0;

          @Override
          public String get() {
            return "test_alias_" + ++i;
          }
        }));
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", true, false);

    assertEquals("should be injectable block with aliased arguments",
        makeFunctionNode(fnName, Arrays.asList("arg1", "arg2"), null,
            NodeUtil.newAssignStatementNode("arg1", args.get("arg1"))),
        resultNode);
  }

  @Test(expected = IllegalStateException.class)
  public void testMutateFunctionWithArgumentsAndModifiedParametersAndCallInLoop() {
    String fnName = "foo";
    Node fnNode = makeFunctionNode(fnName, Arrays.asList("arg1", "arg2"), 0, null);
    Node callNode = makeCallNode(fnName, Arrays.asList("arg1", "arg2"), null);
    Set<String> namesToAlias = Sets.newHashSet("arg1");
    LinkedHashMap<String, Node> args =
        FunctionArgumentInjector.getFunctionCallParameterMap(fnNode, callNode, new LabelNameSupplier(new Supplier<String>() {
          int i = 0;

          @Override
          public String get() {
            return "test_alias_" + ++i;
          }
        }));
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", true, true);

    fail("should not be injectable block with aliased arguments in for loop");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionWithTooManyArguments() {
    String fnName = "foo";
    Node fnNode = makeFunctionNode(fnName, Arrays.asList("arg1", "arg2", "arg3"), 0, null);
    Node callNode = makeCallNode(fnName, Arrays.asList("arg1", "arg2"), null);
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", true, false);

    fail("should not be injectable block with too many arguments");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionWithMissingArgument() {
    String fnName = "foo";
    Node fnNode = makeFunctionNode(fnName, Arrays.asList("arg1"), 0, null);
    Node callNode = makeCallNode(fnName, Arrays.asList(), null);
    Node resultNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, "result", true, false);

    fail("should not be injectable block with missing argument");
  }

  private Node makeFunctionNode(String name, List<String> parameters, int bodyType, Node body) {
    Node function = new Node(Token.FUNCTION);
    Node funcName = Node.newString(Token.FUNCTION_NAME, name);
    function.addChildToFront(funcName);
    function.addChildrenToBack(makeParameterListNode(parameters));
    function.setBody(bodyType, body);
    return function;
  }

  private Node makeCallNode(String name, List<Node> arguments, Node sourceNode) {
    Node call = new Node(Token.CALL);
    Node callee = Node.newString(Token.NAME, name);
    call.addChildToFront(callee);
    for (Node arg : arguments) {
      call.addChildToBack(arg);
    }
    call.setParent(sourceNode);
    return call;
  }

  private Node makeParameterListNode(List<String> parameters) {
    Node parameterList = new Node(Token.LIST);
    for (String name : parameters) {
      Node param = new Node(Token.VAR);
      Node nameNode = Node.newString(Token.VAR_NAME, name);
      Node initializer = null;
      param.addChildrenToBack(nameNode, initializer);
      parameterList.addChildToBack(param);
    }
    return parameterList;
  }
}