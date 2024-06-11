
package org.mockito.internal.configuration.injection;

import org.junit.*;
import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldLoader;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mockitoSession;

import java.lang.reflect.Field;
import java.util.*;

public class FinalMockCandidateFilterTest {

    @BeforeClass
    public static void init() {
        MockitoSession session = new MockitoSession();
    }

    @Test
    public void testFilterCandidate_SingleMock_Success() {
        Object mock = session.spy(MockitoTestClass.class);
        Field field = FieldLoader.getPrivateField(MockitoTestClass.class, "fieldName");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(Arrays.asList(mock), field, fieldInstance);

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(mock);

        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_Failure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(new MockitoTestClass1(), new MockitoTestClass2());
        Field field = FieldLoader.getPrivateField(MockitoTestClass.class, "fieldName");
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_ExceptionThrownDuringFieldSetting() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(new MockitoTestClass());
        Field field = FieldLoader.getPrivateField(MockitoTestClass.class, "fieldName");
        Object fieldInstance = new Object();

        when(field.getType()).thenReturn(String.class); // Mock the field type for the exception

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set("ExceptionMessage"); // Set a string to the field, which is not the expected type

        injecter.thenInject();
    }
}

class MockitoTestClass {
    private Object fieldName;
}

class FieldLoader {
    public static Field getPrivateField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

class MockitoTestClass1 {
}

class MockitoTestClass2 {
}

class FieldSetter {
    private Object target;
    private Field field;

    public FieldSetter(Object target, Field field) {
        this.target = target;
        this.field = field;
    }

    public void set(Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
