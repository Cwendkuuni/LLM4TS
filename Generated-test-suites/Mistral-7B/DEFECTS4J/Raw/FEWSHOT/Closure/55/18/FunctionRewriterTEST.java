package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import static com.google.javascript.jscomp.NodeUtil.*;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Reducer;
import com.google.javascript.jscomp.FunctionRewriter;

public class FunctionRewriterTest {
    @Test
    public void testEmptyFunctionReducer() {
        FunctionRewriter rewriter = new FunctionRewriter(null);
        Node root = rewriter.compiler.parseTestCode("emptyFunctionTest",
                "function() {}");

        NodeTraversal traversal = new NodeTraversal(rewriter.compiler);
        traversal.traverse(root);

        Node emptyFunction = root.getFirstChild().getLastChild();
        assertEquals("emptyFn()", emptyFunction.toString());
    }

    @Test
    public void testIdentityReducer() {
        FunctionRewriter rewriter = new FunctionRewriter(null);
        Node root = rewriter.compiler.parseTestCode("identityTest",
                "function(x) {return x}");

        NodeTraversal traversal = new NodeTraversal(rewriter.compiler);
        traversal.traverse(root);

        Node identityFunction = root.getFirstChild().getLastChild();
        assertEquals("identityFn(identityFn_value)", identityFunction.toString());

        Node identityFunctionCall = root.getFirstChild().getPrevious();
        assertEquals("identity(identity_value)", identityFunctionCall.toString());
    }

    @Test
    public void testReturnConstantReducer() {
        FunctionRewriter rewriter = new FunctionRewriter(null);
        Node root = rewriter.compiler.parseTestCode("returnConstantTest",
                "function() {return 10}");

        NodeTraversal traversal = new NodeTraversal(rewriter.compiler);
        traversal.traverse(root);

        Node constantFunction = root.getFirstChild().getLastChild();
        assertEquals("returnArg(10)", constantFunction.toString());

        Node constantFunctionCall = root.getFirstChild().getPrevious();
        assertEquals("returnconst(10)", constantFunctionCall.toString());
    }

    @Test
    public void testGetterReducer() {
        FunctionRewriter rewriter = new FunctionRewriter(null);
        Node root = rewriter.compiler.parseTestCode("getterTest",
                "function() {return this.a}");

        NodeTraversal traversal = new NodeTraversal(rewriter.compiler);
        traversal.traverse(root);

        Node getterFunction = root.getFirstChild().getLastChild();
        assertEquals("getter(\"a\")", getterFunction.toString());

        Node getterFunctionCall = root.getFirstChild().getPrevious();
        assertEquals("getter(\"a_\")", getterFunctionCall.toString());
    }

    @Test
    public void testSetterReducer() {
        FunctionRewriter rewriter = new FunctionRewriter(null);
        Node root = rewriter.compiler.parseTestCode("setterTest",
                "function(value) {this.a = value}");

        NodeTraversal traversal = new NodeTraversal(rewriter.compiler);
        traversal.traverse(root);

        Node setterFunction = root.getFirstChild().getLastChild();
        assertEquals("setter(\"a\")", setterFunction.toString());

        Node setterFunctionCall = root.getFirstChild().getPrevious();
        assertEquals("setter(\"a_\")", setterFunctionCall.toString());
    }
}