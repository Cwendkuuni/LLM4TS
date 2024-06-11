import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class ScopedAliasesTest {
    private ScopedAliases scopedAliases;
    private AliasTransformationHandler aliasTransformationHandler;

    @Before
    public void setUp() {
        aliasTransformationHandler = new AliasTransformationHandler();
        scopedAliases = new ScopedAliases(null, null, aliasTransformationHandler);
    }

    @Test
    public void testProcess() {
        Node externs = null;
        Node root = new Node(Token.SCRIPT);
        scopedAliases.process(externs, root);
    }

    @Test
    public void testHotSwapScript() {
        Node root = new Node(Token.SCRIPT);
        Node originalRoot = null;
        scopedAliases.hotSwapScript(root, originalRoot);
    }

    @Test
    public void testEnterScope() {
        NodeTraversal t = new NodeTraversal(null, null);
        scopedAliases.enterScope(t);
    }

    @Test
    public void testExitScope() {
        NodeTraversal t = new NodeTraversal(null, null);
        scopedAliases.exitScope(t);
    }

    @Test
    public void testShouldTraverse() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.SCRIPT);
        Node parent = new Node(Token.SCRIPT);
        boolean result = scopedAliases.shouldTraverse(t, n, parent);
    }

    @Test
    public void testValidateScopeCall() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.CALL);
        Node parent = new Node(Token.CALL);
        scopedAliases.validateScopeCall(t, n, parent);
    }

    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.SCRIPT);
        Node parent = new Node(Token.SCRIPT);
        scopedAliases.visit(t, n, parent);
    }

    @Test
    public void testFixTypeNode() {
        Node typeNode = new Node(Token.STRING);
        scopedAliases.fixTypeNode(typeNode);
    }

    @Test
    public void testScopedAliases() {
        scopedAliases = new ScopedAliases(null, null, aliasTransformationHandler);
    }

    @Test
    public void testScopedAliasesWithParams() {
        scopedAliases = new ScopedAliases(null, null, aliasTransformationHandler);
    }

}