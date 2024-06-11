package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Supplier;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class FunctionToBlockMutatorTest {
    @Test
    public void testMutate() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new Supplier<String>() {
            public String get() {
                return "test";
            }
        };

        FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, supplier);

        String fnName = "foo";
        Node fnNode = new Node(Token.FUNCTION);
        Node callNode = new Node(Token.CALL);
        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = true;

        Node result = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        assertNotNull(result);
        assertEquals(Token.BLOCK, result.getType());
        assertEquals(0, result.getChildCount());
    }
}