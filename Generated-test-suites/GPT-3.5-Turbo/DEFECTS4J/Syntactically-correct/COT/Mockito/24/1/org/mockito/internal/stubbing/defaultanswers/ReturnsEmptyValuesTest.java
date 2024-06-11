import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = createMockInvocation();

        Object result = returnsEmptyValues.answer(invocation);

        // Assert expected behavior
        // ...
    }

    @Test
    public void testReturnValueFor() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> type = DummyClass.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        // Assert expected behavior
        // ...
    }

    // Helper method to create a mock InvocationOnMock object
    private InvocationOnMock createMockInvocation() {
        // Implementation details
        // ...
    }
}