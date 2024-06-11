import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse_notGlobalFunction() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
        
        Node functionNode = Node.newString(Token.FUNCTION, "functionNode");
        Node parent = Node.newString(Token.NAME, "parent");
        Node grandparent = Node.newString(Token.ASSIGN, "grandparent");
        parent.setParent(grandparent);
        functionNode.setParent(parent);
        
        boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_constructorFunction() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
        
        Node functionNode = Node.newString(Token.FUNCTION, "functionNode");
        Node parent = Node.newString(Token.NAME, "parent");
        Node grandparent = Node.newString(Token.ASSIGN, "grandparent");
        parent.setParent(grandparent);
        functionNode.setParent(parent);
        
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        
        functionNode.setJSDocInfo(jsDoc);
        
        boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_thisAnnotatedFunction() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
        
        Node functionNode = Node.newString(Token.FUNCTION, "functionNode");
        Node parent = Node.newString(Token.NAME, "parent");
        Node grandparent = Node.newString(Token.ASSIGN, "grandparent");
        parent.setParent(grandparent);
        functionNode.setParent(parent);
        
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        
        functionNode.setJSDocInfo(jsDoc);
        
        boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_propertyAssignment() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
        
        Node assign = Node.newString(Token.ASSIGN, "assign");
        Node lhs = Node.newString(Token.GETPROP, "lhs");
        Node rhs = Node.newString(Token.NAME, "rhs");
        assign.addChildToBack(lhs);
        assign.addChildToBack(rhs);
        
        Node grandparent = Node.newString(Token.ASSIGN, "grandparent");
        grandparent.addChildToBack(assign);
        
        Node parent = Node.newString(Token.NAME, "parent");
        parent.setParent(grandparent);
        
        boolean result = checkGlobalThis.shouldTraverse(traversal, lhs, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testVisit_globalThisEncountered() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
        
        Node thisNode = Node.newString(Token.THIS, "thisNode");
        Node parent = Node.newString(Token.NAME, "parent");
        
        JSType type = new JSType();
        type.addWarning(DiagnosticType.warning(
            "JSC_USED_GLOBAL_THIS",
            "dangerous use of the global 'this' object"));
        
        NodeTraversal.ErrorReport errorReport = new NodeTraversal.ErrorReport(traversal, type);
        traversal.setErrorHandler(errorReport);
        
        checkGlobalThis.visit(traversal, thisNode, parent);
        
        assertEquals(1, errorReport.getNumErrors());
    }
    
    @Test
    public void testVisit_assignLhsChildNotNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
        
        Node thisNode = Node.newString(Token.THIS, "thisNode");
        Node parent = Node.newString(Token.NAME, "parent");
        
        checkGlobalThis.visit(traversal, thisNode, parent);
        
        assertNull(checkGlobalThis.assignLhsChild);
    }
    
    private class AbstractCompiler {
        
        public void report(DiagnosticType type) {
            
        }
    }
    
    private class JSDocInfo {
        
        private boolean isConstructor;
        private boolean thisType;
        
        public void setConstructor(boolean isConstructor) {
            this.isConstructor = isConstructor;
        }
        
        public void recordThisType() {
            thisType = true;
        }
        
        public boolean isConstructor() {
            return isConstructor;
        }
        
        public boolean hasThisType() {
            return thisType;
        }
    }
    
    private class JSType {
        
        private List<DiagnosticType> warnings = new ArrayList<>();
        
        public void addWarning(DiagnosticType warning) {
            warnings.add(warning);
        }
        
        public List<DiagnosticType> getWarnings() {
            return warnings;
        }
    }
}