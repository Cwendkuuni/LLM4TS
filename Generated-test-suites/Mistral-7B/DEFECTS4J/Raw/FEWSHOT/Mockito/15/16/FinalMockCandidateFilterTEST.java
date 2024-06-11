package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mockitosis.MockitoTestSupport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest extends MockitoTestSupport {

    @Test
    public void testFilterCandidate_OneMock_Success() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(ClassUnderTest.class);
        mocks.add(mock);

        Field field = ClassUnderTest.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        Assert.assertFalse(field.isAccessible(null));
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(mock);
        Assert.assertTrue(field.isAccessible(null));
        Assert.assertEquals(mock, field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidate_OneMock_Failure() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(ClassUnderTest.class);
        mocks.add(mock);

        Field field = ClassUnderTest.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.thenInject();
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            Assert.assertEquals("Problems injecting dependency in fieldToInject", e.getMessage());
        }

        Assert.assertFalse(field.isAccessible(null));
        Assert.assertNull(field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatch_ReturnsEmptyInjecter() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(ClassUnderTest.class);
        Object mock2 = mock(ClassUnderTest.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = ClassUnderTest.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        Assert.assertFalse(field.isAccessible(null));
        Assert.assertNull(field.get(fieldInstance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFilterCandidate_MultipleMocks_NoMatch_ThrowsExceptionWhenTryingToInject() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(ClassUnderTest.class);
        Object mock2 = mock(ClassUnderTest.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = ClassUnderTest.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        fail("Expected UnsupportedOperationException to be thrown");
    }

    @Test
    public void testFilterCandidate_MultipleMocks_Match_InjectsUsingField() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(ClassUnderTest.class, "mock1");
        Object mock2 = mock(ClassUnderTest.class, "mock2");
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = ClassUnderTest.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        Assert.assertFalse(field.isAccessible(null));
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        Assert.assertEquals(mock1, fieldSetter.getRawValue());
        Assert.assertEquals(mock1, field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_Match_InjectsUsingProperty() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(ClassUnderTest.class, "mock1");
        Object mock2 = mock(ClassUnderTest.class, "mock2");
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = ClassUnderTest.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();
        field.setAccessible(true);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        Assert.assertTrue(field.isAccessible(null));
        Assert.assertEquals(mock1.getClass().getDeclaredField("propertyToInject").get(mock1), field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatch_ThrowsExceptionWhenInjectingUsingProperty() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(ClassUnderTest.class, "mock1");
        Object mock2 = mock(ClassUnderTest.class, "mock2");
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = ClassUnderTest.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();
        field.setAccessible(true);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.thenInject();
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            Assert.assertEquals("Problems injecting dependency in fieldToInject", e.getMessage());
        }

        Assert.assertTrue(field.isAccessible(null));
        Assert.assertNull(field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatch_ThrowsExceptionWhenInjectingUsingField() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(ClassUnderTest.class, "mock1");
        Object mock2 = mock(ClassUnderTest.class, "mock2");
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = ClassUnderTest.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.thenInject();
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        Assert.assertFalse(field.isAccessible(null));
        Assert.assertNull(field.get(fieldInstance));
    }
}