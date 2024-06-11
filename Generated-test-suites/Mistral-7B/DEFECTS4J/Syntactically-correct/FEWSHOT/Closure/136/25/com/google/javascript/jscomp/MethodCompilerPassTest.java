package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {
    @Test
    public void testMethodCompilerPass() {
        AbstractCompiler compiler = new Compiler();
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

        // Test empty code
        Node root = Node.NULL;
        Node externs = Node.NULL;
        methodCompilerPass.process(externs, root);
        assertEmpty(methodCompilerPass.externMethods);
        assertEmpty(methodCompilerPass.methodDefinitions);

        // Test extern methods without signatures
        externs = Node.objectLiteral(
            Node.PROPERTIES,
            Node.PROPERTY(Node.STRING("foo"), Node.STRING(""))
        );
        root = Node.script(Node.EMPTY, Node.EMPTY);
        methodCompilerPass.process(externs, root);
        assertThat(methodCompilerPass.externMethodsWithoutSignatures).containsExactly("foo");
        assertThat(methodCompilerPass.externMethods).isEmpty();
        assertThat(methodCompilerPass.methodDefinitions).isEmpty();

        // Test extern methods with signatures
        externs = Node.objectLiteral(
            Node.PROPERTIES,
            Node.PROPERTY(Node.STRING("foo"), Node.FUNCTION(
                Node.VOID,
                Node.LIST(Node.NAME("arg1"), Node.NAME("arg2")),
                Node.BLOCK(
                    Node.RETURN(Node.NAME("arg1")),
                    Node.ASSIGN(Node.NAME("arg1"), Node.NAME("arg2"))
                )
            ))
        );
        root = Node.script(Node.EMPTY, Node.EMPTY);
        methodCompilerPass.process(externs, root);
        Set<String> expectedExternMethods = Sets.newHashSet("foo");
        Set<String> expectedExternMethodsWithoutSignatures = Sets.newHashSet();
        assertThat(methodCompilerPass.externMethods).containsExactlyElementsIn(expectedExternMethods);
        assertThat(methodCompilerPass.externMethodsWithoutSignatures).isEmpty();
        Map<String, Node> expectedMethodDefinitions = HashMultimap.create().putAll(methodCompilerPass.methodDefinitions);
        Node expectedMethodDefinition = expectedMethodDefinitions.get("foo").getFirst();
        assertThat(expectedMethodDefinitions.size()).isEqualTo(1);
        assertThat(expectedMethodDefinition.getFirstChild().getType()).isEqualTo(Token.FUNCTION);
        List<Node> expectedFunctionParameters = Lists.newArrayList(
            Node.VAR(Node.VAR_DECLARE_FUNCTION, Node.NAME("arg1"), Node.TYPE_NULL, Node.EMPTY, Node.EMPTY),
            Node.VAR(Node.VAR_DECLARE_FUNCTION, Node.NAME("arg2"), Node.TYPE_NULL, Node.EMPTY, Node.EMPTY)
        );
        Node expectedFunctionBody = Node.BLOCK(
            Node.ASSIGN(Node.NAME("arg1"), Node.NAME("arg2")),
            Node.RETURN(Node.NAME("arg1"))
        );
        assertThat(expectedMethodDefinition.getLastChild().getFirstChild().getType()).isEqualTo(Token.FUNCTION);
        assertThat(expectedMethodDefinition.getLastChild().getFirstChild().getFirstChild().getType()).isEqualTo(Token.VARDECLIST);
        assertThat(expectedMethodDefinition.getLastChild().getFirstChild().getLastChild().getType()).isEqualTo(Token.BLOCK);
        assertThat(expectedMethodDefinition.getLastChild().getLastChild().getFirstChild().getType()).isEqualTo(Token.ASSIGN);
        assertThat(expectedMethodDefinition.getLastChild().getLastChild().getLastChild().getType()).isEqualTo(Token.RETURN);
        Node actualMethodDefinition = methodCompilerPass.methodDefinitions.get("foo").get(0);
        assertThat(actualMethodDefinition).isEqualTo(expectedMethodDefinition);

        // Test non-method properties
        externs = Node.objectLiteral(
            Node.PROPERTIES,
            Node.PROPERTY(Node.STRING("foo"), Node.STRING("bar"))
        );
        root = Node.script(Node.EMPTY, Node.EMPTY);
        methodCompilerPass.process(externs, root);
        Set<String> expectedNonMethodProperties = Sets.newHashSet("foo");
        assertThat(methodCompilerPass.externMethods).isEmpty();
        assertThat(methodCompilerPass.methodDefinitions).isEmpty();
        assertThat(methodCompilerPass.nonMethodProperties).containsExactly("foo");
    }

    private void assertEmpty(Set<String> set) {
        assertThat(set).isEmpty();
    }
}

class Compiler extends AbstractCompiler {
    @Override
    public void reportMessage(Node node, String message, int line, int lineOffset, int endLine, int endLineOffset) {
        // Do nothing
    }
}