import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Mockito mockito;

 @Before
 public void setup() {
 mockito = Mockito.mock(Answer.class);
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 @Test
 public void testReturnsSmartNullWhenMethodIsNotToString() {
 // Given
 Object mock = mock(String.class, returnsSmartNulls);

 // When
 String result = (String) mock.toString();

 // Then
 verify(mockito).smartNullPointerException(ArgumentMatchers.any(Location.class));
 }

 @Test
 public void testReturnsSmartNullWhenMethodIsToString() {
 // Given
 Object mock = mock(String.class, returnsSmartNulls);

 // When
 String result = mock.toString();

 // Then
 assertEquals("SmartNull returned by unstubbed toString method on mock", result);
 }

 @Test
 public void testReturnsDefaultValueWhenMethodIsMockable() {
 // Given
 Object mock = mock(Integer.class, returnsSmartNulls);

 // When
 int result = (int) mock.hashCode();

 // Then
 verify(mockito, never()).smartNullPointerException(ArgumentMatchers.any(Location.class));
 assertEquals(0, result);
 }

 @Test(expected = NullPointerException.class)
 public void testThrowsNPEWhenMethodIsNotMockable() {
 // Given
 Object mock = mock(String.class, returnsSmartNulls);

 // When & Then
 // NPE is thrown and caught by smartNullPointerException method
 }
}