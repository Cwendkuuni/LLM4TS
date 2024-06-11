
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.MethodCompilerPass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MethodCompilerPassTest {

    private AbstractCompiler compiler;
    private MethodCompilerPass.SignatureStore signatureStore;
    private MethodCompilerPass methodCompilerPass;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        signatureStore = new MethodCompilerPass.SignatureStore() {
            @Override
            public void reset() {}

            @Override
            public void addSignature(String functionName, Node functionNode, String sourceFile) {}

            @Override
            public void removeSignature(String functionName) {}
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
        Node externs = new Node();
        Node root = new Node();
        methodCompilerPass.process(externs, root);
        // assert something
    }

}
