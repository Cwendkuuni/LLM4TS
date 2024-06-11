package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.SimpleField;
import org.mockito.internal.util.reflection.SimpleType;
import org.mockito.internal.util.reflection.TypeUtil;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockitoCore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    @Rule
    public MockitoCore mockitoCore = new MockitoCore();

    private Reporter reporter;
    private BeanPropertySetter beanPropertySetter;
    private FieldSetter fieldSetter;
    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        reporter = mock(Reporter.class);
        beanPropertySetter = new BeanPropertySetter();
        fieldSetter = new FieldSetter();
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        // given
        Object fieldInstance = mock(SimpleType.class);
        Field field = new SimpleField("testField", new SimpleType("TestType"), new int[0]);
        Collection<Object> mocks = Arrays.asList(mock(SimpleType.class));

        // when
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertNotNull(result);
        verify(reporter).cannotInjectDependency(field, result, new IllegalArgumentException("Cannot assign a value to final field: testField in TestType\n" +
                "Consider using a static factory method to produce instead of this instance."));
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // given
        Object fieldInstance = spy(new Object() {
            private final Field testField = new Field("testField", new SimpleType("TestType"), new int[0]);
        });
        Field field = new SimpleField("testField", new SimpleType("TestType"), new int[0]);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(SimpleType.class));
        mocks.add(mock(SimpleType.class));

        // when
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(injecter);
        assertNull(injecter.thenInject());
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithException() {
        // given
        Object fieldInstance = mock(SimpleType.class);
        Field field = new SimpleField("testField", new SimpleType("TestType"), new int[0]);
        Collection<Object> mocks = Arrays.asList(mock(SimpleType.class), mock(SimpleType.class));

        // when
        filter.filterCandidate(mocks, field, fieldInstance);

        // then
        throw new RuntimeException("Expected exception");
    }

    @Test
    public void testSetValueUsingBeanPropertySetter() {
        // given
        Object fieldInstance = mock(SimpleType.class);
        Object testMock = mock(SimpleType.class);
        Field field = new SimpleField("testField", new SimpleType("TestType"), new int[0]);
        MockCreationSettings creationSettings = new MockCreationSettings.Builder().build();

        // when
        filter.filterCandidate(Arrays.asList(testMock), field, fieldInstance).thenInject();

        // then
        verify(beanPropertySetter).set(fieldInstance, field, testMock);
    }

    @Test
    public void testSetValueUsingFieldSetter() {
        // given
        Object fieldInstance = mock(SimpleType.class);
        Object testMock = mock(SimpleType.class);
        Field field = new SimpleField("testField", new SimpleType("TestType"), new int[0]);
        MockCreationSettings creationSettings = new MockCreationSettings.Builder().build();

        // when
        filter.filterCandidate(Arrays.asList(testMock), field, fieldInstance).thenInject();

        // then
        verify(fieldSetter).set(fieldInstance, field, testMock);
    }
}