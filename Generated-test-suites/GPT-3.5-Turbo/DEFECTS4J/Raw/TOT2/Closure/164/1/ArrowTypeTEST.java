import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testArrowTypeConstructor() {
        Node parameters = new Node(Token.EMPTY);
        JSType returnType = JSTypeNative.NUMBER_TYPE;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        assertEquals(parameters, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
    }

    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType2 = new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.STRING_TYPE);
        
        assertFalse(arrowType2.isSubtype(arrowType1));
    }

    @Test
    public void testIsEquivalentTo() {
        ArrowType arrowType1 = new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType2 = new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.NUMBER_TYPE);
        
        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType arrowType = new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.NUMBER_TYPE);
        arrowType.getLeastSupertype(new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.STRING_TYPE));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType arrowType = new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.NUMBER_TYPE);
        arrowType.getGreatestSubtype(new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.STRING_TYPE));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType arrowType = new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.NUMBER_TYPE);
        arrowType.testForEquality(new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.STRING_TYPE));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType arrowType = new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.NUMBER_TYPE);
        arrowType.visit(visitor);
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new DefaultErrorReporter();
        StaticScope<JSType> scope = new DefaultStaticScope<>();
        ArrowType arrowType = new ArrowType(registry, new Node(Token.EMPTY), JSTypeNative.NUMBER_TYPE);
        arrowType.resolveInternal(errorReporter, scope);

        assertEquals(JSTypeNative.NUMBER_TYPE, arrowType.returnType);
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        Node parameters = new Node(Token.EMPTY);
        JSType returnType = JSTypeNative.NUMBER_TYPE;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        Node parameters = new Node(Token.EMPTY);
        JSType returnType = JSTypeNative.NUMBER_TYPE;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        assertEquals(super.toString(), arrowType.toStringHelper(true));
    }
}