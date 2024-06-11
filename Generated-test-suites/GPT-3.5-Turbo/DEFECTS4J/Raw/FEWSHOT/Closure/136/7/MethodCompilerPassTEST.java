import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MethodCompilerPassTest {
    @Test
    public void testGetActingCallback() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return null;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
        assertNull(methodCompilerPass.getActingCallback());
    }

    @Test
    public void testGetSignatureStore() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return null;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
        assertNull(methodCompilerPass.getSignatureStore());
    }

    @Test
    public void testProcess() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return null;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
        methodCompilerPass.process(null, null);
    }
}