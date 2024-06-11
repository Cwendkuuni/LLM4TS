package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Test;

public class SettableAnyPropertyTest {
    @Test
    public void testDeserializeAndSet() {
        // Test case 1
        SettableAnyProperty anyProperty1 = new SettableAnyProperty(null, null, null, null, null, null);
        assertNull(anyProperty1.deserializeAndSet(null, null, null, ""));
        
        // Test case 2
        SettableAnyProperty anyProperty2 = new SettableAnyProperty(null, null, null, null, null, null);
        assertNull(anyProperty2.deserializeAndSet( null, null, null, null));
        
    }

    @Test
    public void testDeserialize() {
        // Test case 1
        SettableAnyProperty anyProperty1 = new SettableAnyProperty(null, null, null, null, null, null);
        assertNull(anyProperty1.deserialize(null, null));
        
        // Test case 2
        SettableAnyProperty anyProperty2 = new SettableAnyProperty(null, null, null, null, null, null);
        assertNull(anyProperty2.deserialize(null, null));
        
    }

    @Test
    public void testSet() {
        // Test case 1
        SettableAnyProperty anyProperty1 = new SettableAnyProperty(null, null, null, null, null, null);
        try{
            anyProperty1.set(null, "", null);
        } catch (Exception e) {
            fail();
        }
        
        // Test case 2
        SettableAnyProperty anyProperty2 = new SettableAnyProperty(null, null, null, null, null, null);
        try{
            anyProperty2.set(null, null, "");
        } catch (Exception e) {
            fail();
        }
        
    }

    @Test
    public void testThrowAsIOE() {
        // Test case 1
        SettableAnyProperty anyProperty1 = new SettableAnyProperty(null, null, null, null, null, null);
        try{
            anyProperty1._throwAsIOE(new IllegalArgumentException(), null, null);
        } catch (Exception e) {
            fail();
        }
        
        // Test case 2
        SettableAnyProperty anyProperty2 = new SettableAnyProperty(null, null, null, null, null, null);
        try{
            anyProperty2._throwAsIOE(new IllegalArgumentException(), "", "");
        } catch (Exception e) {
            fail();
        }
        
    }
}