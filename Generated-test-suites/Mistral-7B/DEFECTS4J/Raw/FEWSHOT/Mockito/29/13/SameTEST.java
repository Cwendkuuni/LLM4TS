package org.mockito.internal.matchers;

import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Same.describeTo;

import org.junit.Test;

public class SameTest {

    @Test
    public void testSameString() {
        Same sameString = new Same("hello");
        describeTo(System.out, sameString);
        assertTrue(sameString.matches("hello"));
    }

    @Test
    public void testSameCharacter() {
        Same sameCharacter = new Same('a');
        describeTo(System.out, sameCharacter);
        assertTrue(sameCharacter.matches((char) 97));
    }

    @Test
    public void testSameObject() {
        Same sameObject = new Same(new Object());
        describeTo(System.out, sameObject);
        assertTrue(sameObject.matches(new Object()));
    }

    @Test
    public void testSameNull() {
        Same sameNull = new Same(null);
        describeTo(System.out, sameNull);
        assertNull(sameNull.matches(new Object()));
    }

    @Test
    public void testSamePrimitiveBoolean() {
        Same sameBoolean = new Same(Boolean.TRUE);
        describeTo(System.out, sameBoolean);
        assertTrue(sameBoolean.matches(Boolean.TRUE));
    }

    @Test
    public void testSamePrimitiveByte() {
        Same sameByte = new Same((byte) 1);
        describeTo(System.out, sameByte);
        assertTrue(sameByte.matches((byte) 1));
    }

    @Test
    public void testSamePrimitiveShort() {
        Same sameShort = new Same((short) 1);
        describeTo(System.out, sameShort);
        assertTrue(sameShort.matches((short) 1));
    }

    @Test
    public void testSamePrimitiveInt() {
        Same sameInt = new Same(1);
        describeTo(System.out, sameInt);
        assertTrue(sameInt.matches(1));
    }

    @Test
    public void testSamePrimitiveLong() {
        Same sameLong = new Same(1L);
        describeTo(System.out, sameLong);
        assertTrue(sameLong.matches(1L));
    }

    @Test
    public void testSamePrimitiveChar() {
        Same sameChar = new Same((char) 1);
        describeTo(System.out, sameChar);
        assertTrue(sameChar.matches((char) 1));
    }

    @Test
    public void testSamePrimitiveFloat() {
        Same sameFloat = new Same(1.0f);
        describeTo(System.out, sameFloat);
        assertTrue(sameFloat.matches(1.0f));
    }

    @Test
    public void testSamePrimitiveDouble() {
        Same sameDouble = new Same(1.0);
        describeTo(System.out, sameDouble);
        assertTrue(sameDouble.matches(1.0));
    }
}