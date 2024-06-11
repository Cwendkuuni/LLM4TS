package org.mockito.internal.matchers;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.same;

import org.hamcrest.Description;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object obj = new Object();
        assertThat(obj, same(obj));
    }

    @Test
    public void testSameString() {
        String str = "foo";
        assertThat(str, same(str));
    }

    @Test
    public void testSameCharacter() {
        Character c = 'a';
        assertThat(c, same(c));
    }

    @Test
    public void testSameNull() {
        assertNull(same(null));
    }

    @Test
    public void testSamePrimitiveBoolean() {
        boolean bool = true;
        assertThat(bool, same(bool));
    }

    @Test
    public void testSamePrimitiveCharacter() {
        char c = 'a';
        assertThat(c, same(c));
    }

    @Test
    public void testSamePrimitiveByte() {
        byte b = 1;
        assertThat(b, same(b));
    }

    @Test
    public void testSamePrimitiveShort() {
        short s = 1;
        assertThat(s, same(s));
    }

    @Test
    public void testSamePrimitiveInt() {
        int i = 1;
        assertThat(i, same(i));
    }

    @Test
    public void testSamePrimitiveLong() {
        long l = 1L;
        assertThat(l, same(l));
    }

    @Test
    public void testSamePrimitiveFloat() {
        float f = 1.0f;
        assertThat(f, same(f));
    }

    @Test
    public void testSamePrimitiveDouble() {
        double d = 1.0;
        assertThat(d, same(d));
    }

    @Test
    public void testSameArrayOfObject() {
        Object[] arr = new Object[0];
        Object[] arrSame = new Object[0];
        assertThat(arr, same(arrSame));
    }

    @Test
    public void testSameArrayOfString() {
        String[] arr = new String[0];
        String[] arrSame = new String[0];
        assertThat(arr, same(arrSame));
    }

    @Test
    public void testSameArrayOfCharacter() {
        char[] arr = new char[0];
        char[] arrSame = new char[0];
        assertThat(arr, same(arrSame));
    }

    @Test
    public void testSameWrapperClassInteger() {
        Integer i = 1;
        assertThat(i, same(i));
    }

    @Test
    public void testSameWrapperClassLong() {
        Long l = 1L;
        assertThat(l, same(l));
    }

    @Test
    public void testSameWrapperClassFloat() {
        Float f = 1.0f;
        assertThat(f, same(f));
    }

    @Test
    public void testSameWrapperClassDouble() {
        Double d = 1.0;
        assertThat(d, same(d));
    }

    @Test
    public void testSameWrapperClassBoolean() {
        Boolean b = true;
        assertThat(b, same(b));
    }

    @Test
    public void testSameWrapperClassCharacter() {
        Character c = 'a';
        Character cSame = 'a';
        assertThat(c, same(cSame));
    }

    @Test
    public void testSameWrapperClassString() {
        String s = "foo";
        String sSame = "foo";
        assertThat(s, same(sSame));
    }
}