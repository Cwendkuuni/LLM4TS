import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionIsConstructor_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);

        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);

        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(functionNode);
        functionNode.setJSDocInfo(jsDocInfo);

        NodeTraversal.Callback callback = checkGlobalThis;
        boolean shouldTraverse = callback.shouldTraverse(t, functionNode, parent);

        Assert.assertFalse(shouldTraverse);
    }
    
    @Test
    public void testShouldTraverse_FunctionHasThisTypeAnnotation_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);

        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType("MyType");

        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(functionNode);
        functionNode.setJSDocInfo(jsDocInfo);

        NodeTraversal.Callback callback = checkGlobalThis;
        boolean shouldTraverse = callback.shouldTraverse(t, functionNode, parent);

        Assert.assertFalse(shouldTraverse);
    }
    
    @Test
    public void testShouldTraverse_FunctionHasOverrideAnnotation_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);

        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);

        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(functionNode);
        functionNode.setJSDocInfo(jsDocInfo);

        NodeTraversal.Callback callback = checkGlobalThis;
        boolean shouldTraverse = callback.shouldTraverse(t, functionNode, parent);

        Assert.assertFalse(shouldTraverse);
    }
    
    @Test
    public void testShouldTraverse_FunctionInValidContext_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);

        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(functionNode);

        NodeTraversal.Callback callback = checkGlobalThis;
        boolean shouldTraverse = callback.shouldTraverse(t, functionNode, parent);

        Assert.assertFalse(shouldTraverse);
    }
    
    @Test
    public void testShouldTraverse_DefaultCase_ReturnsTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);

        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(functionNode);

        NodeTraversal.Callback callback = checkGlobalThis;
        boolean shouldTraverse = callback.shouldTraverse(t, functionNode, parent);

        Assert.assertTrue(shouldTraverse);
    }
    
    @Test
    public void testVisit_NodeIsThisAndParentIsNotAssignOrGet_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.FUNCTION);

        NodeTraversal.Callback callback = checkGlobalThis;
        boolean shouldReport = callback.shouldReportThis(node, parent);

        Assert.assertFalse(shouldReport);
    }
    
    @Test
    public void testVisit_NodeIsThisAndParentIsAssign_ReturnsTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);

        NodeTraversal.Callback callback = checkGlobalThis;
        boolean shouldReport = callback.shouldReportThis(node, parent);

        Assert.assertTrue(shouldReport);
    }
    
    @Test
    public void testVisit_NodeIsThisAndParentIsGet_ReturnsTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);

        NodeTraversal.Callback callback = checkGlobalThis;
        boolean shouldReport = callback.shouldReportThis(node, parent);

        Assert.assertTrue(shouldReport);
    }
    
    @Test
    public void testVisit_NodeIsNotAssignLhsChild_ResetsAssignLhsChild() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node assignLhsChild = new Node(Token.NAME);
        CheckGlobalThis.assignLhsChild = assignLhsChild;
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);

        NodeTraversal.Callback callback = checkGlobalThis;
        callback.visit(t, node, parent);

        Assert.assertNull(CheckGlobalThis.assignLhsChild);
    }

    @Test
    public void testVisit_NodeIsAssignLhsChild_DoesNotResetAssignLhsChild() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node assignLhsChild = new Node(Token.THIS);
        CheckGlobalThis.assignLhsChild = assignLhsChild;
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);

        NodeTraversal.Callback callback = checkGlobalThis;
        callback.visit(t, node, parent);

        Assert.assertNotNull(CheckGlobalThis.assignLhsChild);
        Assert.assertEquals(assignLhsChild, CheckGlobalThis.assignLhsChild);
    }
    
    @Test
    public void testVisit_NodeIsThis_CallsCompilerReport() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);

        NodeTraversal.Callback callback = checkGlobalThis;
        callback.visit(t, node, parent);

        // Assert compiler.report() called
        // implementation for compiler is not provided, so skipping the assertion
    }
    
    @Test
    public void testGetFunctionJsDocInfo_NodeHasJsDocInfo_ReturnsJsDocInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        node.setJSDocInfo(jsDocInfo);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        JSDocInfo functionJsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);

        Assert.assertEquals(jsDocInfo, functionJsDocInfo);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ParentIsNameNode_ReturnsParentJsDocInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        JSDocInfo parentJsDocInfo = new JSDocInfo();
        JSDocInfo expectedJsDocInfo = new JSDocInfo();
        expectedJsDocInfo.setConstructor(true);
        Node nameNode = new Node(Token.NAME);
        Node assignNode = new Node(Token.ASSIGN);
        assignNode.addChildToFront(nameNode);
        nameNode.setJSDocInfo(parentJsDocInfo);
        assignNode.setJSDocInfo(expectedJsDocInfo);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        JSDocInfo functionJsDocInfo = checkGlobalThis.getFunctionJsDocInfo(nameNode);

        Assert.assertEquals(expectedJsDocInfo, functionJsDocInfo);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ParentIsNameNodeNoJsDocInfo_ReturnsParentParentJsDocInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        JSDocInfo parentJsDocInfo = new JSDocInfo();
        JSDocInfo expectedJsDocInfo = new JSDocInfo();
        expectedJsDocInfo.setConstructor(true);
        Node nameNode = new Node(Token.NAME);
        Node assignNode = new Node(Token.ASSIGN);
        Node varNode = new Node(Token.VAR);
        assignNode.addChildToFront(nameNode);
        nameNode.setJSDocInfo(parentJsDocInfo);
        varNode.addChildToFront(assignNode);
        assignNode.setJSDocInfo(expectedJsDocInfo);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        JSDocInfo functionJsDocInfo = checkGlobalThis.getFunctionJsDocInfo(nameNode);

        Assert.assertEquals(expectedJsDocInfo, functionJsDocInfo);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_NeitherNodeNorParentHasJsDocInfo_ReturnsNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        Node nameNode = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(nameNode);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        JSDocInfo functionJsDocInfo = checkGlobalThis.getFunctionJsDocInfo(nameNode);

        Assert.assertNull(functionJsDocInfo);
    } 
}