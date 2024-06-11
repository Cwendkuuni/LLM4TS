package org.apache.commons.cli.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import org.apache.commons.cli.FunctionRewriter;
import org.apache.commons.cli.FunctionRewriter.Reducer;
import org.apache.commons.cli.FunctionRewriter.Reduction;

import java.util.List;

public class FunctionRewriterTest {

    @Test
    public void testEmptyFunctionReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        Node root = new Node(Token.BLOCK, 1, 1);
        Node emptyFunction = new Node(Token.FUNCTION, 1, 1);
        emptyFunction.putBooleanProp(Node.IS_EXPRESSION, true);
        emptyFunction.addChildToBack(new Node(Token.RETURN, 1, 1));
        emptyFunction.addChildToBack(new Node(Token.BLOCK, 1, 1));
        root.addChildToBack(emptyFunction);

        FunctionRewriter.Reducer reducer = new FunctionRewriter.EmptyFunctionReducer();
        Node helperCode = functionRewriter.parseHelperCode(reducer);

        NodeTraversal traversal = new NodeTraversal(functionRewriter.compiler);
        traversal.traverse(root, new FunctionRewriter.ReductionGatherer(
                List.of(reducer), null));

        List<FunctionRewriter.Reduction> reductions = ((FunctionRewriter.ReductionGatherer) traversal.getUserData())
                .getReductions();

        for (Reduction reduction : reductions) {
            reduction.apply();
        }

        Node newRoot = functionRewriter.compiler.getNodeForCodeInsertion(null);
        newRoot.addChildrenToFront(helperCode);

        Node result = functionRewriter.compiler.getRootNode();
        assertNotNull(helperCode);
        assertNull(emptyFunction);
    }

    @Test
    public void testIdentityReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        Node root = new Node(Token.BLOCK, 1, 1);
        Node identityFunction = new Node(Token.FUNCTION, 1, 1);
        identityFunction.putBooleanProp(Node.IS_EXPRESSION, true);
        identityFunction.addChildToBack(new Node(Token.ARGUMENTS, 1, 1));
        identityFunction.addChildToBack(new Node(Token.RETURN, 1, 1));
        identityFunction.addChildToBack(new Node(Token.FUNCTION, 1, 1));
        identityFunction.getLastChild().addChildToBack(new Node(Token.RETURN, 1, 1));
        identityFunction.getLastChild().addChildToBack(new Node(Token.BLOCK, 1, 1));
        identityFunction.addChildToBack(new Node(Token.IDENTIFIER, 1, 1, "a"));
        identityFunction.addChildToBack(new Node(Token.IDENTIFIER, 1, 1, "this"));
        identityFunction.addChildToBack(new Node(Token.IDENTIFIER, 1, 1, "return"));
        identityFunction.addChildToBack(new Node(Token.ASSIGN, 1, 1,
                Node.newQualifiedIdentifier(new Node(Token.IDENTIFIER, 1, 1, "this"),
                        new Node(Token.IDENTIFIER, 1, 1, "a"))));
        root.addChildToBack(identityFunction);

        FunctionRewriter.Reducer reducer = new FunctionRewriter.IdentityReducer();
        Node helperCode = functionRewriter.parseHelperCode(reducer);

        NodeTraversal traversal = new NodeTraversal(functionRewriter.compiler);
        traversal.traverse(root, new FunctionRewriter.ReductionGatherer(
                List.of(reducer), null));

        List<FunctionRewriter.Reduction> reductions = ((FunctionRewriter.ReductionGatherer) traversal.getUserData())
                .getReductions();

        for (Reduction reduction : reductions) {
            reduction.apply();
        }

        Node newRoot = functionRewriter.compiler.getNodeForCodeInsertion(null);
        newRoot.addChildrenToFront(helperCode);

        Node result = functionRewriter.compiler.getRootNode();
        assertNotNull(helperCode);
        assertNull(identityFunction);
    }

    @Test
    public void testReturnConstantReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        Node root = new Node(Token.BLOCK, 1, 1);
        Node returnConstantFunction = new Node(Token.FUNCTION, 1, 1);
        returnConstantFunction.putBooleanProp(Node.IS_EXPRESSION, true);
        returnConstantFunction.addChildToBack(new Node(Token.ARGUMENTS, 1, 1));
        returnConstantFunction.addChildToBack(new Node(Token.RETURN, 1, 1));
        returnConstantFunction.addChildToBack(new Node(Token.NUMBER_LITERAL, 1, 1, 10));
        root.addChildToBack(returnConstantFunction);

        FunctionRewriter.Reducer reducer = new FunctionRewriter.ReturnConstantReducer();
        Node helperCode = functionRewriter.parseHelperCode(reducer);

        NodeTraversal traversal = new NodeTraversal(functionRewriter.compiler);
        traversal.traverse(root, new FunctionRewriter.ReductionGatherer(
                List.of(reducer), null));

        List<FunctionRewriter.Reduction> reductions = ((FunctionRewriter.ReductionGatherer) traversal.getUserData())
                .getReductions();

        for (Reduction reduction : reductions) {
            reduction.apply();
        }

        Node newRoot = functionRewriter.compiler.getNodeForCodeInsertion(null);
        newRoot.addChildrenToFront(helperCode);

        Node result = functionRewriter.compiler.getRootNode();
        assertNotNull(helperCode);
        assertNull(returnConstantFunction);
    }

    @Test
    public void testGetterReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        Node root = new Node(Token.BLOCK, 1, 1);
        Node getterFunction = new Node(Token.FUNCTION, 1, 1);
        getterFunction.putBooleanProp(Node.IS_EXPRESSION, true);
        getterFunction.addChildToBack(new Node(Token.ARGUMENTS, 1, 1));
        getterFunction.addChildToBack(new Node(Token.RETURN, 1, 1));
        getterFunction.addChildToBack(new Node(Token.FUNCTION, 1, 1));
        getterFunction.getLastChild().addChildToBack(new Node(Token.RETURN, 1, 1));
        getterFunction.getLastChild().addChildToBack(new Node(Token.BLOCK, 1, 1));
        getterFunction.addChildToBack(new Node(Token.IDENTIFIER, 1, 1, "this"));
        getterFunction.addChildToBack(new Node(Token.IDENTIFIER, 1, 1, "get"));
        getterFunction.addChildToBack(new Node(Token.IDENTIFIER, 1, 1, "getB"));
        getterFunction.addChildToBack(new Node(Token.IDENTIFIER, 1, 1, "this"));
        getterFunction.addChildToBack(new Node(Token.IDENTIFIER, 1, 1, "b"));
        root.addChildToBack(getterFunction);

        FunctionRewriter.Reducer reducer = new FunctionRewriter.GetterReducer();
        Node helperCode = functionRewriter.parseHelperCode(reducer);

        NodeTraversal traversal = new NodeTraversal(functionRewriter.compiler);
        traversal.traverse(root, new FunctionRewriter.ReductionGatherer(
                List.of(reducer), null));

        List<FunctionRewriter.Reduction> reductions = ((FunctionRewriter.ReductionGatherer) traversal.getUserData())
                .getReductions();

        for (Reduction reduction : reductions) {
            reduction.apply();
        }

        Node newRoot = functionRewriter.compiler.getNodeForCodeInsertion(null);
        newRoot.addChildrenToFront(helperCode);

        Node result = functionRewriter.compiler.getRootNode();
        assertNotNull(helperCode);
        assertNull(getterFunction);
    }
}