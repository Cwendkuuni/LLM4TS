import org.junit.Test;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {
    @Test
    public void testProcess() {
        MethodCompilerPass pass = new MethodCompilerPass(new AbstractCompiler());
        pass.process(null, null);
    }

    @Test
    public void testGetActingCallback() {
        MethodCompilerPass pass = new MethodCompilerPass(new AbstractCompiler());
        MethodCompilerPass.Callback callback = pass.getActingCallback();
        assertNotNull(callback);
    }

    @Test
    public void testGetSignatureStore() {
        MethodCompilerPass pass = new MethodCompilerPass(new AbstractCompiler());
        MethodCompilerPass.SignatureStore store = pass.getSignatureStore();
        assertNotNull(store);
    }
}