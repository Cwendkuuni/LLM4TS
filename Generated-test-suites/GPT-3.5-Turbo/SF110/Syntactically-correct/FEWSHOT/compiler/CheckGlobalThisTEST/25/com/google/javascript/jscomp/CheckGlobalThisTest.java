package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldReportThis() {
        NodeAssignmentTester nodeAssignmentTester = new NodeAssignmentTester();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(nodeAssignmentTester);
        
        // Test invalid assignments of "this"
        Node invalidAssignment1 = new Node(Token.ASSIGN);
        invalidAssignment1.addChildToBack(new Node(Token.THIS));
        Node invalidAssignment2 = new Node(Token.ASSIGN);
        invalidAssignment2.addChildToBack(new Node(Token.THIS));
        invalidAssignment2.getFirstChild().addChildToBack(new Node(Token.GETPROP));
        Node invalidAssignment3 = new Node(Token.ASSIGN);
        invalidAssignment3.addChildToBack(new Node(Token.THIS));
        invalidAssignment3.getFirstChild().addChildToBack(new Node(Token.GETPROP));
        invalidAssignment3.getFirstChild().getFirstChild().addChildToBack(new Node(Token.GETPROP));
        
        
        // Test valid assignment of "this"
        Node validAssignment1 = new Node(Token.ASSIGN);
        validAssignment1.addChildToBack(new Node(Token.GETPROP));
        validAssignment1.getFirstChild().addChildToBack(new Node(Token.THIS));
        
        Node validAssignment2 = new Node(Token.ASSIGN);
        validAssignment2.addChildToBack(new Node(Token.GETPROP));
        validAssignment2.getFirstChild().addChildToBack(new Node(Token.GETPROP));
        validAssignment2.getFirstChild().getFirstChild().addChildToBack(new Node(Token.THIS));
        
        Node validAssignment3 = new Node(Token.ASSIGN);
        validAssignment3.addChildToBack(new Node(Token.THIS));
        validAssignment3.getFirstChild().addChildToBack(new Node(Token.GETELEM));
        
        // Test shouldReportThis()
        assertEquals(true, checkGlobalThis.shouldReportThis(invalidAssignment1));
        assertEquals(true, checkGlobalThis.shouldReportThis(invalidAssignment2));
        assertEquals(true, checkGlobalThis.shouldReportThis(invalidAssignment3));
        assertEquals(false, checkGlobalThis.shouldReportThis(validAssignment1));
        assertEquals(false, checkGlobalThis.shouldReportThis(validAssignment2));
        assertEquals(false, checkGlobalThis.shouldReportThis(validAssignment3));
    }
    
    @Test
    public void testShouldTraverse() {
        NodeTraversalTester nodeTraversalTester = new NodeTraversalTester();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(nodeTraversalTester);
        
        Node validFunction1 = new Node(Token.FUNCTION);
        Node validFunction2 = new Node(Token.FUNCTION);
        Node validFunction3 = new Node(Token.FUNCTION);
        Node validFunction4 = new Node(Token.FUNCTION);
        Node validFunction5 = new Node(Token.FUNCTION);
        Node invalidFunction1 = new Node(Token.FUNCTION);
        Node invalidFunction2 = new Node(Token.FUNCTION);
        Node invalidFunction3 = new Node(Token.FUNCTION);
        Node invalidFunction4 = new Node(Token.FUNCTION);
        Node invalidFunction5 = new Node(Token.FUNCTION);
        
        JSDocTester jsDocTester = new JSDocTester();
        validFunction1.setJSDocInfo(jsDocTester.getFunctionJsDocInfo(true, false, false, false));
        validFunction2.setJSDocInfo(jsDocTester.getFunctionJsDocInfo(false, true, false, false));
        validFunction3.setJSDocInfo(jsDocTester.getFunctionJsDocInfo(false, false, true, false));
        validFunction4.setJSDocInfo(jsDocTester.getFunctionJsDocInfo(false, false, false, true));
        validFunction5.setJSDocInfo(jsDocTester.getFunctionJsDocInfo(true, false, false, true));
        invalidFunction1.setJSDocInfo(jsDocTester.getFunctionJsDocInfo(true, true, true, true));
        invalidFunction2.setJSDocInfo(jsDocTester.getFunctionJsDocInfo(false, false, false, false));
        invalidFunction3.setJSDocInfo(jsDocTester.getFunctionJsDocInfo(false, false, false, false));
        invalidFunction4.setJSDocInfo(jsDocTester.getFunctionJsDocInfo(false, false, false, false));
        invalidFunction5.setJSDocInfo(jsDocTester.getFunctionJsDocInfo(false, false, false, false));
        
        Node validParent1 = new Node(Token.BLOCK);
        Node validParent2 = new Node(Token.SCRIPT);
        Node validParent3 = new Node(Token.NAME);
        Node validParent4 = new Node(Token.ASSIGN);
        Node validParent5 = new Node(Token.STRING_KEY);
        Node invalidParent1 = new Node(Token.VAR);
        Node invalidParent2 = new Node(Token.FUNCTION);
        Node invalidParent3 = new Node(Token.OBJECTLIT);
        Node invalidParent4 = new Node(Token.GETPROP);
        
        validFunction1.setParent(validParent1);
        validFunction2.setParent(validParent2);
        validFunction3.setParent(validParent3);
        validFunction4.setParent(validParent4);
        validFunction5.setParent(validParent5);
        invalidFunction1.setParent(invalidParent1);
        invalidFunction2.setParent(invalidParent2);
        invalidFunction3.setParent(invalidParent3);
        invalidFunction4.setParent(invalidParent4);
        
        Node validNode1 = new Node(Token.THIS);
        Node validNode2 = new Node(Token.THIS);
        Node invalidNode1 = new Node(Token.THIS);
        Node invalidNode2 = new Node(Token.THIS);
        
        validNode1.getParent().setParent(validParent1);
        validNode2.getParent().setParent(validParent1);
        invalidNode1.getParent().setParent(invalidParent2);
        invalidNode2.getParent().setParent(invalidParent3);
        
        NodeTraversalTester nodeTraversalTester = new NodeTraversalTester();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(nodeTraversalTester);
        
        // Test shouldTraverse()
        assertEquals(true, checkGlobalThis.shouldTraverse(nodeTraversalTester, validNode1, validNode1.getParent().getParent()));
        assertEquals(true, checkGlobalThis.shouldTraverse(nodeTraversalTester, validNode2, validNode2.getParent().getParent()));
        assertEquals(false, checkGlobalThis.shouldTraverse(nodeTraversalTester, invalidNode1, invalidNode1.getParent().getParent()));
        assertEquals(true, checkGlobalThis.shouldTraverse(nodeTraversalTester, invalidNode2, invalidNode2.getParent().getParent()));

    }
    
    @Test
    public void testVisit() {
        NodeAssignmentTester nodeAssignmentTester = new NodeAssignmentTester();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(nodeAssignmentTester);
        
        Node validThis1 = new Node(Token.THIS);
        Node validThis2 = new Node(Token.THIS);
        Node invalidThis1 = new Node(Token.THIS);
        Node invalidThis2 = new Node(Token.THIS);
        
        // Test visit()
        checkGlobalThis.visit(nodeAssignmentTester, validThis1, validThis1.getParent());
        checkGlobalThis.visit(nodeAssignmentTester, validThis2, validThis2.getParent());
        checkGlobalThis.visit(nodeAssignmentTester, invalidThis1, invalidThis1.getParent());
        checkGlobalThis.visit(nodeAssignmentTester, invalidThis2, invalidThis2.getParent());
        
        assertEquals(true, nodeAssignmentTester.isReported());
        assertNull(nodeAssignmentTester.getAssignLhsChild());

    }
    
    @Test
    public void testGetFunctionJsDocInfo() {
        NodeAssignmentTester nodeAssignmentTester = new NodeAssignmentTester();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(nodeAssignmentTester);
        
        Node validNode1 = new Node(Token.FUNCTION);
        Node invalidNode1 = new Node(Token.FUNCTION);
        Node validNode2 = new Node(Token.NAME);
        Node validNode3 = new Node(Token.ASSIGN);
        
        JSDocInfoTester jsDocInfoTester = new JSDocInfoTester();
        validNode1.setJSDocInfo(jsDocInfoTester.getJSDocInfo(true));
        invalidNode1.setJSDocInfo(null);
        
        validNode2.getParent().setParent(validNode3);
        validNode3.getParent().setParent(validNode2.getParent());
        
        // Test getFunctionJsDocInfo()
        assertEquals(true, checkGlobalThis.getFunctionJsDocInfo(validNode1) != null);
        assertEquals(true, checkGlobalThis.getFunctionJsDocInfo(invalidNode1) == null);
        assertEquals(true, checkGlobalThis.getFunctionJsDocInfo(validNode2) == null);
        assertEquals(true, checkGlobalThis.getFunctionJsDocInfo(validNode3) != null);

    }
    
    class JSDocInfoTester {
        public JSDocInfo getJSDocInfo(boolean isConstructor) {
            JSDocInfo jsDocInfo = new JSDocInfo(false);
            
            if (isConstructor) {
                jsDocInfo.setConstructor();
            }
            
            return jsDocInfo;
        }
    }
    
    class NodeAssignmentTester extends AbstractCompiler {
        private boolean reported = false;
        private Node assignLhsChild = null;

        @Override
        public void report(JSError error) {
            this.reported = true;
        }

        @Override
        public ErrorReporter getDefaultErrorReporter() {
            return null;
        }

        public boolean isReported() {
            return reported;
        }

        public void setReported(boolean reported) {
            this.reported = reported;
        }

        public Node getAssignLhsChild() {
            return assignLhsChild;
        }

        public void setAssignLhsChild(Node assignLhsChild) {
            this.assignLhsChild = assignLhsChild;
        }
    }
    
    class NodeTraversalTester extends AbstractCompiler {
        @Override
        public void report(JSError error) {
        }

        @Override
        public ErrorReporter getDefaultErrorReporter() {
            return null;
        }
    }
}