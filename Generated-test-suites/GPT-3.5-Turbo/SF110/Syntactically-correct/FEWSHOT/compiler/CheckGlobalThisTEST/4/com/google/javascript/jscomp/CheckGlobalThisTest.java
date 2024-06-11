import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler mockCompiler;

    @Before
    public void setUp() {
        mockCompiler = Mockito.mock(AbstractCompiler.class);
        checkGlobalThis = new CheckGlobalThis(mockCompiler);
    }

    @Test
    public void testShouldTraverse_functionIsConstructor() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        node.setJSDocInfo(jsDocInfo);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Mockito.when(t.getNode()).thenReturn(node);
        Mockito.when(t.getParent()).thenReturn(null);

        boolean result = checkGlobalThis.shouldTraverse(t, node, null);

        assert result == false;
    }

    @Test
    public void testShouldTraverse_functionHasThisType() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType("SomeType");
        node.setJSDocInfo(jsDocInfo);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Mockito.when(t.getNode()).thenReturn(node);
        Mockito.when(t.getParent()).thenReturn(null);

        boolean result = checkGlobalThis.shouldTraverse(t, node, null);

        assert result == false;
    }

    @Test
    public void testShouldTraverse_functionHasOverrideAnnotation() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);
        node.setJSDocInfo(jsDocInfo);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Mockito.when(t.getNode()).thenReturn(node);
        Mockito.when(t.getParent()).thenReturn(null);

        boolean result = checkGlobalThis.shouldTraverse(t, node, null);

        assert result == false;
    }

    @Test
    public void testShouldTraverse_notFunctionParentIsNotValid() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        node.setJSDocInfo(jsDocInfo);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node parent = new Node(Token.ADD);
        Mockito.when(t.getNode()).thenReturn(node);
        Mockito.when(t.getParent()).thenReturn(parent);

        boolean result = checkGlobalThis.shouldTraverse(t, node, parent);

        assert result == false;
    }

    @Test
    public void testShouldTraverse_parentIsVarWithNoAnnotation() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        node.setJSDocInfo(jsDocInfo);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node parent = new Node(Token.NAME);
        Node grandParent = new Node(Token.VAR);
        parent.addChildToBack(grandParent);
        Mockito.when(t.getNode()).thenReturn(node);
        Mockito.when(t.getParent()).thenReturn(parent);

        boolean result = checkGlobalThis.shouldTraverse(t, node, parent);

        assert result == false;
    }

    @Test
    public void testShouldTraverse_parentIsVarWithAnnotation() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        node.setJSDocInfo(jsDocInfo);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node parent = new Node(Token.NAME);
        Node grandParent = new Node(Token.VAR);
        JSDocInfo grandParentJsDocInfo = new JSDocInfo();
        grandParent.setJSDocInfo(grandParentJsDocInfo);
        parent.addChildToBack(grandParent);
        Mockito.when(t.getNode()).thenReturn(node);
        Mockito.when(t.getParent()).thenReturn(parent);

        boolean result = checkGlobalThis.shouldTraverse(t, node, parent);

        assert result == true;
    }

    @Test
    public void testVisit_thisShouldReport() {
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETELEM);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Mockito.when(t.makeError(node, CheckGlobalThis.GLOBAL_THIS)).thenReturn(null);
        checkGlobalThis.assignLhsChild = null;

        checkGlobalThis.visit(t, node, parent);

        Mockito.verify(mockCompiler).report(null);
        assert checkGlobalThis.assignLhsChild == null;
    }
    
    @Test
    public void testVisit_thisShouldNotReport() {
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);

        checkGlobalThis.visit(t, node, parent);

        Mockito.verify(mockCompiler, Mockito.never()).report(null);
        assert checkGlobalThis.assignLhsChild == null;
    }
}