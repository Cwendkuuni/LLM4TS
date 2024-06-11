import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.mockito.MockSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.junit.Test;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Verify that actualParameterizedType method is called
        Object mock = mock(Object.class);
        when(returnsDeepStubs.actualParameterizedType(mock)).thenReturn(new GenericMetadataSupport());

        // Verify that deepStub method is called
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
        when(returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata)).thenReturn("mockedReturnValue");

        // Verify that delegate method is called
        when(returnsDeepStubs.delegate()).thenReturn(new ReturnsEmptyValues());
        
        // Verify the answer method returns the mocked return value
        Object result = returnsDeepStubs.answer(invocation);
        assertEquals("mockedReturnValue", result);
    }
}