import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.MethodCompilerPass.SignatureStore;
import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {
    private MethodCompilerPass compilerPass;

    @Before
    public void setUp() {
        compilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return null;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
    }

    @Test
    public void testProcess() {
        Node externs = null;
        Node root = null;
        compilerPass.process(externs, root);
    }

    @Test
    public void testVisit_GetProp() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        compilerPass.new GatherSignatures().visit(t, n, parent);
    }

    @Test
    public void testVisit_ObjectLit() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        compilerPass.new GatherSignatures().visit(t, n, parent);
    }

    @Test
    public void testGetFooterMessage() {
        String expected = "";
        String actual = compilerPass.getFooterMessage();
        assertEquals(expected, actual);
    }
}