import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        // Don't traverse functions that are constructors or have the @this or @override annotation
        assertFalse(new CheckGlobalThis(null, null).shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.NAME)));
        assertFalse(new CheckGlobalThis(null, null).shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.CALL)));
        assertFalse(new CheckGlobalThis(null, null).shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.THIS)));
        
        // Don't traverse functions unless they would normally be able to have a @this annotation associated with them
        assertFalse(new CheckGlobalThis(null, null).shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.STRING)));
        assertFalse(new CheckGlobalThis(null, null).shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.NUMBER)));

        // Always traverse the left side of the assignment
        assertTrue(new CheckGlobalThis(null, null).shouldTraverse(null, new Node(Token.NAME), new Node(Token.ASSIGN)));
        
        // Only traverse the right side if it's not an assignment to a prototype property or subproperty
        assertFalse(new CheckGlobalThis(null, null).shouldTraverse(null, new Node(Token.GETPROP), new Node(Token.ASSIGN)));
        assertFalse(new CheckGlobalThis(null, null).shouldTraverse(null, new Node(Token.GETPROP), new Node(Token.GETPROP)));
    }

    @Test
    public void testVisit() {
        // Should report a THIS on the left side of an assign
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        checkGlobalThis.assignLhsChild = new Node(Token.THIS);
        assertFalse(checkGlobalThis.shouldReportThis(new Node(Token.THIS), null));

        // Should report a THIS with a property access
        assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.GETPROP)));
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        // Get function's JSDoc information
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        assertNull(checkGlobalThis.getFunctionJsDocInfo(functionNode));
        
        // Get function's parent's JSDoc information
        Node nameNode = new Node(Token.NAME);
        nameNode.setJSDocInfo(new JSDocInfo());
        Node parentNameNode = new Node(Token.NAME, nameNode);
        assertNull(checkGlobalThis.getFunctionJsDocInfo(functionNode));

        // Get function's parent's grandparent's JSDoc information
        Node varNode = new Node(Token.VAR, parentNameNode);
        varNode.setJSDocInfo(new JSDocInfo());
        assertNull(checkGlobalThis.getFunctionJsDocInfo(functionNode));

        // Get function's parent's JSDoc information
        parentNameNode.setJSDocInfo(new JSDocInfo());
        assertNotNull(checkGlobalThis.getFunctionJsDocInfo(functionNode));

        // Get function's JSDoc information
        functionNode.setJSDocInfo(new JSDocInfo());
        assertNotNull(checkGlobalThis.getFunctionJsDocInfo(functionNode));
    }
}