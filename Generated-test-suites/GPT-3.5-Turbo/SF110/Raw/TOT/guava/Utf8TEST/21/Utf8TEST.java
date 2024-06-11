package com.google.common.base;

import org.junit.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_typicalUseCase() {
        CharSequence sequence = "Hello world";
        int expected = 11;
        int actual = Utf8.encodedLength(sequence);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testEncodedLength_edgeCase() {
        CharSequence sequence = "";
        int expected = 0;
        int actual = Utf8.encodedLength(sequence);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_errorScenario() {
        CharSequence sequence = "Hello world";
        sequence = sequence + "追加";
        Utf8.encodedLength(sequence);
    }
    
    @Test
    public void testIsWellFormed_typicalUseCase() {
        byte[] bytes = {(byte) 72, (byte) 101, (byte) 108, (byte) 108, (byte) 111, (byte) 32, (byte) 119, (byte) 111, (byte) 114, (byte) 108, (byte) 100};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIsWellFormed_edgeCase() {
        byte[] bytes = {};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIsWellFormed_errorScenario() {
        byte[] bytes = {(byte) 72, (byte) 101, (byte) 108, (byte) 108, (byte) -128, (byte) 32, (byte) 119, (byte) 111, (byte) 114, (byte) 108, (byte) 100};
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expected, actual);
    }

}