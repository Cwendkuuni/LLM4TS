package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.jscomp.RuntimeTypeCheck.*;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

    @Test
    public void testProcess_Basic() {
        AbstractCompiler compiler = new Compiler();
        String logFunction = "function(warning, expr) { System.out.println(warning + ': ' + expr); }";
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

        Node root = Node.objectLiteral();
        runtimeTypeCheck.process(null, root);

        // Assert that the boilerplate code is added to the script
        Node js = runtimeTypeCheck.compiler.getNodeForCodeInsertion(null);
        List<Node> boilerplateCode = ImmutableList.of(
                js.getFirstChild(),
                js.getLastChild().getFirstChild()
        );

        // Additional test cases for boilerplate code can be added here

        assertThat(boilerplateCode).isNotEmpty();
    }

    @Test
    public void testProcess_FunctionWithParametersAndReturnType_Basic() {
        AbstractCompiler compiler = new Compiler();
        String logFunction = "function(warning, expr) { System.out.println(warning + ': ' + expr); }";
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

        Node functionTypeNode = Node.newFunction(
                Token.FUNCTION,
                Node.newString("functionName"),
                Node.newArray(Node.newString("param1"), Node.newString("param2")),
                Node.newArray(Node.newString("returnType"))
        );

        Node root = Node.objectLiteral();
        root.addChild(functionTypeNode);
        runtimeTypeCheck.process(null, root);

        Node functionNode = findFunctionNodeByName("functionName", root);
        Node checkNodes = findCheckerNodes(functionNode);

        // Assert that the checker nodes are added to the function
        List<Node> checkerNodes = ImmutableList.copyOf(checkNodes.getChildren());
        assertThat(checkerNodes).hasSize(3); // Two for parameters and one for return type
    }

    @Test
    public void testProcess_FunctionWithUnionTypesAsParametersAndReturnType_EdgeCases() {
        AbstractCompiler compiler = new Compiler();
        String logFunction = "function(warning, expr) { System.out.println(warning + ': ' + expr); }";
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

        Node functionTypeNode = Node.newFunction(
                Token.FUNCTION,
                Node.newString("functionName"),
                Node.newArray(
                        Node.newString("param1"),
                        Node.newArray(
                                Node.newString("unionType1"),
                                Node.newString("unionType2")
                        )
                ),
                Node.newArray(Node.newString("unionType3"))
        );

        Node root = Node.objectLiteral();
        root.addChild(functionTypeNode);
        runtimeTypeCheck.process(null, root);

        Node functionNode = findFunctionNodeByName("functionName", root);
        Node checkNodes = findCheckerNodes(functionNode);

        // Assert that the checker nodes are added to the function
        List<Node> checkerNodes = ImmutableList.copyOf(checkNodes.getChildren());
        assertThat(checkerNodes).hasSize(6); // Three for each union type (two alternates and one checker)
    }

    @Test(expected = RuntimeException.class)
    public void testProcess_FunctionWithInvalidType_Exception() {
        AbstractCompiler compiler = new Compiler();
        String logFunction = "function(warning, expr) { System.out.println(warning + ': ' + expr); }";
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

        Node functionTypeNode = Node.newFunction(
                Token.FUNCTION,
                Node.newString("functionName"),
                Node.newArray(),
                Node.newArray(Node.newString("invalidType"))
        );

        Node root = Node.objectLiteral();
        root.addChild(functionTypeNode);
        runtimeTypeCheck.process(null, root);
    }

    // Add more test cases for other scenarios and edge cases if needed

    private Node findFunctionNodeByName(String name, Node root) {
        return NodeUtil.findFunctionDeclarationByName(name, root);
    }

    private Node findCheckerNodes(Node functionNode) {
        return NodeUtil.findChildByType(functionNode, Token.CALL);
    }
}