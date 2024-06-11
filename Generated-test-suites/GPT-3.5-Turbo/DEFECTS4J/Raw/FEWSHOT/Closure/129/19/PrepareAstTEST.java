package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class PrepareAstTest {
    private PrepareAst prepareAst;
    
    @Before
    public void setUp() {
        prepareAst = new PrepareAst(compiler);
    }
    
    @Test
    public void testProcess_withExternsAndRoot() {
        Node externs = IR.root(IR.externs(IR.var(IR.name("externVar"))));
        Node root = IR.root(IR.script(IR.var(IR.name("rootVar"))));
        
        prepareAst.process(externs, root);
        
        assertEquals(externs, compiler.getExternsRoot());
        assertEquals(root, compiler.getRoot());
    }
    
    @Test
    public void testProcess_withExternsOnly() {
        Node externs = IR.root(IR.externs(IR.var(IR.name("externVar"))));
        Node root = null;
        
        prepareAst.process(externs, root);
        
        assertEquals(externs, compiler.getExternsRoot());
        assertNull(compiler.getRoot());
    }
    
    @Test
    public void testProcess_withRootOnly() {
        Node externs = null;
        Node root = IR.root(IR.script(IR.var(IR.name("rootVar"))));
        
        prepareAst.process(externs, root);
        
        assertNull(compiler.getExternsRoot());
        assertEquals(root, compiler.getRoot());
    }
    
    @Test
    public void testNormalizeNodeTypes_withNullNode() {
        Node n = null;
        
        prepareAst.normalizeNodeTypes(n);
        
        assertNull(n);
    }
    
    @Test
    public void testNormalizeNodeTypes_withEmptyNode() {
        Node n = IR.empty();
        
        prepareAst.normalizeNodeTypes(n);
        
        assertFalse(n.hasChildren());
    }
    
    @Test
    public void testNormalizeNodeTypes_withBlocks() {
        Node n = IR.block(
                IR.exprResult(IR.call(IR.name("foo"))),
                IR.ifNode(IR.trueNode(), IR.block(IR.exprResult(IR.call(IR.name("bar")))), null),
                IR.var(IR.name("x"), IR.trueNode())
                );
        
        prepareAst.normalizeNodeTypes(n);
        
        assertTrue(n.hasChildren());
        assertEquals(IR.block(), n.getFirstChild());
        assertEquals(IR.block(IR.exprResult(IR.call(IR.name("foo")))), n.getFirstChild().getNext());
        assertEquals(IR.block(IR.exprResult(IR.call(IR.name("bar")))), n.getFirstChild().getNext().getNext());
        assertEquals(IR.block(IR.var(IR.name("x"), IR.trueNode())), n.getLastChild());
    }
    
    @Test
    public void testPrepareAnnotations_withObjectLiteral() {
        Node objlit = IR.objectlit(IR.key("a"), IR.string("b"), IR.key("c"), IR.string("d"));
        
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        
        prepareAnnotations.normalizeObjectLiteralAnnotations(objlit);
        
        assertEquals(IR.key("a"), objlit.getFirstChild());
        assertEquals(IR.string("b"), objlit.getFirstChild().getFirstChild());
        assertEquals(IR.key("c"), objlit.getFirstChild().getNext());
        assertEquals(IR.string("d"), objlit.getFirstChild().getNext().getFirstChild());
    }
    
    @Test
    public void testPrepareAnnotations_withCalls() {
        Node call1 = IR.call(IR.name("foo"));
        Node call2 = IR.call(IR.getprop(IR.name("obj"), IR.string("method")));
        Node call3 = IR.call(IR.name("eval"));
        Node call4 = IR.call(IR.getelem(IR.name("arr"), IR.number(0)));
        
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        
        prepareAnnotations.annotateCalls(call1);
        prepareAnnotations.annotateCalls(call2);
        prepareAnnotations.annotateCalls(call3);
        prepareAnnotations.annotateCalls(call4);
        
        assertFalse(call1.getBooleanProp(Node.FREE_CALL));
        assertFalse(call2.getBooleanProp(Node.FREE_CALL));
        assertFalse(call3.getBooleanProp(Node.FREE_CALL));
        assertFalse(call4.getBooleanProp(Node.FREE_CALL));
        
        assertFalse(call1.getBooleanProp(Node.DIRECT_EVAL));
        assertFalse(call2.getBooleanProp(Node.DIRECT_EVAL));
        assertFalse(call4.getBooleanProp(Node.DIRECT_EVAL));
        assertTrue(call3.getBooleanProp(Node.DIRECT_EVAL));
    }
    
    @Test
    public void testPrepareAnnotations_withDispatchers() {
        Node function = IR.function(IR.name("foo"), IR.block(IR.exprResult(IR.name("x"))));
        Node var = IR.var(IR.name("fn"), IR.assign(function));
        
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        
        prepareAnnotations.annotateDispatchers(function, var);
        
        assertTrue(function.getBooleanProp(Node.IS_DISPATCHER));
        assertFalse(node.hasChildren());
    }
    
    @Test
    public void testPrepareAnnotations_withFunctionNodes() {
        Node function1 = IR.function(IR.name("foo"), IR.block(IR.exprResult(IR.name("x"))));
        Node function2 = IR.function(IR.name("bar"), IR.block(IR.exprResult(IR.name("y"))));
        
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        
        prepareAnnotations.visit(null, function1, null);
        prepareAnnotations.visit(null, function2, null);
        
        assertTrue(function1.getBooleanProp(Node.IS_DISPATCHER));
        assertFalse(function1.hasChildren());
        
        assertFalse(function2.getBooleanProp(Node.IS_DISPATCHER));
        assertFalse(function2.hasChildren());
    }
    
}