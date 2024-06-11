import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.assertEquals;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenConstructorWithThisTypeAnnotation() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        node.setJSDocInfo(jsDocInfo);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenFunctionWithThisTypeAnnotation() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        node.setJSDocInfo(jsDocInfo);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenFunctionWithoutThisTypeAnnotation() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        node.setJSDocInfo(jsDocInfo);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenNodeIsNotFunction() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.VAR);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenParentIsAssignAndNodeIsLeftSideOfAssign() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(node);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenParentIsAssignAndNodeIsRightSideOfAssign() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(node);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenParentIsAssignAndNodeIsRightSideOfAssignAndAssignIsToPrototype() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(node);
        Node lhs = new Node(Token.NAME);
        lhs.addChildToBack(new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "prototype")));
        parent.addChildToFront(lhs);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenParentIsAssignAndNodeIsRightSideOfAssignAndAssignIsToSubpropertyOfPrototype() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(node);
        Node lhs = new Node(Token.NAME);
        lhs.addChildToBack(new Node(Token.GETPROP, new Node(Token.STRING, "window"), new Node(Token.STRING, "prototype.property")));
        parent.addChildToFront(lhs);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnTrueWhenParentIsAssignAndNodeIsNotLeftOrRightSideOfAssign() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(new Node(Token.STRING, "other"));
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnTrueWhenParentIsNullAndNodeTypeIsFunction() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.FUNCTION);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);
        assertEquals(true, result);
    }
    
    @Test
    public void testVisit_shouldReportErrorWhenNodeTypeIsThisAndShouldReportThisIsTrue() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        checkGlobalThis.assignLhsChild = null;
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        compiler.report = jest.fn();
        checkGlobalThis.visit(nodeTraversal, node, parent);
        expect(compiler.report).toHaveBeenCalledWith(expect.any(Error));
        expect(checkGlobalThis.assignLhsChild).toBe(null);
    }
    
    @Test
    public void testVisit_shouldNotReportErrorWhenNodeTypeIsThisAndShouldReportThisIsFalse() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        compiler.report = jest.fn();
        checkGlobalThis.visit(nodeTraversal, node, parent);
        expect(compiler.report).not.toHaveBeenCalled();
        expect(checkGlobalThis.assignLhsChild).toBe(null);
    }
}