package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Reducer;
import com.google.javascript.jscomp.FunctionRewriter;

public class FunctionRewriterTest {

    @Test
    public void testEmptyFunctionReducer() {
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node emptyFunction = Node.newFunction(
                "prototype",
                ImmutableList.of(),
                Token.BLOCK,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                null);
        root.addChildToBack(emptyFunction);

        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        functionRewriter.process(null, root);

        Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.EmptyFunctionReducer.class);
        assertNotNull(helperCode);
        assertEquals(2, helperCode.getNumChildren());

        Node helperCall = helperCode.getLastChild();
        assertEquals(Token.CALL, helperCall.getType());
        assertEquals("JSCompiler_emptyFn", helperCall.getString());
        assertEquals(0, helperCall.getNumChildren());

        Node emptyFunctionReduced = root.getLastChild();
        assertEquals(Token.CALL, emptyFunctionReduced.getType());
        assertEquals("JSCompiler_emptyFn", emptyFunctionReduced.getString());
        assertNull(emptyFunctionReduced.getFirstChild());
    }

    @Test
    public void testIdentityReducer() {
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node identityFunction = Node.newFunction(
                "prototype",
                ImmutableList.of("value"),
                Token.BLOCK,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                null);
        identityFunction.getLastChild().setFirstChild(
                Node.newName("value", 0, 0));
        root.addChildToBack(identityFunction);

        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        functionRewriter.process(null, root);

        Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.IdentityReducer.class);
        assertNotNull(helperCode);
        assertEquals(2, helperCode.getNumChildren());

        Node helperCall = helperCode.getLastChild();
        assertEquals(Token.CALL, helperCall.getType());
        assertEquals("JSCompiler_identityFn", helperCall.getString());
        assertNotNull(helperCall.getFirstChild());
        assertEquals("value", helperCall.getFirstChild().getString());

        Node identityFunctionReduced = root.getLastChild();
        assertEquals(Token.CALL, identityFunctionReduced.getType());
        assertEquals("JSCompiler_identityFn", identityFunctionReduced.getString());
        assertEquals("value", identityFunctionReduced.getFirstChild().getString());
    }

    @Test
    public void testReturnConstantReducer() {
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node returnConstantFunction = Node.newFunction(
                "prototype",
                ImmutableList.of(),
                Token.BLOCK,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                Node.newNumber(10, 0, 0));
        root.addChildToBack(returnConstantFunction);

        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        functionRewriter.process(null, root);

        Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.ReturnConstantReducer.class);
        assertNotNull(helperCode);
        assertEquals(2, helperCode.getNumChildren());

        Node helperCall = helperCode.getLastChild();
        assertEquals(Token.CALL, helperCall.getType());
        assertEquals("JSCompiler_returnArg", helperCall.getString());
        assertNotNull(helperCall.getFirstChild());
        assertEquals(10, ((Node) helperCall.getFirstChild()).getIntValue());

        Node returnConstantFunctionReduced = root.getLastChild();
        assertEquals(Token.CALL, returnConstantFunctionReduced.getType());
        assertEquals("JSCompiler_returnArg", returnConstantFunctionReduced.getString());
        assertNull(returnConstantFunctionReduced.getFirstChild());
    }

    @Test
    public void testGetterReducer() {
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node getterFunction = Node.newFunction(
                "prototype",
                ImmutableList.of("a"),
                Token.BLOCK,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                Node.newGetProp("a_", null, 0, 0));
        root.addChildToBack(getterFunction);

        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        functionRewriter.process(null, root);

        Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.GetterReducer.class);
        assertNotNull(helperCode);
        assertEquals(2, helperCode.getNumChildren());

        Node helperCall = helperCode.getLastChild();
        assertEquals(Token.CALL, helperCall.getType());
        assertEquals("JSCompiler_get", helperCall.getString());
        assertEquals("a_", helperCall.getFirstChild().getString());

        Node getterFunctionReduced = root.getLastChild();
        assertEquals(Token.CALL, getterFunctionReduced.getType());
        assertEquals("JSCompiler_get", getterFunctionReduced.getString());
        assertEquals("a_", getterFunctionReduced.getFirstChild().getString());
    }

    @Test
    public void testSetterReducer() {
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node setterFunction = Node.newFunction(
                "prototype",
                ImmutableList.of("value"),
                Token.BLOCK,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                Node.newSetProp("b_", Node.newName("value", 0, 0), null, 0, 0));
        root.addChildToBack(setterFunction);

        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        functionRewriter.process(null, root);

        Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.SetterReducer.class);
        assertNotNull(helperCode);
        assertEquals(2, helperCode.getNumChildren());

        Node helperCall = helperCode.getLastChild();
        assertEquals(Token.CALL, helperCall.getType());
        assertEquals("JSCompiler_set", helperCall.getString());
        assertEquals("b_", helperCall.getFirstChild().getString());

        Node setterFunctionReduced = root.getLastChild();
        assertEquals(Token.CALL, setterFunctionReduced.getType());
        assertEquals("JSCompiler_set", setterFunctionReduced.getString());
        assertEquals("b_", setterFunctionReduced.getFirstChild().getString());
    }

    @Test
    public void testMultipleReducers() {
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node emptyFunction = Node.newFunction(
                "prototype",
                ImmutableList.of(),
                Token.BLOCK,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                null);
        root.addChildToBack(emptyFunction);

        Node identityFunction = Node.newFunction(
                "prototype",
                ImmutableList.of("value"),
                Token.BLOCK,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                Node.newName("value", 0, 0));
        identityFunction.getLastChild().setFirstChild(
                Node.newName("value", 0, 0));
        root.addChildToBack(identityFunction);

        Node getterFunction = Node.newFunction(
                "prototype",
                ImmutableList.of(),
                Token.BLOCK,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                Node.newGetProp("a_", null, 0, 0));
        root.addChildToBack(GetterReducerTest.createGetterFunctionWithReturnValue(getNodeForName("a_"), identityFunction));
        root.addChildToBack(getNodeForName("a_"));

        Node returnConstantFunction = Node.newFunction(
                "prototype",
                ImmutableList.of(),
                Token.BLOCK,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                Node.newNumber(10, 0, 0));
        root.addChildToBack(returnConstantFunction);

        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        functionRewriter.process(null, root);

        Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.class);
        assertNotNull(helperCode);
        assertEquals(9, helperCode.getNumChildren());

        Node emptyFunctionHelper = helperCode.get(0);
        assertEquals(Token.BLOCK, emptyFunctionHelper.getType());
        assertEquals(0, emptyFunctionHelper.getNumChildren());

        Node identityFunctionHelper = helperCode.get(1);
        assertEquals(Token.BLOCK, identityFunctionHelper.getType());
        assertEquals(1, identityFunctionHelper.getNumChildren());
        Node identityFunctionHelperArg = identityFunctionHelper.getFirstChild();
        assertEquals(Token.VAR_DECL, identityFunctionHelperArg.getType());
        assertEquals("value", identityFunctionHelperArg.getString());

        Node getterFunctionHelper = helperCode.get(2);
        assertEquals(Token.BLOCK, getterFunctionHelper.getType());
        assertEquals(1, getterFunctionHelper.getNumChildren());
        Node getterFunctionHelperArg = getterFunctionHelper.getFirstChild();
        assertEquals(Token.VAR_DECL, getterFunctionHelperArg.getType());
        assertEquals("value", getterFunctionHelperArg.getString());

        Node returnConstantFunctionHelper = helperCode.get(3);
        assertEquals(Token.BLOCK, returnConstantFunctionHelper.getType());
        assertEquals(1, returnConstantFunctionHelper.getNumChildren());
        Node returnConstantFunctionHelperArg = returnConstantFunctionHelper.getFirstChild();
        assertEquals(Token.VAR_DECL, returnConstantFunctionHelperArg.getType());
        assertEquals("value", returnConstantFunctionHelperArg.getString());

        Node getterFunctionCall = helperCode.get(4);
        assertEquals(Token.CALL, getterFunctionCall.getType());
        assertEquals("JSCompiler_get", getterFunctionCall.getString());
        assertEquals("a_", getterFunctionCall.getFirstChild().getString());

        Node identityFunctionCall = helperCode.get(5);
        assertEquals(Token.CALL, identityFunctionCall.getType());
        assertEquals("JSCompiler_identityFn", identityFunctionCall.getString());
        assertNotNull(identityFunctionCall.getFirstChild());
        assertEquals("value", identityFunctionCall.getFirstChild().getString());

        Node returnConstantFunctionCall = helperCode.get(6);
        assertEquals(Token.CALL, returnConstantFunctionCall.getType());
        assertEquals("JSCompiler_returnArg", returnConstantFunctionCall.getString());
        assertNotNull(returnConstantFunctionCall.getFirstChild());
        assertEquals(10, ((Node) returnConstantFunctionCall.getFirstChild()).getIntValue());

        Node emptyFunctionCall = helperCode.get(7);
        assertEquals(Token.CALL, emptyFunctionCall.getType());
        assertEquals("JSCompiler_emptyFn", emptyFunctionCall.getString());
        assertNull(emptyFunctionCall.getFirstChild());

        Node setterFunction = root.getLastChild();
        assertEquals(Token.CALL, setterFunction.getType());
        assertEquals("JSCompiler_set", setterFunction.getString());
        assertEquals("a_", setterFunction.getFirstChild().getString());
        assertNull(setterFunction.getLastChild());

        Node getterFunctionReduced = root.get(3);
        assertEquals(Token.CALL, getterFunctionReduced.getType());
        assertEquals("JSCompiler_get", getterFunctionReduced.getString());
        assertEquals("a_", getterFunctionReduced.getFirstChild().getString());

        Node identityFunctionReduced = root.get(4);
        assertEquals(Token.CALL, identityFunctionReduced.getType());
        assertEquals("JSCompiler_identityFn", identityFunctionReduced.getString());
        assertEquals("value", identityFunctionReduced.getFirstChild().getString());

        Node returnConstantFunctionReduced = root.get(5);
        assertEquals(Token.CALL, returnConstantFunctionReduced.getType());
        assertEquals("JSCompiler_returnArg", returnConstantFunctionReduced.getString());
        assertNull(returnConstantFunctionReduced.getFirstChild());

        Node getterFunctionReturnValue = root.get(2);
        assertEquals(Token.GETPROP, getterFunctionReturnValue.getType());
        assertEquals("value", getterFunctionReturnValue.getString());
        assertEquals("a_", getterFunctionReturnValue.getParent().getString());
    }

    private Node getNodeForName(String name) {
        return NodeUtil.getFirstChildByProperty(NodeUtil.getFirstChildByProperty(root, "prototype"), "name", name);
    }

    private Node createGetterFunctionWithReturnValue(Node property, Node identityFunction) {
        Node function = Node.newFunction(
                "prototype",
                ImmutableList.of(),
                Token.BLOCK,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                Node.newGetProp(property.getString(), null, 0, 0));
        function.getLastChild().setFirstChild(identityFunction);
        return function;
    }

    private Node root;
}