package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.compiler.AbstractCompiler;
import com.google.javascript.jscomp.compiler.CompilerPass;
import com.google.javascript.jscomp.functionrewriter.FunctionRewriter;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class FunctionRewriterTest {

    private AbstractCompiler compiler;
    private Node root;

    public FunctionRewriterTest(AbstractCompiler compiler, Node root) {
        this.compiler = compiler;
        this.root = root;
    }

    @Parameterized.Parameters(name = "{index}: testFunctionRewriter with root={0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {new MyCompiler(), new Node(Token.PROGRAM, 0, 0)}
        });
    }

    @Test
    public void testFunctionRewriter() {
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        functionRewriter.process(null, root);

        Node gettingA = findNode("C.prototype.getA", root);
        assertNull(gettingA);

        Node settingA = findNode("C.prototype.setA", root);
        assertNull(settingA);

        Node gettingA_helper = findNode("C.prototype.getA=JSCompiler_get", root);
        assertNotNull(gettingA_helper);

        Node settingA_helper = findNode("C.prototype.setA=JSCompiler_set", root);
        assertNotNull(settingA_helper);
    }

    private Node findNode(String name, Node node) {
        NodeTraversal traversal = new NodeTraversal(node, null);
        for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
            if (NodeUtil.isName(child) && child.getString().equals(name)) {
                return child;
            }
            Node found = findNode(name, child);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static boolean isName(Node node) {
        return node.getType() == Token.NAME;
    }

    private static Node findNode(String name, Node node) {
        if (NodeUtil.isName(node) && node.getString().equals(name)) {
            return node;
        }
        for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
            Node found = findNode(name, child);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static class MyCompiler extends AbstractCompiler {
    }
}