import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverseForGlobalContext() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testVisitForThisNode() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        // Act
        checkGlobalThis.visit(t, n, parent);

        // No assertions as this method only reports errors
    }

    @Test
    public void testGetFunctionJsDocInfoForFunctionWithJsDoc() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node();
        n.setJSDocInfo(new JSDocInfo());

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetFunctionJsDocInfoForNameWithJsDoc() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node();
        Node parent = new Node();
        n.setParent(parent);
        parent.setType(Token.NAME);
        parent.setJSDocInfo(new JSDocInfo());

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetFunctionJsDocInfoForVarWithNameWithJsDoc() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node();
        Node parent = new Node();
        Node gramps = new Node();
        n.setParent(parent);
        parent.setType(Token.NAME);
        parent.setParent(gramps);
        gramps.setType(Token.VAR);
        gramps.setJSDocInfo(new JSDocInfo());

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetFunctionJsDocInfoForFunctionWithoutJsDoc() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node();

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testGetFunctionJsDocInfoForNameWithoutJsDoc() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node();
        Node parent = new Node();
        n.setParent(parent);
        parent.setType(Token.NAME);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testGetFunctionJsDocInfoForVarWithoutJsDoc() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node();
        Node parent = new Node();
        Node gramps = new Node();
        n.setParent(parent);
        parent.setType(Token.NAME);
        parent.setParent(gramps);
        gramps.setType(Token.VAR);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        Assert.assertNull(result);
    }

}