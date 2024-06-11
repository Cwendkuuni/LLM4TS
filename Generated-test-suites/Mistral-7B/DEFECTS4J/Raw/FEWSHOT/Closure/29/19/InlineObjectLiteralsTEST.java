package org.junit.tests.inlineobjectliterals;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilationTarget;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {
    @Test
    public void testInlineObjectLiterals() {
        String sourceCode = "function foo() {" +
                "  var obj = {a: 1, b: 2};" +
                "  var c = obj.a;" +
                "  var d = obj.b;" +
                "  obj.c = 3;" +
                "  obj.d = 4;" +
                "}";

        Compiler compiler = new Compiler();
        compiler.setTarget(new CompilationTarget());
        AbstractCompiler abstractCompiler = compiler.getOptions().getPassManager().getByName("InlineObjectLiterals");
        abstractCompiler.run(null, Node.parseTree(sourceCode));
        Scope rootScope = compiler.getOptions().getOutput().getRootScope();

        Node rootNode = rootScope.getRootNode();
        Node functionNode = NodeUtil.getFunctionDeclaration(rootNode, "foo");
        List<Node> bodyStatements = functionNode.getBody().getStatements();

        // Check that assignments to variables c and d were replaced with assignments to the inlined variables.
        for (int i = 0; i < bodyStatements.size(); i++) {
            Node statement = bodyStatements.get(i);
            if (statement.isAssign()) {
                Node lhs = statement.getLhs();
                if (lhs.isName() && lhs.getString().equals("c")) {
                    assertNotNull(rootScope.getVar(VAR_PREFIX + "a"));
                    assertEquals(VAR_PREFIX + "a", lhs.getString());
                } else if (lhs.isName() && lhs.getString().equals("d")) {
                    assertNotNull(rootScope.getVar(VAR_PREFIX + "b"));
                    assertEquals(VAR_PREFIX + "b", lhs.getString());
                }
            }
        }

        // Check that the object literal has been replaced with the inlined variables.
        Node objNode = NodeUtil.getVariableDeclaration(rootNode, "obj");
        assertNull(objNode);
    }
}