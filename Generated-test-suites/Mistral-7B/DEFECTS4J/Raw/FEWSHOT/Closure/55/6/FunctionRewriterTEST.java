package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;

public class FunctionRewriterTest {

    @Test
    public void testEmptyFunctionReducer() {
        String source = "function Test() {}";
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node externs = new Node(Token.PROGRAM, 0, 0);

        compilerSetup(source);
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        functionRewriter.process(externs, root);
        Node emptyFunction = getFunctionByName(root, "Test");
        Node expected = builderHelperCode("JSCompiler_emptyFn");

        assertEquals(expected, emptyFunction.getFirstChild());
    }

    @Test
    public void testIdentityReducer() {
        String source = "function Test(value) { return value }";
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node externs = new Node(Token.PROGRAM, 0, 0);

        compilerSetup(source);
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        functionRewriter.process(externs, root);
        Node identityFunction = getFunctionByName(root, "Test");
        String helperCode = "function Test(value) { return " +
                "JSCompiler_identityFn_value; }" +
                "function JSCompiler_identityFn() {" +
                "  return function(JSCompiler_identityFn_value) {" +
                "    return JSCompiler_identityFn_value;" +
                "  }" +
                "}";
        Node expected = parseNode(helperCode);

        Node helperCodeNode = identityFunction.getChildren().get(0);
        assertEquals(expected, helperCodeNode);
    }

    @Test
    public void testReturnConstantReducer() {
        String source = "function Test() { return 10 }";
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node externs = new Node(Token.PROGRAM, 0, 0);

        compilerSetup(source);
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        functionRewriter.process(externs, root);
        Node returnConstantFunction = getFunctionByName(root, "Test");
        String helperCode = "function Test() { return " +
                "JSCompiler_returnArg(10); }" +
                "function JSCompiler_returnArg(value) {" +
                "  return function() { return " +
                "JSCompiler_returnArg_value; }" +
                "}";
        Node expected = parseNode(helperCode);

        Node helperCodeNode = returnConstantFunction.getChildren().get(0);
        assertEquals(expected, helperCodeNode);
    }

    @Test
    public void testGetterReducer() {
        String source = "function Test() { return this.a }";
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node externs = new Node(Token.PROGRAM, 0, 0);

        compilerSetup(source);
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        functionRewriter.process(externs, root);
        Node getterFunction = getFunctionByName(root, "Test");
        String helperCode = "function Test() { return " +
                "JSCompiler_get(\"a\"); }" +
                "function JSCompiler_get(name) {" +
                "  return function() { return this[" + name + "]; }" +
                "}";
        Node expected = parseNode(helperCode);

        Node helperCodeNode = getterFunction.getChildren().get(0);
        assertEquals(expected, helperCodeNode);
    }

    @Test
    public void testSetterReducer() {
        String source = "function Test(value) { this.a = value }";
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node externs = new Node(Token.PROGRAM, 0, 0);

        compilerSetup(source);
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        functionRewriter.process(externs, root);
        Node setterFunction = getFunctionByName(root, "Test");
        String helperCode = "function Test(value) {" +
                "  JSCompiler_set(\"a\", value); }" +
                "function JSCompiler_set(name, value) {" +
                "  this[" + name + "] = value;" +
                "}";
        Node expected = parseNode(helperCode);

        Node helperCodeNode = setterFunction.getChildren().get(0);
        assertEquals(expected, helperCodeNode);
    }

    @Test
    public void testFunctionWithMultipleStatements() {
        String source = "function Test() {" +
                "  this.a = 10;" +
                "  this.b = 20; }";
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node externs = new Node(Token.PROGRAM, 0, 0);

        compilerSetup(source);
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        functionRewriter.process(externs, root);
        Node functionNode = getFunctionByName(root, "Test");
        Node expected = parseNode(source);

        assertEquals(expected, functionNode);
    }

    @Test
    public void testFunctionWithConditionalStatement() {
        String source = "function Test() {" +
                "  if (true) {" +
                "    this.a = 10;" +
                "  } else {" +
                "    this.b = 20;" +
                "  }}" +
                "}";
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node externs = new Node(Token.PROGRAM, 0, 0);

        compilerSetup(source);
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        functionRewriter.process(externs, root);
        Node functionNode = getFunctionByName(root, "Test");
        Node expected = parseNode(source);

        assertEquals(expected, functionNode);
    }

    @Test
    public void testFunctionWithLoopStatement() {
        String source = "function Test() {" +
                "  for (var i = 0; i < 10; i++) {" +
                "    this.a[i] = i;" +
                "  }}" +
                "}";
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node externs = new Node(Token.PROGRAM, 0, 0);

        compilerSetup(source);
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        functionRewriter.process(externs, root);
        Node functionNode = getFunctionByName(root, "Test");
        Node expected = parseNode(source);

        assertEquals(expected, functionNode);
    }

    private void compilerSetup(String source) {
        compiler = new Compiler();
        compiler.setOptions(new CompilerOptions(true, null));
        compiler.parseAndCompile(source, null, null);
    }

    private Node parseNode(String code) {
        Node root = new Node(Token.PROGRAM, 0, 0);
        Node externs = new Node(Token.PROGRAM, 0, 0);
        compilerSetup(code);
        compiler.parseAndCompile("(function() {" + code + "})()", null, externs);
        return root;
    }

    private Node getFunctionByName(Node root, String name) {
        Node function = null;
        for (Node child : root.getChildren()) {
            if (NodeUtil.isFunction(child) &&
                    NodeUtil.getName(child).equals(name)) {
                function = child;
                break;
            }
        }
        return function;
    }

    private Node builderHelperCode(String helperName) {
        String code = "function " + helperName + "() { }";
        return parseNode(code).getFirstChild();
    }

    private Compiler compiler;
}