package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType namedType1 = new NamedType(String.class);
        NamedType namedType2 = new NamedType(Integer.class);
        subtypeResolver.registerSubtypes(namedType1, namedType2);
        
        Collection<NamedType> registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
        
        assertNotNull(registeredSubtypes);
        assertEquals(2, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(namedType1));
        assertTrue(registeredSubtypes.contains(namedType2));
    }

    // Add more test cases for other methods as needed

}