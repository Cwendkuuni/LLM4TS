package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterSuccess() {
        Collection<Object> mocks = Arrays.asList(createMock());
        Field field = Mockito.class.getDeclaredField("fieldToMock");
        Object fieldInstance = new Object();
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Stubber stubber = Mockito.stub(field.getType());

        injecter.thenInject();
        stubber.doReturn(mocks.iterator().next()).when(fieldInstance).getClass();
        stubber.doAnswer(i -> field.setAccessible(true)).when(fieldInstance).getClass();
        stubber.doAnswer(i -> new FieldSetter(fieldInstance, field).set((Object) i.getArgument(0), mocks.iterator().next())).when(fieldInstance).setField(any());

        Assert.assertEquals(mocks.iterator().next(), field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterFailure() {
        Collection<Object> mocks = Arrays.asList(createMock());
        Field field = Mockito.class.getDeclaredField("fieldToMock");
        Object fieldInstance = new Object();
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            Assert.fail();
        } catch (MockitoException e) {
            Assert.assertEquals("Problems injecting dependency in fieldToMock", e.getMessage());
        }
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldAccessSuccess() {
        Collection<Object> mocks = Arrays.asList(createMock());
        Field field = Mockito.class.getDeclaredField("fieldToMock");
        Object fieldInstance = new Object();
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        field.setAccessible(true);
        field.set(fieldInstance, mocks.iterator().next());
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertEquals(mocks.iterator().next(), field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldAccessFailure() {
        Collection<Object> mocks = Arrays.asList(createMock());
        Field field = Mockito.class.getDeclaredField("fieldToMock");
        Object fieldInstance = new Object();
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertNotNull(e);
        }

        Assert.assertFalse(field.isAccessible(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatch() {
        Collection<Object> mocks = Arrays.asList(createMock(), createMock());
        Field field = Mockito.class.getDeclaredField("fieldToMock");
        Object fieldInstance = new Object();
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertFalse(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FirstMatch() {
        Collection<Object> mocks = Arrays.asList(createMock1(), createMock2());
        Field field = Mockito.class.getDeclaredField("fieldToMock");
        Object fieldInstance = new Object();
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertEquals(mock1, field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_SecondMatch() {
        Collection<Object> mocks = Arrays.asList(createMock1(), createMock2());
        Field field = Mockito.class.getDeclaredField("fieldToMock");
        Object fieldInstance = new Object();
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Field field1 = Mockito.class.getDeclaredField("fieldToMock1");
        Object fieldInstance1 = new Object();
        OngoingInjecter injecter1 = filter.filterCandidate(Arrays.asList(mock1, mock2), field1, fieldInstance1);

        Assert.assertEquals(mock1, field.get(fieldInstance));
        Assert.assertTrue(injecter1.thenInject());
    }

    private Object createMock() {
        return Mockito.mock(String.class);
    }

    private Object createMock1() {
        return Mockito.mock(String.class, "mock1");
    }

    private Object createMock2() {
        return Mockito.mock(String.class, "mock2");
    }

    private InvocationOnMock mock1 = Mockito.mock(InvocationOnMock.class);
    private InvocationOnMock mock2 = Mockito.mock(InvocationOnMock.class);
}