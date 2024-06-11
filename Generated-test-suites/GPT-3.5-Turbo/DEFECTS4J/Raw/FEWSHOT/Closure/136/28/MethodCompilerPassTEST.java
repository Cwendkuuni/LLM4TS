package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {

    private MethodCompilerPass.SignatureStore signatureStore;
    private MethodCompilerPass methodCompilerPass;

    @Before
    public void setUp() {
        Compiler compiler = new Compiler();
        signatureStore = new MethodCompilerPass.SignatureStore() {

            @Override
            public void reset() {

            }

            @Override
            public void addSignature(String functionName, Node functionNode, String sourceFile) {

            }

            @Override
            public void removeSignature(String functionName) {

            }
        };
        methodCompilerPass = new MethodCompilerPass(compiler) {

            @Override
            MethodCompilerPass.Callback getActingCallback() {
                return null;
            }

            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return signatureStore;
            }
        };
    }

    @Test
    public void testProcess() {
        Node externs = null;
        Node root = null;
        methodCompilerPass.process(externs, root);
    }

    @Test
    public void testGetActingCallback() {
        MethodCompilerPass.Callback callback = methodCompilerPass.getActingCallback();
        assertNull(callback);
    }

    @Test
    public void testGetSignatureStore() {
        MethodCompilerPass.SignatureStore store = methodCompilerPass.getSignatureStore();
        assertEquals(signatureStore, store);
    }

}