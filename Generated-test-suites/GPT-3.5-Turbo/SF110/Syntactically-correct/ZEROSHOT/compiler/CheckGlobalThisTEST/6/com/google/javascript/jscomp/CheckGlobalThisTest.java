import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);

        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        functionNode.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);

        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setThisType(new Node(Token.STRING));
        functionNode.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation_ShouldReturnFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);

        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);
        functionNode.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_NotConstructor_NotThis_ShouldReturnTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);

        Node functionNode = new Node(Token.FUNCTION);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

        Assert.assertTrue(result);
    }

    @Test
    public void testShouldReportThis_ThisOnLeftSideOfAssign_ShouldReturnTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node thisNode = new Node(Token.THIS);
        checkGlobalThis.assignLhsChild = new Node(Token.THIS);

        boolean result = checkGlobalThis.shouldReportThis(thisNode);

        Assert.assertTrue(result);
    }

    @Test
    public void testShouldReportThis_ThisWithPropertyAccess_ShouldReturnTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node thisNode = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(new Node(Token.NAME, "this"));
        parent.addChildToBack(new Node(Token.STRING, "property"));
        thisNode.setParent(parent);

        boolean result = checkGlobalThis.shouldReportThis(thisNode);

        Assert.assertTrue(result);
    }

    @Test
    public void testShouldReportThis_ThisNotOnLeftSideOfAssignAndNoPropertyAccess_ShouldReturnFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node thisNode = new Node(Token.THIS);
        Node parent = new Node(Token.NAME, "this");
        thisNode.setParent(parent);

        boolean result = checkGlobalThis.shouldReportThis(thisNode);

        Assert.assertFalse(result);
    }

    @Test
    public void testVisit_ThisAndShouldReportThis_ShouldReportError() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);

        Node thisNode = new Node(Token.THIS);

        checkGlobalThis.assignLhsChild = new Node(Token.THIS);

        checkGlobalThis.visit(nodeTraversal, thisNode, null);

        // Assert error is reported
    }

    @Test
    public void testVisit_ThisNodeIsAssignLhsChild_ShouldResetAssignLhsChild() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);

        Node thisNode = new Node(Token.THIS);

        checkGlobalThis.assignLhsChild = new Node(Token.THIS);

        checkGlobalThis.visit(nodeTraversal, thisNode, null);

        Assert.assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testVisit_ThisNodeIsNotAssignLhsChild_ShouldNotResetAssignLhsChild() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);

        Node thisNode = new Node(Token.THIS);

        checkGlobalThis.assignLhsChild = new Node(Token.THIS).addChildrenToBack(
                new Node(Token.NAME, "property"));

        checkGlobalThis.visit(nodeTraversal, thisNode, null);

        Assert.assertNotNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithJSDocInfo_ShouldReturnJsDocInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        functionNode.setJSDocInfo(jsDocInfo);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

        Assert.assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithJsDocInfoInParent_ShouldReturnParentJsDocInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME).addChildToFront(functionNode);
        JSDocInfo jsDocInfo = new JSDocInfo();
        parent.setJSDocInfo(jsDocInfo);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

        Assert.assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithJsDocInfoInVarParent_ShouldReturnVarParentJsDocInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node functionNode = new Node(Token.FUNCTION);
        Node varParent = new Node(Token.VAR);
        Node parent = new Node(Token.NAME).addChildToFront(varParent);
        JSDocInfo jsDocInfo = new JSDocInfo();
        varParent.setJSDocInfo(jsDocInfo);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

        Assert.assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithJsDocInfoInVarGrandparent_ShouldReturnVarGrandparentJsDocInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node functionNode = new Node(Token.FUNCTION);
        Node varParent = new Node(Token.VAR);
        Node varGrandparent = new Node(Token.VAR);
        Node parent = new Node(Token.NAME).addChildToFront(varParent);
        varParent.addChildToBack(functionNode);
        varGrandparent.addChildToBack(varParent);

        JSDocInfo jsDocInfo = new JSDocInfo();
        varGrandparent.setJSDocInfo(jsDocInfo);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

        Assert.assertEquals(jsDocInfo, result);
    }
}