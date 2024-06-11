import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrowTypeTest {

    private ArrowType arrowType;

    @Before
    public void setUp() {
        arrowType = new ArrowType(new JSTypeRegistry(), null, null);
    }

    // Testing the isSubtype method

    @Test
    public void testIsSubtype_WhenOtherIsNotArrowType_ReturnsFalse() {
        JSType other = new JSType(new JSTypeRegistry());

        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_WhenOtherHasDifferentReturnType_ReturnsFalse() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null, new JSType(new JSTypeRegistry()));

        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_WhenOtherHasDifferentParameterCount_ReturnsFalse() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null, null);
        other.parameters.addChildToBack(new Node(Token.NAME));

        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubtype_WhenOtherHasDifferentParameterTypes_ReturnsFalse() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null, null);
        arrowType.parameters.addChildToBack(Node.newNumber(1));
        other.parameters.addChildToBack(new Node(Token.NAME));

        assertFalse(arrowType.isSubtype(other));
    }

    // More test cases could be added here...

    // Testing the hasEqualParameters method

    @Test
    public void testHasEqualParameters_WhenParameterListsAreNull_ReturnsTrue() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null, null);

        assertTrue(arrowType.hasEqualParameters(other, false));
    }

    @Test
    public void testHasEqualParameters_WhenParameterListsHaveDifferentLength_ReturnsFalse() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null, null);
        other.parameters.addChildToBack(new Node(Token.NAME));

        assertFalse(arrowType.hasEqualParameters(other, false));
    }

    @Test
    public void testHasEqualParameters_WhenParameterListsHaveDifferentTypes_ReturnsFalse() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null, null);
        arrowType.parameters.addChildToBack(Node.newString("param1"));
        other.parameters.addChildToBack(new Node(Token.NAME));

        assertFalse(arrowType.hasEqualParameters(other, false));
    }

    // More test cases could be added here...

    // Testing the checkArrowEquivalenceHelper method

    @Test
    public void testCheckArrowEquivalenceHelper_WhenReturnTypesAreDifferent_ReturnsFalse() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null, new JSType(new JSTypeRegistry()));

        assertFalse(arrowType.checkArrowEquivalenceHelper(other, false));
    }

    @Test
    public void testCheckArrowEquivalenceHelper_WhenParameterListsAreNotEqual_ReturnsFalse() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null, null);
        arrowType.parameters.addChildToBack(new Node(Token.NAME));

        assertFalse(arrowType.checkArrowEquivalenceHelper(other, false));
    }

    // More test cases could be added here...

    // Testing the hashCode method

    @Test
    public void testHashCode_WhenReturnTypeIsNull_ReturnsZero() {
        arrowType.returnType = null;

        assertEquals(0, arrowType.hashCode());
    }

    @Test
    public void testHashCode_WhenReturnTypeIsNotNull_ReturnsHashCodeOfReturnType() {
        JSType returnType = new JSType(new JSTypeRegistry());
        arrowType.returnType = returnType;

        assertEquals(returnType.hashCode(), arrowType.hashCode());
    }

    // More test cases could be added here...

}