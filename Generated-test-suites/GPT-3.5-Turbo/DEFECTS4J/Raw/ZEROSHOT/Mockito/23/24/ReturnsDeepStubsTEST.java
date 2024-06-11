import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test case 1
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        when(invocation1.getMock()).thenReturn(null);
        Object result1 = returnsDeepStubs.answer(invocation1);
        assertNull(result1);

        // Test case 2
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        ReturnsEmptyValues delegate = new ReturnsEmptyValues();
        returnsDeepStubs.delegate = delegate;
        when(invocation2.getMock()).thenReturn(null);
        Object result2 = returnsDeepStubs.answer(invocation2);
        assertNull(result2);

        // Test case 3
        InvocationOnMock invocation3 = mock(InvocationOnMock.class);
        GenericMetadataSupport genericMetadataSupport3 = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport3.rawType()).thenReturn(Object.class);
        MockitoCore mockitoCore3 = mock(MockitoCore.class);
        when(mockitoCore3.isTypeMockable(Object.class)).thenReturn(true);
        returnsDeepStubs.mockitoCore = mockitoCore3;
        when(invocation3.getMock()).thenReturn(null);
        when(invocation3.getMethod()).thenReturn(null);
        when(invocation3.getMock()).thenReturn(genericMetadataSupport3);
        Object result3 = returnsDeepStubs.answer(invocation3);
        assertNull(result3);

        // Test case 4
        InvocationOnMock invocation4 = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler4 = mock(InternalMockHandler.class);
        MockUtil mockUtil4 = mock(MockUtil.class);
        when(mockUtil4.getMockHandler(null)).thenReturn(handler4);
        returnsDeepStubs.delegate = delegate;
        returnsDeepStubs.mockitoCore = mockitoCore3;
        when(invocation4.getMock()).thenReturn(null);
        InvocationContainerImpl container4 = mock(InvocationContainerImpl.class);
        when(handler4.getInvocationContainer()).thenReturn(container4);
        when(container4.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);
        when(container4.getInvocationForStubbing()).thenReturn(null);
        Object result4 = returnsDeepStubs.answer(invocation4);
        assertNull(result4);

        // Test case 5
        InvocationOnMock invocation5 = mock(InvocationOnMock.class);
        GenericMetadataSupport genericMetadataSupport5 = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport5.rawType()).thenReturn(Object.class);
        when(invocation5.getMock()).thenReturn(null);
        when(invocation5.getMethod()).thenReturn(null);
        when(invocation5.getMock()).thenReturn(genericMetadataSupport5);
        when(container4.getInvocationForStubbing()).thenReturn(invocation5);
        when(invocation5.matches(null)).thenReturn(true);
        StubbedInvocationMatcher stubbedInvocationMatcher5 = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher5.answer(invocation5)).thenReturn(null);
        when(container4.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher5});
        Object result5 = returnsDeepStubs.answer(invocation5);
        assertNull(result5);

        // Test case 6
        InvocationOnMock invocation6 = mock(InvocationOnMock.class);
        GenericMetadataSupport genericMetadataSupport6 = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport6.rawType()).thenReturn(Object.class);
        when(invocation6.getMock()).thenReturn(null);
        when(invocation6.getMethod()).thenReturn(null);
        when(invocation6.getMock()).thenReturn(genericMetadataSupport6);
        Object mock6 = mock(Object.class);
        when(mockitoCore3.mock(Object.class, withSettingsUsing(genericMetadataSupport6))).thenReturn(mock6);
        Object result6 = returnsDeepStubs.answer(invocation6);
        assertSame(mock6, result6);
    }
}