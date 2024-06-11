package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_BothObjectsNull_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_FirstObjectNullAndSecondObjectNotNull_ReturnsFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_FirstObjectNotNullAndSecondObjectNull_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_BothObjectsNotNullAndEqual_ReturnsTrue() {
        Object object = new Object();
        assertTrue(Equality.areEqual(object, object));
    }

    @Test
    public void testAreEqual_BothObjectsNotNullAndNotEqual_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreEqual_FirstObjectArrayAndSecondObjectNotArray_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object[]{1, 2, 3}, new Object()));
    }

    @Test
    public void testAreEqual_FirstObjectNotArrayAndSecondObjectArray_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object(), new Object[]{1, 2, 3}));
    }

    @Test
    public void testAreEqual_BothObjectsArrayWithDifferentLengths_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3, 4}));
    }

    @Test
    public void testAreEqual_BothObjectsArrayWithSameLengthsButDifferentElements_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 4}));
    }

    @Test
    public void testAreEqual_BothObjectsArrayWithSameLengthsAndElements_ReturnsTrue() {
        assertTrue(Equality.areEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3}));
    }

    @Test
    public void testAreEqual_FirstObjectArrayWithNullElementAndSecondObjectArray_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object[]{null, 2, 3}, new Object[]{1, 2, 3}));
    }

    @Test
    public void testAreEqual_FirstObjectArrayAndSecondObjectArrayWithNullElement_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object[]{1, 2, 3}, new Object[]{null, 2, 3}));
    }

    @Test
    public void testAreEqual_BothObjectsArrayWithNullElements_ReturnsTrue() {
        assertTrue(Equality.areEqual(new Object[]{null, null}, new Object[]{null, null}));
    }

    @Test
    public void testAreEqual_FirstObjectArrayAndSecondObjectArrayWithDifferentTypes_ReturnsFalse() {
        assertFalse(Equality.areEqual(new Object[]{1, 2, 3}, new Double[]{1.0, 2.0, 3.0}));
    }

    @Test
    public void testAreEqual_BothObjectsArrayWithSameLengthsAndDifferentTypes_ReturnsTrue() {
        assertFalse(Equality.areEqual(new Number[]{1, 2, 3}, new Double[]{1.0, 2.0, 3.0}));
    }
}