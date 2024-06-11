
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();

        // Test 1: Valid invocation
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        Object result1 = deepStubs.answer(invocation1);
        assertNotNull(result1);

        // Test 2: Invalid invocation
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation2.getMock()).thenReturn(null);
        Object result2 = deepStubs.answer(invocation2);
        assertNull(result2);
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();

        // Test 1: Valid invocation
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata1 = new GenericMetadataSupport();
        returnTypeGenericMetadata1.setRawType(String.class);
        Object result1 = deepStubs.getMock(invocation1, returnTypeGenericMetadata1);
        assertNotNull(result1);

        // Test 2: Invalid raw type
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata2 = new GenericMetadataSupport();
        returnTypeGenericMetadata2.setRawType(null);
        Object result2 = deepStubs.getMock(invocation2, returnTypeGenericMetadata2);
        assertNull(result2);
    }

    @Test
    public void testCreateNewDeepStubMock() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();

        // Test 1: Valid metadata
        GenericMetadataSupport returnTypeGenericMetadata1 = new GenericMetadataSupport();
        returnTypeGenericMetadata1.setRawType(String.class);
        Object result1 = deepStubs.createNewDeepStubMock(returnTypeGenericMetadata1);
        assertNotNull(result1);

        // Test 2: Invalid metadata
        GenericMetadataSupport returnTypeGenericMetadata2 = new GenericMetadataSupport();
        returnTypeGenericMetadata2.setRawType(null);
        Object result2 = deepStubs.createNewDeepStubMock(returnTypeGenericMetadata2);
        assertNull(result2);
    }

    @Test
    public void testWithSettingsUsing() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();

        // Test 1: Valid metadata with extra interfaces
        GenericMetadataSupport returnTypeGenericMetadata1 = new GenericMetadataSupport();
        returnTypeGenericMetadata1.setRawType(String.class);
        returnTypeGenericMetadata1.setRawExtraInterfaces(new Class[] {Serializable.class});
        Object result1 = deepStubs.withSettingsUsing(returnTypeGenericMetadata1);
        assertNotNull(result1);

        // Test 2: Valid metadata without extra interfaces
        GenericMetadataSupport returnTypeGenericMetadata2 = new GenericMetadataSupport();
        returnTypeGenericMetadata2.setRawType(String.class);
        returnTypeGenericMetadata2.setRawExtraInterfaces(new Class[0]);
        Object result2 = deepStubs.withSettingsUsing(returnTypeGenericMetadata2);
        assertNotNull(result2);

        // Test 3: Invalid metadata
        GenericMetadataSupport returnTypeGenericMetadata3 = new GenericMetadataSupport();
        returnTypeGenericMetadata3.setRawType(null);
        returnTypeGenericMetadata3.setRawExtraInterfaces(new Class[0]);
        Object result3 = deepStubs.withSettingsUsing(returnTypeGenericMetadata3);
        assertNull(result3);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();

        // Test 1: Valid metadata
        GenericMetadataSupport returnTypeGenericMetadata1 = new GenericMetadataSupport();
        returnTypeGenericMetadata1.setRawType(String.class);
        Object result1 = deepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata1);
        assertNotNull(result1);

        // Test 2: Invalid metadata
        GenericMetadataSupport returnTypeGenericMetadata2 = new GenericMetadataSupport();
        returnTypeGenericMetadata2.setRawType(null);
        Object result2 = deepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata2);
        assertNull(result2);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();

        // Test 1: Valid mock
        Object mock1 = new Object();
        InvocationContainerImpl container1 = new InvocationContainerImpl();
        Object result1 = deepStubs.recordDeepStubMock(mock1, container1);
        assertNotNull(result1);

        // Test 2: Invalid mock
        Object mock2 = null;
        InvocationContainerImpl container2 = new InvocationContainerImpl();
        Object result2 = deepStubs.recordDeepStubMock(mock2, container2);
        assertNull(result2);
    }

    @Test
    public void testActualParameterizedType() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();

        // Test 1: Valid mock
        Object mock1 = Mockito.mock(Object.class);
        GenericMetadataSupport result1 = deepStubs.actualParameterizedType(mock1);
        assertNotNull(result1);

        // Test 2: Invalid mock
        Object mock2 = null;
        GenericMetadataSupport result2 = deepStubs.actualParameterizedType(mock2);
        assertNull(result2);
    }
}

