package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.parser.util.Position;
import com.google.javascript.rhino.Token;

import org.apache.commons.cli.FunctionRewriter;
import org.apache.commons.cli.EmptyFunctionReducer;
import org.apache.commons.cli.IdentityReducer;
import org.apache.commons.cli.ReturnConstantReducer;
import org.apache.commons.cli.GetterReducer;
import org.apache.commons.cli.SetterReducer;

public class FunctionRewriterTest {

    @Test
    public void testEmptyFunctionReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);

        String sourceCode = "function() {}";

        Node rootNode = functionRewriter.compiler.parse(sourceCode);

        FunctionRewriter.EmptyFunctionReducer emptyFunctionReducer = new FunctionRewriter.EmptyFunctionReducer();

        FunctionRewriter.Reduction reduction = new FunctionRewriter.Reduction(
                rootNode,
                rootNode,
                emptyFunctionReducer.buildCallNode(
                        FunctionRewriter.EmptyFunctionReducer.FACTORY_METHOD_NAME,
                        null,
                        Position.UNKNOWN,
                        Position.UNKNOWN));

        functionRewriter.process(null, rootNode);

        Node expectedResult = emptyFunctionReducer.buildCallNode(
                FunctionRewriter.EmptyFunctionReducer.FACTORY_METHOD_NAME,
                null,
                Position.UNKNOWN,
                Position.UNKNOWN);

        assertEquals(expectedResult.toString(), rootNode.toString());
    }

    @Test
    public void testIdentityReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);

        String sourceCode = "function(x) {return x}";

        Node rootNode = functionRewriter.compiler.parse(sourceCode);

        FunctionRewriter.IdentityReducer identityReducer = new FunctionRewriter.IdentityReducer();

        FunctionRewriter.Reduction reduction = new FunctionRewriter.Reduction(
                rootNode,
                rootNode,
                identityReducer.buildCallNode(
                        FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME,
                        rootNode.getFirstChild(),
                        Position.UNKNOWN,
                        Position.UNKNOWN));

        functionRewriter.process(null, rootNode);

        Node expectedResult = identityReducer.buildCallNode(
                FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME,
                rootNode.getFirstChild(),
                Position.UNKNOWN,
                Position.UNKNOWN);

        assertEquals(expectedResult.toString(), rootNode.toString());
    }

    @Test
    public void testReturnConstantReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);

        String sourceCode = "function() {return 10}";

        Node rootNode = functionRewriter.compiler.parse(sourceCode);

        FunctionRewriter.ReturnConstantReducer returnConstantReducer = new FunctionRewriter.ReturnConstantReducer();

        FunctionRewriter.Reduction reduction = new FunctionRewriter.Reduction(
                rootNode,
                rootNode,
                returnConstantReducer.buildCallNode(
                        FunctionRewriter.ReturnConstantReducer.FACTORY_METHOD_NAME,
                        rootNode.getLastChild(),
                        Position.UNKNOWN,
                        Position.UNKNOWN));

        functionRewriter.process(null, rootNode);

        Node expectedResult = returnConstantReducer.buildCallNode(
                FunctionRewriter.ReturnConstantReducer.FACTORY_METHOD_NAME,
                rootNode.getLastChild(),
                Position.UNKNOWN,
                Position.UNKNOWN);

        assertEquals(expectedResult.toString(), rootNode.toString());
    }

    @Test
    public void testGetterReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);

        String sourceCode = "function() {return this.a}";

        Node rootNode = functionRewriter.compiler.parse(sourceCode);

        FunctionRewriter.GetterReducer getterReducer = new FunctionRewriter.GetterReducer();

        FunctionRewriter.Reduction reduction = new FunctionRewriter.Reduction(
                rootNode,
                rootNode,
                getterReducer.buildCallNode(
                        FunctionRewriter.GetterReducer.FACTORY_METHOD_NAME,
                        new Node(Token.NAME, "a", Position.UNKNOWN, Position.UNKNOWN),
                        Position.UNKNOWN,
                        Position.UNKNOWN));

        functionRewriter.process(null, rootNode);

        Node expectedResult = getterReducer.buildCallNode(
                FunctionRewriter.GetterReducer.FACTORY_METHOD_NAME,
                new Node(Token.NAME, "a", Position.UNKNOWN, Position.UNKNOWN),
                Position.UNKNOWN,
                Position.UNKNOWN);

        assertEquals(expectedResult.toString(), rootNode.toString());
    }

    @Test
    public void testSetterReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);

        String sourceCode = "function(x) {this.a = x}";

        Node rootNode = functionRewriter.compiler.parse(sourceCode);

        FunctionRewriter.SetterReducer setterReducer = new FunctionRewriter.SetterReducer();

        FunctionRewriter.Reduction reduction = new FunctionRewriter.Reduction(
                rootNode,
                rootNode,
                setterReducer.buildCallNode(
                        FunctionRewriter.SetterReducer.FACTORY_METHOD_NAME,
                        new Node(Token.NAME, "a", Position.UNKNOWN, Position.UNKNOWN),
                        rootNode.getLastChild(),
                        Position.UNKNOWN,
                        Position.UNKNOWN));

        functionRewriter.process(null, rootNode);

        Node expectedResult = setterReducer.buildCallNode(
                FunctionRewriter.SetterReducer.FACTORY_METHOD_NAME,
                new Node(Token.NAME, "a", Position.UNKNOWN, Position.UNKNOWN),
                rootNode.getLastChild(),
                Position.UNKNOWN,
                Position.UNKNOWN);

        assertEquals(expectedResult.toString(), rootNode.toString());
    }

    @Test
    public void testFunctionWithMultipleStatements() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);

        String sourceCode = "function() {x = 1; y = 2}";

        Node rootNode = functionRewriter.compiler.parse(sourceCode);

        FunctionRewriter.ReturnConstantReducer returnConstantReducer = new FunctionRewriter.ReturnConstantReducer();

        FunctionRewriter.Reduction reduction = null;

        NodeTraversal nodeTraversal = functionRewriter.compiler.getTraverser();

        nodeTraversal.traverse(new FunctionRewriter.ReductionGatherer(
                ImmutableList.of(returnConstantReducer), null), rootNode);

        reduction = nodeTraversal.getLastReduction();

        functionRewriter.process(null, rootNode);

        assertNull(reduction);
    }
}