package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.mockitoAPIMock;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldTestUtils;
import org.mockito.internal.util.reflection.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.FieldTestUtils.setFieldValue;
import static org.mockito.internal.util.reflection.ReflectUtils.getFieldValue;
import static org.mockito.internal.util.reflection.ReflectUtils.newInstance;

public class FinalMockCandidateFilterTest {
    private static final String FIELD_NAME = "testField";
    private static final Object FIELD_VALUE = new Object() {
    };

    private MockCandidateFilter filter;
    private Object mockObject;
    private Object testObject;
    private Class<? extends Object> mockitoAPIClass;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mockitoAPIClass = mockitoAPIMock.class;
        testObject = new Object() {
            private Field testField = ReflectUtils.findField(this.getClass(), FIELD_NAME);
        };
        setFieldValue(testObject, FIELD_NAME, FIELD_VALUE);
        mockObject = new MockitoTestObject();
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterSuccess() {
        Collection<Object> mocks = Arrays.asList(mockObject);
        Field field = ReflectUtils.findField(testObject.getClass(), FIELD_NAME);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, testObject);

        assertFalse(injecter.thenInject());
        FieldSetter fieldSetter = new FieldSetter(testObject, field);
        assertEquals(mockObject, fieldSetter.getActualValue());
        assertEquals(mockObject, getFieldValue(testObject, FIELD_NAME));
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterFailure() {
        Collection<Object> mocks = Arrays.asList(mockObject);
        Field field = ReflectUtils.findField(testObject.getClass(), FIELD_NAME);
        ReflectUtils.setAccessible(field);

        try {
            setFieldValue(mockObject, "errorField", new Object() {});
            OngoingInjecter injecter = filter.filterCandidate(mocks, field, testObject);
            assertFalse(injecter.thenInject());
            throw new AssertionError();
        } catch (MockitoException e) {
            assertEquals("Problems injecting dependency in testField", e.getMessage());
        }
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldAccessSuccess() {
        Collection<Object> mocks = Arrays.asList(new MockitoTestObject());
        Field field = ReflectUtils.findField(testObject.getClass(), FIELD_NAME);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, testObject);

        assertFalse(injecter.thenInject());
        Object fieldValue = ReflectUtils.getFieldValue(testObject, FIELD_NAME);
        assertEquals(mockitoAPIClass, fieldValue.getClass());
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldAccessFailure() {
        Collection<Object> mocks = Arrays.asList(new MockitoTestObject());
        Field field = ReflectUtils.findField(testObject.getClass(), FIELD_NAME);
        ReflectUtils.setAccessible(field);

        try {
            setFieldValue(mockObject, "errorField", new Object() {});
            OngoingInjecter injecter = filter.filterCandidate(mocks, field, testObject);
            assertFalse(injecter.thenInject());
            throw new AssertionError();
        } catch (MockitoException e) {
            assertEquals("Problems injecting dependency in testField", e.getMessage());
        }
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatch() {
        Collection<Object> mocks = Arrays.asList(new MockitoTestObject(), new MockitoTestObject2());
        Field field = ReflectUtils.findField(testObject.getClass(), FIELD_NAME);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, testObject);

        assertFalse(injecter.thenInject());
        assertNull(getFieldValue(testObject, FIELD_NAME));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterSuccess() {
        Collection<Object> mocks = Arrays.asList(mockObject, new MockitoTestObject());
        Field field = ReflectUtils.findField(testObject.getClass(), FIELD_NAME);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, testObject);

        assertFalse(injecter.thenInject());
        FieldSetter fieldSetter = new FieldSetter(testObject, field);
        assertEquals(mockObject, fieldSetter.getActualValue());
        assertEquals(mockObject, getFieldValue(testObject, FIELD_NAME));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterFailure() {
        Collection<Object> mocks = Arrays.asList(mockObject, new MockitoTestObject());
        Field field = ReflectUtils.findField(testObject.getClass(), FIELD_NAME);
        ReflectUtils.setAccessible(field);

        try {
            setFieldValue(mockObject, "errorField", new Object() {});
            OngoingInjecter injecter = filter.filterCandidate(mocks, field, testObject);
            assertFalse(injecter.thenInject());
            throw new AssertionError();
        } catch (MockitoException e) {
            assertEquals("Problems injecting dependency in testField", e.getMessage());
        }
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldAccessSuccess() {
        Collection<Object> mocks = Arrays.asList(new MockitoTestObject(), new MockitoTestObject2());
        Field field = ReflectUtils.findField(testObject.getClass(), FIELD_NAME);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, testObject);

        assertFalse(injecter.thenInject());
        Object fieldValue = ReflectUtils.getFieldValue(testObject, FIELD_NAME);
        assertNull(fieldValue);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldAccessFailure() {
        Collection<Object> mocks = Arrays.asList(new MockitoTestObject(), new MockitoTestObject2());
        Field field = ReflectUtils.findField(testObject.getClass(), FIELD_NAME);
        ReflectUtils.setAccessible(field);

        try {
            setFieldValue(mockObject, "errorField", new Object() {});
            OngoingInjecter injecter = filter.filterCandidate(mocks, field, testObject);
            assertFalse(injecter.thenInject());
            throw new AssertionError();
        } catch (MockitoException e) {
            assertEquals("Problems injecting dependency in testField", e.getMessage());
        }
    }

    @Test
    public void testFilterCandidate_EmptyMocks() {
        Collection<Object> mocks = Collections.emptyList();
        Field field = ReflectUtils.findField(testObject.getClass(), FIELD_NAME);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, testObject);

        assertFalse(injecter.thenInject());
        assertNull(getFieldValue(testObject, FIELD_NAME));
    }
}

class MockitoTestObject {
}

class MockitoTestObject2 {
}