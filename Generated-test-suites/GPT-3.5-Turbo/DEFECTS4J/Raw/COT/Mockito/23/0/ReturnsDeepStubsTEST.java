import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;
    private MockUtil mockUtil;
    private InvocationOnMock invocation;
    private GenericMetadataSupport genericMetadataSupport;
    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
        mockUtil = new MockUtil();
        invocation = mock(InvocationOnMock.class);
        genericMetadataSupport = mock(GenericMetadataSupport.class);
        mockitoCore = mock(MockitoCore.class);
        returnsDeepStubs.returnTypeGenericMetadata = genericMetadataSupport;
        returnsDeepStubs.mockitoCore = mockitoCore;
    }

    @Test
    public void testAnswer() throws Throwable {
        when(genericMetadataSupport.rawType()).thenReturn(TestClass.class);
        when(mockitoCore.isTypeMockable(TestClass.class)).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock(TestClass.class));

        returnsDeepStubs.answer(invocation);

        // Verify the necessary interactions with mock objects
        verify(genericMetadataSupport).rawType();
        verify(mockitoCore).isTypeMockable(TestClass.class);
        verify(invocation).getMock();
    }

    @Test
    public void testGetMock() throws Throwable {
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Object mock = mock(Object.class);

        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mock);

        returnsDeepStubs.getMock(invocation, genericMetadataSupport);

        // Verify the necessary interactions with mock objects
        verify(mockUtil).getMockHandler(invocation.getMock());
        verify(handler).getInvocationContainer();
        verify(container).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
        verify(stubbedInvocationMatcher).answer(invocation);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        when(genericMetadataSupport.rawType()).thenReturn(TestClass.class);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class[]{ExtraInterface.class});

        returnsDeepStubs.createNewDeepStubMock(genericMetadataSupport);

        // Verify the necessary interactions with mock objects
        verify(genericMetadataSupport).rawType();
        verify(genericMetadataSupport).rawExtraInterfaces();
    }

    @Test
    public void testWithSettingsUsing() {
        when(genericMetadataSupport.rawType()).thenReturn(TestClass.class);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class[]{ExtraInterface.class});

        returnsDeepStubs.withSettingsUsing(genericMetadataSupport);

        // Verify the necessary interactions with mock objects
        verify(genericMetadataSupport).rawType();
        verify(genericMetadataSupport).rawExtraInterfaces();
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs innerReturnsDeepStubs = mock(ReturnsDeepStubs.class);

        when(genericMetadataSupport.rawType()).thenReturn(TestClass.class);
        when(innerReturnsDeepStubs.actualParameterizedType(any())).thenReturn(genericMetadataSupport);

        ReturnsDeepStubs outerReturnsDeepStubs = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);
        outerReturnsDeepStubs.actualParameterizedType(mock(Object.class));

        // Verify the necessary interactions with mock objects
        verify(genericMetadataSupport).rawType();
        verify(innerReturnsDeepStubs).actualParameterizedType(any());
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);
        Answer<Object> answer = mock(Answer.class);

        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(mockUtil);
        when(mockUtil.getMockSettings()).thenReturn(mock(MockitoCore.CreationSettings.class));
        when(mockUtil.getMockSettings().getTypeToMock()).thenReturn(TestClass.class);
        when(answer.answer(invocation)).thenReturn(mock);
        when(container.addAnswer(answer, false)).thenReturn(container);

        returnsDeepStubs.recordDeepStubMock(mock, container);

        // Verify the necessary interactions with mock objects
        verify(mockUtil).getMockHandler(invocation.getMock());
        verify(mockUtil).getMockSettings();
        verify(mockUtil.getMockSettings()).getTypeToMock();
        verify(container).addAnswer(answer, false);
    }
    
    private interface ExtraInterface {}
    
    private static class TestClass implements ExtraInterface {}

}