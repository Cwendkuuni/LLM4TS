import org.junit.jupiter.api.*;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @BeforeAll
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testToStringMethod() {
        // given
        Object mock = new Object();
        InvocationOnMock invocation = Mockito.mock(String.class).invocationOnMock(Mockito.methodCalled("toString"));

        // when
        String result = (String) returnsEmptyValues.answer(invocation);

        // then
        Assertions.assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToMethod() {
        // given
        Object mock1 = new Object();
        Object mock2 = new Object();

        InvocationOnMock invocation = Mockito.mock(Comparable.class).invocationOnMock(Mockito.methodCalled("compareTo"));
        invocation.setArguments(mock1, mock2);

        // when
        int result = (int) returnsEmptyValues.answer(invocation);

        // then
        Assertions.assertNotEquals(0, result);
    }

    @Test
    public void testReturnValueForPrimitivesOrWrappers() {
        // given
        InvocationOnMock invocation = Mockito.mock(Number.class).invocationOnMock(Mockito.methodCalled("doubleValue"));

        // when
        Double result = (Double) returnsEmptyValues.answer(invocation);

        // then
        Assertions.assertEquals(0.0, result);
    }

    // Add test cases for other return types
}