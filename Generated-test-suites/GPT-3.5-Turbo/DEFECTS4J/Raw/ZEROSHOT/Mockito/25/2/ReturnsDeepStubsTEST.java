import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;
import org.mockito.MockUtil;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;
    private InvocationOnMock invocation;
    private InvocationContainerImpl container;
    
    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        invocation = mock(InvocationOnMock.class);
        container = mock(InvocationContainerImpl.class);
    }
    
    @Test
    public void testAnswer() throws Throwable {
        when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(mock(StubbedInvocationMatcher.class)));

        Object result = returnsDeepStubs.answer(invocation);

        verify(invocation, times(1)).getMock();
        verify(container, times(1)).getInvocationForStubbing();
        verify(container, times(1)).getStubbedInvocations();

        assertNotNull(result);
    }

    @Test
    public void testGetMock() throws Throwable {
        when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(mock(StubbedInvocationMatcher.class)));

        Object result = returnsDeepStubs.getMock(invocation);

        verify(invocation, times(1)).getMock();
        verify(container, times(1)).getInvocationForStubbing();
        verify(container, times(1)).getStubbedInvocations();

        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(null);

        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        verify(invocation, times(1)).getMethod();
        verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());

        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = Mockito.mock(Object.class);
        CreationSettings mockSettings = Mockito.mock(CreationSettings.class);

        when(MockUtil.getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        assertNotNull(result);
    }
}