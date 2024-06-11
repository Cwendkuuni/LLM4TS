package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.exceptions.MockitoRuntimeException.cannotInjectDependency;
import static org.mockito.internal.util.reflection.FieldSetter.setField;
import static org.mockito.internal.util.reflection.ReflectionUtils.setFieldValue;
import static org.mockito.internal.util.reflection.ReflectionUtils.setPropertyValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.Injectable;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.ReflectionUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakers;
import org.mockito.plugins.MockitoCore;

public class FinalMockCandidateFilterTest {
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;
    private Object mock;
    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() throws Exception {
        MockitoCore mockitoCore = MockMakers.getDefaultMockMaker().getMockitoCore();
        MockitoConfiguration mockitoConfiguration = mockitoCore.getConfiguration();
        Reporter reporter = mockitoCore.getReporter();
        MockMaker mockMaker = MockMakers.getDefaultMockMaker();

        field = ReflectionUtils.findField(Injectable.class, "field");
        fieldInstance = new Object() {
            Injectable injectable = new Injectable() {
                private Object value;
            };

            public Object getFieldValue() {
                return injectable.value;
            }
        };

        mock = mockMaker.createMock(Injectable.class);
        setPropertyValue(mock, "field", new Object());
        mocks = new ArrayList<>();
        mocks.add(mock);

        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        assertNotNull(ongoingInjecter.thenInject());
        assertEquals(mock, ongoingInjecter.thenInject());
    }

    @Test(expected = cannotInjectDependency.class)
    public void testFilterCandidateWithOneMockButCantInject() throws Exception {
        setFieldValue(fieldInstance, field, this);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        ongoingInjecter.thenInject();
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Object anotherMock = mockMaker.createMock(Injectable.class);
        mocks.add(anotherMock);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        assertNull(ongoingInjecter.thenInject());
    }

    @Test
    public void testPropertySetterInjectsCorrectly() throws Exception {
        Object expectedValue = new Object();
        BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
        beanPropertySetter.set(mock, expectedValue);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object actualValue = (Object) ongoingInjecter.thenInject();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testFieldSetterInjectsCorrectly() throws Exception {
        Object expectedValue = new Object();
        setField(fieldInstance, field, expectedValue);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object actualValue = (Object) ongoingInjecter.thenInject();

        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = cannotInjectDependency.class)
    public void testFilterCandidateWithNullMocks() {
        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullField() {
        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        filter.filterCandidate(mocks, field, null);
    }
}