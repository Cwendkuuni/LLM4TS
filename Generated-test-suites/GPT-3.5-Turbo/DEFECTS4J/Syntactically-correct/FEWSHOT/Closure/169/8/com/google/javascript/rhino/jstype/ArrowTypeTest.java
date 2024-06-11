package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
    
    private ArrowType arrowType;
    private JSTypeRegistry registry;
    
    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        arrowType = new ArrowType(registry, null, null);
    }
    
    @Test
    public void testIsSubtype() {
        ArrowType other = new ArrowType(registry, null, null);
        assertTrue(arrowType.isSubtype(other));
        
        other = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE));
        assertTrue(arrowType.isSubtype(other));
        
        other = new ArrowType(registry, null, registry.createUnionType(
            getNativeType(UNKNOWN_TYPE), getNativeType(BOOLEAN_TYPE)));
        assertFalse(arrowType.isSubtype(other));
    }
    
    @Test
    public void testHasEqualParameters() {
        ArrowType that = new ArrowType(registry, null, null);
        assertTrue(arrowType.hasEqualParameters(that, true));
        
        Node parameters = new Node(Token.FUNCTION, Node.newString(Token.NAME, Token.STRING, "param"));
        that = new ArrowType(registry, parameters, null);
        assertFalse(arrowType.hasEqualParameters(that, true));
        
        that = new ArrowType(registry, null, null);
        assertFalse(arrowType.hasEqualParameters(that, true));
    }
    
    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType that = new ArrowType(registry, null, null);
        assertTrue(arrowType.checkArrowEquivalenceHelper(that, true));
        
        that = new ArrowType(registry, null, registry.createUnionType(
            getNativeType(UNKNOWN_TYPE), getNativeType(BOOLEAN_TYPE)));
        assertFalse(arrowType.checkArrowEquivalenceHelper(that, true));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, arrowType.hashCode());
        
        arrowType.returnType = getNativeType(BOOLEAN_TYPE);
        arrowType.parameters = new Node(Token.FUNCTION, Node.newString(Token.NAME, Token.STRING, "param"));
        Node paramNode = new Node(Token.STRING, Node.newString(Token.NAME, Token.STRING, "param"));
        paramNode.putProp(Node.JSTYPE_PROP, getNativeType(NUMBER_TYPE));
        arrowType.parameters.addChildToFront(paramNode);
        assertEquals(4, arrowType.hashCode());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        arrowType.getLeastSupertype(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        arrowType.getGreatestSubtype(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        arrowType.testForEquality(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        arrowType.visit(null);
    }
    
    @Test
    public void testGetPossibleToBooleanOutcomes() {
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testResolveInternal() {
        arrowType.resolveInternal(null, null);
    }
    
    @Test
    public void testHasUnknownParamsOrReturn() {
        assertFalse(arrowType.hasUnknownParamsOrReturn());
        
        arrowType.returnType = getNativeType(UNKNOWN_TYPE);
        assertTrue(arrowType.hasUnknownParamsOrReturn());
    }
    
    @Test
    public void testToStringHelper() {
        assertEquals("[ArrowType]", arrowType.toStringHelper(true));
    }
    
    @Test
    public void testHasAnyTemplateInternal() {
        assertFalse(arrowType.hasAnyTemplateInternal());
        
        arrowType.returnType = registry.createPrimitiveType(TokenType.STRING);
        arrowType.parameters = new Node(Token.FUNCTION, Node.newString(Token.NAME, Token.STRING, "param"));
        Node paramNode = new Node(Token.STRING, Node.newString(Token.NAME, Token.STRING, "param"));
        paramNode.putProp(Node.JSTYPE_PROP, registry.createTemplateType(TokenType.STRING));
        arrowType.parameters.addChildToFront(paramNode);
        assertTrue(arrowType.hasAnyTemplateInternal());
    }
}