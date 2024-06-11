package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.Reflections.getFieldValue;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.MockitoConfigurationBuilder;
import org.mockito.internal.configuration.MockitoConfigurationType;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mockitousage.InjectMocks;

@SuppressWarnings("unchecked")
public class FinalMockCandidateFilterTest {

    private static class TestClass {
        @InjectMocks
        private static class Target {
            private String field;
        }
    }

    @Test
    public void testFilterCandidate_singleMock_success() {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new Target());
        Field field = TestClass.Target.class.getDeclaredField("field");
        Object fieldInstance = new TestClass().new Target();

        // when
        OngoingInjecter filter = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(filter);
        assertFalse(filter.thenInject()); // should not be invoked in this test
    }

    @Test
    public void testFilterCandidate_singleMock_failure() {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new Target());
        Field field = TestClass.Target.class.getDeclaredField("field");
        Object fieldInstance = new TestClass().new Target();

        // when
        assertThrows(MockitoException.class, () -> new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance));

        // then
        // not reached
    }

    @Test
    public void testFilterCandidate_multipleMocks_success() {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new Target(), new TestClass().new Target());
        Field field = TestClass.Target.class.getDeclaredField("field");
        Object fieldInstance = new TestClass().new Target();

        // when
        OngoingInjecter filter = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(filter);
        assertFalse(filter.thenInject()); // should not be invoked in this test
    }

    @Test
    public void testFilterCandidate_multipleMocks_success_with_property_setter() throws Exception {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new Target(), new TestClass().new Target());
        Field field = TestClass.Target.class.getDeclaredField("field");
        Object fieldInstance = new TestClass().new Target();

        // when
        Object mock = new TestClass().new Target();
        setFieldValue(mock, field, "value");
        new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance).thenInject();

        // then
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        assertNotNull(fieldSetter.getInjectedValue(mock));
        assertEquals("value", fieldSetter.getInjectedValue(mock));
    }

    @Test
    public void testFilterCandidate_multipleMocks_failure() {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new Target(), new TestClass().new Target());
        Field field = TestClass.Target.class.getDeclaredField("field");
        Object fieldInstance = new TestClass().new Target();

        // when & then
        assertThrows(MockitoException.class, () -> new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_nullMocks() {
        // given
        Field field = TestClass.Target.class.getDeclaredField("field");
        Object fieldInstance = new TestClass().new Target();

        // when
        assertThrows(NullPointerException.class, () -> new FinalMockCandidateFilter().filterCandidate(null, field, fieldInstance));

        // then
        // not reached
    }

    @Test
    public void testFilterCandidate_nullField() {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new Target(), new TestClass().new Target());
        Object fieldInstance = new TestClass().new Target();

        // when
        assertThrows(NullPointerException.class, () -> new FinalMockCandidateFilter().filterCandidate(mocks, null, fieldInstance));

        // then
        // not reached
    }

    @Test
    public void testFilterCandidate_nullFieldInstance() {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new Target(), new TestClass().new Target());
        Field field = TestClass.Target.class.getDeclaredField("field");

        // when
        assertThrows(NullPointerException.class, () -> new FinalMockCandidateFilter().filterCandidate(mocks, field, null));

        // then
        // not reached
    }

    @Test
    public void testFilterCandidate_emptyMockList() {
        // given
        Field field = TestClass.Target.class.getDeclaredField("field");
        Object fieldInstance = new TestClass().new Target();

        // when
        OngoingInjecter filter = new FinalMockCandidateFilter().filterCandidate(Collections.emptyList(), field, fieldInstance);

        // then
        assertNotNull(filter);
        assertFalse(filter.thenInject()); // should not be invoked in this test
    }

    @Test
    public void testFilterCandidate_noMatchingMock() {
        // given
        List<Object> mocks = Arrays.asList(new Object(), new Object());
        Field field = TestClass.Target.class.getDeclaredField("field");
        Object fieldInstance = new TestClass().new Target();

        // when
        assertThrows(MockitoException.class, () -> new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance));

        // then
        // not reached
    }

    @Test
    public void testFilterCandidate_multipleMocksWithSameType() {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new Target(), new TestClass().new Target());
        Field field = TestClass.Target.class.getDeclaredField("field");
        Object fieldInstance = new TestClass().new Target();

        // when
        assertThrows(MockitoException.class, () -> new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance));

        // then
        // not reached
    }

    @Test
    public void testFilterCandidate_noField() {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new Target(), new TestClass().new Target());
        Object fieldInstance = new TestClass().new Target();

        // when
        assertThrows(NoSuchFieldException.class, () -> new FinalMockCandidateFilter().filterCandidate(mocks, null, fieldInstance));

        // then
        // not reached
    }

    @Test
    public void testFilterCandidate_incompatibleFieldType() {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new Target(), new TestClass().new Target());
        Field field = TestClass.Target.class.getDeclaredField("notAStringField");
        Object fieldInstance = new TestClass().new Target();

        // when
        assertThrows(MockitoException.class, () -> new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance));

        // then
        // not reached
    }

    @Test
    public void testFilterCandidate_incompatibleFieldType_withPropertySetter() throws Exception {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new Target(), new TestClass().new Target());
        Field field = TestClass.Target.class.getDeclaredField("notAStringField");
        Object fieldInstance = new TestClass().new Target();

        // when & then
        assertThrows(MockitoException.class, () -> new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_injectionWithAnnotation() {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new TargetWithAnnotation(), new TestClass().new TargetWithAnnotation());
        Field field = TestClass.TargetWithAnnotation.class.getDeclaredField("field");
        Object fieldInstance = new TestClass().new TargetWithAnnotation();

        // when
        OngoingInjecter filter = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(filter);
        assertTrue(filter instanceof InjectingOngoingInjecter);
    }

    @Test
    public void testFilterCandidate_injectionWithAnnotation_noMatchingMock() {
        // given
        List<Object> mocks = Arrays.asList(new TestClass().new TargetWithAnnotation(), new TestClass().new Target());
        Field field = TestClass.TargetWithAnnotation.class.getDeclaredField("field");
        Object fieldInstance = new TestClass().new TargetWithAnnotation();

        // when
        assertThrows(MockitoException.class, () -> new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance));

        // then
        // not reached
    }

    private static class Target {
    }

    private static class TargetWithAnnotation {
        @Mock
        private String field;
    }

    private static class InjectingOngoingInjecter extends OngoingInjecter {
        // empty implementation
    }
}