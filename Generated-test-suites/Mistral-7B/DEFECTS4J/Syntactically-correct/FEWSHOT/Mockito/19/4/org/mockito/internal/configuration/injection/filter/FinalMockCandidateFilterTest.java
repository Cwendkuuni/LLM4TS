package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;
import static org.mockito.internal.util.reflection.ReflectionUtils.getDeclaredField;
import static org.mockito.internal.util.reflection.ReflectionUtils.setAccessible;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private Reporter reporter;

    @Before
    public void setUp() {
        mocks = new ArrayList<>();
        field = getDeclaredField(Object.class, "fieldName");
        setAccessible(field);
        fieldInstance = mock(Object.class);
        reporter = mock(Reporter.class);
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Stubber stubber = stub(mock);
        InvocationOnMock invocation = stubber.when(mock).thenReturn(mock);
        stub(field, "value");
        Object result = injecter.thenInject();

        assertEquals(mock, result);
        assertEquals("value", getFieldValue(field, mock));
        assertEquals(mock, getFieldValue(field, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter.thenInject());
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithFailedPropertySetter() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Stubber stubber = stub(mock);
        stub(field, "value").thenThrow(new RuntimeException());

        injecter.thenInject();
    }

    @Test
    public void testFilterCandidateWithFailedFieldSetter() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Stubber stubber = stub(mock);
        stub(field, "value").thenThrow(new RuntimeException());

        try {
            injecter.thenInject();
        } catch (RuntimeException e) {
            new Reporter().cannotInjectDependency(field, mock, e);
        }

        assertNull(getFieldValue(field, fieldInstance));
    }

    private Object getFieldValue(Field field, Object object) {
        try {
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}