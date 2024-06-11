package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.reflection.BeanPropertySetter;
import org.mockito.internal.reflection.FieldSetter;
import org.mockito.internal.util.collection.ArrayUtil;
import org.mockito.internal.util.reflection.FieldInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.ReflectionUtil.getFieldValue;
import static org.mockito.internal.util.reflection.ReflectionUtil.setFieldValue;

public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;
    private final Reporter reporter = new Reporter();

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        mock1 = new Object() {
            @SuppressWarnings("unused")
            private static final Field field1 = new FieldInfo("field1", Integer.TYPE);
        };
        mock2 = new Object() {
            @SuppressWarnings("unused")
            private static final Field field1 = new FieldInfo("field1", String.class);
        };
        mocks.add(mock1);
        mocks.add(mock2);
        field = new FieldInfo("field", Integer.TYPE);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, actual);
        OngoingInjecter expected = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                try {
                    new BeanPropertySetter(fieldInstance, field).set(mock1);
                } catch (RuntimeException e) {
                    reporter.cannotInjectDependency(field, mock1, e);
                }
                return mock1;
            }
        };
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterCandidateWithTwoMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, actual);
        OngoingInjecter expected = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return null;
            }
        };
        assertEquals(expected, actual);
    }

    @Test
    public void testInjectCandidateWithOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNotNull(result);
        assertEquals(mock1, result);

        assertFieldSet(field1, mock1, fieldInstance);
    }

    @Test(expected = RuntimeException.class)
    public void testInjectCandidateWithTwoMocksFails() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();
    }

    @Test
    public void testInjectCandidateWithTwoMocksReturnsNull() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter.thenInject());
    }

    @Test
    public void testInjectCandidateWithPropertySetter() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                Object oldValue = getFieldValue(fieldInstance, field1);
                try {
                    new BeanPropertySetter(fieldInstance, field).set(mock1);
                } catch (RuntimeException e) {
                    reporter.cannotInjectDependency(field, mock1, e);
                }
                setFieldValue(fieldInstance, field1, mock1);
                return mock1;
            }
        };

        Object result = injecter.thenInject();

        assertNotNull(result);
        assertEquals(mock1, result);

        assertFieldSet(field1, mock1, fieldInstance);
    }

    @Test
    public void testInjectCandidateWithFieldAccess() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                Object oldValue = getFieldValue(fieldInstance, field1);
                try {
                    new FieldSetter(fieldInstance, field).set(mock1);
                } catch (RuntimeException e) {
                    reporter.cannotInjectDependency(field, mock1, e);
                }
                setFieldValue(fieldInstance, field1, mock1);
                return mock1;
            }
        };

        Object result = injecter.thenInject();

        assertNotNull(result);
        assertEquals(mock1, result);

        assertFieldSet(field1, mock1, fieldInstance);
    }

    private void assertFieldSet(FieldInfo fieldInfo, Object expectedValue, Object fieldInstance) {
        Object actualValue = getFieldValue(fieldInstance, fieldInfo);
        assertEquals(expectedValue, actualValue);
    }
}