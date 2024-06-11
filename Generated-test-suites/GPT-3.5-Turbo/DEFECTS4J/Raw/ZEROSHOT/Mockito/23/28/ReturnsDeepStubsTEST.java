import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);
        // assert result
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();
        Object result = returnsDeepStubs.getMock(invocation, genericMetadataSupport);
        // assert result
    }

    @Test
    public void testCreateNewDeepStubMock() {
        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();
        Object result = returnsDeepStubs.createNewDeepStubMock(genericMetadataSupport);
        // assert result
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(genericMetadataSupport);
        // assert mockSettings
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();
        ReturnsDeepStubs returnsDeepStubs = this.returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);
        // assert returnsDeepStubs
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Object mock = new Object();
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);
        // assert result
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = Mockito.mock(Object.class);
        GenericMetadataSupport genericMetadataSupport = returnsDeepStubs.actualParameterizedType(mock);
        // assert genericMetadataSupport
    }
}