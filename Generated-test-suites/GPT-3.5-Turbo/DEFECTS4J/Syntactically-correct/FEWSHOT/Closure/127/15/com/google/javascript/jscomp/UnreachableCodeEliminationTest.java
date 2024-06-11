package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
    @Test
    public void testProcess() {
        // Test cases for removing dead code after return statements
        UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination();
        Node externs = new Node(Token.ROOT);
        Node toplevel = new Node(Token.ROOT);
        toplevel.addChildToFront(parse("if (x) { return; alert('unreachable'); }"));
        unreachableCodeElimination.process(externs, toplevel);
        assertNull(toplevel.getFirstChild());

        // Test cases for removing statements with no side effects
        toplevel.addChildToFront(parse("a.b.MyClass.prototype.propertyName;"));
        toplevel.addChildToFront(parse("true;"));
        unreachableCodeElimination.process(externs, toplevel);
        assertNull(toplevel.getFirstChild());
    }

    private Node parse(String sourceCode) {
        Node ast = null;
        try {
            ast = new JsAst(SourceFile.fromCode("test", sourceCode), false, true,
                    LineNumberCheckMode.OFF).getAstRoot(compiler);
        } catch (Exception e) {
            fail("Failed to parse source code: " + sourceCode);
        }
        return ast;
    }
}