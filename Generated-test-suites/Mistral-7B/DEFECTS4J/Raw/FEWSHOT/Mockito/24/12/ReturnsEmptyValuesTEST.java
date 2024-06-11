package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;

import org.junit.Test;
import org.mockito.internal.stubbing.DefaultAnswer;
import org.mockito.internal.stubbing.DefaultAnswer.ReturnsEmptyValues;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.mock.MockName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_PrimitiveTypes() {
        DefaultAnswer answer = new ReturnsEmptyValues();
        ObjectMethodsGuru methodsGuru = answer.methodsGuru;

        for (Class<?> type : new Class[] { byte.class, short.class, int.class, long.class, char.class, float.class, double.class, boolean.class }) {
            assertEquals(Primitives.wrap(Primitives.defaultValueForPrimitiveOrWrapper(type)), answer.returnValueFor(type));
        }
    }

    @Test
    public void testAnswer_CollectionTypes() {
        DefaultAnswer answer = new ReturnsEmptyValues();

        for (Class<?> type : new Class[] { Collection.class, Set.class, HashSet.class, SortedSet.class, TreeSet.class, LinkedHashSet.class, List.class, LinkedList.class, ArrayList.class }) {
            Collection collection = (Collection) answer.returnValueFor(type);
            assertNotNull(collection);
            assertEquals(0, collection.size());
        }
    }

    @Test
    public void testAnswer_OtherTypes() {
        DefaultAnswer answer = new ReturnsEmptyValues();

        for (Class<?> type : new Class[] { Map.class, HashMap.class, SortedMap.class, TreeMap.class, LinkedHashMap.class }) {
            Map map = (Map) answer.returnValueFor(type);
            assertNotNull(map);
            assertEquals(0, map.size());
        }

        //Check that null is returned for non-supported types
        assertNull(answer.returnValueFor(String.class));
        assertNull(answer.returnValueFor(Object.class));
    }
}