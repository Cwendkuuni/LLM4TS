import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.FunctionToBlockMutator.*;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = newNode("function", "testFunction", Lists.newArrayList(
        newNode("param", null, null, "p"),
        newNode("return", null, newNode("expr", null, newNode("num", null, 42)))
    ));
    Node callNode = newNode("expr", null, fnNode);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Check that the function body has been transformed
    Node functionBody = NodeUtil.getFunctionBody(mutatedNode);
    assertEquals(2, functionBody.getChildCount());

    // Check that the return statement has been replaced by an assignment to 'result'
    Node returnNode = fnNode.getLastChild();
    Node expectedAssignment = newNode("expr", null, newNode("assign", null, newNode("name", null, resultName), newNode("num", null, 42)));
    Node actualAssignment = functionBody.getLastChild();
    assertEquals(expectedAssignment, actualAssignment);

    // Check that the label name has been set
    String labelName = getLabelNameForFunction(fnName);
    Node labelNode = mutatedNode;
    while (labelNode != null) {
      Node nameNode = labelNode.getFirstChild();
      assertEquals("JSCompiler_inline_label_testFunction_test", nameNode.getString());
      labelNode = labelNode.getNextSibling();
    }
  }

  @Test
  public void testReplaceReturns() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = newNode("function", "testFunction", Lists.newArrayList(
        newNode("param", null, null, "p"),
        newNode("return", null, newNode("expr", null, newNode("num", null, 42)))
    ));
    String resultName = "result";
    String labelName = "testLabel";
    boolean needsDefaultResult = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, null, resultName, needsDefaultResult, false);
    Node replacedNode = replaceReturns(mutatedNode, resultName, labelName, needsDefaultResult);

    // Check that the node has been transformed into a block with a break statement
    Node expectedBlock = newNode("block", null, Lists.newArrayList(
        newNode("expr", null, newNode("name", null, resultName)),
        new Node("break", new Node("label", null, new Node("name", null, labelName)))
    ));
    Node actualBlock = replacedNode;
    while (actualBlock != null && actualBlock.getType() != Token.BLOCK) {
      actualBlock = actualBlock.getNextSibling();
    }
    assertEquals(expectedBlock, actualBlock);
  }

  @Test
  public void testAliasAndInlineArguments() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = newNode("function", "testFunction", Lists.newArrayList(
        newNode("param", null, null, "p"),
        newNode("param", null, null, "q"),
        newNode("return", null, newNode("expr", null, newNode("binop", null, newNode("name", null, "p"), newNode("name", null, "q"), Token.MINUS)))
    ));
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("p", newNode("name", null, "pOriginal"));
    argMap.put("q", newNode("name", null, "qOriginal"));
    Set<String> namesToAlias = Sets.newHashSet("p");

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, null, null, false, false);
    Node aliasAndInlineNode = aliasAndInlineArguments(mutatedNode, argMap, namesToAlias);

    // Check that the arguments have been aliased and inlined
    Node expectedBlock = newNode("block", null, Lists.newArrayList(
        newNode("var", null, newNode("name", null, "pAlias"), null),
        newNode("var", null, newNode("name", null, "qAlias"), null),
        newNode("expr", null, newNode("assign", null, newNode("name", null, "pAlias"), newNode("name", null, "pOriginal"))),
        newNode("expr", null, newNode("assign", null, newNode("name", null, "qAlias"), newNode("name", null, "qOriginal"))),
        newNode("expr", null, newNode("binop", null, newNode("name", null, "pAlias"), newNode("name", null, "qAlias"), Token.MINUS))
    ));
    Node actualBlock = aliasAndInlineNode;
    while (actualBlock != null && actualBlock.getType() != Token.BLOCK) {
      actualBlock = actualBlock.getNextSibling();
    }
    assertEquals(expectedBlock, actualBlock);
  }

  @Test
  public void testFixUnitializedVarDeclarations() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = newNode("function", "testFunction", Lists.newArrayList(
        newNode("var", null, null, "p", null, newNode("num", null, 1)),
        newNode("return", null, newNode("expr", null, newNode("name", null, "p")))
    ));
    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, null, null, false, false);

    Node expectedBlock = newNode("block", null, Lists.newArrayList(
        newNode("var", null, null, "p", null, newNode("name", null, "p"), newNode("num", null, 1)),
        newNode("return", null, newNode("name", null, "p"))
    ));
    Node actualBlock = mutatedNode;
    while (actualBlock != null && actualBlock.getType() != Token.BLOCK) {
      actualBlock = actualBlock.getNextSibling();
    }
    assertEquals(expectedBlock, actualBlock);
  }
}