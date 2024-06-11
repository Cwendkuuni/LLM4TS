import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReturnsEmptyValuesTest {
    @Test
    void testAnswerToStringMethod() {
        Object mock = Mockito.mock(String.class);
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);
        Mockito.when(answer.answer(Mockito.any(InvocationOnMock.class))).then(answer);

        String result = (String) answer.answer(Mockito.argumentThat(invocation -> methodsGuru.isToString(invocation.getMethod())));
        Assertions.assertEquals("Mock for [class java.lang.String], hashCode: " + mock.hashCode(), result);
    }

    @Test
    void testAnswerCompareToMethod() {
        Object mock = Mockito.mock(Comparable.class);
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);
        Mockito.when(answer.answer(Mockito.any(InvocationOnMock.class))).then(answer);

        int comparisonResult = (Integer) answer.answer(Mockito.argumentThat(invocation -> methodsGuru.isCompareToMethod(invocation.getMethod())));
        Assertions.assertEquals(0, comparisonResult);
    }

    @Test
    void testAnswerNonMatchingMethodSignature() {
        Object mock = Mockito.mock(Answer.class);
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);
        Mockito.when(answer.answer(Mockito.any(InvocationOnMock.class))).then(answer);

        Assertions.assertThrows(IllegalArgumentException.class, () -> answer.answer(Mockito.mock(Object.class)));
    }
}