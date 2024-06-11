
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setup() {
        AbstractCompiler compiler = new AbstractCompiler();
        checkGlobalThis = new CheckGlobalThis(compiler);
    }

    @Test
    public void testShouldReportThis_assignLhsChildNotNull_shouldReturnTrue() {
        checkGlobalThis.assignLhsChild = new Node(Token.BLOCK);
        Node n = new Node(Token.THIS);
        assertTrue(checkGlobalThis.shouldReportThis(n));
    }

    @Test
    public void testShouldReportThis_parentIsGet_shouldReturnTrue() {
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(new Node(Token.THIS));
        Node n = new Node(Token.THIS);
        n.setParent(parent);
        assertTrue(checkGlobalThis.shouldReportThis(n));
    }

    @Test
    public void testShouldReportThis_parentIsNull_shouldReturnFalse() {
        Node n = new Node(Token.THIS);
        assertFalse(checkGlobalThis.shouldReportThis(n));
    }

    @Test
    public void testShouldTraverse_functionConstructorJsDoc_shouldReturnFalse() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordConstructor();
        n.setJSDocInfo(jsDocInfo);
        assertFalse(checkGlobalThis.shouldTraverse(null, n, null));
    }

    @Test
    public void testShouldTraverse_functionInterfaceJsDoc_shouldReturnFalse() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordInterface();
        n.setJSDocInfo(jsDocInfo);
        assertFalse(checkGlobalThis.shouldTraverse(null, n, null));
    }

    @Test
    public void testShouldTraverse_functionHasThisTypeJsDoc_shouldReturnFalse() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        n.setJSDocInfo(jsDocInfo);
        assertFalse(checkGlobalThis.shouldTraverse(null, n, null));
    }

    @Test
    public void testShouldTraverse_functionOverrideJsDoc_shouldReturnFalse() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordOverride();
        n.setJSDocInfo(jsDocInfo);
        assertFalse(checkGlobalThis.shouldTraverse(null, n, null));
    }

    @Test
    public void testShouldTraverse_parentIsVar_shouldReturnTrue() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(n);
        assertTrue(checkGlobalThis.shouldTraverse(null, n, parent));
    }

    // Add more test methods to cover all public methods in CheckGlobalThis class. 

}
