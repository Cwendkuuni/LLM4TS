import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse_Function_Constructor() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
        assertTrue(CheckGlobalThis.shouldTraverse(t, n, null));
    }
    
    @Test
    public void testShouldTraverse_Function_ThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setThisType(new Node(Token.STRING));
        n.putProp(Node.JSDOC_INFO_PROP, jsDoc);
        assertFalse(CheckGlobalThis.shouldTraverse(t, n, null));
    }
    
    @Test
    public void testShouldTraverse_Function_NoLogicalPlace() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ADD);
        Node grandparent = new Node(Token.BLOCK);
        parent.addChildToFront(n);
        grandparent.addChildToFront(parent);
        assertFalse(CheckGlobalThis.shouldTraverse(t, n, grandparent));
    }
    
    @Test
    public void testShouldTraverse_Function_Block() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(n);
        assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_Function_Assign() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_ObjectLit_InsidePrototype() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.STRING_KEY);
        Node parent = new Node(Token.OBJECTLIT);
        Node grandparent = new Node(Token.ASSIGN);
        Node greatGrandparent = new Node(Token.BLOCK);
        grandparent.addChildToFront(n);
        greatGrandparent.addChildToFront(grandparent);
        parent.addChildToFront(n);
        grandparent.addChildToFront(parent);
        assertFalse(CheckGlobalThis.shouldTraverse(t, n, greatGrandparent));
    }
    
    @Test
    public void testShouldTraverse_Other() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.ADD);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(n);
        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldReportThis_Assign_LhsChildNotNull() {
        Node n = new Node(Token.THIS);
        CheckGlobalThis.assignLhsChild = new Node(Token.ADD);
        assertTrue(CheckGlobalThis.shouldReportThis(n));
    }
    
    @Test
    public void testShouldReportThis_GetParentNull() {
        Node n = new Node(Token.THIS);
        assertFalse(CheckGlobalThis.shouldReportThis(n));
    }
    
    @Test
    public void testShouldReportThis_GetParentNotGet() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.STRING);
        n.addChildToFront(parent);
        assertFalse(CheckGlobalThis.shouldReportThis(n));
    }
    
    @Test
    public void testShouldReportThis_IsGetPropPrototype() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        Node grandparent = new Node(Token.OBJECTLIT);
        n.addChildToFront(parent);
        parent.addChildToFront(new Node(Token.STRING, "prototype"));
        parent.addChildToFront(new Node(Token.NAME, "constructor"));
        grandparent.addChildToFront(parent);
        assertFalse(CheckGlobalThis.shouldReportThis(n));
    }
    
    @Test
    public void testShouldReportThis_IsGetPropNotPrototype() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        Node grandparent = new Node(Token.OBJECTLIT);
        n.addChildToFront(parent);
        parent.addChildToFront(new Node(Token.STRING, "prop"));
        parent.addChildToFront(new Node(Token.NAME, "obj"));
        grandparent.addChildToFront(parent);
        assertTrue(CheckGlobalThis.shouldReportThis(n));
    }
    
    @Test
    public void testVisit_IsThis_ShouldReportThis_True() {
        NodeTraversal t = new NodeTraversal();
        CheckGlobalThis compiler = new CheckGlobalThis(t);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);
        Node grandparent = new Node(Token.BLOCK);
        n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
        parent.addChildToFront(n);
        grandparent.addChildToFront(parent);
        compiler.visit(t, n, grandparent);
        // assert that the compiler reported an error
    }
    
    @Test
    public void testVisit_IsThis_ShouldReportThis_False() {
        NodeTraversal t = new NodeTraversal();
        CheckGlobalThis compiler = new CheckGlobalThis(t);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.STRING);
        Node grandparent = new Node(Token.BLOCK);
        parent.addChildToFront(n);
        grandparent.addChildToFront(parent);
        compiler.visit(t, n, grandparent);
        // assert that the compiler did not report an error
    }
    
    @Test
    public void testGetFunctionJsDocInfo_HasJsDoc() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.putProp(Node.JSDOC_INFO_PROP, jsDoc);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ParentIsName_HasJsDoc() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.putProp(Node.JSDOC_INFO_PROP, jsDoc);
        parent.addChildToFront(n);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ParentIsName_DoesNotHaveJsDoc() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(n);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertNull(result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ParentIsAssign_HasJsDoc() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.putProp(Node.JSDOC_INFO_PROP, jsDoc);
        parent.addChildToFront(n);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ParentIsAssign_DoesNotHaveJsDoc() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertNull(result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_GrandparentIsVar_HasJsDoc() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        Node grandparent = new Node(Token.VAR);
        JSDocInfo jsDoc = new JSDocInfo();
        grandparent.putProp(Node.JSDOC_INFO_PROP, jsDoc);
        grandparent.addChildToFront(parent);
        parent.addChildToFront(n);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_GrandparentIsVar_DoesNotHaveJsDoc() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        Node grandparent = new Node(Token.VAR);
        grandparent.addChildToFront(parent);
        parent.addChildToFront(n);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertNull(result);
    }
}