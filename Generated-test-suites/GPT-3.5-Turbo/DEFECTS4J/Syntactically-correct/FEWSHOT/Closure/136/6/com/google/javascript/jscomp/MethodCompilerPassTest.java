package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MethodCompilerPassTest {
    @Test
    public void testProcess() {
        // Test case 1
        MethodCompilerPass classUnderTest1 = new MethodCompilerPassImpl();
        classUnderTest1.process(null, null);

        // Test case 2
        MethodCompilerPass classUnderTest2 = new MethodCompilerPassImpl();
        Node externs2 = new Node(Token.EXTERN_RESULT);
        Node root2 = new Node(Token.ROOT);
        classUnderTest2.process(externs2, root2);
    }

    @Test
    public void testGetActingCallback() {
        MethodCompilerPass classUnderTest = new MethodCompilerPassImpl();
        MethodCompilerPass.Callback callback = classUnderTest.getActingCallback();
        assertNotNull(callback);
    }

    @Test
    public void testGetSignatureStore() {
        MethodCompilerPass classUnderTest = new MethodCompilerPassImpl();
        MethodCompilerPass.SignatureStore store = classUnderTest.getSignatureStore();
        assertNotNull(store);
    }
    
    private static class MethodCompilerPassImpl extends MethodCompilerPass {
        public MethodCompilerPassImpl() {
            super(null);
        }

        @Override
        Callback getActingCallback() {
            return null;
        }

        @Override
        SignatureStore getSignatureStore() {
            return null;
        }
    }
}