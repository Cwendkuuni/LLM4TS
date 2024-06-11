package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.mockfactory.MockFactory;
import org.mockito.internal.configuration.mockfactory.MockitoAnnotations;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.util.reflection.Reflections.getFieldValue;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;

public class FinalMockCandidateFilterTest {

    @Rule
    public MockitoJUnit MockitoJUnitRule = new MockitoJUnit();

    private static final String TESTED_CLASS_NAME = FinalMockCandidateFilter.class.getName();
    private static final String MOCK_FACTORY_CLASS_NAME = MockFactory.class.getName();
    private static final String ONGOING_INJECTER_CLASS_NAME = OngoingInjecter.class.getName();

    private Reporter reporter;
    private MockFactory mockFactory;

    @Before
    public void setUp() {
        reporter = new Reporter() {
        };
        mockFactory = MockitoJUnitRunner.getInstance(MockitoJUnitRule.getTestClass()).getMockFactory();
    }

    @Test
    public void testFilterCandidate_SingleMock_SuccessfulPropertySetterInjection() {
        // given
        final Object mock = mockFactory.createMock(String.class);
        final Field field = Whitebox.getInternalState(mock, "field");
        final Object fieldInstance = field.get(mock);

        // when
        final Object result = new FinalMockCandidateFilter().filterCandidate(Collections.singletonList(mock), field, fieldInstance)[0];

        // then
        assertEquals(mock, result);
        assertInstanceOf(OngoingInjecter.class, getFieldValue(result, "delegate"));
    }

    @Test
    public void testFilterCandidate_SingleMock_UnsuccessfulPropertySetterInjection() {
        // given
        final Object mock = mockFactory.createMock(String.class);
        final Field field = Whitebox.getInternalState(mock, "field");
        final Object fieldInstance = field.get(mock);
        final Field unaccessibleField = Whitebox.getInternalState(mock, "unaccessibleField");

        // when
        try {
            setFieldValue(unaccessibleField, mock, "unaccessibleValue");
        } catch (IllegalAccessException e) {
            // do nothing
        }

        final Object result = new FinalMockCandidateFilter().filterCandidate(Collections.singletonList(mock), field, fieldInstance)[0];

        // then
        assertSame(mock, getFieldValue(result, "delegate"));
        assertNull(getFieldValue(result, "propertySetterResult"));
        assertInstanceOf(OngoingInjecter.class, getFieldValue(result, "delegate"));
    }

    @Test
    public void testFilterCandidate_SingleMock_SuccessfulFieldAccessInjection() {
        // given
        final Object mock = mockFactory.createMock(String.class);
        final Field field = Whitebox.getInternalState(mock, "field");
        final Object fieldInstance = field.get(mock);

        // when
        final Object result = new FinalMockCandidateFilter().filterCandidate(Collections.singletonList(mock), field, fieldInstance)[0];

        // then
        assertEquals(mock, result);
        assertInstanceOf(OngoingInjecter.class, getFieldValue(result, "delegate"));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoInjection() {
        // given
        final Object mock1 = mockFactory.createMock(String.class);
        final Object mock2 = mockFactory.createMock(String.class);
        final Field field = Whitebox.getInternalState(mock1, "field");
        final Object fieldInstance = field.get(mock1);

        // when
        final Object[] result = new FinalMockCandidateFilter().filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);

        // then
        assertNull(result[0]);
        assertInstanceOf(OngoingInjecter.class, getFieldValue(result[1], "delegate"));
    }
}