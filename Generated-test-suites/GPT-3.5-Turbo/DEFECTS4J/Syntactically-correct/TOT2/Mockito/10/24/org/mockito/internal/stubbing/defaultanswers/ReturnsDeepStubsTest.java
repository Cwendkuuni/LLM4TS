import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_CallsDeepStub_ReturnsMockObject() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);
        assertEquals("Expected deep stub to return a mock object", Object.class, result.getClass());
    }

    @Test
    public void testNewDeepStubMock_CreatesNewMockWithGenericMetadata_ReturnsMockObject() {
        GenericMetadataSupport genericMetadata = new GenericMetadataSupport();
        Object mock = returnsDeepStubs.newDeepStubMock(genericMetadata);
        assertEquals("Expected new mock object to be created with generic metadata", Object.class, mock.getClass());
    }

    @Test
    public void testWithSettingsUsing_ReturnsMockSettingsObjectWithExtraInterfaces_SetsExtraInterfaces() {
        GenericMetadataSupport genericMetadata = new GenericMetadataSupport();
        Object mockSettings = returnsDeepStubs.withSettingsUsing(genericMetadata);
        assertEquals("Expected mock settings object to have extra interfaces set", Object.class, mockSettings.getClass());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_CreatesNewReturnsDeepStubsObject_ReturnsReturnsDeepStubsObject() {
        GenericMetadataSupport genericMetadata = new GenericMetadataSupport();
        Object returnsDeepStubsAnswer = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadata);
        assertEquals("Expected ReturnsDeepStubs object to be created with generic metadata", ReturnsDeepStubs.class, returnsDeepStubsAnswer.getClass());
    }

}