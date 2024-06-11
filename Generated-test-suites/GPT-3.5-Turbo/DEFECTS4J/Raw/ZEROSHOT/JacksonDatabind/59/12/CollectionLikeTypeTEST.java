package com.fasterxml.jackson.databind.type;

import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    public CollectionLikeTypeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isTrueCollectionType method, of class CollectionLikeType.
     */
    @Test
    public void testIsTrueCollectionType() {
        System.out.println("isTrueCollectionType");
        CollectionLikeType instance = new CollectionLikeType();
        boolean expResult = true;
        boolean result = instance.isTrueCollectionType();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class CollectionLikeType.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        CollectionLikeType instance = new CollectionLikeType();
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class CollectionLikeType.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        CollectionLikeType instance = new CollectionLikeType();
        String expResult = "[collection-like type; class com.fasterxml.jackson.databind.type.CollectionLikeType, contains com.fasterxml.jackson.databind.type.JavaType]";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
  
}