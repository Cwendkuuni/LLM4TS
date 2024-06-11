import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Mock
    private SomeClass someMock;

    @Test
    public void testAnswerReturnsPreviouslyCreatedMock() {
        // Arrange
        SomeClass nestedMock = mock(SomeClass.class);
        when(someMock.getNestedMock()).thenReturn(nestedMock);

        // Act
        SomeClass result = someMock.getNestedMock();

        // Assert
        assertSame(nestedMock, result);
    }

    @Test
    public void testAnswerReturnsReturnValueFromNestedMock() {
        // Arrange
        SomeClass nestedMock = mock(SomeClass.class);
        when(nestedMock.getValue()).thenReturn("Test value");
        when(someMock.getNestedMock()).thenReturn(nestedMock);

        // Act
        String result = someMock.getNestedMock().getValue();

        // Assert
        assertEquals("Test value", result);
    }

    @Test
    public void testAnswerReturnsDefaultValueForNonMockableType() {
        // Arrange
        SomeClass nestedMock = mock(SomeClass.class);
        when(someMock.getNonMockableType()).thenReturn(nestedMock);

        // Act
        SomeClass result = someMock.getNonMockableType();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof SomeClass);
    }

    @Test
    public void testAnswerReturnsDefaultValueForInvalidInvocation() {
        // Arrange
        SomeClass nestedMock = mock(SomeClass.class);
        when(someMock.getNestedMock()).thenReturn(nestedMock);

        // Act
        SomeClass result = someMock.getInvalidMethod();

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnswerReturnsDefaultValueForNullReturnValue() {
        // Arrange
        SomeClass nestedMock = mock(SomeClass.class);
        when(someMock.getNestedMock()).thenReturn(null);

        // Act
        SomeClass result = someMock.getNestedMock();

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnswerReturnsReturnValueFromStubbedInvocation() {
        // Arrange
        SomeClass nestedMock = mock(SomeClass.class);
        when(someMock.getNestedMock()).thenReturn(nestedMock);

        // Act
        String result = someMock.getNestedMock().getString();

        // Assert
        assertNull(result);
    }

    private static class SomeClass {
        public SomeClass getNestedMock() {
            return null;
        }

        public String getValue() {
            return null;
        }

        public SomeClass getNonMockableType() {
            return null;
        }

        public String getString() {
            return null;
        }

        public SomeClass getInvalidMethod() {
            return null;
        }
    }
}