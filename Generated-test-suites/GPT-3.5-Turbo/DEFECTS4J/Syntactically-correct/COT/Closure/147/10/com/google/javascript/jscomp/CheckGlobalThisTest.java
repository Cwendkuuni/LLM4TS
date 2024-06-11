import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_returnsTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testShouldTraverse_returnsFalse_constructorFunction() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfoBuilder().withConstructor(true).build());
        Node parent = null;

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_returnsFalse_functionWithThisAnnotation() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfoBuilder().withThisType("MyObject").build());
        Node parent = null;

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_returnsFalse_functionWithOverrideAnnotation() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfoBuilder().withOverride(true).build());
        Node parent = null;

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_returnsFalse_notInBlockOrScript() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ADD);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testVisit_reportsWarningWhenThisIsGlobal() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);

        // Act
        checkGlobalThis.visit(t, n, parent);
        DiagnosticType diagnosticType = t.getTypedErrorReports().get(0).getType();

        // Assert
        Assert.assertEquals(DiagnosticType.warning("JSC_USED_GLOBAL_THIS"), diagnosticType);
    }

    @Test
    public void testVisit_doesNotReportWarningWhenThisIsNotGlobal() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);

        // Act
        checkGlobalThis.visit(t, n, parent);
        boolean hasErrors = !t.getTypedErrorReports().isEmpty();

        // Assert
        Assert.assertFalse(hasErrors);
    }

    @Test
    public void testVisit_clearsAssignLhsChild() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null);
        Node n = null;
        Node parent = null;
        checkGlobalThis.shouldTraverse(t, new Node(Token.THIS), new Node(Token.ADD));

        // Act
        checkGlobalThis.visit(t, n, parent);
        Node assignLhsChild = checkGlobalThis.getAssignLhsChild();

        // Assert
        Assert.assertNull(assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_returnsNull() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);

        // Act
        JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        Assert.assertNull(jsDocInfo);
    }

    @Test
    public void testGetFunctionJsDocInfo_returnsJSDocInfo_fromFunction() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfoBuilder().withDescription("Function").build());
        Node parent = new Node(Token.NAME);

        // Act
        JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        Assert.assertNotNull(jsDocInfo);
        Assert.assertEquals("Function", jsDocInfo.getDescription());
    }

    @Test
    public void testGetFunctionJsDocInfo_returnsJSDocInfo_fromParentName() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.setJSDocInfo(new JSDocInfoBuilder().withDescription("Parent").build());

        // Act
        JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        Assert.assertNotNull(jsDocInfo);
        Assert.assertEquals("Parent", jsDocInfo.getDescription());
    }

    @Test
    public void testGetFunctionJsDocInfo_returnsJSDocInfo_fromParentVar() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.VAR);
        parent.setJSDocInfo(new JSDocInfoBuilder().withDescription("Parent").build());

        // Act
        JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        Assert.assertNotNull(jsDocInfo);
        Assert.assertEquals("Parent", jsDocInfo.getDescription());
    }
}