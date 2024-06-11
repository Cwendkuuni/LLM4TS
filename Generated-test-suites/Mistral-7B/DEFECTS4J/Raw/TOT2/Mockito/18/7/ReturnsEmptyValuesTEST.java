import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

  @Test
  public void testAnswerToStringMethod() {
    ReturnsEmptyValues emptyValues = mock(ReturnsEmptyValues.class);
    MockName mockName = mock(MockName.class);
    when(mockName.toString()).thenReturn("MockName");
    when(emptyValues.answer(any(InvocationOnMock.class))).thenReturn(mockName);
    assertEquals("MockName", emptyValues.answer(any(InvocationOnMock.class)));
  }

  @Test
  public void testAnswerCompareToMethod() {
    ReturnsEmptyValues emptyValues = mock(ReturnsEmptyValues.class);
    Object mock = new Object();
    when(emptyValues.answer(any(InvocationOnMock.class))).thenReturn(mock);
    int comparisonResult = emptyValues.answer(eq(mock)).compareTo(mock);
    assertEquals(0, comparisonResult);
  }
}