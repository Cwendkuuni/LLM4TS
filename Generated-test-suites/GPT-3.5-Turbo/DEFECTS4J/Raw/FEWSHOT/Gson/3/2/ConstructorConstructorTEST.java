package com.google.gson.internal;

import static org.junit.Assert.*;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class ConstructorConstructorTest {
    @Test
    public void testGet() {
        // Test case 1: Test when typeCreator is not null
        ConstructorConstructor cc = new ConstructorConstructor(null);
        ObjectConstructor<String> oc = cc.get(TypeToken.get(String.class));
        assertNotNull(oc);

        // Test case 2: Test when rawTypeCreator is not null
        cc = new ConstructorConstructor(null);
        oc = cc.get(TypeToken.get(ConstructorConstructor.class));
        assertNotNull(oc);

        // Test case 3: Test when defaultConstructor is not null
        cc = new ConstructorConstructor(null);
        oc = cc.get(TypeToken.get(Integer.class));
        assertNotNull(oc);

        // Test case 4: Test when defaultImplementation is not null
        cc = new ConstructorConstructor(null);
        oc = cc.get(TypeToken.get(ArrayList.class));
        assertNotNull(oc);

        // Test case 5: Test when all conditions are null
        cc = new ConstructorConstructor(null);
        oc = cc.get(TypeToken.get(LinkedList.class));
        assertNotNull(oc);

        // Test case 6: Test when unsafeAllocator is not null
        cc = new ConstructorConstructor(null);
        oc = cc.get(TypeToken.get(Set.class));
        assertNotNull(oc);
    }

    @Test
    public void testNewDefaultConstructor() {
        // Test case 1: Test when constructor is accessible
        ConstructorConstructor cc = new ConstructorConstructor(null);
        ObjectConstructor<Integer> oc = cc.newDefaultConstructor(Integer.class);
        assertNotNull(oc);

        // Test case 2: Test when constructor is not accessible
        cc = new ConstructorConstructor(null);
        oc = cc.newDefaultConstructor(ConstructorConstructor.class);
        assertNotNull(oc);
    }

    @Test
    public void testNewDefaultImplementationConstructor() {
        // Test case 1: Test when type is null
        ConstructorConstructor cc = new ConstructorConstructor(null);
        ObjectConstructor<Object> oc = cc.newDefaultImplementationConstructor(null, ArrayList.class);
        assertNotNull(oc);

        // Test case 2: Test when rawType implements Collection
        cc = new ConstructorConstructor(null);
        oc = cc.newDefaultImplementationConstructor(ArrayList.class, LinkedList.class);
        assertNotNull(oc);

        // Test case 3: Test when rawType is EnumSet
        cc = new ConstructorConstructor(null);
        oc = cc.newDefaultImplementationConstructor(ArrayList.class, EnumSet.class);
        assertNotNull(oc);

        // Test case 4: Test when rawType is Set
        cc = new ConstructorConstructor(null);
        oc = cc.newDefaultImplementationConstructor(ArrayList.class, Set.class);
        assertNotNull(oc);

        // Test case 5: Test when rawType is Queue
        cc = new ConstructorConstructor(null);
        oc = cc.newDefaultImplementationConstructor(ArrayList.class, Queue.class);
        assertNotNull(oc);

        // Test case 6: Test when rawType is neither Collection nor EnumSet or Set or Queue
        cc = new ConstructorConstructor(null);
        oc = cc.newDefaultImplementationConstructor(ArrayList.class, Object.class);
        assertNotNull(oc);

        // Test case 7: Test when rawType is Map and type is null
        cc = new ConstructorConstructor(null);
        oc = cc.newDefaultImplementationConstructor(null, Map.class);
        assertNotNull(oc);

        // Test case 8: Test when rawType is Map and type is ParameterizedType
        cc = new ConstructorConstructor(null);
        oc = cc.newDefaultImplementationConstructor(ArrayList.class, Map.class);
        assertNotNull(oc);

        // Test case 9: Test when rawType is Map and type is ParameterizedType but type argument is not String
        cc = new ConstructorConstructor(null);
        oc = cc.newDefaultImplementationConstructor(Integer.class, Map.class);
        assertNotNull(oc);

        // Test case 10: Test when rawType is Map and type is ParameterizedType and type argument is String
        cc = new ConstructorConstructor(null);
        oc = cc.newDefaultImplementationConstructor(String.class, Map.class);
        assertNotNull(oc);
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor cc = new ConstructorConstructor(null);
        ObjectConstructor<String> oc = cc.newUnsafeAllocator(String.class, String.class);
        assertNotNull(oc);
    }
}