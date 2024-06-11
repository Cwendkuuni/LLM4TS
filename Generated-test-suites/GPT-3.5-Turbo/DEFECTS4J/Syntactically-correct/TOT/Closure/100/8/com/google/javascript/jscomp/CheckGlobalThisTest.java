import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.mockito.Mockito;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        // Typical use case
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);

        Mockito.when(n.getType()).thenReturn(Token.FUNCTION);
        Mockito.when(parent.getType()).thenReturn(Token.ASSIGN);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Verify that the function is not a constructor or has the @this annotation
        Mockito.verify(n, Mockito.times(1)).getType();
        Mockito.verify(t, Mockito.times(0)).makeError(Mockito.any(Node.class), Mockito.any(CheckLevel.class), Mockito.any(DiagnosticType.class));
        assert(result == true);
    }

    @Test
    public void testVisit() {
        // Typical use case
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);

        Mockito.when(n.getType()).thenReturn(Token.THIS);
        Mockito.when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);

        checkGlobalThis.visit(t, n, parent);

        // Verify that a report is made
        Mockito.verify(t, Mockito.times(1)).makeError(Mockito.any(Node.class), Mockito.any(CheckLevel.class), Mockito.any(DiagnosticType.class));
    }

    @Test
    public void testGetFunctionJSDocInfo() {
        // Typical use case
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);

        Mockito.when(n.getJSDocInfo()).thenReturn(null);
        Mockito.when(parent.getType()).thenReturn(Token.NAME);
        Mockito.when(parent.getJSDocInfo()).thenReturn(null);
        Mockito.when(parent.getParent()).thenReturn(Mockito.mock(Node.class));
        Mockito.when(parent.getParent().getType()).thenReturn(Token.VAR);
        Mockito.when(parent.getParent().getJSDocInfo()).thenReturn(Mockito.mock(JSDocInfo.class));

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Verify that the JSDoc information is retrieved from the parent of the parent
        Mockito.verify(n, Mockito.times(1)).getJSDocInfo();
        Mockito.verify(parent, Mockito.times(1)).getType();
        Mockito.verify(parent, Mockito.times(2)).getJSDocInfo();
        Mockito.verify(parent.getParent(), Mockito.times(1)).getType();
        Mockito.verify(parent.getParent().getJSDocInfo(), Mockito.times(1)).getJSDocInfo();
        assert(result != null);
    }

    @Test
    public void testShouldReportThis() {
        // Typical use case
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);

        Mockito.when(checkGlobalThis.shouldReportThis(n, parent)).thenCallRealMethod();

        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Verify that the THIS node is reported
        assert(result == false);
    }
}