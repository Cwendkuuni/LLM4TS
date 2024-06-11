package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.stubbing.answers.ReturnsEmptyValues.Primitives;
import static org.mockito.mockitoSession.internal.MockitoSessionValues.getObjectMethodsGuru;
import static org.mockito.mockitoSession.internal.MockitoSessionValues.getMockUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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

    private ObjectMethodsGuru methodsGuru;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        methodsGuru = getObjectMethodsGuru();
        mockUtil = getMockUtil();
    }

    @Test
    public void testAnswer_toString() {
        InvocationOnMock invocation = mockUtil.createMockInvocation("toString");

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object answer = returnsEmptyValues.answer(invocation);

        MockName mockName = mockUtil.getMockName(invocation.getMock());
        if (mockName.isDefault()) {
            assertEquals("Mock for unknownType, hashCode: 592560448", answer);
        } else {
            assertEquals(mockName.toString(), answer);
        }
    }

    @Test
    public void testAnswer_compareTo() {
        InvocationOnMock invocation = mockUtil.createMockInvocation("compareTo", Integer.class, 1);

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object answer = returnsEmptyValues.answer(invocation);

        assertEquals(1, answer);
    }

    @Test
    public void testAnswer_primitiveTypes() {
        InvocationOnMock invocation = mockUtil.createMockInvocation("booleanValue");

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object answer = returnsEmptyValues.answer(invocation);

        assertTrue(answer instanceof Boolean);
        assertEquals(false, answer);

        invocation = mockUtil.createMockInvocation("byteValue");
        answer = returnsEmptyValues.answer(invocation);
        assertEquals((byte) 0, answer);

        invocation = mockUtil.createMockInvocation("charValue");
        answer = returnsEmptyValues.answer(invocation);
        assertEquals((char) 0, answer);

        invocation = mockUtil.createMockInvocation("shortValue");
        answer = returnsEmptyValues.answer(invocation);
        assertEquals((short) 0, answer);

        invocation = mockUtil.createMockInvocation("intValue");
        answer = returnsEmptyValues.answer(invocation);
        assertEquals(0, answer);

        invocation = mockUtil.createMockInvocation("longValue");
        answer = returnsEmptyValues.answer(invocation);
        assertEquals(0L, answer);

        invocation = mockUtil.createMockInvocation("floatValue");
        answer = returnsEmptyValues.answer(invocation);
        assertEquals(0.0f, answer, 0.0001f);

        invocation = mockUtil.createMockInvocation("doubleValue");
        answer = returnsEmptyValues.answer(invocation);
        assertEquals(0.0, answer, 0.0001);

        invocation = mockUtil.createMockInvocation("byte[]Value");
        answer = returnsEmptyValues.answer(invocation);
        assertArrayEquals(new byte[0], (byte[]) answer);
    }

    @Test
    public void testAnswer_emptyCollection() {
        testAnswer_emptyCollection("Collection");
        testAnswer_emptyCollection("Set");
        testAnswer_emptyCollection("HashSet");
        testAnswer_emptyCollection("SortedSet");
        testAnswer_emptyCollection("TreeSet");
        testAnswer_emptyCollection("LinkedHashSet");
        testAnswer_emptyCollection("List");
        testAnswer_emptyCollection("LinkedList");
        testAnswer_emptyCollection("ArrayList");
        testAnswer_emptyCollection("Map");
        testAnswer_emptyCollection("HashMap");
        testAnswer_emptyCollection("SortedMap");
        testAnswer_emptyCollection("TreeMap");
        testAnswer_emptyCollection("LinkedHashMap");
    }

    private void testAnswer_emptyCollection(String collectionType) {
        InvocationOnMock invocation = mockUtil.createMockInvocation("size", collectionType);

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object answer = returnsEmptyValues.answer(invocation);

        assertEquals(0, answer);

        invocation = mockUtil.createMockInvocation("isEmpty", collectionType);
        answer = returnsEmptyValues.answer(invocation);

        assertTrue(answer instanceof Boolean);
        assertEquals(true, answer);

        invocation = mockUtil.createMockInvocation("add", collectionType, String.class, "test");
        answer = returnsEmptyValues.answer(invocation);

        assertNull(answer);
    }
}