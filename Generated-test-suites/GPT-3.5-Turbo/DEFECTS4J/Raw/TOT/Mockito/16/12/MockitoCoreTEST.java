import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock_returnsMockObject() {
        ExampleClass exampleMock = mockitoCore.mock(ExampleClass.class);
        assertNotNull(exampleMock);
    }

    @Test
    public void testMock_withMockSettings_returnsMockObject() {
        MockSettings mockSettings = Mockito.withSettings();
        ExampleClass exampleMock = mockitoCore.mock(ExampleClass.class, mockSettings);
        assertNotNull(exampleMock);
    }

    @Test(expected = NotAMockException.class)
    public void testVerify_notAMockObject_throwsNotAMockException() {
        ExampleClass example = new ExampleClass();
        mockitoCore.verify(example, times(1));
    }

    @Test
    public void testVerify_mockObject_callsVerify() {
        ExampleClass exampleMock = mockitoCore.mock(ExampleClass.class);
        mockitoCore.verify(exampleMock, times(1));
    }

    @Test
    public void testReset_resetsMocks() {
        ExampleClass exampleMock = mockitoCore.mock(ExampleClass.class);
        mockitoCore.reset(exampleMock);
        verifyNoMoreInteractions(exampleMock);
    }

    @Test
    public void testReset_multipleMocks_resetsMocks() {
        ExampleClass exampleMock1 = mockitoCore.mock(ExampleClass.class);
        ExampleClass exampleMock2 = mockitoCore.mock(ExampleClass.class);
        mockitoCore.reset(exampleMock1, exampleMock2);
        verifyNoMoreInteractions(exampleMock1);
        verifyNoMoreInteractions(exampleMock2);
    }

    @Test
    public void testVerifyNoMoreInteractions_noInteractions_callsVerifyNoMoreInteractions() {
        ExampleClass exampleMock = mockitoCore.mock(ExampleClass.class);
        mockitoCore.verifyNoMoreInteractions(exampleMock);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_notAMockObject_throwsNotAMockException() {
        ExampleClass example = new ExampleClass();
        mockitoCore.verifyNoMoreInteractions(example);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNoMoreInteractions_nullObject_throwsNullPointerException() {
        mockitoCore.verifyNoMoreInteractions((ExampleClass) null);
    }

    @Test
    public void testInOrder_validMocks_returnsInOrderObject() {
        ExampleClass exampleMock1 = mockitoCore.mock(ExampleClass.class);
        ExampleClass exampleMock2 = mockitoCore.mock(ExampleClass.class);
        InOrder inOrder = mockitoCore.inOrder(exampleMock1, exampleMock2);
        assertNotNull(inOrder);
    }

    @Test(expected = NotAMockException.class)
    public void testInOrder_notAMockObject_throwsNotAMockException() {
        ExampleClass example = new ExampleClass();
        mockitoCore.inOrder(example);
    }

    @Test(expected = NullPointerException.class)
    public void testInOrder_nullObject_throwsNullPointerException() {
        mockitoCore.inOrder((ExampleClass) null);
    }

    @Test(expected = NullPointerException.class)
    public void testDoAnswer_nullAnswer_throwsNullPointerException() {
        Answer answer = null;
        mockitoCore.doAnswer(answer);
    }

    @Test
    public void testStubVoid_validMock_returnsVoidMethodStubbable() {
        ExampleClass exampleMock = mockitoCore.mock(ExampleClass.class);
        VoidMethodStubbable<Void> stubbable = mockitoCore.stubVoid(exampleMock);
        assertNotNull(stubbable);
    }

    @Test(expected = NotAMockException.class)
    public void testStubVoid_notAMockObject_throwsNotAMockException() {
        ExampleClass example = new ExampleClass();
        mockitoCore.stubVoid(example);
    }

    @Test(expected = NullPointerException.class)
    public void testStubVoid_nullObject_throwsNullPointerException() {
        mockitoCore.stubVoid((ExampleClass) null);
    }

    @Test
    public void testValidateMockitoUsage_validCalls_doesNotThrowException() {
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation_validStubbing_returnsLastInvocation() {
        ExampleClass exampleMock = mockitoCore.mock(ExampleClass.class);
        when(exampleMock.method()).thenReturn("test");
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        assertNotNull(lastInvocation);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLastInvocation_noStubbing_throwsNullPointerException() {
        mockitoCore.getLastInvocation();
    }

}

class ExampleClass {
    public String method() {
        return null;
    }
}