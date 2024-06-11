import org.junit.Assert;
import org.junit.Test;

public class MethodCompilerPassTest {

    @Test
    public void testGetActingCallback() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {

            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return null;
            }

            @Override
            NodeTraversal.Callback getActingCallback() {
                return null;
            }
        };

        Assert.assertNull(methodCompilerPass.getActingCallback());
    }

    @Test
    public void testGetSignatureStore() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {

            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return null;
            }

            @Override
            NodeTraversal.Callback getActingCallback() {
                return null;
            }
        };

        Assert.assertNull(methodCompilerPass.getSignatureStore());
    }

    @Test
    public void testProcess() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {

            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return null;
            }

            @Override
            NodeTraversal.Callback getActingCallback() {
                return null;
            }
        };

        Node externs = null;
        Node root = null;

        methodCompilerPass.process(externs, root);

        Assert.assertEquals(0, methodCompilerPass.externMethods.size());
        Assert.assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
        Assert.assertEquals(0, methodCompilerPass.nonMethodProperties.size());
        Assert.assertEquals(0, methodCompilerPass.methodDefinitions.size());
    }
}