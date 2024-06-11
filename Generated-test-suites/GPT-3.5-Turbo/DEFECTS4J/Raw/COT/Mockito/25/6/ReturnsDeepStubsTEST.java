
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = Mockito.mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Set up mock objects and invocation
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(new Object());
        Mockito.when(delegate.returnValueFor(Object.class)).thenReturn(new Object());

        // Test answer method
        Object result = returnsDeepStubs.answer(invocation);

        // Verify that delegate.returnValueFor is called
        Mockito.verify(delegate).returnValueFor(Object.class);

        // Verify that the correct result is returned
        assertEquals(new Object(), result);
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsEmptyValues delegate = Mockito.mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Set up mock objects and invocation
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object mock = Mockito.mock(Object.class);
        Mockito.when(invocation.getMock()).thenReturn(mock);

        // Test getMock method
        Object result = returnsDeepStubs.getMock(invocation);

        // Verify that MockUtil.getMockHandler is called
        Mockito.verify(new MockUtil()).getMockHandler(mock);

        // Verify that InvocationContainerImpl.getStubbedInvocations is called
        Mockito.verify(new MockUtil().getMockHandler(mock).getInvocationContainer()).getStubbedInvocations();

        // Verify that the correct result is returned
        assertNull(result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsEmptyValues delegate = Mockito.mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Set up mock objects and invocation
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
        Mockito.when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        Mockito.when(handler.getInvocationContainer()).thenReturn(container);
        Object mock = Mockito.mock(Object.class);
        Mockito.when(Mockito.mock(Object.class, returnsDeepStubs)).thenReturn(mock);

        // Test recordDeepStubMock method
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Verify that Mockito.mock is called
        Mockito.verify(Mockito.class).mock(Object.class, returnsDeepStubs);

        // Verify that InvocationContainerImpl.addAnswer is called
        Mockito.verify(container).addAnswer(Mockito.any(), Mockito.eq(false));

        // Verify that the correct result is returned
        assertEquals(mock, result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsEmptyValues delegate = Mockito.mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Set up mock object
        Object mock = Mockito.mock(Object.class);
        CreationSettings mockSettings = Mockito.mock(CreationSettings.class);
        Mockito.when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        Mockito.when(mockSettings.getTypeToMock()).thenReturn(Object.class);

        // Test actualParameterizedType method
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Verify that GenericMetadataSupport.inferFrom is called
        Mockito.verify(GenericMetadataSupport.class).inferFrom(Object.class);

        // Verify that the correct result is returned
        assertNotNull(result);
    }
}
