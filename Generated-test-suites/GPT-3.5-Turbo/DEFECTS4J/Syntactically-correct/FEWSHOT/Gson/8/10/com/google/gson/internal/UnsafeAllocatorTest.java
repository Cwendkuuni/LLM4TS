package com.google.gson.internal;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator ua = UnsafeAllocator.create();
        assertNotNull(ua.newInstance(Object.class));
        assertNotNull(ua.newInstance(String.class));
        assertNotNull(ua.newInstance(Integer.class));
    }

}